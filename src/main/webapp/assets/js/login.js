$(function(){
 	$('.theme-qrcode').click(function(){
 		$('.theme-popover-mask-2').fadeIn(100);
 		$('.theme-popover-2').slideDown(200);
 	});
 	$('.theme-poptit-2 .close').click(function(){
 		$('.theme-popover-mask-2').fadeOut(100);
 		$('.theme-popover-2').slideUp(200);
 	});
 	
	$("#login").on("click",function(e){
		debugger
		var systemID=$("[name='systemid']").val();
		var passwd=$("[name='passwd']").val();
		//alert(school);
		login_check(systemID,passwd);
	});

});

function login_check(loginName, password) {
	$.ajax({
		type : "POST",
		url : baseUrl+"/Auth/appLogin",
		async : true,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify({
			loginName : loginName,
			password : password
		}),
		dataType : "json",
		success : function(response) {
			debugger
			var status = response.status;
			if (status == "fail") {
				var msg = response.msg;
				$('#showLoginError').html('<font color="#FF0000">*登录名或密码错误</font>');
				$("#showLoginError").css('display','block');
			} else {
				var token=response.Accesstoken;
			}
		},
		error : function(response) {
			debugger
			alert("Unknown Sever Error!Please Contact Us!");
		}
	});
}


