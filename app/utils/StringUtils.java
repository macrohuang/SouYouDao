package utils;

public class StringUtils {
  /**
   * 截取字符串，过滤html字符
   * @param text
   * @param length
   */
  public static String subWithoutHtml(String text,int length){
    if(text == null){
      return "";
    }
    text = text.replaceAll("<\\s*[A-Za-z]+\\s*>|<\\/\\s*[A-Za-z]+\\s*>|&nbsp;", "");
    if(text.length() < length){
      return text;
    }else{
      return text.substring(0, length);
    }
  }
  public static void main(String[] args) {
    System.out.println(StringUtils.subWithoutHtml("<div >你好啊asdsad</div></p><p>", 4));
  }
}
