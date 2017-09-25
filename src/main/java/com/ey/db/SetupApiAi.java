package com.ey.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;


/**
 * Servlet implementation class SetupApiAi
 * 
 * Developer access eyChatbot : ff28c61a38424a3684af062f491003ca 
 */

public class SetupApiAi {
	static String USER_AGENT = "Mozilla/5.0";
	private static final Logger log = Logger.getLogger(SetupApiAi.class.getName());
	static String baseUrl = "https://api.api.ai/v1/";
	static String urlVersion = 	"?v=20150910";
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
				 log.info("json obj created " + jsonObject );
			} catch (IOException | ParseException e) {
				log.info("Exception : " + e);
			}
		

		return jsonObject;
	}

	public static String addQueryIntent() throws UnableToCreateIntent {
		log.info("In addQueryIntent");
		String response = "";
		String queryIntentResponse = addIntent("/QueryIntent.json" , null,null);
		log.info("response by adding query intent : "+response);
		String parentId = getId(queryIntentResponse);
		log.info("Parent Id fetched : "+ parentId);
		if (parentId != "") {
			log.info("adding Query - yes follow up intent");
			response = addIntent("/QueryYesIntent.json" , parentId , null);
			log.info("response by adding query - yes follow up intent : "+response);

		}
		else{
			throw new UnableToCreateIntent("Query Intent" ,queryIntentResponse );
		}
		return response;
	}
	public static String addStateIntent() {
		log.info("In addStateIntent ");

		String response = "";
		response = addIntent("/StateIntent.json" , null ,null);
		log.info("response by adding state intent : "+response);

		return response;
	}
	public static String addComplianceExpertIntent() throws UnableToCreateIntent {
		log.info("In addComplianceExpertIntent ");
		log.info("adding ComplianceExpert Intent");
		String response  = addIntent("/ComplianceExpertIntent.json" , null,null);
		log.info("response by adding ComplianceExpert Intent : "+response);
		String rootParentId = getId(response);
		log.info("rootParent Id fetched : "+ rootParentId);
		if (rootParentId != "") {
			log.info("adding ComplianceExpertNoIntent ");
			response = addIntent("/ComplianceExpertNoIntent.json" , rootParentId , null);
			log.info("response by adding ComplianceExpertNoIntent Intent : "+response);
			log.info("adding ComplianceExpertNoIntent ");
			response = addIntent("/ComplianceExpertYesIntent.json" , rootParentId , null);
			log.info("response by adding ComplianceExpertYesIntent Intent : "+response);
			String parentId = getId(response);
			log.info("Parent Id fetched : "+ parentId);
			if (parentId != null) {
				log.info("adding ComplianceExpertYesYesIntent ");
				response = addIntent("/ComplianceExpertYesYesIntent.json", parentId, rootParentId);
				log.info("response by adding ComplianceExpertYesYesIntent  : "+response);
				log.info("adding ComplianceExpertYesNoIntent ");
				response = addIntent("/ComplianceExpertYesNoIntent.json", parentId, rootParentId);
				log.info("response by adding ComplianceExpertYesNoIntent  : "+response);				
				parentId = getId(response);
				log.info("Parent Id fetched : "+ parentId);
				if (parentId != null) {
					log.info("adding ComplianceExpertYesNoNoIntent ");
					response = addIntent("/ComplianceExpertYesNoNoIntent.json", parentId, rootParentId);
					log.info("response by adding ComplianceExpertYesNoNoIntent  : "+response);
					log.info("adding ComplianceExpertYesNoYesIntent ");
					response = addIntent("/ComplianceExpertYesNoYesIntent.json", parentId, rootParentId);
					log.info("response by adding ComplianceExpertYesNoYesIntent  : "+response);

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
		log.info("In getId() " );
		String id = null;
		JSONParser parser = new JSONParser();
		Object obj;
			try {
				log.info("parsing object");
				obj = parser.parse(response);
				JSONObject responseObject = (JSONObject) obj;
				id = responseObject.get("id").toString();
				log.info("id fetched : "+id);
			} catch (ParseException e) {
				log.severe("Exception : " + e);
			}
			return id;
	}
	@SuppressWarnings("unchecked")
	public static String addIntent(String intent, String parentId,String rootParentId ) {
		log.info("add intent()");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(baseUrl + "intents"+urlVersion);
		StringBuffer responseBuffer = new StringBuffer();
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization",auth);
		StringEntity entity;
		log.info("getting json file for : "+ intent);
		JSONObject intentObject = getJsonStringForIntent(intent);
		if (intentObject != null) {
			log.info("json fetched");
			if (parentId != null) {
				intentObject.put("parentId", parentId);
				log.info("parent id added");
			}
			if (rootParentId != null) {
				intentObject.put("rootParentId", rootParentId);
				log.info("root parent id added");
			}
			try {
				entity = new StringEntity(intentObject.toJSONString());
				log.info("posting to API AI");
				post.setEntity(entity);
				HttpResponse response = client.execute(post);
				log.info("got response : "+ response);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					responseBuffer.append(line);
				}
				log.info("parsed responsed");
			} catch (IOException e) {
				log.severe("Exception : " + e);
			}
		}

		return responseBuffer.toString();
	}
	private static JSONObject getJsonForEntity(String entity) {
		// TODO Auto-generated method stub		
			JSONObject entityObject = new JSONObject();
			entityObject.put("name", entity);
			JSONArray entries = new JSONArray();
			entityObject.put("entries",entries);
			
		
		return entityObject;
	}
	public static String addEntity(String entity){
		log.info("addEntity()");
		String addEntityUrl = baseUrl+"entities"+urlVersion;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(addEntityUrl);
		StringBuffer responseBuffer = new StringBuffer();
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization",auth);
		StringEntity stringEntity;
		log.info("getting json file for : "+ entity);
		JSONObject entityObject = getJsonForEntity(entity);
		log.info("json : "+entityObject.toJSONString());
		if (entityObject != null) {
			log.info("json fetched");
			
			try {
				stringEntity = new StringEntity(entityObject.toJSONString());
				log.info("posting to API AI");
				post.setEntity(stringEntity);
				HttpResponse response = client.execute(post);
				log.info("got response : "+ response);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					responseBuffer.append(line);
				}
				log.info("parsed responsed");
				String entityId = getId(responseBuffer.toString());
				addElements( entityId , entity);
			} catch (IOException e) {
				log.severe("Exception : " + e);
			}
			
			
		}
		return responseBuffer.toString();

	}

	private static String addElements(String entityId, String entity) {

		log.info("add addElements()");
		String addEntityUrl = baseUrl+"entities/"+entityId+"/entries"+urlVersion;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(addEntityUrl);
		StringBuffer responseBuffer = new StringBuffer();
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		post.setHeader("Authorization",auth);
		StringEntity stringEntity;
		log.info("getting json file for elements of : "+ entity);
		JSONObject entityObject = getEntityElementsJSON(entity);
		log.info("json : "+entityObject.toJSONString());
		if (entityObject != null) {
			log.info("json fetched");
			
			try {
				stringEntity = new StringEntity(entityObject.toJSONString());
				log.info("posting to API AI");
				post.setEntity(stringEntity);
				HttpResponse response = client.execute(post);
				log.info("got response : "+ response);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					responseBuffer.append(line);
				}
				log.info("parsed responsed");
				
			} catch (IOException e) {
				log.severe("Exception : " + e);
			}
			
			
		}

	return responseBuffer.toString();
		
	}

	private static JSONObject getEntityElementsJSON(String entity) {
		
		JSONObject elementJson = new JSONObject();
		LinkedHashMap<String,String[]> list = new LinkedHashMap<>();
		try {
			String path = SetupApiAi.class.getResource("/us_states.xlsx")
					.getPath();
			//System.out.println(path);
			FileInputStream excelFile = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			String[] cRow = new String[10];
			int index = 0;
			while (iterator.hasNext()) {
				index = 0;
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					// getCellTypeEnum shown as deprecated for version 3.15
					// getCellTypeEnum ill be renamed to getCellType starting
					// from version 4.0

					if (currentCell.getCellTypeEnum() == CellType.STRING) {
						cRow[index] = currentCell.getStringCellValue();
						index++;



					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {


						cRow[index] = currentCell.getStringCellValue();

					}
				}
				String [] synonyms = new String[]{cRow[1],cRow[2]};
				list.put(cRow[0],synonyms);
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray elementJsonArray = new JSONArray();
		for (String element : list.keySet()) {
			JSONArray entries = new JSONArray();
			JSONObject entriesJson = new JSONObject();
			JSONArray synonymsJson = new JSONArray();
			
			String[] synonyms = list.get(entries);
			synonymsJson.add(synonyms[0]);
			synonymsJson.add(synonyms[1]);
			
			entriesJson.put("value", element);
			entriesJson.put("synonyms", synonymsJson);
			entries.add(entriesJson);
			elementJson.put("name", element);
			elementJson.put("entries", entries);

			
		}

		return elementJson;
	
	}
	
}
