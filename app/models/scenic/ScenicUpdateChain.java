package models.scenic;

import java.lang.reflect.Field;

import utils.WebResponse;

public abstract class ScenicUpdateChain {
    public ScenicUpdateChain(ScenicUpdateHelper helper) {
        helper.addScenicUpdateChain(this);
    }

    public abstract void updateField(WebResponse response, Scenic scenic, Field field, String value);
}
