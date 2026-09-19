package com.octodev.relay;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class APIKeyStorage {

    private static final String KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "relay_api_key";

    private static final String PREFS_NAME = "relay_secure_storage";
    private static final String PREF_API_KEY = "api_key";
    private static final String PREF_IV = "api_key_iv";

    private final SharedPreferences preferences;

    public APIKeyStorage(Context context) {
        preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        );
    }

    public void save(String apiKey) throws Exception {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is empty");
        }

        SecretKey key = getOrCreateKey();

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted = cipher.doFinal(
            apiKey.getBytes(StandardCharsets.UTF_8)
        );

        byte[] iv = cipher.getIV();

        preferences.edit()
            .putString(
                PREF_API_KEY,
                Base64.encodeToString(encrypted, Base64.NO_WRAP)
            )
            .putString(
                PREF_IV,
                Base64.encodeToString(iv, Base64.NO_WRAP)
            )
            .apply();
    }

    public String load() throws Exception {
        String encryptedString = preferences.getString(PREF_API_KEY, null);
        String ivString = preferences.getString(PREF_IV, null);

        if (encryptedString == null || ivString == null) {
            return null;
        }

        SecretKey key = getOrCreateKey();

        byte[] encrypted = Base64.decode(
            encryptedString,
            Base64.NO_WRAP
        );

        byte[] iv = Base64.decode(
            ivString,
            Base64.NO_WRAP
        );

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            spec
        );

        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public void clear() {
        preferences.edit()
            .remove(PREF_API_KEY)
            .remove(PREF_IV)
            .apply();

        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE);
            keyStore.load(null);

            if (keyStore.containsAlias(KEY_ALIAS)) {
                keyStore.deleteEntry(KEY_ALIAS);
            }

        } catch (Exception ignored) {

        }
    }

    private SecretKey getOrCreateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE);
        keyStore.load(null);

        if (keyStore.containsAlias(KEY_ALIAS)) {
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            return entry.getSecretKey();
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE
        );

        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
            KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE
            )
            .setRandomizedEncryptionRequired(true)
            .build();

        keyGenerator.init(spec);

        return keyGenerator.generateKey();
    }
}
