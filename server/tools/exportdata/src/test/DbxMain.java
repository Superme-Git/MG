package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.goldhuman.Common.Octets;
import com.goldhuman.Common.Marshal.MarshalException;
import com.goldhuman.Common.Marshal.OctetsStream;

import xbean.LocojoyPlatUserInfo;
import xbean.UserInfo;
import xdb.util.DatabaseMetaData;
import xdb.util.Dbx;

public class DbxMain {
	public static class ChargeInfo {
    	public final String locouser;
    	public final Integer userid;
    	public final Integer charge;
    	public ChargeInfo(String a, int b, int c ) {
    		locouser = a;
    		userid = b;
    		charge = c;
    	}
    }
	public static java.util.Map<Integer, Integer> usercharges = new java.util.HashMap<Integer, Integer>();
	public static java.util.Map<String, ChargeInfo> czlocouser = new java.util.HashMap<String, ChargeInfo>();
	
	private static String path = "resultOut.txt";
	private static File outFile = new File(path);
	
	public static void main(String args[]) throws IOException {
		try {
			Scanner in = new Scanner(new File("czmd.txt"));
			while(in.hasNextLine()) {
				String str = in.nextLine();
				String strs[] = str.split(",");
				Integer userid = Integer.parseInt(strs[0]);
				Integer charge = Integer.parseInt(strs[1]);
				usercharges.put(userid, charge);
			}
			in.close();
		} catch (FileNotFoundException e){	
			e.printStackTrace();
		}
		
		Dbx.start(".");
		final Dbx db = Dbx.open(new File("xdb"), DatabaseMetaData.getInstance());
		try {
			{
				final String tableName = "locojoyplatusers";
				walkDumpLocoUsers(db.openTable(tableName), Keytype.eDataMeta, -1);
			}
			
			if(!outFile.exists())
				outFile.createNewFile();
			else {
				outFile.delete();
				outFile.createNewFile();
			}
			
			FileWriter writer = new FileWriter(outFile);
			BufferedWriter bw = new BufferedWriter(writer);			
			
			for(java.util.Map.Entry<String, ChargeInfo> entry : czlocouser.entrySet()) {
				System.out.println(entry.getKey() + "," + entry.getValue());
				bw.write(entry.getKey() + "," + entry.getValue().userid + "," + entry.getValue().charge + "\n");
			}		
			bw.close();
			writer.close();
			
			/*
			{
				final String tableName = "users";
				walkDumpKeys(db.openTable(tableName), Keytype.eDataMeta, -1);
				//browseDumpKeys(db.openTable(tableName), Keytype.eDataMeta, -1);
			}
			*/
			/*
			{
				final String tableName = "locojoyplatusers";
				walkDumpKeys(db.newTable(tableName), Keytype.eString, -1);
				browseDumpKeys(db.newTable(tableName), Keytype.eString, -1);
			}
			*/
		} finally {
			db.close();
		}
	}
	
	static void walkDumpLocoUsers(Dbx.Table table, Keytype eKeytype, int limit) {
		System.out.println("walk on table='" + table.getName() + "':");
		if (eKeytype == Keytype.eDataMeta)
			table.walk(new WalkerDumpLocoUser(table.getMetaData(), limit));
		else
			table.walk(new WalkerDumpkeyWithKeytype(eKeytype, limit));
	}

	static void walkDumpKeys(Dbx.Table table, Keytype eKeytype, int limit) {
		System.out.println("walk on table='" + table.getName() + "':");
		if (eKeytype == Keytype.eDataMeta)
			table.walk(new WalkerDumpkey(table.getMetaData(), limit));
		else
			table.walk(new WalkerDumpkeyWithKeytype(eKeytype, limit));
	}

	static void browseDumpKeys(Dbx.Table table, Keytype eKeytype, int limit) {
		System.out.println("browse on table='" + table.getName() + "':");
		if (eKeytype == Keytype.eDataMeta)
			table.browse(new WalkerDumpkey(table.getMetaData(), limit));
		else
			table.browse(new WalkerDumpkeyWithKeytype(eKeytype, limit));
	}

	static class WalkerDumpkey implements xdb.Storage.IWalk {
		private final DatabaseMetaData.Type keytype;
		private final DatabaseMetaData.Type valuetype;
		private int limit;
		private int count = 0;

		WalkerDumpkey(DatabaseMetaData.Table tableMetaData, int limit) {
			this.keytype = tableMetaData.getKeyType();
			this.valuetype = tableMetaData.getValueType();
			this.limit = limit;
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			if (limit < 0 || ++count < limit) {
				Object key = this.keytype.unmarshal(_key);
				UserInfo value = (UserInfo) this.valuetype.unmarshal(_data);
				System.out.println(key + "," + value.getPlat() + "," + value.getUid());
			}
			return true;
		}
	}

	static class WalkerDumpLocoUser implements xdb.Storage.IWalk {
		private final DatabaseMetaData.Type keytype;
		private final DatabaseMetaData.Type valuetype;
		private int limit;
		private int count = 0;

		WalkerDumpLocoUser(DatabaseMetaData.Table tableMetaData, int limit) {
			this.keytype = tableMetaData.getKeyType();
			this.valuetype = tableMetaData.getValueType();
			this.limit = limit;
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			if (limit < 0 || ++count < limit) {
				Object key = this.keytype.unmarshal(_key);
				LocojoyPlatUserInfo value = (LocojoyPlatUserInfo) this.valuetype.unmarshal(_data);
				if(usercharges.containsKey(value.getUserid()))
					czlocouser.put((String)key, new ChargeInfo((String)key, value.getUserid(), usercharges.get(value.getUserid())));
				//System.out.println(key + "," + value.getUserid() + "," + value.getUserinfoid());
			}
			return true;
		}
	}

	enum Keytype {
		eInt,
		eLong,
		eString,
		eDataMeta,
	}

	static class WalkerDumpkeyWithKeytype implements xdb.Storage.IWalk {
		private Keytype eKeytype;
		private int limit;
		private int count = 0;

		WalkerDumpkeyWithKeytype(Keytype eKeytype, int limit) {
			this.eKeytype = eKeytype;
			this.limit = limit;
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			if (limit < 0 || ++count < limit) {
				OctetsStream os = OctetsStream.wrap(Octets.wrap(_key));
				try {
					switch (eKeytype) {
					case eInt:
						System.out.println(os.unmarshal_int());
						break;
					case eLong:
						System.out.println(os.unmarshal_long());
						break;
					case eString:
						System.out.println(os.unmarshal_String(xdb.Const.IO_CHARSET));
						break;
					default:
						throw new RuntimeException("unkown keytype!");
					}
				} catch (MarshalException e) {
					throw new xio.MarshalError("unmarshal key error.", e);
				}
			}
			return true;
		}
	}
}
