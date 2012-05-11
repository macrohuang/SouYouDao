package utils;

import java.lang.reflect.Field;

import models.scenic.Scenic;
import models.utils.area.City;
import po.WebResponse;
import po.WebResponse.ResponseStatus;

public class ScenicCityUpdateChain extends ScenicUpdateChain {

    public ScenicCityUpdateChain(ScenicUpdateHelper helper) {
        super(helper);
    }

    @Override
    public void updateField(WebResponse response, Scenic scenic, Field field, String value) {
        if ("city".equals(field.getName())) {
            long id = Long.parseLong(value);
            scenic.city = City.findById(id);
            scenic.save();
            response.status = ResponseStatus.WEB_RESPONSE_OK;
        }
    }

}
