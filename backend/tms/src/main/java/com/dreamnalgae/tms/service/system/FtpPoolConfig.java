package com.dreamnalgae.tms.service.system;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FtpPoolConfig {

    @Value("${ftp.pool.maxTotal:10}")   int maxTotal;
    @Value("${ftp.pool.maxIdle:5}")     int maxIdle;
    @Value("${ftp.pool.minIdle:1}")     int minIdle;
    @Value("${ftp.pool.testOnBorrow:true}") boolean testOnBorrow;
    @Value("${ftp.pool.testWhileIdle:true}") boolean testWhileIdle;

    private final FtpClientPooledFactory factory;

    @Bean
    public GenericObjectPool<FTPClient> ftpClientPool() {
        GenericObjectPoolConfig<FTPClient> cfg = new GenericObjectPoolConfig<>();
        cfg.setMaxTotal(maxTotal);
        cfg.setMaxIdle(maxIdle);
        cfg.setMinIdle(minIdle);
        cfg.setTestOnBorrow(testOnBorrow);
        cfg.setTestWhileIdle(testWhileIdle);
        // 유휴 검사 주기/최대 검사 수 등 세부 튜닝 가능
        return new GenericObjectPool<>(factory, cfg);
    }
    
}
