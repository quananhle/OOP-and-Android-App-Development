import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.Vector;
class PackageClient{
	public static void main(String []args){
		//read the package information from the file
		//file format is 
		//from-address:to-address:wt:package-type
		//package-type=1 (two day) 2(overnight)
		Vector <Package> db=new Vector<Package>();
		String line=new String();
		try{
			//read the file and create the objects
			File textFile = new File("package.txt");
			Scanner scan = new Scanner(textFile);
			while(scan.hasNextLine())
			{
				line=scan.nextLine();				
				String []tok=line.split(":");
				String fromAddress=tok[0];
				String toAddress=tok[1];
				float wt=Float.parseFloat(tok[2]);
				int type=Integer.parseInt(tok[3]);
				if(type==1){//two day package
					db.add(new TwoDayPackage(fromAddress,toAddress,wt));
				}else if(type==2){//overnight package
					db.add(new OverNightPackage(fromAddress,toAddress,wt));
				}else{
					System.out.println("Ilegal package type");
				}
			}
			
		}catch(IOException e){
			System.out.println("Issuse with reading the file. Aborting");
			System.out.println(e.getMessage());
		}
		//print out object information
		for(int i=0;i<db.size();++i){
			System.out.println(db.get(i).printReceipt());
		}
	}
}