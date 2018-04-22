package edu.psu.ist440.team2.translator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

/**
 * The Translator ...
 *
 */
public class LambdaFunctionHandler implements RequestHandler<DataModel, DataModel> {

	@Override
	public DataModel handleRequest(DataModel input, Context context) {
		context.getLogger().log("Input: " + input);

		// TODO: implement your handler
		return input;
	}

	/**
	 * Gets the text of the decrypted source file
	 * 
	 * @param bucket name of the S3 bucket containing the decrypted file
	 * @param key name of the decrypted file
	 * @return decrypted data as a string
	 * @throws IOException if the decrypted data file cannot be read
	 */
	protected String getDecryptedFileData(String bucket, String key) throws IOException {
		StringBuilder sb = new StringBuilder();

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
	 * @param bucket name of the S3 bucket to which the file will be saved
	 * @param key file name
	 * @param data a string containing the output of translation
	 * 
	 * @throws UnsupportedEncodingException if the data string encoding is unsupported
	 */
	protected void writeTranslatedFile(String bucket, String key, String data) {

		byte[] dataByteArray = data.getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(dataByteArray);
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("text/plain");
		metadata.setContentLength(dataByteArray.length);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		PutObjectRequest putRequest = new PutObjectRequest(bucket, key, in, metadata);
		s3client.putObject(putRequest);
	}
}
