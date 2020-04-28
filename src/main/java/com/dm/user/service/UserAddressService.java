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
}
