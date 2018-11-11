package com.common.tools.util;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.common.tools.cons.ConstantExp;
import com.common.tools.ex.ToolsException;

public class EncryptUtils {

	private static final String MD5WITH_RSA = "MD5withRSA";
	private static final String RSA = "RSA";
	private static final String AES = "AES";
	private static final String PBEWITHMD5AND_DES = "PBEWITHMD5andDES";
	private static Logger logger = Logger.getLogger(EncryptUtils.class);

	private EncryptUtils() {
	}

	private static KeyPair initRsaKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
		keyPairGenerator.initialize(512);
		return keyPairGenerator.generateKeyPair();
	}

	private static SecretKey initAesKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
		keyGenerator.init(128);// size
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] keyBytes = secretKey.getEncoded();
		return new SecretKeySpec(keyBytes, AES);
	}

	private static SecretKey initPbeKey(String password) throws ToolsException {
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBEWITHMD5AND_DES);
			return secretKeyFactory.generateSecret(pbeKeySpec);
		} catch (GeneralSecurityException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	private static PBEParameterSpec initPbeSpec() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] salt = secureRandom.generateSeed(8);
		return new PBEParameterSpec(salt, 100);
	}

	public static String aesEnc(String src) throws ToolsException {
		try {
			SecretKey generateKey = initAesKey();

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, generateKey);
			byte[] resultBytes = cipher.doFinal(src.getBytes());

			return Hex.encodeHexString(resultBytes);
		} catch (GeneralSecurityException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static String aesDec(String src) throws ToolsException {
		try {
			SecretKey generateKey = initAesKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, generateKey);
			byte[] result = Hex.decodeHex(src.toCharArray());
			return new String(cipher.doFinal(result));
		} catch (GeneralSecurityException | DecoderException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static String pbeEnc(String src, String password) throws ToolsException {
		try {
			PBEParameterSpec pbeParameterSpec = initPbeSpec();
			SecretKey generateKey = initPbeKey(password);
			Cipher cipher = Cipher.getInstance(PBEWITHMD5AND_DES);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey, pbeParameterSpec);
			byte[] resultBytes = cipher.doFinal(src.getBytes());
			return Hex.encodeHexString(resultBytes);
		} catch (GeneralSecurityException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public String pbeDec(String src, String password) throws ToolsException {
		try {
			PBEParameterSpec pbeParameterSpec = initPbeSpec();
			SecretKey generateKey = initPbeKey(password);
			Cipher cipher = Cipher.getInstance(PBEWITHMD5AND_DES);
			cipher.init(Cipher.DECRYPT_MODE, generateKey, pbeParameterSpec);
			byte[] result = Hex.decodeHex(src.toCharArray());
			return new String(cipher.doFinal(result));
		} catch (GeneralSecurityException | DecoderException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static String rsaPriEnc(String src) throws ToolsException {
		try {
			// 初始化密钥
			KeyPair keyPair = initRsaKeyPair();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

			// 私钥加密 公钥解密
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] resultBytes = cipher.doFinal(src.getBytes());

			return Hex.encodeHexString(resultBytes);
		} catch (GeneralSecurityException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static String rsaPubEnc(String src) throws ToolsException {
		try {
			// 初始化密钥
			KeyPair keyPair = initRsaKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();

			// 私钥解密 公钥加密
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] resultBytes = cipher.doFinal(src.getBytes());

			return Hex.encodeHexString(resultBytes);
		} catch (GeneralSecurityException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static String rsaPubDec(String src) throws ToolsException {
		try {
			KeyPair keyPair = initRsaKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			// 私钥加密 公钥解密
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] resultBytes = cipher.doFinal(Hex.decodeHex(src.toCharArray()));

			return new String(resultBytes);
		} catch (GeneralSecurityException | DecoderException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static String rsaPriDec(String src) throws ToolsException {
		try {
			// 初始化密钥
			KeyPair keyPair = initRsaKeyPair();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			// 私钥解密 公钥加密
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] resultBytes = cipher.doFinal(Hex.decodeHex(src.toCharArray()));

			return new String(resultBytes);
		} catch (GeneralSecurityException | DecoderException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}

	public static boolean verifySign(String src) throws ToolsException {
		try {
			KeyPair keyPair = initRsaKeyPair();
			PublicKey rsaPublicKey = keyPair.getPublic();
			PrivateKey rsaPrivateKey = keyPair.getPrivate();

			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Signature signature = Signature.getInstance(MD5WITH_RSA);
			signature.initSign(privateKey);
			signature.update(src.getBytes());
			// 生成签名bytes
			byte[] signBytes = signature.sign();

			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			keyFactory = KeyFactory.getInstance(RSA);
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			signature = Signature.getInstance(MD5WITH_RSA);
			signature.initVerify(publicKey);
			signature.update(src.getBytes());

			return signature.verify(signBytes);
		} catch (GeneralSecurityException e) {
			logger.error(ConstantExp.GeneralSecurity_Exception, e);
			throw new ToolsException(e);
		}
	}
}
