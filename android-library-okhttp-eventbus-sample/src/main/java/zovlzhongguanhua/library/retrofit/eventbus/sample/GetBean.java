package zovlzhongguanhua.library.retrofit.eventbus.sample;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    {
        "args": {
        "name": "Tom",
                "password": "123456"
    },
        "headers": {
        "Accept-Encoding": "gzip",
                "Connection": "close",
                "Host": "httpbin.org",
                "User-Agent": "okhttp/3.3.1"
    },
        "origin": "183.3.149.194",
            "url": "http://httpbin.org/get?name=Tom&password=123456"
    }*/

public class GetBean implements Serializable {
    @SerializedName("args")
    public ArgsBean args;
    @SerializedName("headers")
    public HeadersBean headers;
    public String origin;
    public String url;

    public static class ArgsBean {
        public String name;
        public String password;
    }

    public static class HeadersBean {
        @SerializedName("Accept-Encoding")
        private String AcceptEncoding;
        private String Connection;
        private String Host;
        @SerializedName("User-Agent")
        private String UserAgent;
    }
}