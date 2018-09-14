class EnumDemo
{
  public enum Days { Sun, Mon, Tue, Wed, Thur, Fri, Sat };

  public static void main( String [] args )
  {
    Days d1, d2;  // declare two Days object references
    d1 = Days.Wed;
    d2 = Days.Fri;
    System.out.println( "Comparing objects using equals" );
    if ( d1.equals( d2 ) )
       System.out.println( d1 + " equals " + d2 );
     else
       System.out.println( d1 + " does not equal " + d2 );
	if ( d1.compareTo( d2 ) > 0 )
       System.out.println( d1 + " is greater than " + d2 );
    else if ( d1.compareTo( d2 ) < 0 )
       System.out.println( d1 + " is less than " + d2 );
    else
       System.out.println( d1 + " is equal to " + d2 );
	System.out.println( "\nGetting the ordinal value" );
	System.out.println( "The value of " + d1 + " is "
                         + d1.ordinal( ) );
	System.out.println( "\nConverting a String to an object" );
	Days day = Days.valueOf( "Mon" );
	System.out.println( "The value of day is " + day );
  }
}
