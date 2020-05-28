package com.dm.user.service.impl;

import com.dm.user.entity.BizCertFiles;
import com.dm.user.entity.BizCertFilesVo;
import com.dm.user.entity.BizItemAttachment;
import com.dm.user.mapper.BizItemAttachmentDao;
import com.dm.user.service.BizItemAttachmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审核人员上传附件表
(BizItemAttachment)表服务实现类
 *
 * @author makejava
 * @since 2020-05-27 18:47:11
 */
@Service("bizItemAttachmentService")
public class BizItemAttachmentServiceImpl implements BizItemAttachmentService {
    @Resource
    private BizItemAttachmentDao bizItemAttachmentDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BizItemAttachment queryById(Integer id) {
        return this.bizItemAttachmentDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<BizItemAttachment> queryAllByLimit(int offset, int limit) {
        return this.bizItemAttachmentDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param bizItemAttachment 实例对象
     * @return 实例对象
     */
    @Override
    public BizItemAttachment insert(BizItemAttachment bizItemAttachment) {
        this.bizItemAttachmentDao.insert(bizItemAttachment);
        return bizItemAttachment;
    }

    @Override
    public int insertSelective(BizItemAttachment bizItemAttachment) {
        return bizItemAttachmentDao.insertSelective(bizItemAttachment);
    }

    /**
     * 修改数据
     *
     * @param bizItemAttachment 实例对象
     * @return 实例对象
     */
    @Override
    public BizItemAttachment update(BizItemAttachment bizItemAttachment) {
        this.bizItemAttachmentDao.update(bizItemAttachment);
        return this.queryById(bizItemAttachment.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.bizItemAttachmentDao.deleteById(id) > 0;
    }

    @Override
    public List<BizCertFilesVo> getBizItemAttachmentInfo(Integer applyId) {
        List<BizCertFilesVo> fileList = bizItemAttachmentDao.getFlieList(applyId);
        return fileList;
    }
}
