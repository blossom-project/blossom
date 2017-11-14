package fr.blossom.core.role;

import fr.blossom.core.common.entity.AbstractEntity;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "blossom_role")
public class Role extends AbstractEntity {

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Lob
  @Column(name = "description", nullable = false)
  private String description;

  @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
  @CollectionTable(name="blossom_role_privileges", joinColumns = @JoinColumn(name="id", referencedColumnName = "id"))
  @Column(name="privilege")
  private List<String> privileges;

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

  public List<String> getPrivileges() {
    return privileges;
  }

  public void setPrivileges(List<String> privileges) {
    this.privileges = privileges;
  }

  @Override
  public String toString() {
    return "Role{name='" + name + "\', description='" + description + "\'}";
  }
}
