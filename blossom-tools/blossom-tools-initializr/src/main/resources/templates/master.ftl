
<#macro default>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Blossom</title>

  <link href="/css/bootstrap.min.css" rel="stylesheet">
  <link href="/css/style.css" rel="stylesheet">
  <link href="/font-awesome/css/font-awesome.css" rel="stylesheet">
  <link href="/css/plugins/codemirror/codemirror.css" rel="stylesheet">
  <link href="/css/plugins/codemirror/ambiance.css" rel="stylesheet">

  <script src="/js/jquery-3.1.1.min.js"></script>
  <script src="/js/bootstrap.min.js"></script>
</head>

<body id="page-top" class="landing-page no-skin-config">

  <div class="navbar-wrapper">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container white-bg">
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
            <li><a class="page-scroll" href="/initializr">Generate</a></li>
            <li><a class="page-scroll" href="/documentation">Documentation</a></li>
            <li><a class="page-scroll" href="https://github.com/blossom-project/blossom/" target="_blank">Source code</a></li>
          </ul>
        </div>
      </div>
    </nav>
  </div>

  <#nested/>

  <script src="/js/plugins/codemirror/codemirror.js"></script>
  <script src="/js/plugins/codemirror/mode/properties/properties.js"></script>
  <script src="/js/plugins/codemirror/mode/yaml/yaml.js"></script>
  <script src="/js/plugins/codemirror/mode/clike/clike.js"></script>

</body>
</html>
</#macro>
