package zovlzhongguanhua.library.retrofit.rxjava.sample;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    {
        "args": {},
        "data": "",
            "files": {},
        "form": {
        "name": "Tom",
                "password": "123456"
    },
        "headers": {
        "Accept-Encoding": "gzip",
                "Connection": "close",
                "Content-Length": "24",
                "Content-Type": "application/x-www-form-urlencoded",
                "Host": "httpbin.org",
                "User-Agent": "okhttp/3.3.1"
    },
        "json": null,
            "origin": "183.3.149.194",
            "url": "http://httpbin.org/post"
    }*/

public class PostBean implements Serializable {
    @SerializedName("args")
    public ArgsBean args;
    public String data;
    @SerializedName("files")
    public FilesBean files;
    @SerializedName("form")
    public FormBean form;
    @SerializedName("headers")
    public HeadersBean headers;
    public Object json;
    public String origin;
    public String url;

    public static class ArgsBean {}

    public static class FilesBean {}

    public static class FormBean {
        private String name;
        private String password;
    }

    public static class HeadersBean {
        @SerializedName("Accept-Encoding")
        private String AcceptEncoding;
        private String Connection;
        @SerializedName("Content-Length")
        private String ContentLength;
        @SerializedName("Content-Type")
        private String ContentType;
        private String Host;
        @SerializedName("User-Agent")
        private String UserAgent;
    }
}