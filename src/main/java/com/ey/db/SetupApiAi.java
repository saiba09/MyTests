package com.ey.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

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
 * 
 * Developer access eyChatbot : ff28c61a38424a3684af062f491003ca 
 */

public class SetupApiAi {
	static String USER_AGENT = "Mozilla/5.0";
	private static final Logger log = Logger.getLogger(SetupApiAi.class.getName());
	static String url = "https://api.api.ai/v1/intents?v=20150910";
	static String contentType = "application/json";
	static String auth = "Bearer 4d8addaa7dce4bf9a07af12182ab6922";

	private static JSONObject getJsonStringForIntent(String name) {
		JSONObject jsonObject = null;
		String path = SetupApiAi.class.getResource(name).getPath();
		System.out.println("Path exists : " + path);
		JSONParser parser = new JSONParser();
		Object obj;
		
			try {
				obj = parser.parse(new FileReader(path));
				 jsonObject = (JSONObject) obj;
			} catch (IOException | ParseException e) {
				log.info("Exception : " + e);
			}
		

		return jsonObject;
	}

	public static String addQueryIntent() throws UnableToCreateIntent {
		String response = "";
		String queryIntentResponse = addIntent("/QueryIntent.json" , null,null);
		String parentId = getId(queryIntentResponse);
		if (parentId != "") {
			response = addIntent("/QueryYesIntent.json" , parentId , null);
		}
		else{
			throw new UnableToCreateIntent("Query Intent" ,queryIntentResponse );
		}
		return response;
	}
	public static String addStateIntent() {
		String response = "";
		response = addIntent("/StateIntent.json" , null ,null);
		return response;
	}
	public static String addComplianceExpertIntent() throws UnableToCreateIntent {
		String response  = addIntent("/ComplianceExpertIntent.json" , null,null);
		String rootParentId = getId(response);
		if (rootParentId != "") {
			response = addIntent("/ComplianceExpertNoIntent.json" , rootParentId , null);
			response = addIntent("/ComplianceExpertYesIntent.json" , rootParentId , null);
			String parentId = getId(response);
			if (parentId != null) {
				response = addIntent("/ComplianceExpertYesYesIntent", parentId, rootParentId);
				response = addIntent("/ComplianceExpertYesNoIntent", parentId, rootParentId);
				parentId = getId(response);
				if (parentId != null) {
					response = addIntent("/ComplianceExpertYesNoNoIntent", parentId, rootParentId);
					response = addIntent("/ComplianceExpertYesNoYesIntent", parentId, rootParentId);
					parentId = getId(response);
				}else{
					throw new UnableToCreateIntent("ComplianceExpert-Yes-No Intent" ,response );
				}
			}else{
				throw new UnableToCreateIntent("ComplianceExpert-Yes Intent" ,response );
			}
		}
		else{
			throw new UnableToCreateIntent("ComplianceExpertIntent" ,response );
		}
		return response;
	}
	private static String getId(String response){
		String id = null;
		JSONParser parser = new JSONParser();
		Object obj;
			try {
				obj = parser.parse(response);
				JSONObject responseObject = (JSONObject) obj;
				id = responseObject.get("id").toString();
			} catch (ParseException e) {
				log.severe("Exception : " + e);
			}
			return id;
	}
	@SuppressWarnings("unchecked")
	public static String addIntent(String intent, String parentId,String rootParentId ) {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		StringBuffer responseBuffer = new StringBuffer();
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization",auth);
		StringEntity entity;
		JSONObject intentObject = getJsonStringForIntent(intent);
		if (intentObject != null) {
			if (parentId != null) {
				intentObject.put("parentId", parentId);
			}
			if (rootParentId != null) {
				intentObject.put("rootParentId", rootParentId);

			}
			try {
				entity = new StringEntity(intentObject.toJSONString());

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
		}

		return responseBuffer.toString();
	}
}
