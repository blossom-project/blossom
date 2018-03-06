package com.blossomproject.module.article;

import javax.validation.constraints.NotBlank;

public class ArticleUpdateForm {

    public ArticleUpdateForm() {
    }

    public ArticleUpdateForm(ArticleDTO article) {
        this.name = article.getName();
        this.description=article.getDescription();
        this.viewable=article.isViewable();
        this.content=article.getContent();
    }

    @NotBlank(message = "{articles.article.validation.name.NotBlank.message}")
    private String name = "";

    private String description;

    private String content;

    private boolean viewable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isViewable() {
        return viewable;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }
}
