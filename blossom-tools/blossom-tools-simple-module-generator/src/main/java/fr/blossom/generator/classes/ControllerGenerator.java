package fr.blossom.generator.classes;

import com.google.common.base.Strings;
import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JConditional;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.generator.utils.GeneratorUtils;
import fr.blossom.generator.configuration.model.Settings;
import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public class ControllerGenerator implements ClassGenerator {

  private AbstractJClass dtoClass;
  private AbstractJClass serviceClass;


  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
    this.serviceClass = codeModel.ref(GeneratorUtils.getServiceFullyQualifiedClassName(settings));
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

      JMethod method = buildGetPage(definedClass, settings, codeModel, service, searchEngine);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

  private JMethod buildGetPage(JDefinedClass definedClass, Settings settings,
    JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
    JMethod method = definedClass.method(JMod.PUBLIC, ModelAndView.class, "getPage");
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

}
