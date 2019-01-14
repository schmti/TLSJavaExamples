package pki;

import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

public class BasicBCTLSServerWithClientAuth
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("StartServer");
		Security.addProvider(new BouncyCastleJsseProvider());
		Security.addProvider(new BouncyCastleProvider());

		SSLContext sslContext = SSLContext.getInstance("TLS", "BCJSSE");
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX", "BCJSSE");
		kmf.init(Utils.createServerKeyStore(), Utils.SERVER_PASSWORD);

		// F�r Client Auth auch noch eine TrustManagerFactroy
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
		tmf.init(Utils.createClientTrustStore());

		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		SSLServerSocketFactory fact = sslContext.getServerSocketFactory();
		SSLServerSocket serverSocket = (SSLServerSocket) fact.createServerSocket(Utils.PORT_NO);

		serverSocket.setNeedClientAuth(true);

		System.out.println("waiting for clients ...");
		SSLSocket socket = (SSLSocket) serverSocket.accept();
		System.out.println("Have new Client: " + socket.getRemoteSocketAddress());

		System.out.println("send and receive here ...");
		
		socket.close();
		System.out.println("Close Socket");

	}
}
