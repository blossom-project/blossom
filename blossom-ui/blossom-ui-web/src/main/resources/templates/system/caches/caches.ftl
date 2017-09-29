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
          <th>Action</th>
          <th>Estimated size</th>
          <th>Hit count</th>
          <th>Miss count</th>
          <th>Load success</th>
          <th>Load failures</th>
          <th>Total load time</th>
          <th>Eviction count</th>
          <th>Eviction weight</th>
        </tr>
        </thead>
        <tbody>
          <#list caches?keys as cacheKey>
          <tr>
            <td>
            ${cacheKey}
            </td>
            <td>
              <button class="btn btn-xs btn-danger" data-action="emptyCache" data-cache="${cacheKey}"><i
                class="fa fa-trash"></i></button>
            </td>
            <td>
            ${caches[cacheKey].size?c}
            </td>
            <td>
            ${caches[cacheKey].stats.hitCount()}
            </td>

            <td>
            ${caches[cacheKey].stats.missCount()}
            </td>

            <td>
            ${caches[cacheKey].stats.loadSuccessCount()}
            </td>

            <td>
            ${caches[cacheKey].stats.loadFailureCount()}
            </td>

            <td>
            ${caches[cacheKey].stats.totalLoadTime()}
            </td>

            <td>
            ${caches[cacheKey].stats.evictionCount()}
            </td>

            <td>
            ${caches[cacheKey].stats.evictionWeight()}
            </td>
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
  });
</script>
</@master.default>
