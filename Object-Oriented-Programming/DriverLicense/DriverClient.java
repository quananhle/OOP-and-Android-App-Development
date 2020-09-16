import java.util.Scanner;
class DriverClient{
//helper class
public int licenseFee(Driver dr){
	int fee=(int)Math.min(((dr.getAgeYears()*dr.getLicenseNo())/10000 + (float)50),(float)100);
	return fee;
}

public static void main(String [] args)
{
	String name;
	int age;
	float exp;
	int lic;
	//get the values of first driver from the command line arguments
	name=args[0];
	age=Integer.parseInt(args[1]);
	lic=Integer.parseInt(args[2]);
	exp=Float.parseFloat(args[3]);
	Driver driverA = new Driver(name,age,lic,exp);
	
	//get the values for second driver from the stdin
	Driver driverB = new Driver();	
	Scanner console=new Scanner(System.in);
	System.out.println("Enter the name:");
	name=console.next();
	driverB.setLastName(name);
	
	System.out.println("Enter the age:");
	age=Integer.parseInt(console.next());
	driverB.setAgeYears(age);
	
	System.out.println("Enter the licenseno");
	lic=Integer.parseInt(console.next());
	driverB.setLicenseNo(lic);
	System.out.println("Enter the driving experience");
	exp=Float.parseFloat(console.next());
	driverB.setDrivingExp(exp);

	//calculate the license fees
	DriverClient feeCalc = new DriverClient();
	System.out.println(driverA.toString());
	System.out.println("Fee for the first driver:" + feeCalc.licenseFee(driverA));
	System.out.println(driverB.toString());
	System.out.println("Fee for the second driver:" + feeCalc.licenseFee(driverB));
	
	//check if the two driver information is equal
	if(driverA.isEqual(driverB)){
		System.out.println("The command line object is equal to the keyboard object");
	}
	else
	{
		System.out.println("The command line object is not equal to the keyboard object");
	}	
}
}