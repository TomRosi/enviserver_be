package cz.aimtec.enviserver.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helper {
	
	private Helper() {
		// hiding the implicit constructor
	}

	public static String readResource(String resourcePath, Charset charset) throws IOException {
		InputStream is = Helper.class.getResourceAsStream(resourcePath);
		if (is != null) {
			return getStringFromInputStream(is, charset);
		}
		return null;
	}
	
	// convert InputStream to String
	public static String getStringFromInputStream(InputStream is, Charset charset) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));) {			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
		} catch (Exception e) {
			Logger logger = LoggerFactory.getLogger(Helper.class);
			logger.error("Error reading the data.", e);
		}
		return sb.toString();
	}
	
}
