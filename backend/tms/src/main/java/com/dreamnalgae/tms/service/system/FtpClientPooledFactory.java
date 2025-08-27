package com.dreamnalgae.tms.service.system;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FtpClientPooledFactory implements PooledObjectFactory<FTPClient> {

    @Value("${ftp.host}") String host;
    @Value("${ftp.port:21}") int port;
    @Value("${ftp.username}") String username;
    @Value("${ftp.password}") String password;
    @Value("${ftp.timeouts.connectMillis:3000}") int connectTimeout;
    @Value("${ftp.timeouts.dataMillis:10000}") int dataTimeout;
    @Value("${ftp.keepAliveSec:0}") int keepAliveSec;

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient c = new FTPClient();
        c.setConnectTimeout(connectTimeout);
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
        c.enterLocalPassiveMode();
        c.setFileType(FTP.BINARY_FILE_TYPE);
        // 필요 시: c.setUseEPSVwithIPv4(false);

        return new DefaultPooledObject<>(c);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        FTPClient c = p.getObject();
        if (c.isConnected()) {
            try { c.logout(); } catch (Exception ignore) {}
            try { c.disconnect(); } catch (Exception ignore) {}
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        try {
            return p.getObject().sendNoOp(); // 제어연결 살아있는지 확인
        } catch (IOException e) {
            return false;
        }
    }

    @Override public void activateObject(PooledObject<FTPClient> p) {}
    @Override public void passivateObject(PooledObject<FTPClient> p) {}





    
}
