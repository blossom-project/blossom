/**
 *
 */
package fr.mgargadennec.blossom.core.common.model.common;

import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity(name = "Revinfo")
@Table(name = "REVINFO")
@RevisionEntity
public class BlossomRevisionEntity {

  @Id
  @GeneratedValue
  @Column(name = "REV")
  @RevisionNumber
  private int id;

  @Column(name = "REVTSTMP")
  @RevisionTimestamp
  private long timestamp;

  private String user;

  @ElementCollection
  @JoinTable(name = "REVCHANGES", joinColumns = @JoinColumn(name = "REV"))
  @Column(name = "ENTITYNAME")
  @ModifiedEntityNames
  private Set<String> modifiedEntityNames;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Transient
  public Date getRevisionDate() {
    return new Date(timestamp);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Set<String> getModifiedEntityNames() {
    return modifiedEntityNames;
  }

  public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
    this.modifiedEntityNames = modifiedEntityNames;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BlossomRevisionEntity)) {
      return false;
    }

    final BlossomRevisionEntity that = (BlossomRevisionEntity) o;
    return id == that.id && timestamp == that.timestamp;
  }

  @Override
  public int hashCode() {
    int result;
    result = id;
    result = 31 * result + (int) (timestamp ^ timestamp >>> 32);
    return result;
  }

  @Override
  public String toString() {
    return "BlossomRevisionEntity(id = " + id + ", revisionDate = "
        + DateFormat.getDateTimeInstance().format(getRevisionDate()) + ")";
  }
}
