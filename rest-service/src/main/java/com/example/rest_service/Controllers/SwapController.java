package com.example.rest_service.Controllers;

import com.example.rest_service.Services.UserAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/swap")
public class SwapController {

    @Autowired
    private UserAssetsService userAssetsService;

    @PostMapping(value = "/show")
    public ResponseEntity<Map<String, String>> swapAssets(@RequestBody Map<String, String> input) {
        try {
            String username = input.get("username");
            String fromCoin = input.get("fromCoin");
            String toCoin = input.get("toCoin");
            BigDecimal amount = new BigDecimal(input.get("amount"));

            Map<String, String> result = userAssetsService.showSwapAssets(username, fromCoin, toCoin, amount);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<String> confirmSwap(@RequestBody Map<String, String> input) {
        try {
            String username = input.get("username");
            String fromCoin = input.get("fromCoin");
            String toCoin = input.get("toCoin");

            boolean success = userAssetsService.confirmSwap(username, fromCoin, toCoin);

            if (success) {
                return new ResponseEntity<>("Swap successful!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Swap failed or rate expired!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Sawfafwawfawfe expired!", HttpStatus.BAD_REQUEST);
        }
    }
}