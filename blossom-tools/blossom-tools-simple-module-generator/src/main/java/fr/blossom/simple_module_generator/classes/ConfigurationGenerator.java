package fr.blossom.simple_module_generator.classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JLambda;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import fr.blossom.core.common.search.IndexationEngineImpl;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.simple_module_generator.EntityField;
import fr.blossom.simple_module_generator.GeneratorUtils;
import fr.blossom.simple_module_generator.Parameters;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
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

public class ConfigurationGenerator implements ClassGenerator {

  private final JDefinedClass entityClass;
  private final JDefinedClass repositoryClass;
  private final JDefinedClass daoClass;
  private final JDefinedClass daoImplClass;
  private final JDefinedClass dtoClass;
  private final JDefinedClass mapperClass;
  private final JDefinedClass serviceClass;
  private final JDefinedClass serviceImplClass;
  private final JDefinedClass controllerClass;

  public ConfigurationGenerator(JDefinedClass entityClass, JDefinedClass repositoryClass,
    JDefinedClass daoClass, JDefinedClass daoImplClass, JDefinedClass dtoClass,
    JDefinedClass mapperClass,
    JDefinedClass serviceClass, JDefinedClass serviceImplClass,
    JDefinedClass controllerClass) {
    this.entityClass = entityClass;
    this.repositoryClass = repositoryClass;
    this.daoClass = daoClass;
    this.daoImplClass = daoImplClass;
    this.dtoClass = dtoClass;
    this.mapperClass = mapperClass;
    this.serviceClass = serviceClass;
    this.serviceImplClass = serviceImplClass;
    this.controllerClass = controllerClass;
  }

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getConfigurationFullyQualifiedClassName(parameters));
      definedClass.annotate(Configuration.class);
      definedClass.annotate(EnableJpaRepositories.class)
        .param("basePackageClasses", repositoryClass.dotclass());
      definedClass.annotate(EntityScan.class).param("basePackageClasses", entityClass.dotclass());

      JMethod mapperBean = definedClass.method(JMod.PUBLIC, mapperClass, mapperClass.name());
      mapperBean.annotate(Bean.class);
      mapperBean.body()._return(JExpr._new(mapperClass));

      JMethod daoBean = definedClass.method(JMod.PUBLIC, daoClass, daoClass.name());
      daoBean.annotate(Bean.class);
      JVar daoBeanRepository = daoBean.param(repositoryClass, "repository");
      daoBean.body()._return(JExpr._new(daoImplClass).arg(daoBeanRepository));

      JMethod serviceBean = definedClass.method(JMod.PUBLIC, serviceClass, serviceClass.name());
      serviceBean.annotate(Bean.class);
      JVar serviceBeanDao = serviceBean.param(daoClass, "dao");
      JVar serviceBeanDTOMapper = serviceBean.param(mapperClass, "mapper");
      JVar serviceBeanApplicationEvent = serviceBean.param(ApplicationEventPublisher.class,"publisher");
      serviceBean.body()._return(JExpr._new(serviceImplClass).arg(serviceBeanDao).arg(serviceBeanDTOMapper).arg(serviceBeanApplicationEvent));

      JMethod indexationEngineBean = definedClass.method(JMod.PUBLIC, codeModel.ref(IndexationEngineImpl.class).narrow(dtoClass), parameters.getEntityNameLowerCamel()+"IndexationEngine");
      indexationEngineBean.annotate(Bean.class);
      JVar indexationEngineBeanEsClient = indexationEngineBean.param(Client.class, "client");
      JVar indexationEngineBeanService = indexationEngineBean.param(serviceClass, "service");
      JVar indexationEngineBeanBulkProcessor = indexationEngineBean.param(BulkProcessor.class, "bulkProcessor");
      JVar indexationEngineBeanObjectMapper = indexationEngineBean.param(ObjectMapper.class,"objectMapper");
      JVar indexationEngineBeanMappings = indexationEngineBean.param(Resource.class, "resource");
      indexationEngineBeanMappings.annotate(Value.class).param("value", "classpath:/elasticsearch/"+parameters.getEntityNameLowerUnderscore()+".json");
      JLambda typeLambda = new JLambda ();
      typeLambda.addParam("item");
      typeLambda.body().lambdaExpr(JExpr.lit(parameters.getEntityNameLowerUnderscore()));
      indexationEngineBean.body()._return(JExpr._new(codeModel.ref(IndexationEngineImpl.class).narrow(dtoClass))
        .arg(dtoClass.dotclass())
        .arg(indexationEngineBeanEsClient)
        .arg(indexationEngineBeanMappings)
        .arg(parameters.getEntityNameLowerUnderscore()+"s")
        .arg(typeLambda)
        .arg(indexationEngineBeanService)
        .arg(indexationEngineBeanBulkProcessor)
        .arg(indexationEngineBeanObjectMapper));

      JMethod searchEngineBean = definedClass.method(JMod.PUBLIC, codeModel.ref(SearchEngineImpl.class).narrow(dtoClass), parameters.getEntityNameLowerCamel()+"SearchEngine");
      searchEngineBean.annotate(Bean.class);
      JVar searchEngineBeanEsClient = searchEngineBean.param(Client.class, "client");
      JVar searchEngineBeanObjectMapper = searchEngineBean.param(ObjectMapper.class,"objectMapper");
      JInvocation searchableFields = codeModel.ref(Lists.class).staticInvoke("newArrayList");
      for(EntityField field : parameters.getFields()){
        if(field.isSearchable()){
          searchableFields.arg(field.getName());
        }
      }
      searchEngineBean.body()._return(JExpr._new(codeModel.ref(SearchEngineImpl.class).narrow(dtoClass))
        .arg(dtoClass.dotclass())
        .arg(searchEngineBeanEsClient)
        .arg(searchableFields)
        .arg(parameters.getEntityNameLowerUnderscore()+"s")
        .arg(searchEngineBeanObjectMapper));

      JMethod moduleMenuItemBean = definedClass.method(JMod.PUBLIC,MenuItem.class, "moduleMenuItem");
      moduleMenuItemBean.annotate(Bean.class);
      moduleMenuItemBean.annotate(ConditionalOnMissingBean.class).param("name","moduleMenuItem");
      moduleMenuItemBean.annotate(Order.class).param("value", 3);
      JVar builder = moduleMenuItemBean.param(MenuItemBuilder.class ,"builder");
      moduleMenuItemBean.body()._return(JExpr.ref(builder)
        .invoke("key").arg("modules")
        .invoke("label").arg("menu.modules").arg(true)
        .invoke("icon").arg("fa fa-puzzle-piece")
        .invoke("link").arg("/blossom/modules")
        .invoke("leaf").arg(false)
        .invoke("order").arg(Integer.MAX_VALUE - 1)
        .invoke("build"));

      JMethod readPrivilegeBean = definedClass.method(JMod.PUBLIC, Privilege.class, parameters.getEntityName()+"ReadPrivilegePlugin");
      readPrivilegeBean.annotate(Bean.class);
      readPrivilegeBean.body()._return(JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules").arg(parameters.getEntityNameLowerUnderscore()+"s").arg("read"));

      JMethod writePrivilegeBean = definedClass.method(JMod.PUBLIC, Privilege.class, parameters.getEntityNameLowerUnderscore()+"WritePrivilegePlugin");
      writePrivilegeBean.annotate(Bean.class);
      writePrivilegeBean.body()._return(JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules").arg(parameters.getEntityNameLowerUnderscore()+"s").arg("write"));

      JMethod createPrivilegeBean = definedClass.method(JMod.PUBLIC, Privilege.class, parameters.getEntityName()+"CreatePrivilegePlugin");
      createPrivilegeBean.annotate(Bean.class);
      createPrivilegeBean.body()._return(JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules").arg(parameters.getEntityNameLowerUnderscore()+"s").arg("create"));

      JMethod deletePrivilegeBean = definedClass.method(JMod.PUBLIC, Privilege.class, parameters.getEntityName()+"DeletePrivilegePlugin");
      deletePrivilegeBean.annotate(Bean.class);
      deletePrivilegeBean.body()._return(JExpr._new(codeModel.ref(SimplePrivilege.class)).arg("modules").arg(parameters.getEntityNameLowerUnderscore()+"s").arg("delete"));

      JMethod menuItemBean = definedClass.method(JMod.PUBLIC, MenuItem.class, parameters.getEntityName()+"MenuItem");
      menuItemBean.annotate(Bean.class);
      JVar builder2 = menuItemBean.param(MenuItemBuilder.class ,"builder");
      JVar parentMenu = menuItemBean.param(MenuItem.class ,"parentMenu");
      parentMenu.annotate(Qualifier.class).param("value", "moduleMenuItem");
      menuItemBean.body()._return(JExpr.ref(builder2)
        .invoke("key").arg(parameters.getEntityNameLowerUnderscore()+"s")
        .invoke("label").arg("menu."+parameters.getEntityNameLowerUnderscore()).arg(true)
        .invoke("icon").arg("fa fa-question")
        .invoke("link").arg("/blossom/modules/" + parameters.getEntityNameLowerUnderscore()+"s")
        .invoke("parent").arg(parentMenu)
        .invoke("privilege").arg(JExpr.invoke(readPrivilegeBean))
        .invoke("build"));



      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }
}
