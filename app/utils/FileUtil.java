package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import play.Play;
import play.libs.Codec;
import sun.misc.BASE64Decoder;

public class FileUtil extends FileUtils {
  /**
   * @param file
   * @return "jpg/png/NULL"
   */
  public static String getExtension(File file) {
    if (file == null) return null;
    return file.getName().replaceAll(".+\\.", "").toLowerCase();
  }

  /**
   * @param fullName
   * @return "jpg/png/NULL"
   */
  public static String getExtension(String fullName) {
    String extension = null;
    if (fullName != null && fullName.length() > 0) {
      int dotIndex = fullName.lastIndexOf(".");
      if (dotIndex > 0) {
        extension = fullName.substring(dotIndex + 1);
      }
    }
    return extension;
  }

  /**
   * 获得系统路径
   * @param dirs "data","scenic","images"
   * @return "/User/root/test/"
   */
  public static String getApplicationPath(String... dirs) {
    String separator = Play.applicationPath.separator;
    StringBuilder builder = new StringBuilder(Play.applicationPath.getAbsolutePath() + separator);
    for (String s : dirs) {
      builder.append(s + separator);
    }
    return builder.toString();
  }

  /**
   * 根据页面直接粘贴的图片所生成的Base64编码生成图片.
   */
  public static File writeImageFromBase64(String path, String base64) {
    String suffix = base64.split(";")[0].replaceAll("[\\s\\S]+\\/+", "");
    String imageCode = base64.split(",")[1];
    File image = new File(path + Codec.UUID() + "." + suffix);
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      // Base64解码
      byte[] b = decoder.decodeBuffer(imageCode);
      for (int i = 0; i < b.length; ++i) {
        if (b[i] < 0) {// 调整异常数据
          b[i] += 256;
        }
      }
      OutputStream out = new FileOutputStream(image);
      out.write(b);
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }

  // public static void main(String[] args) {
  // FileUtil.writeImageFromBase64("",
  // "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAK4AAACcC");
  // }
}
