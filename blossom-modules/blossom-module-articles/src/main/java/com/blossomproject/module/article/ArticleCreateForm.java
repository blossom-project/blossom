package com.blossomproject.module.article;


import javax.validation.constraints.NotBlank;

public class ArticleCreateForm {

    @NotBlank(message = "{articles.article.validation.name.NotBlank.message}")
    private String name = "";

    private String summary;

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
}
