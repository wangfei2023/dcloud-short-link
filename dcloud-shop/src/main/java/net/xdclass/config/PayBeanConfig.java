/**
 * @project dcloud-short-link
 * @description 代码读取私钥
 * @author Administrator
 * @date 2023/7/7 0007 23:51:30
 * @version 1.0
 */

package net.xdclass.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

public class PayBeanConfig {
    /**
     * 加载秘钥
     *
     * @param payConfig
     * @return
     * @throws IOException
     */
    @Autowired
    private WeChatPayConfig payConfig;

    public PrivateKey getPrivateKey() throws IOException {
        InputStream inputStream = new ClassPathResource(payConfig.getPrivateKeyPath()
                .replace("classpath:", "")).getInputStream();

        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");

            PrivateKey finalPrivateKey = kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));

            return finalPrivateKey;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }
}


