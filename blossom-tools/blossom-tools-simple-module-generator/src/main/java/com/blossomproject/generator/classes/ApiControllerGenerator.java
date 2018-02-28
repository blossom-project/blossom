package com.blossomproject.generator.classes;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.utils.GeneratorUtils;
import com.blossomproject.ui.stereotype.BlossomApiController;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.helger.jcodemodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

public class ApiControllerGenerator implements ClassGenerator {

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
                    ._class(GeneratorUtils.getApiControllerFullyQualifiedClassName(settings));
            definedClass.annotate(BlossomApiController.class);
            definedClass.annotate(RequestMapping.class)
                    .param("value", "/modules/" + settings.getEntityNameLowerUnderscore() + "s");

            JFieldVar service = definedClass.field(JMod.PRIVATE + JMod.FINAL, serviceClass, "service");

            JFieldVar searchEngine = definedClass.field(JMod.PRIVATE + JMod.FINAL, codeModel.ref(
                    SearchEngineImpl.class).narrow(dtoClass), "searchEngine");


            JMethod constructor = definedClass.constructor(JMod.PUBLIC);
            constructor.body()
                    .assign(JExpr.refthis(service.name()), constructor.param(serviceClass, "service"));
            constructor.body().assign(JExpr.refthis(searchEngine.name()),
                    constructor.param(codeModel.ref(SearchEngineImpl.class).narrow(dtoClass), "searchEngine"));

            JMethod methodList = buildList(definedClass, settings, codeModel, service, searchEngine);

            JMethod methodCreate = buildCreate(definedClass, settings, codeModel, service, searchEngine);

            JMethod methodGet = buildGet(definedClass, codeModel, settings, service);

            JMethod methodUpdate = buildUpdate(definedClass, codeModel, settings, service);

            JMethod methodDelete = buildDelete(definedClass, settings, codeModel, service, searchEngine);


            return definedClass;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can't generate repository class", e);
        }
    }

    private JMethod buildList(JDefinedClass definedClass, Settings settings,
                              JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.ref(Page.class).narrow(dtoClass), "list");
        method.annotate(GetMapping.class);
        method.annotate(PreAuthorize.class).param("value",
                "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:read')");

        JVar query = method.param(String.class, "q");
        query.annotate(RequestParam.class).param("value", "q").param("required", false);

        JVar pageable = method.param(Pageable.class, "pageable");
        pageable.annotate(PageableDefault.class).param("size", 25);

        JBlock body = method.body();

        JConditional ifCondition = body
                ._if(codeModel.ref(Strings.class).staticInvoke("isNullOrEmpty").arg(query));
        ifCondition._then()._return(JExpr.refthis(service.name()).invoke("getAll").arg(pageable));
        ifCondition._else()._return(
                JExpr.refthis(searchEngine.name()).invoke("search").arg(query).arg(pageable)
                        .invoke("getPage"));

        return method;
    }

    private JMethod buildCreate(JDefinedClass definedClass, Settings settings,
                                JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {
        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.ref(ResponseEntity.class).narrow(dtoClass), "create");
        method.annotate(PostMapping.class);
        method.annotate(PreAuthorize.class).param("value",
                "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:create')");
        method._throws(codeModel.ref(Exception.class));

        JVar createForm = method.param(createFormClass, "createForm");
        createForm.annotate(NotNull.class);
        createForm.annotate(Valid.class);
        createForm.annotate(RequestBody.class);

        JBlock body = method.body();

        body.staticInvoke(codeModel.ref(Preconditions.class), "checkArgument").arg(JExpr.ref(createForm).neNull());

        body._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(JExpr.ref(service).invoke("create").arg(createForm)).arg(codeModel.ref(HttpStatus.class).staticRef("CREATED")));

        return method;
    }


    private JMethod buildGet(JDefinedClass definedClass,
                             JCodeModel codeModel, Settings settings, JFieldVar service) {
        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.ref(ResponseEntity.class).narrow(dtoClass), "get");
        method.annotate(GetMapping.class).param("value", "/{id}");
        method.annotate(PreAuthorize.class).param("value",
                "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:read')");

        JVar id = method.param(Long.class, "id");
        id.annotate(PathVariable.class);

        JBlock body = method.body();

        body.staticInvoke(codeModel.ref(Preconditions.class), "checkArgument").arg(JExpr.ref(id).neNull());

        JVar entity = body.decl(dtoClass, "entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

        JConditional ifCondition = body._if(entity.eqNull());
        ifCondition._then()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(codeModel.ref(HttpStatus.class).staticRef("NOT_FOUND")));
        ifCondition._else()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(entity).arg(codeModel.ref(HttpStatus.class).staticRef("OK")));

        return method;
    }

    private JMethod buildUpdate(JDefinedClass definedClass,
                                JCodeModel codeModel, Settings settings, JFieldVar service) {
        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.ref(ResponseEntity.class).narrow(dtoClass), "update");
        method.annotate(PutMapping.class).param("value", "/{id}");
        method.annotate(PreAuthorize.class).param("value",
                "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:write')");

        JVar id = method.param(Long.class, "id");
        id.annotate(PathVariable.class);

        JVar updateForm = method.param(updateFormClass, "updateForm");
        updateForm.annotate(Valid.class);
        updateForm.annotate(RequestBody.class);

        JBlock body = method.body();

        body.staticInvoke(codeModel.ref(Preconditions.class), "checkArgument").arg(JExpr.ref(id).neNull());

        JVar entity = body.decl(dtoClass, "entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

        JConditional ifCondition = body._if(entity.eqNull());
        ifCondition._then()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(codeModel.ref(HttpStatus.class).staticRef("NOT_FOUND")));
        ifCondition._else()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(JExpr.ref(service).invoke("update").arg(id).arg(updateForm)).arg(codeModel.ref(HttpStatus.class).staticRef("OK")));

        return method;
    }


    private JMethod buildDelete(JDefinedClass definedClass, Settings settings,
                                JCodeModel codeModel, JFieldVar service, JFieldVar searchEngine) {

        JNarrowedClass response = codeModel.ref(ResponseEntity.class).narrow(codeModel.ref(Map.class).narrow(codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends()), codeModel.ref(Long.class)));

        JMethod method = definedClass.method(JMod.PUBLIC, response, "delete");
        method.annotate(DeleteMapping.class).param("value", "/{id}");
        method.annotate(PreAuthorize.class).param("value",
                "hasAuthority('modules:" + settings.getEntityNameLowerUnderscore() + "s:delete')");

        JVar id = method.param(Long.class, "id");
        id.annotate(PathVariable.class);

        JVar force = method.param(Boolean.class, "force");
        force.annotate(RequestParam.class).param("value", "force").param("required", false).param("defaultValue", "false");

        JBlock body = method.body();

        JVar entity = body.decl(dtoClass, "entity", JExpr.refthis(service.name()).invoke("getOne").arg(id));

        JConditional ifCondition = body._if(entity.eqNull());
        ifCondition._then()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(codeModel.ref(HttpStatus.class).staticRef("NOT_FOUND")));

        JVar result = body.decl(
                codeModel.ref(Optional.class).narrow(codeModel.ref(Map.class).narrow(codeModel.ref(Class.class).narrow(codeModel.ref(AbstractDTO.class).wildcardExtends()), codeModel.ref(Long.class)))
                , "result",
                JExpr.refthis(service.name()).invoke("delete").arg(entity).arg(force));

        JConditional conditional = body._if(result.invoke("isPresent").not().cor(result.invoke("get").invoke("isEmpty")));
        conditional._then()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(codeModel.ref(HttpStatus.class).staticRef("OK")));

        conditional._else()._return(JExpr._new(codeModel.ref(ResponseEntity.class).narrowEmpty()).arg(result.invoke("get")).arg(codeModel.ref(HttpStatus.class).staticRef("CONFLICT")));

        return method;
    }

}
