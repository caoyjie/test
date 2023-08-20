$(function () {
    loadModuleData();
});

var zTreeObj;

/**
 * 加载资源树形数据
 */
function loadModuleData() {
    var setting = {
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onCheck: zTreeOnCheck
        }
    }

    $.ajax({
        type: "get",
        url: ctx + "/module/queryAllModules",
        dataType: "json",
        success: function (data) {
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    });
}

/**
 *
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnCheck(event,treeId,treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    var nodes=zTreeObj.getCheckedNodes(true);
    //console.log(nodes);
    if(nodes.length>0){
        var mIds="mIds=";
        for (var i=0;i<nodes.length;i++){
            if (i<nodes.length-1){
                mIds+=nodes[i].id+"&mIds=";
            }else {
                mIds+=nodes[i].id;
            }
        }
        console.log(mIds);
    }

    //拿隐藏域中的id
    var roleId=$("[name='roleId']").val();

    //发送ajax
    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mIds+"&roleId="+roleId,
        dataType: "json",
        success:function (data) {
            console.log(data);
        }
    });
}