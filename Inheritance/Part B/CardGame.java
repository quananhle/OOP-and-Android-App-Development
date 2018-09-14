import java.util.Random;
import java.util.Vector;
abstract class CardGame{
	Vector <Card>deck=new Vector<Card>(52);
	CardGame(){
		//initialize the values of the cards		
		for(int i=1;i<5;++i){//suite
			for(int j=1;j<14;++j){//value
				deck.add(new Card(i,j));
			}			
		}
		//shuffle
		this.shuffle();
	}
	public void shuffle(){
		//random number generator
		Random rndGen = new Random();
		int rndCard;
		for(int i=0;i<200;++i){//You can change this number as you wish
			//pick a random card and move it to the end
			rndCard=rndGen.nextInt(52);
			this.deck.add(this.deck.remove(rndCard));
		}
	}
	public abstract String displayDescription();
	public abstract Vector deal();
	
}