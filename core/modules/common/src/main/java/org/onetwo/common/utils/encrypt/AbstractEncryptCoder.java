package org.onetwo.common.utils.encrypt;

import java.security.Key;

import javax.crypto.Cipher;

import org.onetwo.common.exception.BaseException;

abstract public class AbstractEncryptCoder implements EncryptCoder{

	abstract protected String getAlgorithmCipher();
	

	protected byte[] doCipher(Key skeySpec, int opmode, byte[] byteContent){
		//构造密匙
//		SecretKeySpec skeySpec = new SecretKeySpec(key, AES_KEY);
//		SecretKeySpec skeySpec = new SecretKeySpec(skeyEncoded, AES_KEY);
		//密码器
		Cipher aesCipher = null;
		byte[] result = null;
		try{
			aesCipher = Cipher.getInstance(getAlgorithmCipher());
			aesCipher.init(opmode, skeySpec);
			result = aesCipher.doFinal(byteContent);
		}catch (Exception e) {
			throw new BaseException("Cipher error: " + e.getMessage() , e);
		}
		return result;
	}
}
