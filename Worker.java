import java.io.*;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Name: Deliana Escobari       Date: Tuesday January 19th, 2015
 * Java version used: 1.8 
 * Compile with command: javac Worker.java 
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
 * Notes: Fulfills a client request, job, prover, or warning message, based on
 * the default mode or the mode specified by the user in JokeClientAdmin.
 */

public class Worker extends Thread{
	String mode;
	static String name;
	
	Socket sock; 	
	private static List<String> jokes;
	private static List<String> proverbs;
	static volatile Hashtable<String, ClientState> clients;
	String id;

	
	Worker (Socket s) {
		sock = s;	
	}
	
	public void run(){

		PrintStream out = null; 
		BufferedReader in = null;
		
		//Point the local lists to the main lists 
		//stored on the server side. 
		clients = AsyncJokeServer.clients;
		
		try{
			
			//input to the socket
			in = new BufferedReader
					(new InputStreamReader(sock.getInputStream()));

			
			try{
				
			    name = in.readLine(); //get user's name from the socket	
			    mode = ModeWorker.mode; //Get the mode from the ModeWorker
			    id = in.readLine(); //Get the id from the socket
			    System.out.println("Fulfilling request for " + name + 
			    		" id: " + id);

			    //If this is a new client, add it to the client table
			    if (!clients.containsKey(id)){
			    	ClientState newclient = new ClientState();
			    	clients.put(id, newclient);
			    }
			    
			    /*Get the port number associated with the listening client
			     * to send the connection
			     */
			     int port = Integer.parseInt(in.readLine());
			     System.out.println("Got client listening port: " + port);
			     String serverName = "localhost"; //this can be expanded
			     
			     //break the connection
			     sock.close(); 
			     
			     
			     while(true){
			    	 try {
					    	System.out.println("Sleeping for a bit...");
							sleep(10000);
							//start new connection at listening client 
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				     Socket clientsock = new Socket(serverName, port);
			    	 System.out.println("Socket is bound to listening port at client: " + clientsock.isBound());
				     //output goes to client socket
				     out = new PrintStream(clientsock.getOutputStream());
			     

			     
				    /* Set our local tables to be the ones that belong 
				     * to this particular client
				     */
				    jokes = clients.get(id).getJokeList();
				    proverbs = clients.get(id).getProverbList();
				    
				    
				    
				    /* get joke or proverb based on the mode
				     * and update the client's file
				     */
				    System.out.println("Sending client request");
				    printRequest(mode, out);
				    clientsock.close();
			     }
			   

			} catch (IOException x){  //catch exceptions. 
				System.out.println("Server read error");
				x.printStackTrace(); 
			}
			
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		
	}

	/* ***********Helper functions********** */
	public void printRequest(String mode, PrintStream out){
		String str;
		if (mode.equals("m")){
			str = "WARNING: Server is temporarily unavailable. Check-back shortly.";
			out.println(str);
		}
		else if (mode.equals("p")){
			str = chooseProverb();
			out.println(str);
		}
		else {
			str = chooseJoke();
			out.println(str);
		}
		
		System.out.println(str);
	}
	
	/* The following 4 functions help
	 * select a random item in the list and return it. 
	 * If the list is empty, they re add all items,
	 * then choose a random item. 
	 * Finally, update the client's tables on the 
	 * server's hash table.
	 */
	public String chooseJoke(){
		int idx = 0; 				
		String joke;
		
		if (!jokes.isEmpty()){
			idx = ThreadLocalRandom.current().nextInt(0, jokes.size());
			joke = jokes.remove(idx);
		}
		else {
			addJokes();
			idx = ThreadLocalRandom.current().nextInt(0, jokes.size());
			joke = jokes.get(idx);
			jokes.remove(idx);
		}
		
		clients.get(id).updateJokeList(jokes);
		return "Joke for " + name + "\n" + joke;
	}
	
	public String chooseProverb(){
		int idx = 0; 
		
		String proverb;
		
		if (!proverbs.isEmpty()){
			idx = ThreadLocalRandom.current().nextInt(0, proverbs.size());
			proverb = proverbs.get(idx);
			proverbs.remove(idx);
		}
		else {
			addProverbs();
			idx = ThreadLocalRandom.current().nextInt(0, proverbs.size());
			proverb = proverbs.get(idx);
			proverbs.remove(idx);
		}
		
		clients.get(id).updateProverbList(proverbs);
		return "Proverb for " + name + "\n" + proverb;
	}
	
	private void addJokes(){
		jokes.add("A. I changed my password to \"incorrect.\" \nSo whenever "
				+ "I forget what it is, the computer will say \n\"Your password is incorrect.\"");
		jokes.add("B. In the 21st century deleting history \n"
				+ "has become more important than making it.");
		jokes.add("C. " + name + ", you know what's ironic?\n"
				+ "Red, white, and blue stand for freedom until\n"
				+ "they are flashing behind you.");
		jokes.add("D. A clean house is the sign of a broken computer.");
		jokes.add("E. " + name + ", what did the spider do on the computer?\nMade a web-site!");
		
	}
	
	private void addProverbs(){
		proverbs.add("A. \"Fear is the path to the dark side…fear leads to anger…\nanger "
				+ "leads to hate…hate leads to suffering\" \n"
				+ "                                    -Master Yoda");
		
		proverbs.add("B. \"Do. Or do not. There is no try.\" \n"
				+ "                  -Master Yoda");
		
		proverbs.add("C. \"Who's more foolish? The fool, \n"
				+ "or the fool that follow him?\" \n"
				+ "               -Obi Wan Kenobi");
		
		proverbs.add("D. All we have to decide is what to do\n"
				+ "with the time that is given to us.\n"
				+ "                        -Gandalf The Grey");
		
		proverbs.add("E. There is some good in this world, " + name
				+ "\n and it's worth fighting for. \n"
				+ "                                      -Samwise Gamgee");
	}
}
