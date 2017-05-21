<div class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>${folder.getName()}
            <small>(${folder.getPath()})</small>
        </h5>
        <div class="ibox-tools">
            <a class="btn btn-xs btn-rounded" href="#">
                <i class="fa fa-folder"></i>
            </a>
        </div>
    </div>

    <div class="ibox-content">
        <div class="row">
            <div class="col-lg-12">
            <#list files as file>
                <div class="file-box">
                    <div class="file">
                        <a href="#">
                            <span class="corner"></span>

                            <div class="icon">
                                <i class="fa fa-file"></i>
                            </div>
                            <div class="file-name">
                            ${file.name}
                                <br>
                                <small>Added: ${file.dateCreation?datetime}</small>
                            </div>
                        </a>
                    </div>

                </div>
            </#list>
            </div>
        </div>
    </div>
</div>