package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.model.Asset;
import com.roushan.trading.model.User;
import com.roushan.trading.service.AssetService;
import com.roushan.trading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable("assetId") Long assetId) throws Exception {
        Asset asset = this.assetService.getAssetById(assetId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("coinId") String coinId) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Asset asset = this.assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetsForUser(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Asset> assetList = this.assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(assetList);
    }

}
