package com.ey.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;

/**
 * Servlet implementation class SetupApiAi
 */

public class SetupApiAi {
	static String USER_AGENT = "Mozilla/5.0";
	private static final Logger log = Logger.getLogger(SetupApiAi.class.getName());
	static String url = "https://api.api.ai/v1/intents?v=20150910";
	static String contentType = "application/json";
	static String auth = "Bearer 4d8addaa7dce4bf9a07af12182ab6922";

	private static String getJsonStringForQueryIntent() {
		String inputJson = "";
		String path = SetupApiAi.class.getResource("/QueryIntent.json").getPath();
		System.out.println("Path exists : " + path);
		JSONParser parser = new JSONParser();
		Object obj;
		if (!inputJson.equals("")) {
			try {
				obj = parser.parse(new FileReader(path));
				JSONObject jsonObject = (JSONObject) obj;
				inputJson = jsonObject.toJSONString();
			} catch (IOException | ParseException e) {
				log.info("Exception : " + e);
			}
		}

		return inputJson;
	}

	public static String addQueryIntent() {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization", auth);

		StringEntity entity;
		String result = " ";
		try {
			entity = new StringEntity(getJsonStringForQueryIntent());
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			Header[] header = response.getAllHeaders();
			for (Header header2 : header) {
				log.info("header : " + header2);
			}
			log.info(response.toString());
			// log.severe("Response Code : " +
			// response.getStatusLine().getStatusCode());
			log.info("Response Code : " + response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			result = response.toString();
			StringBuffer stringBuffer = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			log.info("result " + result);
		} catch (IOException e) {
			log.severe("exception :" + e);
			e.printStackTrace();
		}
		return result;
	}

	public static String addIntent() {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization", auth);

		StringEntity entity;
		String result = " ";
		try {
			entity = new StringEntity(getJsonStringForQueryIntent());
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			log.info(response.toString());
			// log.severe("Response Code : " +
			// response.getStatusLine().getStatusCode());
			log.info("Response Code : " + response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			result = response.toString();
			StringBuffer stringBuffer = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			log.info("result " + result);
		} catch (IOException e) {
			log.severe("exception :" + e);
			e.printStackTrace();
		}
		return result;
	}
}
