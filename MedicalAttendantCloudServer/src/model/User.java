package model;

public class User
{
	String id;
	String pw;
	int age;
	int zipCode;
	boolean isDoctor;
	
	public User(String id, String pw, int age, int zipCode, boolean isDoctor)
	{
		this.id = id;
		this.pw = pw;
		this.age = age;
		this.zipCode = zipCode;
		this.isDoctor = isDoctor;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public String getPW()
	{
		return this.pw;
	}
	
	public int getAge()
	{
		return this.age;
	}
	
	public int getZip()
	{
		return this.zipCode;
	}
	
	public boolean getIsDoctor()
	{
		return this.isDoctor;
	}
}
