package fr.mgargadennec.blossom.simple_module_generator.generator;

import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import fr.mgargadennec.blossom.core.common.event.CreatedEvent;
import fr.mgargadennec.blossom.core.common.event.UpdatedEvent;
import fr.mgargadennec.blossom.core.common.service.GenericCrudServiceImpl;
import fr.mgargadennec.blossom.simple_module_generator.ClassGenerator;
import fr.mgargadennec.blossom.simple_module_generator.EntityField;
import fr.mgargadennec.blossom.simple_module_generator.GeneratorUtils;
import fr.mgargadennec.blossom.simple_module_generator.Parameters;
import org.springframework.context.ApplicationEventPublisher;

public class ServiceImplGenerator implements ClassGenerator {

  private final JDefinedClass entityClass;
  private final JDefinedClass daoClass;
  private final JDefinedClass mapperClass;
  private final JDefinedClass dtoClass;
  private final JDefinedClass serviceClass;
  private final JDefinedClass createFormClass;
  private final JDefinedClass updateFormClass;

  public ServiceImplGenerator(JDefinedClass entityClass, JDefinedClass daoClass,
    JDefinedClass mapperClass,
    JDefinedClass dtoClass, JDefinedClass serviceClass,
    JDefinedClass createFormClass, JDefinedClass updateFormClass) {
    this.entityClass = entityClass;
    this.daoClass = daoClass;
    this.mapperClass = mapperClass;
    this.dtoClass = dtoClass;
    this.serviceClass = serviceClass;
    this.createFormClass = createFormClass;
    this.updateFormClass = updateFormClass;
  }

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getServiceImplFullyQualifiedClassName(parameters));
      definedClass
        ._extends(codeModel.ref(GenericCrudServiceImpl.class).narrow(dtoClass, entityClass));
      definedClass._implements(serviceClass);

      JMethod constructor = definedClass.constructor(JMod.PUBLIC);
      constructor.body().invoke("super").arg(constructor.param(daoClass, "dao")).arg(constructor.param(mapperClass, "mapper")).arg(constructor.param(ApplicationEventPublisher.class, "publisher"));

      buildCreate(parameters,definedClass,codeModel);
      buildUpdate(parameters,definedClass,codeModel);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

  private void buildCreate(Parameters parameters, JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod create = definedClass.method(JMod.PUBLIC, dtoClass, "create");
    create.annotate(Override.class);
    JVar form = create.param(createFormClass, "createForm");

    JBlock body = create.body();
    JVar toCreate = body.decl(entityClass, "toCreate", JExpr._new(entityClass));
    for(EntityField field : parameters.getFields()){
      if(field.isRequiredCreate()){
        body.add(toCreate.invoke(field.getSetterName()).arg(form.invoke(field.getGetterName())));
      }
    }
    JVar savedEntity = body.decl(entityClass, "savedEntity", JExpr.refthis("dao").invoke("create").arg(toCreate));
    JVar savedDto = body.decl(dtoClass,"savedDto", JExpr.refthis("mapper").invoke("mapEntity").arg(savedEntity));

    body.add(JExpr.refthis("publisher").invoke("publishEvent").arg(JExpr._new(codeModel.ref(CreatedEvent.class).narrow(dtoClass)).arg(JExpr._this()).arg(savedDto)));

    body._return(savedDto);
  }

  private void buildUpdate(Parameters parameters, JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod update = definedClass.method(JMod.PUBLIC, dtoClass, "update");
    update.annotate(Override.class);
    JVar id = update.param(Long.class, "id");
    JVar form = update.param(updateFormClass, "updateForm");


    JBlock body = update.body();
    JVar toCreate = body.decl(entityClass, "toUpdate", JExpr._new(entityClass));
    for(EntityField field : parameters.getFields()){
      if(field.isPossibleUpdate()){
        body.add( toCreate.invoke(field.getSetterName()).arg(form.invoke(field.getGetterName())));
      }
    }

    JVar savedEntity = body.decl(entityClass, "savedEntity", JExpr.refthis("dao").invoke("update").arg(id).arg(toCreate));
    JVar savedDto = body.decl(dtoClass,"savedDto", JExpr.refthis("mapper").invoke("mapEntity").arg(savedEntity));

    body.add(JExpr.refthis("publisher").invoke("publishEvent").arg(JExpr._new(codeModel.ref(UpdatedEvent.class).narrow(dtoClass)).arg(JExpr._this()).arg(savedDto)));

    body._return(savedDto);

  }


}
