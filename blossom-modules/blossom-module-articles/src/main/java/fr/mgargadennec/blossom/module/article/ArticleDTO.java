package fr.mgargadennec.blossom.module.article;

import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
