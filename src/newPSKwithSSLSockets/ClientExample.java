package newPSKwithSSLSockets;

import java.security.SecureRandom;
import java.security.Security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;
import org.bouncycastle.tls.PSKTlsClient;
import org.bouncycastle.tls.TlsClientProtocol;
import org.bouncycastle.tls.TlsPSKIdentity;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

public class ClientExample
{
	public static void main(String[] args) throws Exception
	{
		Security.addProvider(new BouncyCastleJsseProvider());
		Security.addProvider(new BouncyCastleProvider());

		SSLContext sslContext = SSLContext.getInstance("TLS", "BCJSSE");
		sslContext.init(null, null, null);
		SSLSocketFactory fact = sslContext.getSocketFactory();
		//SSLSocket socket = (SSLSocket) fact.createSocket("192.168.178.57", 55555);
		SSLSocket socket = (SSLSocket) fact.createSocket("localhost", 55555);

		TlsCrypto crypto = new BcTlsCrypto(new SecureRandom());
		TlsPSKIdentity identity = new MyTlsPSKIdentiy();
		PSKTlsClient pskClient = new PSKTlsClient(crypto, identity);
		TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream());
		protocol.connect(pskClient);

		System.out.println("Yeaaa");

		protocol.close();
		socket.close();

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
