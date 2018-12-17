package professionelPSK;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.tls.PSKTlsServer;
import org.bouncycastle.tls.TlsPSKIdentityManager;
import org.bouncycastle.tls.TlsServerProtocol;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

public class ServerExample
{
	public static void main(String[] args) throws Exception
	{
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
		
		PSKTlsServer server = new PSKTlsServer(crypto,manager);
		
		ServerSocket ss = new ServerSocket(55555);
		Socket s = ss.accept();
		
		TlsServerProtocol protocol = new TlsServerProtocol(s.getInputStream(), s.getOutputStream());
		
		protocol.accept(server);
		
		System.out.println("Yeaaa");
		
		protocol.close();
		s.close();
		ss.close();
	}
}
