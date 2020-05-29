package com.dm.user.mapper;

import com.dm.user.entity.BizItemApply;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * app申请公证表(BizItemApply)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-28 17:34:23
 */
public interface BizItemApplyDao {

    /**
     * 通过ID查询单条数据
     *
     * @param applyid 主键
     * @return 实例对象
     */
    BizItemApply queryById(Integer applyid);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<BizItemApply> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param bizItemApply 实例对象
     * @return 对象列表
     */
    List<BizItemApply> queryAll(BizItemApply bizItemApply);

    /**
     * 新增数据
     *
     * @param bizItemApply 实例对象
     * @return 影响行数
     */
    int insert(BizItemApply bizItemApply);

    /**
     * 修改数据
     *
     * @param bizItemApply 实例对象
     * @return 影响行数
     */
    int update(BizItemApply bizItemApply);

    /**
     * 通过主键删除数据
     *
     * @param applyid 主键
     * @return 影响行数
     */
    int deleteById(Integer applyid);

}
