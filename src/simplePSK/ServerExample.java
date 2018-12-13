/* Author: Tim Schmidt
 * Date: 13.12.2018 
 */

package simplePSK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.crypto.tls.PSKTlsServer;
import org.bouncycastle.crypto.tls.TlsPSKIdentityManager;
import org.bouncycastle.crypto.tls.TlsServerProtocol;

/* 
 * Eine simple TLS-PSK Server(TCP) Implementierung mit Bouncy Castle
 * 
 * benötigt wird die hier eine jar-Datei (bcprov-ext-jdk15on-160.jar)
 * (ist schon im Repository)
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
 * Für eine professionelle Implementierung von TLS (PKI und PSK) sollten
 * Klassen aus den folgenden beiden packages benutzt werden
 * 
 * org.bouncycastle.tls.crypto.impl.bc.*
 * org.bouncycastle.tls.crypto.impl.jcajce.*
 * 
 * hier für wird eine WEITERE jar von BouncyCastle benötigt !!!
 * 'DTLS/TLS API/JSSE Provider' (bctls-jdk15on-160.jar)
 *  ---->   https://www.bouncycastle.org/latest_releases.html
 *  
 *  weitere Infos im package professionelPSK ...
 *  
 */

public class ServerExample
{
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		// bereite ServerSocket und normalen Socket vor.
		int port = 55555;
		ServerSocket serversocket = new ServerSocket(port);
		System.out.println("open serversocket (" + InetAddress.getLocalHost() + ") \nwaiting for clients ...");
		Socket socket = serversocket.accept();

		/*
		 * Der Server braucht keine eigene TlsPskIdentity wie der Client sondern
		 * einen dazu passenden Manager. Er dient als Wrapperklasse für den PRE
		 * SHARED KEY
		 * 
		 * TlsPSKIdentityManager ist ein Interface und muss daher implementiert
		 * werden
		 */

		TlsPSKIdentityManager identityManager = new TlsPSKIdentityManager()
		{

			@Override
			public byte[] getPSK(byte[] arg0)
			{
				return "password".getBytes();
			}

			@Override
			public byte[] getHint()
			{
				return "a little hint".getBytes();
			}
		};

		
		/*
		 * Die Klasse PSKTlsServer erbt von AbstractTlsServer und implementiert
		 * die Schnittstelle TlsServer
		 * 
		 * Sie beinhaltet alle wichtigen Daten und liefert die Methoden für die
		 * spätere Authentisierung.
		 */
		PSKTlsServer server = new PSKTlsServer(identityManager);

		TlsServerProtocol protocol = new TlsServerProtocol(socket.getInputStream(), socket.getOutputStream(),
				new SecureRandom());

		System.out.println("wait for client Hello (handshake)");
		// warte auf Handshake anfrage des Clients
		protocol.accept(server);
		System.out.println("handshake done");

		// Hier kann können jetzt Daten (verschlüsselt) auf den Sockets
		// übertragen werden ==========================>

		// Wenn Objekte übertragen werden sollen müssen Sie das
		// (Marker)interface Serilizable implementieren
		ObjectOutputStream oos = new ObjectOutputStream(protocol.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(protocol.getInputStream());

		// !!! der Methode readObject() ist BLOCKIEREND!!!
		// Es wird erst weitergemacht wenn wirklich ein request vom Client kommt
		String request = (String) ois.readObject();

		String reply = "hello i`am a simple server reply !";
		oos.writeObject(reply);
		oos.flush();
		System.out.println("send a reply to the client: " + request);

		// <===============================================

		protocol.close();
		oos.close();
		ois.close();
		socket.close();
		serversocket.close();
	}
}
