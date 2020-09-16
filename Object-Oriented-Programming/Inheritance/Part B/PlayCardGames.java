import java.util.Vector;
class PlayCardGames{
	public static void printHand(Vector <Card>arr){
		System.out.println("Hand of the player consusts of: ");
		for(int i=0;i<arr.size();++i){
			System.out.println("Card " + i + " : ");
			System.out.println(arr.get(i).toString());
		}
	}
	public static void main(String []args){
		//deal a hand to player player poker
		Poker game1=new Poker();
		Vector <Card>hand1=game1.deal();
		//print out the hand
		PlayCardGames.printHand(hand1);
		Bridge game2=new Bridge();
		Vector <Card>hand2=game2.deal();
		PlayCardGames.printHand(hand2);
	}
}