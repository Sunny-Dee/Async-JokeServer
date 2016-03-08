import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientWorker extends Thread{
	
	Socket sock;

	
	ClientWorker (Socket s) {sock = s;}
	
	
	public void run(){
		//Get I/O streams in/out from the socket
		PrintStream out = null; 
		BufferedReader in = null;
		
		String nums;
		
		try{
			//input to the socket
			in = new BufferedReader
					(new InputStreamReader(sock.getInputStream()));
			//output from the socket
			out = new PrintStream(sock.getOutputStream());
			
			
			System.out.print("Enter numbers to sum: ");
			try {
				/* Get numbers from user*/
			    nums = in.readLine(); 
			    System.out.print(nums + "\n");
			    
			    //Split the number string into an array 
			    String numsArr[] = nums.split(" ");
			    
			    int result = 0; 
			    
			    for (String num : numsArr)
			    	result += Integer.parseInt(num);
			    System.out.print("Your sum is: " + result);
			    
			} catch (IOException x){
				System.out.println("Server read error");
				x.printStackTrace();
			}
	
			sock.close(); //end thread, server keeps on going
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
		

}

