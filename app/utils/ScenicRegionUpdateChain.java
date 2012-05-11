package utils;

import java.lang.reflect.Field;

import models.scenic.Scenic;
import models.utils.area.Region;
import po.WebResponse;
import po.WebResponse.ResponseStatus;

public class ScenicRegionUpdateChain extends ScenicUpdateChain {

    public ScenicRegionUpdateChain(ScenicUpdateHelper helper) {
        super(helper);
    }

    @Override
    public void updateField(WebResponse response, Scenic scenic, Field field, String value) {
        if ("region".equals(field.getName())) {
            long id = Long.parseLong(value);
            scenic.region = Region.findById(id);
            scenic.save();
            response.status = ResponseStatus.WEB_RESPONSE_OK;
        }
    }

}
