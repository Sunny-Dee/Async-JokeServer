import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Name: Deliana Escobari       Date: Tuesday January 19th, 2015
 * Java version used: 1.8 
 * Compile with command: javac ModeServer.java 
 * 		or java *.java once to compile all files in the whole folder
 * 
 *  * Compile with command: javac JokeSClient.java 
 * 		or java *.java once to compile all files in the whole folder
 * Run with command: java JokeClient
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
 * Notes: This class, similarly to JokeServer, waits for a connection from
 * JokeClientAdmin and once it connects this spawns a ModeWorker to take
 * care of the request. 
 */
 
public class ModeServer implements Runnable{

	public void run() {
		int q_len = 6;
		int port = 4500;
		Socket sock;
		
		
		try{
			@SuppressWarnings("resource")
			ServerSocket servsock = new ServerSocket(port, q_len);
			/* If there is a connection, spawn a mode worker thread
			 * to take care of the mode administration
			*/
			while(servsock.isBound()){  
				sock = servsock.accept();
				new ModeWorker(sock).start();
			}
		} catch (IOException ioe) {
			System.out.println(ioe);}
	}
}
