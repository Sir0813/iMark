package com.dm.user.entity;

/**
 * @author cui
 * @date 2019-09-26
 */
public class CertTemplate {

    private Integer templateId;

    private String templateTitle;

    private String templateUrl;

    /** 1劳动模板2租赁模板 */
    private String templateType;

    private String templatePath;

    private String templateEditUrl;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle == null ? null : templateTitle.trim();
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl == null ? null : templateUrl.trim();
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType == null ? null : templateType.trim();
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath == null ? null : templatePath.trim();
    }

    public String getTemplateEditUrl() {
        return templateEditUrl;
    }

    public void setTemplateEditUrl(String templateEditUrl) {
        this.templateEditUrl = templateEditUrl == null ? null : templateEditUrl.trim();
    }
}