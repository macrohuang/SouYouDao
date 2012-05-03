package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jobs.PlanDayImageDownloader;
import models.plan.Plan;
import models.plan.PlanDay;
import models.plan.PlanDayImage;
import models.plan.PlanTime;
import play.Logger;
import play.libs.Codec;
import utils.Constants;
import utils.FileUtil;
import utils.Secure;

public class Plans extends Application {

  public static void index() {
    List<Plan> myPlans = new ArrayList<Plan>();
    List<Plan> plans = Plan.find("order by id desc").fetch();
    // 我的计划
    if (session.get(Constants.USER_ID_IN_SESSION) != null) {
      long userId = Long.parseLong(session.get(Constants.USER_ID_IN_SESSION));
      myPlans = Plan.find("user.id = ?", userId).fetch();
    }
    render(myPlans, plans);
  }

  /**
   * 创建计划第一步，命名
   * @param planName
   */
  @Secure(login=true)
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
   * 进入计划查看页面
   * @param planId
   */
  public static void detail(long planId) {
    Plan plan = Plan.findById(planId);
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

  /**
   * @param planDayId
   * @param webImg 网络图片
   * @param localImg 本地图片
   * @param pasteImg 直接粘贴图片,Base64编码字符串
   * @param imgSrc 图片源
   */
  public static void savePlanDayImage(long planDayId, String webImg, File localImg,
      String pasteImg, String imgSrc) {
    PlanDay pd = PlanDay.findById(planDayId);
    if ("local".equals(imgSrc)) {// 本地图片
      PlanDayImage pdi = new PlanDayImage();
      File image =
          new File(Constants.PLAN_DAY_IMAGE_PATH + Codec.UUID() + "."
              + FileUtil.getExtension(localImg));
      // TODO renameTo only works on the same File System.
      localImg.renameTo(image);
      pdi.name = image.getName();
      pdi.planDay = pd;
      pdi.save();
    } else if ("web".equals(imgSrc)) {// 网络图片
      Logger.info("web " + webImg);
      PlanDayImageDownloader downloader = new PlanDayImageDownloader(webImg, planDayId);
      downloader.now();
    } else if ("paste".equals(imgSrc)) {    // 直接粘贴,需要对Base64进行解码
      PlanDayImage pdi = new PlanDayImage();
      File image = FileUtil.writeImageFromBase64(Constants.PLAN_DAY_IMAGE_PATH, pasteImg);
      pdi.name = image.getName();
      pdi.planDay = pd;
      pdi.save();
    }
    edit(pd.plan.id);
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
