import java.io.*;


public class ClientWorker extends Thread{
	
	public void run(){
		//Get I/O streams in/out from the socket
		PrintStream out = null; 
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String nums;
		
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

	}
}

