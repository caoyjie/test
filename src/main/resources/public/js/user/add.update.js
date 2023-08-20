layui.use(['form', 'layer', 'formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    /**
     * 表单submit监听
     */
    form.on('submit(addOrUpdateUser)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = top.layer.msg("数据提交中,请稍后...", {
            icon: 16, // 图标
            time: false, // 不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        //得到所有表单元素的值
        var formData = data.field;
        //请求的地址
        var url = ctx + "/user/add";

        //判断隐藏域中id是否为空，不为空则是更新
        if ($("[name='id']").val()) {
            //更新
            var url = ctx + "/user/update";
        }

        //判断计划项id是否为空 为空则是更新，
        if ($('[name="id"]').val()) {
            url = ctx + "/user/update";
        }

        $.post(url, data.field, function (result) {
            // 判断操作是否执行成功 200=成功
            if (result.code == 200) {
                // 成功
                // 提示成功
                top.layer.msg("操作成功！", {icon: 6});
                // 关闭加载层
                top.layer.close(index);
                // 关闭弹出层
                layer.closeAll("iframe");
                // 刷新父窗口，重新加载数据
                parent.location.reload();
            } else {
                // 失败
                layer.msg(result.msg, {icon: 5});
            }
        });
        // 阻止表单提交
        return false;
    });

    /**
     *  加载下拉框
     * 1.配置远程搜索, 请求头, 请求参数, 请求类型等
     *
     * formSelects.config(ID, Options, isJson);
     *
     * @param ID        xm-select的值
     * @param Options   配置项
     * @param isJson    是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    var userId = $("[name='id']").val();
    formSelects.config("selectId", {
        type: "post",                              //请求方式: post, get, put, delete...
        searchUrl: ctx + "/role/queryAllRoles?userId=" + userId,     //搜索地址, 默认使用xm-select-search的值, 此参数优先级高
        keyName: 'roleName',                      //自定义返回数据中name的key, 默认 name
        keyVal: 'id',                             //自定义返回数据中value的key, 默认 value
    }, true);
});