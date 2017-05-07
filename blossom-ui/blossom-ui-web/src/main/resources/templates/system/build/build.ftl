<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
    <h2>Informations de build</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom">Accueil</a>
      </li>
      <li class="active">
        <strong>Informations de build</strong>
      </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
  <div class="middle-box text-center animated fadeInDownBig">
    <div class="ibox float-e-margins">
      <div class="ibox-content no-padding">
        <table class="table">
          <tr>
            <td class="font-bold">GroupId</td>
            <td>${build.group}</td>
          </tr>
          <tr>
            <td class="font-bold">ArtifactId</td>
            <td>${build.artifact}</td>
          </tr>
          <tr>
            <td class="font-bold">Name</td>
            <td>${build.name}</td>
          </tr>
          <tr>
            <td class="font-bold">Version</td>
            <td>${build.version}</td>
          </tr>
          <tr>
            <td class="font-bold">Date</td>
            <td>${build.time?datetime}</td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</div>
</@master.default>
