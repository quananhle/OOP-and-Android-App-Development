package iit.db.service;
public class Student
{
	  private String lastName;
	  private String firstName;
      private int age;	  
	  //accessor methods
	  public String getLastName(){
		  return this.lastName;		  
	  }
	  public String getFirstName(){
		  return this.firstName;		  
	  }
	  public int getAge(){
		  return this.age;		  
	  }
	  //mutator methods
	  public void setLastName(String lname){
		  this.lastName=lname;		  
	  }
	  public void setFirstName(String fname){
		  this.firstName=fname;		  
	  }
	  public void setAge(int a){
		  this.age=a;		  
	  }
	  // toString()
	  public String toString(){
		  String str = "Name:" + this.getFirstName() + " " + this.getLastName() +"\n";
		  str= str + "Age: " + this.getAge();
		  return str;
	  }
 	  public Student(String first, String last){
		  this.setFirstName(first);
		  this.setLastName(last);
	  } 
}
