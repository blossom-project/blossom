package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bo_file")
public class File extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "content_type", nullable = false)
  private String contentType;

  @Column(name = "extension", nullable = false)
  private String extension;

  @Column(name = "size", nullable = false)
  private Long size;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> tags;

  @Column(name = "hash", nullable = false, updatable = false)
  private String hash;

  @Column(name = "hash_algorithm", nullable = false, updatable = false)
  private String hashAlgorithm;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String type) {
    this.contentType = type;
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
