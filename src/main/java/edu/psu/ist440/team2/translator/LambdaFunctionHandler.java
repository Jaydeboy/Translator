package edu.psu.ist440.team2.translator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.StringUtils;
import com.darkprograms.speech.translator.GoogleTranslate; // repository of classes to connect to the google translation API

/**
 * The Translator ...
 *
 */
public class LambdaFunctionHandler implements RequestHandler<DataModel, DataModel> {

	StringBuilder sb = new StringBuilder();

	@Override
	public DataModel handleRequest(DataModel input, Context context) {
		context.getLogger().log("Input: " + input);
		DataModel output = new DataModel();
		// the holder for the translated data- text
		try {
			// validate the input
			if (StringUtils.isNullOrEmpty(input.getDecryptedBucket())) {
				throw new IOException("Invalid Bucket name!");
			}
			if (StringUtils.isNullOrEmpty(input.getDecryptedKey())) {
				throw new IOException("Invalid Key name!");
			}
			// retrieves the current bucket containing the text
			String bucket = input.getDecryptedBucket();
			// decryption key - name of file
			String key = input.getDecryptedKey();
			// targeted language used
			StringBuilder newKey = new StringBuilder(key);
			newKey.append("_translated");
			String sourceLanguage = input.getSourceLanguage();
			/*
			 * calls a static function to detect the language and translate it via google
			 * API (com.darkprograms.speech.translator.GoogleTranslate)
			 * 
			 */

			String translated = GoogleTranslate.translate("en", this.getFileData(bucket, key));
			this.writeTranslatedFile(bucket, newKey.toString(), translated);

			// OUTPUT
			context.getLogger().log("\nData: " + translated);
			output.setConfidence(input.getConfidence());
			output.setDecryptedBucket(input.getDecryptedBucket());
			output.setDecryptedKey(input.getDecryptedKey());
			output.setMethod(input.getMethod());
			output.setSourceLanguage(input.getSourceLanguage());
			output.setTranslatedBucket(input.getDecryptedBucket());
			output.setTranslatedKey(newKey.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.getLogger().log("\nOutput: " + output);
		return output;
	}

	/**
	 * Gets the text of the source file
	 * 
	 * @param bucket
	 *            name of the S3 bucket containing the file
	 * @param key
	 *            name of the file
	 * @return decrypted data as a string
	 * @throws IOException
	 *             if the decrypted data file cannot be read
	 */
	protected String getFileData(String bucket, String key) throws IOException {
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		GetObjectRequest getRequest = new GetObjectRequest(bucket, key);
		S3Object s3obj = s3client.getObject(getRequest);

		try (S3ObjectInputStream s3in = s3obj.getObjectContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(s3in))) {
			String line = "";

			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return sb.toString();
	}

	/**
	 * Writes the translation output to S3
	 * 
	 * @param bucket
	 *            name of the S3 bucket to which the file will be saved
	 * @param key
	 *            file namexxxxx
	 * @param data
	 *            a string containing the output of translation
	 * 
	 * @throws UnsupportedEncodingException
	 *             if the data string encoding is unsupported
	 */
	protected void writeTranslatedFile(String bucket, String key, String data) {

		byte[] dataByteArray = data.getBytes();

		ByteArrayInputStream in = new ByteArrayInputStream(dataByteArray);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("text/plain");
		metadata.setContentLength(dataByteArray.length);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		PutObjectRequest putRequest = new PutObjectRequest(bucket, key, in, metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead);
		s3client.putObject(putRequest);
	}
}
