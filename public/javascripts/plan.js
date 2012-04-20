$(function(){
	if($("#planNameForm").length > 0){
		$("#planNameForm").validate();
	}
	//date-pickers
	$(".datepick").datepicker().on('changeDate', function(ev){
		datepickerCallback(ev,$(this));
	});
	//time-pickers
	$('.timepick').ptTimeSelect();
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
	var d = new Date();
	var date = d.getFullYear() + "-" + d.getMonth() + "-" + d.getDate();
	var html = '<div class="day-plan">' +
				'<div class="day-label">' +
	  				'<div class="btn-group">' +
	  					'<button class="btn btn-info datepick" planDayId="0" data-date-format="yyyy-mm-dd" data-date="'+date+'">选择日期</button>' +
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
				'<div id="day-plan-maps">Maps</div>' + 
				'<div id="day-plan-images">IMAGES</div>' + 
				'</div>';
	$("#days-plan-container").append(html);
	$(".datepick").datepicker().on('changeDate', function(ev){
		datepickerCallback(ev,$(this));
	});
}
//添加一天内的时间段
function addDayTime(_node){
	var html = '<div class="day-content" planDayId="0">' + 
					'<div class="day-time-plan">' + 
						'<div class="day-time">' +
						'<input type="text" planTimeId="0" class="timepick input-small" value="开始时间"/> 到 <input type="text" planTimeId="0" class="timepick input-small" value="结束时间"/>' +
						'</div>' + 
						'<div class="day-time-content">TEST</div>' + 
					'</div>' + 
				'</div>';
	$(_node).closest(".day-plan").append(html);
	// 添加新的HTML元素后重新设置时间选择事件
	$('.timepick').ptTimeSelect({onClose:timepickerCallback});
}
//date-picker 回调函数,_node为选择日期的按钮
function datepickerCallback(ev,_node){
	var d = ev.date;
	var date = d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate();
	var dayId = $(_node).closest(".day-plan").attr("planDayId");
	//显示新日期
	$(_node).html(date);
	//持久化 PlanDay
	$.post("/Plans/savePlanDay",{planDayId:dayId,planId:$("#planId").val(),date:date},function(planDayId){
		$(_node).closest(".day-plan").attr("planDayId",planDayId);
	});
}
//time-picker 回调函数,_time是时间选择框的JQuery对象
function timepickerCallback(_time){
	var planDayId = $(_time).closest(".day-plan").attr("planDayId");
	var time = $(_time).val();
	var timeId = $(_time).attr("planTimeId");
	$.post("/Plans/savePlanTime",
			{planDayId:planDayId,planTimeId:timeId,startTime:startTime,endTime:endTime},
			function(planTimeId){
		$(_time).attr("planTimeId",planTimeId);
	});
	//alert($(_time).closest(".day-plan").attr("planDayId"));
}