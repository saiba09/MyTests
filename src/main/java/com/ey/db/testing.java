package com.ey.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.DBCellRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class testing
 */
public class testing extends HttpServlet {
	private static final Logger log = Logger.getLogger(testing.class.getName());

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String resultJson = "";
		String response = "";

		try {
			resultJson = SetupApiAi.addComplianceExpertIntent();

			JSONParser parser = new JSONParser();
			Object obj;

			obj = parser.parse(resultJson);
			JSONObject responseObject = (JSONObject) obj;
			response = responseObject.get("status").toString();
		} catch (UnableToCreateIntent | ParseException e) {
			log.info("Exception : " + e);
		}
		resp.getWriter().write(response);
	}
	/*
	 * protected void doGet(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException { // TODO Auto-generated
	 * method stub
	 * 
	 * // new DbOperation().deleteFromDb("sf", "sfs"); int i = 0;
	 * log.info("Method start"); try {
	 * 
	 * String path =
	 * readFromExcel.class.getResource("/sample_data.xlsx").getPath();
	 * FileInputStream excelFile = new FileInputStream(new File(path)); Workbook
	 * workbook = new XSSFWorkbook(excelFile); Sheet datatypeSheet =
	 * workbook.getSheetAt(0); Iterator<Row> iterator =
	 * datatypeSheet.iterator(); HashMap<String , ArrayList<String>>
	 * topicsSubtopic = new HashMap<String , ArrayList<String>>();
	 * HashSet<String> state = new HashSet<String>(); HashSet<String> subTopics
	 * = new HashSet<String>(); TreeMap<String, HashMap<String, String>>
	 * descriptionLib = new TreeMap<String ,HashMap<String, String>>(); // map
	 * of subTopic --> <state,law> TreeMap<String, String> questionLib = new
	 * TreeMap<String, String>(); // msp subTopic --> question String[] headers
	 * = new String[55]; String[] cRow = new String[55]; boolean firstRow = true
	 * ; int index = 0; while (iterator.hasNext()) { index = 0; Row currentRow =
	 * iterator.next(); Iterator<Cell> cellIterator = currentRow.iterator();
	 * 
	 * while (cellIterator.hasNext()) { Cell currentCell = cellIterator.next();
	 * //getCellTypeEnum shown as deprecated for version 3.15 //getCellTypeEnum
	 * ill be renamed to getCellType starting from version 4.0
	 * 
	 * if (currentCell.getCellTypeEnum() == CellType.STRING) { if(firstRow){
	 * headers[index] = currentCell.getStringCellValue(); index++; } else{
	 * cRow[index] = currentCell.getStringCellValue(); index++; }
	 * 
	 * 
	 * } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
	 * 
	 * //System.out.print(currentCell.getNumericCellValue() + "--");
	 * if(firstRow){ headers[index] = currentCell.getStringCellValue(); index++;
	 * } else{ cRow[index] = currentCell.getStringCellValue(); index++; } } }
	 * //log.info(cRow[0] + "  " +cRow[1]); if(!firstRow){
	 * 
	 * if (topicsSubtopic.containsKey(cRow[0].trim().toUpperCase())) {
	 * ArrayList<String> subTopic =
	 * topicsSubtopic.get(cRow[0].trim().toUpperCase());
	 * subTopic.add(cRow[1].trim().toUpperCase());
	 * topicsSubtopic.put(cRow[0].trim().toUpperCase(), subTopic); } else{
	 * ArrayList<String> subTopic = new ArrayList<String> ();
	 * subTopic.add(cRow[1].trim().toUpperCase());
	 * topicsSubtopic.put(cRow[0].trim().toUpperCase(), subTopic); }
	 * HashMap<String, String> stateLawMap = new HashMap<String, String>();
	 * for(int k = 3 ; k < cRow.length ; k++){
	 * stateLawMap.put(headers[k].trim().toUpperCase() ,
	 * cRow[k].trim().toUpperCase()); }
	 * descriptionLib.put(cRow[1].trim().toUpperCase(), stateLawMap);
	 * questionLib.put(cRow[1].trim().toUpperCase(), cRow[2]);
	 * 
	 * //log.info(" insert topic "); //readFromExcel.insertTopic(cRow[0]);
	 * 
	 * //log.info("insert subTopic"); //readFromExcel.insertSubTopic(cRow[1],
	 * cRow[0]);
	 * 
	 * //insertSubTopic(conn, cRow[1], cRow[0], out); //insertState(conn,
	 * headers, "US", out); //insertLawDesc(headers, cRow);
	 * //insertQuestion(conn, cRow[2], cRow[1], cRow[2], out); } else {
	 * //log.info("insert state"); //readFromExcel.insertState(headers, "US");
	 * for(int k = 4 ; k < headers.length ; k++){ state.add(headers[k]); } }
	 * firstRow = false; //System.out.println(cRow[0]);
	 * 
	 * } workbook.close();
	 * 
	 * //readFromExcel.insertTopic(topicsSubtopic.keySet());
	 * //readFromExcel.insertSubTopic(topicsSubtopic); log.info("add desc!");
	 * readFromExcel.insertLawDesc(descriptionLib); } catch (Exception e) {
	 * log.info("exception reading excel : " + e); e.printStackTrace(); }
	 * 
	 * response.getWriter().write("end---------"); }
	 * 
	 *//**
		 * @see HttpServlet#doPost(HttpServletRequest request,
		 *      HttpServletResponse response)
		 *//*
		 * protected void doPost(HttpServletRequest request, HttpServletResponse
		 * response) throws ServletException, IOException { // TODO
		 * Auto-generated method stub doGet(request, response); } protected void
		 * doGet(HttpServletRequest request, HttpServletResponse response)
		 * throws ServletException, IOException { // TODO Auto-generated method
		 * stub //String topic = request.getParameter("topic"); String subTopic
		 * = request.getParameter("subTopic"); String state =
		 * request.getParameter("state");
		 * 
		 * response.getWriter().write("state :"+ state + "  subTopic : "+
		 * subTopic); //DbOperation.addNewTopicToDB(topic, subTopic);
		 * response.getWriter().write(DbOperation.getResponse(subTopic ,state ,
		 * "1"));
		 * 
		 * }
		 */
}
