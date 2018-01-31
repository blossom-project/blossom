<#import "/spring.ftl" as spring>

<div id="jvm" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5><@spring.message "dashboard.jvm.status"/></h5>
  </div>
  <div class="ibox-content">
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>

    <div class="row">
      <div class="col-xs-12">
        <h3><@spring.message "dashboard.jvm.classes"/></h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
            <@spring.message "dashboard.jvm.classes.total"/>
            </th>
            <th>
            <@spring.message "dashboard.jvm.classes.loaded"/>
            </th>
            <th>
            <@spring.message "dashboard.jvm.classes.unloaded"/>
            </th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
            ${jvm.classes.total?c}
            </td>
            <td>
            ${jvm.classes.loaded?c}
            </td>
            <td>
            ${jvm.classes.unloaded?c}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="col-xs-12">
        <h3><@spring.message "dashboard.jvm.gc"/></h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
            <@spring.message "dashboard.jvm.gc.name"/>
            </th>
            <th>
            <@spring.message "dashboard.jvm.gc.count"/>
            </th>
            <th>
            <@spring.message "dashboard.jvm.gc.time"/>
            </th>
          </tr>
          </thead>
          <tbody>
          <#list jvm.gcs.types as type>
          <tr>
            <td>
            ${type.name}
            </td>
            <td>
            ${type.count?c}
            </td>
            <td>
             ${type.time?c} ms
            </td>
          </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-12">
        <h3><@spring.message "dashboard.jvm.threads"/></h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
            <@spring.message "dashboard.jvm.threads.live"/>
            </th>
            <th>
            <@spring.message "dashboard.jvm.threads.peak"/>
            </th>
            <th>
            <@spring.message "dashboard.jvm.threads.daemons"/>
            </th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
            ${jvm.threads.live?c}
            </td>
            <td>
            ${jvm.threads.peak?c}
            </td>
            <td>
            ${jvm.threads.daemon?c}
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="col-xs-12">
        <h3><@spring.message "dashboard.jvm.processors"/></h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
              <@spring.message "dashboard.jvm.processors.total"/>
            </th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
            ${jvm.processors.total?c}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
