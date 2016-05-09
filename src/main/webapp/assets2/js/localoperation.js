
/**
 * 加载本地操作
 * @param groupid为按钮组的id
 * @param loadbutton 本地操作父节点id
 */
loadbutton = function(loadbutton,groupid){
	if(groupid != null){
    	var cmd = new Power.command("parallel/sysmanagement/localgroup", "post");
    	cmd.setParameter("id", groupid);
    	var ret = cmd.execute("loadbutton");
    	if (!ret.error && ret.state == 1) {
    		var Opinionlist = ret.buttonlist;
    		var load="";
    		$.each(Opinionlist,function(i,item){ 
    			load="<div style='float: right; margin-left: 10px'><span class='btn btn-rounded btn-primary' onclick="+item.ACTION_SCRIPT+"><i class='"+item.ACTION_CLASS+"'></i>"+"&nbsp;"+item.ACTION_ID+"</span></div>";
    			$("#"+loadbutton).append(load);
    		});
    	};
	};
};

/**
 * 根据flow_id和flow_node_id加载本地操作
 * @param  divid 本地操作父节点id
 * @param  flow_id
 * @param  flow_node_id
 */
loadbuttonbyflow = function(divid,biz_id,root){
	if(biz_id.length!=0){
		var cmd = new Power.command("enterprise/business/todotaskcontroller", "post");
		cmd.setParameter("biz_id", biz_id);
		var ret = cmd.execute("loadButtons");
		if (!ret.error && ret.state == 1) {
			var Opinionlist = ret.buttonlist;
			var load="";
			$.each(Opinionlist,function(i,item){
				if(item.ACTION_ID=="导出"){
					var cmd = new Power.command("enterprise/business/todotaskcontroller","post");
					cmd.setParameter("biz_id",biz_id);
					var ret = cmd.execute("checkPrintOut");
					if(ret.status=="ok"){
						load="<a href='"+root+"/enterprise/business/todotaskcontroller/printOut?biz_id="+biz_id+"'><div style='float: right; margin-left: 10px'><span class='btn btn-rounded btn-primary'><i class='"+item.ACTION_CLASS+"'></i>"+"&nbsp;"+item.ACTION_ID+"</span></div></a>";
					}else{
						load="<div style='float: right; margin-left: 10px'><span class='btn btn-rounded btn-primary' onclick="+item.ACTION_SCRIPT+"><i class='"+item.ACTION_CLASS+"'></i>"+"&nbsp;"+item.ACTION_ID+"</span></div>";
					}
					
				}else{
					load="<div style='float: right; margin-left: 10px'><span class='btn btn-rounded btn-primary' onclick="+item.ACTION_SCRIPT+"><i class='"+item.ACTION_CLASS+"'></i>"+"&nbsp;"+item.ACTION_ID+"</span></div>";
				}
				
				$("#"+divid).append(load);
			});
		};
	}
};

loadbuttonfirst = function(divid,biz_id){
	if(biz_id.length!=0){
		var cmd = new Power.command("/enterprise/business/parallelreceive", "post");
		cmd.setParameter("biz_id", biz_id);
		var ret = cmd.execute("loadButtons");
		if (!ret.error && ret.state == 1) {
			var Opinionlist = ret.buttonlist;
			var load="";
			$.each(Opinionlist,function(i,item){
				load="<div style='float: right; margin-left: 10px'><span class='btn btn-rounded btn-primary' onclick="+item.ACTION_SCRIPT+"><i class='"+item.ACTION_CLASS+"'></i>"+"&nbsp;"+item.ACTION_ID+"</span></div>";
				$("#"+divid).append(load);
			});
		};
	}
};
//收费预核算
function addfare(){
	var addButtons = [{
		id:'pub-form-add',
	    name: '确定',
	    callback: function () {
	    	if(submit()){
				return true;
			}
			return false;
	    },
	    focus: true
	}, {
	    name: '关闭'
	}];
	Power.dialog({
		id:"addreport",
		title:"添加收费实例",
		width:"940px",
		height:"430px",
		url : "parallel/faremanagement/fareitem/addfare?biz_id="+biz_id,
		button:addButtons,
	    top:0,
		init:function(){
		
		},
		close:function(){
		}
	});
}
//收费汇总
function faredetial(){
	var addButtons = [{
		
	    name: '关闭'
	}];
	Power.dialog({
		id:"addreport",
		title:"收费汇总",
		width:"920px",
		height:"430px",
		url : "parallel/faremanagement/fareitem/summaryFare?biz_id="+biz_id,
		button:addButtons,
	    top:0,
		init:function(){
		
		},
		close:function(){
		}
	});
}
