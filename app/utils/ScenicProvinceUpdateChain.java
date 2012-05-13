package utils;

import java.lang.reflect.Field;

import models.scenic.Scenic;
import models.utils.area.Province;
import po.WebResponse;
import po.WebResponse.ResponseStatus;

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
