import java.io.*;
import java.net.Socket;


public class ClientWorker extends Thread{
	static Socket clientSock;
	int listeningPort = 4111;
	
	ClientWorker (Socket s) {
		clientSock = s;	
	}
	
	
	public void run(){
	    try {
			//take to helper functions to fulfill request
			getJokeOrProb() ;
			//close socket each time. 
			clientSock.close();
	    } catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private static void getJokeOrProb() {
		
		BufferedReader fromServer; 
		String textFromServer;
		
		try {
			//Create filter I/O streams for the socket
			fromServer = 
					new BufferedReader(new InputStreamReader(clientSock.getInputStream()));

			//Read up to four lines of response from server,
			//and block while synchronously waiting
			System.out.println("\n");
			for (int i = 1; i <= 4; i++){
				textFromServer = fromServer.readLine();
				if (textFromServer != null)
					System.out.println(textFromServer);
			}
		}  catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}		
	}
}

