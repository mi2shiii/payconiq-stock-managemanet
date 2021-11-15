package com.payconiq.backend.repository;

import com.payconiq.backend.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock getStockByName(String name);
}
