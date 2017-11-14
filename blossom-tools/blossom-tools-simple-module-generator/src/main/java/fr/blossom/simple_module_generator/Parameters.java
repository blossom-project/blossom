package fr.blossom.simple_module_generator;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Sets;
import java.util.Scanner;
import java.util.Set;

public class Parameters {

  private String basePackage;
  private String entityName;
  private String entityNameUpperUnderscore;
  private String entityNameLowerUnderscore;
  private String entityNameLowerCamel;
  private String entityNameLowerHyphen;
  private Set<EntityField> fields;

  private boolean overwriteModule;

  public static Parameters readParametersInput(Scanner scanner) {

    System.out.println("----- Start Parameters -----");


    System.out.print("Enter the base package of your application :\t");
    String basePackage = scanner.nextLine();

    System.out.print("Enter an entityName :\t");
    String entityName = scanner.nextLine();

    Set<EntityField> fields = Sets.newLinkedHashSet();

    System.out.print("Add default fields (name/description) ? [Y/n] \t");
    boolean defaultFields = scanner.nextLine().toLowerCase().trim().startsWith("y");
    if (defaultFields) {
      fields.addAll(EntityField.defaultFields());
    }

    System.out.print("Add a new field ? [Y/n] \t");
    boolean addProperty = scanner.nextLine().toLowerCase().trim().startsWith("y");

    while (addProperty) {
      EntityField entityField = EntityField.readParametersInput(scanner);
      boolean added = fields.add(entityField);
      if (!added) {
        System.err.println("Can't add field " + entityField.getName() + " : it already is defined");
      }

      System.out.println("Added " + entityField);
      System.out.print("\n\n");

      System.out.print("Add a new property ? [Y/n] \t");
      String input = scanner.nextLine();
      addProperty = input.toLowerCase().trim().startsWith("y");
    }

    System.out.println("----- End Parameters -----");

    Parameters params = new Parameters();
    params.entityName = entityName.trim();
    params.entityNameUpperUnderscore = CaseFormat.UPPER_CAMEL
      .to(CaseFormat.UPPER_UNDERSCORE, entityName);
    params.entityNameLowerUnderscore = CaseFormat.UPPER_CAMEL
      .to(CaseFormat.LOWER_UNDERSCORE, entityName);
    params.entityNameLowerHyphen = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, entityName);
    params.entityNameLowerCamel = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, entityName);
    params.basePackage = basePackage + ".modules." + params.entityNameLowerUnderscore;
    params.fields = fields;

    return params;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getEntityNameUpperUnderscore() {
    return entityNameUpperUnderscore;
  }

  public String getEntityNameLowerUnderscore() {
    return entityNameLowerUnderscore;
  }

  public String getEntityNameLowerCamel() {
    return entityNameLowerCamel;
  }

  public String getEntityNameLowerHyphen() {
    return entityNameLowerHyphen;
  }

  public Set<EntityField> getFields() {
    return fields;
  }

  public boolean isOverwriteModule() {
    return overwriteModule;
  }
}
