package jobs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import models.plan.PlanDay;
import models.plan.PlanDayImage;
import play.Logger;
import play.jobs.Job;
import play.libs.Codec;
import utils.Constants;
import utils.FileUtil;

/**
 * 异步下载指定url地址序列的的图片，作为PlanDay的图片
 * @author royguo1988@gmail.com
 */
public class PlanDayImageDownloader extends Job {
  private String link;
  private Long planDayId;

  /**
   * @param links 需要下载数据的链接
   * @param dir 保存目录
   * @param scenic 关联的景区实体
   * @param saveType 保存类型
   */
  public PlanDayImageDownloader(String link, Long planDayId) {
    this.link = link;
    this.planDayId = planDayId;
  }

  @Override
  public void doJob() throws Exception {
    PlanDayImage pdi = new PlanDayImage();
    pdi.planDay = PlanDay.findById(this.planDayId);
    String dir = Constants.PLAN_DAY_IMAGE_PATH;
    if (link != null) {
      String extension = FileUtil.getExtension(link);
      if (link != null && link.length() > 0 && extension != null) {
        link = link.trim();
        // 根据URL获取图片流，并保存到dir目录下
        try {
          Logger.info("PlanDay Image Downloading : " + link);
          String imageName = Codec.UUID() + "." + extension;
          URL url = new URL(link);
          InputStream is = new BufferedInputStream(url.openStream());
          OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + imageName));
          for (int b; (b = is.read()) != -1;) {
            out.write(b);
          }
          pdi.name = imageName;
          pdi.save();
          Logger.info("PlanDay Image Downloaded : " + link);
        } catch (Exception e) {
          e.printStackTrace();
          Logger.error("PlanDay Image Download Error : " + link);
        }
      }
    }
  }
}
