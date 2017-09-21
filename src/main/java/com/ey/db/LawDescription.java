package com.ey.db;

public class LawDescription {
private String state;
String country;
String topic;
String subTopic;
String law;
@Override
public String toString() {
	return "LawDescription [state=" + state + ", country=" + country + ", topic=" + topic + ", subTopic=" + subTopic
			+ ", law=" + law + "]";
}
public String getState() {
	return state;
}
public LawDescription() {
this("", "", "", "", "");
}
public void setState(String state) {
	this.state = state;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getTopic() {
	return topic;
}
public void setTopic(String topic) {
	this.topic = topic;
}
public String getSubTopic() {
	return subTopic;
}
public void setSubTopic(String subTopic) {
	this.subTopic = subTopic;
}
public String getLaw() {
	return law;
}
public void setLaw(String law) {
	this.law = law;
}
public LawDescription(String topic, String subTopic, String country, String state, String law) {
	super();
	this.state = state;
	this.country = country;
	this.topic = topic;
	this.subTopic = subTopic;
	this.law = law;
}

}
