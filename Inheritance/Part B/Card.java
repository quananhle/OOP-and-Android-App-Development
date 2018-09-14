class Card{
	int suite;//1-heart 2-club 3-diamond 4-spade
	int value;//1-Ace, 2-10; 11-jack 12-queen 13-king 
	Card(int s,int v){
			this.suite=s;
			this.value=v;
	}
	public String toString(){
		String st=""; 
		String s="";
		String v="";
		if(this.suite==1){
			s="Heart";
		}else if(this.suite==2){
			s="Club";
		}else if(this.suite==3){
			s="Diamond";
		}else if(this.suite==4){
			s="Spade";
		}
		
		if(this.value==11){
			v="Jack";
		}else if(this.value==12){
			v="Queen";
		}else if(this.value==13){
			v="King";
		}else{
			v=""+this.value;
		}
			
		st="Card: " + v + "of " + s + "\n";
		return st;
	}
}