package com.roushan.trading.repository;

import com.roushan.trading.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {

    WatchList findByUserId(Long userId);

}
