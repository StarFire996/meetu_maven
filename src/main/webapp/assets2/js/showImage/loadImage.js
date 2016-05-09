function loadImage(id,path){
	var cmd = new Power.command(path+"enterprise/statistics/flowImage","post");
	cmd.setParameter("id", id);
	var conts = cmd.execute("showImage");
	if(conts!="no"){
		$.each(conts,function(i,item){
			if(item.HANDLE_STATE=="10"){
				$("[name='"+item.ITEM_ID+"']").attr("src","/static/js/showImage/images/enterprise/u4.png");
			}else if(item.HANDLE_STATE=="11"){
				$("[name='"+item.ITEM_ID+"']").attr("src","/static/js/showImage/images/enterprise/u2.png");
			}else if(item.HANDLE_STATE=="99"){
				$("[name='"+item.ITEM_ID+"']").attr("src","/static/js/showImage/images/enterprise/u0.png");
			}
		});
	}
}