import java.util.Vector;
class Poker extends CardGame{
	
	int cardsPerPerson;
	Poker(){
			this.cardsPerPerson=5;
			
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
		return "This is a game of Poker. Each player gets 5 cards";
	}
	
}