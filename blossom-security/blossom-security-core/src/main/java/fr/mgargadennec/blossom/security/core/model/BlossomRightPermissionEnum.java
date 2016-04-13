package fr.mgargadennec.blossom.security.core.model;

public enum BlossomRightPermissionEnum {

  READ("R"),
  WRITE("W"),
  CREATE("C"),
  DELETE("D"),
  CLEARANCE("CL");

  private String code;

  private BlossomRightPermissionEnum(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public static BlossomRightPermissionEnum getByCode(String code) {
    if (code == null) {
      return null;
    }
    for (BlossomRightPermissionEnum element : BlossomRightPermissionEnum.values()) {
      if (element.code.equals(code)) {
        return element;
      }
    }
    return null;
  }
}
