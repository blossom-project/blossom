package fr.mgargadennec.blossom.core.common.support.indexation.builder;

public class BlossomIndexInfosDTO {

  private String alias;
  private String indexedResourceType;
  private boolean filtered;

  public BlossomIndexInfosDTO(String alias, String indexedResourceType, boolean filtered) {
    super();
    this.alias = alias;
    this.indexedResourceType = indexedResourceType;
    this.filtered = filtered;
  }

  public String getAlias() {
    return alias;
  }

  public String getIndexedResourceType() {
    return indexedResourceType;
  }

  public boolean isFiltered() {
    return filtered;
  }

}
