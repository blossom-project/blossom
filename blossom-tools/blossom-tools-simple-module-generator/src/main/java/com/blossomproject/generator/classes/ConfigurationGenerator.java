package com.blossomproject.generator.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JLambda;
import com.helger.jcodemodel.JLambdaParam;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.IndexationEngineConfiguration;
import com.blossomproject.core.common.search.IndexationEngineConfigurationImpl;
import com.blossomproject.core.common.search.IndexationEngineImpl;
import com.blossomproject.core.common.search.SearchEngineConfiguration;
import com.blossomproject.core.common.search.SearchEngineConfigurationImpl;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SummaryDTO.SummaryDTOBuilder;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.utils.GeneratorUtils;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import java.util.function.Function;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.plugin.core.PluginRegistry;

public class ConfigurationGenerator implements ClassGenerator {

  private AbstractJClass entityClass;
  private AbstractJClass repositoryClass;
  private AbstractJClass daoClass;
  private AbstractJClass daoImplClass;
  private AbstractJClass dtoClass;
  private AbstractJClass mapperClass;
  private AbstractJClass serviceClass;
  private AbstractJClass serviceImplClass;
  private AbstractJClass controllerClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.entityClass = codeModel
      .ref(GeneratorUtils.getConfigurationFullyQualifiedClassName(settings));
    ;
    this.repositoryClass = codeModel
      .ref(GeneratorUtils.getRepositoryFullyQualifiedClassName(settings));
    this.daoClass = codeModel.ref(GeneratorUtils.getDaoFullyQualifiedClassName(settings));
    this.daoImplClass = codeModel.ref(GeneratorUtils.getDaoImplFullyQualifiedClassName(settings));
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
    this.mapperClass = codeModel.ref(GeneratorUtils.getMapperFullyQualifiedClassName(settings));
    ;
    this.serviceClass = codeModel.ref(GeneratorUtils.getServiceFullyQualifiedClassName(settings));
    this.serviceImplClass = codeModel
      .ref(GeneratorUtils.getServiceImplFullyQualifiedClassName(settings));
    this.controllerClass = codeModel
      .ref(GeneratorUtils.getControllerFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = generateClass(settings, codeModel);

      JFieldVar associationRegistry = appendAssociationRegistry(codeModel, definedClass);

      appendMapperBean(definedClass);

      appendDaoBean(definedClass);

      appendServiceBean(definedClass, associationRegistry);

      appendIndexationConfiguration(settings, codeModel, definedClass);

      appendIndexationEngineBean(settings, codeModel, definedClass);

      appendSearchEngineConfiguration(settings, codeModel, definedClass);

      appendSearchEngineBean(settings, codeModel, definedClass);

      appendModuleMenuItemBean(definedClass);

      JMethod readPrivilegeBean = appendReadPrivilegeBean(settings, codeModel, definedClass);

      appendWritePrivilegeBean(settings, codeModel, definedClass);

      appendCreatePrivilegeBean(settings, codeModel, definedClass);

      appendDeletePrivilege(settings, codeModel, definedClass);

      appendMenuItemBean(settings, definedClass, readPrivilegeBean);

      appendControllerBean(definedClass, codeModel);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }


  private void appendMenuItemBean(Settings settings, JDefinedClass definedClass,
    JMethod readPrivilegeBean) {
    JMethod menuItemBean = definedClass
      .method(JMod.PUBLIC, MenuItem.class, settings.getEntityName() + "MenuItem");
    menuItemBean.annotate(Bean.class);
    JVar builder2 = menuItemBean.param(MenuItemBuilder.class, "builder");
    JVar parentMenu = menuItemBean.param(MenuItem.class, "parentMenu");
    parentMenu.annotate(Qualifier.class).param("value", "moduleMenuItem");
    menuItemBean.body()._return(JExpr.ref(builder2)
      .invoke("key").arg(settings.getEntityNameLowerUnderscore() + "s")
      .invoke("label").arg("menu." + settings.getEntityNameLowerUnderscore())
      .invoke("icon").arg("fa fa-question")
      .invoke("link").arg("/blossom/modules/" + settings.getEntityNameLowerUnderscore() + "s")
      .invoke("parent").arg(parentMenu)
      .invoke("privilege").arg(JExpr.invoke(readPrivilegeBean))
      .invoke("build"));
  }

  private void appendDeletePrivilege(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod deletePrivilegeBean = definedClass
      .method(JMod.PUBLIC, Privilege.class, settings.getEntityName() + "DeletePrivilegePlugin");
    deletePrivilegeBean.annotate(Bean.class);
    deletePrivilegeBean.body()._return(
      JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules")
        .arg(settings.getEntityNameLowerUnderscore() + "s").arg("delete"));
  }

  private void appendCreatePrivilegeBean(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod createPrivilegeBean = definedClass
      .method(JMod.PUBLIC, Privilege.class, settings.getEntityName() + "CreatePrivilegePlugin");
    createPrivilegeBean.annotate(Bean.class);
    createPrivilegeBean.body()._return(
      JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules")
        .arg(settings.getEntityNameLowerUnderscore() + "s").arg("create"));
  }

  private void appendWritePrivilegeBean(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod writePrivilegeBean = definedClass.method(JMod.PUBLIC, Privilege.class,
      settings.getEntityName() + "WritePrivilegePlugin");
    writePrivilegeBean.annotate(Bean.class);
    writePrivilegeBean.body()._return(
      JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules")
        .arg(settings.getEntityNameLowerUnderscore() + "s").arg("write"));
  }

  private JMethod appendReadPrivilegeBean(Settings settings, JCodeModel codeModel, JDefinedClass definedClass) {
    JMethod readPrivilegeBean = definedClass
      .method(JMod.PUBLIC, Privilege.class, settings.getEntityName() + "ReadPrivilegePlugin");
    readPrivilegeBean.annotate(Bean.class);
    readPrivilegeBean.body()._return(JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules").arg(settings.getEntityNameLowerUnderscore() + "s").arg("read"));
    return readPrivilegeBean;
  }

  private void appendModuleMenuItemBean(JDefinedClass definedClass) {
    JMethod moduleMenuItemBean = definedClass
      .method(JMod.PUBLIC, MenuItem.class, "moduleMenuItem");
    moduleMenuItemBean.annotate(Bean.class);
    moduleMenuItemBean.annotate(ConditionalOnMissingBean.class).param("name", "moduleMenuItem");
    moduleMenuItemBean.annotate(Order.class).param("value", 3);
    JVar builder = moduleMenuItemBean.param(MenuItemBuilder.class, "builder");
    moduleMenuItemBean.body()._return(JExpr.ref(builder)
      .invoke("key").arg("modules")
      .invoke("label").arg("menu.modules")
      .invoke("icon").arg("fa fa-puzzle-piece")
      .invoke("link").arg("/blossom/modules")
      .invoke("leaf").arg(false)
      .invoke("order").arg(Integer.MAX_VALUE - 1)
      .invoke("build"));
  }


  private void appendSearchEngineConfiguration(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod searchEngineConfigurationBean = definedClass
      .method(JMod.PUBLIC, codeModel.ref(SearchEngineConfiguration.class).narrow(dtoClass), settings.getEntityNameLowerCamel() + "SearchEngineConfiguration");
    searchEngineConfigurationBean.annotate(Bean.class);

    JInvocation searchableFields = codeModel.ref(Lists.class).staticInvoke("newArrayList");
    for(Field field : settings.getFields()){
      if(field.isSearchable()){
        searchableFields.arg(field.getName());
      }
    }

    searchEngineConfigurationBean.body()._return(
      JExpr._new(codeModel.ref(SearchEngineConfigurationImpl.class).narrow(dtoClass))
        .arg(settings.getEntityName())
        .arg(settings.getEntityNameLowerUnderscore() + "s")
        .arg(dtoClass.dotclass())
        .arg(searchableFields)
    );
  }

  private void appendSearchEngineBean(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod searchEngineBean = definedClass
      .method(JMod.PUBLIC, codeModel.ref(SearchEngineImpl.class).narrow(dtoClass),
        settings.getEntityNameLowerCamel() + "SearchEngine");
    searchEngineBean.annotate(Bean.class);
    JVar searchEngineBeanEsClient = searchEngineBean.param(Client.class, "client");
    JVar searchEngineBeanObjectMapper = searchEngineBean
      .param(ObjectMapper.class, "objectMapper");
    JVar searchEngineBeanSearchEngineConfiguration = searchEngineBean
      .param(codeModel.ref(SearchEngineConfiguration.class).narrow(dtoClass),
        settings.getEntityNameLowerCamel() + "SearchEngineConfiguration");

    searchEngineBean.body()
      ._return(JExpr._new(codeModel.ref(SearchEngineImpl.class).narrow(dtoClass))
        .arg(searchEngineBeanEsClient)
        .arg(searchEngineBeanObjectMapper)
        .arg(searchEngineBeanSearchEngineConfiguration));
  }

  private void appendIndexationEngineBean(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod indexationEngineBean = definedClass
      .method(JMod.PUBLIC, codeModel.ref(IndexationEngineImpl.class).narrow(dtoClass),
        settings.getEntityNameLowerCamel() + "IndexationEngine");
    indexationEngineBean.annotate(Bean.class);
    JVar indexationEngineBeanEsClient = indexationEngineBean.param(Client.class, "client");
    JVar indexationEngineBeanService = indexationEngineBean.param(serviceClass, "service");
    JVar indexationEngineBeanBulkProcessor = indexationEngineBean
      .param(BulkProcessor.class, "bulkProcessor");
    JVar indexationEngineBeanObjectMapper = indexationEngineBean
      .param(ObjectMapper.class, "objectMapper");
    JVar indexationEngineBeanIndexationEngineConfiguration = indexationEngineBean
      .param(codeModel.ref(
        IndexationEngineConfiguration.class).narrow(dtoClass),
        settings.getEntityNameLowerCamel() + "IndexationEngineConfiguration");

    indexationEngineBean.body()
      ._return(JExpr._new(codeModel.ref(IndexationEngineImpl.class).narrow(dtoClass))
        .arg(indexationEngineBeanEsClient)
        .arg(indexationEngineBeanService)
        .arg(indexationEngineBeanBulkProcessor)
        .arg(indexationEngineBeanObjectMapper)
        .arg(indexationEngineBeanIndexationEngineConfiguration));
  }

  private JDefinedClass generateClass(Settings settings, JCodeModel codeModel)
    throws JClassAlreadyExistsException {
    JDefinedClass definedClass = codeModel
      ._class(GeneratorUtils.getConfigurationFullyQualifiedClassName(settings));
    definedClass.annotate(Configuration.class);
    definedClass.annotate(EnableJpaRepositories.class)
      .param("basePackageClasses", repositoryClass.dotclass());
    definedClass.annotate(EntityScan.class).param("basePackageClasses", entityClass.dotclass());
    return definedClass;
  }

  private JFieldVar appendAssociationRegistry(JCodeModel codeModel, JDefinedClass definedClass) {
    JFieldVar associationRegistry = definedClass
      .field(JMod.PRIVATE, codeModel.ref(PluginRegistry.class)
          .narrow(codeModel.ref(AssociationServicePlugin.class),
            codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends())),
        "associationRegistry");
    associationRegistry.annotate(Autowired.class);
    associationRegistry.annotate(Qualifier.class).param("value",
      codeModel.ref(PluginConstants.class).staticRef("PLUGIN_ASSOCIATION_SERVICE"));
    return associationRegistry;
  }

  private void appendMapperBean(JDefinedClass definedClass) {
    JMethod mapperBean = definedClass.method(JMod.PUBLIC, mapperClass, mapperClass.name());
    mapperBean.annotate(Bean.class);
    mapperBean.body()._return(JExpr._new(mapperClass));
  }

  private void appendDaoBean(JDefinedClass definedClass) {
    JMethod daoBean = definedClass.method(JMod.PUBLIC, daoClass, daoClass.name());
    daoBean.annotate(Bean.class);
    JVar daoBeanRepository = daoBean.param(repositoryClass, "repository");
    daoBean.body()._return(JExpr._new(daoImplClass).arg(daoBeanRepository));
  }

  private void appendServiceBean(JDefinedClass definedClass, JFieldVar associationRegistry) {
    JMethod serviceBean = definedClass.method(JMod.PUBLIC, serviceClass, serviceClass.name());
    serviceBean.annotate(Bean.class);
    JVar serviceBeanDao = serviceBean.param(daoClass, "dao");
    JVar serviceBeanDTOMapper = serviceBean.param(mapperClass, "mapper");
    JVar serviceBeanApplicationEvent = serviceBean
      .param(ApplicationEventPublisher.class, "publisher");
    serviceBean.body()._return(
      JExpr._new(serviceImplClass).arg(serviceBeanDao).arg(serviceBeanDTOMapper)
        .arg(serviceBeanApplicationEvent).arg(associationRegistry));
  }


  private void appendControllerBean(JDefinedClass definedClass, JCodeModel codeModel) {
    JMethod controllerBean = definedClass.
            method(JMod.PUBLIC, controllerClass, controllerClass.name());


    controllerBean.annotate(Bean.class);
    controllerBean.annotate(ConditionalOnMissingBean.class).param("value", controllerClass);
    JVar controllerBeanService = controllerBean.param(serviceClass, "service");
    JVar controllerBeanSearchEngine = controllerBean
            .param(codeModel.ref(SearchEngineImpl.class).narrow(dtoClass), "searchEngine");

    controllerBean.body()._return(JExpr._new(controllerClass).arg(controllerBeanService)
            .arg(controllerBeanSearchEngine));
  }

  private void appendIndexationConfiguration(Settings settings, JCodeModel codeModel,
    JDefinedClass definedClass) {
    JMethod indexationEngineConfigurationBean = definedClass
      .method(JMod.PUBLIC, codeModel.ref(IndexationEngineConfiguration.class).narrow(dtoClass),
        settings.getEntityNameLowerCamel() + "IndexationEngineConfiguration");
    indexationEngineConfigurationBean.annotate(Bean.class);
    JVar indexationEngineConfigurationBeanMappings = indexationEngineConfigurationBean
      .param(Resource.class, "resource");
    indexationEngineConfigurationBeanMappings.annotate(Value.class).param("value",
      "classpath:/elasticsearch/" + settings.getEntityNameLowerUnderscore() + ".json");

    JLambda typeLambda = new JLambda();
    typeLambda.addParam("item");
    typeLambda.body().lambdaExpr(JExpr.lit(settings.getEntityNameLowerUnderscore()));

    JVar typeFunction = indexationEngineConfigurationBean.body().decl(
      codeModel.ref(Function.class).narrow(dtoClass, codeModel.ref(String.class)),
      "typeFunction", typeLambda);

    JLambda summaryLambda = new JLambda();
    JLambdaParam summaryLambdaParam = summaryLambda.addParam("item");

    JInvocation summaryLambdaBody =
      codeModel
        .ref(SummaryDTOBuilder.class)
        .staticInvoke("create")
        .invoke("id").arg(summaryLambdaParam.invoke("getId"))
        .invoke("type").arg(typeFunction.invoke("apply").arg(summaryLambdaParam))
        .invoke("name").arg(summaryLambdaParam.invoke("getId").invoke("toString"))
        .invoke("description").arg("")
        .invoke("uri").arg(JExpr.lit("/modules/" + settings.getEntityNameLowerUnderscore() + "s/").plus(summaryLambdaParam.invoke("getId")))
        .invoke("build");

    summaryLambda.body().lambdaExpr(summaryLambdaBody);

    indexationEngineConfigurationBean.body()._return(
      JExpr._new(codeModel.ref(IndexationEngineConfigurationImpl.class).narrow(dtoClass))
        .arg(dtoClass.dotclass())
        .arg(indexationEngineConfigurationBeanMappings)
        .arg(settings.getEntityNameLowerUnderscore() + "s")
        .arg(typeLambda)
        .arg(summaryLambda)
    );
  }
}
