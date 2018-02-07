package com.blossom_project.simple_module_generator.classes;

import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JOp;
import com.helger.jcodemodel.JVar;
import com.blossom_project.core.common.mapper.AbstractDTOMapper;
import com.blossom_project.simple_module_generator.EntityField;
import com.blossom_project.simple_module_generator.GeneratorUtils;
import com.blossom_project.simple_module_generator.Parameters;

public class MapperGenerator implements ClassGenerator {

  private JDefinedClass entityClass;
  private JDefinedClass dtoClass;

  public MapperGenerator(JDefinedClass entityClass, JDefinedClass dtoClass) {
    this.entityClass = entityClass;
    this.dtoClass = dtoClass;
  }

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getMapperFullyQualifiedClassName(parameters));
      definedClass._extends(codeModel.ref(AbstractDTOMapper.class).narrow(entityClass, dtoClass));

      buildMapEntity(definedClass, parameters);
      buildMapDto(definedClass, parameters);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate mapper class", e);
    }
  }

  private void buildMapEntity(JDefinedClass definedClass, Parameters parameters) {

    JMethod mapEntity = definedClass.method(JMod.PUBLIC, dtoClass, "mapEntity");
    JVar entity = mapEntity.param(entityClass, "entity");
    JBlock mapEntityBody = mapEntity.body();
    mapEntityBody._if(JOp.eq(entity, JExpr._null()))._then()._return(JExpr._null());

    JVar dto = mapEntityBody.decl(dtoClass, "dto", JExpr._new(dtoClass));
    JInvocation mapEntityCommonFields = mapEntityBody.invoke("mapEntityCommonFields").arg(dto)
      .arg(entity);

    for (EntityField field : parameters.getFields()) {
      mapEntityBody.add(dto.invoke(field.getSetterName()).arg(entity.invoke(field.getGetterName())));
    }

    mapEntityBody._return(dto);

  }

  private void buildMapDto(JDefinedClass definedClass, Parameters parameters) {

    JMethod mapDto = definedClass.method(JMod.PUBLIC, entityClass, "mapDto");
    JVar dto = mapDto.param(dtoClass, "dto");
    JBlock mapDtoBody = mapDto.body();
    mapDtoBody._if(JOp.eq(dto, JExpr._null()))._then()._return(JExpr._null());

    JVar entity = mapDtoBody.decl(entityClass, "entity", JExpr._new(entityClass));
    JInvocation mapDtoCommonFields = mapDtoBody.invoke("mapDtoCommonFields").arg(entity).arg(dto);


    for (EntityField field : parameters.getFields()) {
      mapDtoBody.add(entity.invoke(field.getSetterName()).arg(dto.invoke(field.getGetterName())));
    }

    mapDtoBody._return(entity);
  }

}
