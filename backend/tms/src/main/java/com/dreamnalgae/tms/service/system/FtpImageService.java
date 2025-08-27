package com.dreamnalgae.tms.service.system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.system.ImageData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FtpImageService {

    // @Value("${ftp.host}") private String host;
    // @Value("${ftp.port:21}") private int port;
    // @Value("${ftp.username}") private String username;
    // @Value("${ftp.password}") private String password;

    // @Value("${ftp.basePaths.initial}") private String baseInitial;
    // @Value("${ftp.basePaths.final}") private String baseFinal;

    // public ImageData fetch(String kind, String fileName) throws IOException {
    //     if (!"initial".equals(kind) && !"final".equals(kind)) {
    //         throw new IllegalArgumentException("kind must be initial or final");
    //     }

    //     String base = "initial".equals(kind) ? baseInitial : baseFinal;

    //     String fullPath = base.endsWith("/") ? base + fileName : base + "/" + fileName;

    //     FTPClient ftp = new FTPClient();
    //     try {
    //         ftp.connect(host, port);
    //         if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
    //             throw new IOException("FTP connect failed: " + ftp.getReplyCode());
    //         }
    //         if (!ftp.login(username, password)) {
    //             throw new IOException("FTP login failed");
    //         }
    //         ftp.enterLocalPassiveMode();
    //         ftp.setFileType(FTP.BINARY_FILE_TYPE);

    //         try (InputStream is = ftp.retrieveFileStream(fullPath)) {
    //             if (is == null) throw new FileNotFoundException(fullPath);
    //             byte[] bytes = is.readAllBytes();
    //             if (!ftp.completePendingCommand()) throw new IOException("FTP pending not complete");

    //             ImageData out = new ImageData();
    //             out.setBytes(bytes);
    //             out.setContentType(guess(fileName));
    //             return out;
    //         }
    //     } finally {
    //         if (ftp.isConnected()) {
    //             try { ftp.logout(); } catch (Exception ignore) {}
    //             try { ftp.disconnect(); } catch (Exception ignore) {}
    //         }
    //     }
    // }
    

    private String guess(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }



    private final GenericObjectPool<FTPClient> ftpPool;
    
    @Value("${ftp.basePaths.initial}") String baseInitial;
    @Value("${ftp.basePaths.final}")   String baseFinal;

    // ftp pool 방식 접속 사용
    public ImageData fetch(String kind, String fileName) throws IOException {
        if (!"initial".equals(kind) && !"final".equals(kind)) {
            throw new IllegalArgumentException("kind must be initial or final");
        }
        if (fileName == null || fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new FileNotFoundException("invalid file name");
        }

        String base = "initial".equals(kind) ? baseInitial : baseFinal;
        String fullPath = base.endsWith("/") ? base + fileName : base + "/" + fileName;

        FTPClient ftp = null;
        InputStream is = null;
        try {
            ftp = ftpPool.borrowObject();  // 🔹 풀에서 커넥션 대여
            is = ftp.retrieveFileStream(fullPath);
            if (is == null) throw new FileNotFoundException(fullPath);

            byte[] bytes = is.readAllBytes();
            is.close(); is = null;         // 먼저 스트림 닫고
            if (!ftp.completePendingCommand()) {
                throw new IOException("FTP pending not complete");
            }

            ImageData out = new ImageData();
            out.setBytes(bytes);
            out.setContentType(guess(fileName));
            return out;

        } catch (Exception e) {
            // 사용 중인 커넥션을 풀에서 폐기(invalidate)
            if (ftp != null) {
                try { ftpPool.invalidateObject(ftp); ftp = null; } catch (Exception ignore) {}
            }
            if (e instanceof IOException) throw (IOException) e;
            throw new IOException(e);
        } finally {
            if (is != null) try { is.close(); } catch (Exception ignore) {}
            // 정상 객체는 반납
            if (ftp != null) {
                try { ftpPool.returnObject(ftp); } catch (Exception ignore) {}
            }
        }
    }




}
