package controllers;

import java.util.Iterator;
import java.util.List;

import models.utils.area.City;
import models.utils.area.Province;
import models.utils.area.Region;
import play.mvc.Controller;
/**
 * Ajax工具、临时入口等
 * @author royguo1988@gmail.com
 *
 */
public class Utils extends Controller {
  /**
   * 获取所有省市区数据
   */
  public static void province() {
    List<Province> provinces = Province.findAll();
    StringBuilder json = new StringBuilder("[");
    for (Iterator iterator = provinces.iterator(); iterator.hasNext();) {
      Province p = (Province) iterator.next();
      json.append("{\"id\":\"" + p.id + "\",\"name\":\""+ p.name +"\"}");
      if(iterator.hasNext()){
        json.append(",");
      }
    }
    json.append("]");
    renderJSON(json.toString());
  }
  public static void city(Long province_id){
    List<City> cities = City.find("province.id = ?", province_id).fetch();
    StringBuilder json = new StringBuilder("[");
    for (Iterator iterator = cities.iterator(); iterator.hasNext();) {
      City c = (City) iterator.next();
      json.append("{\"id\":\"" + c.id + "\",\"name\":\""+ c.name +"\"}");
      if(iterator.hasNext()){
        json.append(",");
      }
    }
    json.append("]");
    renderJSON(json.toString());
  }
  public static void region(Long city_id){
    List<Region> regions = Region.find("city.id = ?", city_id).fetch();
    StringBuilder json = new StringBuilder("[");
    for (Iterator iterator = regions.iterator(); iterator.hasNext();) {
      Region r = (Region) iterator.next();
      json.append("{\"id\":\"" + r.id + "\",\"name\":\""+ r.name +"\"}");
      if(iterator.hasNext()){
        json.append(",");
      }
    }
    json.append("]");
    renderJSON(json.toString());
  }
}