package com.roushan.trading.service;

import com.roushan.trading.model.Asset;
import com.roushan.trading.model.Coin;
import com.roushan.trading.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity);

    Asset getAssetById(Long assetId) throws Exception;

    Asset getAssetByUserIdAndId(Long userId, Long assetId);

    List<Asset> getUsersAssets(Long userId);

    Asset updateAsset(Long assetId, double quantity) throws Exception;


    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId) throws Exception;
}
