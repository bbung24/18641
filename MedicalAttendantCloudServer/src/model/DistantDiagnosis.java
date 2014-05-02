package model;

public class DistantDiagnosis
{
	private String id, name, symptom, picLoc, vocLoc;

	public DistantDiagnosis(String id, String name, String symptom,
			String picLoc, String vocLoc)
	{
		this.id = id;
		this.name = name;
		this.symptom = symptom;
		this.picLoc = picLoc;
		this.vocLoc = vocLoc;
	}

	public String getID()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}

	public String getSymptom()
	{
		return this.symptom;
	}

	public String getPicLoc()
	{
		return this.picLoc;
	}

	public String getVocLoc()
	{
		return this.vocLoc;
	}
	
	public void setID(String id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setSymptom(String symptom)
	{
		this.symptom = symptom;
	}
	
	public void setPicLoc(String picLoc)
	{
		this.picLoc = picLoc;
	}
	
	public void setVocLoc(String vocLoc)
	{
		this.vocLoc = vocLoc;
	}

}
