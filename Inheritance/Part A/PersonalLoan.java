class PersonalLoan extends Loan{
	PersonalLoan(String name,float amt, float intRate, int termType){
		super(name,amt, (intRate+2.0f), termType);		
	}
	public String toString(){
		String st=super.toString();
		st=st+"Loan Type: Personal" +"\n";
		st=st+"==============\n";
		return st;
	}
}