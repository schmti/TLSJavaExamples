package pki;

import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

public class BasicBCTLSServer
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("StartServer");
		Security.addProvider(new BouncyCastleJsseProvider());
		Security.addProvider(new BouncyCastleProvider());

		SSLContext sslContext = SSLContext.getInstance("TLS", "BCJSSE");
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX", "BCJSSE");

		kmf.init(Utils.createServerKeyStore(), Utils.SERVER_PASSWORD);

		sslContext.init(kmf.getKeyManagers(), null, null);

		SSLServerSocketFactory fact = sslContext.getServerSocketFactory();
		SSLServerSocket serverSocket = (SSLServerSocket) fact.createServerSocket(Utils.PORT_NO);

		System.out.println("waiting for clients ...");
		SSLSocket socket = (SSLSocket) serverSocket.accept();
		System.out.println("Have new Client: " + socket.getRemoteSocketAddress());

		System.out.println("send and receive here ...");

		socket.close();
		serverSocket.close();
		System.out.println("Close Socket");

	}

}
