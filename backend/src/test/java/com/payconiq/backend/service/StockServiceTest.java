package com.payconiq.backend.service;


import com.payconiq.backend.domain.Stock;
import com.payconiq.backend.exception.EntityIsLockedException;
import com.payconiq.backend.exception.EntityNotFoundException;
import com.payconiq.backend.repository.StockRepository;
import lombok.SneakyThrows;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = NONE)
public class StockServiceTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Value("${stock.lockTimeInSeconds}")
    private String stockLockTimeInSeconds;

    @AfterEach
    public void deleteAll() {
        stockRepository.deleteAll();
    }

    @Test
    void findById_forSavedStock_returnsStock() {
        Stock savedStock = stockRepository.save(new Stock("Apple", 10.0));

        Stock retrievedStock = stockService.findById(savedStock.getId());

        then(retrievedStock.getId()).isNotNull();
        then(retrievedStock.getName()).isEqualTo("Apple");
        then(retrievedStock.getCurrentPrice()).isEqualTo(10.0);
    }

    @Test
    void findById_whenMissingStock_notFoundExceptionThrown() {
        Long id = 1234L;

        Throwable throwable = catchThrowable(() -> stockService.findById(id));

        BDDAssertions.then(throwable).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void findAll_returnsStockList() {

        stockRepository.save(new Stock("Apple", 10.0));
        stockRepository.save(new Stock("Amazon", 5.0));
        stockRepository.save(new Stock("Microsoft", 2.0));

        List<Stock> stockList = stockService.findAll();

        then(stockList.size()).isEqualTo(3);
    }

    @Test
    public void createStock_returnsStock() {
        Stock stock = new Stock("Apple", 10.10);

        Stock stockCreated = stockService.create(stock);

        then(stockCreated.getId()).isNotNull();
        then(stockCreated.getCreatedAt()).isNotNull();
        then(stockCreated.getLastUpdate()).isNotNull();
        then(stockCreated.getName()).isEqualTo("Apple");
        then(stockCreated.getCurrentPrice()).isEqualTo(10.10);
    }

    @SneakyThrows
    @Test
    public void updateStock_whenStockIsNotLocked_ReturnsStock() {
        Stock savedStock = stockRepository.save(new Stock("Apple", 10.0));
        Long stockToBeUpdatedId = savedStock.getId();
        Stock stockToBeUpdated = new Stock("Apple", 11.0);

        TimeUnit.SECONDS.sleep(Long.parseLong(stockLockTimeInSeconds) + 1);
        Stock updatedStock = stockService.update(stockToBeUpdatedId, stockToBeUpdated);

        then(updatedStock.getCurrentPrice()).isEqualTo(11.0);
    }

    @SneakyThrows
    @Test
    public void updateStock_whenStockIsLocked_EntityIsLockedException() {

        Stock savedStock = stockRepository.save(new Stock("Apple", 10.0));
        Long stockToBeUpdatedId = savedStock.getId();
        Stock stockToBeUpdated = new Stock("Apple", 11.0);

        Throwable throwable = catchThrowable(() -> stockService.update(stockToBeUpdatedId, stockToBeUpdated));

        BDDAssertions.then(throwable).isInstanceOf(EntityIsLockedException.class);
    }

    @SneakyThrows
    @Test
    public void deleteStock_whenStockIsNotLocked_deletesStock() {
        Stock savedStock = stockRepository.save(new Stock("Apple", 10.0));
        Long stockToBeDeletedId = savedStock.getId();

        TimeUnit.SECONDS.sleep(Long.parseLong(stockLockTimeInSeconds) + 1);
        boolean isDeleted = stockService.delete(stockToBeDeletedId);

        then(isDeleted).isEqualTo(Boolean.TRUE);
    }

    @SneakyThrows
    @Test
    public void deleteStock_whenStockIsLocked_EntityIsLockedException() {
        Stock savedStock = stockRepository.save(new Stock("Apple", 10.0));
        Long stockToBeDeletedId = savedStock.getId();

        Throwable throwable = catchThrowable(() -> stockService.delete(stockToBeDeletedId));

        BDDAssertions.then(throwable).isInstanceOf(EntityIsLockedException.class);
    }
}
