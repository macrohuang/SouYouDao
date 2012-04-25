package controllers;

import java.io.File;
import java.util.List;

import jobs.PlanDayImageDownloader;
import models.plan.Plan;
import models.plan.PlanDay;
import models.plan.PlanDayImage;
import models.plan.PlanTime;
import play.Logger;
import play.libs.Codec;
import play.mvc.Controller;
import utils.Constants;
import utils.FileUtil;

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

  public static void deletePlanDay(long planDayId) {
    PlanDay pd = PlanDay.findById(planDayId);
    pd.delete();
  }

  public static long savePlanTime(long planDayId, long planTimeId, String time, boolean isStartTime) {
    if (planDayId == 0) {
      throw new RuntimeException("未选择天计划日期!");
    }
    PlanTime pt = null;
    if (planTimeId == 0) {
      pt = new PlanTime();
      pt.planDay = PlanDay.findById(planDayId);
    } else {
      pt = PlanTime.findById(planTimeId);
    }
    if (isStartTime) {
      pt.startTime = time;
    } else {
      pt.endTime = time;
    }
    pt.save();
    return pt.id;
  }

  public static void deletePlanTime(long planTimeId) {
    PlanTime pt = PlanTime.findById(planTimeId);
    pt.delete();
  }

  public static void savePlanTimeContent(long planTimeId, String content) {
    PlanTime pt = PlanTime.findById(planTimeId);
    pt.content = content;
    pt.save();
  }

  public static void savePlanDayImage(long planDayId, String webImg, File localImg, String imgSrc) {
    if ("local".equals(imgSrc)) {// 本地图片
      PlanDay pd = PlanDay.findById(planDayId);
      PlanDayImage pdi = new PlanDayImage();
      File image =
          new File(Constants.PLAN_DAY_IMAGE_PATH + Codec.UUID() + "."
              + FileUtil.getExtension(localImg));
      // TODO renameTo only works on the same File System.
      localImg.renameTo(image);
      pdi.name = image.getName();
      pdi.planDay = pd;
      pdi.save();
    } else {// 网络图片
      Logger.info("web " + webImg);
      PlanDayImageDownloader downloader = new PlanDayImageDownloader(webImg, planDayId);
      downloader.now();
    }
    edit(planDayId);
  }

  public static void deletePlanDayImg(long imageId) {
    PlanDayImage pdi = PlanDayImage.findById(imageId);
    File image = new File(Constants.PLAN_DAY_IMAGE_PATH + pdi.name);
    if (image.exists()) {
      image.delete();
    }
    pdi.delete();
  }
}
