package mkio;

import mkio.security.HmacMd5Hash;
import mkio.security.MD5Hash;
import mkio.security.Security;

import com.locojoy.base.Octets;

public class HelperSecurity 
{
	final public static int SIZE_MD5 = 16;
	final public static int SIZE_CHALLENGE = 16;
	
	public static void makePassword(Octets username, Octets password, Octets key)
	{
		Security md5 = new MD5Hash();							
		md5.doUpdate(username);
		md5.doUpdate(password);				
		key = md5.doFinal(key);
		key.resize(SIZE_MD5);
	}
	
	public static void generateKeyByPassword(Octets identity, Octets password, Octets nonce, Octets key)
	{		
		Security hmac = new HmacMd5Hash();
		hmac.setParameter(password);
		hmac.doUpdate(identity);
		hmac.doUpdate(nonce);
		hmac.doFinal(key);
		key.resize(SIZE_MD5);
	}
	
	public static void randomUpdate(Octets random)
	{
		random.resize(SIZE_CHALLENGE);
		java.util.Random ran = new java.util.Random();
		ran.setSeed((new java.util.Date()).getTime());
		
		for (int i = 0; i < random.size(); ++i)
		{
			random.setByte(i, (byte) (ran.nextLong() & 0XFF));
		}
	}
}
