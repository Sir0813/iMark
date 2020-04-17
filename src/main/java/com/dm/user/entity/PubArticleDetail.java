package com.dm.user.entity;

public class PubArticleDetail {
    private Integer id;

    private Integer articleId;

    private Integer revise;

    private String content;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getRevise() {
        return revise;
    }

    public void setRevise(Integer revise) {
        this.revise = revise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}