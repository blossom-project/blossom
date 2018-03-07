package com.blossomproject.module.article;

import javax.validation.constraints.NotBlank;

public class ArticleUpdateForm {

    public ArticleUpdateForm() {
    }

    public ArticleUpdateForm(ArticleDTO article) {
        this.name = article.getName();
        this.summary = article.getSummary();
        this.status = article.getStatus();
        this.content = article.getContent();
    }

    @NotBlank(message = "{articles.article.validation.name.NotBlank.message}")
    private String name = "";

    private String summary;

    private String content;

    private Article.Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article.Status getStatus() {
        return status;
    }

    public void setStatus(Article.Status status) {
        this.status = status;
    }
}
