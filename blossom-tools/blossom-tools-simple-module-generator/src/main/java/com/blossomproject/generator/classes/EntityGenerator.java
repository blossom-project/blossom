package com.blossomproject.generator.classes;

import com.blossomproject.generator.configuration.model.impl.EnumField;
import com.helger.jcodemodel.*;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.configuration.model.StringField;
import com.blossomproject.generator.configuration.model.TemporalField;
import com.blossomproject.generator.configuration.model.impl.BlobField;
import com.blossomproject.generator.utils.GeneratorUtils;

import javax.persistence.*;
import java.util.EnumSet;

public class EntityGenerator implements ClassGenerator {

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {

  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
      definedClass._extends(AbstractEntity.class);

      // Entity name annotation
      JAnnotationUse entityAnnotation = definedClass.annotate(Entity.class);
      entityAnnotation.param("name", settings.getEntityName());

      // Table name annotation
      JAnnotationUse tableAnnotation = definedClass.annotate(Table.class);
      tableAnnotation.param("name", settings.getEntityNameLowerUnderscore());

      // Fields
      for (Field field : settings.getFields()) {
        addField(codeModel, definedClass, field);
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate PO", e);
    }
  }

  private void addField(JCodeModel codeModel, JDefinedClass definedClass, Field field) {



    // Field
    JFieldVar fieldVar = definedClass
      .field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName());
    JAnnotationUse columnAnnotation = fieldVar.annotate(Column.class);
    columnAnnotation.param("name", field.getColumnName().toLowerCase());



    if (field instanceof StringField) {
      if (((StringField) field).getMaxLength() != null) {
        columnAnnotation.param("length", ((StringField) field).getMaxLength());
      }

      if (((StringField) field).isLob()) {
        fieldVar.annotate(Lob.class);
      }
    }

    if (field instanceof TemporalField) {
      if (((TemporalField) field).getTemporalType() != null) {
        JAnnotationUse temporalTypeAnnotation = fieldVar.annotate(Temporal.class);
        temporalTypeAnnotation.param("value", ((TemporalField) field).getTemporalType());
      }
    }

    if(field instanceof BlobField){
      fieldVar.annotate(Lob.class);
    }


    if(field instanceof EnumField){
      try {
        JDefinedClass definedEnum = definedClass._enum(field.getClassName().getSimpleName());

        for(Object enumObj : EnumSet.allOf((Class<? extends Enum>)field.getClassName()) ){
          Enum enumCasted =  (Enum) enumObj;
          definedEnum.direct(enumCasted.toString()+",");
        }
        fieldVar.type(codeModel.ref(definedEnum.name()));
        fieldVar.annotate(Enumerated.class).param("value",EnumType.STRING);
      }
      catch (Exception e){
        System.out.println(e.getMessage());
      }
    }




    // Getter
    JMethod getter = definedClass.method(JMod.PUBLIC, fieldVar.type(), field.getGetterName());
    getter.body()._return(fieldVar);

    // Setter
    JMethod setter = definedClass.method(JMod.PUBLIC, void.class, field.getSetterName());
    JVar param = setter.param(fieldVar.type(), field.getName());
    setter.body().assign(JExpr.refthis(fieldVar.name()), param);
  }
}
