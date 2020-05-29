package com.dm.user.service.impl;

import com.dm.user.entity.BizItemApply;
import com.dm.user.mapper.BizItemApplyDao;
import com.dm.user.service.BizItemApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * app申请公证表(BizItemApply)表服务实现类
 *
 * @author makejava
 * @since 2020-05-28 17:34:24
 */
@Service("bizItemApplyService")
public class BizItemApplyServiceImpl implements BizItemApplyService {
    @Resource
    private BizItemApplyDao bizItemApplyDao;

    /**
     * 通过ID查询单条数据
     *
     * @param applyid 主键
     * @return 实例对象
     */
    @Override
    public BizItemApply queryById(Integer applyid) {
        return this.bizItemApplyDao.queryById(applyid);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<BizItemApply> queryAllByLimit(int offset, int limit) {
        return this.bizItemApplyDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param bizItemApply 实例对象
     * @return 实例对象
     */
    @Override
    public BizItemApply insert(BizItemApply bizItemApply) {
        this.bizItemApplyDao.insert(bizItemApply);
        return bizItemApply;
    }

    /**
     * 修改数据
     *
     * @param bizItemApply 实例对象
     * @return 实例对象
     */
    @Override
    public BizItemApply update(BizItemApply bizItemApply) {
        this.bizItemApplyDao.update(bizItemApply);
        return this.queryById(bizItemApply.getApplyid());
    }

    /**
     * 通过主键删除数据
     *
     * @param applyid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer applyid) {
        return this.bizItemApplyDao.deleteById(applyid) > 0;
    }
}
