package de.uniks.pfp.digitaltwin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DigitalTwin {

	private String targetIP = "192.168.0.119";//"localhost";114
	private int targetPort = 33333;
	private int sendErrorCounter = 0;
	private int numberOfSendTrys = 3;
	private static ArrayList<String> recivedMessages = new ArrayList<>();
	private static ArrayList<String> sentMessages = new ArrayList<>();

	public Socket socket;
	
	public DigitalTwin() throws UnknownHostException, IOException {
		socket = new java.net.Socket(targetIP, targetPort);
	}

	public DigitalTwin(String ip) {
		targetIP = ip;
	}

	public DigitalTwin(String ip, int port) {
		targetIP = ip;
		targetPort = port;
	}

	public static void main(String[] args) {

		// LegoClient client = new LegoClient();

		// try {

		// client.test();

		// } catch (IOException e) {

		// e.printStackTrace();

		// }

	}



	void test() throws IOException {

		String ip = targetIP;

		int port = targetPort;

		java.net.Socket socket = new java.net.Socket(ip, port); // verbindet

																// sich mit

																// Server

		String zuSendendeNachricht = "hallo";

		schreibeNachricht(socket, zuSendendeNachricht);

//		String empfangeneNachricht = leseNachricht(socket);

//		System.out.println(empfangeneNachricht);

	}



	public String sendMessage(String message) {



		/**@param sendet Nachricht and Server

		 *  versucht dies X und meldet Fehler falls es nicht funktionier

		 *  gibt die empfangene antwort zurück ans Hauptprogramm

		 *  Speichert gesendete und empfangen Nachrichten im Array

		 */

		

		String ip = targetIP;

		int port = targetPort;

		String empfangeneNachricht = "";

		

		try {

			

			schreibeNachricht(socket, message);

			// empfangeneNachricht = leseNachricht(socket);
// System.out.println(empfangeneNachricht);
//			getSentMessages().add(message);

//			writeSentMessageInFile();

					

		} catch (UnknownHostException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}



//		if (!empfangeneNachricht.equals("")) {

//			sendErrorCounter = 0;

//			recivedMessages.add(empfangeneNachricht);          // add recivedmessages to Arraylist

//			sentMessages.add(message);

//		}

//

//		if (empfangeneNachricht.equals("") && sendErrorCounter > numberOfSendTrys) { // try again recursiv

//			sendErrorCounter++;

//			empfangeneNachricht = sendMessage(message);

//			sentMessages.add("nicht empfangen: " +message);

//		}

		

		

		return empfangeneNachricht;

	}


	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	public void schreibeNachricht(java.net.Socket socket, String nachricht) throws IOException {

		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		printWriter.print(nachricht);
		printWriter.flush();

	}

	



	public String leseNachricht() throws IOException {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		char[] buffer = new char[200];

		socket.setSoTimeout(20000);
		
		int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert

																	// bis

																	// Nachricht

																	// empfangen

		String nachricht = new String(buffer, 0, anzahlZeichen);

		return nachricht;

	}

	public void deleteRecivedMessages() {
		getRecivedMessages().clear();
	}

	public void deleteSentMessages() {
		getRecivedMessages().clear();
	}

	public String getTargetIP() {
		return targetIP;
	}

	public void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

	public static ArrayList<String> getRecivedMessages() {
		return recivedMessages;
	}

	public static void setRecivedMessages(ArrayList<String> recivedMessages) {
		DigitalTwin.recivedMessages = recivedMessages;
	}

	public static ArrayList<String> getSentMessages() {
		return sentMessages;
	}

	public static void setSentMessages(ArrayList<String> sentMessages) {
		DigitalTwin.sentMessages = sentMessages;
	}

}