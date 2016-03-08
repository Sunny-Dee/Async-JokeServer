package asynjokeserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Name: Deliana Escobari       Date: Tuesday January 19th, 2015
 * Java version used: 1.8 
 * Compile with command: javac JokeClientAdmin.java 
 * 		or java *.java once to compile all files in the whole folder
 * Run with command: java JokeClientAdmin
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
 * Notes: This is the file that connects to the admin server. Then asks the 
 * user if they like to change the mode. 
 * If they don't input anything the mode is joke by default. 
 * If they press 'j' for joke, 'p' for proverb, or 'm' for maintenance the 
 * mode changes accordingly.  
 */

public class AsyncJokeAdminClient {
	private static final int port = 4500;
	
	public static void main(String args[]){
		String serverName;
		/* If server does not have an IP address
		 * default to local host
		 */
		if (args.length < 1)
			serverName = "localhost";
		else serverName = args[0];
		
		//Start a list of acceptable input values
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("p"); modes.add("j"); modes.add("m");
		
		System.out.println("***********************************");
		System.out.println("* Deliana's JokeServer Mode Admin.*");
		System.out.println("***********************************");
		System.out.println("Using server: " + serverName + ", Port: " + port);
		
		//Input stream to ask the user for a mode
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String mode;
			
			/* Keep looping to asynchronously change the mode
			 * on JokeServer whenever the user wants. 
			 */
			do {
				System.out.print("Enter a 'j' for joke mode, 'p' for " +
					"proverb, or 'm' for maintenance: ");
				System.out.flush();
				mode = in.readLine();
				
				//Change mode only if answer is acceptable
				if (modes.contains(mode))
					//fulfill request:
					changeMode(mode, serverName);
			} while (true); 
		} catch (IOException x) {x.printStackTrace();}
		
	}
	
	private static void changeMode(String mode, String serverName) {
		Socket sock;
		BufferedReader fromServer; 
		PrintStream toServer;
		String textFromServer;
		
		try {
			
			/* Open our connection to server port. 
			 * Choose the same port number we use
			 * in ModeServer */
			sock = new Socket(serverName, port);
			
			//Create filter I/O streams for the socket
			fromServer = 
					new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());
			
			//Send mode to mode server
			toServer.println(mode);
			toServer.flush();
			
			//Read two or three lines of response from server,
			//and block while synchronously waiting
			for (int i = 1; i <= 3; i++){
				textFromServer = fromServer.readLine();
				if (textFromServer != null)
					System.out.println(textFromServer);
			}
			
			//close socket
			sock.close();
			
		}  catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
		
	}
	
}
