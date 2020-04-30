package com.dm.user.service;

import com.dm.user.entity.UserAddress;

import java.util.List;

public interface UserAddressService {

    /**
     * 地址管理列表
     *
     * @return
     */
    List<UserAddress> list(String userId) throws Exception;

    /**
     * 新增
     *
     * @param userAddress
     * @throws Exception
     */
    void insert(UserAddress userAddress) throws Exception;

    /**
     * 删除
     *
     * @param id
     */
    void delete(Integer id) throws Exception;

    /**
     * 默认地址查询
     *
     * @param userId
     * @param status
     * @return
     */
    UserAddress selectByUserIdAndStatus(String userId, String status);

}
