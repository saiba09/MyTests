package com.ey.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public  class readFromExcel {
	

	static Connection connection;
	static {
		//System.out.println("in static block");
		try {
			connection = ConnectionService.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void finalize() throws Throwable {
		connection.close();
	};

static void toDb(){
	

	int t = 0;
	//createTables();
	System.out.println("tables created");
	ArrayList<LinkedList<LawDescription>> mainList = getDataFromExcel();
	//System.out.println("main list" + mainList);
	System.out.println(mainList.size());
	for (LinkedList<LawDescription> linkedList : mainList) {
		//System.out.println(linkedList);
		for (LawDescription lawDescription : linkedList) {
			//System.out.println(lawDescription);
		t++;
				try {
					addData(lawDescription);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
		}
		System.out.println("20--------------complete");
	}
	
	System.out.println(t);
	/*LinkedList<Question> queList = getQuestionDataFromExcel();
	for (Question question : queList) {
		addQue(question);
	}*/
	

	
}
	static void addData(LawDescription lawDesc)throws Exception{	

		String topic = lawDesc.getTopic().trim();
		String subTopic = lawDesc.getSubTopic().trim();
		String country = lawDesc.getCountry().trim();
		String state = lawDesc.getState().trim();
		String lawDescription = lawDesc.getLaw().trim();				
					
		int countryId = getId("select country_id from Country where upper(country_name)= ?", country.toUpperCase());
		System.out.println("country : "+ country + "  id : "+countryId);
		if (countryId == -1) {
			System.out.println("add new country");
			try(PreparedStatement stmt = connection.prepareStatement("INSERT INTO Country(country_name) values ?")){
				stmt.setString(1, country);
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		countryId = getId("select country_id from Country where upper(country_name)= ?", country.toUpperCase());
		System.out.println("country : "+ country + "  id : "+countryId);
		//check for states
		int stateId = getId("select state_id from State where upper(state_name)=?", state.toUpperCase());
		System.out.println("state : "+ state + "  id : "+stateId);
		if (stateId == -1) {
			System.out.println("add new state");
			try(PreparedStatement stmt = connection.prepareStatement("insert into State(state_name,country_id) values(?,?)")){
				stmt.setString(1, state);
				stmt.setInt(2, countryId);

				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stateId = getId("select state_id from State where upper(state_name)=?", state.toUpperCase());
		int subTopicId = getId("select sub_topic_id from SubTopics where upper(sub_topic_name) = ?", subTopic.toUpperCase());;

		//System.out.println("subTopicId : "+ subTopicId+" stateId : "+ stateId + " countryId :  "+ countryId+ " topicId : "+ topicId);
//Check for Law Description	

		int lawDescriptionId = getId("select law_desc_id from Law_Description where (state_id , country_id ,sub_topic_id) = (?,?,?,?) ", new int[]{stateId,countryId,subTopicId});
		//System.out.println("lawDescription : "+ lawDescription + "  id : "+lawDescriptionId);
		if (lawDescriptionId == -1) {
			//System.out.println("add new lawDescription");
			try(PreparedStatement stmt = connection.prepareStatement("insert into Law_Description(law_description,state_id,country_id,sub_topic_id) values(?,?,?,?)")){
				stmt.setString(1, lawDescription);
				stmt.setInt(4, subTopicId);
				stmt.setInt(2, stateId);
				stmt.setInt(3, countryId);

				stmt.executeUpdate();
				stmt.close();
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
e.printStackTrace();		}
		}
		lawDescriptionId = lawDescriptionId = getId("select law_desc_id from Law_Description where (state_id , country_id ,sub_topic_id) = (?,?,?,?) ", new int[]{stateId,countryId,subTopicId});
		
		//System.out.println("lawDescription : "+ lawDescription + "  id : "+lawDescriptionId);
		
	}
	private static int getId(String  sql , int parameter[]){

		int id = -1;
		int count =1;
		try {
			//jdbcObj.printDbStatus();

			try(PreparedStatement stmt = connection.prepareStatement(sql)){
				 for (int i = 0; i < parameter.length; i++) {
						stmt.setInt(count++, parameter[i]);

				}
				ResultSet resultset = stmt.executeQuery();
				if (resultset.next()) {
					id = resultset.getInt(1);
				}
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return id;
	}
	private static int getId(String  sql , String parameter){

		int id = -1;
		try {
			//dbcObj.printDbStatus();

			try(PreparedStatement stmt = connection.prepareStatement(sql)){
				stmt.setString(1, parameter);
				ResultSet resultset = stmt.executeQuery();
				if (resultset.next()) {
					id = resultset.getInt(1);
				}
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return id;
	}
	static ArrayList<LinkedList<LawDescription>> getDataFromExcel(){
		ArrayList<LinkedList<LawDescription>> listOfobjects = new ArrayList<LinkedList<LawDescription>>();
		int i = 0;
		LinkedList<LawDescription> list = new LinkedList<LawDescription>();
		try {
			String escapeSequence = "\\\\";
			String path = readFromExcel.class.getResource("/formatedSampleData.xlsx")
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
				i++;
				if (i < 20) {
					cRow[2] = cRow[2].replaceAll("\'", escapeSequence+"\'").replaceAll("\"", escapeSequence+"\""); //.replaceAll("\n", "/\n").replaceAll("\t", "/\t");
					LawDescription lawDescription = new LawDescription(cRow[3], cRow[4], cRow[0], cRow[1], cRow[2]);
					//System.out.println(lawDescription);
					list.add(lawDescription);
				}
				else{//34012
					System.out.println("in else");
					listOfobjects.add(list);
					list = new LinkedList<LawDescription>();
					cRow[2] = cRow[2].replaceAll("\'", escapeSequence+"\'").replaceAll("\"", escapeSequence+"\""); //.replaceAll("\n", "/\n").replaceAll("\t", "/\t");
					LawDescription lawDescription = new LawDescription(cRow[3], cRow[4], cRow[0], cRow[1], cRow[2]);
					//System.out.println(lawDescription);
					list.add(lawDescription);
					i=0;
				}
			}
			if (i< 20) {
				listOfobjects.add(list);
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}



		return listOfobjects;
	}

}
