package com.bigyj.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 使用AES 128位加密，加密模式采用CBC，填充模式采用PKCS5Padding方式
 * AES加解密工具
 * 128位
 * 加解密模式-CBC ：使用CBC模式，需要一个向量，增加算法的强度
 * 填充模式-PKCS5Padding
 * base64编码
 **/
public class AESUtil {
	/** 字符集名称 **/
	public static final String charsetName = "utf-8";
	/** 算法 **/
	public static final String algorithm  = "AES";
	/** 算法/模式/补码方式 **/
	public static final String transformation  = "AES/CBC/PKCS5Padding";

	/**
	 * 加密
	 * @param DataSecret 消息秘钥
	 * @param DataSecretIV 秘钥初始化向量
	 * @param content 要加密的内容
	 * @return 返回base64编码的String
	 * @throws Exception
	 */
	public static String encrypt(String DataSecret, String DataSecretIV, String content) throws Exception {
		byte[] raw = DataSecret.getBytes(charsetName);
		SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
		Cipher cipher = Cipher.getInstance(transformation);
		IvParameterSpec ips = new IvParameterSpec(DataSecretIV.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
		byte[] encrypted = cipher.doFinal(content.getBytes());
		return new BASE64Encoder().encode(encrypted);
	}

	/**
	 * 解密
	 * @param DataSecret 消息秘钥
	 * @param DataSecretIV 秘钥初始化向量
	 * @param content 要解密的内容
	 * @return 返回原文
	 * @throws Exception
	 */
	public static String decrypt(String DataSecret, String DataSecretIV, String content) throws Exception {
		byte[] raw = DataSecret.getBytes(charsetName);
		SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
		Cipher cipher = Cipher.getInstance(transformation);
		IvParameterSpec ips = new IvParameterSpec(DataSecretIV.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
		byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original);
		return originalString;
	}

}
