package com.dm.user.service;

import com.dm.user.entity.BizCertFilesVo;
import com.dm.user.entity.BizItemAttachment;

import java.util.List;

/**
 * 审核人员上传附件表
(BizItemAttachment)表服务接口
 *
 * @author makejava
 * @since 2020-05-27 18:47:11
 */
public interface BizItemAttachmentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BizItemAttachment queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<BizItemAttachment> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param bizItemAttachment 实例对象
     * @return 实例对象
     */
    BizItemAttachment insert(BizItemAttachment bizItemAttachment);

    int insertSelective(BizItemAttachment bizItemAttachment);

    /**
     * 修改数据
     *
     * @param bizItemAttachment 实例对象
     * @return 实例对象
     */
    BizItemAttachment update(BizItemAttachment bizItemAttachment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<BizCertFilesVo> getBizItemAttachmentInfo(Integer applyId);
}
