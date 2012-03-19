package jobs;

import models.Admin;
import models.utils.area.City;
import models.utils.area.Province;
import models.utils.area.Region;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * 测试环境初始化系统所需数据
 * @author royguo1988@gmail.com
 */
@OnApplicationStart
public class DataInitial extends Job {

  @Override
  public void doJob() throws Exception {
    if (Play.configuration.get("db").equals("mem")) {
      Admin admin = new Admin("admin", "admin");
      admin.save();

      Province p = new Province();
      p.name = "火星";
      p.save();

      City c = new City();
      c.name = "大海";
      c.province = p;
      c.save();

      Region r = new Region();
      r.name = "巧克力";
      r.city = c;
      r.save();
    }
  }
}
