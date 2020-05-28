package com.dm.user.mapper;

import com.dm.user.entity.BizCertFiles;
import com.dm.user.entity.BizCertFilesVo;
import com.dm.user.entity.BizItemAttachment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 审核人员上传附件表
(BizItemAttachment)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-28 12:41:20
 */
public interface BizItemAttachmentDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BizItemAttachment queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<BizItemAttachment> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param bizItemAttachment 实例对象
     * @return 对象列表
     */
    List<BizItemAttachment> queryAll(BizItemAttachment bizItemAttachment);

    /**
     * 新增数据
     *
     * @param bizItemAttachment 实例对象
     * @return 影响行数
     */
    int insert(BizItemAttachment bizItemAttachment);
    int insertSelective(BizItemAttachment bizItemAttachment);

    /**
     * 修改数据
     *
     * @param bizItemAttachment 实例对象
     * @return 影响行数
     */
    int update(BizItemAttachment bizItemAttachment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    List<BizCertFilesVo> getFlieList(@Param("applyId") Integer applyId);
}
