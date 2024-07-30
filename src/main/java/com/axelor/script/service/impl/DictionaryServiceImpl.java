package com.axelor.script.service.impl;

import com.axelor.script.service.DictionaryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DictionaryServiceImpl implements DictionaryService {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String TRANSLATE_API_URL = "https://translate.googleapis.com/translate_a/single";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getTranslation(String from, String to, String request) {
        try {
            String url = buildUrl(from, to, request);
            HttpURLConnection connection = setupConnection(url);

            String response = getResponse(connection);
            String translatedText = parseResponse(response);

            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String buildUrl(String from, String to, String request) throws Exception {
        return TRANSLATE_API_URL + "?" +
                "client=gtx&" +
                "sl=" + from + "&" +
                "tl=" + to + "&" +
                "dt=t&q=" + URLEncoder.encode(request, "UTF-8");
    }

    private HttpURLConnection setupConnection(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return connection;
    }

    private String getResponse(HttpURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    private String parseResponse(String response) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(response);
        if (jsonNode.isArray()) {
            return jsonNode.get(0).get(0).get(0).asText();
        } else {
            return jsonNode.asText();
        }
    }
}
