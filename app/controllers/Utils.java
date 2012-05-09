package controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	public static void locationMap() {
		List<Province> provinces = Province.findAll();
		Map<String, Object> lMap = new HashMap<String, Object>();
		for (Province province : provinces) {
			Map<String, Object> pMap = new HashMap<String, Object>();
			List<City> cities = City.find("province.id = ?", province.id).fetch();
			Map<String, Object> cMap = new HashMap<String, Object>();
			for (City city : cities) {
				Map<String, Object> ccMap = new HashMap<String, Object>();
				ccMap.put("name", city.name);
				List<Region> regions = Region.find("city.id = ?", city.id).fetch();
				Map<String, Object> rMap = new HashMap<String, Object>();
				for (Region region : regions) {
					rMap.put("" + region.id, region.name);
				}
				ccMap.put("region", rMap);
				cMap.put("" + city.id, ccMap);
			}
			pMap.put("cities", cMap);
			pMap.put("name", province.name);
			lMap.put("" + province.id, pMap);
		}
		renderJSON(lMap);
	}
}