import java.io.*;

public class Driver{
	private static int idCnt=1000;
	private int ID;
	private String lastName;
	private int ageYears;
	private int licenseNo;
	private float drivingExp;
	
	public Driver()
	{
		this.ID=Driver.idCnt++;
		this.lastName="name";
		this.ageYears=16;
		this.licenseNo=11111;
		this.drivingExp=0.5f;
		
	}
	
	public Driver(String lname, int age, int licno, float exp)
	{
		this.ID=Driver.idCnt++;
		this.lastName=lname;
		this.ageYears=age;
		this.licenseNo=licno;
		this.drivingExp=exp;		
	}
	public int getID()
	{
		return this.ID;		
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	public int getAgeYears()
	{
		return this.ageYears;
	}
	
	public int getLicenseNo()
	{
		return this.licenseNo;
	}
	
	public float getDrivingExp()
	{
		return this.drivingExp;
	}
	
	public void setLastName(String name)
	{
		this.lastName=name;
	}
	
	public void setAgeYears(int age)
	{
		this.ageYears=age;
	}
	
	public void setLicenseNo(int licNo)
	{
		this.licenseNo=licNo;
	}
	
	public void setDrivingExp(float exp)
	{
		this.drivingExp=exp;
	}
	public String toString()
	{
		return "The name of the driver is:" + this.getLastName() +" The age is: " + this.getAgeYears() + " The license number is " + this.getLicenseNo() +" The driving experience is " + this.getDrivingExp() + " His ID is " + this.getID();
	}
	public boolean isEqual(Driver driverA)
	{
		if(!this.getLastName().equals(driverA.getLastName()))
			return false;
		if(this.getAgeYears()!=driverA.getAgeYears())
			return false;
		if(this.getDrivingExp()!=driverA.getDrivingExp())
			return false;
		if(this.getLicenseNo()!=driverA.getLicenseNo())
			return false;
		return true;
	}
}