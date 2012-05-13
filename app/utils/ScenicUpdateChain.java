package utils;

import java.lang.reflect.Field;

import models.scenic.Scenic;
import po.WebResponse;

public abstract class ScenicUpdateChain {
    public ScenicUpdateChain(ScenicUpdateHelper helper) {
        helper.addScenicUpdateChain(this);
    }

    public abstract void updateField(WebResponse response, Scenic scenic, Field field, String value);
}
