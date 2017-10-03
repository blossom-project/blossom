package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by Maël Gargadennnec on 22/05/2017.
 */
@Entity
@Table(name = "blossom_file_content")
public class FileContent extends AbstractEntity {

  @Column(name = "file_id")
  private Long fileId;

  @Lob
  @Column(name = "data")
  private byte[] data;

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
