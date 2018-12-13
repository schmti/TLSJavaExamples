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
		 * Sie dient grundsätlich nur als Wrapperklasse für den PRE SHARED KEY
		 * 
		 * Im Konstrucktor muss noch eine Bezeichnung für die Identity
		 * hinterlegt werden entweder als String oder byte[]. (Bsp.
		 * "client","peter","userID")
		 * 
		 * Die spätere Protokollklasse benötigt die Methode getPSK() um den PRE
		 * SHARED KEY für den Handshake zu verwenden.
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
		 * Sie beinhaltet alle wichtigen Daten und liefert Methoden für den
		 * späteren Schlüsselaustausch.
		 */

		PSKTlsClient client = new PSKTlsClient(identity);

		/*
		 * Die Klasse TlsClientProtocol führt den Handshake auf dem Socket auf.
		 * 
		 * Sie besitzten alle wichtigen Methoden (Bsp. sendClientHelloMessage(),
		 * sendClientKeyExchangeMessage() ...)
		 * 
		 * mit connect(TlsClient) wird der Handshake zum Server ausgestoßen.
		 */
		TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream(),
				new SecureRandom());

		System.out.println("do handshake");
		// Handshake wird angestoßen
		protocol.connect(client);
		System.out.println("handshake done");
		

		// Hier kann können jetzt Daten (verschlüsselt) auf den Sockets
		// übertragen werden ==========================>

		//Wenn Objekte übertragen werden sollen müssen Sie das (Marker)interface Serilizable implementieren
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

		//stream schließen !
		oos.close();
		ois.close();
		System.out.println("close stream´s");
		
		// Socket schließen !
		socket.close();
		System.out.println("close socket");
		System.out.println("terminated");

	}
}
