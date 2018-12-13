/* Author: Tim Schmidt
 * Date: 13.12.2018 
 */

package psk;

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

public class ServerExample
{
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
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
