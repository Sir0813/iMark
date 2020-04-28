package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.ItemApplyMapper;
import com.dm.user.msg.*;
import com.dm.user.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Result insert(ItemApply itemApply) throws Exception {
        boolean insert = null == itemApply.getApplyid();
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
        if (itemApply.getItemValue() > 0) {
            /* 按标的计价 计算用户应交总费用 **/
            BigDecimal itemValue = new BigDecimal(itemApply.getItemValue());
            BigDecimal price = new BigDecimal(orgItems.getPrice());
            /* 按标的总共应收金额 **/
            BigDecimal multiply = itemValue.multiply(price);
            if (multiply.compareTo(new BigDecimal(orgItems.getLowestPrice())) > -1) {
                /* 按标的应收金额大于等于最低收费金额 **/
                itemApply.setPrice(multiply.doubleValue());
            } else {
                itemApply.setPrice(orgItems.getLowestPrice());
            }
        }
        if (insert) {
            SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssSSS");
            String format1 = format.format(new Date());
            itemApply.setApplyNo(format1);
            itemApply.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
            itemApply.setCreatedDate(new Date());
            itemApply.setSubmitDate(new Date());
            itemApply.setItemCode(orgItems.getItemCode());
            itemApplyMapper.insertSelective(itemApply);
            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.DRAFT.getCode(), ItemApplyEnum.DRAFT.getDesc());
            /*ApplyExpand applyExpand = itemApply.getApplyExpand();
            applyExpand.setApplyId(itemApply.getApplyid());
            applyExpandService.insert(applyExpand);*/
        } /*else {
            applyExpandService.update(itemApply.getApplyExpand());
        }*/
        List<ApplyFile> applyFileList = itemApply.getApplyFileList();
        for (int i = 0; i < applyFileList.size(); i++) {
            String requeredid = applyFileList.get(i).getRequeredid();
            String[] split = applyFileList.get(i).getFileid().split(",");
            if (!insert) {
                itemApplyFilesService.deleteByApplyIdAndRequeredId(itemApply.getApplyid(), requeredid);
            }
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
        itemApplyMapper.updateByPrimaryKeySelective(itemApply);
        return ResultUtil.success();
    }

    @Override
    public PageInfo<ItemApply> list(int pageNum) throws Exception {
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<ItemApply> itemApplyList = itemApplyMapper.list(Integer.parseInt(LoginUserHelper.getUserId()));
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> detail(int applyid) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        Map<String, Object> userMap = new LinkedHashMap<>(16);
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
        Org org = orgService.selectById(orgItems.getOrgid());
        List<ItemRequered> list = itemRequeredService.selectByItemId(itemApply.getItemid());
        UserCard userCard = userCardService.selectByUserIdAndStatus(itemApply.getUserid());
        AppUser user = userService.selectByPrimaryKey(itemApply.getUserid());
        /* 用户提交的文件清单 **/
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
        if (ItemApplyEnum.REVIEW_NO.getCode() == itemApply.getStatus()) {
            wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
        }
        if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
            /* 固定价格计费 返回本次公正所需费用 **/
            map.put("price", orgItems.getPrice());
        } else {
            /* 按标的 或公正人员自定义先缴纳最低价格 **/
            map.put("price", orgItems.getLowestPrice());
        }
        CertFiles certFiles = null;
        List<ApplyHistory> applyHistories = null;
        if (itemApply.getStatus() != ItemApplyEnum.DRAFT.getCode()) {
            Map<String, Object> fileMap = new LinkedHashMap<>(16);
            fileMap.put("applyId", applyid);
            fileMap.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(fileMap);
            if (null != itemApplyFiles) {
                certFiles = certFilesService.selectByPrimaryKey(itemApplyFiles.getFileid());
            }
            /* 审核历史记录 **/
            applyHistories = itemApplyMapper.history(applyid);
        }
        if (itemApply.getStatus() == ItemApplyEnum.REVIEW_SUCCESS.getCode()) {
            /* 审核完成显示盖章意见书 **/
            Map<String, Object> map2 = new HashMap<>(16);
            map2.put("applyId", applyid);
            map2.put("state", ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectByApplyIdAndState(map2);
            CertFiles certFiles1 = certFilesService.selectByPrimaryKey(itemApplyFiles.getFileid());
            map.put("fileUrl", certFiles1.getFileUrl());
        }
        List<BizItemVideo> bizItemVideoList = bizItemVideoService.selectByApplyId(applyid);
        //ApplyExpand applyExpand = applyExpandService.selectByApplyId(applyid);
        userMap.put("userName", null == userCard ? "" : userCard.getRealName());
        userMap.put("userPhone", user.getUsername());
        userMap.put("userCard", null == userCard ? "" : userCard.getCardNumber());
        userMap.put("userId", user.getUserid());
        //map.put("applyExpand", applyExpand);
        map.put("orgName", org.getOrgname());
        map.put("itemValue", itemApply.getItemValue());
        map.put("bizItemVideoList", bizItemVideoList);
        /* 审批历史公正意见书 (未盖章) **/
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
        map.put("allNode", getAllNode());
        map.put("historyNode", historyNode(applyid));
        return map;
    }

    private List<ItemApplyLog> historyNode(int applyid) throws Exception {
        return itemApplyLogService.historyNode(applyid);
    }

    private List<ItemapplyNode> getAllNode() {
        Map<Integer, Object> map = new LinkedHashMap<>(16);
        map.put(ItemApplyEnum.DRAFT.getCode(), ItemApplyEnum.DRAFT.getDesc());
        map.put(ItemApplyEnum.WAIT_PAY.getCode(), ItemApplyEnum.WAIT_PAY.getDesc());
        map.put(ItemApplyEnum.REVIEW.getCode(), ItemApplyEnum.REVIEW.getDesc());
        map.put(ItemApplyEnum.REVIEW_YES.getCode(), ItemApplyEnum.REVIEW_YES.getDesc());
        map.put(ItemApplyEnum.SUBMISSION.getCode(), ItemApplyEnum.SUBMISSION.getDesc());
        map.put(ItemApplyEnum.REVIEW_SUCCESS.getCode(), ItemApplyEnum.REVIEW_SUCCESS.getDesc());
        List<ItemapplyNode> collect = map.entrySet().stream().map(e -> new ItemapplyNode(e.getKey(), e.getValue().toString())).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Result delete(int applyid) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("applyId", applyid);
        map.put("fileTypes", ItemFileTypeEnum.USER_FILE.getCode());
        if (itemApply.getStatus() == ItemApplyEnum.DRAFT.getCode()) {
            itemApplyFilesService.deleteByApplyIdAndFileType(map);
            itemApplyMapper.deleteByPrimaryKey(applyid);
            return ResultUtil.success();
        }
        return ResultUtil.error();
    }

    @Override
    public Result pendList(Integer pageNum, int itemId) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("status", ItemApplyEnum.REVIEW.getCode());
        List<ItemApply> itemApplyList = new ArrayList<>();
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        itemApplyList = itemApplyMapper.pendList(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return ResultUtil.success();
        }
        return ResultUtil.success(pageInfo);
    }

    @Override
    public synchronized Result takeOrder(ItemApply itemApply) throws Exception {
        ItemApply ia = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
        if (ia.getHandleUserid() != 0) {
            throw new Exception(StateMsg.TAKE_ORDER_ERROR_MSG);
        }
        ia.setHandleCreatedDate(new Date());
        ia.setHandleUserid(Integer.parseInt(LoginUserHelper.getUserId()));
        itemApplyMapper.updateByPrimaryKeySelective(ia);
        itemApplyLogService.insertLog(LoginUserHelper.getUserId(), ia.getApplyid(), new Date(), ItemApplyEnum.REVIEW.getCode(), ItemApplyEnum.REVIEW.getDesc());
        return ResultUtil.success();
    }

    @Override
    public Result pendReview(Integer pageNum, int itemId, int type) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userId", LoginUserHelper.getUserId());
        if (1 == type) {
            /* 处理中 **/
            map.put("status", new int[]{ItemApplyEnum.REVIEW.getCode(), ItemApplyEnum.SUBMISSION.getCode()});
        } else if (2 == type) {
            /* 已处理 **/
            map.put("status", new int[]{ItemApplyEnum.REVIEW_YES.getCode(), ItemApplyEnum.REVIEW_NO.getCode(), ItemApplyEnum.REVIEW_SUCCESS.getCode()});
        }
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        List<ItemApply> itemApplyList = itemApplyMapper.pendReview(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return ResultUtil.success();
        }
        return ResultUtil.success(pageInfo);
    }

    @Override
    public Result rejectReason(Map<String, Object> map) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.parseInt(map.get("applyid").toString()));
        if (1 == Integer.valueOf(map.get("type").toString())) {
            itemApply.setStatus(ItemApplyEnum.REVIEW_NO.getCode());
            WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            wfInstance.setRejectReason(map.get("rejectReason").toString());
            wfInstance.setStatus(InstanceEnum.REVIEW_NOT_PASS.getCode());
            wfInstance.setUpdateDate(new Date());
            wfInstanceService.updateById(wfInstance);
            itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.REVIEW_NO.getCode(), ItemApplyEnum.REVIEW_NO.getDesc());
        } else if (2 == Integer.valueOf(map.get("type").toString())) {
            map.put("userId", LoginUserHelper.getUserId());
            wfInstAuditTrackService.insertData(map);
            WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            WfItemNode wfItemNode = wfItemNodeService.selectByItemId(itemApply.getItemid());
            /* 如果是第一个审批人 **/
            if (wfInstance.getNodeid().equals(wfItemNode.getId())) {
                map.put("instanceState", InstanceEnum.PENDING_REVIEW.getCode());
                itemApplyFilesService.updateDelState(itemApply.getApplyid());
                wfInstanceService.updateByInstanceId(map);
                itemApply.setStatus(ItemApplyEnum.REVIEW.getCode());
                itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            } else {
                /* 审批节点减一 **/
                WfItemNode wfItemNode1 = wfItemNodeService.selectById(wfInstance.getNodeid());
                map.put("itemId", wfItemNode1.getItemid());
                map.put("order", wfItemNode1.getOrder() - 1);
                WfItemNode wfItemNode2 = wfItemNodeService.selectByOrder(map);
                wfInstance.setNodeid(wfItemNode2.getId());
                wfInstanceService.updateById(wfInstance);
            }
        }
        return ResultUtil.success();
    }

    @Override
    public Result pass(ItemApplyFiles itemApplyFiles) throws Exception {
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(itemApplyFiles.getApplyid());
        if (itemApplyFiles.getFileTypes() == 0) {
            itemApplyFiles.setFileTypes(ItemFileTypeEnum.OPINION_FILE.getCode());
            itemApply.setStatus(ItemApplyEnum.REVIEW_YES.getCode());
            ItemApplyLog itemApplyLog = itemApplyLogService.selectByApplyIdAndStatus(itemApply.getApplyid(), ItemApplyEnum.REVIEW_YES.getCode());
            if (null == itemApplyLog) {
                itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.REVIEW_YES.getCode(), ItemApplyEnum.REVIEW_YES.getDesc());
            } else {
                itemApplyLog.setCreatedDate(new Date());
                itemApplyLogService.updateById(itemApplyLog);
            }
        } else if (itemApplyFiles.getFileTypes() == 1) {
            itemApplyFiles.setFileTypes(ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            itemApply.setStatus(ItemApplyEnum.REVIEW_SUCCESS.getCode());
            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.SUBMISSION.getCode(), ItemApplyEnum.SUBMISSION.getDesc());
            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply.getApplyid(), new Date(), ItemApplyEnum.REVIEW_SUCCESS.getCode(), ItemApplyEnum.REVIEW_SUCCESS.getDesc());
        }
        itemApplyFiles.setCreatedDate(new Date());
        itemApplyFiles.setIsDel(ItemFileTypeEnum.FILE_EXISTENCE.getCode());
        itemApplyFilesService.insert(itemApplyFiles);
        itemApplyMapper.updateByPrimaryKeySelective(itemApply);
        return ResultUtil.success();
    }

    @Override
    public Result notes(Map<String, Object> map) throws Exception {
        // boolean verifyState = false;
        String aid = String.valueOf(map.get("aid"));
        String signature = String.valueOf(map.get("signature"));
        String describe = String.valueOf(map.get("describe"));
            /*if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(describe) && StringUtils.isNotBlank(signature)) {
                verifyState = IkiUtil.verifyData(aid, describe, signature);
            }
            if (!verifyState) {
                return ResultUtil.info("error.code", "signature.error.msg");
            }*/
        return itemApplyFilesService.notes(map);
    }

    @Override
    public PageInfo<ItemApply> waitList(Integer pageNum, int itemId) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userId", LoginUserHelper.getUserId());
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        map.put("realState", UserCardEnum.REAL_SUCCESS.getCode());
        map.put("payStatus", ItemApplyEnum.PAY_ALL.getCode());
        map.put("status", ItemApplyEnum.REVIEW_YES.getCode());
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<ItemApply> itemApplyList = itemApplyMapper.selectWaitList(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public PageInfo<ItemApply> dealList(Integer pageNum, int itemId) throws Exception {
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userId", LoginUserHelper.getUserId());
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        List<ItemApply> itemApplyList = itemApplyMapper.dealList(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public void approved(Map<String, Object> map) throws Exception {
        map.put("userId", LoginUserHelper.getUserId());
        wfInstAuditTrackService.insertApproved(map);
        ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.valueOf(map.get("applyid").toString()));
        WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
        WfItemNode wfItemNode = wfItemNodeService.selectByItemIdDesc(itemApply.getItemid());
        /* 当前节点 不是 审批最后一个节点  当前节点 需 +1 **/
        if (!wfInstance.getNodeid().equals(wfItemNode.getId())) {
            WfItemNode wfItemNode1 = wfItemNodeService.selectById(wfInstance.getNodeid());
            map.put("itemId", wfItemNode1.getItemid());
            map.put("order", wfItemNode1.getOrder() + 1);
            WfItemNode wfItemNode2 = wfItemNodeService.selectByOrder(map);
            wfInstance.setNodeid(wfItemNode2.getId());
        } else {
            wfInstance.setStatus(InstanceEnum.REVIEW_PASS.getCode());
            itemApply.setStatus(ItemApplyEnum.SUBMISSION.getCode());
            itemApplyMapper.updateByPrimaryKeySelective(itemApply);
        }
        wfInstance.setUpdateDate(new Date());
        wfInstanceService.updateById(wfInstance);
    }

    @Override
    public Result updateToDraft(ItemApply itemApply) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("state", ItemApplyEnum.DRAFT.getCode());
        map.put("applyid", itemApply.getApplyid());
        itemApplyMapper.updateState(map);
        return ResultUtil.success();
    }

    @Override
    public Result submitApply(ItemApply itemApply) throws Exception {
        /* 交钱成功之后在修改状态 后续集成支付 **/
        ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
        OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply1.getItemid());
        if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
            /* 固定价格一次付清 **/
            itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
        } else {
            itemApply1.setPayStatus(ItemApplyEnum.PAY_PRE.getCode());
            if (orgItems.getValuation() == OrgItemEnum.SUBJECT_PRICE.getCode()) {
                BigDecimal itemValue = new BigDecimal(itemApply1.getItemValue());
                BigDecimal price = new BigDecimal(orgItems.getPrice());
                /* 按标的总共应收金额 **/
                BigDecimal multiply = itemValue.multiply(price);
                if (multiply.compareTo(new BigDecimal(orgItems.getLowestPrice())) > 0) {
                    /* 费率金额 > 最低收费 **/
                    itemApply1.setPrice(multiply.doubleValue());
                }
            }
        }
        itemApply1.setStatus(ItemApplyEnum.REVIEW.getCode());
        itemApply1.setSubmitDate(new Date());
        WfItemNode wfItemNode = wfItemNodeService.selectByItemId(itemApply1.getItemid());
        WfInstance wfInstance = new WfInstance();
        wfInstance.setCreatedBy(Integer.parseInt(LoginUserHelper.getUserId()));
        wfInstance.setCreatedDate(new Date());
        wfInstance.setNodeid(wfItemNode.getId());
        wfInstance.setStatus(InstanceEnum.PENDING_REVIEW.getCode());
        wfInstanceService.insert(wfInstance);
        itemApply1.setWfInstanceId(wfInstance.getId().toString());
        itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
        itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApply1.getApplyid(), new Date(), ItemApplyEnum.WAIT_PAY.getCode(), ItemApplyEnum.WAIT_PAY.getDesc());
        return ResultUtil.success();
    }

    @Override
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
            /* 输入金额 > 预付款 (要缴纳尾款) **/
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
            /* 输入金额 <= 预付款 通知管理员审核资料 **/
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
    }

    @Override
    public Result payBalance(ItemApply itemApply) throws Exception {
        /* 支付尾款 后续集成支付接口 **/
        ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
        itemApply1.setPayEndPrice(0.00);
        itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
        itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
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
        dataMap.put("payStatus", ItemApplyEnum.PAY_ALL.getCode());
        dataMap.put("status", ItemApplyEnum.REVIEW_YES.getCode());
        dataMap.put("inProcessing", new int[]{ItemApplyEnum.REVIEW.getCode(), ItemApplyEnum.SUBMISSION.getCode()});
        dataMap.put("userId", LoginUserHelper.getUserId());
        dataMap.put("isProcessing", new int[]{ItemApplyEnum.REVIEW_YES.getCode(), ItemApplyEnum.REVIEW_NO.getCode(), ItemApplyEnum.REVIEW_SUCCESS.getCode()});
        Integer newOrderCount = itemApplyMapper.selectOrderCount(ItemApplyEnum.REVIEW.getCode());
        Integer inProcessingCount = itemApplyMapper.inProcessing(dataMap);
        Integer isProcessingCount = itemApplyMapper.isProcessing(dataMap);
        Integer waitApplyCount = itemApplyMapper.selectWaitApplyCount(dataMap);
        Integer myApplyCount = itemApplyMapper.selectMyApplyCount(dataMap);
        List<ItemApply> myApplyList = itemApplyMapper.selectMyApply(dataMap);
        List<ItemApply> newApplyList = itemApplyMapper.selectNewApply(ItemApplyEnum.REVIEW.getCode());
        List<PubArticle> articleList = pubArticleService.articleTop();
        map.put("articleList", articleList);
        map.put("newOrderCount", newOrderCount);
        map.put("inProcessingCount", inProcessingCount);
        map.put("isProcessingCount", isProcessingCount);
        map.put("waitApplyCount", waitApplyCount);
        map.put("myApplyCount", myApplyCount);
        map.put("myApplyList", myApplyList);
        map.put("newApplyList", newApplyList);
        return ResultUtil.success(map);
    }
}
