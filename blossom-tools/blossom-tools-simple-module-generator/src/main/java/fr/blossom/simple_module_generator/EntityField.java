package fr.blossom.simple_module_generator;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Scanner;

public class EntityField {

  // Basic informations
  private String name;
  private String tableName;
  private String tableNameLowerCase;
  private String getterName;
  private String setterName;

  // Class informations
  private String className;
  private String temporalType;
  private String jdbcType;
  private boolean lob;

  // Functionnality
  private boolean searchable;
  private boolean requiredCreate;
  private boolean possibleUpdate;
  private boolean nullable;
  private boolean notBlank;
  private Integer maxLength;
  private String defaultValue;

  private EntityField(String name, String className, boolean searchable, boolean requiredCreate,
    boolean possibleUpdate, boolean nullable, boolean notBlank,
    String defaultValue,
    Integer maxLength, String temporalType, String jdbcType, boolean lob) {
    this.name = name;
    this.className = className;
    this.tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
    this.tableNameLowerCase = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    this.getterName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    this.setterName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    this.searchable = searchable;
    this.requiredCreate = requiredCreate;
    this.possibleUpdate = possibleUpdate;
    this.nullable = nullable;
    this.notBlank = notBlank;
    this.defaultValue = defaultValue;
    this.maxLength = maxLength;
    this.temporalType = temporalType;
    this.jdbcType = jdbcType;
    this.lob = lob;
  }

  public static EntityField readParametersInput(Scanner scanner) {
    System.out.println("-- Start Entity Field --");

    System.out.print("Field name : \t");
    String fieldName = scanner.nextLine();

    System.out.println("Classtype : ");
    System.out.println("1. java.lang.String");
    System.out.println("2. java.lang.Long");
    System.out.println("3. java.lang.Integer");
    System.out.println("4. java.lang.Boolean");
    System.out.println("5. java.util.Date");
    System.out.println("6. java.sql.Timestamp");
    System.out.println("7. java.sql.Date");
    System.out.println("8. java.sql.Time");
    System.out.println("9. byte[]");

    String classType = null;
    String temporalType = null;
    Integer maxSize = null;
    String jdbcType = null;
    boolean isLob = false;

    System.out.print("Choose a classtype from above propositions : \t");
    int entityClassType = scanner.nextInt();
    scanner.nextLine(); // throw away the \n not consumed by nextInt()

    boolean notBlank = false;
    switch (entityClassType) {
      case 1:
        classType = "java.lang.String";

        System.out.print("Has the field a max length ? [Y/n] \t");
        boolean hasMaxSize = scanner.nextLine().toLowerCase().trim().startsWith("y");
        if (hasMaxSize) {
          System.out.print("Enter max size value : \t");
          maxSize = scanner.nextInt();
          scanner.nextLine(); // throw away the \n not consumed by
          // nextInt()
          jdbcType = "varchar(" + maxSize + ")";
        } else {
          System.out.print("Is the field a CLOB (long text)? [Y/n] \t");
          isLob = !scanner.nextLine().toLowerCase().trim().startsWith("y");
          if (isLob) {
            jdbcType = "clob";
          } else {
            jdbcType = "varchar(255)";
          }
        }

        System.out.print("Can the field be blank ? [Y/n] \t");
        notBlank = !scanner.nextLine().toLowerCase().trim().startsWith("y");

        break;
      case 2:
        classType = "java.lang.Long";
        jdbcType = "long";
        break;
      case 3:
        classType = "java.lang.Integer";
        jdbcType = "integer";
        break;
      case 4:
        classType = "java.lang.Boolean";
        jdbcType = "boolean";
        break;
      case 5:
        classType = "java.util.Date";

        System.out.println("Temporal type : ");
        System.out.println("1. TIMESTAMP");
        System.out.println("2. DATE");
        System.out.println("3. TIME");

        System.out.print("Choose a classtype from above propositions : \t");
        int temporalTypeChoice = scanner.nextInt();
        scanner.nextLine();
        switch (temporalTypeChoice) {
          case 1:
            temporalType = "TIMESTAMP";
            jdbcType = "timestamp";
            break;
          case 2:
            temporalType = "DATE";
            jdbcType = "date";
            break;
          case 3:
            temporalType = "TIME";
            jdbcType = "time";
            break;
          default:
            break;
        }
        break;
      case 6:
        classType = "java.sql.Timestamp";
        jdbcType = "timestamp";
        break;
      case 7:
        classType = "java.sql.Date";
        jdbcType = "date";
        break;
      case 8:
        classType = "java.sql.Time";
        jdbcType = "time";
        break;
      case 9:
        classType = "byte[]";
        jdbcType = "blob";
        isLob = true;
        break;
      default:
        break;
    }

    System.out.print("Is the field needed for creation ? [Y/n] \t");
    boolean requiredCreate = scanner.nextLine().toLowerCase().trim().startsWith("y");

    System.out.print("Is the field updatable ? [Y/n] \t");
    boolean possibleUpdate = scanner.nextLine().toLowerCase().trim().startsWith("y");

    System.out.print("Is the field searchable (elasticsearch) ? [Y/n] \t");
    boolean searchable = scanner.nextLine().toLowerCase().trim().startsWith("y");

    System.out.print("Is the field nullable ? [Y/n] \t");
    boolean nullable = scanner.nextLine().toLowerCase().trim().startsWith("y");

    System.out.print("Has the field a default value ? [Y/n] \t");
    boolean hasDefaultValue = scanner.nextLine().toLowerCase().trim().startsWith("y");
    String defaultValue = null;
    if (hasDefaultValue) {
      System.out.print("Enter default value : \t");
      defaultValue = scanner.nextLine();
    }

    System.out.println("-- End Entity field--");

    return new EntityField(fieldName, classType, searchable, requiredCreate, possibleUpdate,
      nullable, notBlank, defaultValue, maxSize,
      temporalType, jdbcType, isLob);
  }

  public static List<EntityField> defaultFields() {
    return Lists.newArrayList(
      new EntityField("name", "java.lang.String", true, true, true, false, true, null, 50, null,
        "varchar(100)", false),
      new EntityField("description", "java.lang.String", true, true, true, true, false, null, null,
        null, "clob", true));
  }

  public String getName() {
    return name;
  }

  public String getTableName() {
    return tableName;
  }

  public String getTableNameLowerCase() {
    return tableNameLowerCase;
  }

  public String getGetterName() {
    return getterName;
  }

  public String getSetterName() {
    return setterName;
  }

  public String getClassName() {
    return className;
  }

  public String getTemporalType() {
    return temporalType;
  }

  public boolean isSearchable() {
    return searchable;
  }

  public boolean isNullable() {
    return nullable;
  }

  public boolean isNotBlank() {
    return notBlank;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean isRequiredCreate() {
    return requiredCreate;
  }

  public boolean isPossibleUpdate() {
    return possibleUpdate;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public boolean isLob() {
    return lob;
  }
}
