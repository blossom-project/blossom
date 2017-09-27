package fr.mgargadennec.blossom.module.article;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Locale;

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
