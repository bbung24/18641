package model;

import java.util.ArrayList;

public class CheckUp
{
	private String patient_id;
	private String date;
	private String result;
	private ArrayList<String> medicationList;
	
	public CheckUp(String patient_id, String date, String result, ArrayList<String> medicationList)
	{
		this.patient_id = patient_id;
		this.date = date;
		this.result = result;
		this.medicationList = medicationList;
	}
	
	public void setPatientID(String id)
	{
		this.patient_id = id;
	}
	public String getPatientID()
	{
		return this.patient_id;
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
