package jobs;

import models.Admin;
import models.Scenic;
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
    String db = Play.configuration.getProperty("db");
    if (db.equals("fs") || db.equals("mem")) {
      Scenic.deleteAll();
      // 添加测试景点数据
      Scenic s1 = new Scenic();
      Scenic s2 = new Scenic();
      Scenic s3 = new Scenic();
      s1.name = "HUANG SHAN FENGJINGQU";
      s1.save();
      s2.name = "YUN TAISHAN FENGJINGQU";
      s2.save();
      s3.name = "YI HE YUAN";
      s3.save();

      // 添加管理员账号
      Admin.deleteAll();
      new Admin("gkk", "gkk").save();
      new Admin("xjx", "xjx").save();
    }
  }
}
