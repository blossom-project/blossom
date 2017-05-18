<div id="jvm" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5>JVM</h5>
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
      <div class="col-xs-12 col-lg-6">
        <h3>Classes</h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
              Total
            </th>
            <th>
              Loaded
            </th>
            <th>
              Unloaded
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
      <div class="col-xs-12 col-lg-6">
        <h3>GC</h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
              Name
            </th>
            <th>
              Count
            </th>
            <th>
              Time
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
            ${type.time?c}
            </td>
          </tr>
          </#list>
          </tbody>
        </table>
      </div>
    </div>
    <div class="row">
      <div class="col-xs-12 col-lg-6">
        <h3>Threads</h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
              Total
            </th>
            <th>
              Total started
            </th>
            <th>
              Peak
            </th>
            <th>
              Daemons
            </th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
            ${jvm.threads.total?c}
            </td>
            <td>
            ${jvm.threads.totalStarted?c}
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

      <div class="col-xs-12 col-lg-6">
        <h3>Processors</h3>
        <table class="table table-stripped small">
          <thead>
          <tr>
            <th>
              Total
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
