<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>

<@master.default currentUser=currentUser>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-search"></i> <@spring.message "omnisearch.title"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "omnisearch.title"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-lg-12">
      <div class="ibox float-e-margins">
        <div class="ibox-content">
          <h2>
            <#assign sizeArgs = ["${total?c}"]/>
            <@spring.messageArgs "omnisearch.total" sizeArgs />&nbsp;<span class="text-navy">“${q}”</span>
          </h2>
          <small>
            <#assign durationArgs = ["${(duration / 1000)?c}"]/>
            <@spring.messageArgs "omnisearch.duration" durationArgs />
          </small>

          <div class="search-form m-b-lg">
            <form action="/blossom/search" method="GET">
              <div class="input-group">
                <input type="text" class="form-control input-lg" name="q" value="${q}" placeholder="<@spring.message "omnisearch.placeholder"/>">
                <div class="input-group-btn">
                  <button class="btn btn-lg btn-primary" type="submit">
                    <@spring.message "search"/>
                  </button>
                </div>
              </div>
            </form>
          </div>

          <#if results?size == 0>
            <div class="alert alert-default text-center">
              <@spring.message "list.no.element.found.label"/>
            </div>
          <#else>
          <div class="tabs-container">
            <ul class="nav nav-tabs">
                <#list results as key, searchResult>
                  <li class="<#if key?is_first>active</#if>"><a data-toggle="tab" href="#tab_${key?index}" aria-expanded="true">
                    <@spring.messageText key key/> (${searchResult.page.totalElements?c})
                  </a></li>
                </#list>
            </ul>
            <div class="tab-content">
              <#list results as key, searchResult>
                <div id="tab_${key?index}" class="tab-pane <#if key?is_first>active</#if>">
                  <div class="panel-body">
                    <#if searchResult.page.content?size gt 0>
                      <#list searchResult.page.content as item>
                        <div class="hr-line-dashed"></div>
                        <div class="search-result">
                          <h3><a href="${item.uri}">${item.name}</a></h3>
                          <a href="${item.uri}" class="search-link">${item.uri}</a>
                          <p>
                            ${item.description!''}
                          </p>
                        </div>
                      </#list>
                    <#else>
                      No result
                    </#if>
                  </div>
                </div>
              </#list>
            </div>
          </div>
          </#if>
        </div>
      </div>
    </div>
  </div>
</div>
</@master.default>
