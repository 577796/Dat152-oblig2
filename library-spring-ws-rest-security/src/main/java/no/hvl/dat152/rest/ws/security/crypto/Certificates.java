/**
 * 
 */
package no.hvl.dat152.rest.ws.security.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author tdoy
 *
 */
public class Certificates {

	/**
	 * Given a certificate, extract the public key for operations such as encryption/signature
	 */
	
	/**
	 * Client side public key methods
	 * @param certfile
	 * @return
	 */
	public static PublicKey getPublicKey(String certfile) {
		
		X509Certificate certificate = null;
		try {
			FileInputStream input = new FileInputStream(certfile);
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			certificate = (X509Certificate)f.generateCertificate(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		
		return certificate.getPublicKey();		
	
	}

}
