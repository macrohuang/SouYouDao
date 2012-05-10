package po;

public class WebResponse {
    public enum ResponseStatus {
        WEB_RESPONSE_OK, WEB_RESPONSE_PARAM_ERROR, WEB_RESPONSE_PER, WEB_RESPONSE_ERROR;
    }

    public ResponseStatus status;
    public Object data;
    public String msg;

    public String toJsonString() {
        return String.format("{\"status\":%d,\"data\":%s,\"msg\":%s}", status.ordinal(), data, msg);
    }
}
