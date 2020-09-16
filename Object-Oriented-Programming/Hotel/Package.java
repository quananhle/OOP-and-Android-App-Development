abstract class Package implements ChargeConstants{
		private String fromAddress;
		private String toAddress;
		private float wt;
		private int packageID;
		private static int counter=10000;//5 digit integer ID
		
		/* accessor methods*/
		public int getID(){
			return this.packageID;
		}
		public String getFromAddress(){
			return this.fromAddress;
		}
		public String getToAddress(){
			return this.toAddress;
		}
		public float getWt(){
			return this.wt;
		}
		
		/*mutator methods*/
		public void setID(){
			this.packageID=counter++;
		}
		public void setFromAddress(String address){
			this.fromAddress=address;
		}
		public void setToAddress(String address){
			this.toAddress=address;			
		}
		public void setWt(float w){
			this.wt=w;
		}
		/*other methods*/
		public float calculateCharge(){
			float charge = this.getWt()*baseCharge;
			return charge;
		}
		/*abstract class*/
		public abstract String printReceipt();			
}