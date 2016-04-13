package fr.mgargadennec.blossom.core.common.support.history;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.RevisionType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

public class BlossomHistoryDTO<E extends BlossomAbstractEntity> {

  private Long entityId;
  private String entityName;
  private String entityClass;

  private int revisionId;
  private RevisionType revisionType;
  private long revisionTimestamp;

  private String userModification;
  private String userCreation;

  private List<BlossomHistoryChangeDTO> diff;

  private E beforeEntity;
  private E afterEntity;

  private boolean association;
  private boolean linked;

  private Map<String, Object> extraInformations = new HashMap<String, Object>();

  public BlossomHistoryDTO() {
    this.association = false;
    this.linked = false;
  }

  public BlossomHistoryDTO(boolean association, boolean linked) {
    this.association = association;
    this.linked = linked;
  }

  /**
   * @return the revisionType
   */
  public RevisionType getRevisionType() {
    return revisionType;
  }

  /**
   * @param revisionType the revisionType to set
   */
  public void setRevisionType(RevisionType revisionType) {
    this.revisionType = revisionType;
  }

  /**
   * @return the revisionTimestamp
   */
  public long getRevisionTimestamp() {
    return revisionTimestamp;
  }

  /**
   * @param revisionTimestamp the revisionTimestamp to set
   */
  public void setRevisionTimestamp(long revisionTimestamp) {
    this.revisionTimestamp = revisionTimestamp;
  }

  /**
   * @return the entityName
   */
  public String getEntityName() {
    return entityName;
  }

  /**
   * @param entityName the entityName to set
   */
  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  /**
   * @return the entityId
   */
  public Long getEntityId() {
    return entityId;
  }

  /**
   * @param entityId the id to set
   */
  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  /**
   * @return the revisionId
   */
  public int getRevisionId() {
    return revisionId;
  }

  /**
   * @param revisionId the revisionId to set
   */
  public void setRevisionId(int revisionId) {
    this.revisionId = revisionId;
  }

  /**
   * @return the userModification
   */
  public String getUserModification() {
    return userModification;
  }

  /**
   * @param userModification the userModification to set
   */
  public void setUserModification(String userModification) {
    this.userModification = userModification;
  }

  /**
   * @return the userCreation
   */
  public String getUserCreation() {
    return userCreation;
  }

  /**
   * @param userCreation the userCreation to set
   */
  public void setUserCreation(String userCreation) {
    this.userCreation = userCreation;
  }

  /**
   * @return the diff
   */
  public List<BlossomHistoryChangeDTO> getDiff() {
    return diff;
  }

  /**
   * @param diff the diff to set
   */
  public void setDiff(List<BlossomHistoryChangeDTO> diff) {
    this.diff = diff;
  }

  @JsonIgnore
  public String getIdAsString() {
    return this.entityId.toString() + "_" + this.revisionId;
  }

  public boolean isAssociation() {
    return association;
  }

  public E getBeforeEntity() {
    return beforeEntity;
  }

  public void setBeforeEntity(E beforeEntity) {
    this.beforeEntity = beforeEntity;
  }

  public E getAfterEntity() {
    return afterEntity;
  }

  public void setAfterEntity(E afterEntity) {
    this.afterEntity = afterEntity;
  }

  public String getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(String entityClass) {
    this.entityClass = entityClass;
  }

  public Map<String, Object> getExtraInformations() {
    return extraInformations;
  }

  public boolean isLinked() {
    return linked;
  }
}
