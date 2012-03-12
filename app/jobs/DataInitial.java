package jobs;

import models.Scenic;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * 初始化系统所需数据
 * @author royguo1988@gmail.com
 */
@OnApplicationStart
public class DataInitial extends Job {

  @Override
  public void doJob() throws Exception {
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
  }
}
