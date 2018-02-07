package com.blossom_project.simple_module_generator.classes;

import com.helger.jcodemodel.JAnnotationUse;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossom_project.core.common.entity.AbstractEntity;
import com.blossom_project.simple_module_generator.EntityField;
import com.blossom_project.simple_module_generator.GeneratorUtils;
import com.blossom_project.simple_module_generator.Parameters;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class EntityGenerator implements ClassGenerator {

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getEntityFullyQualifiedClassName(parameters));
      definedClass._extends(AbstractEntity.class);

      // Entity name annotation
      JAnnotationUse entityAnnotation = definedClass.annotate(Entity.class);
      entityAnnotation.param("name", parameters.getEntityName());

      // Table name annotation
      JAnnotationUse tableAnnotation = definedClass.annotate(Table.class);
      tableAnnotation.param("name", parameters.getEntityNameLowerUnderscore());

      // Fields
      for (EntityField field : parameters.getFields()) {
        addField(codeModel, definedClass, field);
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate PO", e);
    }
  }


  private void addField(JCodeModel codeModel, JDefinedClass definedClass, EntityField field) {
    // Field
    JFieldVar fieldVar = definedClass.field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName());
    JAnnotationUse columnAnnotation = fieldVar.annotate(Column.class);
    columnAnnotation.param("name", field.getTableName());

    if (field.getMaxLength() != null) {
      columnAnnotation.param("length", field.getMaxLength());
    }

    if(field.isLob()){
      fieldVar.annotate(Lob.class);
    }

    if (field.getTemporalType() != null) {
      JAnnotationUse temporalTypeAnnotation = fieldVar.annotate(Temporal.class);
      temporalTypeAnnotation.param("value", codeModel.ref(TemporalType.class).staticRef(field.getTemporalType()));
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
