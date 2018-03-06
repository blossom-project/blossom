package com.blossomproject.module.article;


import javax.validation.constraints.NotBlank;

public class ArticleCreateForm {

    @NotBlank(message = "{articles.article.validation.name.NotBlank.message}")
    private String name = "";

    private String description;

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
}
