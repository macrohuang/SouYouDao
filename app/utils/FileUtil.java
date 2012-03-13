package utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

import play.Play;

public class FileUtil extends FileUtils {
  public static String getExtension(File file) {
    if (file == null) return null;
    return file.getName().replaceAll(".+\\.", "").toLowerCase();
  }

  /**
   * 获得系统路径
   * @param dirs
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
