package client;
import superclass.Figure;
public class Figclient{

	public static void main(String []args){
		/*Task: look at the Figure superclass. Change the class to abstract class by adding the keyword "abstract" to the class definition.
		Compile the Figure class using the javac -d option.
		
		*/
		/* Task: Now add two abstract methods area() and perimeter() to the Figure class
		Compile it.
		*/
		/*
			Task: Now instantiate an object of abstract Class Figure called fig1 in this main()
			
			Compile. Do you get an error? Why?
			
			Remove the object instantiation of Fig1
		*/
		//Figure fig1 =new Figure(1,2);
		
		/*Task: Now create a subclass of Figure called Square in package subclass
		
			Create a default constructor  that sets the length to 5
			
			Create a non-default constructor that takes in three values: X coordinate, Y coordinate and Lenght. Use explicit invocation of the superclass constructor to set the values of X andY coordinates
			
			Square should have additional attributes called lenght and noSides. Also create accessor and mutator methods for these new attributes. Since we know that square always has 4 side, you can initialize noSides to 4 during declaration in the class.
			
			Having a variable for noSides is a little weird as it has a constant value of 4. This is to illustrate a point we will make later in this exercise.

			Compile. Do you get an error?
		*/
		
		/*
			Since Square is a non-abstract subclass of an absract class it will have to implement all the abstract methods.
			
			So go ahead and implement just the area() using the formula for the area of a square
			
			Compile. Do you get an error?
		*/
		
		/*
			Task: To avoid the error above, you have to implement ALL the abstract methods from the superclass

			Go ahead and implement the perimeter() for the Square class
			
			Also implement toString() method so that any subclass of Figure should be able to use it.
		*/
		
			
		/*
			Task: instantiate an object s1 of class square with the parameters (x=2,y=2,length=10) and print out the area and perimeter
		*/
		
		/* Task: now modify the perimeter() in the Square subclass so that the return value is float. You will need to appropriate typecasting in the method to prevent errors.
		
		So the signature for perimeter() in the Square subclass is now  "public float perimeter()"
		
		Compile the Square Class. What error do you get? Why?
		*/
		
		/*
			Task: Change back the return value to double in the perimeter method
		*/
		
		
		
		/*
			Task: Create a subClass EquiTriangle by inheriting Figure class, and implement the area() and perimeter() for an equilateral triangle. 
			
			Put EquiTriangle in package subclass as you did with Square class
			
			Create a default constructor  that sets the length to 5
			
			Create a non-default constructor that takes in three values: X coordinate, Y coordinate and Lenght. Use explicit invocation of the superclass constructor to set the values of X andY coordinates
			
			Remember to create attributes for length and noSides. Also create accessor and mutator methods for these new attributes.
			
			Since we know that a triangle always has 3 sides, you can initialize noSides to 3 during declaration in the class.
			
			Implement the area() and perimeter() methods for this class.
			
		*/
		
		/*
			Task: instantiate an object t1 of class EquiTriangle with the parameters (x=2,y=2,length=10) and print out the area and perimeter
			
			Now when the objects print out the area and perimeter , it should also print out the object type, namely "This is a Square with area....". What changes do you need to make?
		*/
		
		/*
			Task: Create a subClass Circle by inheriting Figure class, and implement the area() and perimeter() for a circle. 
			
			Put Circle in package subclass as you did with Square and EquiTriangle class
			
			Create a default constructor  that sets the radius to 5
			
			Create a non-default constructor that takes in three values: X coordinate, Y coordinate and radius. Use explicit invocation of the superclass constructor to set the values of X andY coordinates
			
			Remember to create attributes for radius. Also create accessor and mutator methods for the new attributes.
			
			Implement the area() and perimeter() methods for this class.
		*/
		
		/*
			Task: instantiate an object c1 of class Circle with the parameters (x=2,y=2,radius=10) and print out the area and perimeter
		*/
		
		
		
		
		/*Smarter Design+/
		It is possible to define an abstract subclass of an abstract superclass. This makes sense when the abstract subclass implements some but not all the abstract method in the superclass. 
		
		Is this helpful here?
		*/
		
		/*
		Task: Create an intermediate abstract subclass of Figure called Polygon. Remember Square and Equilateral triagles are actually different types of polygon. So Square and EquilTriangle can be subclassed from Polygon.
		
		The abstract class Polygon will now have the attributes lenght and noSides, where were originally in Square and EquiTriangle classes

		Create a default contructor that sets the lenght to 5 and no of sides to 3
		
		Create a non-default contructor that takes the values of X cordinate and Y cordinate along with the lenght and no of sides. 
		Now how will you set the values of X coordinate and Y coordinate which are the attributes of Figure class?

		
		You will now implement the Perimeter() method in Polygon because it has both the attributes you need i.e. lenght of the side and no. of sides.
		*/
		
		/*
			Task: modify the class Square and EquiTriangle so that now it extends Polygon instead of Figure.

			Modify/remove the constructors appropriately
		*/
		
		/* Do we need to change the Circle Class?*/
		
		/*
			Compile and rerun the client class
		
		*/
	
	}
}