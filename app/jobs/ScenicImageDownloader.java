package jobs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

import models.scenic.Scenic;
import models.scenic.ScenicImage;
import play.jobs.Job;
import play.libs.Codec;
import play.libs.Images;
import utils.Constants;
import utils.FileUtil;

/**
 * 异步下载指定url地址序列的的图片，作为Scenic的图片
 * @author royguo1988@gmail.com
 */
public class ScenicImageDownloader extends Job {

  private String urls;
  private Long scenicId;

  /**
   * @param links 需要下载数据的链接
   * @param dir 保存目录
   * @param scenic 关联的景区实体
   */
  public ScenicImageDownloader(String urls, Long scenicId) {
    this.urls = urls;
    this.scenicId = scenicId;
  }

  @Override
  public void doJob() throws Exception {
    String[] links = urls.split("[\\r|\\n]+");
    Scenic scenic = Scenic.findById(scenicId);
    if (links != null && links.length > 0) {
      // 遍历所有url
      for (int i = 0; i < links.length; i++) {
        String link = links[i];
        String extension = FileUtil.getExtension(link).toUpperCase();
        if (link.length() > 0 && Arrays.binarySearch(Constants.ALLOWED_IMAGES, extension) > -1) {
          link = link.trim();
          // 根据URL获取图片流，并保存到dir目录下
          try {
            String imageName = Codec.UUID() + "." + extension;
            URL url = new URL(link);
            InputStream is = new BufferedInputStream(url.openStream());
            OutputStream out =
                new BufferedOutputStream(new FileOutputStream(Constants.SCENIC_IMAGE_DIR
                    + imageName));
            for (int b; (b = is.read()) != -1;) {
              out.write(b);
            }
            // 生成缩略图
            File image = new File(Constants.SCENIC_IMAGE_DIR + imageName);
            File thumb = new File(Constants.SCENIC_IMAGE_THUMB_DIR + imageName);
            Images.resize(image, thumb, 160, -1);
            new ScenicImage(imageName, scenic).save();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
