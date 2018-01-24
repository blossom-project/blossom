<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2>This is main title</h2>
    <ol class="breadcrumb">
      <li>
        <a href="index.html">This is</a>
      </li>
      <li class="active">
        <strong>Breadcrumb</strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <a href="" class="btn btn-primary">This is action area</a>
    </div>
  </div>
</div>
<div class="wrapper wrapper-content">
  <div class="middle-box text-center">
    <h3 class="font-bold">This is page content</h3>
    <div class="error-desc">
      You can create here any grid layout you want. And any variation layout you imagine:) Check out
      main dashboard and other site. It use many different layout.
      <br><a href="index.html" class="btn btn-primary m-t">Dashboard</a>
    </div>
  </div>
</div>
</@master.default>
