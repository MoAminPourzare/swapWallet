package com.example.rest_service.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.rest_service.Utils.Defines.CoinbaseUrl;
import static com.example.rest_service.Utils.Defines.CryptoPriceUrl;

public class CryptoAPI {

    public static BigDecimal getCoinPrice(String cryptoSymbol) throws Exception {
        String urlString = CryptoPriceUrl + cryptoSymbol + CoinbaseUrl;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();
        JSONObject jsonObject = new JSONObject(content.toString());
        return BigDecimal.valueOf(jsonObject.getJSONObject("RAW").getDouble("PRICE"));
    }
}
