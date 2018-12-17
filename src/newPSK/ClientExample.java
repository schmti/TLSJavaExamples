package newPSK;

import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.tls.PSKTlsClient;
import org.bouncycastle.tls.TlsClientProtocol;
import org.bouncycastle.tls.TlsPSKIdentity;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

public class ClientExample
{
	public static void main(String[] args) throws Exception
	{
		Socket s = new Socket("192.168.178.57", 55555);
		//Socket s = new Socket("localhost", 55555);

		TlsCrypto crypto = new BcTlsCrypto(new SecureRandom());
		TlsPSKIdentity identity = new MyTlsPSKIdentiy();
		PSKTlsClient pskClient = new PSKTlsClient(crypto, identity);
		TlsClientProtocol protocol = new TlsClientProtocol(s.getInputStream(), s.getOutputStream());
		protocol.connect(pskClient);

		System.out.println("Yeaaa");

		protocol.close();
		s.close();

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

