package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                    .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                    .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                    .build())
            .build();

    public static void main(String[] args) throws IOException {

        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType()); // отправка запроса
        CloseableHttpResponse response = httpClient.execute(request); // вывод полученных заголовков

        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        List<ToJson> jsonList = mapper.readValue(body, new TypeReference<List<ToJson>>() {
                }).stream()
                .filter(value -> (value.getUpvotes() > 0))
                .collect(Collectors.toList());

        // Вывод списка объектов ToJson
        jsonList.forEach(System.out::println);
    }
}
