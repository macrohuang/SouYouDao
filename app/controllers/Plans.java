package controllers;

import models.plan.Plan;
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
    render(plan);
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
}
