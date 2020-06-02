package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.ApplySupplement;
import com.dm.user.entity.ApplySupplementVo;

public interface ApplySupplementService {

    /**
     * 补充材料
     *
     * @param applySupplement
     * @return
     */
    Result add(ApplySupplement applySupplement) throws Exception;

    Result userApplySupplement(ApplySupplementVo applySupplementVo) throws Exception;

}
