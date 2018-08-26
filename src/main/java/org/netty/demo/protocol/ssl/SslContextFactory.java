package org.netty.demo.protocol.ssl;

import io.netty.util.internal.SystemPropertyUtil;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by XiuYin.Cui on 2018/8/26.
 */
public class SslContextFactory {

    private static final String PROTOCOL = "TLS";
    private static SSLContext SERVER_CONTEXT;
    private static SSLContext CLIENT_CONTEXT;

    private static String CLIENT_KEY_STORE = "E:\\javassl2\\sslclientkeys";
    private static String CLIENT_TRUST_KEY_STORE = "E:\\javassl2\\sslclienttrust";
    private static String CLIENT_KEY_STORE_PASSWORD = "123456";
    private static String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";

    private static String SERVER_KEY_STORE = "E:\\javassl2\\sslserverkeys";
    private static String SERVER_TRUST_KEY_STORE = "E:\\javassl2\\sslservertrust";
    private static String SERVER_KEY_STORE_PASSWORD = "123456";
    private static String SERVER_TRUST_KEY_STORE_PASSWORD = "123456";

    static {
        String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        try {
            /**
             * 初始化服务端 SSLContext
             */
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(SERVER_KEY_STORE), SERVER_KEY_STORE_PASSWORD.toCharArray());
            KeyStore keyStoreTrust = KeyStore.getInstance("JKS");
            keyStoreTrust.load(new FileInputStream(SERVER_TRUST_KEY_STORE), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

            //Set up key manager factory to use our key store
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, SERVER_KEY_STORE_PASSWORD.toCharArray());
            trustManagerFactory.init(keyStoreTrust);

            // Initialize the SSLContext to work with our key managers
            SERVER_CONTEXT = SSLContext.getInstance(PROTOCOL);
            SERVER_CONTEXT.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            /**
             * 初始化客户端 SSLContext
             */
            KeyStore keyStoreClient = KeyStore.getInstance("JKS");
            keyStoreClient.load(new FileInputStream(CLIENT_KEY_STORE), CLIENT_KEY_STORE_PASSWORD.toCharArray());
            KeyStore keyStoreTrustClient = KeyStore.getInstance("JKS");
            keyStoreTrustClient.load(new FileInputStream(CLIENT_TRUST_KEY_STORE), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

            // Set up key manager factory to use our key store
            KeyManagerFactory keyManagerFactoryClient = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory trustManagerFactoryClient = TrustManagerFactory.getInstance("SunX509");
            keyManagerFactoryClient.init(keyStoreClient, CLIENT_KEY_STORE_PASSWORD.toCharArray());
            trustManagerFactoryClient.init(keyStoreTrustClient);

            // Initialize the SSLContext to work with our key managers
            CLIENT_CONTEXT = SSLContext.getInstance(PROTOCOL);
            CLIENT_CONTEXT.init(keyManagerFactoryClient.getKeyManagers(), trustManagerFactoryClient.getTrustManagers(), null);

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
