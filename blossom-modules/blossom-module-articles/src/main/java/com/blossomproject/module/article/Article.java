
package com.blossomproject.module.article;

import com.blossomproject.core.common.entity.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "blossom_article")
public class Article extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Lob
  @Column(name = "summary")
  private String summary;

  @Lob
  @Column(name = "content")
  private String content;

  @Column(name = "status") @Enumerated(EnumType.STRING)
  private Status status;

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public enum Status {
    DRAFT, PUBLISHED, HIDDEN;
  }
}
