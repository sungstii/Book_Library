package server.book_library.config.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig simpleConfig = new SimpleStringPBEConfig();
        simpleConfig.setPassword(password);
        simpleConfig.setAlgorithm("PBEWithMD5AndDES");
        simpleConfig.setKeyObtentionIterations("1000");
        simpleConfig.setPoolSize("1");
        simpleConfig.setProviderName("SunJCE");
        simpleConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        simpleConfig.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        simpleConfig.setStringOutputType("base64");
        encryptor.setConfig(simpleConfig);

        return encryptor;
    }
}
