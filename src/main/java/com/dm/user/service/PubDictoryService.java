package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;

public interface PubDictoryService {

    /**
     * 查询所有国家
     *
     * @return
     */
    Result selectCountry() throws Exception;

    /**
     * 国家下语言
     *
     * @param id
     * @return
     */
    Result selectCountryLanguage(Integer id) throws Exception;
}
