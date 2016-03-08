import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Name: Deliana Escobari       Date: Tuesday January 19th, 2015
 * Java version used: 1.8 
 * Compile with command: javac ModeClient.java 
 * 		or java *.java once to compile all files in the whole folder
 * Run with command: java ModeClient
 */
public class ModeClient {
	private static final int port = 4500;
	
	public static void main(String args[]){
		String serverName;
		if (args.length < 1)
			serverName = "localhost";
		else serverName = args[0];
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("p"); modes.add("j"); modes.add("m");
		
		System.out.println("Deliana's JokeServer Admin Mode.");
		System.out.println("Using server: " + serverName + ", Port: " + port);
		
		//Input stream to ask the user for a mode
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String mode;
			do {
				System.out.print("Enter a 'j' for joke mode, 'p' for " +
					"proverb, or 'm' for maintenance: ");
				System.out.flush();
				mode = in.readLine();
				if (modes.contains(mode))
					changeMode(mode, serverName);
			} while (true); // YOU CAN CHANGE THIS LATER TO ALLOW FOR QUIT!!!
		} catch (IOException x) {x.printStackTrace();}
		
	}
	
	private static void changeMode(String mode, String serverName) {
		Socket sock;
		BufferedReader fromServer; 
		PrintStream toServer;
		String textFromServer;
		
		try {
			sock = new Socket(serverName, port);
			
			fromServer = 
					new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());
			
			toServer.println(mode);
			toServer.flush();
			
			//Read two or three lines of response from server,
			//and block while synchronously waiting
			for (int i = 1; i <= 3; i++){
				textFromServer = fromServer.readLine();
				if (textFromServer != null)
					System.out.println(textFromServer);
			}
			
			sock.close();
			
		}  catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
		
	}
	
}
