//安全沙箱，用来处理容器逻辑
IWeb = {};
IWeb.Const = {};
IWeb.Sandbox = function($) {
    var result = function(coreObj, moduleId,containerObj) {
        this.coreObj = coreObj;
        this.moduleId = moduleId;
        if(containerObj == null){
            this.container = $(document.body);
        }else{
            this.container = containerObj;
        }
        var tid = this.container.attr("id");
        if(tid == null || tid == undefined || tid == "undefined"){
            tid = this.container.get(0).tagName;
        }
        this.instanceId = this.moduleId+"_"+tid;
    };
    result.fn = result.prototype;
    result.fn.listen = function(eventNames, callback, module) {
        this.coreObj.listen(this.instanceId, eventNames, callback, module);
        return this
    };
    result.fn.notify = function(event) {
        event.instanceId = this.instanceId;
        this.coreObj.broadcast(event);
        return this;
    };

    result.fn.showLoading = function() {                //显示加载效果
        this.notify({
            type: IWeb.Const.SHOW_LOADING,
            data: ""
        });
        return this;
    };
    result.fn.hideLoading = function() {                //隐藏加载效果
        this.notify({
            type:  IWeb.Const.HIDE_LOADING,
            data: ""
        });
        return this;
    };
    result.fn.showDialog = function(data) {             //现实弹出框
        this.notify({
            type:  IWeb.Const.SHOW_DIALOG,
            data: data
        });
    };
    result.fn.showConfirm = function(data) {            //现实选择框
        this.notify({
            type:  IWeb.Const.SHOW_CONFIRM,
            data: data
        });
    };
    result.fn.stopEvent = function() {                  //取消事件向下触发
    	try{
	        if(window.event)
	            window.event.returnValue = false;
	        else
	            event.preventDefault();//for firefox
    	}
    	catch(e){}
    };
   
    result.fn.find = function(iname) {                  //查找iname元素
    	if(IWeb.isNotNull(iname)){
    		if(iname.indexOf(".")>-1){
    		}else if(iname.indexOf("#")>-1){
    			
    		}else{
    			iname = "*[iname='"+iname+"']";
    		}
            return this.container.find(iname);
    	}else{
    		return this.container;
    	}
    };
    return result;
}(jQuery);
//核心处理类，处理各模块之间的加载及映射关系
IWeb.Core = function($, library) {
    //默认是否开启ajax请求缓存
    jQuery.ajaxSetup({cache:IWeb.Const.AJAX_CACHE});

    //模块（插件）信息
    var modules = {};
    //监听事件
    var listens = {};

    //触发一个事件
    function triggerEvent(obj) {
        var type = obj.type;
        var data = obj.data;
        var instanceId = obj.instanceId;
        var lisarr = listens[instanceId][type];
        if (lisarr) {
            for (var i = 0, j = lisarr.length; i < j; i++) {
                lisarr[i]["method"](data, type);
            }
        }
    };

    function hashChangeHandler() {
        var hash = location.hash;
        if (hash) {
            //获取网盘id
            var diskinfo = hash.split("|");
            if(diskinfo.length > 1) {
                datamap.diskinfo = diskinfo[1];
            } else {
                datamap.diskinfo = "";
            }

            var key = hash.split("/");
            switch (key[0]) {
                default:
                    break;
            }
        } else {
            triggerEvent({
                type: IWeb.Const.LOC_HASH_INFO,
                data: "#disk"
            });
        }
    }
    return {
        currentModule:function(name){
            if(IWeb.isNotNull(name)){
                this._currentName = name;
            }
            if(!IWeb.isNotNull(this._currentName)){
                this._currentName = IWeb.randomNum(100000000,999999999)+"";
            }
            return this._currentName;
        },
        startApp: function() {                      //启动应用
            /*$(window).hashchange(function() {
                hashChangeHandler();
            });*/
            $(window).resize(function() {
                triggerEvent({
                    type: IWeb.Const.WINDOW_RESIZE,
                    data: location.hash
                })
            });
            hashChangeHandler();
        },
        register: function(name,module) {          //注册一个模块，每个模块都必须定义一个唯一的name，建议使用《应用名称.模块名称.功能名称》
            modules[name] = {
                creator: module,
                instance: null
            }
        },
        start: function(name,containerObj) {                     //开始实例化一个模块
        	if(typeof(modules[name]) == "undefined" || modules[name] == null){
                modules[name] = {};
            }
            if(typeof(modules[name].instance) == "undefined" || modules[name].instance == null){
                modules[name].instance = {};
            }
            var tbox = new IWeb.Sandbox(this,name,containerObj);
            modules[name].instance[tbox.instanceId] = modules[name].creator(tbox);
            modules[name].instance[tbox.instanceId].init();
        },
        stop: function(name) {                      //开始释放一个模块实例
            var module = modules[name];
            if (module.instance) {
                module.instance.destroy();
                module.instance = null
            }
        },
        startAll: function() {                      //实例化所有模块
            for (var m in modules) {
                if (modules.hasOwnProperty(m)) {
                    this.start(m)
                }
            }
        },
        stopAll: function() {                       //开始释放所有实例
            for (var m in modules) {
                if (modules.hasOwnProperty(m)) {
                    this.stop(m)
                }
            }
        },
        listen: function(instanceId, eventNames, callback, module) {
                                                    //注册一个模块中的事件
            if (!$.isArray(eventNames)) {
            	if(typeof(eventNames)=='object'){
            		var arr = [];
            		for(var p in eventNames){  
            		      if(typeof(eventNames[p])=="function"){
            		      }else{  
            		    	  arr.push(eventNames[p]); 
            		      }  
            		  }
            		eventNames = arr;
            	}else{
            		eventNames = [eventNames]
            	}
            }
            if(listens[instanceId]== null){
                listens[instanceId] = {};
            }
            $.each(eventNames, function(i, name) {
                if (listens[instanceId][name]) {
                    listens[instanceId][name].push({
                        method: callback,
                        scope: module
                    })
                } else {
                    listens[instanceId][name] = [{
                        method: callback,
                        scope: module
                    }]
                }
            })
        },
        broadcast: function(event) {                //广播触发一个事件
            triggerEvent(event);
        }
    }
}(jQuery);