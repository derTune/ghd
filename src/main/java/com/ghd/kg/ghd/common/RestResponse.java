package com.ghd.kg.ghd.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponse {
    Object data;
    String msg;
    int status;

    public static RestResponse success(Object data) {
        RestResponse response = new RestResponse();
        response.setData(data);
        response.setStatus(RestStatus.SUCCESS);
        return response;
    }

    public static RestResponse failed(int status, String message) {
        RestResponse response = new RestResponse();
        response.setMsg(message);
        response.setStatus(status);
        return response;
    }
}
