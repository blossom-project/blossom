
package fr.mgargadennec.blossom.module.article;

import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bo_article")
public class Article extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
