//保存计划名称
function savePlanName(){
	$("#planName").addClass("plan-name-saved");
	$("#savePlanNameBtn").attr("disabled","disabled");
}
//重新编辑计划名称
function editPlanName(){
	$("#planName").removeClass("plan-name-saved");
	$("#savePlanNameBtn").removeAttr("disabled");
}