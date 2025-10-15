package com.locojoy.IO.Protocol;

import java.io.File;
import java.net.URL;

public class Rpcgen extends Parser
{
	public Rpcgen(URL newconfig)
	{
		super();
		if ( newconfig!=null )
			setConfig(newconfig);
	}
	public void generate_code() throws Exception
	{
		super.ParseProtocol(null);
		//super.ParseState(null);
		super.ParseRpc(null);
	}
	public static void main(String[] args)
	{
		String filename=null;
		if ( args.length!=0 )
			filename=args[0];
		try {
			Rpcgen gen=new Rpcgen( filename==null ? null :
                        new File(filename).toURI().toURL() );
			gen.generate_code();
		}
		catch ( Exception e ) { e.printStackTrace(); }
	}
}
