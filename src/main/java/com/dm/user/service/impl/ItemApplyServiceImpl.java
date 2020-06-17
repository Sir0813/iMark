package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.ItemApplyMapper;
import com.dm.user.msg.*;
import com.dm.user.service.*;
import com.dm.user.util.Arith;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemApplyServiceImpl implements ItemApplyService {

    @Autowired
    private ItemApplyMapper itemApplyMapper;

    @Autowired
    private OrgItemService orgItemService;

    @Autowired
    private ItemRequeredService itemRequeredService;

    @Autowired
    private UserCardService userCardService;

    @Autowired
    private ItemApplyFilesService itemApplyFilesService;

    @Autowired
    private CertFilesService certFilesService;

    @Autowired
    private WfInstanceService wfInstanceService;

    @Autowired
    private WfItemNodeService wfItemNodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgUserService orgUserService;

    @Autowired
    private WfInstAuditTrackService wfInstAuditTrackService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private PushMsgService pushMsgService;

    @Autowired
    private BizItemVideoService bizItemVideoService;

    @Autowired
    private PubArticleService pubArticleService;

    @Autowired
    private ItemApplyLogService itemApplyLogService;

    @Autowired
    private ApplyExpandService applyExpandService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private ItemChargeService itemChargeService;

    @Autowired
    private ChargeDetailService chargeDetailService;

    @Autowired
    private ApplyFileLogService applyFileLogService;

    @Autowired
    private ApplySupplementService applySupplementService;

    private List<ChargeDetail> addChargeDetail(ItemApply itemApply) {
        List<ChargeDetail> itemCharges = new ArrayList<>();
        ItemCharge prePay = itemChargeService.selectByName("公证预付款");
        ChargeDetail preCharge = new ChargeDetail();
        preCharge.setPrice(itemApply.getPrice());
        preCharge.setPayStatus(0);
        preCharge.setChargeId(prePay.getId());
        preCharge.setApplyId(itemApply.getApplyid());
        preCharge.setCratedDate(new Date());
        preCharge.setCreatedId(Integer.parseInt(LoginUserHelper.getUserId()));
        preCharge.setIsDel(0);
        preCharge.setIsPay(0);
        if (itemApply.getApplyExpand().getFileNum() > 1) {
            ItemCharge filePay = itemChargeService.selectByName("副本费");
            ChargeDetail fileCharge = new ChargeDetail();
            fileCharge.setPrice(BigDecimal.valueOf(StateMsg.FILE_FEE).multiply(BigDecimal.valueOf(itemApply.getApplyExpand().getFileNum() - 1)).doubleValue());
            fileCharge.setPayStatus(0);
            fileCharge.setChargeId(filePay.getId());
            fileCharge.setApplyId(itemApply.getApplyid());
            fileCharge.setCratedDate(new Date());
            fileCharge.setCreatedId(Integer.parseInt(LoginUserHelper.getUserId()));
            fileCharge.setIsDel(0);
            fileCharge.setIsPay(0);
            fileCharge.setFileNum(itemApply.getApplyExpand().getFileNum());
            itemCharges.add(fileCharge);
        }
        itemCharges.add(preCharge);
        return itemCharges;
    }

    @Override
    public Result insert(ItemApply itemApply) throws Exception {
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
        Map<String, Object> map = new HashMap<>(16);
        if (null == itemApply.getApplyid()) {
            SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssSSS");
            String format1 = format.format(new Date());
            itemApply.setApplyNo(format1);
            itemApply.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
            itemApply.setItemCode(orgItems.getItemCode());
            itemApply.setCreatedDate(new Date());
            itemApplyMapper.insertSelective(itemApply);
            ApplyExpand applyExpand = itemApply.getApplyExpand();
            applyExpand.setApplyId(itemApply.getApplyid());
            applyExpandService.insert(applyExpand);
        } else {
            chargeDetailService.deleteByApplyId(itemApply.getApplyid());
            applyExpandService.update(itemApply.getApplyExpand());
        }
        List<ChargeDetail> chargeDetails = addChargeDetail(itemApply);
        if (orgItems.getValuation() == OrgItemEnum.CUSTOM_PRICE.getCode()) {
            /* 公正人员自定义价格 需要输入尾款 */
            ItemCharge endPay = itemChargeService.selectByName("公证尾款");
            ChargeDetail endCharge = new ChargeDetail();
            endCharge.setPayStatus(0);
            endCharge.setChargeId(endPay.getId());
            endCharge.setApplyId(itemApply.getApplyid());
            endCharge.setCratedDate(new Date());
            endCharge.setCreatedId(Integer.parseInt(LoginUserHelper.getUserId()));
            endCharge.setIsDel(0);
            endCharge.setIsPay(0);
            chargeDetails.add(endCharge);
        }
        if (itemApply.getItemValue() > 0) {
            /* 按标的总共应收金额 **/
            BigDecimal multiply = Arith.mul(itemApply.getItemValue(), orgItems.getPrice());
            if (multiply.compareTo(BigDecimal.valueOf(orgItems.getLowestPrice())) > -1) {
                BigDecimal subtract = multiply.subtract(BigDecimal.valueOf(itemApply.getPrice()));
                ItemCharge endPay = itemChargeService.selectByName("公证尾款");
                ChargeDetail endCharge = new ChargeDetail();
                endCharge.setPrice(subtract.doubleValue());
                endCharge.setPayStatus(0);
                endCharge.setChargeId(endPay.getId());
                endCharge.setApplyId(itemApply.getApplyid());
                endCharge.setCratedDate(new Date());
                endCharge.setCreatedId(Integer.parseInt(LoginUserHelper.getUserId()));
                endCharge.setIsDel(0);
                endCharge.setIsPay(0);
                chargeDetails.add(endCharge);
                /* 按标的应收金额大于等于最低收费金额 **/
                itemApply.setPrice(multiply.doubleValue());
            } else {
                itemApply.setPrice(orgItems.getLowestPrice());
            }
        }
        chargeDetailService.insertList(chargeDetails);
        List<ApplyFile> applyFileList = itemApply.getApplyFileList();
        for (int i = 0; i < applyFileList.size(); i++) {
            String requeredid = applyFileList.get(i).getRequeredid();
            String[] split = applyFileList.get(i).getFileid().split(",");
            itemApplyFilesService.deleteByApplyIdAndRequeredId(itemApply.getApplyid(), requeredid);
            for (int j = 0; j < split.length; j++) {
                ItemApplyFiles itemApplyFiles = new ItemApplyFiles();
                String s = split[j];
                if (StringUtils.isNotBlank(s)) {
                    itemApplyFiles.setApplyid(itemApply.getApplyid());
                    itemApplyFiles.setCreatedDate(new Date());
                    itemApplyFiles.setFileid(Integer.parseInt(s));
                    itemApplyFiles.setRequeredid(Integer.parseInt(requeredid));
                    itemApplyFiles.setFileTypes(ItemFileTypeEnum.USER_FILE.getCode());
                    itemApplyFiles.setIsDel(ItemFileTypeEnum.FILE_EXISTENCE.getCode());
                    itemApplyFilesService.insert(itemApplyFiles);
                }
            }
        }
        if (itemApply.getStatus() == ItemApplyEnum.WAIT_PAY.getCode()) {
            itemApply.setSubmitDate(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(itemApply.getSubmitDate());
            map.put("submitDate", format);
            map.put("applyid", itemApply.getApplyid());
        }
        itemApplyMapper.updateByPrimaryKeySelective(itemApply);
        return ResultUtil.success(map);
    }

    @Override
    public PageInfo<ItemApply> list(int pageNum, int type, String word) throws Exception {
        if (type == 4) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        switch (type) {
            case 0: /* 全部数据 */
                break;
            case 1: /* 草稿公正 */
                map.put("state", ItemApplyEnum.FILE_DRAFT.getCode());
                break;
            case 2: /* 已递交公正 */
                map.put("state", ItemApplyEnum.FILE_RECEPTION.getCode());
                break;
            case 3: /* 已完成公正 */
                map.put("states", new int[]{ItemApplyEnum.APPLY_SUCCESS.getCode(), ItemApplyEnum.APPLY_FAIL.getCode(), ItemApplyEnum.REJECT_REASON.getCode()});
                break;
            case 4: /* 我的预约后续加入 */
                break;
        }
        if (StringUtils.isNotBlank(word)) {
            map.put("word", word);
        }
        map.put("userId", LoginUserHelper.getUserId());
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<ItemApply> itemApplyList = itemApplyMapper.list(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> detail(int applyid) throws Exception {
        Map<String, Object> applyMap = new LinkedHashMap<>(16);
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
        List<ItemRequered> list = itemRequeredService.selectByRequeredIdAndApplyId(applyid);
        boolean otherFile = false;
        /* 用户提交的文件清单 **/
        for (int i = 0; i < list.size(); i++) {
            ItemRequered itemRequered = list.get(i);
            if (itemRequered.getName().equals("其他材料")) {
                otherFile = true;
            }
            Map<String, Object> m = new LinkedHashMap<>(16);
            m.put("applyid", applyid);
            m.put("requeredid", itemRequered.getRequeredid());
            m.put("fileTypes", ItemFileTypeEnum.USER_FILE.getCode());
            List<ItemApplyFiles> itemApplyFilesList = itemApplyFilesService.selectByApplyIdAndRequeredId(m);
            if (itemApplyFilesList.size() > 0) {
                String[] ids = new String[itemApplyFilesList.size()];
                for (int j = 0; j < itemApplyFilesList.size(); j++) {
                    ItemApplyFiles itemApplyFiles = itemApplyFilesList.get(j);
                    Integer fileid = itemApplyFiles.getFileid();
                    ids[j] = fileid.toString();
                }
                List<CertFiles> cfList = certFilesService.findByFilesIds2(ids);
                itemRequered.setCertFilesList(cfList);
            }
        }
        if (!otherFile) {
            ItemRequered itemRequered = itemRequeredService.selectOtherFile(list.get(0).getItemid());
            list.add(itemRequered);
        }
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("applyid", applyid);
        map.put("userId", LoginUserHelper.getUserId());
        ApplyUserInfo applyInfo = itemApplyMapper.selectDetailInfo(map);
        /*OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
        if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
            *//* 固定价格计费 返回本次公正所需费用 **//*
            applyInfo.setPrice(orgItems.getPrice());
        } else {
            *//* 按标的 或公正人员自定义先缴纳最低价格 **//*
            applyInfo.setPrice(orgItems.getLowestPrice());
        }*/
        ApplyExpand applyExpand = applyExpandService.selectByApplyId(applyid);
        if (applyExpand.getAddressId() == 0) {
            /* 自取 */
            applyInfo.setIsSend(0);
        } else {
            /* 邮寄 */
            applyInfo.setIsSend(1);
            UserAddress userAddress = userAddressService.selectById(applyExpand.getAddressId());
            applyInfo.setUserAddress(userAddress);
        }
        Org org = orgService.selectByApplyId(applyid);
        applyInfo.setOrgAddress(org.getAddress());
        applyMap.put("allNode", getAllNode());
        applyMap.put("historyNode", historyNode(applyid));
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
        if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
            applyInfo.setPrice(orgItems.getPrice());
        } else {
            applyInfo.setPrice(orgItems.getLowestPrice());
        }
        List<ApplyFee> applyFees = chargeDetailService.selectByApplyId(applyid);
        /* 补充材料 */
        List<CertFiles> addFiles = certFilesService.selectIsAddFiles(applyid);
        /* 修改材料 */
        List<CertFiles> updateFiles = certFilesService.selectIsUpdateFiles(applyid);
        /* 收费项 */
        List<ApplyFee> applyFee = chargeDetailService.selectByApplyIdAndStatus(applyid);
        applyMap.put("isPay", applyFee.size() == 0 ? "" : applyFee.get(0));
        if (itemApply.getPayStatus() == ItemApplyEnum.PAY_ALL.getCode()) {
            List<ApplyFee> applyFeeList = chargeDetailService.selectByApplyIdAndStatus(applyid);
            applyFeeList.remove(0);
            applyMap.put("applyFeeList", applyFeeList);
        } else {
            List<ApplyFee> applyFeeList = chargeDetailService.selectByApplyId(applyid);
            applyMap.put("applyFeeList", applyFeeList);
        }
        applyMap.put("orgName", org.getOrgname());
        applyMap.put("updateFiles", updateFiles);
        applyMap.put("addFiles", addFiles);
        applyMap.put("applyFees", applyFees);
        applyMap.put("payEndPrice", itemApply.getPayEndPrice());
        applyMap.put("applyInfo", applyInfo);
        applyMap.put("itemRequered", list);
        applyMap.put("applyExpand", applyExpand);
        return applyMap;
    }

    /*@Override
    public Map<String, Object> detail(int applyid) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        Map<String, Object> userMap = new LinkedHashMap<>(16);
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
        Org org = orgService.selectById(orgItems.getOrgid());
        List<ItemRequered> list = itemRequeredService.selectByItemId(itemApply.getItemid());
        UserCard userCard = userCardService.selectByUserIdAndStatus(itemApply.getUserid());
        AppUser user = userService.selectByPrimaryKey(itemApply.getUserid());
        *//* 用户提交的文件清单 **//*
        for (int i = 0; i < list.size(); i++) {
            ItemRequered itemRequered = list.get(i);
            Map<String, Object> m = new LinkedHashMap<>(16);
            m.put("applyid", applyid);
            m.put("requeredid", itemRequered.getRequeredid());
            m.put("fileTypes", ItemFileTypeEnum.USER_FILE.getCode());
            List<ItemApplyFiles> itemApplyFilesList = itemApplyFilesService.selectByApplyIdAndRequeredId(m);
            if (itemApplyFilesList.size() > 0) {
                String[] ids = new String[itemApplyFilesList.size()];
                for (int j = 0; j < itemApplyFilesList.size(); j++) {
                    ItemApplyFiles itemApplyFiles = itemApplyFilesList.get(j);
                    Integer fileid = itemApplyFiles.getFileid();
                    ids[j] = fileid.toString();
                }
                List<CertFiles> cfList = certFilesService.findByFilesIds2(ids);
                itemRequered.setCertFilesList(cfList);
            }
        }
        WfInstance wfInstance = new WfInstance();
        *//*if (ItemApplyEnum.REVIEW_NO.getCode() == itemApply.getStatus()) {
            wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
        }*//*
        if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
            *//* 固定价格计费 返回本次公正所需费用 **//*
            map.put("price", orgItems.getPrice());
        } else {
            *//* 按标的 或公正人员自定义先缴纳最低价格 **//*
            map.put("price", orgItems.getLowestPrice());
        }
        CertFiles certFiles = null;
        List<ApplyHistory> applyHistories = null;
        *//*if (itemApply.getStatus() != ItemApplyEnum.DRAFT.getCode()) {
            Map<String, Object> fileMap = new LinkedHashMap<>(16);
            fileMap.put("applyId", applyid);
            fileMap.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(fileMap);
            if (null != itemApplyFiles) {
                certFiles = certFilesService.selectByPrimaryKey(itemApplyFiles.getFileid());
            }
            *//**//* 审核历史记录 **//**//*
            applyHistories = itemApplyMapper.history(applyid);
        }
        if (itemApply.getStatus() == ItemApplyEnum.REVIEW_SUCCESS.getCode()) {
            *//**//* 审核完成显示盖章意见书 **//**//*
            Map<String, Object> map2 = new HashMap<>(16);
            map2.put("applyId", applyid);
            map2.put("state", ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(map2);
            CertFiles certFiles1 = certFilesService.selectByPrimaryKey(itemApplyFiles.getFileid());
            map.put("fileUrl", certFiles1.getFileUrl());
        }*//*
        List<BizItemVideo> bizItemVideoList = bizItemVideoService.selectByApplyId(applyid);
        ApplyExpand applyExpand = applyExpandService.selectByApplyId(applyid);
        UserAddress userAddress = userAddressService.selectByUserIdAndStatus(LoginUserHelper.getUserId(), "1");
        if (null == userAddress) {
            UserAddress userAddress1 = userAddressService.selectByUserIdAndStatus(LoginUserHelper.getUserId(), "0");
            if (null != userAddress1) {
                map.put("address", userAddress1.getReceiverAddress() + userAddress1.getReceiverDetailAddress());
            } else {
                map.put("address", null);
            }
        } else {
            map.put("address", userAddress.getReceiverAddress() + userAddress.getReceiverDetailAddress());
        }
        userMap.put("userName", null == userCard ? "" : userCard.getRealName());
        userMap.put("userPhone", user.getUsername());
        userMap.put("userCard", null == userCard ? "" : userCard.getCardNumber());
        userMap.put("userId", user.getUserid());
        map.put("applyExpand", applyExpand);
        map.put("orgName", org.getOrgname());
        map.put("itemValue", itemApply.getItemValue());
        map.put("bizItemVideoList", bizItemVideoList);
        *//* 审批历史公正意见书 (未盖章) **//*
        map.put("opinionFile", certFiles);
        map.put("handleUserId", itemApply.getHandleUserid());
        map.put("history", applyHistories);
        map.put("applyid", applyid);
        map.put("itemDesc", orgItems.getItemDesc());
        map.put("itemRequered", list);
        map.put("userInfo", userMap);
        map.put("status", itemApply.getStatus());
        map.put("applyDescribe", itemApply.getDescribe());
        map.put("reason", wfInstance.getRejectReason());
        map.put("valuation", orgItems.getValuation());
        map.put("payStatus", itemApply.getPayStatus());
        map.put("payEndPrice", itemApply.getPayEndPrice());
        //map.put("allNode", getAllNode());
        map.put("historyNode", historyNode(applyid));
        return map;
    }*/

    private List<ItemApplyLog> historyNode(int applyid) throws Exception {
        return itemApplyLogService.historyNode(applyid);
    }

    private List<ItemapplyNode> getAllNode() {
        Map<Integer, Object> map = new LinkedHashMap<>(16);
        map.put(ItemApplyEnum.FILE_RECEPTION.getCode(), ItemApplyEnum.FILE_RECEPTION.getDesc());
        map.put(ItemApplyEnum.WAIT_ORDER.getCode(), ItemApplyEnum.WAIT_ORDER.getDesc());
        map.put(ItemApplyEnum.FILE_CHECK.getCode(), ItemApplyEnum.FILE_CHECK.getDesc());
        map.put(ItemApplyEnum.FILE_REVIEW.getCode(), ItemApplyEnum.FILE_REVIEW.getDesc());
        map.put(ItemApplyEnum.FILE_MAKE.getCode(), ItemApplyEnum.FILE_MAKE.getDesc());
        map.put(ItemApplyEnum.FILE_SEND.getCode(), ItemApplyEnum.FILE_SEND.getDesc());
        map.put(ItemApplyEnum.APPLY_SUCCESS.getCode(), ItemApplyEnum.APPLY_SUCCESS.getDesc());
        List<ItemapplyNode> collect = map.entrySet().stream().map(e -> new ItemapplyNode(e.getKey(), e.getValue().toString())).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Result delete(int applyid) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("applyId", applyid);
        map.put("fileTypes", ItemFileTypeEnum.USER_FILE.getCode());
        if (itemApply.getStatus() == ItemApplyEnum.FILE_DRAFT.getCode()) {
            itemApplyFilesService.deleteByApplyIdAndFileType(map);
            itemApplyMapper.deleteByPrimaryKey(applyid);
            return ResultUtil.success();
        }
        return ResultUtil.error();
    }

    @Override
    public synchronized Result takeOrder(ItemApply itemApply) throws Exception {
        ItemApply ia = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
        if (null != ia.getHandleUserid() && ia.getHandleUserid() != 0) {
            throw new Exception(StateMsg.TAKE_ORDER_ERROR_MSG);
        }
        ia.setStatus(ItemApplyEnum.FILE_CHECK.getCode());
        ia.setHandleCreatedDate(new Date());
        ia.setHandleUserid(Integer.parseInt(LoginUserHelper.getUserId()));
        itemApplyMapper.updateByPrimaryKeySelective(ia);
        itemApplyLogService.insertLog(LoginUserHelper.getUserId(), ia.getApplyid(), new Date(), ItemApplyEnum.FILE_CHECK.getCode(), ItemApplyEnum.FILE_CHECK.getDesc());
        return ResultUtil.success();
    }

   /* @Override
    public Result rejectReason(Map<String, Object> map) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.parseInt(map.get("applyid").toString()));
        if (1 == Integer.valueOf(map.get("type").toString())) {
            //itemApply.setStatus(ItemApplyEnum.REVIEW_NO.getCode());
            WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            wfInstance.setRejectReason(map.get("rejectReason").toString());
            wfInstance.setStatus(InstanceEnum.REVIEW_NOT_PASS.getCode());
            wfInstance.setUpdateDate(new Date());
            wfInstanceService.updateById(wfInstance);
            itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            //itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.REVIEW_NO.getCode(), ItemApplyEnum.REVIEW_NO.getDesc());
        } else if (2 == Integer.valueOf(map.get("type").toString())) {
            map.put("userId", LoginUserHelper.getUserId());
            wfInstAuditTrackService.insertData(map);
            WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            WfItemNode wfItemNode = wfItemNodeService.selectByItemId(itemApply.getItemid());
            *//* 如果是第一个审批人 **//*
            if (wfInstance.getNodeid().equals(wfItemNode.getId())) {
                map.put("instanceState", InstanceEnum.PENDING_REVIEW.getCode());
                itemApplyFilesService.updateDelState(itemApply.getApplyid());
                wfInstanceService.updateByInstanceId(map);
                //itemApply.setStatus(ItemApplyEnum.REVIEW.getCode());
                itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            } else {
                *//* 审批节点减一 **//*
                WfItemNode wfItemNode1 = wfItemNodeService.selectById(wfInstance.getNodeid());
                map.put("itemId", wfItemNode1.getItemid());
                map.put("order", wfItemNode1.getOrder() - 1);
                WfItemNode wfItemNode2 = wfItemNodeService.selectByOrder(map);
                wfInstance.setNodeid(wfItemNode2.getId());
                wfInstanceService.updateById(wfInstance);
            }
        }
        return ResultUtil.success();
    }*/

    /*@Override
    public Result pass(ItemApplyFiles itemApplyFiles) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(itemApplyFiles.getApplyid());
        if (itemApplyFiles.getFileTypes() == 0) {
            itemApplyFiles.setFileTypes(ItemFileTypeEnum.OPINION_FILE.getCode());
            //itemApply.setStatus(ItemApplyEnum.REVIEW_YES.getCode());
            //ItemApplyLog itemApplyLog = itemApplyLogService.selectByApplyIdAndStatus(itemApply.getApplyid(), ItemApplyEnum.REVIEW_YES.getCode());
            *//*if (null == itemApplyLog) {
                itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.REVIEW_YES.getCode(), ItemApplyEnum.REVIEW_YES.getDesc());
            } else {
                itemApplyLog.setCreatedDate(new Date());
                itemApplyLogService.updateById(itemApplyLog);
            }*//*
        } else if (itemApplyFiles.getFileTypes() == 1) {
            itemApplyFiles.setFileTypes(ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            //itemApply.setStatus(ItemApplyEnum.REVIEW_SUCCESS.getCode());
//            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.SUBMISSION.getCode(), ItemApplyEnum.SUBMISSION.getDesc());
//            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.REVIEW_SUCCESS.getCode(), ItemApplyEnum.REVIEW_SUCCESS.getDesc());
        }
        itemApplyFiles.setCreatedDate(new Date());
        itemApplyFiles.setIsDel(ItemFileTypeEnum.FILE_EXISTENCE.getCode());
        itemApplyFilesService.insert(itemApplyFiles);
        itemApplyMapper.updateByPrimaryKeySelective(itemApply);
        return ResultUtil.success();
    }*/

    @Override
    public Result notes(Map<String, Object> map) throws Exception {
        /*boolean verifyState = false;
        String aid = String.valueOf(map.get("aid"));
        String signature = String.valueOf(map.get("signature"));
        String describe = String.valueOf(map.get("describe"));
        if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(describe) && StringUtils.isNotBlank(signature)) {
            verifyState = IkiUtil.verifyData(aid, describe, signature);
        }
        if (!verifyState) {
            return ResultUtil.info("error.code", "signature.error.msg");
        }*/
        try {
            ApplyFileLog applyFileLog = new ApplyFileLog();
            applyFileLog.setCreateTime(new Date());
            applyFileLog.setCreateUser(Integer.parseInt(LoginUserHelper.getUserId()));
            applyFileLog.setFileId(Integer.parseInt(map.get("id").toString()));
            int fileStatus = Integer.parseInt(map.get("fileStatus").toString());
            applyFileLog.setFileStatus(fileStatus);
            String json = "{\"describe\":\"" + map.get("describe") + "\",\"fileId\":" + map.get("id") + ",\"fileStatus\":" + map.get("fileStatus") + "}";
            if (fileStatus == 0) {
                applyFileLog.setFileIndex(Integer.parseInt(map.get("fileIndex").toString()));
            }
            applyFileLog.setLogString(json);
            applyFileLogService.insertData(applyFileLog);
            itemApplyMapper.updateStatus(map.get("id").toString());
            return itemApplyFilesService.notes(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /*@Override
    public void approved(Map<String, Object> map) throws Exception {
        map.put("userId", LoginUserHelper.getUserId());
        wfInstAuditTrackService.insertApproved(map);
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.valueOf(map.get("applyid").toString()));
        WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
        WfItemNode wfItemNode = wfItemNodeService.selectByItemIdDesc(itemApply.getItemid());
        *//* 当前节点 不是 审批最后一个节点  当前节点 需 +1 **//*
        if (!wfInstance.getNodeid().equals(wfItemNode.getId())) {
            WfItemNode wfItemNode1 = wfItemNodeService.selectById(wfInstance.getNodeid());
            map.put("itemId", wfItemNode1.getItemid());
            map.put("order", wfItemNode1.getOrder() + 1);
            WfItemNode wfItemNode2 = wfItemNodeService.selectByOrder(map);
            wfInstance.setNodeid(wfItemNode2.getId());
        } else {
            wfInstance.setStatus(InstanceEnum.REVIEW_PASS.getCode());
            //itemApply.setStatus(ItemApplyEnum.SUBMISSION.getCode());
            itemApplyMapper.updateByPrimaryKeySelective(itemApply);
        }
        wfInstance.setUpdateDate(new Date());
        wfInstanceService.updateById(wfInstance);
    }*/

    @Override
    public Result updateToDraft(ItemApply itemApply) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("state", ItemApplyEnum.FILE_DRAFT.getCode());
        map.put("applyid", itemApply.getApplyid());
        itemApplyMapper.updateState(map);
        return ResultUtil.success();
    }

    @Override
    public Result submitApply(ItemApply itemApply) throws Exception {
        /* 交钱成功之后在修改状态 后续集成支付 **/
        ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply1.getItemid());
        itemApply1.setPayStatus(ItemApplyEnum.PAY_PRE.getCode());
        /*if (orgItems.getValuation() == OrgItemEnum.SUBJECT_PRICE.getCode()) {
         *//* 按标的总共应收金额 **//*
            BigDecimal multiply = Arith.mul(itemApply1.getItemValue(), orgItems.getPrice());
            if (multiply.compareTo(BigDecimal.valueOf(orgItems.getLowestPrice())) > 0) {
                *//* 费率金额 > 最低收费 **//*
                itemApply1.setPrice(multiply.doubleValue());
            }
        }*/
        itemApply1.setStatus(ItemApplyEnum.FILE_RECEPTION.getCode());
        itemApply1.setSubmitDate(new Date());
        itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
        ItemCharge prePay = itemChargeService.selectByName("公证预付款");
        Map<String, Object> chargeMap = new HashMap<>(16);
        chargeMap.put("applyId", itemApply1.getApplyid());
        chargeMap.put("id", prePay.getId());
        String systemTimeStr = DateUtil.getSystemTimeStr();
        chargeMap.put("payDate", systemTimeStr);
        chargeDetailService.updateByIdAndApplyId(chargeMap);
        itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply1.getApplyid(), new Date(), ItemApplyEnum.FILE_RECEPTION.getCode(), ItemApplyEnum.FILE_RECEPTION.getDesc());
        Map<String, Object> map = new HashMap<>(16);
        map.put("orderSubmitDate", systemTimeStr);
        map.put("applyNo", itemApply1.getApplyNo());
        map.put("itemName", orgItems.getItemName());
        return ResultUtil.success(map);
    }

    /*@Override
    public Result submitCustomPrice(Map<String, Object> map) throws Exception {
        ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(Integer.parseInt(map.get("applyid").toString()));
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply1.getItemid());
        if (map.get("type").toString().equals("1")) {
            itemApply1.setPrice(Double.valueOf(map.get("price").toString()));
        }
        BigDecimal price = new BigDecimal(itemApply1.getPrice());
        BigDecimal lowestPrice = new BigDecimal(orgItems.getLowestPrice());
        PushMsg pushMsg = new PushMsg();
        if (price.compareTo(lowestPrice) > 0) {
            *//* 输入金额 > 预付款 (要缴纳尾款) **//*
            itemApply1.setPayEndPrice(price.subtract(lowestPrice).doubleValue());
            itemApply1.setPayStatus(ItemApplyEnum.PAY_BALANCE.getCode());
            pushMsg.setCertFicateId(itemApply1.getApplyid().toString());
            pushMsg.setTitle("支付通知");
            pushMsg.setContent("订单号:" + itemApply1.getApplyNo() + "支付尾款通知！待支付金额:" + itemApply1.getPayEndPrice());
            pushMsg.setServerTime(DateUtil.getSystemTimeStr());
            pushMsg.setType(PushEnum.PAY_MSG.getCode());
            pushMsg.setState("1"); // 成功
            pushMsg.setIsRead("0"); // 未读取
            pushMsg.setUserId(itemApply1.getUserid());
        } else {
            *//* 输入金额 <= 预付款 通知管理员审核资料 **//*
            itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
            pushMsg.setTitle("审核通知");
            pushMsg.setContent("订单号:" + itemApply1.getApplyNo() + "尾款结清，请上传意见书！");
            pushMsg.setServerTime(DateUtil.getSystemTimeStr());
            pushMsg.setType(PushEnum.REVIEW_MSG.getCode());
            pushMsg.setState("1");
            pushMsg.setIsRead("0");
            pushMsg.setUserId(Integer.valueOf(LoginUserHelper.getUserId()));
        }
        pushMsgService.insertSelective(pushMsg);
        itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
        return ResultUtil.success();
    }*/

    @Override
    public Result payBalance(ApplyFeeView applyFeeView) throws Exception {
        /* 支付尾款 后续集成支付接口 */
        ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(applyFeeView.getApplyid());
        itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
        itemApply1.setPayEndDate(new Date());
        List<ApplyFee> applyFees1 = chargeDetailService.selectByApplyId(itemApply1.getApplyid());
        double sum = applyFees1.stream().mapToDouble(ApplyFee::getPrice).sum();
        if (String.valueOf(sum).equals(String.valueOf(applyFeeView.getPayEndPrice()))) {
            itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
        } else {
            throw new Exception();
        }
        applyFees1.forEach(applyFee -> {
            chargeDetailService.updatePayStatus(applyFee.getId());
        });
        PushMsg pushMsg = new PushMsg();
        pushMsg.setCertFicateId(itemApply1.getApplyid().toString());
        pushMsg.setTitle("审核通知");
        pushMsg.setContent("订单号:" + itemApply1.getApplyNo() + "尾款结清，请上传意见书！");
        pushMsg.setServerTime(DateUtil.getSystemTimeStr());
        pushMsg.setType(PushEnum.REVIEW_MSG.getCode());
        pushMsg.setState("1");
        pushMsg.setIsRead("0");
        pushMsg.setUserId(itemApply1.getHandleUserid());
        pushMsgService.insertSelective(pushMsg);
        return ResultUtil.success();
    }

    @Override
    public Result index() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        Map<String, Object> dataMap = new LinkedHashMap<>(16);
        dataMap.put("userId", LoginUserHelper.getUserId());
        Integer myApplyCount = itemApplyMapper.selectMyApplyCount(dataMap);
        List<ItemApply> myApplyList = itemApplyMapper.selectMyApply(dataMap);
        List<PubArticle> articleList = pubArticleService.articleTop();
        dataMap.put("status", ItemApplyEnum.FILE_CHECK.getCode());
        Integer myTaskCount = itemApplyMapper.inProcessing(dataMap);
        map.put("articleList", articleList);
        map.put("myApplyCount", myApplyCount);
        map.put("myApplyList", myApplyList);
        map.put("myTaskCount", myTaskCount);
        return ResultUtil.success(map);
    }

    @Override
    public Result acceptProgress(int applyid) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        ApplyUserInfo applyInfo = itemApplyMapper.selectApplyInfo(applyid);
        List<ItemApplyLog> itemApplyLogs = historyNode(applyid);
        if (itemApplyLogs.size() > 1) {
            ItemApplyLog itemApplyLog = itemApplyLogs.get(itemApplyLogs.size() - 1);
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
            if (Integer.parseInt(itemApplyLog.getStatus()) == ItemApplyEnum.APPLY_FAIL.getCode() || Integer.parseInt(itemApplyLog.getStatus()) == ItemApplyEnum.REJECT_REASON.getCode()) {
                itemApplyLog.setRejectReason(itemApply.getRejectReason());
            }
        }
        map.put("allNode", getAllNode());
        map.put("historyNode", itemApplyLogs);
        map.put("applyInfo", applyInfo);
        return ResultUtil.success(map);
    }

    @Override
    public Result myTaskIndex() throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", LoginUserHelper.getUserId());
        map.put("status", ItemApplyEnum.FILE_CHECK.getCode());
        map.put("isEnd", new int[]{ItemApplyEnum.APPLY_SUCCESS.getCode(), ItemApplyEnum.APPLY_FAIL.getCode(), ItemApplyEnum.REJECT_REASON.getCode()});
        map.put("inPro", new int[]{ItemApplyEnum.FILE_CHECK.getCode(), ItemApplyEnum.FILE_REVIEW.getCode(), ItemApplyEnum.FILE_MAKE.getCode(), ItemApplyEnum.CHECK_RETURN.getCode()});
        map.put("review", ItemApplyEnum.FILE_REVIEW.getCode());
        Integer newOrderCount = itemApplyMapper.selectOrderCount(ItemApplyEnum.FILE_RECEPTION.getCode());
        Integer isVerifiCount = itemApplyMapper.isVerifi(ItemApplyEnum.WAIT_ORDER.getCode());
        Integer inProcessingCount = itemApplyMapper.inProcessing(map);
        Integer isProcessingCount = itemApplyMapper.isProcessing(map);
        Integer inReviewCount = itemApplyMapper.inReview(map);
        List<ItemApply> newApplyList = itemApplyMapper.selectNewApply(ItemApplyEnum.FILE_RECEPTION.getCode());
        List<ItemApply> inProcessingList = itemApplyMapper.inProcessingList(map);
        Map<String, Object> applyInfo = new LinkedHashMap<>(16);
        applyInfo.put("newOrderCount", newOrderCount);
        applyInfo.put("isVerifiCount", isVerifiCount);
        applyInfo.put("inProcessingCount", inProcessingCount);
        applyInfo.put("isProcessingCount", isProcessingCount);
        applyInfo.put("inReviewCount", inReviewCount);
        applyInfo.put("newApplyList", newApplyList);
        applyInfo.put("inProcessingList", inProcessingList);
        return ResultUtil.success(applyInfo);
    }

    @Override
    public Result myTaskList(int pageNum, int type, Integer itemId, String word) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        switch (type) {
            case 1: /* 新申请 */
                map.put("status", new int[]{ItemApplyEnum.FILE_RECEPTION.getCode()});
                break;
            case 2: /* 已初审 */
                map.put("status", new int[]{ItemApplyEnum.WAIT_ORDER.getCode()});
                break;
            case 3: /* 正在受理 */
                map.put("userId", LoginUserHelper.getUserId());
                map.put("status", new int[]{ItemApplyEnum.FILE_CHECK.getCode(), ItemApplyEnum.FILE_REVIEW.getCode(), ItemApplyEnum.FILE_MAKE.getCode(), ItemApplyEnum.CHECK_RETURN.getCode()});
                break;
            case 4: /* 已完成 */
                map.put("userId", LoginUserHelper.getUserId());
                map.put("status", new int[]{ItemApplyEnum.APPLY_SUCCESS.getCode(), ItemApplyEnum.APPLY_FAIL.getCode(), ItemApplyEnum.REJECT_REASON.getCode()});
                break;
        }
        if (StringUtils.isNotBlank(word)) {
            map.put("word", word);
        }
        if (null != itemId && itemId > 0) {
            map.put("itemId", itemId);
        }
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<ItemApply> itemApplyList = itemApplyMapper.selectMyTaskList(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return ResultUtil.success();
        }
        return ResultUtil.success(pageInfo);
    }

    @Override
    public Result mytaskDetail(int applyid) throws Exception {
        Map<String, Object> applyMap = new LinkedHashMap<>(16);
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
        List<ItemRequered> list = itemRequeredService.selectByRequeredIdAndApplyId(applyid);
        boolean otherFile = false;
        /* 用户提交的文件清单 **/
        for (int i = 0; i < list.size(); i++) {
            ItemRequered itemRequered = list.get(i);
            if (itemRequered.getName().equals("其他材料")) {
                otherFile = true;
            }
            Map<String, Object> m = new LinkedHashMap<>(16);
            m.put("applyid", applyid);
            m.put("requeredid", itemRequered.getRequeredid());
            m.put("fileTypes", ItemFileTypeEnum.USER_FILE.getCode());
            List<ItemApplyFiles> itemApplyFilesList = itemApplyFilesService.selectByApplyIdAndRequeredId(m);
            if (itemApplyFilesList.size() > 0) {
                String[] ids = new String[itemApplyFilesList.size()];
                for (int j = 0; j < itemApplyFilesList.size(); j++) {
                    ItemApplyFiles itemApplyFiles = itemApplyFilesList.get(j);
                    Integer fileid = itemApplyFiles.getFileid();
                    ids[j] = fileid.toString();
                }
                List<CertFiles> cfList = certFilesService.findByFilesIds2(ids);
                itemRequered.setCertFilesList(cfList);
            }
        }
        if (!otherFile) {
            ItemRequered itemRequered = itemRequeredService.selectOtherFile(list.get(0).getItemid());
            list.add(itemRequered);
        }
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("applyid", applyid);
        /* 订单信息及项目信息 */
        ApplyUserInfo applyInfo = itemApplyMapper.selectDetailInfo(map);
        ApplyExpand applyExpand = applyExpandService.selectByApplyId(applyid);
        if (applyExpand.getAddressId() == 0) {
            /* 自取 */
            Org org = orgService.selectByApplyId(applyid);
            applyInfo.setIsSend(0);
            applyInfo.setOrgAddress(org.getAddress());
        } else {
            /* 邮寄 */
            UserAddress userAddress = userAddressService.selectById(applyExpand.getAddressId());
            applyInfo.setIsSend(1);
            applyInfo.setUserAddress(userAddress);
        }
        /* 公证收款项 */
        List<ApplyFee> applyFee = chargeDetailService.selectByApplyIdAndStatus(applyid);
        applyMap.put("isPay", applyFee.get(0));
        if (itemApply.getPayStatus() == ItemApplyEnum.PAY_ALL.getCode()) {
            List<ApplyFee> applyFees = chargeDetailService.selectByApplyIdAndStatus(applyid);
            applyFees.remove(0);
            applyMap.put("applyFees", applyFees);
        } else {
            List<ApplyFee> applyFees = chargeDetailService.selectByApplyId(applyid);
            applyMap.put("applyFees", applyFees);
        }
        /* 视频预约 */
        List<BizItemVideo> bizItemVideoList = bizItemVideoService.selectByApplyId(applyid, 1);
        /* 补充材料 */
        List<CertFiles> addFiles = certFilesService.selectIsAddFiles(applyid);
        /* 修改材料 */
        List<CertFiles> updateFiles = certFilesService.selectIsUpdateFiles(applyid);
        /* 公正意见书版本 */
        if (LoginUserHelper.getUserId().equals(String.valueOf(itemApply.getHandleUserid()))) {
            /* 申请审批后进入详情看自己提交意见书 其他信息审批进度中查看 */
            BookView bookView = selectBook(itemApply);
            if (bookView.getId() != null) {
                applyMap.put("firstBook", bookView);
            }
        }
        if (itemApply.getStatus() == ItemApplyEnum.FILE_REVIEW.getCode() || itemApply.getStatus() == ItemApplyEnum.CHECK_RETURN.getCode()) {
            /* 查询是否有草稿意见书 */
            WfInstAuditTrack auditTrack = wfInstAuditTrackService.selectEditData(applyid, LoginUserHelper.getUserId());
            if (null != auditTrack) {
                BookView bookView = new BookView();
                bookView.setId(auditTrack.getId());
                bookView.setCreatedDate(DateUtil.stringToDate(auditTrack.getAuditDate(), ""));
                bookView.setType(1);
                applyMap.put("newBook", bookView);
            } else {
                /* 是第一审批人取提交版本 不是第一审批人取上一版本 */
                WfItemNode wfItemNode = wfItemNodeService.selectFirstNode(map);
                if (LoginUserHelper.getUserId().equals(String.valueOf(wfItemNode.getAuditUserid()))) {
                    BookView bookView = selectBook(itemApply);
                    applyMap.put("newBook", bookView);
                } else {
                    WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectLastReason(applyid);
                    if (null != wfInstAuditTrack) {
                        BookView bookView = new BookView();
                        bookView.setId(wfInstAuditTrack.getId());
                        bookView.setCreatedDate(DateUtil.stringToDate(wfInstAuditTrack.getAuditDate(), ""));
                        bookView.setReason(wfInstAuditTrack.getReason());
                        bookView.setType(1);
                        applyMap.put("newBook", bookView);
                    }
                }
            }
        }
        if (itemApply.getStatus() == ItemApplyEnum.FILE_MAKE.getCode()) {
            Map<String, Object> hashMap = new HashMap<>(16);
            hashMap.put("applyId", itemApply.getApplyid());
            hashMap.put("state", ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(hashMap);
            if (null == itemApplyFiles) {
                applyInfo.setMakeFileStatus(0);
            } else {
                applyInfo.setMakeFileStatus(1);
            }
        }
        if (itemApply.getStatus() == ItemApplyEnum.REJECT_REASON.getCode() || itemApply.getStatus() == ItemApplyEnum.APPLY_FAIL.getCode()) {
            WfInstAuditTrack track1 = new WfInstAuditTrack();
            track1.setReason(itemApply.getRejectReason());
            track1.setAuditDate(DateUtil.dateToString(itemApply.getHandleCreatedDate(), ""));
            track1.setStatus(1);
            applyMap.put("track", track1);
        } else {
            WfInstAuditTrack track = wfInstAuditTrackService.selectByApplyIdAndUserId(applyid);
            applyMap.put("track", track);
        }
        applyMap.put("addFiles", addFiles);
        applyMap.put("updateFiles", updateFiles);
        applyMap.put("applyVideo", bizItemVideoList.size() > 0 ? bizItemVideoList.get(0) : null);
        applyMap.put("payEndPrice", itemApply.getPayEndPrice());
        applyMap.put("payEndDate", DateUtil.dateToString(itemApply.getPayEndDate(), ""));
        applyMap.put("applyInfo", applyInfo);
        applyMap.put("itemRequered", list);
        return ResultUtil.success(applyMap);
    }

    private BookView selectBook(ItemApply itemApply) throws Exception {
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("applyId", itemApply.getApplyid());
        ItemApplyFiles itemApplyFiles = null;
        if (itemApply.getStatus() == ItemApplyEnum.FILE_MAKE.getCode()) {
            hashMap.put("state", ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            if (null == itemApplyFiles) {
                hashMap.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
            }
        } else if (itemApply.getStatus() == ItemApplyEnum.APPLY_SUCCESS.getCode()) {
            hashMap.put("state", ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
        } else {
            hashMap.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
        }
        itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(hashMap);
        BookView bookView = new BookView();
        if (null != itemApplyFiles) {
            bookView.setId(itemApplyFiles.getId());
            bookView.setCreatedDate(itemApplyFiles.getCreatedDate());
            bookView.setType(0);
        }
        return bookView;
    }

    @Override
    public Result mytaskPassed(int applyid) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", applyid);
        map.put("state", ItemApplyEnum.WAIT_ORDER.getCode());
        itemApplyMapper.updateState(map);
        itemApplyLogService.insertLog(LoginUserHelper.getUserId(), applyid, new Date(), ItemApplyEnum.WAIT_ORDER.getCode(), ItemApplyEnum.WAIT_ORDER.getDesc());
        return ResultUtil.success();
    }

    @Override
    public Result mytaskRejectReason(Reject reject) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>(16);
            map.put("applyId", reject.getApplyid());
            map.put("rejectReason", reject.getRejectReason());
            if (reject.getType() == 0) {
                /* 预审退费 全额退款 */
                map.put("status", ItemApplyEnum.REJECT_REASON.getCode());
                map.put("handleUserid", LoginUserHelper.getUserId());
                map.put("handleCreatedDate", new Date());
                itemApplyMapper.updateRejectReason(map);
                ChargeDetail chargeDetail = chargeDetailService.selectById(reject.getFeeInfo().get(0).getId());
                chargeDetail.setId(null);
                chargeDetail.setPayStatus(1); /* 退费 */
                chargeDetail.setIsPay(1);
                chargeDetail.setCratedDate(new Date());
                chargeDetailService.insertData(chargeDetail);
                itemApplyLogService.insertLog(LoginUserHelper.getUserId(), reject.getApplyid(), new Date(), ItemApplyEnum.REJECT_REASON.getCode(), ItemApplyEnum.REJECT_REASON.getDesc());
            } else if (reject.getType() == 1) {
                /* 接单后退费 自定义退款 */
                map.put("status", ItemApplyEnum.APPLY_FAIL.getCode());
                itemApplyMapper.updateRejectReason(map);
                for (ChargeDetail chargeDetail : reject.getFeeInfo()) {
                    ChargeDetail detail = chargeDetailService.selectById(chargeDetail.getId());
                    chargeDetail.setId(null);
                    chargeDetail.setPayStatus(1); /* 退费 */
                    chargeDetail.setIsPay(1);
                    chargeDetail.setCratedDate(new Date());
                    chargeDetailService.insertData(chargeDetail);
                }
                itemApplyLogService.insertLog(LoginUserHelper.getUserId(), reject.getApplyid(), new Date(), ItemApplyEnum.APPLY_FAIL.getCode(), ItemApplyEnum.APPLY_FAIL.getDesc());
            }
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result rejectDetail(int applyid) throws Exception {
        Map<String, Object> mapInfo = new HashMap<>(16);
        OrderInfo orderInfo = itemApplyMapper.orderInfo(applyid);
        List<ChargeDetail> chargeDetailList = itemApplyMapper.feeInfo(applyid);
        mapInfo.put("orderInfo", orderInfo);
        mapInfo.put("feeInfo", chargeDetailList);
        return ResultUtil.success(mapInfo);
    }

    @Override
    public Result notesList(int id) throws Exception {
        List<ApplyFileLog> applyFileLogs = applyFileLogService.selectByFileId(id);
        return ResultUtil.success(applyFileLogs);
    }

    @Override
    public Result reviewList(Integer pageNum, String word, int type, int itemId) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        List<ItemApply> itemApplyList = new ArrayList<>();
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        if (StringUtils.isNotBlank(word)) {
            map.put("word", word);
        }
        map.put("userId", LoginUserHelper.getUserId());
        if (type == 1) { /* 待审核列表 */
            map.put("status", ItemApplyEnum.FILE_REVIEW.getCode());
            PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
            itemApplyList = itemApplyMapper.selectWaitList(map);
        } else if (type == 2) { /* 已审核列表 */
            itemApplyList = itemApplyMapper.dealList(map);
        } else {
            return ResultUtil.error();
        }
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return ResultUtil.success();
        }
        return ResultUtil.success(pageInfo);
    }

    @Override
    public Result processTemplate() throws Exception {
        List<ProcessConfig> nodes = wfItemNodeService.selectByUserId();
        return ResultUtil.success(nodes);
    }

    @Override
    public Result processList() throws Exception {
        List<OrgUser> orgUserList = orgUserService.selectAdminList();
        return ResultUtil.success(orgUserList);
    }

    @Override
    public Result otherFeeList() throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("state", 0);
        return itemChargeService.selectByOrgIdAndState(map);
    }

    @Override
    public Result otherFeeSave(ApplyFeeView applyFeeView) throws Exception {
        chargeDetailService.deleteByApplyId(applyFeeView.getApplyid());
        List<ChargeDetail> chargeDetailList = new ArrayList<>();
        applyFeeView.getApplyFees().forEach(applyFee -> {
            ChargeDetail chargeDetail = new ChargeDetail();
            chargeDetail.setApplyId(applyFeeView.getApplyid());
            chargeDetail.setChargeId(applyFee.getChargeId());
            chargeDetail.setPrice(applyFee.getPrice());
            chargeDetail.setCreatedId(Integer.parseInt(LoginUserHelper.getUserId()));
            chargeDetail.setHandleId(Integer.parseInt(LoginUserHelper.getUserId()));
            chargeDetail.setIsPay(0);
            chargeDetail.setPayStatus(0);
            chargeDetail.setFileNum(applyFee.getFileNum());
            chargeDetail.setCratedDate(new Date());
            chargeDetail.setIsDel(0);
            chargeDetailList.add(chargeDetail);
        });
        chargeDetailService.insertList(chargeDetailList);
        return ResultUtil.success();
    }

    @Override
    public Result noticePay(ItemApply itemapply) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyId", itemapply.getApplyid());
        map.put("payEndPrice", itemapply.getPayEndPrice());
        map.put("status", ItemApplyEnum.PAY_BALANCE.getCode());
        List<ApplyFee> applyFees = chargeDetailService.selectByApplyId(itemapply.getApplyid());
        double sum = applyFees.stream().mapToDouble(ApplyFee::getPrice).sum();
        if (String.valueOf(sum).equals(String.valueOf(itemapply.getPayEndPrice()))) {
            itemApplyMapper.updateById(map);
        } else {
            return ResultUtil.error();
        }
        return ResultUtil.success();
    }

    @Override
    public void updateById(Integer applyId) throws Exception {
        itemApplyMapper.updateAddFileStatus(applyId);
    }

    @Override
    public Result addFiles(int applyid) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>(16);
            List<CertFiles> updateFiles = certFilesService.selectUpdateFiles(applyid, ItemFileTypeEnum.UPDATE_FILE.getCode());
            List<CertFiles> addFiles = certFilesService.selectAddFiles(applyid, ItemFileTypeEnum.ADD_FILE.getCode());
            map.put("updateFiles", updateFiles);
            map.put("addFiles", addFiles);
            return ResultUtil.success(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result processSave(ProcessConfigView processConfigView) throws Exception {
        /* 删除该公正审批模板及之前审批节点 */
        wfItemNodeService.deleteByUserId(LoginUserHelper.getUserId(), processConfigView.getApplyid());
        List<ProcessConfig> processConfigList = processConfigView.getProcessConfigList();
        Integer nodeId = null;
        /* 新增该公正审批模板及审批节点 */
        for (int i = 0; i < processConfigList.size(); i++) {
            ProcessConfig processConfig = processConfigList.get(i);
            WfItemNode wfItemNode = new WfItemNode();
            WfItemNode node = new WfItemNode();
            wfItemNode.setUserId(Integer.parseInt(LoginUserHelper.getUserId()));
            wfItemNode.setOrder(processConfig.getOrder());
            wfItemNode.setAuditUserid(processConfig.getAuditUserid().toString());
            wfItemNode.setCreatedDate(DateUtil.getSystemTimeStr());
            wfItemNode.setCreatedBy(LoginUserHelper.getUserId());
            try {
                BeanUtils.copyProperties(node, wfItemNode);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            wfItemNode.setApplyId(0);
            node.setApplyId(processConfigView.getApplyid());
            wfItemNodeService.insertData(wfItemNode);
            wfItemNodeService.insertData(node);
            if (null == nodeId) {
                nodeId = node.getId();
            }
        }
        /* 创建唯一审批示例 */
        WfInstance wfInstance = new WfInstance();
        wfInstance.setCreatedDate(new Date());
        wfInstance.setUpdateDate(new Date());
        wfInstance.setNodeid(nodeId);
        wfInstance.setStatus(InstanceEnum.UNDER_REVIEW.getCode());
        wfInstance.setCreatedBy(Integer.parseInt(LoginUserHelper.getUserId()));
        wfInstanceService.insert(wfInstance);
        /* 公正申请添加审批流程实例 */
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyId", processConfigView.getApplyid());
        map.put("wfInstanceId", wfInstance.getId());
        map.put("status", ItemApplyEnum.FILE_REVIEW.getCode());
        itemApplyMapper.updateWfInstanceIdById(map);
        return ResultUtil.success();
    }

    @Override
    public Result approvalProgress(Integer applyid) throws Exception {
        /* 查询已审批节点审批人信息 */
        List<ProgressView> isProgress = wfInstAuditTrackService.selectIsProgress(applyid);
        /* 查询未审批人审批信息 */
        List<ProgressView> isNotProgress = wfItemNodeService.isNotProgress(applyid);
        if (isNotProgress.size() == 1) {
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
            WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectByApplyIdAndInstIdAndNodeId(String.valueOf(applyid), itemApply.getWfInstanceId(), isNotProgress.get(0).getId());
            if (null != wfInstAuditTrack && wfInstAuditTrack.getStatus() != 2) {
                isNotProgress.remove(0);
            }
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("isProgress", isProgress);
        map.put("isNotProgress", isNotProgress);
        return ResultUtil.success(map);
    }

    @Override
    public Result approved(Map<String, Object> map) throws Exception {
        try {
            map.put("userId", LoginUserHelper.getUserId());
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.valueOf(map.get("applyid").toString()));
            WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            WfItemNode wfItemNode = wfItemNodeService.selectLaseNode(map);
            map.put("nodeid", wfInstance.getNodeid());
            map.put("instid", wfInstance.getId());
            WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectByInstanId(map);
            if (null == wfInstAuditTrack) {
                /* 判断是否是第一审批人 */
                WfItemNode wfItemNode1 = wfItemNodeService.selectFirstNode(map);
                /* 是第一审批人 取制证版本保存 */
                if (wfItemNode1.getAuditUserid().equals(LoginUserHelper.getUserId())) {
                    map.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
                    map.put("applyId", itemApply.getApplyid());
                    ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(map);
                    map.put("fileString", itemApplyFiles.getFileString());
                } else {
                    /* 不是第一审批人 取 上一节点意见书保存 */
                    WfItemNode wfItemNode2 = wfItemNodeService.selectPreviousNode(map.get("applyid").toString(), wfInstance.getNodeid());
                    WfInstAuditTrack wfInstAuditTrack1 = wfInstAuditTrackService.selectByNodeId(wfItemNode2.getId());
                    map.put("fileString", wfInstAuditTrack1.getFileString());
                }
                wfInstAuditTrackService.insertApproved(map);
            } else {
                wfInstAuditTrack.setAuditDate(DateUtil.getSystemTimeStr());
                wfInstAuditTrack.setStatus(0);
                wfInstAuditTrack.setReason(String.valueOf(map.get("opinion")));
                wfInstAuditTrackService.updateData(wfInstAuditTrack);
            }
            /* 当前节点 不是 审批最后一个节点  当前节点 需 +1 */
            if (!wfInstance.getNodeid().equals(wfItemNode.getId())) {
                map.put("nodeId", wfInstance.getNodeid());
                WfItemNode wfItemNode2 = wfItemNodeService.selectNextNode(map);
                wfInstance.setNodeid(wfItemNode2.getId());
            } else {
                wfInstance.setStatus(InstanceEnum.REVIEW_PASS.getCode());
                itemApply.setStatus(ItemApplyEnum.FILE_MAKE.getCode());
                itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            }
            wfInstance.setUpdateDate(new Date());
            wfInstanceService.updateById(wfInstance);
            if (itemApply.getStatus() == ItemApplyEnum.FILE_REVIEW.getCode()) {
                itemApplyLogService.insertLog(LoginUserHelper.getUserId(), Integer.parseInt(map.get("applyid").toString()), new Date(), ItemApplyEnum.FILE_REVIEW.getCode(), ItemApplyEnum.FILE_REVIEW.getDesc());
            } else {
                itemApplyLogService.insertLog(LoginUserHelper.getUserId(), Integer.parseInt(map.get("applyid").toString()), new Date(), ItemApplyEnum.FILE_MAKE.getCode(), ItemApplyEnum.FILE_MAKE.getDesc());
            }
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result rejectReason(Map<String, Object> map) throws Exception {
        try {
            /* 退回到发起人 状态改为 审核退回  */
            map.put("userId", LoginUserHelper.getUserId());
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.valueOf(map.get("applyid").toString()));
            WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            map.put("instanceState", InstanceEnum.PENDING_REVIEW.getCode());
            wfInstanceService.updateByInstanceId(map);
            itemApply.setStatus(ItemApplyEnum.CHECK_RETURN.getCode());
            itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            map.put("instid", wfInstance.getId());
            map.put("nodeid", wfInstance.getNodeid());
            WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectByInstanId(map);
            if (null == wfInstAuditTrack) {
                WfItemNode wfItemNode = wfItemNodeService.selectByAuditIdAndApplyId(map);
                if (wfItemNode.getOrder() == 1) {
                    Map<String, Object> dataMap = new HashMap<>(16);
                    dataMap.put("applyId", Integer.valueOf(map.get("applyid").toString()));
                    dataMap.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
                    ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(dataMap);
                    map.put("fileString", itemApplyFiles.getFileString());
                } else {
                    WfItemNode wfItemNode1 = wfItemNodeService.selectPreviousNode(map.get("applyid").toString(), wfItemNode.getId());
                    WfInstAuditTrack wfInstAuditTrack1 = wfInstAuditTrackService.selectByApplyIdAndInstIdAndNodeId(map.get("applyid").toString(), itemApply.getWfInstanceId(), wfItemNode1.getId());
                    map.put("fileString", wfInstAuditTrack1.getFileString());
                }
                wfInstAuditTrackService.insertData(map);
            } else {
                wfInstAuditTrack.setAuditDate(DateUtil.getSystemTimeStr());
                wfInstAuditTrack.setStatus(1);
                wfInstAuditTrack.setReason(String.valueOf(map.get("opinion")));
                wfInstAuditTrackService.updateData(wfInstAuditTrack);
            }
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ItemApply selectById(Integer applyId) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyId);
        return itemApply;
    }

    @Override
    public Result isSuccess(int applyid) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("state", ItemApplyEnum.APPLY_SUCCESS.getCode());
        map.put("applyid", applyid);
        itemApplyMapper.updateState(map);
        itemApplyLogService.insertLog(LoginUserHelper.getUserId(), applyid, new Date(), ItemApplyEnum.APPLY_SUCCESS.getCode(), ItemApplyEnum.APPLY_SUCCESS.getDesc());
        return ResultUtil.success();
    }

    @Override
    public Result seeBook(Integer id, Integer type) throws Exception {
        if (type == 0) {
            WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectById(id);
            return ResultUtil.success(wfInstAuditTrack.getFileString());
        } else if (type == 1) {
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectById(id);
            return ResultUtil.success(itemApplyFiles.getFileString());
        }
        return null;
    }

    @Override
    public void updateByPrimaryKeySelective(ItemApply itemApply) {
        itemApplyMapper.updateByPrimaryKeySelective(itemApply);
    }
}
