package fr.mgargadennec.blossom.module.article;

import org.hibernate.validator.constraints.NotBlank;

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
