class BusinessLoan extends Loan{
	BusinessLoan(String name,float amt, float intRate, int term){
		super(name,amt, (intRate+1.0f), term);		
	}
	public String toString(){
		String st=super.toString();
		st=st+"Loan Type: Business" +"\n";
		st=st+"==============\n";
		return st;
	}
}