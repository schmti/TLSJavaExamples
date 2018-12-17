package newPSK;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.tls.PSKTlsServer;
import org.bouncycastle.tls.TlsPSKIdentityManager;
import org.bouncycastle.tls.TlsServerProtocol;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

/*
 * Hier bei handelt es nicht nun um eine "neuere" Variante 
 * um PSK-TLS über Bouncy Castle zu implementieren.
 * 
 * Hierfür benötigen wir klassen aus der "bctls-jdk15on-160.jar"
 * 
 * sehr ähnlich wie bei dem "alten" PSK ! 
 * nur sehr kleine Unterschiede
 * 
 * 
 */

public class ServerExample
{
	public static void main(String[] args) throws Exception
	{

		// bereite ServerSocket und normalen Socket vor.
		int port = 55555;
		ServerSocket serversocket = new ServerSocket(port);
		System.out.println("open serversocket (" + InetAddress.getLocalHost() + ") \nwaiting for clients ...");
		Socket socket = serversocket.accept();

		/*
		 * Jetzt NEU -> TlsCrypto
		 * alle wichtigen Cyptofunktionen wurden nun in eine 
		 * eigene Klasse ausgelagert.
		 * Sie muss jetzt auch bei der Erzeugung des PSKTlsServers mitgegeben werden
		 */
		TlsCrypto crypto = new BcTlsCrypto(new SecureRandom());

		//alles beim alten
		TlsPSKIdentityManager manager = new TlsPSKIdentityManager()
		{

			@Override
			public byte[] getPSK(byte[] arg0)
			{
				// TODO Auto-generated method stub
				return "password".getBytes();
			}

			@Override
			public byte[] getHint()
			{
				// TODO Auto-generated method stub
				return "???".getBytes();
			}
		};

		/*
		 * NEU crypto muss jetzt mitgegeben werden
		 */
		PSKTlsServer server = new PSKTlsServer(crypto, manager);

		TlsServerProtocol protocol = new TlsServerProtocol(socket.getInputStream(), socket.getOutputStream());

		
		//Handshakewunsch annehmen
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
		
		// Protokoll abbauen
		protocol.close();
		System.out.println("do quit handshake");

		// Stream schließen !
		oos.close();
		ois.close();
		System.out.println("close stream´s");

		// Socket schließen !
		socket.close();
		serversocket.close();
		System.out.println("close socket");
		System.out.println("terminated");
	}
}
