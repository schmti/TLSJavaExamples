/* Author: Tim Schmidt
 * Date: 13.12.2018 
 */

package simplePSK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.crypto.tls.BasicTlsPSKIdentity;
import org.bouncycastle.crypto.tls.PSKTlsClient;
import org.bouncycastle.crypto.tls.TlsClientProtocol;


/* 
 * Eine simple TLS-PSK Client(TCP) Implementierung mit Bouncy Castle
 * 
 * ben�tigt wird die hier ein jar-Datei (bcprov-ext-jdk15on-160.jar)
 * (ist schon eingebunden)
 * Ein BC-Provider muss NICHT extra angemeledet werden.
 * 
 * BEACHTE! hierbei handelt es sich um eine LOW LEVEL Tls API von BouncyCastle
 * Dies macht sich in den mitgelieferten Captures bemerkbar. 
 * Wireshark erkennt kein TLS Protokoll da der Handshake auf den TCP Sockets 'simuliert wird'.
 * benutzte Klassen:
 * 
 * import org.bouncycastle.crypto.tls.BasicTlsPSKIdentity;
 * import org.bouncycastle.crypto.tls.PSKTlsClient;
 * import org.bouncycastle.crypto.tls.TlsClientProtocol;
 * 
 * F�r eine professionelle Implementierung von TLS (PKI und PSK) sollten
 * Klassen aus den folgenden beiden packages benutzt werden
 * 
 * org.bouncycastle.tls.crypto.impl.bc.*
 * org.bouncycastle.tls.crypto.impl.jcajce.*
 * 
 * hier f�r wird eine WEITERE jar von BouncyCastle ben�tigt !!!
 * 'DTLS/TLS API/JSSE Provider' (bctls-jdk15on-160.jar)
 *  ---->   https://www.bouncycastle.org/latest_releases.html
 *  
 *  weitere Infos im package professionelPSK ...
 *  
 */
public class ClientExample
{
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		// bereite eine Socket vor
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
		 */
		BasicTlsPSKIdentity identity = new BasicTlsPSKIdentity("testClient", "password".getBytes());

		/*
		 * Die Klasse PSKTlsClient erbt von AbstractTlsClient und implementiert
		 * die Schnittstellen TlsClient und TlsPeer.
		 * 
		 * Sie beinhaltet alle wichtigen Daten und liefert die Methoden f�r die
		 * sp�tere Authentisierung.
		 */
		PSKTlsClient client = new PSKTlsClient(identity);

		/*
		 * Die Klasse TlsClientProtocol f�hrt den Handshake auf dem Socket auf.
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

		// Wenn Objekte �bertragen werden sollen m�ssen die Objecte das
		// (Marker)Interface Serializable implementieren
		ObjectOutputStream oos = new ObjectOutputStream(protocol.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(protocol.getInputStream());

		String request = "hello i`am a simple client Request !";
		oos.writeObject(request);
		oos.flush();
		System.out.println("send a request to the Server: " + request);

		// !!! der Methode readObject() ist BLOCKIEREND!!!
		// Es wird erst weitergemacht wenn wirklich ein reply vom Server kommt
		String reply = (String) ois.readObject();
		System.out.println("got a reply from the server: " + reply);

		// <===============================================

		// Protokoll abbauen
		protocol.close();
		System.out.println("do quit handshake");

		// Stream schlie�en !
		oos.close();
		ois.close();
		System.out.println("close stream�s");

		// Socket schlie�en !
		socket.close();
		System.out.println("close socket");
		System.out.println("terminated");

	}
}
