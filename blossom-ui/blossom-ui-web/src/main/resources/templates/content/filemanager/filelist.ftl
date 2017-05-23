<#function icon file>
  <#if file.extension == 'pdf' >
    <#return "fa fa-file-pdf-o">
  <#elseif file.extension == 'png' || file.extension == 'jpg' || file.extension == 'jpeg'|| file.extension == 'gif' >
    <#return "fa fa-photo">
  <#elseif file.extension == 'xls' || file.extension == 'xlsx' || file.extension == 'ods' | file.extension == 'csv'>
    <#return "fa fa fa-bar-chart-o">
  <#elseif file.extension == 'ppt' || file.extension == 'pptx' || file.extension == 'odp'>
    <#return "fa fa-file-powerpoint-o">
  <#elseif file.extension == 'docx' || file.extension == "doc" || file.extension == 'odt'>
    <#return "fa fa-file-text-o">
  <#elseif file.extension == 'xml' || file.extension == "js" ||file.extension == "css" || file.extension == 'json'>
    <#return "fa fa-file-code-o">
  <#elseif file.extension == 'tar.gz' || file.extension == "tar" || file.extension == "rar" || file.extension == "zip">
    <#return "fa fa-archive">
  <#elseif file.extension == 'mp3'>
    <#return "fa fa-volume-up">
  <#elseif file.extension == 'mp4'>
    <#return "fa fa-film">
  </#if>

  <#return "fa fa-file">
</#function>

<#if files.content?size gt 0>
  <#list files.content as file>
  <div class="file-box">
    <div class="file">
      <span class="corner"></span>
      <div class="icon">
        <i class="${icon(file)}"></i>
      </div>
      <div class="file-name">
        <a href="/files/${file.id?c}" target="_blank">${file.name}</a>
        <br/>
        <small>Added: ${file.dateCreation?datetime}</small>
      </div>
    </div>
  </div>

  <script>
    $(document).ready(function () {
      var heights = $(".file-box .file-name").map(function () {
        return $(this).height();
      }).get();

      var maxHeight = Math.max.apply(null, heights);

      $(".file-box .file-name").height(maxHeight);
    });

  </script>
  </#list>

  <#if !files.last>
  <div class="row">
    <div class="col-xs-12">
      <button type="button" class="btn btn-block btn-primary" onclick="appendFilelist();$(this).remove();">Show more
      </button>
    </div>
  </div>
  </#if>
<#else>
<div class="well-lg">No file found</div>
</#if>
