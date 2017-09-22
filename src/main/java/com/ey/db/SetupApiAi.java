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
 * 
 * Developer access Akshay : ff28c61a38424a3684af062f491003ca 
 */

public class SetupApiAi {
	static String USER_AGENT = "Mozilla/5.0";
	private static final Logger log = Logger.getLogger(SetupApiAi.class.getName());
	static String url = "https://api.api.ai/v1/intents?v=20150910";
	static String contentType = "application/json";
	static String auth = "Bearer 4d8addaa7dce4bf9a07af12182ab6922";

	private static String getJsonStringForIntent(String name) {
		String inputJson = "";
		String path = SetupApiAi.class.getResource(name).getPath();
		System.out.println("Path exists : " + path);
		JSONParser parser = new JSONParser();
		Object obj;
		
			try {
				obj = parser.parse(new FileReader(path));
				JSONObject jsonObject = (JSONObject) obj;
				inputJson = jsonObject.toJSONString();
			} catch (IOException | ParseException e) {
				log.info("Exception : " + e);
			}
		

		return inputJson;
	}

	public static String addQueryIntent() {
		String response = "";
		String queryIntentResponse = addIntent("/QueryIntent.json");
		String parentId = "";
		JSONParser parser = new JSONParser();
		Object obj;
			try {
				obj = parser.parse(queryIntentResponse);
				JSONObject responseObject = (JSONObject) obj;
				parentId = responseObject.get("id").toString();
			} catch (ParseException e) {
				log.info("Exception : " + e);
			}
		if (parentId != "") {
			response = addIntent("/QueryYesIntent.json");
		}
		return response;
	}

	public static String addIntent(String intent) {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		StringBuffer responseBuffer = new StringBuffer();
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization",auth);
		StringEntity entity;
		try {
			entity = new StringEntity(getJsonStringForIntent(intent));
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseBuffer.append(line);
			}
		} catch (IOException e) {
			log.info("Exception : " + e);
		}
		return responseBuffer.toString();
	}
}
