package com.example.rest_service.Services;

import com.example.rest_service.Entities.User;
import com.example.rest_service.Utils.CryptoByUSD;
import com.example.rest_service.Entities.UserAssets;
import com.example.rest_service.Repositories.UserAssetsRepo;
import com.example.rest_service.Repositories.UserRepo;
import com.example.rest_service.Utils.CryptoAPI;
import com.example.rest_service.Utils.SwapRequestData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAssetsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserAssetsRepo userAssetsRepo;
    private final Map<String, SwapRequestData> swapRequests = new HashMap<>();

    public synchronized ResponseEntity<String> createUserAssets(String username, BigDecimal usdt, BigDecimal btc, BigDecimal eth, BigDecimal doge, BigDecimal xrp) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found with username: " + username);
        }
        UserAssets userAssets = new UserAssets();
        userAssets.setUser(user);
        userAssets.setUsername(user.getUsername());
        userAssets.setUsdt(usdt);
        userAssets.setBtc(btc);
        userAssets.setEth(eth);
        userAssets.setDoge(doge);
        userAssets.setXrp(xrp);

        userAssetsRepo.save(userAssets);
        return ResponseEntity.ok().body("User created successfully");
    }

    public Map<String, String> getUserAssets(String username) throws Exception {
        UserAssets userAssets = userAssetsRepo.findByUsername(username);
        List<String> assetsKeyList = List.of("usdt", "btc", "eth", "doge", "xrp");

        Map<String, String> userAssetByUSDMap = new HashMap<>();
        for (String assetKey : assetsKeyList) {
            BigDecimal coinValue = getUserCoinBalance(userAssets, assetKey);
            BigDecimal price = CryptoAPI.getCoinPrice(assetKey.toUpperCase());

            CryptoByUSD userAssetByUSD =  new CryptoByUSD(price, coinValue);
            userAssetByUSDMap.put(assetKey, userAssetByUSD.getResult());
        }
        return userAssetByUSDMap;
    }

    public synchronized Map<String, String> showSwapAssets(String username, String fromCoin, String toCoin, BigDecimal fromAmount) throws Exception {
        BigDecimal fromCoinPrice = CryptoAPI.getCoinPrice(fromCoin.toUpperCase());
        BigDecimal toCoinPrice = CryptoAPI.getCoinPrice(toCoin.toUpperCase());

        BigDecimal fromCoinValueInUSD = fromCoinPrice.multiply(fromAmount);
        BigDecimal toAmount = fromCoinValueInUSD.divide(toCoinPrice, 10, BigDecimal.ROUND_HALF_UP);

        if (userAssetsRepo.findByUsername(username) == null) {
            throw new Exception("User not found with username: " + username);
        }

        String key = username + fromCoin + toCoin;
        swapRequests.put(key, new SwapRequestData(fromCoin, toCoin, fromAmount, toAmount, LocalDateTime.now()));

        return Map.of("fromCoin", fromCoin, "toCoin", toCoin, "fromAmount", fromAmount.toString(), "toAmount", toAmount.toString());
    }

    @Transactional
    public synchronized boolean confirmSwap(String username, String fromCoin, String toCoin) throws Exception {
        String key = username + fromCoin + toCoin;
        SwapRequestData targetSwapRequest = swapRequests.get(key);
        if (targetSwapRequest != null) {
            if (isRateExpired(targetSwapRequest.getTimestamp())) {
                swapRequests.remove(key);
                return false;
            }
            UserAssets userAssets = userAssetsRepo.findByUsername(username);
            BigDecimal userFromAmount = getUserCoinBalance(userAssets, fromCoin);

            if (userFromAmount.compareTo(targetSwapRequest.getFromAmount()) < 0) {
                throw new Exception("User not have enough balance");
            }

            updateUserCoinBalance(userAssets, fromCoin, targetSwapRequest.getFromAmount().negate());
            updateUserCoinBalance(userAssets, toCoin, targetSwapRequest.getToAmount());

            swapRequests.remove(key);
            userAssetsRepo.save(userAssets);

            return true;
        } else {
            return false;
        }
    }

    private boolean isRateExpired(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        return timestamp.plusMinutes(1).isBefore(now);
    }

    private BigDecimal getUserCoinBalance(UserAssets userAssets, String coin) throws ReflectiveOperationException {
        String getterName = "get" + coin.substring(0, 1).toUpperCase() + coin.substring(1);
        Method getterMethod = UserAssets.class.getMethod(getterName);
        return (BigDecimal) getterMethod.invoke(userAssets);
    }

    private void updateUserCoinBalance(UserAssets userAssets, String coin, BigDecimal amount) throws ReflectiveOperationException {
        BigDecimal currentAmount = getUserCoinBalance(userAssets, coin);
        BigDecimal newAmount = currentAmount.add(amount);
        String setterName = "set" + coin.substring(0, 1).toUpperCase() + coin.substring(1);
        Method setterMethod = UserAssets.class.getMethod(setterName, BigDecimal.class);
        setterMethod.invoke(userAssets, newAmount);
    }
}