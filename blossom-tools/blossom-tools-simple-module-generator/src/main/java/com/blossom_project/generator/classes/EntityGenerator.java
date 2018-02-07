package com.blossom_project.generator.classes;

import com.helger.jcodemodel.JAnnotationUse;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossom_project.core.common.entity.AbstractEntity;
import com.blossom_project.generator.configuration.model.Field;
import com.blossom_project.generator.configuration.model.Settings;
import com.blossom_project.generator.configuration.model.StringField;
import com.blossom_project.generator.configuration.model.TemporalField;
import com.blossom_project.generator.configuration.model.impl.BlobField;
import com.blossom_project.generator.utils.GeneratorUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;

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

    // Getter
    JMethod getter = definedClass.method(JMod.PUBLIC, fieldVar.type(), field.getGetterName());
    getter.body()._return(fieldVar);

    // Setter
    JMethod setter = definedClass.method(JMod.PUBLIC, void.class, field.getSetterName());
    JVar param = setter.param(fieldVar.type(), field.getName());
    setter.body().assign(JExpr.refthis(fieldVar.name()), param);
  }
}
