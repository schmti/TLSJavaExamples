package psk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.crypto.tls.BasicTlsPSKIdentity;
import org.bouncycastle.crypto.tls.PSKTlsClient;
import org.bouncycastle.crypto.tls.TlsClientProtocol;

/* Ein Basic TLS-PSK Client.
 * 
 */
public class ClientExample
{
	public static void main(String[] args) throws IOException,ClassNotFoundException
	{
		// bereite eine SocketVor
		String host = "localhost";
		int port = 55555;
		Socket socket = new Socket(host, port);
		System.out.println("open socket");

		/*
		 * Die Klasse BasicTlsPSKIndentity implementiert das Interface
		 * TlsIdentity
		 * 
		 * Sie dient grunds�tlich nur als Wrapperklasse f�r den PRE SHARED KEY
		 * 
		 * Im Konstrucktor muss noch eine Bezeichnung f�r die Identity
		 * hinterlegt werden entweder als String oder byte[]. (Bsp.
		 * "client","peter","userID")
		 * 
		 * Die sp�tere Protokollklasse ben�tigt die Methode getPSK() um den PRE
		 * SHARED KEY f�r den Handshake zu verwenden.
		 * 
		 * -->
		 * https://www.bouncycastle.org/docs/tlsdocs1.5on/org/bouncycastle/tls/
		 * TlsPSKIdentity.html
		 */

		BasicTlsPSKIdentity identity = new BasicTlsPSKIdentity("client", "password".getBytes());

		/*
		 * Die Klasse PSKTlsClient erbt von AbstractTlsClient und implementiert
		 * die Schnittstellen TlsClient und TlsPeer.
		 * 
		 * Sie beinhaltet alle wichtigen Daten und liefert Methoden f�r den
		 * sp�teren Schl�sselaustausch.
		 */

		PSKTlsClient client = new PSKTlsClient(identity);

		/*
		 * Die Klasse TlsClientProtocol f�hrt den Handshake auf dem Socket auf.
		 * 
		 * Sie besitzten alle wichtigen Methoden (Bsp. sendClientHelloMessage(),
		 * sendClientKeyExchangeMessage() ...)
		 * 
		 * mit connect(TlsClient) wird der Handshake zum Server ausgesto�en.
		 */
		TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream(),
				new SecureRandom());

		System.out.println("do handshake");
		// Handshake wird angesto�en
		protocol.connect(client);
		System.out.println("handshake done");
		

		// Hier kann k�nnen jetzt Daten (verschl�sselt) auf den Sockets
		// �bertragen werden ==========================>

		//Wenn Objekte �bertragen werden sollen m�ssen Sie das (Marker)interface Serilizable implementieren
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
		String request = "hello i`am a simple client Request !";
		oos.writeObject(request);
		oos.flush();
		System.out.println("send a request to the Server: " + request);
		
		//!!! der Methode readObject() ist BLOCKIEREND!!!
		//Es wird erst weitergemacht wenn wirklich ein reply vom Server kommt
		String reply = (String)ois.readObject();
		System.out.println("got a reply from the server: " + reply);
		

		// <===============================================

		// Protokoll abbauen
		protocol.close();
		System.out.println("do quit handshake");

		//stream schlie�en !
		oos.close();
		ois.close();
		System.out.println("close stream�s");
		
		// Socket schlie�en !
		socket.close();
		System.out.println("close socket");
		System.out.println("terminated");

	}
}
