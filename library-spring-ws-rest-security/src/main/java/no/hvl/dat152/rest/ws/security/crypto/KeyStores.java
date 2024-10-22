/**
 * 
 */
package no.hvl.dat152.rest.ws.security.crypto;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;


/**
 * @author tdoy
 *
 */
public class KeyStores {

	
	/**
	 * 
	 * @param keystore
	 * @param alias
	 * @param keystorepassword
	 * @return
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(String keystore, String alias, String keystorepassword) 
			throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		try(InputStream s = new FileInputStream(keystore)){
			keyStore.load(s, keystorepassword.toCharArray());
		}
		catch(Exception e){
			//
		}
		
		PrivateKey key = (PrivateKey) keyStore.getKey(alias,keystorepassword.toCharArray());
		
		return key;
	}
}
