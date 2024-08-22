package com.example.rest_service.Controllers;

import com.example.rest_service.Services.UserAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/user_assets")
public class UserAssetsController {

    @Autowired
    private UserAssetsService userAssetsService;

    @PostMapping("/create")
    public ResponseEntity<String> createUserAssets(@RequestBody Map<String, String> input) {
        String username = input.get("username");
        BigDecimal usdt = new BigDecimal(input.get("usdt"));
        BigDecimal btc = new BigDecimal(input.get("btc"));
        BigDecimal eth = new BigDecimal(input.get("eth"));
        BigDecimal doge = new BigDecimal(input.get("doge"));
        BigDecimal xrp = new BigDecimal(input.get("xrp"));

        return userAssetsService.createUserAssets(username, usdt, btc, eth, doge, xrp);
    }

    @PostMapping(value = "/show_assets")
    public ResponseEntity<Map<String, String>> showUserAssets(@RequestBody Map<String, String> input) throws Exception {
        String username = input.get("username");
        Map<String, String> userAssets = userAssetsService.getUserAssets(username);
        return ResponseEntity.ok((Map<String, String>)userAssets);
    }
}