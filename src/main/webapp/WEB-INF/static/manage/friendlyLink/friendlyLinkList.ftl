<#assign menu="friendlyLink">
<#assign submenu="cog">
<#include "/manage/head.ftl">

<section id="main-content">
    <section class="wrapper">
        <!-- page start-->
        <div class="row">
            <div class="col-lg-4">
                <section class="panel">
                    <header class="panel-heading"> 添加链接</header>
                    <div class="panel-body">
                        <form  method="post" class="form-horizontal basicForm"
                               autocomplete="off"
                               action="${BASE_PATH}/manage/friendlyLink/addNew.json">
                            <fieldset>
                                <div class="form-group">
                                    <label class="col-sm-3 col-sm-3 control-label">名称</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="name"
                                               id="adminName" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 col-sm-3 control-label">链接地址</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="url" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label">链接状态</label>
                                    <div class="col-xs-9">
                                        <div class="col-sm-10" style="margin-bottom:10px;">
                                            <div class="checkbox checkbox-info" style="display: inline-block;margin: 0;padding:0">
                                                <input type="radio" name="status" id="radio1" value="display" checked>
                                                <label for="radio1">
                                                    显示
                                                </label>
                                            </div>
                                            <div class="checkbox checkbox-info" style="display: inline-block;padding-top:0">
                                                <input type="radio" name="status" id="radio2" value="hidden" >
                                                <label for="radio2">
                                                    隐藏
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 col-sm-3 control-label"></label>
                                    <div class="col-sm-9">
                                        <button class="btn btn-danger" type="submit">增加</button>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </section>
            </div>
            <div class="col-lg-8">
                <section class="panel">
                    <header class="panel-heading"> 链接列表</header>
                    <div class="panel-body">
                        <div class="adv-table">
                            <div role="grid" class="dataTables_wrapper"
                                 id="hidden-table-info_wrapper">
                                <table class="table table-striped table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th>顺序</th>
                                        <th>名称</th>
                                        <th>地址</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody role="alert" aria-live="polite" aria-relevant="all">
                                    <#list list as l>
                                    <tr class="gradeA odd">
                                        <td class="folderSort"><input type="number"
                                                                      folderId="${l.id}" value="${l.sort}"
                                                                      name="sort"
                                                                      class="js_folder_sort" style="width: 40px;" required ></td>
                                        <td>${l.name}</td>
                                        <td>${l.url}</td>
                                        <td>${l.status}</td>
                                        <td>
                                            <a  id="btn_add" data-toggle="modal" data-target="#myModal" href="${BASE_PATH}/manage/friendlyLink/update.htm?id=${l.id}"
                                                title="修改">
                                                修改
                                            </a>

                                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">



                                                    </div>
                                                </div>
                                            </div>

                                            |
                                            <a href="javascript:void(0);" adminId="${l.id}" title="删除"
                                               class="js_delete_admin">
                                                删除
                                            </a>
                                        </td>
                                    </tr>
                                    </#list>
                                    </tbody>

                                </table>
                            </div>
                            <div>
                            <#if list?size gt 0>
                                <button class="btn btn-info js_update_sort" type="button">
                                    <i class="icon-refresh"></i> 更新排序
                                </button>
                            </#if>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
    </section>
</section>

<script type="text/javascript">
    $(function () {

        $('.js_update_sort').click(function () {
            var folderSort = new Array();
            $('.js_folder_sort').each(function (i, element) {
                var folder = {};
                folder.id = $(element).attr('folderId');
                folder.sort = $(element).val();
                folderSort.push(folder);
            });
            $.post("${BASE_PATH}/manage/friendlyLink/sort.json", {
                        "sortJson": $.toJSON(folderSort)
                    },
                    function (data) {
                        if (data.result) {
                            bootbox.alert("更新成功",
                                    function () {
                                        window.location.reload();
                                    });
                        } else {
                            bootbox.alert(data.msg,
                                    function () {
                                    });
                        }
                    },
                    "json");
        });


        $('.basicForm').ajaxForm({
            dataType: 'json',
            success: function (data) {
                if (data.result) {
                    bootbox.alert("保存成功，将刷新页面", function () {
                        window.location.reload();
                    });
                } else {
                    showErrors($('#basicForm'), data.errors);
                }
            }
        });

        $('.js_delete_admin').click(function () {
            var adminId = $(this).attr('adminId');
            bootbox.dialog({
                message: "是否" + $(this).attr('title') + "该链接",
                title: "提示",
                buttons: {
                    "delete": {
                        label: "删除",
                        className: "btn-success",
                        callback: function () {
                            $.post("${BASE_PATH}/manage/friendlyLink/delete.json", {
                                        "id": adminId
                                    },
                                    function (data) {
                                        if (data.result) {
                                            bootbox.alert("删除成功",
                                                    function () {
                                                        window.location.reload();
                                                    });
                                        } else {
                                            bootbox.alert(data.msg,
                                                    function () {
                                                    });
                                        }
                                    },
                                    "json");
                        }
                    },
                    "cancel": {
                        label: "取消",
                        className: "btn-primary",
                        callback: function () {
                        }
                    }
                }
            });
        });
    });
</script>
<#include "/manage/foot.ftl">