import java.io.*;
import java.net.*;
import java.util.*;
import java.applet.*;
import java.awt.*;
import javax.swing.*;

public class ChatterServer extends JApplet {
	
	final static int SERVER_PORT = 3333;
	public void init(){
		try {
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	                createGUI();
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("createGUI didn't successfully complete");
	    }
	}
	
	private void createGUI() {
	    JLabel label = new JLabel("Server Chat");
	    label.setHorizontalAlignment(JLabel.CENTER);
	    label.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
	    getContentPane().add(label, BorderLayout.CENTER);
	}
	
	public static void main(String [] args) throws Exception {
		
		// server sockets wait to hear from clients, so after the socket is created, it sits and waits until a client connects
		
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
		System.err.println("Waiting for a client");
		
		// when a client tries to connect, save a reference to the client socket
		
		Socket clientSocket = serverSocket.accept();

		System.out.println("Connection requested from: " + clientSocket.getLocalAddress());

		// just like the client, set up input and output streams, but of course in reverse: output goes out to, and input comes in from, the -client-
		
		PrintStream toClient = new PrintStream(clientSocket.getOutputStream(), true);
		BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		// and set up to read keyboard input
		
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		// start communication, then enter a similar loop of communication.
		
		toClient.println("What's up?");
		String incoming = fromClient.readLine();
		do {
			System.out.println(incoming);
			System.out.print("Your turn> ");
			String myReply;
			myReply = keyboard.readLine();
			toClient.println(myReply);		
			//Loop that determines whether server can continue to chat if their response ends with "...". Unable to get it to fully function.
			while(myReply.endsWith("...") == true)
			{
				myReply = keyboard.readLine();
				toClient.println(myReply);	
			}
			incoming = fromClient.readLine();
			
			
		}
		while(incoming != null); //I changed the while loop into a do while loop in an effort to use the "..." continuation   
		
		// close the socket when communication is complete
		
		serverSocket.close();
	}

}
