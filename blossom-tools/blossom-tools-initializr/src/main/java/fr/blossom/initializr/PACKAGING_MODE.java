package fr.blossom.initializr;

public enum PACKAGING_MODE {

  JAR("jar"),
  WAR("war");

  private String label;

  private PACKAGING_MODE(String label) {
    this.label = label;
  }

  public static PACKAGING_MODE getByLabel(String label) {
    for (PACKAGING_MODE value : PACKAGING_MODE.values()) {
      if (value.getLabel().equals(label)) {
        return value;
      }
    }
    return null;
  }

  public String getLabel() {
    return label;
  }

}
