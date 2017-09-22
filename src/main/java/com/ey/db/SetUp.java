package com.ey.db;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SetUp {
	static String USER_AGENT = "Mozilla/5.0";
	private static final Logger log = Logger.getLogger(SetUp.class.getName());

	public static void main(String[] a) {

		addQueryIntent();

	}

	private static String getJsonStringForQueryIntent() {
		
		
		String inputJson = null;
		String path = SetUp.class.getResource("/QueryIntent.json")
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
		String s = "noooo";
		try {
			s = sendPost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
		/*
		String url = "https://api.api.ai/v1/intents?v=20150910";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization",
				"Bearer 4d8addaa7dce4bf9a07af12182ab6922");

		StringEntity entity;
		String result = " ";
		try {
			entity = new StringEntity(getJsonStringForQueryIntent());
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			System.out.println(response);

			// log.severe("Response Code : " +
			// response.getStatusLine().getStatusCode());
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			result = response.toString();
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
		return result;*/
	}

	public  static String sendPost() throws Exception {

		String url = "https://api.api.ai/v1/intents?v=20150910";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization",
				"Bearer 4d8addaa7dce4bf9a07af12182ab6922");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeUTF(getJsonStringForQueryIntent());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		String responseMessage = con.getResponseMessage();
		
		log.info("\nSending 'POST' request to URL : " + url);
		log.info("Response Code : " + responseCode);
		log.info("responseMessage : " + responseMessage);
		log.info("con : "+ con.toString());
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		log.info(response.toString());
		return con.getContent().toString();
	}

}
