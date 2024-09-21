package com.roushan.trading.service;

import com.roushan.trading.model.Asset;
import com.roushan.trading.model.Coin;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
       Asset asset = new Asset();
       asset.setUser(user);
       asset.setCoin(coin);
       asset.setQuantity(quantity);
       asset.setBuyPrice(coin.getCurrentPrice());
       asset = this.assetRepository.save(asset);
       return  asset;
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        Optional<Asset> optionalAsset = this.assetRepository.findById(assetId);
        if(optionalAsset.isPresent()) {
            return optionalAsset.get();
        }
        throw new Exception("Asset not found");
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        List<Asset> assetList = this.assetRepository.findByUserId(userId);
        return assetList;
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        Asset oldAsset = getAssetById(assetId);
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());
        oldAsset = this.assetRepository.save(oldAsset);
        return oldAsset;
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        Asset asset = this.assetRepository.findByUserIdAndCoinId(userId, coinId);
        return asset;
    }

    @Override
    public void deleteAsset(Long assetId) throws Exception {
        Asset asset = this.getAssetById(assetId);
        this.assetRepository.delete(asset);
    }

}
