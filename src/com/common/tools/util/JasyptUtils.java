package com.common.tools.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;


public class JasyptUtils {
	private JasyptUtils() {
	}

	public static String encryptPwd(String password, String value) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(cryptOr(password));
		return encryptor.encrypt(value);
	}

	public static String decryptPwd(String password, String value) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(cryptOr(password));
		return encryptor.decrypt(value);
	}

	public static SimpleStringPBEConfig cryptOr(String password) {
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(password);
		config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		return config;
	}

	

}
