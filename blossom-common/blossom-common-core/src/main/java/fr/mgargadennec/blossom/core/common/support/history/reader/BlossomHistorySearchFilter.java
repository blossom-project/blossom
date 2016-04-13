/**
 *
 */
package fr.mgargadennec.blossom.core.common.support.history.reader;

/**
 * Holder des proprietes a filtrer lors de la lecture de l'historique.
 *
 * HystorySearchFilter <=> String example : "property1:value1;property2:value2"
 *
 * Pour ajouter une nouvelle propriete ajouter simplement un attribut de type String avec getter et setter. La
 * conversion HistorySearchFilter <=> String est generique et utilise les proprietes de type String de
 * HistorySearchFilter. Il faut ensuite ajouter la prise en compte du filtre dans HistoryReaderImpl.
 *
 */
public class BlossomHistorySearchFilter {

  public static final String SEPARATOR = ";";
  public static final String INNER_SEPARATOR = "|";
  public static final String ALLOCATION = ":";

  private String entityId;

  private String[] entityName;

  private String userModification;

  private String userCreation;

  private String[] revisionType;

  /**
   * @return the entityId
   */
  public String getEntityId() {
    return entityId;
  }

  /**
   * @param entityId the entityId to set
   */
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  /**
   * @return the entityName
   */
  public String[] getEntityName() {
    return entityName == null ? null : entityName.clone();
  }

  /**
   * @param entityName the entityName to set
   */
  public void setEntityName(String[] entityName) {
    this.entityName = entityName == null ? null : entityName.clone();
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

  public String[] getRevisionType() {
    return revisionType == null ? null : revisionType.clone();
  }

  public void setRevisionType(String[] revisionType) {
    this.revisionType = revisionType == null ? null : revisionType.clone();
  }
}
