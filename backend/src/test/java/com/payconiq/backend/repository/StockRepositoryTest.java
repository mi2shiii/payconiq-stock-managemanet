package com.payconiq.backend.repository;

import com.payconiq.backend.domain.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;

@ActiveProfiles("test")
@DataJpaTest
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testGetStockByName_returnsStockDetails() {
        Stock savedStock = testEntityManager.persistFlushFind(new Stock("Apple", 10.0));

        Stock stock = stockRepository.getStockByName("Apple");

        then(stock.getId()).isNotNull();
        then(stock.getName()).isEqualTo(savedStock.getName());
    }
}
