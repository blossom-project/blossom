<#import "/spring.ftl" as spring>

<#macro switch checked name="" disabled=false>
  <#assign id= 'button_'+name+'_'+.now?long?c />
<div class="switch">
  <div class="onoffswitch">
    <input name="${name}" type="checkbox" <#if checked>checked</#if> <#if disabled>disabled</#if>
           class="onoffswitch-checkbox" id="${id}"/>
    <label class="onoffswitch-label" for="${id}">
      <span class="onoffswitch-inner"></span>
      <span class="onoffswitch-switch"></span>
    </label>
  </div>
</div>
</#macro>

<#macro delete id uri redirect="./">
<button type="button" data-href="${uri}" onclick="delete_${id}()" class="btn btn-danger">
  <i class="fa fa-trash"></i>
</button>
<script>
  var success_delete_${id} = function () {
    swal({
      title: "<@spring.message 'success'/>",
      text: "<@spring.message 'remaining.associations.deleted.text'/>",
      icon: "success",
      buttons: {
        confirm: {
          text: "<@spring.message 'ok'/>",
          value: true,
          visible: true,
          className: "",
          closeModal: true
        }
      }
    }).then(function (value) {
      window.location.href = '${redirect}';
    });
  };

  var buildTable_delete_${id} = function (data) {

    var div = document.createElement("div");
    var table = document.createElement("table");
    table.className = "table small";

    var tbody = document.createElement("tbody");

    $.each(data, function (prop, val) {
      var tr = document.createElement("tr");
      var tdType = document.createElement("td");
      tdType.innerText = prop;
      var tdCount = document.createElement("td");
      tdCount.innerText = val;

      tr.appendChild(tdType);
      tr.appendChild(tdCount);
      tbody.appendChild(tr);
    });
    table.appendChild(tbody);

    var text = document.createElement("p");
    text.innerHTML = "<@spring.message 'remaining.associations.text'/>";
    div.appendChild(text)
    div.appendChild(table);
    return div;
  };

  var delete_${id} = function () {
    var content = document.createElement("div");
    var contentTitle = document.createElement("div");
    contentTitle.className = "swal-title";
    contentTitle.innerHTML = "<@spring.message 'are_you_sure'/>";
    var contentText = document.createElement("div");
    contentText.className = "swal-text";
    contentText.innerHTML = "<@spring.message 'action_is_irreversible'/>";

    content.appendChild(contentTitle);
    content.appendChild(contentText);

    swal({
      content: content,
      icon: "warning",
      dangerMode: true,
      buttons: {
        cancel: {
          text: "<@spring.message 'cancel'/>",
          value: null,
          visible: true,
          className: "",
          closeModal: true,
        },
        confirm: {
          text: "<@spring.message 'delete'/>",
          value: true,
          visible: true,
          className: "",
          closeModal: true
        }
      }
    }).then(function (value) {
      if (!value) {
        return;
      }
      $.post('${uri}')
      .done(success_delete_${id})
      .fail(function (jqXHR) {
        swal({
          title: "<@spring.message 'remaining.associations.title'/>",
          content: buildTable_delete_${id}(jqXHR.responseJSON),
          icon: "warning",
          dangerMode: true,
          buttons: {
            cancel: {
              text: "<@spring.message 'cancel'/>",
              value: null,
              visible: true,
              className: "",
              closeModal: true,
            },
            confirm: {
              text: "<@spring.message 'delete.anyway'/>",
              value: true,
              visible: true,
              className: "",
              closeModal: true
            }
          }
        })
        .then(function (value) {
          if (value) {
            $.post('${uri}' + '?force=true')
            .done(success_delete_${id})
            .fail(function () {
              swal({
                title: "<@spring.message 'conflict'/>",
                text: "<@spring.message 'remaining.associations.deleted.error.text'/>",
                icon: "error",
                buttons: {
                  confirm: {
                    text: "<@spring.message 'ok'/>",
                    value: true,
                    visible: true,
                    className: "",
                    closeModal: true
                  }
                }
              });
            });
          }
        });
      })
    });
  }
</script>
</#macro>
