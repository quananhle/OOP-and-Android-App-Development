package iit.db.client;
import iit.db.service.Student;
public class StudentClient
{
	   public static void main(String[] args){
		  Student a = new Student("Jason", "Bourne");
		  a.setAge(18);
		  System.out.println(a.toString());
	  }

}
