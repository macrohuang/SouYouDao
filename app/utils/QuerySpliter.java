package utils;

public class QuerySpliter {
  /**
   * @param keywords
   * @return not null
   */
  public static String[] splite(String keywords) {
    if (keywords != null) {
      return keywords.split("\\s+");
    }
    return new String[] {};
  }

  public static void main(String[] args) {
    System.out.println(QuerySpliter.splite("11 2222 33333    444     66   ")[4]);
  }
}
