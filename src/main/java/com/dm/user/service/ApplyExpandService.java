package com.dm.user.service;

import com.dm.user.entity.ApplyExpand;

public interface ApplyExpandService {

    void insert(ApplyExpand applyExpand);

    void update(ApplyExpand applyExpand);

    ApplyExpand selectByApplyId(int applyid);
}
