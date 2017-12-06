<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2>
      <i class="fa fa-magnet"></i>
      <@spring.message "menu.system.caches"/>
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/system"><@spring.message "menu.system"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.caches"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <div class="ibox float-e-margins">
    <div class="ibox-content">
      <div class="row">
        <div class="col-sm-9 m-b-xs">
        </div>
        <div class="col-sm-3">
          <div class="input-group">
            <input type="text"
                   placeholder="<@spring.message "list.searchbar.placeholder"/>"
                   class="table-search input-sm form-control"
                   onkeyup="var which = event.which || event.keyCode;if(which === 13) {$(this).closest('.input-group').find('button.table-search').first().click();}"
              <#if q?has_content> value="${q}"</#if>
            />


            <span class="input-group-btn">
              <button type="button"
                      class="btn btn-sm btn-primary table-search"
                      onclick="var value = $(this).closest('.input-group').children('input.table-search').first().val();window.location.href = $.updateQueryStringParameter(window.location.href,'q',value);">
                  <i class="fa fa-search"></i>
              </button>
            </span>
          </div>
        </div>
      </div>

      <table class="table table-stripped">
        <thead>
        <tr>
          <th>Name</th>
          <th>Specification</th>
          <th>Action</th>
          <th>Estimated size</th>
          <th>Hit count</th>
          <th>Miss count</th>
          <th>Eviction count</th>
        </tr>
        </thead>
        <tbody>
          <#list caches?keys as cacheKey>
          <tr>
            <td>
              ${cacheKey}
            </td>
            <td>
              ${caches[cacheKey].cache.configuration.specification()!''}
            </td>
            <td>
              <button class="btn btn-xs btn-danger" data-action="emptyCache" data-cache="${cacheKey}"><i class="fa fa-trash"></i></button>
              <#if caches[cacheKey].cache.enabled>
                <button class="btn btn-xs btn-warning" data-action="disableCache" data-cache="${cacheKey}"><i class="fa fa-power-off"></i></button>
              <#else>
                <button class="btn btn-xs btn-primary" data-action="enableCache" data-cache="${cacheKey}"><i class="fa fa-play-circle-o"></i></button>
              </#if>
            </td>
            <td>${caches[cacheKey]["size"]?c}</td>
            <td>${caches[cacheKey]["hits"]}</td>
            <td>${caches[cacheKey]["misses"]}</td>
            <td>${caches[cacheKey]["evictions"]}</td>
          </tr>
          </#list>
        </tbody>
      </table>
    </div>
  </div>
</div>


<script>
  $(document).ready(function () {
    $("[data-action='emptyCache']").click(function (e) {
      var cache = $(this).data("cache");

      $.post("caches/" + cache + "/_empty", function () {
        window.location.reload(true);
      });
    });

    $("[data-action='enableCache']").click(function (e) {
      var cache = $(this).data("cache");

      $.post("caches/" + cache + "/_enable", function () {
        window.location.reload(true);
      });
    });

    $("[data-action='disableCache']").click(function (e) {
      var cache = $(this).data("cache");

      $.post("caches/" + cache + "/_disable", function () {
        window.location.reload(true);
      });
    });
  });
</script>
</@master.default>
