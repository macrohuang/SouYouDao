$(function(){
	if($("#planNameForm").length > 0){
		$("#planNameForm").validate();
	}
});
//保存计划名称
function savePlanName(_id){
	$.post("/Plans/savePlanName",{planId:_id,planName:$("#planName").val()},function(data){
		$("#planName").addClass("plan-name-saved");
		$("#savePlanNameBtn").attr("disabled","disabled");
	});
}
//重新编辑计划名称
function editPlanName(){
	$("#planName").removeClass("plan-name-saved");
	$("#savePlanNameBtn").removeAttr("disabled");
}
//添加一天计划
function addDayPlan(){
	var html = '<div class="day-plan">' +
				'<div class="day-label">' +
	  				'<div class="btn-group">' +
	  					'<button class="btn btn-info">选择日期</button>' +
	  					'<button class="btn btn-info dropdown-toggle" data-toggle="dropdown">' +
	  						'<span class="caret"></span>' +
	  					'</button>' +
						'<ul class="dropdown-menu">' +
							'<li><a onclick="addDayTime(this)">添加时间段</a></li>' +
							'<li><a href="#">添加图片</a></li>' +
							'<li><a href="#">添加地图</a></li>' +
							'<li><a href="#">删除本日计划</a></li>' +
						'</ul>' +
					'</div>' +
				'</div>'+
				'</div>';
	$("#days-plan-container").append(html);
}
//添加一天内的时间段
function addDayTime(_node){
	var html = '<div class="day-content">' + 
					'<div id="day-plan-maps"></div>' + 
					'<div id="day-plan-images"></div>' + 
					'<div class="day-time-plan">' + 
						'<div class="day-time">08:00 ~ 09:30</div>' + 
						'<div class="day-time-content">TEST</div>' + 
					'</div>' + 
				'</div>';
	$(_node).closest(".day-plan").append(html);
}