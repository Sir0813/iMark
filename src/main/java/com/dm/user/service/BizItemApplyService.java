package com.dm.user.service;

import com.dm.user.entity.BizItemApply;
import java.util.List;

/**
 * app申请公证表(BizItemApply)表服务接口
 *
 * @author makejava
 * @since 2020-05-28 17:34:24
 */
public interface BizItemApplyService {

    /**
     * 通过ID查询单条数据
     *
     * @param applyid 主键
     * @return 实例对象
     */
    BizItemApply queryById(Integer applyid);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<BizItemApply> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param bizItemApply 实例对象
     * @return 实例对象
     */
    BizItemApply insert(BizItemApply bizItemApply);

    /**
     * 修改数据
     *
     * @param bizItemApply 实例对象
     * @return 实例对象
     */
    BizItemApply update(BizItemApply bizItemApply);

    /**
     * 通过主键删除数据
     *
     * @param applyid 主键
     * @return 是否成功
     */
    boolean deleteById(Integer applyid);

}