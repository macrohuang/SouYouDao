package jobs;

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

  }
}
