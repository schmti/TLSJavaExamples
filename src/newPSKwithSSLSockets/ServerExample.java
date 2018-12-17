package newPSKwithSSLSockets;

import java.security.SecureRandom;
import java.security.Security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;
import org.bouncycastle.tls.PSKTlsServer;
import org.bouncycastle.tls.TlsPSKIdentityManager;
import org.bouncycastle.tls.TlsServerProtocol;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

public class ServerExample
{
	public static void main(String[] args) throws Exception
	{

		Security.addProvider(new BouncyCastleJsseProvider());
		Security.addProvider(new BouncyCastleProvider());

		SSLContext sslContext = SSLContext.getInstance("TLS", "BCJSSE");
		sslContext.init(null, null, null);
		SSLServerSocketFactory fact = sslContext.getServerSocketFactory();
		SSLServerSocket serverSocket = (SSLServerSocket) fact.createServerSocket(55555);

		SSLSocket socket = (SSLSocket) serverSocket.accept();

		TlsCrypto crypto = new BcTlsCrypto(new SecureRandom());

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

		PSKTlsServer server = new PSKTlsServer(crypto, manager);

		TlsServerProtocol protocol = new TlsServerProtocol(socket.getInputStream(), socket.getOutputStream());

		protocol.accept(server);

		System.out.println("Yeaaa");

		protocol.close();
		socket.close();
		serverSocket.close();
	}
}
