<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-4">
        <h2><@spring.message "caches.title"/></h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
            <li>
                <a href="/blossom/system"><@spring.message "menu.system"/></a>
            </li>
            <li class="active">
                <strong><@spring.message "caches.title"/></strong>
            </li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content scheduler">
    <table class="table table-stripped">
        <thead>
        <tr>
            <th>Name</th>
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
                ${caches[cacheKey].hitCount()}
                </td>

                <td>
                ${caches[cacheKey].missCount()}
                </td>

                <td>
                ${caches[cacheKey].loadSuccessCount()}
                </td>

                <td>
                ${caches[cacheKey].loadFailureCount()}
                </td>

                <td>
                ${caches[cacheKey].totalLoadTime()}
                </td>

                <td>
                ${caches[cacheKey].evictionCount()}
                </td>

                <td>
                ${caches[cacheKey].evictionWeight()}
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</@master.default>
