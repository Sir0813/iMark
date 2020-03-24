package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.*;
import com.dm.user.mapper.OrgItemsMapper;
import com.dm.user.msg.OrgItemEnum;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public OrgItems selectByPrimaryKey(Integer itemid) throws Exception {
        try {
            return orgItemsMapper.selectByPrimaryKey(itemid);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public PageInfo<OrgItems> itemList(Page<OrgItems> page, int orgId) throws Exception {
        try {
            PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
            List<OrgItems> orgItemsList = orgItemsMapper.itemList(orgId);
            PageInfo<OrgItems> pageInfo = new PageInfo<>(orgItemsList);
            if (page.getPageNum() > pageInfo.getPages()) {
                return null;
            }
            return pageInfo;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Map<String, Object> content(int itemId) throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            Map<String, Object> userMap = new LinkedHashMap<>(16);
            OrgItems orgItems = orgItemsMapper.selectByPrimaryKey(itemId);
            List<ItemRequered> list = itemRequeredService.selectByItemId(itemId);
            UserCard userCard = userCardService.selectByUserId(LoginUserHelper.getUserId(), "2");
            AppUser appUser = userService.selectByPrimaryKey(Integer.valueOf(LoginUserHelper.getUserId()));
            if (null == userCard) {
                throw new Exception(StateMsg.NOTREAL);
            }
            if (OrgItemEnum.FIXED_PRICE.getCode() == orgItems.getValuation()) {
                // 固定价格计费 返回本次公正所需费用
                map.put("price", orgItems.getPrice());
            } else {
                // 按标的 或公正人员自定义先缴纳最低价格
                map.put("price", orgItems.getLowestPrice());
            }
            userMap.put("userName", userCard.getRealName());
            userMap.put("userPhone", LoginUserHelper.getUserName());
            userMap.put("userCard", userCard.getCardNumber());
            map.put("valuation", orgItems.getValuation());
            map.put("itemDesc", orgItems.getItemDesc());
            map.put("itemRequered", list);
            map.put("userInfo", userMap);
            map.put("address", StringUtils.isBlank(appUser.getAddress()) ? "" : appUser.getAddress());
            return map;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result typeList() throws Exception {
        try {
            OrgUser orgUser = orgUserService.selectByUserId(Integer.parseInt(LoginUserHelper.getUserId()));
            if (null != orgUser) {
                Map<String, Object> map = new LinkedHashMap<>(16);
                map.put("status", OrgItemEnum.SHELVES.getCode());
                map.put("orgId", orgUser.getOrgid());
                List<OrgItems> orgItemsList = orgItemsMapper.selectByOrgIdAndStatus(map);
                return ResultUtil.success(orgItemsList);
            }
            return null;
        } catch (NumberFormatException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<OrgItems> selectByOrgIdAndStatus(Map<String, Object> map) throws Exception {
        try {
            return orgItemsMapper.selectByOrgIdAndStatus(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
