import java.util.Scanner;
public class CreateLoans{
	Loan [] Loans;
	public static void main(String []args){
		CreateLoans client=new CreateLoans();
		Scanner in = new Scanner(System.in);
		float pRate;
		int noLoans=2;
		client.Loans=new Loan[noLoans];
		System.out.println("Enter the prime interest rate");
		while(true){
			if(in.hasNextFloat()){
				pRate = in.nextFloat();
				break;
			}
		}
		int cnt=0;
		String name;
		float amt;
		int loanType;
		int term;
		Loan aLoan=null;
		//get the info for five loans
		while(cnt<noLoans){
			System.out.println("Enter the information for a loan");
			//enter name
			System.out.println("Enter name");
			name=in.next();
			//enter amount
			System.out.println("Enter loan amount");
			amt=in.nextFloat();
			if(amt>Loan.maxLoan){
				System.out.println("Loan amount too large. Cancelling");
				continue;
			}
			//enter loan term
			System.out.println("Loan Term: Enter 1 for Short term Loan, 2 for Medium Term Loan and 3 for Long Term Loan");
			
			term=in.nextInt();
			if(term==1){
				term=Loan.shortTerm;
			}else if(term==2){
				term=Loan.mediumTerm;
			}else if(term==3){
				term=Loan.longTerm;
			}else{
				term=Loan.shortTerm;
			}
			
			//enter loan type
			System.out.println("Loan type: Enter 1 for Business Loan and 2 for Personal Loan");
			while(true){
				loanType=in.nextInt();
				if(loanType==1){
					//create an object of type business
					aLoan=new BusinessLoan(name,amt,pRate,term);
					break;					
				}
				else if(loanType==2){
					//create an object of type personal
					aLoan=new PersonalLoan(name,amt,pRate,term);
					break;
				}
				else{
					System.out.println("Bad Loan Type.Try again");			
					
				}
			}
			//add object to arry
			client.Loans[cnt]=aLoan;
			++cnt;
		}
		//print out all the loan objects
		for(int i=0;i<noLoans;++i){
			System.out.println(client.Loans[i].toString());
		}
	}

}