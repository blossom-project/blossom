
package com.blossomproject.module.article;

import com.blossomproject.core.common.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "blossom_article")
public class Article extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Lob
  @Column(name = "description")
  private String description;

  @Lob
  @Column(name = "content")
  private String content;

  @Column(name="viewable")
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
