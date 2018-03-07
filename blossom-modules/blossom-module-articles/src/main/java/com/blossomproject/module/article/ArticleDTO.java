package com.blossomproject.module.article;

import com.blossomproject.core.common.dto.AbstractDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticleDTO extends AbstractDTO {

  private String name;
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
