package ru.kpfu.itis.baigulova.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

public interface HttpClient {
    //    https://postman-echo.com/get
    String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException;

    //    https://postman-echo.com/post
    String post(String url, Map<String, String> headers, Map<String, String> params) throws IOException;

}
