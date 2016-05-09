Utilities = {
		version: "1.0",
		type: "spring"
};
/**
 * 树组件
 * @param settings
 * {
 * 		clickId:"",
 *		id:"",
 *		selectType:"radio"||"checkbox",
 *		title:"",
 *		classify:"DEPT"||"USER",
 *		depts:"",部门id
 *		regCode:"",区划id
 *		defultVal：“”默认选中
 * }
 */
Utilities.orgTreeSelect = function(settings){
	var _containerId = "";
	if (typeof settings == "string") {
		_containerId = settings;
	} else {
		if (settings == null || settings.id == null) {
			alert("orgTreeSelect中至少包含id参数，格式为：{id:'regionList'}");
			return
		} else {
			_containerId = settings.id;
		}
	}
	var defaultSettings = {
		title: "选择",
		isChange: false,
		selectType: "checkbox",
		classify: "DEPT",
		singleType: false,
		depts: "",
		regCode:"",
		defultVal: "",
		init: "",
		close: ""
	};
	settings = $.extend({},
			defaultSettings, settings || {});
	var _clickId = _containerId;
	if (settings.clickId) {
		_clickId = settings.clickId;
	}
	$("#" + _clickId).unbind("click").bind("click",
		function () {
			var _dfval = settings.defultVal;
			if ($("#" + _containerId).attr("code")) {
				if (_dfval.length > 0) {
					_dfval = _dfval + "," + $("#" + _containerId).attr("code");
				} else {
					_dfval = _dfval + $("#" + _containerId).attr("code");
				}
			}
			var url="";
			if(settings.headPath){
				url=Power.rootPath+settings.headPath;
			}
			url ="utilities/tree/orgTree?classify=" + settings.classify + "&chkType=" + settings.selectType + "&orgCode=" + settings.depts +"&regCode="+settings.regCode+ "&selData=" + _dfval;
			var addButtons = [{
				id: _containerId,
				name: "选择",
				callback: function () {
					return chooseOrgan(_containerId, settings.isChange);
				},
				focus: true
			},
			{
				name: "清空",
				callback: function () {
					Power.dialog.confirm("确实要清空所选信息? ",
							function () {
								$("#" + _containerId).val("");
								$("#" + _containerId).attr("code", "");
								cancelAllNodes();
						});
						return false;
				}
			},
			{
				name: "关闭"
			}];
			Power.dialog({
				title: settings.title,
				url: url,
				button: addButtons,
				top: 0,
				width: 350,
				height: 200,
				init: function () {
					if (settings.init) {
						eval(settings.init)
					}
					$(".organizetreeSerch").hide();
					if(settings.classify=="DEPT"){
						$(".organizetreeSerch").show();
					}
					initTree();
				},
				close: function () {
					if (settings.close) {
						eval(settings.close)
					}
				}
			})
		})
};
//switch组件
/*Utilities.loadSwitch = function (C, A, B) {
    var D = {};
    D.onText = "是";
    D.offText = "否";
    D.onColor = "info";
    D.offColor = "warning";
    D.size = "small";
    D.state = B;
    if (A == 0) {
        $("#" + C).bootstrapSwitch(D, "")
    } else {
        if (A == 1) {
            $("[name=" + C + "]").bootstrapSwitch(D, "")
        } else {
            if (A == 2) {
                $(C).bootstrapSwitch(D, "")
            }
        }
    }
};
Utilities.loadSetSwitch = function (B, C, G, H, F, E, D, A) {
    var I = {};
    I.onText = C;
    I.offText = G;
    I.onColor = H;
    I.offColor = F;
    I.size = E;
    I.state = D;
    if (A == 0) {
        $("#" + B).bootstrapSwitch(I, "")
    } else {
        if (A == 1) {
            $("[name=" + B + "]").bootstrapSwitch(I, "")
        } else {
            if (A == 2) {
                $(B).bootstrapSwitch(I, "")
            }
        }
    }
};*/
Utilities.loadCheck = function (B) {
    var D = "icheckbox_square-blue";
    if (B.boxclass != null) {
        D = B.boxclass
    }
    var C = "iradio_square-blue";
    if (B.radioclass != null) {
        C = B.radioclass
    }
    var G = "20%";
    if (B.increasearea != null) {
        G = B.increasearea
    }
    $(B.selector).iCheck({
        checkboxClass: D,
        radioClass: C,
        increaseArea: G
    });
    if (B.ifChecked != null) {
        $(B.selector).on("ifChecked", B.ifChecked)
    }
    if (B.ifUnchecked != null) {
        $(B.selector).on("ifUnchecked", B.ifUnchecked)
    }
    if (B.radioName != null && B.radioIndex != null) {
        var A = parseInt(B.radioIndex) - 1;
        $("[name='" + B.radioName + "']").eq(A).iCheck("check").attr("checked", true);
    }
    if (B.checkboxName != null && B.checkboxIndexArray != null) {
        var F = B.checkboxIndexArray.split(",");
        for (var A = 0; A < F.length; A++) {
            var E = parseInt(F[A]) - 1;
            $("[name='" + B.checkboxName + "']").eq(E).iCheck("check").attr("checked", true);
        }
    }
};