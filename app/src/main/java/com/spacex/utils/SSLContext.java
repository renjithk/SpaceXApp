package com.spacex.utils;

import android.content.Context;

import com.spacex.R;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Utility class to provide secured scoket factory for network operations. It uses certificate pinning
 * mechanism to facilitate the functionality
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class SSLContext {
    public static SSLSocketFactory instance(Context context) throws Exception {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");

            //copy server certificate id as keystore file id
            //name public key
            try (InputStream in = context.getResources().openRawResource(R.raw.keystore)) {
                //keystore password can be user password or can be encrypted
                trusted.load(in, "S*pa0#1523".toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(trusted);

            javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Failed to load SSL certificate");
        }
    }
}

