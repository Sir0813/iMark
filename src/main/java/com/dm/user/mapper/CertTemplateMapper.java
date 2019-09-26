package com.dm.user.mapper;

import com.dm.user.entity.CertTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface CertTemplateMapper {
    /**
     * 根据ID删除
     * @param templateId
     * @return
     */
    int deleteByPrimaryKey(Integer templateId);

    /**
     * 新增
     * @param record
     * @return
     */
    int insertSelective(CertTemplate record);

    /**
     * 根据ID查询
     * @param templateId
     * @return
     */
    CertTemplate selectByPrimaryKey(Integer templateId);

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CertTemplate record);

    /**
     * 查询机集合
     * @return
     */
    List<CertTemplate> list();

    /**
     * 根据类型查询模板
     * @param type
     * @return
     */
    CertTemplate getByTemplateType(String type);
}