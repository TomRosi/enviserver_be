package cz.aimtec.enviserver.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cz.aimtec.enviserver.common.Constants;

@Controller
public class DummyController {
			
	/*Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping(path="/files/{filename:.+}")
	public ResponseEntity<String> getIssuesDummy(@PathVariable String filename) {
		String result = "{}";
		try {
			boolean binary = !filename.endsWith(".json");
			// TODO binary does not work (tested on *.jpeg)
			result = readResource(Constants.DATA_FOLDER + filename, StandardCharsets.UTF_8, binary);
			if (result != null && result.startsWith("{")) {
				try {
					result = new JSONObject(result).toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (result != null && result.startsWith("[")) {
				try {
					result = new JSONArray(result).toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			logger.error("Error reading data from the file.", e);
		} 
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private String readResource(String resourcePath, Charset charset, boolean binary) throws IOException {
		 
		InputStream resourceStream = DummyController.class.getResourceAsStream(resourcePath);
		if (resourceStream  != null) {
			return binary ? getStringFromInputStreamBin(resourceStream) : getStringFromInputStream(resourceStream, charset);
		}
		return null;
	}
	
	private String getStringFromInputStreamBin(InputStream resourceStream) {
		StringBuilder sb = new StringBuilder();
		byte[] buffer = new byte[2048];
		try (BufferedInputStream bufResourceStream = new BufferedInputStream(resourceStream)) {
			int count;
			while ((count = bufResourceStream.read(buffer)) != -1) {
				for (int i = 0; i < count; i++) {
					sb.append((char) (buffer[i] & 0xFF));					
				}
			}			
		}
		catch (Exception e) {			
			logger.error("Error reading data.", e);		
		}
		return sb.toString();
	}

	private String getStringFromInputStream(InputStream is, Charset charset) throws IOException {		
		StringBuilder sb = new StringBuilder();
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, charset))) {			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
		} catch (Exception e) {			
			logger.error("Error reading data.", e);		
		}
		return sb.toString();
	}*/
}