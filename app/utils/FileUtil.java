package utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

import play.Play;

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
}
