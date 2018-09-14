class OverNightPackage extends Package{	
	OverNightPackage(String fromAddress, String  toAddress, float wt){
		this.setFromAddress(fromAddress);
		this.setToAddress(toAddress);
		this.setWt(wt);
		this.setID();
	}
	public float calculateCharge(){
		float charge = super.calculateCharge()+(this.getWt()*overNightExtraCharge);
		return charge;
	}
	public String  printReceipt(){
		
		String st="========================================\n";
		st=st + "Receipt for Package ID: "+ this.getID()+"\n";
		st=st + "Type: Overnight"+"\n";
		st=st + "From Address: " + this.getFromAddress()+"\n";
		st=st + "To Address: " + this.getToAddress()+"\n";
		st=st + "Weight: " + this.getWt()+" oz.\n";
		st=st + "Charge: " + this.calculateCharge()+"\n";
		st=st + "==========================================\n";
		return st;
	}
}