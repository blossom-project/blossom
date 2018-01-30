package fr.blossom.initializr.enums;

public enum SOURCE_LANGUAGE {
  JAVA("Java", true),
  JAVA_KOTLIN("Java + Kotlin", false),
  KOTLIN("Kotlin", false),
  ;

  private final String displayName;
  private final boolean isDefault;

  SOURCE_LANGUAGE(String displayName, boolean isDefault) {
    this.displayName = displayName;
    this.isDefault = isDefault;
  }

  public String getDisplayName() {
    return displayName;
  }

  public boolean isDefault() {
    return isDefault;
  }
}
