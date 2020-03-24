package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.*;
import com.dm.user.mapper.ItemApplyMapper;
import com.dm.user.msg.*;
import com.dm.user.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Result insert(ItemApply itemApply) throws Exception {
        try {
            boolean insert = null == itemApply.getApplyid();
            // 按标的计价 计算用户应交总费用
            if (itemApply.getItemValue() > 0) {
                OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
                BigDecimal itemValue = new BigDecimal(itemApply.getItemValue());
                BigDecimal price = new BigDecimal(orgItems.getPrice());
                // 按标的总共应收金额
                BigDecimal multiply = itemValue.multiply(price);
                if (multiply.compareTo(new BigDecimal(orgItems.getLowestPrice())) > -1) {
                    // 按标的应收金额大于等于最低收费金额
                    itemApply.setPrice(multiply.doubleValue());
                } else {
                    itemApply.setPrice(orgItems.getLowestPrice());
                }
            }
            if (insert) {
                SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssSSS");
                Date date = new Date();
                String format1 = format.format(date);
                itemApply.setApplyNo(format1);
                itemApply.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
                itemApply.setCreatedDate(new Date());
                itemApply.setSubmitDate(new Date());
                itemApplyMapper.insertSelective(itemApply);
            }
            List<ApplyFile> applyFileList = itemApply.getApplyFileList();
            for (int i = 0; i < applyFileList.size(); i++) {
                ApplyFile applyFile = applyFileList.get(i);
                String requeredid = applyFile.getRequeredid();
                String fileid = applyFile.getFileid();
                String[] split = fileid.split(",");
                if (ItemApplyEnum.DRAFT.getCode() == itemApply.getStatus() || ItemApplyEnum.REVIEW.getCode() == itemApply.getStatus()) {
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
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public PageInfo<ItemApply> list(int pageNum) throws Exception {
        try {
            PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
            List<ItemApply> itemApplyList = itemApplyMapper.list(Integer.parseInt(LoginUserHelper.getUserId()));
            PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
            if (pageNum > pageInfo.getPages()) {
                return null;
            }
            return pageInfo;
        } catch (NumberFormatException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Map<String, Object> detail(int applyid) throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            Map<String, Object> userMap = new LinkedHashMap<>(16);
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(applyid);
            OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getItemid());
            List<ItemRequered> list = itemRequeredService.selectByItemId(itemApply.getItemid());
            UserCard userCard = userCardService.selectByUserId(itemApply.getUserid().toString(), "2");
            AppUser user = userService.selectByPrimaryKey(itemApply.getUserid());
            /**
             * 用户提交的文件清单
             */
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
                    String logoUrl = itemRequered.getLogoUrl();
                    JSONArray json = JSONArray.fromObject(logoUrl);
                    if (cfList.size() > 0) {
                        for (int j = 0; j < cfList.size(); j++) {
                            if (json.size() > 0) {
                                CertFiles certFiles = cfList.get(j);
                                for (int k = 0; k < json.size(); k++) {
                                    if (cfList.size() == json.size()) {
                                        JSONObject job = json.getJSONObject(j);
                                        certFiles.setDetail(job.get("describe").toString());
                                        break;
                                    } else {
                                        certFiles.setDetail(json.getJSONObject(k).get("describe").toString());
                                    }
                                }
                            }
                        }
                    }
                    itemRequered.setCertFilesList(cfList);
                }
            }
            WfInstance wfInstance = new WfInstance();
            if (ItemApplyEnum.REVIEW_NO.getCode() == itemApply.getStatus()) {
                wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
            }
            if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
                // 固定价格计费 返回本次公正所需费用
                map.put("price", orgItems.getPrice());
            } else {
                // 按标的 或公正人员自定义先缴纳最低价格
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
                // 审核历史记录
                applyHistories = itemApplyMapper.history(applyid);
            }
            userMap.put("userName", null == userCard ? "" : userCard.getRealName());
            userMap.put("userPhone", user.getUsername());
            userMap.put("userCard", null == userCard ? "" : userCard.getCardNumber());
            Org org = orgService.selectById(orgItems.getOrgid());
            map.put("orgName", org.getOrgname());
            // 公正意见书
            map.put("opinionFile", certFiles);
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
            return map;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result delete(int applyid) throws Exception {
        try {
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
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result pendList(Page<ItemApply> page, int itemId) throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("status", ItemApplyEnum.REVIEW.getCode());
            List<ItemApply> itemApplyList = new ArrayList<>();
            if (itemId != 0) {
                map.put("itemId", itemId);
                PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
                itemApplyList = itemApplyMapper.pendList(map);
            } else {
                OrgUser orgUser = orgUserService.selectByUserId(Integer.parseInt(LoginUserHelper.getUserId()));
                if (null != orgUser) {
                    Map<String, Object> linkedHashMap = new LinkedHashMap<>(16);
                    linkedHashMap.put("status", OrgItemEnum.SHELVES.getCode());
                    linkedHashMap.put("orgId", orgUser.getOrgid());
                    List<OrgItems> orgItemsList = orgItemService.selectByOrgIdAndStatus(linkedHashMap);
                    int[] ids = new int[orgItemsList.size()];
                    for (int i = 0; i < orgItemsList.size(); i++) {
                        OrgItems orgItems = orgItemsList.get(i);
                        ids[i] = orgItems.getItemid();
                    }
                    map.put("itemIds", ids);
                    PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
                    itemApplyList = itemApplyMapper.pendList(map);
                }
            }
            PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
            if (page.getPageNum() > pageInfo.getPages()) {
                return ResultUtil.success();
            }
            return ResultUtil.success(pageInfo);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public synchronized Result takeOrder(ItemApply itemApply) throws Exception {
        try {
            ItemApply ia = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
            if (ia.getHandleUserid() != 0) {
                throw new Exception(StateMsg.TAKE_ORDER_ERROR_MSG);
            }
            ia.setHandleCreatedDate(new Date());
            ia.setHandleUserid(Integer.parseInt(LoginUserHelper.getUserId()));
            itemApplyMapper.updateByPrimaryKeySelective(ia);
            return ResultUtil.success();
        } catch (NumberFormatException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result pendReview(Page<ItemApply> page, int itemId, int type) throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("userId", LoginUserHelper.getUserId());
            if (1 == type) {
                // 待审核列表
                int[] ids = new int[2];
                ids[0] = ItemApplyEnum.REVIEW.getCode();
                ids[1] = ItemApplyEnum.SUBMISSION.getCode();
                map.put("status", ids);
            } else if (2 == type) {
                // 审核通过退回列表(历史记录)
                int[] ids = new int[3];
                ids[0] = ItemApplyEnum.REVIEW_YES.getCode();
                ids[1] = ItemApplyEnum.REVIEW_NO.getCode();
                ids[2] = ItemApplyEnum.REVIEW_SUCCESS.getCode();
                map.put("status", ids);
            } else {
                throw new Exception("状态错误");
            }
            List<ItemApply> itemApplyList = new ArrayList<>();
            if (itemId != 0) {
                // 查询全部数据
                map.put("itemId", itemId);
                PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
                itemApplyList = itemApplyMapper.pendReview(map);
            } else {
                OrgUser orgUser = orgUserService.selectByUserId(Integer.parseInt(LoginUserHelper.getUserId()));
                if (null != orgUser) {
                    Map<String, Object> linkedHashMap = new LinkedHashMap<>(16);
                    linkedHashMap.put("status", OrgItemEnum.SHELVES.getCode());
                    linkedHashMap.put("orgId", orgUser.getOrgid());
                    List<OrgItems> orgItemsList = orgItemService.selectByOrgIdAndStatus(linkedHashMap);
                    int[] ids = new int[orgItemsList.size()];
                    for (int i = 0; i < orgItemsList.size(); i++) {
                        OrgItems orgItems = orgItemsList.get(i);
                        ids[i] = orgItems.getItemid();
                    }
                    map.put("itemIds", ids);
                    PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
                    itemApplyList = itemApplyMapper.pendReview(map);
                }
            }
            PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
            if (page.getPageNum() > pageInfo.getPages()) {
                return ResultUtil.success();
            }
            return ResultUtil.success(pageInfo);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result rejectReason(Map<String, Object> map) throws Exception {
        try {
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(Integer.parseInt(map.get("applyid").toString()));
            if (1 == Integer.valueOf(map.get("type").toString())) {
                itemApply.setStatus(ItemApplyEnum.REVIEW_NO.getCode());
                WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
                wfInstance.setRejectReason(map.get("rejectReason").toString());
                wfInstance.setStatus(InstanceEnum.REVIEW_NOT_PASS.getCode());
                wfInstance.setUpdateDate(new Date());
                wfInstanceService.updateById(wfInstance);
                itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            } else if (2 == Integer.valueOf(map.get("type").toString())) {
                map.put("userId", LoginUserHelper.getUserId());
                wfInstAuditTrackService.insertData(map);
                WfInstance wfInstance = wfInstanceService.selectById(itemApply.getWfInstanceId());
                WfItemNode wfItemNode = wfItemNodeService.selectByItemId(itemApply.getItemid());
                // 如果是第一个审批人
                if (wfInstance.getNodeid().equals(wfItemNode.getId())) {
                    map.put("instanceState", InstanceEnum.PENDING_REVIEW.getCode());
                    itemApplyFilesService.updateDelState(itemApply.getApplyid());
                    wfInstanceService.updateByInstanceId(map);
                    itemApply.setStatus(ItemApplyEnum.REVIEW.getCode());
                    itemApplyMapper.updateByPrimaryKeySelective(itemApply);
                } else {
                    // 审批节点减一
                    WfItemNode wfItemNode1 = wfItemNodeService.selectById(wfInstance.getNodeid());
                    map.put("itemId", wfItemNode1.getItemid());
                    map.put("order", wfItemNode1.getOrder() - 1);
                    WfItemNode wfItemNode2 = wfItemNodeService.selectByOrder(map);
                    wfInstance.setNodeid(wfItemNode2.getId());
                    wfInstanceService.updateById(wfInstance);
                }
            }
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result pass(ItemApplyFiles itemApplyFiles) throws Exception {
        try {
            ItemApply itemApply = itemApplyMapper.selectByPrimaryKey(itemApplyFiles.getApplyid());
            if (itemApplyFiles.getFileTypes() == 0) {
                itemApplyFiles.setFileTypes(ItemFileTypeEnum.OPINION_FILE.getCode());
                itemApply.setStatus(ItemApplyEnum.REVIEW_YES.getCode());
            } else if (itemApplyFiles.getFileTypes() == 1) {
                itemApplyFiles.setFileTypes(ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
                itemApply.setStatus(ItemApplyEnum.REVIEW_SUCCESS.getCode());
            }
            itemApplyFiles.setCreatedDate(new Date());
            itemApplyFiles.setIsDel(ItemFileTypeEnum.FILE_EXISTENCE.getCode());
            itemApplyFilesService.insert(itemApplyFiles);
            itemApplyMapper.updateByPrimaryKeySelective(itemApply);
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result notes(Map<String, Object> map) throws Exception {
        try {
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
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public PageInfo<ItemApply> waitList(Page<ItemApply> page, int itemId) throws Exception {
        PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userId", LoginUserHelper.getUserId());
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        List<ItemApply> itemApplyList = itemApplyMapper.selectWaitList(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (page.getPageNum() > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public PageInfo<ItemApply> dealList(Page<ItemApply> page, int itemId) throws Exception {
        PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userId", LoginUserHelper.getUserId());
        if (itemId != 0) {
            map.put("itemId", itemId);
        }
        List<ItemApply> itemApplyList = itemApplyMapper.dealList(map);
        PageInfo<ItemApply> pageInfo = new PageInfo<>(itemApplyList);
        if (page.getPageNum() > pageInfo.getPages()) {
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
        // 当前节点 不是 审批最后一个节点  当前节点 需 +1
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
        try {
            Map<String, Object> map = new HashMap<>(16);
            map.put("state", ItemApplyEnum.DRAFT.getCode());
            map.put("applyid", itemApply.getApplyid());
            itemApplyMapper.updateState(map);
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result submitApply(ItemApply itemApply) throws Exception {
        try {
            // 交钱成功之后在修改状态 后续集成支付
            ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
            OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getApplyid());
            if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
                itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
            } else {
                itemApply1.setPayStatus(ItemApplyEnum.PAY_PRE.getCode());
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
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result submitCustomPrice(ItemApply itemApply) throws Exception {
        try {
            ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
            OrgItems orgItems = orgItemService.selectByPrimaryKey(itemApply.getApplyid());
            itemApply1.setPrice(itemApply.getPrice());
            BigDecimal price = new BigDecimal(itemApply.getPrice());
            BigDecimal lowestPrice = new BigDecimal(orgItems.getLowestPrice());
            PushMsg pushMsg = new PushMsg();
            if (price.compareTo(lowestPrice) == 1) {
                // 输入金额 > 预付款 (要缴纳尾款)
                itemApply1.setPayEndPrice(price.subtract(lowestPrice).doubleValue());
                itemApply1.setPayStatus(ItemApplyEnum.PAY_BALANCE.getCode());
                pushMsg.setCertFicateId(itemApply1.getApplyid().toString());
                pushMsg.setTitle("支付通知");
                pushMsg.setContent("订单号:" + itemApply1.getApplyNo() + "支付尾款通知！待支付金额:" + itemApply1.getPayEndPrice());
                pushMsg.setServerTime(new Date().toString());
                pushMsg.setType(PushEnum.PAY_MSG.getCode());
                pushMsg.setState("1"); // 成功
                pushMsg.setIsRead("0"); // 未读取
                pushMsg.setUserId(itemApply1.getUserid());
            } else {
                // 输入金额 <= 预付款 通知管理员审核资料
                itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
                pushMsg.setTitle("审核通知");
                pushMsg.setContent("订单号:" + itemApply1.getApplyNo() + "尾款结清，请上传意见书！");
                pushMsg.setServerTime(new Date().toString());
                pushMsg.setType(PushEnum.REVIEW_MSG.getCode());
                pushMsg.setState("1");
                pushMsg.setIsRead("0");
                pushMsg.setUserId(Integer.valueOf(LoginUserHelper.getUserId()));
            }
            pushMsgService.insertSelective(pushMsg);
            itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result payBalance(ItemApply itemApply) throws Exception {
        // 支付尾款 后续集成支付接口
        try {
            ItemApply itemApply1 = itemApplyMapper.selectByPrimaryKey(itemApply.getApplyid());
            itemApply1.setPayEndPrice(0.00);
            itemApply1.setPayStatus(ItemApplyEnum.PAY_ALL.getCode());
            itemApplyMapper.updateByPrimaryKeySelective(itemApply1);
            PushMsg pushMsg = new PushMsg();
            pushMsg.setCertFicateId(itemApply1.getApplyid().toString());
            pushMsg.setTitle("审核通知");
            pushMsg.setContent("订单号:" + itemApply1.getApplyNo() + "尾款结清，请上传意见书！");
            pushMsg.setServerTime(new Date().toString());
            pushMsg.setType(PushEnum.REVIEW_MSG.getCode());
            pushMsg.setState("1"); // 成功
            pushMsg.setIsRead("0"); // 未读取
            pushMsg.setUserId(itemApply1.getHandleUserid());
            pushMsgService.insertSelective(pushMsg);
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
