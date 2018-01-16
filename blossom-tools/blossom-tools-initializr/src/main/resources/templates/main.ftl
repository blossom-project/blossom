<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Blossom - Initializr</title>

  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/style.css" rel="stylesheet">
</head>

<body id="page-top" class="landing-page no-skin-config">

  <div class="navbar-wrapper">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header page-scroll">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Blossom</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right text-primary">
            <li><a class="page-scroll" href="https://blossom-project.github.io/blossom/" target="_blank">Documentation</a></li>
            <li><a class="page-scroll" href="https://github.com/blossom-project/blossom/" target="_blank">Source code</a></li>
          </ul>
        </div>
      </div>
    </nav>
  </div>


  <section id="generator" class="container">
    <div class="row">
      <div class="col-lg-12 text-center">
        <div class="navy-line"></div>
        <h1>Generate your project</h1>
        <p>Name it, select any modules and start your own Blossom-based project !</p>
      </div>
    </div>

    <form method="post" class="form-horizontal">
      <div class="row m-t-xl">
        <div class="col-md-6">
          <div class="form-group">
            <label class="col-sm-2 control-label">Blossom version</label>
            <div class="col-sm-10">
              <select class="form-control"  name="version"><#list initializr.versions as version><option value="${version.blossom}" <#if version?is_first>selected="selected"</#if>>${version.blossom}</option></#list></select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Group</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="groupId" value="${project.groupId}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">ArtifactId</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="artifactId" value="${project.artifactId}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Packaging mode</label>
            <div class="col-sm-10">
              <select class="form-control" name="packagingMode"><#list packagingModes as packagingMode><option value="${packagingMode}">${packagingMode}</option></#list></select>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label class="col-sm-2 control-label">Name</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="name" value="${project.name}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Description</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="description" value="${project.description}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Package name</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="packageName" value="${project.packageName}"/>
            </div>
          </div>
        </div>
      </div>

    <div class="row">
      <#list initializr.groups as group>
        <div class="panel col-md-6">
          <h2>${group.name}</h2>
          <p>${group.description}</p>

          <div>
            <#list group.dependencies as dependency>
              <div class="form-group">
                <div class="col-sm-12">
                  <input type="checkbox" value="${dependency.id}" name="dependencies" <#if project.dependencies?seq_contains(dependency.id)>checked="checked"</#if>/>
                  ${dependency.name}
                  <p class="text-muted text-normal">${dependency.description}</p>
                </div>
              </div>
            </#list>
          </div>
        </div>
      </#list>
    </div>

    <div class="row text-center">
      <button class="btn btn-lg btn-primary" type="submit">
        Generate project
      </button>
    </div>

    </form>
  </section>

  <script src="js/jquery-3.1.1.min.js"></script>
  <script src="js/bootstrap.min.js"></script>

</body>
</html>
