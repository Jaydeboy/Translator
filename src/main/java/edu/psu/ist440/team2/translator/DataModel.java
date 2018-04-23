package edu.psu.ist440.team2.translator;

import java.io.Serializable;

/**
 * DataModel contains the metadata associated with decrypting then translating a
 * file.
 * 
 * An instance of this object will be passed to the Translator lambda function
 * as a parameter. This instance should be updated then returned by the lambda
 * function.
 * 
 * @author j6r
 *
 */
public class DataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The name of the decryption method used */
	private String method;
	
	/** The confidence value produced by decryption (optional) */
	private String confidence;
	
	/** The S3 bucket where the file produced by decryption is stored */
	private String decryptedBucket;
	
	/** The decrypted output file name */
	private String decryptedKey;
	
	/** The language used for decryption, also the source language for translation */
	private String sourceLanguage;
	
	/** The S3 bucket where the file produced by translation is stored */
	private String translatedBucket;
	
	/** The translator output file name */
	private String translatedKey;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getDecryptedBucket() {
		return decryptedBucket;
	}

	public void setDecryptedBucket(String decryptedBucket) {
		this.decryptedBucket = decryptedBucket;
	}

	public String getDecryptedKey() {
		return decryptedKey;
	}

	public void setDecryptedKey(String decryptedKey) {
		this.decryptedKey = decryptedKey;
	}

	public String getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public String getTranslatedBucket() {
		return translatedBucket;
	}

	public void setTranslatedBucket(String translatedBucket) {
		this.translatedBucket = translatedBucket;
	}

	public String getTranslatedKey() {
		return translatedKey;
	}

	public void setTranslatedKey(String translatedKey) {
		this.translatedKey = translatedKey;
	}

	@Override
	public String toString() {
		return "DataModel [method=" + method + ", confidence=" + confidence + ", decryptedBucket=" + decryptedBucket
				+ ", decryptedKey=" + decryptedKey + ", sourceLanguage=" + sourceLanguage + ", translatedBucket="
				+ translatedBucket + ", translatedKey=" + translatedKey + "]";
	}

}
