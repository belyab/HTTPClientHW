package ru.kpfu.itis.baigulova.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientRealisation implements HttpClient {

    public String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        URL urlMain = new URL(methodForUrl(url, params));
        HttpURLConnection connection = (HttpURLConnection) urlMain.openConnection();

        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.get(key));
        }
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(7000);
        connection.setReadTimeout(7000);

        System.out.println(connection.getResponseCode());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())
        )) {
            StringBuilder content = new StringBuilder();
            String input;
            while ((input = reader.readLine()) != null) {
                content.append(input);
            }
            connection.disconnect();
            return content.toString();
        }
    }

    public String post(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        URL postUrl = new URL(url);
        HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
        postConnection.setRequestMethod("POST");
        for(String key: headers.keySet()){
            postConnection.setRequestProperty(key, headers.get(key));
        }
        postConnection.setDoOutput(true);

        String jsonInputString = methodForJson(params);

        try(OutputStream outputStream = postConnection.getOutputStream()){
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input,0,input.length);

        }

        System.out.println(postConnection.getResponseCode());
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(postConnection.getInputStream(),StandardCharsets.UTF_8))) {
            String input;
            while ((input = reader.readLine()) != null) {
                content.append(input.trim());
            }
        }
                return content.toString();
    }
    public String methodForUrl(String url, Map<String,String> params){
        int counterParams = 0;
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");

        for(String key: params.keySet()){
            if (counterParams > 0) {
                urlBuilder.append("&");
            }
            urlBuilder.append(key).append("=").append(params.get(key));
            counterParams +=1;
        }
        return urlBuilder.toString();
    }

    public String methodForJson(Map<String,String> params){
        int counter = 0;
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for(String key: params.keySet()){
            if(counter > 0){
                jsonBuilder.append(",");
            }
            jsonBuilder.append(key).append(":").append(params.get(key));
            counter +=1;
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
