import java.util.Vector;
class Bridge extends CardGame{
	
	int cardsPerPerson;
	Bridge(){
			this.cardsPerPerson=13;
			
	}
	public Vector deal(){
		if(this.deck.size()<this.cardsPerPerson){//check if there are sufficient cards left
			System.out.println("Not enough cards left to deal. Returning");
			return null;
		}
		Vector <Card>dealtCards=new Vector<Card>();
		for(int i=0;i<this.cardsPerPerson;++i){
			dealtCards.add(this.deck.remove(0));
		}
		return dealtCards;
	}
	public String displayDescription(){
		return "This is a game of Bridge. Each player gets 13 cards";
	}
}