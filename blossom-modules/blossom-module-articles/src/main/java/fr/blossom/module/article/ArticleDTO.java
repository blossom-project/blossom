package fr.blossom.module.article;

import fr.blossom.core.common.dto.AbstractDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArticleDTO extends AbstractDTO {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
