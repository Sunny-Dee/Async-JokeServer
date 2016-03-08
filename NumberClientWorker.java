import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NumberClientWorker extends Thread{
	public void run(){
		BufferedReader bf = 
				new BufferedReader(new InputStreamReader(System.in)); 
		
		while(true){
			System.out.flush();
			String nums = "";
			/*
			 * Do work while we wait for server response
			 */
			System.out.print("Enter numbers to add: ");
			try {
				// Get numbers from user
				nums = bf.readLine();
				
				//Split the number string into an array 
			    String numsArr[] = nums.split(" ");
			    
			    int result = 0; 
			    
			    for (String num : numsArr)
			    	result += Integer.parseInt(num);
			    System.out.println("Your sum is: " + result);
			} catch (IOException e) {
				e.printStackTrace();
			} 

		}
	}
}
