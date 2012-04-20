package controllers;

import java.util.List;

import models.plan.Plan;
import models.plan.PlanDay;
import models.plan.PlanTime;
import play.mvc.Controller;

public class Plans extends Controller {

  public static void index() {
    render();
  }

  /**
   * 创建计划第一步，命名
   * @param planName
   */
  public static void save(String planName) {
    Plan plan = new Plan();
    plan.name = planName;
    plan.save();
    edit(plan.id);
  }

  /**
   * 进入编辑页面
   * @param planId
   */
  public static void edit(long planId) {
    Plan plan = Plan.findById(planId);
    if (plan == null) {
      index();
    }
    List<PlanDay> planDays = PlanDay.find("plan.id = ? order by date asc", planId).fetch();
    render(plan, planDays);
  }

  /**
   * 更改计划名称
   * @param planId
   * @param planName
   */
  public static void savePlanName(long planId, String planName) {
    Plan plan = Plan.findById(planId);
    plan.name = planName;
    plan.save();
  }

  public static long savePlanDay(long planDayId, long planId, String date) {
    PlanDay pd = null;
    if (planDayId == 0) {
      pd = new PlanDay();
      pd.plan = Plan.findById(planId);
    } else {
      pd = PlanDay.findById(planDayId);
    }
    pd.date = date;
    pd.save();
    return pd.id;
  }

  public static long savePlanTime(long planDayId, long planTimeId, String startTime, String endTime) {
    PlanTime pt = null;
    if (planTimeId == 0) {
      pt = new PlanTime();
      pt.planDay = PlanDay.findById(planDayId);
    } else {
      pt = PlanTime.findById(planTimeId);
    }
    pt.startTime = startTime;
    pt.endTime = endTime;
    pt.save();
    return pt.id;
  }
}
