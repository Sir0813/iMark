package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.*;
import com.dm.user.mapper.OrgItemsMapper;
import com.dm.user.msg.OrgItemEnum;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrgItemServiceImpl implements OrgItemService {

    @Autowired
    private OrgItemsMapper orgItemsMapper;

    @Autowired
    private ItemRequeredService itemRequeredService;

    @Autowired
    private UserCardService userCardService;

    @Autowired
    private OrgUserService orgUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService userAddressService;

    @Override
    public OrgItems selectByPrimaryKey(Integer itemid) throws Exception {
        return orgItemsMapper.selectByPrimaryKey(itemid);
    }

    @Override
    public PageInfo<OrgItems> itemList(Integer pageNum, int orgId, String itemName) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("orgId", orgId);
        map.put("itemName", itemName);
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<OrgItems> orgItemsList = orgItemsMapper.itemList(map);
        PageInfo<OrgItems> pageInfo = new PageInfo<>(orgItemsList);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> content(int itemId) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        Map<String, Object> userMap = new LinkedHashMap<>(16);
        UserCard userCard = userCardService.selectByUserId(LoginUserHelper.getUserId(), "2");
        if (null == userCard) {
            throw new Exception(StateMsg.NOTREAL);
        }
        OrgItems orgItems = orgItemsMapper.selectByPrimaryKey(itemId);
        List<ItemRequered> list = itemRequeredService.selectByItemId(itemId);
        if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
            /* 固定价格计费 返回本次公正所需费用 **/
            map.put("price", orgItems.getPrice());
        } else {
            /* 按标的 或公正人员自定义先缴纳最低价格 **/
            map.put("price", orgItems.getLowestPrice());
        }
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
        userMap.put("userName", userCard.getRealName());
        userMap.put("userPhone", LoginUserHelper.getUserName());
        userMap.put("userCard", userCard.getCardNumber());
        map.put("valuation", orgItems.getValuation());
        map.put("itemDesc", orgItems.getItemDesc());
        map.put("itemRequered", list);
        map.put("userInfo", userMap);
        return map;
    }

    @Override
    public Result typeList() throws Exception {
        OrgUser orgUser = orgUserService.selectByUserId(Integer.parseInt(LoginUserHelper.getUserId()));
        if (null != orgUser) {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("status", OrgItemEnum.SHELVES.getCode());
            map.put("orgId", orgUser.getOrgid());
            List<OrgItems> orgItemsList = orgItemsMapper.selectByOrgIdAndStatus(map);
            return ResultUtil.success(orgItemsList);
        }
        return null;
    }

    @Override
    public List<OrgItems> selectByOrgIdAndStatus(Map<String, Object> map) throws Exception {
        return orgItemsMapper.selectByOrgIdAndStatus(map);
    }

    @Override
    public OrgItems selectByItemCode(String itemCode) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("itemCode", itemCode);
        map.put("status", OrgItemEnum.SHELVES.getCode());
        return orgItemsMapper.selectByItemCode(map);
    }
}
