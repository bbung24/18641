package model;

import java.util.ArrayList;

public class CheckUp
{
	private String patientId;
	private String date;
	private String result;
	private ArrayList<String> medicationList;
	
	public CheckUp(String id, String patientId, String date, String result, ArrayList<String> medicationList)
	{
		this.patientId = patientId;
		this.date = date;
		this.result = result;
		this.medicationList = medicationList;
	}
	
	public void setPatientID(String id)
	{
		this.patientId = id;
	}
	public String getPatientID()
	{
		return this.patientId;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public void setResult(String result)
	{
		this.result = result;
	}
	
	public String getResult()
	{
		return this.result;
	}
	
	public void addMedicine(String medicine)
	{
		this.medicationList.add(medicine);
	}
	
	public ArrayList<String> getMedicationList()
	{
		return this.medicationList;
	}
	
	
}
