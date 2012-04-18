package jobs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import models.Scenic;
import models.image.ScenicImage;
import play.Logger;
import play.jobs.Job;
import play.libs.Codec;
import utils.FileUtil;

/**
 * 异步下载指定url地址序列的的图片，作为Scenic的图片
 * @author royguo1988@gmail.com
 */
public class ScenicImageDownloader extends Job {
  public enum SaveType {
    IMAGES, ROADMAPS
  }

  private String[] links;
  private String dir;
  private Long scenicId;
  private SaveType saveType;

  public ScenicImageDownloader() {
  }

  /**
   * @param links 需要下载数据的链接
   * @param dir 保存目录
   * @param scenic 关联的景区实体
   * @param saveType 保存类型
   */
  public ScenicImageDownloader(String[] links, String dir, Long scenicId, SaveType saveType) {
    this.links = links;
    this.dir = dir;
    this.scenicId = scenicId;
    this.saveType = saveType;
  }

  @Override
  public void doJob() throws Exception {
    Scenic scenic = Scenic.findById(scenicId);
    if (links != null && links.length > 0 && dir != null) {
      // 遍历所有url
      for (int i = 0; i < links.length; i++) {
        String link = links[i];
        String extension = FileUtil.getExtension(link);
        if (link != null && link.length() > 0 && extension != null) {
          link = link.trim();
          // 根据URL获取图片流，并保存到dir目录下
          try {
            Logger.info("Image Downloading : " + link);
            String imageName = Codec.UUID() + "." + extension;
            URL url = new URL(link);
            InputStream is = new BufferedInputStream(url.openStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + imageName));
            for (int b; (b = is.read()) != -1;) {
              out.write(b);
            }
            Logger.info("Image Downloaded : " + link);
            if (saveType.equals(SaveType.IMAGES)) {
              new ScenicImage(imageName, scenic).save();
            } else {
              scenic.roadmapImage = imageName;
              scenic.save();
            }

          } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Image Download Error : " + link);
          }
        }
      }
    }
  }
}
