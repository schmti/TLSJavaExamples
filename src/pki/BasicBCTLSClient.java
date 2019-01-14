package pki;

import java.security.Security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

public class BasicBCTLSClient
{

	public static void main(String[] args)
	{
		Security.addProvider(new BouncyCastleProvider());

		try
		{

			Security.addProvider(new BouncyCastleJsseProvider());
			SSLContext sslContext = SSLContext.getInstance("TLS", "BCJSSE");
			TrustManagerFactory trustMgrFact = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
			trustMgrFact.init(Utils.createServerTrustStore());
			sslContext.init(null, trustMgrFact.getTrustManagers(), null);
			SSLSocketFactory fact = sslContext.getSocketFactory();
			System.out.println("open socket ...");
			SSLSocket cSock = (SSLSocket) fact.createSocket(Utils.HOST, Utils.PORT_NO);

			System.out.println("send and receive here ...");

			cSock.close();
			System.out.println("close Socket");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
