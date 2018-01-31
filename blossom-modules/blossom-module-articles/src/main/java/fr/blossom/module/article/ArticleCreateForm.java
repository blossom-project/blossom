package fr.blossom.module.article;


import javax.validation.constraints.NotBlank;

public class ArticleCreateForm {

    @NotBlank(message = "{articles.article.validation.name.NotBlank.message}")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
