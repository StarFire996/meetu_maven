function setForm(flow_id,flow_node_id,form_id){
	var cmd = new Power.command("parallel/sysmanagement/processManagement","post");
	cmd.setParameter("flow_id", flow_id);
	cmd.setParameter("flow_node_id", flow_node_id);
	cmd.setParameter("form_id", form_id);
	var froms = cmd.execute("getFormConts");
	
	$.each(froms,function(i,item){
		if(item.readonly=="1"){
			//$("#"+item.id).attr("readonly","readonly");
			$("#"+item.id).attr("disabled","disabled");
			$("#"+item.id).addClass("cfIsReadonly");
			
		}else if(item.mandatory=="1"){
			$("#"+item.id).parent().append("<font style='color:red'>*</font>");
			$("#"+item.id).addClass("cfIsRequired");
			$("#"+item.id).css("width","88%");
		}else if(item.hidden=="1"){
			$("#"+item.id).addClass("cfHidden");
//			var td = $("#"+item.id).parent();
//			var index = td.parent().find("td").index(td);
//			td.parent().find("td").eq(index-1).text("");
			$("#"+item.id).parent().prev().text("");
		}else if(item.initialize=="1"){
			
		}
	});
	
}
function setData(biz_id,form_id,flow_id){
	var cmd = new Power.command("enterprise/sysmanagement/formMapping","post");
	cmd.setParameter("biz_id", biz_id);
	cmd.setParameter("form_id", form_id);
	cmd.setParameter("flow_id", flow_id);
	var dataArray = cmd.execute("getFormData");
	if(dataArray!="kong"){
		$.each(dataArray,function(i,item){
			$("#"+item.field).val(item.data);
		});
	}
	
}