layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 加载数据表格
     */
    var tableIns = table.render({
        id: 'roleTable'
        // 容器元素的ID属性值
        , elem: '#roleList'
        // 容器的高度 full-差值
        , height: 'full-125'
        // 单元格最小的宽度
        , cellMinWidth: 95
        // 访问数据的URL（后台的数据接口）
        , url: ctx + '/role/list'
        // 开启分页
        , page: true
        // 默认每页显示的数量
        , limit: 10
        // 每页页数的可选项
        , limits: [10, 20, 30, 40, 50]
        // 开启头部工具栏
        , toolbar: '#toolbarDemo'
        // 表头
        , cols: [[
            // field：要求field属性值与返回的数据中对应的属性字段名一致
            // title：设置列的标题
            // sort：是否允许排序（默认：false）
            // fixed：固定列
            {type: 'checkbox', fixed: 'center'}
            , {field: 'id', title: '编号', sort: true, fixed: 'left'}
            , {field: 'roleName', title: '角色名称', align: 'center'}
            , {field: 'roleRemark', title: '角色备注', align: 'center'}
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '修改时间', align: 'center'}
            , {title: '操作', templet: '#roleListBar', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        /**
         * 表格重载 多条件查询
         */
        tableIns.reload({
            // 设置需要传递给后端的参数
            where: { //设定异步数据接口的额外参数，任意设
                // 通过文本框/下拉框的值，设置传递的参数
                roleName: $("[name='roleName']").val() // 用户名称，在user.ftl里面拿
            }
            , page: {
                curr: 1 // 重新从第 1 页开始
            }
        });
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(roles)', function (data) {
        if (data.event == "add") {
            openAddOrUpdateRoleDialog();
        } else if (data.event == "grant") {
            //获取被选中的信息
            var checkStatus = table.checkStatus(data.config.id);
            //console.log(checkStatus);
            //打开授权对话框
            openAddGrantDialog(checkStatus.data);
        }
    });

    /**
     * 行监听事件
     */
    table.on('tool(roles)', function (data) {
        if (data.event == "edit") {
            openAddOrUpdateRoleDialog(data.data.id);
        } else if (data.event == "del") {
            deleteRole(data.data.id);
        }
    });


    /**
     * 打开添加/修改用户的对话框
     */
    function openAddOrUpdateRoleDialog(roleId) {
        var title = "<h3>角色管理 - 添加角色</h3>";
        var url = ctx + "/role/toAddOrUpdateRolePage";

        if (roleId != null && roleId != '') {
            title = "<h3>角色管理 - 更新角色</h3>";
            url += "?roleId=" + roleId;
        }

        layui.layer.open({
            type: 2,
            title: title,
            area: ['500px', '300px'],
            content: url,
            maxmin: true
        });
    }

    /**
     * 删除角色
     * @param roleId
     */
    function deleteRole(roleId) {
        //弹出确认框，询问用户用户是否确认删除
        layer.confirm('确定要删除该记录吗？', {icon: 3, title: "角色管理"}, function (index) {
            //关闭确认框
            layer.close(index);
            //发送ajax删除记录
            $.ajax({
                type: "post",
                url: ctx + "/role/delete",
                data: {
                    roleId: roleId
                },
                success: function (result) {
                    if (result.code == 200) {
                        layer.msg("删除成功", {icon: 6});
                        //刷新表格
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

    /**
     * 打开授权页面
     */
    function openAddGrantDialog(data) {
        if (data.length == 0) {
            layer.msg("请选择要授权的角色", {icon: 5});
            return;
        }
        if (data.length>1){
            layer.msg("暂不支持批量授权", {icon: 5});
            return;
        }

        var url=ctx+"/module/toAddGrantPage?roleId="+data[0].id;
        var title="<h3>角色管理 - 角色授权</h3>";
        layui.layer.open({
            type: 2,
            title: title,
            area: ['600px', '600px'],
            content: url,
            maxmin: true
        });

    }
});
