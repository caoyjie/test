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
    form.on('submit(addOrUpdateRole)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = top.layer.msg("数据提交中,请稍后...", {
            icon: 16, // 图标
            time: false, // 不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        //得到所有表单元素的值
        var formData = data.field;
        //请求的地址
        var url = ctx + "/role/add";

        //判断是否是更新操作（判断隐藏域中有没有角色id）
        var roleId = $("[name='id']").val();
        if (roleId != null && roleId != '') {
            url = ctx + "/role/update";
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
});