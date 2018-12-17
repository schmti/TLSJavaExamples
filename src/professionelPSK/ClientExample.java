package professionelPSK;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

import org.bouncycastle.tls.PSKTlsClient;
import org.bouncycastle.tls.ProtocolVersion;
import org.bouncycastle.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.tls.TlsClientProtocol;
import org.bouncycastle.tls.TlsPSKIdentity;
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

public class ClientExample
{
	public static void main(String[] args) throws Exception
	{
		Socket s = new Socket("localhost", 55555);

		TlsCrypto crypto = new MyTlsCrypto();
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
		return null;
	}

	@Override
	public byte[] getPSK()
	{
		// TODO Auto-generated method stub
		return "password".getBytes();
	}
}

class MyTlsCrypto implements TlsCrypto
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
}
