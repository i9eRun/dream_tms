package com.dreamnalgae.tms.service.system;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SingleFtpConnectionManager {
    private final Object lock = new Object();
    private FTPClient client;

    @Value("${ftp.host}") String host;
    @Value("${ftp.port:21}") int port;
    @Value("${ftp.username}") String username;
    @Value("${ftp.password}") String password;
    @Value("${ftp.timeouts.connectMillis:3000}") int connectTimeout;
    @Value("${ftp.timeouts.dataMillis:10000}") int dataTimeout;
    @Value("${ftp.keepAliveSec:0}") int keepAliveSec;

    @PostConstruct
    public void close() {
        if (client != null && client.isConnected()) {
            try { client.logout(); } catch (Exception ignore) {}
            try { client.disconnect(); } catch (Exception ignore) {}
        }
    }

    private void connectAndLogin() throws IOException {
        FTPClient c = new FTPClient();
        c.setConnectTimeout((connectTimeout));
        c.setDataTimeout(dataTimeout);
        if (keepAliveSec > 0) {
            c.setControlKeepAliveTimeout(keepAliveSec);
            c.setControlKeepAliveReplyTimeout(connectTimeout);
        }

        c.connect(host, port);
        if (!c.login(username, password)) {
            try { c.disconnect(); } catch (Exception ignore) {}
            throw new IOException("FTP login failed");
        }
        c.enterLocalPassiveMode();               // 방화벽/NAT 환경에서 안정적
        c.setFileType(FTP.BINARY_FILE_TYPE);

        this.client = c;
    }

    // 매 요청 전에 유효성 점검 및 필요시 재접속
    public FTPClient borrow() throws IOException {
        synchronized (lock) {
            if (client == null || !client.isConnected()) {
                connectAndLogin();
            } else {
                // 제어연결이 살아있는지 NOOP으로 확인
                try {
                    if (!client.sendNoOp()) {
                        reconnect();
                    }
                } catch (SocketException e) {
                    reconnect();
                }
            }
            return client;
        }
    }


    private void reconnect() throws IOException {
        try { client.logout(); } catch (Exception ignore) {}
        try { client.disconnect(); } catch (Exception ignore) {}
        connectAndLogin();
    }


    public void giveBack() {
        // 단일 연결은 반납 동작 없음(동시성 제어는 호출부에서 synchronized로 처리)
    }










}
