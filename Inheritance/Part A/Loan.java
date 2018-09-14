class Loan implements LoanConstants{
	int loanID;
	String lName;
	float amt;
	float intRate;
	int term;
	static int idCounter=10000;
	Loan()
	{
		this.setID();
		this.setlName("noname");
		this.setAmt(0);
		this.setIntRate(0);
		this.setTerm(1);
	}
	Loan(String name,float amt, float intRate, int term)
	{
		this.setID();
		this.setlName(name);
		this.setAmt(amt);
		this.setIntRate(intRate);
		this.setTerm(term);
	}
	/*Get Methods*/
	public int getID(){
		return this.loanID;
	}
	public String getName(){
		return this.lName;
	}
	public float getAmt(){
		return this.amt;
	}
	public float getIntRate(){
		return this.intRate;
	}
	public int getTerm(){
		return this.term;
	}
	/*Set Methods*/
	public void setID(){
		this.loanID=idCounter++;		
	}
	public void setlName(String n){
		this.lName=n;
	}
	public void setIntRate(float rate){
		
			this.intRate=rate;		
	}
	public void setAmt(float lAmt){
		this.amt=lAmt;
	}
	public  void setTerm(int term){
		
		this.term=term;
		
	}
	public float calcLoanAmt(){
		float totAmt=this.getAmt()*(1+((this.getIntRate()*(float)this.getTerm())/100f));
		return totAmt;
	}
	public String toString(){
		String st="==============\n";
		st=st + "Loan ID: " + this.getID()+"\n";
		st=st + "Name: " + this.getName()+"\n";
		st=st + "Amount: " + this.getAmt()+"\n";
		st=st + "InterestRate: " + this.getIntRate()+"\n";
		st=st + "Term: " + getTerm()+"\n";
		st=st + "Total Amount Owed: " + calcLoanAmt()+"\n";
		return st;
	}	
}