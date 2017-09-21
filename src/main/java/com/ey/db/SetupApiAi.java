package com.ey.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class SetupApiAi
 */

public class SetupApiAi {
	static String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] a) {
		/*
		 * Set up intents in seq: Query Intent Query-follow-up stateLaw
		 * complianceExpert complinanceExpert yes complianceExpert no
		 */
		addQueryIntent();
		//getJsonStringForQueryIntent();

	}

	private static String getJsonStringForQueryIntent() {
		String inputJson = null;
		String path = SetupApiAi.class.getResource("/QueryIntent.txt")
				.getPath();
		System.out.println("Path exists : " + path);
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(path));
			JSONObject jsonObject = (JSONObject) obj;
			// System.out.println(jsonObject.get("webhookUsed"));
			// jsonObject.put("webhookUsed", "false");
			// System.out.println(jsonObject.get("webhookUsed"));
			inputJson = jsonObject.toJSONString();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return inputJson;
	}

	public static String addQueryIntent() {
		String url = "https://api.api.ai/v1/intents?v=20150910";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization",
				"Bearer 36f114a183b241ad8fda33e11c962a5f");

		StringEntity entity;
		String result = " ";
		try {
			entity = new StringEntity(getJsonStringForQueryIntent());
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			// log.severe("Response Code : " +
			// response.getStatusLine().getStatusCode());
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			result = response.getEntity().toString();
			StringBuffer stringBuffer = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			// log.severe("result " + result); // Gives result Json
			System.out.println("result " + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
