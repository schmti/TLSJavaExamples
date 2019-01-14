package pki;

import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

public class BasicBCTLSClientWithClientAuth
{
	public static void main(String[] args) throws Exception
	{
		try
		{

			Security.addProvider(new BouncyCastleJsseProvider());
			Security.addProvider(new BouncyCastleProvider());

			SSLContext sslContext = SSLContext.getInstance("TLS", "BCJSSE");

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX", "BCJSSE");
			kmf.init(Utils.createClientKeyStore(), Utils.CLIENT_PASSWORD);

			TrustManagerFactory trustMgrFact = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
			trustMgrFact.init(Utils.createServerTrustStore());

			sslContext.init(kmf.getKeyManagers(), trustMgrFact.getTrustManagers(), null);
			SSLSocketFactory fact = sslContext.getSocketFactory();

			System.out.println("open socket ...");
			SSLSocket cSock = (SSLSocket) fact.createSocket(Utils.HOST, Utils.PORT_NO);
			System.out.println("send and receive here ..");
			cSock.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
