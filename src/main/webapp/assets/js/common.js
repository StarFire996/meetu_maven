function clicked(url)
{
	window.location.href=url;
}


/*ajax func*/

function ajaxSyncGet(url, successfunc, errorfunc){
	if(url.indexOf("?")>=0){
		url=url + "&";
	}else{
		url=url + "?"; 
	}
	url=url +'timestamp=' +new Date().getTime();
	$.ajax({
		type : "GET",
		url : url,
		async : false,
		contentType : "application/json; charset=utf-8",
		success : successfunc,
		error : errorfunc
	});
}

function ajaxAsyncGet(url, successfunc, errorfunc){
	if(url.indexOf("?")>=0){
		url=url + "&";
	}else{
		url=url + "?"; 
	}
	url=url + "timestamp="+new Date().getTime();
	$.ajax({
		type : "GET",
		url : url,
		async : true,
		contentType : "application/json; charset=utf-8",
		success : successfunc,
		error : errorfunc
	});
}

function ajaxSyncPost(url, jsondata, successfunc, errorfunc){
	$.ajax({
		type : "POST",
		url : url,
		data : jsondata,
		async : false,
		contentType : "application/json; charset=utf-8",
		success : successfunc,
		error : errorfunc
	});
}

function ajaxAsyncPost(url, jsondata, successfunc, errorfunc){
	$.ajax({
		type : "POST",
		url : url,
		data : jsondata,
		async : true,
		contentType : "application/json; charset=utf-8",
		success : successfunc,
		error : errorfunc
	});
}