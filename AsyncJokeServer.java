import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Name: Deliana Escobari       Date: Tuesday January 19th, 2015
 * Java version used: 1.8 
 * Compile with command: javac JokeServer.java 
 * 		or java *.java once to compile all files in the whole folder
 * Run with command: java JokeServer
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
 * Notes: This is the connection coming from JokeClient. The server waits for the
 * connection and once it happens if spawns a mode server administrator and a Worker thread
 * so the user can ask for a joke. 
 */

public class AsyncJokeServer {
	
	/*This program keeps track of data on the server side through
	 * this hash table
	 */
	static volatile Hashtable<String, ClientState> clients = 
			new Hashtable<String, ClientState>();
	
	public static void main(String args[]) throws IOException{
		int q_len = 6; 
		
		int port = 4001;
		Socket sock;
	
		ModeServer modeserver = new ModeServer(); //this is the separate thread for the mode portion
		Thread t = new Thread(modeserver); 
		t.start(); //this calls the run method on ModeServer
		
		//Establish the connection
		ServerSocket servsock = new ServerSocket(port, q_len);
		
		System.out.println("Joke Server starting up.\nReady to "
				+ "brighten someone's day!\nListening at port: " + port + ".");
		
		/*This block spawns a new worker thread to process the
		 * joke/proverb request 
		 */
		try{
			while(true){ 
				System.out.println("Checking for some good stuff to share");
				sock = servsock.accept(); 
				new Worker(sock).start();
			}
		//finally always executes in a try block, I thought it was a good
		//way to make sure the server socket closes. 
		} finally {servsock.close();} 
		
	}
}

class ClientState {
	private volatile List<String> jokes;
	private volatile List<String> proverbs;
	
	public ClientState() {
		jokes = new ArrayList<String>();
		proverbs = new ArrayList<String>();
	}
	
	public List<String> getJokeList(){
		return jokes;
	}
	
	public List<String> getProverbList(){
		return proverbs;
	}
	
	public void updateJokeList(List<String> list){
		jokes = list;
	}
	
	public void updateProverbList(List<String> list){
		proverbs = list;
	}
}

