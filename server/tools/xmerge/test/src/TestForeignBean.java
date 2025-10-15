package src;

import java.io.File;

import org.junit.Test;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import validator.ForeignBeanDB;
import validator.ForeignBeanDB.ForeignBeanTable;
import validator.ForeignBeanKey;
import validator.ForeignBeanValue;
import xmerge.Util;

public class TestForeignBean {
	private DatabaseMetaData curMeta;
	private ForeignBeanDB vdb;

	@Test
	public void testKey() {
		curMeta = DatabaseMetaData.getInstance();
		Dbx.start("./test");
		vdb = new ForeignBeanDB("./test/fdb");
		ForeignBeanTable table = vdb.openTable("test", 128, 128);

		ForeignBeanKey key1 = new ForeignBeanKey("fint", Util.allocateNewKey(curMeta.getType("int")));
		ForeignBeanKey key2 = new ForeignBeanKey("fint", Util.allocateNewKey(curMeta.getType("int")));
		ForeignBeanKey key3 = new ForeignBeanKey("fint", key1.getKey());
		ForeignBeanKey key4 = new ForeignBeanKey("fint", Util.allocateNewKey(curMeta.getType("int")));
		System.out.println(key1 + ":" + key2 + ":" + key3);
		System.out.println(key1.equals(key2));
		System.out.println(key1.equals(key3));
		System.out.println(key1.compareTo(key2));
		System.out.println(key1.compareTo(key3));

		ForeignBeanValue value1 = new ForeignBeanValue();
		System.out.println(value1.addKey(key1));
		System.out.println(value1.addKey(key2));
		System.out.println(value1.addKey(key3));
		System.out.println(value1.getKeySet());

		System.out.println(table.insert(key1, value1));
		System.out.println(table.find(key1));
		System.out.println(table.find(key3));

		ForeignBeanValue value2 = table.find(key1);
		System.out.println(value2.addKey(key4));
		System.out.println(key4);
		System.out.println(value1);
		System.out.println(table.replace(key4, value1));
		System.out.println(table.find(key1));
		System.out.println(table.find(key4));

		System.out.println(value2);
		System.out.println(table.replace(key1, value2));
		System.out.println(table.find(key1));
		System.out.println(table.find(key3));
		System.out.println(table.find(key4));

		Dbx sdb = Dbx.open(new File("./test/src_db"), curMeta);
		OctetsStream os1 = sdb.openTable("fboolean").firstKey();
		OctetsStream os2 = sdb.openTable("bboolean").firstKey();
		ForeignBeanKey k1 = new ForeignBeanKey("fboolean", os1);
		ForeignBeanKey k2 = new ForeignBeanKey("fboolean", os2);
		System.out.println(k1 + ":" + k2);
		System.out.println(key1.equals(key2));
		System.out.println(table.insert(k1, value1));
		System.out.println(table.find(k1));
		System.out.println(table.find(k2));
		System.out.println(table.insert(k2, value1));
		System.out.println(value1.addKey(k1));
		System.out.println(value1);
		System.out.println(value1.addKey(k2));
		System.out.println(value1);
	}

	@Test
	public void testFind() {
		curMeta = DatabaseMetaData.getInstance();
		Dbx.start("./test");
		vdb = new ForeignBeanDB("./test/dest_db");
		@SuppressWarnings("unused")
		ForeignBeanTable table = vdb.openTable("foreigntable", 128, 128);

		Dbx db = Dbx.open(new File("./test/dest_db"), curMeta);

		Dbx.Table table1 = db.openTable("vlong");
		OctetsStream v1 = table1.find(new OctetsStream().marshal(79682431L));
		long lkey = 79682431;
		System.out.println(table1.exist(new OctetsStream().marshal(lkey)));
		Object value1 = curMeta.getTable("vlong").getValueType().unmarshal(v1);
		System.out.println(value1);

		Dbx.Table table2 = db.openTable("fshort");
		OctetsStream v2 = table2.find(new OctetsStream().marshal(Short.valueOf("8631")));
		Object value2 = curMeta.getTable("fshort").getValueType().unmarshal(v2);
		System.out.println(value2);
	}

	@Test
	public void testWalk() {
		curMeta = DatabaseMetaData.getInstance();
		Dbx.start("./test");
		vdb = new ForeignBeanDB("./test/fdb");
		ForeignBeanTable table = vdb.openTable("foreigntable", 128, 128);
		Walker walker = new Walker();
		table.walk(walker);
	}

	class Walker implements mkdb.Storage.IWalk {
		public boolean onRecord(byte[] _key, byte[] _data) {
			ForeignBeanKey key = new ForeignBeanKey();
			key.unmarshal(OctetsStream.wrap(Octets.wrap(_key)));
			ForeignBeanValue value = new ForeignBeanValue();
			value.unmarshal(OctetsStream.wrap(Octets.wrap(_data)));
			mkdb.Trace.info("ForeignDB key=" + key + " value=" + value);
			return true;
		}
	}
}