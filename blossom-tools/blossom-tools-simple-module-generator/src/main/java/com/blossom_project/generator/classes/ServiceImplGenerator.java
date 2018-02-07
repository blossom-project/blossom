package com.blossom_project.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.event.CreatedEvent;
import com.blossom_project.core.common.event.UpdatedEvent;
import com.blossom_project.core.common.service.AssociationServicePlugin;
import com.blossom_project.core.common.service.GenericCrudServiceImpl;
import com.blossom_project.generator.configuration.model.Field;
import com.blossom_project.generator.configuration.model.Settings;
import com.blossom_project.generator.utils.GeneratorUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;

public class ServiceImplGenerator implements ClassGenerator {

  private AbstractJClass entityClass;
  private AbstractJClass daoClass;
  private AbstractJClass mapperClass;
  private AbstractJClass dtoClass;
  private AbstractJClass serviceClass;
  private AbstractJClass createFormClass;
  private AbstractJClass updateFormClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.entityClass = codeModel.ref(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
    this.daoClass = codeModel.ref(GeneratorUtils.getDaoFullyQualifiedClassName(settings));
    this.mapperClass = codeModel.ref(GeneratorUtils.getMapperFullyQualifiedClassName(settings));
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
    this.serviceClass = codeModel.ref(GeneratorUtils.getServiceFullyQualifiedClassName(settings));
    this.createFormClass = codeModel
      .ref(GeneratorUtils.getCreateFormFullyQualifiedClassName(settings));
    this.updateFormClass = codeModel
      .ref(GeneratorUtils.getUpdateFormFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getServiceImplFullyQualifiedClassName(settings));
      definedClass
        ._extends(codeModel.ref(GenericCrudServiceImpl.class).narrow(dtoClass, entityClass));
      definedClass._implements(serviceClass);

      JMethod constructor = definedClass.constructor(JMod.PUBLIC);
      constructor.body().invoke("super")
        .arg(constructor.param(daoClass, "dao"))
        .arg(constructor.param(mapperClass, "mapper"))
        .arg(constructor.param(ApplicationEventPublisher.class, "publisher"))
        .arg(constructor.param(codeModel.ref(PluginRegistry.class)
            .narrow(codeModel.ref(AssociationServicePlugin.class),
              codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends())),
          "associationRegistry"));

      buildCreate(settings, definedClass, codeModel);
      buildUpdate(settings, definedClass, codeModel);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

  private void buildCreate(Settings settings, JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod create = definedClass.method(JMod.PUBLIC, dtoClass, "create");
    create.annotate(Override.class);
    JVar form = create.param(createFormClass, "createForm");

    JBlock body = create.body();
    JVar toCreate = body.decl(entityClass, "toCreate", JExpr._new(entityClass));
    for (Field field : settings.getFields()) {
      if (field.isRequiredCreate()) {
        body.add(toCreate.invoke(field.getSetterName()).arg(form.invoke(field.getGetterName())));
      }
    }
    JVar savedEntity = body
      .decl(entityClass, "savedEntity", JExpr.refthis("crudDao").invoke("create").arg(toCreate));
    JVar savedDto = body
      .decl(dtoClass, "savedDto", JExpr.refthis("mapper").invoke("mapEntity").arg(savedEntity));

    body.add(JExpr.refthis("publisher").invoke("publishEvent").arg(
      JExpr._new(codeModel.ref(CreatedEvent.class).narrow(dtoClass)).arg(JExpr._this())
        .arg(savedDto)));

    body._return(savedDto);
  }

  private void buildUpdate(Settings settings, JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod update = definedClass.method(JMod.PUBLIC, dtoClass, "update");
    update.annotate(Override.class);
    JVar id = update.param(Long.class, "id");
    JVar form = update.param(updateFormClass, "updateForm");

    JBlock body = update.body();
    JVar toCreate = body.decl(entityClass, "toUpdate", JExpr._new(entityClass));
    for (Field field : settings.getFields()) {
      if (field.isPossibleUpdate()) {
        body.add(toCreate.invoke(field.getSetterName()).arg(form.invoke(field.getGetterName())));
      }
    }

    JVar savedEntity = body.decl(entityClass, "savedEntity",
      JExpr.refthis("crudDao").invoke("update").arg(id).arg(toCreate));
    JVar savedDto = body
      .decl(dtoClass, "savedDto", JExpr.refthis("mapper").invoke("mapEntity").arg(savedEntity));

    body.add(JExpr.refthis("publisher").invoke("publishEvent").arg(
      JExpr._new(codeModel.ref(UpdatedEvent.class).narrow(dtoClass)).arg(JExpr._this())
        .arg(savedDto)));

    body._return(savedDto);

  }


}
