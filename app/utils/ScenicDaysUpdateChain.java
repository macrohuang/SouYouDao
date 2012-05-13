package utils;

import java.lang.reflect.Field;

import models.scenic.Period;
import models.scenic.Scenic;
import po.WebResponse;
import po.WebResponse.ResponseStatus;

public class ScenicDaysUpdateChain extends ScenicUpdateChain {

    public ScenicDaysUpdateChain(ScenicUpdateHelper helper) {
        super(helper);
    }

    @Override
    public void updateField(WebResponse response, Scenic scenic, Field field, String value) {
        if ("days".equals(field.getName())) {
            Period period = scenic.period;
            if (period == null) {
                period = new Period();
            }
            period.total += Integer.parseInt(value);
            period.count++;
            scenic.days = period.total / period.count;
            period.save();
            scenic.period = period;
            scenic.save();
            response.status = ResponseStatus.WEB_RESPONSE_OK;
            response.data = scenic.days;
        }
    }

}
