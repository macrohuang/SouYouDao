package utils;

import java.lang.reflect.Field;

import models.scenic.Scenic;
import models.utils.area.City;
import models.utils.area.Province;
import models.utils.area.Region;

public class ScenicUpdateHelper {
  /*
   * 这个做法比较NC，但是没有啥好的办法，有几十个字段，如果要每个都写的话，会吐血，而且有大量重复劳动
   */
  public static void updateByField(Scenic scenic, Field field, String value) throws IllegalArgumentException, IllegalAccessException {
    if (field.getClass().getName().equalsIgnoreCase("string")) {
      field.set(scenic, value);
    } else if (field.getClass().getName().equalsIgnoreCase("int")) {
      field.set(scenic, Integer.parseInt(value));
    } else {
      long id = Long.parseLong(value);
      if ("province".equals(field.getName())) {
        scenic.province = Province.findById(id);
      }
      if ("city".equals(field.getName())) {
        scenic.city = City.findById(id);
      }
      if ("region".equals(field.getName())) {
        scenic.region = Region.findById(id);
      }
    }
    scenic.save();
  }
}
