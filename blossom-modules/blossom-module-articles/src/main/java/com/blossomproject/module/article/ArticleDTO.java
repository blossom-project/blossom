package com.blossomproject.module.article;

import com.blossomproject.core.common.dto.AbstractDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticleDTO extends AbstractDTO {

  private String name;
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
