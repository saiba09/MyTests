package com.ey.db;

public class UnableToCreateIntent  extends Exception{

	private static final long serialVersionUID = 1L;
	String intentName ;
	String response;
	public UnableToCreateIntent(String intentName, String response) {
		super();
		this.intentName = intentName;
		this.response = response;
	}
	public UnableToCreateIntent(){
		this(null, null);
	}
	@Override
	public String toString() {
		return "Intent : "+ this.intentName + "  \n Response :  "+this.response;
	}

}
