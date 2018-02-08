package com.blossom_project.module.article;

import javax.validation.constraints.NotBlank;

public class ArticleUpdateForm {

    public ArticleUpdateForm() {
    }

    public ArticleUpdateForm(ArticleDTO article) {
        this.name = article.getName();
    }

    @NotBlank(message = "{articles.article.validation.name.NotBlank.message}")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
