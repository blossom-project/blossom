package com.blossomproject.generator.classes;

import com.google.common.base.Strings;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Locale;

public class ControllerGenerator implements ClassGenerator {

  private AbstractJClass dtoClass;
  private AbstractJClass serviceClass;
  private AbstractJClass createFormClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
    this.serviceClass = codeModel.ref(GeneratorUtils.getServiceFullyQualifiedClassName(settings));
    this.createFormClass = codeModel.ref(GeneratorUtils.getCreateFormFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getControllerFullyQualifiedClassName(settings));
      definedClass.annotate(BlossomController.class);
      definedClass.annotate(RequestMapping.class)
        .param("value", "/modules/" + settings.getEntityName() + "s");
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




      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

  private JMethod buildGetPage(JDefinedClass definedClass, Settings settings,
    JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityName()+"sPage");
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
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "handle"+settings.getEntityName()+"CreateForm");
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
    tryBlock.body()._return(JExpr._new(codeModel.ref(ModelAndView.class)).arg(JExpr.lit("redirect:../"+settings.getEntityNameLowerCamel()+"s/").plus(entity.invoke("getId"))));

    JCatchBlock catchBlock = tryBlock._catch(codeModel.ref(Exception.class));
    catchBlock.body().add(JExpr.refthis("logger").invoke("error").arg( JExpr.lit("Error on creating article, name ").plus(createForm.invoke("getName")).plus(" already exists ")).arg(catchBlock.param("e")));

    catchBlock.body()._return(JExpr._this().invoke("createView").arg(createForm).arg(model));

    return method;
  }

  private JMethod buildGetCreatePage(JDefinedClass definedClass, Settings settings,
                                     JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "get"+settings.getEntityName()+"CreatePage");
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

}
