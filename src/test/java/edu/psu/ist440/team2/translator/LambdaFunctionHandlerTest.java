package edu.psu.ist440.team2.translator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

	private static DataModel input = new DataModel();
	private LambdaFunctionHandler handler;
	private TestContext context;

	private static final String EXPECTED_DECRYPTED_KEY = "jen_spanish_Caesar_es_translated";
	private static final String EXPECTED_CONFIDENCE = "0.40506";
	private static final String EXPECTED_SOURCE_LANGUAGE = "es";
	private static final String EXPECTED_METHOD = "Caesar";
	private static final String EXPECTED_DECRYPTED_BUCKET = "ist440grp2-decrypted";
	private static final String DECRYPTED_DATA = "I want to be your friend\n";

	@Before
	public void setUp() throws Exception {
		handler = new LambdaFunctionHandler();
		context = new TestContext();
		input.setDecryptedKey(EXPECTED_DECRYPTED_KEY);
		input.setConfidence(EXPECTED_CONFIDENCE);
		input.setSourceLanguage(EXPECTED_SOURCE_LANGUAGE);
		input.setMethod(EXPECTED_METHOD);
		input.setDecryptedBucket(EXPECTED_DECRYPTED_BUCKET);
	}

	@Test
	public void testHandleRequest() {
		DataModel result = handler.handleRequest(input, context);
		assertEquals(EXPECTED_CONFIDENCE, result.getConfidence());
		assertEquals(EXPECTED_DECRYPTED_BUCKET, result.getTranslatedBucket());
	}

	@Test
	public void testHandleRequestNullKey() {
		input.setDecryptedKey(null);
		try {
			DataModel result = handler.handleRequest(input, context);
		} catch (RuntimeException e) {
			assertEquals("Invalid Key name!", e.getMessage());
		}
	}

	@Test
	public void testHandleRequestNullBucket() {
		input.setDecryptedBucket(null);
		try {
			DataModel result = handler.handleRequest(input, context);
		} catch (RuntimeException e) {
			assertEquals("Invalid Bucket name!", e.getMessage());
		}
	}

	@Test
	public void testGetFileData() throws IOException {
		String data = handler.getFileData(EXPECTED_DECRYPTED_BUCKET, EXPECTED_DECRYPTED_KEY);
		assertEquals(DECRYPTED_DATA, data);
	}

	@Test
	public void testWriteTranslatedFile() {
		handler.writeTranslatedFile(EXPECTED_DECRYPTED_BUCKET, EXPECTED_DECRYPTED_KEY, DECRYPTED_DATA);

	}

}
