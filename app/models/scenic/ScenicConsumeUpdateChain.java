package models.scenic;

import java.lang.reflect.Field;

import utils.WebResponse;
import utils.WebResponse.ResponseStatus;

public class ScenicConsumeUpdateChain extends ScenicUpdateChain {

    public ScenicConsumeUpdateChain(ScenicUpdateHelper helper) {
        super(helper);
    }

    @Override
    public void updateField(WebResponse response, Scenic scenic, Field field, String value) {
        if ("consume".equals(field.getName())) {
            Consume consume = scenic.consume2;
            if (consume == null) {
                consume = new Consume();
            }
            consume.total += Integer.parseInt(value);
            consume.count++;
            scenic.consume = consume.total / consume.count;
            consume.save();
            scenic.consume2 = consume;
            scenic.save();
            response.status = ResponseStatus.WEB_RESPONSE_OK;
            response.data = scenic.consume;
        }
    }

}
