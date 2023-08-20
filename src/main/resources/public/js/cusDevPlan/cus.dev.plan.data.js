layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载计划项的数据表格
     */
    var tableIns = table.render({
        id: 'cusDevPlanTable'
        // 容器元素的ID属性值
        , elem: '#cusDevPlanList'
        // 容器的高度 full-差值
        , height: 'full-125'
        // 单元格最小的宽度
        , cellMinWidth: 95
        // 访问数据的URL（后台的数据接口）
        , url: ctx + '/cus_dev_plan/list?saleChanceId=' + $("[name='id']").val()
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
            , {field: 'planItem', title: '计划项', align: 'center'}
            , {field: 'planDate', title: '计划时间', align: 'center'}
            , {field: 'exeAffect', title: '执行效果', align: 'center'}
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '修改时间', align: 'center'}
            , {title: '操作', templet: '#cusDevPlanListBar', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)', function (data) {
        if (data.event == "add") {
            openAddOrUpdateCusDevPlanDialog();
        } else if (data.event == "success") {
            //更新营销机会的开发状态
            updateSaleChanceDevResult(2);//success
        } else if (data.event == "failed") {
            updateSaleChanceDevResult(3)//failed
        }
    });

    /**
     * 监听行工具栏
     */
    table.on('tool(cusDevPlans)', function (data) {
        if (data.event == "edit") {//更新计划项（编辑）
            openAddOrUpdateCusDevPlanDialog(data.data.id); //通过id判断是添加还是修改
        }else if (data.event=="del"){//删除计划项
            deleteCusDevPlan(data.data.id);
        }
    });

    /**
     * 打开添加或修改计划项的页面
     */
    function openAddOrUpdateCusDevPlanDialog(id) {
        var title = "计划项管理 - 添加计划项";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name='id']").val();
        //判断id是否为空，空->添加，不为空->修改
        if (id != null && id != '') {//update
            title = "计划项管理 - 更新计划项";
            url+="&id="+id;
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
     * 删除计划项
     */
    function deleteCusDevPlan(id) {
          layer.confirm('您确认要删除该记录吗？',{icon:3,title:'开发项数据删除'},function (index) {
              $.post(ctx+'/cus_dev_plan/delete',{id:id},function (result) {
                  if (result.code==200){
                      layer.msg('删除成功',{icon:6});
                      tableIns.reload();
                  }else{
                      layer.msg(result.msg,{icon:5});
                  }
              });
          });
    }

    /**
     * 更新营销机会的开发状态
     * @param devResult
     */
    function updateSaleChanceDevResult(devResult) {
        //确认框，询问用户
        layer.confirm('您确认执行该操作吗？',{icon:3,title:"营销机会管理"},function (index) {
            //得到需要被更新的营销机会的id
            var sId=$("[name='id']").val();
            $.post(ctx+'/sale_chance/updateSaleChanceDevResult',{id:sId,devResult:devResult},function (result) {
                if (result.code==200){
                    layer.msg('更新成功',{icon:6});
                    layer.closeAll("iframe");
                    parent.location.reload()
                }else {
                    layer.msg(result.msg,{icon:5});
                }
            });
        });
    }
});
