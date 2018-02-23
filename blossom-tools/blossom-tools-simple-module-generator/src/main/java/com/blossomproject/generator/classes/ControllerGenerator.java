package com.blossomproject.generator.classes;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.helger.jcodemodel.*;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.generator.utils.GeneratorUtils;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ControllerGenerator implements ClassGenerator {

  private AbstractJClass dtoClass;
  private AbstractJClass serviceClass;
  private AbstractJClass createFormClass;
  private AbstractJClass updateFormClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
    this.serviceClass = codeModel.ref(GeneratorUtils.getServiceFullyQualifiedClassName(settings));
    this.createFormClass = codeModel.ref(GeneratorUtils.getCreateFormFullyQualifiedClassName(settings));
    this.updateFormClass = codeModel.ref(GeneratorUtils.getUpdateFormFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getControllerFullyQualifiedClassName(settings));
      definedClass.annotate(BlossomController.class);
      definedClass.annotate(RequestMapping.class)
        .param("value", "/modules/" + settings.getEntityNameLowerUnderscore() + "s");
      definedClass.annotate(OpenedMenu.class)
        .param("value", settings.getEntityNameLowerUnderscore() + "s");

      JFieldVar logger = definedClass
        .field(JMod.PRIVATE + JMod.STATIC + JMod.FINAL, Logger.class, "logger",
          codeModel.ref(LoggerFactory.class).staticInvoke("getLogger")
            .arg(definedClass.dotclass()));

      JFieldVar service = definedClass.field(JMod.PRIVATE + JMod.FINAL, serviceClass, "service");

      JFieldVar searchEngine = definedClass.field(JMod.PRIVATE + JMod.FINAL, codeModel.ref(
        SearchEngineImpl.class).narrow(dtoClass), "searchEngine");

      JMethod constructor = definedClass.constructor(JMod.PUBLIC);
      constructor.body()
        .assign(JExpr.refthis(service.name()), constructor.param(serviceClass, "service"));
      constructor.body().assign(JExpr.refthis(searchEngine.name()),
        constructor.param(codeModel.ref(SearchEngineImpl.class).narrow(dtoClass), "searchEngine"));

      JMethod methodGetPage = buildGetPage(definedClass, settings, codeModel, service, searchEngine);

      JMethod methodGetCreatePage = buildGetCreatePage(definedClass, settings, codeModel, service, searchEngine);

      JMethod methodHandleCreateForm = buildHandleCreateForm(definedClass, settings, codeModel, service, searchEngine);

      JMethod methodCreateView = buildCreateView(definedClass, codeModel, settings);

      JMethod methodGetEntity = buildGetEntity(definedClass, codeModel, settings, service);

      JMethod methodGetEntityInformations = buildGetEntityInformations(definedClass, codeModel, settings, service);

      JMethod methodGetEntityInformationsForm = buildGetEntityInformationsForm(definedClass, codeModel, settings, service);

      JMethod methodHandleInformationsForm = buildHandleInformationsForm(definedClass, settings, codeModel, service, searchEngine);

      JMethod methodDeleteEntity = buildDeleteEntity(definedClass, settings, codeModel, service, searchEngine);

      JMethod methodViewInformationView = buildViewInformationView(definedClass, codeModel, settings);

      JMethod methodUpdateInformationView = buildUpdateInformationView(definedClass, codeModel, settings);




      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

  private JMethod buildGetPage(JDefinedClass definedClass, Settings settings,
    JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityNameLowerCamel()+"sPage");
    method.annotate(GetMapping.class);
    method.annotate(PreAuthorize.class).param("value",
      "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:read')");

    JVar query = method.param(String.class, "q");
    query.annotate(RequestParam.class).param("value", "q").param("required", false);

    JVar pageable = method.param(Pageable.class, "pageable");
    pageable.annotate(PageableDefault.class).param("size", 25);

    JVar model = method.param(Model.class, "model");

    JBlock body = method.body();

    JVar items = body.decl(codeModel.ref(Page.class).narrow(dtoClass), "items");

    JConditional ifCondition = body
      ._if(codeModel.ref(Strings.class).staticInvoke("isNullOrEmpty").arg(query));
    ifCondition._then().assign(items, JExpr.refthis(service.name()).invoke("getAll").arg(pageable));
    ifCondition._else().assign(items,
      JExpr.refthis(searchEngine.name()).invoke("search").arg(query).arg(pageable)
        .invoke("getPage"));

    body.add(model.invoke("addAttribute").arg("items").arg(items));
    body.add(model.invoke("addAttribute").arg("q").arg(query));

    body._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(
      "modules/" + settings.getEntityNameLowerUnderscore() + "s" + "/" + settings
        .getEntityNameLowerUnderscore() + "s").arg(model.invoke("asMap")));

    return method;
  }

  private JMethod buildHandleCreateForm(JDefinedClass definedClass, Settings settings,
                                      JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "handle"+settings.getEntityNameLowerCamel()+"CreateForm");
    method.annotate(PostMapping.class).param("value", "/_create");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:create')");

    JVar createForm = method.param(createFormClass, "createForm");
    createForm.annotate(Valid.class);
    createForm.annotate(ModelAttribute.class).param("value", settings.getEntityNameLowerCamel()+"CreateForm");

    JVar bindingResult = method.param(BindingResult.class, "bindingResult");

    JVar model = method.param(Model.class, "model");

    JBlock body = method.body();

    JConditional ifCondition = body._if(bindingResult.invoke("hasErrors"));
    ifCondition._then()._return(JExpr._this().invoke("createView").arg(createForm).arg(model));

    JTryBlock tryBlock = body._try();
    JVar entity = tryBlock.body().decl(dtoClass,"entity", JExpr.refthis(service.name()).invoke("create").arg(createForm));
    tryBlock.body()._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(JExpr.lit("redirect:../"+settings.getEntityNameLowerUnderscore()+"s/").plus(entity.invoke("getId"))));

    JCatchBlock catchBlock = tryBlock._catch(codeModel.ref(Exception.class));
    catchBlock.body().add(JExpr.refthis("logger").invoke("error").arg( JExpr.lit("Error on creating entity, already exists ")).arg(catchBlock.param("e")));

    catchBlock.body()._return(JExpr._this().invoke("createView").arg(createForm).arg(model));

    return method;
  }

  private JMethod buildGetCreatePage(JDefinedClass definedClass, Settings settings,
                                     JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityNameLowerCamel()+"CreatePage");
    method.annotate(GetMapping.class).param("value", "/_create");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:create')");

    JVar model = method.param(Model.class, "model");

    JVar locale = method.param(Locale.class, "locale");

    JBlock body = method.body();

    JVar entityCreateForm = body.decl(createFormClass, "createForm", JExpr._new(createFormClass));

    body._return(JExpr._this().invoke("createView").arg(entityCreateForm).arg(model));

    return method;
  }

  private JMethod buildCreateView (JDefinedClass definedClass,
                                   JCodeModel codeModel, Settings settings) {
    JMethod method = definedClass.method(JMod.PRIVATE, ModelAndView.class, "createView");

    JVar createForm = method.param(createFormClass, "createForm");
    JVar model = method.param(Model.class, "model");

    JBlock body = method.body();
    body.add(model.invoke("addAttribute").arg(settings.getEntityNameLowerCamel()+"CreateForm").arg(createForm));

    body._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(
            "modules/" + settings.getEntityNameLowerUnderscore() + "s" + "/create").arg(model.invoke("asMap")));

    return method;
  }

  private JMethod buildGetEntity (JDefinedClass definedClass,
                                   JCodeModel codeModel, Settings settings, JFieldVar service) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityNameLowerCamel());
    method.annotate(GetMapping.class).param("value", "/{id}");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:read')");

    JVar id = method.param(Long.class, "id");
    id.annotate(PathVariable.class);
    JVar model = method.param(Model.class, "model");
    JVar request = method.param(HttpServletRequest.class, "request");

    JBlock body = method.body();

    JVar entity = body.decl(dtoClass,"entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

    JConditional ifCondition = body._if(entity.eqNull());
    ifCondition._then()._throw(JExpr._new(codeModel.ref(NoSuchElementException.class)).arg(codeModel.ref(String.class).staticInvoke("format").arg(settings.getEntityNameLowerCamel()+"=%s not found").arg(id)));


    body.add(model.invoke("addAttribute").arg(settings.getEntityNameLowerCamel()).arg(entity));

    body._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(
            "modules/" + settings.getEntityNameLowerUnderscore() + "s" + "/"+settings.getEntityNameLowerUnderscore()).arg(settings.getEntityNameLowerUnderscore()).arg(entity));

    return method;
  }

  private JMethod buildGetEntityInformations (JDefinedClass definedClass,
                                  JCodeModel codeModel, Settings settings, JFieldVar service) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityNameLowerCamel()+"Informations");
    method.annotate(GetMapping.class).param("value", "/{id}/_informations");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:read')");

    JVar id = method.param(Long.class, "id");
    id.annotate(PathVariable.class);
    JVar request = method.param(HttpServletRequest.class, "request");

    JBlock body = method.body();

    JVar entity = body.decl(dtoClass,"entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

    JConditional ifCondition = body._if(entity.eqNull());
    ifCondition._then()._throw(JExpr._new(codeModel.ref(NoSuchElementException.class)).arg(codeModel.ref(String.class).staticInvoke("format").arg(settings.getEntityNameLowerCamel()+"=%s not found").arg(id)));

    body._return(JExpr._this().invoke("viewInformationView").arg(entity));

    return method;
  }

  private JMethod buildGetEntityInformationsForm (JDefinedClass definedClass,
                                              JCodeModel codeModel, Settings settings, JFieldVar service) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityNameLowerCamel()+"InformationsForm");
    method.annotate(GetMapping.class).param("value", "/{id}/_informations/_edit");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:write')");

    JVar id = method.param(Long.class, "id");
    id.annotate(PathVariable.class);

    JBlock body = method.body();

    JVar entity = body.decl(dtoClass,"entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

    JConditional ifCondition = body._if(entity.eqNull());
    ifCondition._then()._throw(JExpr._new(codeModel.ref(NoSuchElementException.class)).arg(codeModel.ref(String.class).staticInvoke("format").arg(settings.getEntityNameLowerCamel()+"=%s not found").arg(id)));

    body._return(JExpr._this().invoke("updateInformationView").arg(JExpr._new(updateFormClass).arg(entity)));

    return method;
  }

  private JMethod buildHandleInformationsForm(JDefinedClass definedClass, Settings settings,
                                        JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "handle"+settings.getEntityNameLowerCamel()+"InformationsForm");
    method.annotate(PostMapping.class).param("value", "/{id}/_informations/_edit");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:write')");

    JVar id = method.param(Long.class, "id");
    id.annotate(PathVariable.class);

    JVar model = method.param(Model.class, "model");

    JVar updateForm = method.param(updateFormClass, "updateForm");
    updateForm.annotate(Valid.class);
    updateForm.annotate(ModelAttribute.class).param("value", settings.getEntityNameLowerCamel()+"UpdateForm");

    JVar bindingResult = method.param(BindingResult.class, "bindingResult");

    JBlock body = method.body();

    JConditional ifErrors = body._if(bindingResult.invoke("hasErrors"));
    ifErrors._then()._return(JExpr._this().invoke("updateInformationView").arg(updateForm));

    JVar entity = body.decl(dtoClass,"entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

    JConditional ifEntity = body._if(entity.eqNull());
    ifEntity._then()._throw(JExpr._new(codeModel.ref(NoSuchElementException.class)).arg(codeModel.ref(String.class).staticInvoke("format").arg(settings.getEntityNameLowerCamel()+"=%s not found").arg(id)));

    JVar entityUpdated = body.decl(dtoClass,"entityUpdated", JExpr.refthis(service.name()).invoke("update").arg(id).arg(updateForm));

    body._return(JExpr._this().invoke("viewInformationView").arg(entityUpdated));

    return method;
  }

  private JMethod buildViewInformationView (JDefinedClass definedClass,
                                   JCodeModel codeModel, Settings settings) {
    JMethod method = definedClass.method(JMod.PRIVATE, ModelAndView.class, "viewInformationView");

    JVar entity = method.param(dtoClass, "entity");

    JBlock body = method.body();

    body._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(
            "modules/" + settings.getEntityNameLowerUnderscore() + "s" + "/"+settings.getEntityNameLowerUnderscore()+"informations").arg(settings.getEntityNameLowerUnderscore()).arg(entity));

    return method;
  }

  private JMethod buildUpdateInformationView (JDefinedClass definedClass,
                                            JCodeModel codeModel, Settings settings) {
    JMethod method = definedClass.method(JMod.PRIVATE, ModelAndView.class, "updateInformationView");

    JVar entityForm = method.param(updateFormClass, "entityUpdateForm");

    JBlock body = method.body();

    body._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(
            "modules/" + settings.getEntityNameLowerUnderscore() + "s" + "/"+settings.getEntityNameLowerUnderscore()+"informations-edit").arg(settings.getEntityNameLowerCamel()+"UpdateForm").arg(entityForm));

    return method;
  }

  private JMethod buildDeleteEntity(JDefinedClass definedClass, Settings settings,
                                     JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {

    //JDefinedClass responseClass = codeModel.ref(ResponseEntity.class);

    JNarrowedClass response = codeModel.ref(ResponseEntity.class).narrow(codeModel.ref(Map.class).narrow(codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends()),codeModel.ref(Long.class)));

    JMethod method = definedClass.method(JMod.PUBLIC, response, "delete"+settings.getEntityNameLowerCamel());
    method.annotate(PostMapping.class).param("value", "/{id}/_delete");
    method.annotate(PreAuthorize.class).param("value",
            "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:delete')");

    JVar id = method.param(Long.class, "id");
    id.annotate(PathVariable.class);

    JVar force = method.param(Boolean.class, "force");
    force.annotate(RequestParam.class).param("value", "force").param("required", false).param("defaultValue", "false");

    JBlock body = method.body();

    JVar result = body.decl(
            codeModel.ref(Optional.class).narrow(codeModel.ref(Map.class).narrow(codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends()),codeModel.ref(Long.class)))
            , "result",
            JExpr.refthis(service.name()).invoke("delete").arg(JExpr.refthis(service.name()).invoke("getOne").arg(id)).arg(force));

    JConditional conditional = body._if(result.invoke("isPresent").not().cor(result.invoke("get").invoke("isEmpty")));
    conditional._then()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(codeModel.ref(Maps.class).staticInvoke("newHashMap")).arg(codeModel.ref(HttpStatus.class).staticRef("OK")));

    conditional._else()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(result.invoke("get")).arg(codeModel.ref(HttpStatus.class).staticRef("CONFLICT")));

    return method;
  }

}
