package jobs;

import models.scenic.ScenicCityUpdateChain;
import models.scenic.ScenicConsumeUpdateChain;
import models.scenic.ScenicDaysUpdateChain;
import models.scenic.ScenicProvinceUpdateChain;
import models.scenic.ScenicRegionUpdateChain;
import models.scenic.ScenicUpdateHelper;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class SystemInitiation extends Job {

    @Override
    public void doJob() throws Exception {
        super.doJob();
        new ScenicDaysUpdateChain(ScenicUpdateHelper.getInstance());
        new ScenicCityUpdateChain(ScenicUpdateHelper.getInstance());
        new ScenicProvinceUpdateChain(ScenicUpdateHelper.getInstance());
        new ScenicRegionUpdateChain(ScenicUpdateHelper.getInstance());
        new ScenicConsumeUpdateChain(ScenicUpdateHelper.getInstance());
    }
}
