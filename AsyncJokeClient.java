import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;



/**
 * Name: Deliana Escobari       Date: Tuesday March 8th, 2015
 * Java version used: 1.8 
 * 
 *Compile with command: javac AsyncJokeSClient.java 
 * 		or java *.java once to compile all files in the whole folder
 * Run with command: java AsyncJokeClient
 * 
 * Notes: This is the file the user uses to input her name and get a joke or proverb 
 * depending on the mode set by JokeClientAdmin. The default mode if nothing is setup 
 * is joke mode. If connection cannot be established or slow, ctrl+c and try again.
 */

public class AsyncJokeClient {
	private static final int port = 4001;
	static final String id = UUID.randomUUID().toString();
	static String name;
	static Socket sock;
	static int listeningPort = 4111;
	static Socket clientSock;
	
	@SuppressWarnings("resource")
	public static void main(String args[]){
		//variables to give the client a listening port. 
		int q_len = 6; 
		
		String serverName;
		/* If server does not have an IP address
		 * default to local host
		 */
		if (args.length < 1)
			serverName = "localhost";
		else serverName = args[0];
		
		System.out.println("************************");
		System.out.println("* Deliana's JokeServer *");
		System.out.println("************************");
		System.out.println("Is your name WIFI? Because I'm feeling "
				+ "a connection.\n");
		System.out.println("Using server: " + serverName + "\nListening at port: " + port);
		
		
		BufferedReader in = 
				new BufferedReader(new InputStreamReader(System.in)); 
		
		
		try {
			/* Start listening for server to send 
			    * the joke whenever it's ready and 
			    * in the mean time work some math. If there is a connection, 
			    * spawn a mode client thread
				* to take care of the mode administration
				*/
			ServerSocket servsocket = new ServerSocket(listeningPort, q_len);
			
			System.out.print("\nPlease enter your name " +
					"or (quit) to end: ");
			
			System.out.flush(); //flush the stream
			name = in.readLine(); //get's name from user
			
			sendNewPort(name, serverName, listeningPort);
			
			
			
			/* Only if user enters a word other than quit
			 * will the request be fulfilled 
			 */
			if (name.indexOf("quit") < 0 ){
				System.out.println("Hi " + name + " let me share with you some "
						+ "of my best material.");
				System.out.println("Press enter to get a joke or proverb: ");
				
				//wait for user to press enter
				new Scanner(System.in).nextLine();
				
				
				/* Loop continues to get joke, proverb or 
				 * maintenance warning. After the server sends something
				 * it goes to sleep and that's when this loop fires a
				 * number adding thread while waiting.
				 */
				do {
					new NumberClientWorker().start();
					
					clientSock = servsocket.accept();
					new ClientWorker(clientSock).start();
				} while(true);
			}
		} catch (IOException x) {x.printStackTrace();}		
	}
	
	/**
	 * This function sends the server information about the user. Most importantly, it sends
	 * the listening port at the client so we can asynchronously send jokes. 
	 * 
	 * @param name the user name to send personalized jokes. 
	 * @param serverName in this case I only had time to implement 'localhost'
	 * @param listeningPort the client port --> 4111
	 */
	private static void sendNewPort(String name, String serverName, int listeningPort) {
		Socket sock; 
		PrintStream toServer;
		
		try {
			/* Open our connection to server port. 
			 * Choose same port number in JokeServer */
			sock = new Socket(serverName, port);
			
			//Create filter input stream for the socket
			toServer = new PrintStream(sock.getOutputStream());
			
			//Send name to server:
			toServer.println(name);
			toServer.flush();
			
			//Send id to server
			toServer.println(id);
			toServer.flush();
			
			//Send client's listening port
			toServer.println(listeningPort);
			toServer.flush();
			
			
			//close socket
			sock.close();
			
		}  catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
		

		
	}
	


}
