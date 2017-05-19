package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class FileDTO extends AbstractDTO {

  private String name;
  private String path;
  private String type;
  private String extension;
  private Long size;
  private List<String> tags;
  private String hash;
  private String hashAlgorithm;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getHashAlgorithm() {
    return hashAlgorithm;
  }

  public void setHashAlgorithm(String hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
  }
}
