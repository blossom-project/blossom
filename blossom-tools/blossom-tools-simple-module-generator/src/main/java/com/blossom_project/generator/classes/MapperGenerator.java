package com.blossom_project.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
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
import com.blossom_project.generator.configuration.model.Field;
import com.blossom_project.generator.configuration.model.Settings;
import com.blossom_project.generator.utils.GeneratorUtils;

public class MapperGenerator implements ClassGenerator {

  private AbstractJClass entityClass;
  private AbstractJClass dtoClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.entityClass = codeModel.ref(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getMapperFullyQualifiedClassName(settings));
      definedClass._extends(codeModel.ref(AbstractDTOMapper.class).narrow(entityClass, dtoClass));

      buildMapEntity(definedClass, settings);
      buildMapDto(definedClass, settings);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate mapper class", e);
    }
  }

  private void buildMapEntity(JDefinedClass definedClass, Settings settings) {

    JMethod mapEntity = definedClass.method(JMod.PUBLIC, dtoClass, "mapEntity");
    JVar entity = mapEntity.param(entityClass, "entity");
    JBlock mapEntityBody = mapEntity.body();
    mapEntityBody._if(JOp.eq(entity, JExpr._null()))._then()._return(JExpr._null());

    JVar dto = mapEntityBody.decl(dtoClass, "dto", JExpr._new(dtoClass));
    JInvocation mapEntityCommonFields = mapEntityBody.invoke("mapEntityCommonFields").arg(dto)
      .arg(entity);

    for (Field field : settings.getFields()) {
      mapEntityBody.add(dto.invoke(field.getSetterName()).arg(entity.invoke(field.getGetterName())));
    }

    mapEntityBody._return(dto);

  }

  private void buildMapDto(JDefinedClass definedClass, Settings settings) {

    JMethod mapDto = definedClass.method(JMod.PUBLIC, entityClass, "mapDto");
    JVar dto = mapDto.param(dtoClass, "dto");
    JBlock mapDtoBody = mapDto.body();
    mapDtoBody._if(JOp.eq(dto, JExpr._null()))._then()._return(JExpr._null());

    JVar entity = mapDtoBody.decl(entityClass, "entity", JExpr._new(entityClass));
    JInvocation mapDtoCommonFields = mapDtoBody.invoke("mapDtoCommonFields").arg(entity).arg(dto);


    for (Field field : settings.getFields()) {
      mapDtoBody.add(entity.invoke(field.getSetterName()).arg(dto.invoke(field.getGetterName())));
    }

    mapDtoBody._return(entity);
  }

}
