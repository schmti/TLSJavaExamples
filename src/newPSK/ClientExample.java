package newPSK;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.tls.PSKTlsClient;
import org.bouncycastle.tls.TlsClientProtocol;
import org.bouncycastle.tls.TlsPSKIdentity;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

/*
 * Hier bei handelt es nicht nun um eine "neuere" Variante 
 * um PSK-TLS �ber Bouncy Castle zu implementieren.
 * 
 * Hierf�r ben�tigen wir klassen aus der "bctls-jdk15on-160.jar"
 * 
 * sehr �hnlich wie bei dem "alten" PSK ! 
 * nur sehr kleine Unterschiede
 * 
 */

public class ClientExample
{
	public static void main(String[] args) throws Exception
	{

		// bereite eine Socket vor
		String host = "192.168.178.57";
		int port = 55555;
		Socket socket = new Socket(host, port);
		System.out.println("open socket");

		/*
		 * Jetzt NEU -> TlsCrypto
		 * alle wichtigen Cyptofunktionen wurden nun in eine 
		 * eigene Klasse ausgelagert.
		 * Sie muss jetzt auch bei der Erzeugung des PSKTlsClients mitgegeben werden
		 */

		TlsCrypto crypto = new BcTlsCrypto(new SecureRandom());

		/*
		 * jetzt NEU -> die BasicTlsPSKIdentity() wurde abge�ndert.
		 * nun ben�tigen wir ein TlsPSKIdentity Object.
		 * 
		 * Da TlsPSKIdentity jedoch ein Interface ist m�ssen
		 * wir diese erst noch implementieren (siehe unten)
		 * 
		 * --> weiterhin wrapper f�r PSK und PSKIdentity!
		 */
		TlsPSKIdentity identity = new MyTlsPSKIdentiy();

		/*
		 * NEU crypto muss jetzt mitgegeben werden
		 */
		PSKTlsClient pskClient = new PSKTlsClient(crypto, identity);

		/*
		 * NEU TlsClientProtocol braucht jetzt nicht mehr ein SecureRandom !! 
		 * das ist jetzt mit dem TlsCrypto Object im Client abgedeckt 
		 */

		TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream());

		//Handshake ansto�en
		protocol.connect(pskClient);
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

class MyTlsPSKIdentiy implements TlsPSKIdentity
{
	@Override
	public void skipIdentityHint()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyIdentityHint(byte[] arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getPSKIdentity()
	{
		// TODO Auto-generated method stub
		return "client".getBytes();
	}

	@Override
	public byte[] getPSK()
	{
		// TODO Auto-generated method stub
		return "password".getBytes();
	}
}
