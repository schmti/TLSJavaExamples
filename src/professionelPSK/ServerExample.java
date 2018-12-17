package professionelPSK;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.tls.PSKTlsServer;
import org.bouncycastle.tls.ProtocolVersion;
import org.bouncycastle.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.tls.TlsPSKIdentityManager;
import org.bouncycastle.tls.TlsServerProtocol;
import org.bouncycastle.tls.crypto.TlsCertificate;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.TlsDHConfig;
import org.bouncycastle.tls.crypto.TlsDHDomain;
import org.bouncycastle.tls.crypto.TlsECConfig;
import org.bouncycastle.tls.crypto.TlsECDomain;
import org.bouncycastle.tls.crypto.TlsHMAC;
import org.bouncycastle.tls.crypto.TlsHash;
import org.bouncycastle.tls.crypto.TlsNonceGenerator;
import org.bouncycastle.tls.crypto.TlsSRP6Client;
import org.bouncycastle.tls.crypto.TlsSRP6Server;
import org.bouncycastle.tls.crypto.TlsSRP6VerifierGenerator;
import org.bouncycastle.tls.crypto.TlsSRPConfig;
import org.bouncycastle.tls.crypto.TlsSecret;

public class ServerExample
{
	public static void main(String[] args) throws Exception
	{
		TlsCrypto crypto = new TlsCrypto()
		{
			
			@Override
			public boolean hasSignatureAndHashAlgorithm(SignatureAndHashAlgorithm arg0)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasSignatureAlgorithm(int arg0)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasSRPAuthentication()
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasRSAEncryption()
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasNamedGroup(int arg0)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasMacAlgorithm(int arg0)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasHashAlgorithm(short arg0)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasEncryptionAlgorithm(int arg0)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasECDHAgreement()
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasDHAgreement()
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasAllRawSignatureAlgorithms()
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public SecureRandom getSecureRandom()
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsSecret generateRSAPreMasterSecret(ProtocolVersion arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsSecret createSecret(byte[] arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsSRP6VerifierGenerator createSRP6VerifierGenerator(TlsSRPConfig arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsSRP6Server createSRP6Server(TlsSRPConfig arg0, BigInteger arg1)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsSRP6Client createSRP6Client(TlsSRPConfig arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsNonceGenerator createNonceGenerator(byte[] arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsHash createHash(short arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsHMAC createHMAC(int arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsECDomain createECDomain(TlsECConfig arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsDHDomain createDHDomain(TlsDHConfig arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsCertificate createCertificate(byte[] arg0) throws IOException
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TlsSecret adoptSecret(TlsSecret arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		
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
				return null;
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
