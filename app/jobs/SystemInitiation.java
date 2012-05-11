package jobs;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.ScenicDaysUpdateChain;
import utils.ScenicUpdateHelper;

@OnApplicationStart
public class SystemInitiation extends Job {

    @Override
    public void doJob() throws Exception {
        super.doJob();
        new ScenicDaysUpdateChain(ScenicUpdateHelper.getInstance());
    }
}
