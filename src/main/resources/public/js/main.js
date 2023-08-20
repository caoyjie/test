layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();

    $(".login-out").click(function () {
        layer.confirm('确定退出登录吗?', {icon: 3, title:'向您确认'}, function(index){
            // do something
            // …
            layer.close(index);
            //清空cookie
            $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
            $.removeCookie("userName",{domain:"localhost",path:"/crm"});
            $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
            //跳到登录页面（父窗口）
            window.parent.location.href=ctx+"/index";
        });
    });

});