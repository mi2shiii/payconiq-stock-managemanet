package com.payconiq.backend.service;

import com.payconiq.backend.base.BaseServiceImpl;
import com.payconiq.backend.domain.Stock;
import com.payconiq.backend.exception.EntityIsLockedException;
import com.payconiq.backend.exception.EntityNotFoundException;
import com.payconiq.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
public class StockServiceImpl extends BaseServiceImpl<Stock, Long> implements StockService {

    private final StockRepository repository;
    @Value("${stock.lockTimeInSeconds}")
    private String stockLockTimeInSeconds;

    public StockServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public StockRepository getRepository() {
        return repository;
    }

    @Override
    public Stock create(Stock entity) {
        return super.create(entity);
    }

    @Override
    public Stock update(Long id, Stock dataEntity) throws EntityNotFoundException {
        Stock entityToBeUpdated = findById(id);
        entityMapper.mapIgnoreNullValues(dataEntity, entityToBeUpdated);
        applyExtraValidation(entityToBeUpdated);
        return getRepository().save(entityToBeUpdated);
    }

    @Override
    public boolean delete(Long id) throws EntityNotFoundException {
        Stock entityToBeDeleted = findById(id);
        applyExtraValidation(entityToBeDeleted);
        getRepository().delete(entityToBeDeleted);
        return Boolean.TRUE;
    }

    protected void applyExtraValidation(Stock entity) {
        checkLockStatus(entity);
    }

    private void checkLockStatus(Stock entity) {
        long spentTime = Duration.between(entity.getLastUpdate(), LocalDateTime.now()).toSeconds();
        long lockTime = Long.parseLong(stockLockTimeInSeconds);
        if (spentTime <= lockTime)
            throw new EntityIsLockedException("You can not delete or update the stock below " + stockLockTimeInSeconds
                    + " seconds! Try again in "
                    + (lockTime - spentTime) + " seconds");
    }
}
