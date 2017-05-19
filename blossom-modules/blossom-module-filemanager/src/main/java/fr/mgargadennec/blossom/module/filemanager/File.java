package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "bo_file")
public class File extends AbstractEntity {

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "path")
  private String path;

  @Column(name = "type", nullable = false)
  private String type;

  @Column(name = "extension", nullable = false)
  private String extension;

  @Column(name = "size", nullable = false)
  private Long size;

  @ElementCollection
  private List<String> tags;

  @Column(name = "hash", nullable = false)
  private String hash;

  @Column(name = "hash_algorithm", nullable = false)
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
