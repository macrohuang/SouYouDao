package models.scenic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import utils.WebResponse;
import utils.WebResponse.ResponseStatus;

public class ScenicUpdateHelper {
    private static List<ScenicUpdateChain> updateChains = new ArrayList<ScenicUpdateChain>();

    private ScenicUpdateHelper() {

    }

    private static final ScenicUpdateHelper INSTANCE = new ScenicUpdateHelper();

    public static ScenicUpdateHelper getInstance() {
        return INSTANCE;
    }

    public static void addScenicUpdateChain(ScenicUpdateChain chain) {
        updateChains.add(chain);
    }

    /*
     * 这个做法比较NC，但是没有啥好的办法，有几十个字段，如果要每个都写的话，会吐血，而且有大量重复劳动
     */
    public static void updateByField(WebResponse response, Scenic scenic, Field field, String value) {
        for (ScenicUpdateChain chain : updateChains) {
            chain.updateField(response, scenic, field, value);
            if (response.status == ResponseStatus.WEB_RESPONSE_OK) {
                return;
            }
        }
        try {
            if (field.getType().equals(String.class)) {
                field.set(scenic, value);
            } else if (field.getType().equals(Integer.class) || field.getType().getName().endsWith("int")) {
                field.set(scenic, Integer.parseInt(value));
            } else if (field.getType().equals(Float.class) || field.getType().getName().endsWith("float")) {
                field.set(scenic, Float.parseFloat(value));
            }
            response.data = value;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.status = ResponseStatus.WEB_RESPONSE_PARAM_ERROR;
            response.msg = "参数错误！";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response.status = ResponseStatus.WEB_RESPONSE_ERROR;
            response.msg = "系统错误，请重试！";
        }
        scenic.save();
        response.status = ResponseStatus.WEB_RESPONSE_OK;
    }
}
