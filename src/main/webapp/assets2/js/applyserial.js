/**
 * 通过存在工程获取编号
 */
applySerialExist = function (){
	
	var addButtons = [ {
			id : 'choose-opercode',
			name : '确定',
			callback : function() {				
			  return getCommonInfo();	
			},
			focus : true
		}, {
			name : '取消'
		}];
		Power.dialog({
			title : "建设项目",
			url : "parallel/business/project/getProjectList",
			button : addButtons,
			top : 0,
			width:350,
			init : function() {
			},
			close : function() {
			}
		});	
};

/**
 * 直接新建工程并提取编号
 */
applySerialadd = function(){
	var addButtons = [ {
		id : 'choose-opercode',
		name : '确定',
		callback : function() {				
		  return getCommonInfo();	
		},
		focus : true
	}, {
		name : '取消'
	}];
	Power.dialog({
		title : "建设项目",
		width:'900px',
		url : "parallel/business/project/add",
		button : addButtons,
		init : function() {
		},
		close : function() {
		}
	});	
};