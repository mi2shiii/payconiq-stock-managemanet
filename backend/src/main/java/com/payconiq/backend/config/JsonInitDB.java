package com.payconiq.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.backend.domain.Stock;
import com.payconiq.backend.repository.StockRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.util.List;

@Profile("!test")
@Configuration
@Log
public class JsonInitDB {

    @Value("classpath:stocks-db-init.json")
    private Resource dbJsonFile;

    @Bean
    public CommandLineRunner initDB(StockRepository stockRepository) {
        log.info("DB is initializing from " + dbJsonFile.getFilename());
        return args -> stockRepository.saveAll(new ObjectMapper().readValue(dbJsonFile.getFile(),
                new TypeReference<List<Stock>>() {
                }));
    }
}
