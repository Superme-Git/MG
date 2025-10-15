import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import java.net.URLConnection;


public class ServerList {

	private static final String HTTPURL = "http://public.locojoy.com/serverlist.xml";
	
	private static void copyStream( final InputStream is, final OutputStream os) throws Exception {
		byte buffer[] = new byte[1024];
		int size = is.read( buffer);
		while( -1 != size) {
			os.write( buffer, 0, size);
			size = is.read( buffer);
		}
	}
	
	private static void backupOldXmlFile( final String filename) {

		try {
			final InputStream is = new BufferedInputStream( new FileInputStream( filename));
			final OutputStream os = new BufferedOutputStream( new FileOutputStream( filename + ".backup"));
			copyStream( is, os);
			is.close();
			os.close();
		} catch( Exception e) {
		}
	}
	
	public static void main( String args[]) throws Exception {
		
		String filename = "serverlist.xml"; 
		if( args.length > 0)
			filename = args[0];
		
		backupOldXmlFile( filename);

		final URLConnection connection = new URL( HTTPURL).openConnection();
		final InputStream httpis = connection.getInputStream();
		final OutputStream fileos = new BufferedOutputStream( new FileOutputStream( filename));

		copyStream( httpis, fileos);
		
		httpis.close();
		fileos.close();
	}
}
