package models.scenic;

import java.lang.reflect.Field;

import models.utils.area.Province;
import utils.WebResponse;
import utils.WebResponse.ResponseStatus;

public class ScenicProvinceUpdateChain extends ScenicUpdateChain {

    public ScenicProvinceUpdateChain(ScenicUpdateHelper helper) {
        super(helper);
    }

    @Override
    public void updateField(WebResponse response, Scenic scenic, Field field, String value) {
        if ("province".equals(field.getName())) {
            long id = Long.parseLong(value);
            scenic.province = Province.findById(id);
            scenic.save();
            response.status = ResponseStatus.WEB_RESPONSE_OK;
        }
    }

}
