package utils;

public class WebResponse {
    public enum ResponseStatus {
        WEB_RESPONSE_OK(200), WEB_RESPONSE_PARAM_ERROR(300), WEB_RESPONSE_PER(403), WEB_RESPONSE_ERROR(
                500);
        int code;

        private ResponseStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public ResponseStatus status;
    public Object data;
    public String msg;

    public String toJsonString() {
        return String.format("{\"status\":\"%d\",\"data\":\"%s\",\"msg\":\"%s\"}",
                status.getCode(), data, msg);
    }
}
