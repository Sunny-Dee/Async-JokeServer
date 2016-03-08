import java.io.*;
import java.net.Socket;

/**
 * Name: Deliana Escobari       Date: Tuesday January 19th, 2015
 * Java version used: 1.8 
 * Compile with command: javac ModeWorker.java 
 * 		or java *.java once to compile all files in the whole folder
 * 
 * How to run this project:
 * 		In separate shell window open:
 * 				java JokeServer
 * 				java JokeClient
 * 				java JokeClientAdmin
 * 
 * 		All acceptable commands are displayed on the various consoles.
 * 		This runs across machines, in which case you have to pass the IP address of
 * 		the server to the clients. For example, if the server is running at
 * 		140.192.1.22 then you would type:
 * 				java JokeClient 140.192.1.22
 * 				java JokeClientAdmin 140.192.1.22
 * 
 * List of files needed for running the program.
 * 				JokeClient.java
 * 				JokeClientAdmin.java
 * 				JokeServer.java
 * 				ModeServer.java
 * 				ModeWorker.java
 * 				Worker.java
 * 				ClientState.java
 * 
 * Notes: Thread that processes a mode change. It is important to know that if the
 * user enters any character that is not j, p, or m, the system will go into the
 * default joke more. 
 */

public class ModeWorker extends Thread{
	
	Socket sock;
	volatile static String mode = "j";
	
	ModeWorker (Socket s) {sock = s;}
	
	
	public void run(){
		//Get I/O streams in/out from the socket
		PrintStream out = null; 
		BufferedReader in = null;
		
		try{
			//input to the socket
			in = new BufferedReader
					(new InputStreamReader(sock.getInputStream()));
			//output from the socket
			out = new PrintStream(sock.getOutputStream());
			
			try {
				/* Get mode from user. This field is used 
				 * by the Worker thread to process requests
				 * so updating it changes the behavior of the program*/
			    mode = in.readLine(); 
			    
			    /* The following block prints out the 
			     * new mode to the server screen and to the 
			     * mode admin client */
			    if (mode.equals("p")){
			    	out.println("Proverb Mode");
			    	System.out.println("Switched to proverb mode.");
			    }
			    else if (mode.equals("m")){
			    	out.println("Maintenance Mode");
			    	System.out.println("Switched to maintenance mode.");
			    }
			    else {
			    	out.println("Default Joke Mode");
			    	System.out.println("Switched to joke mode.");
			    }
			    
			    
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
