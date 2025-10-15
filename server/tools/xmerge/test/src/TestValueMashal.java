package src;


import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

public class TestValueMashal {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	@Test
	public void testValueMarshal()
	{
		String errDB = "test/errDB";
		String LibPath = "../bin";
		File err = new File(errDB);
		Dbx.start(LibPath);		
		Dbx errdb = Dbx.open(err, DatabaseMetaData.getInstance());
		OctetsStream key = new OctetsStream().marshal(0);//所有的errdb的value = 0;
		OctetsStream value = new OctetsStream().marshal(0);//所有的errdb的value = 0;
		Dbx.Table etb =  errdb.openTable("table_int_int");
		etb._insert(key, value);
		etb.close();
		Dbx.stop();
		//重新启动，walk
		Dbx.start(LibPath);
		
		errdb = Dbx.open(err, DatabaseMetaData.getInstance());
		etb =  errdb.openTable("table_int_int");
		
		DbCount dc = new DbCount();
		//etb.walk(dc);会崩的数据
		etb._walk(dc);//不会抛异常的数据
		etb.close();
		Dbx.stop();
		
	}
	
	public class DbCount implements mkdb.Storage.IWalk {
		private long _keycnt = 0;

		//private long _cnt = 0;
		//private final long _checkpoint = 50000;
		public DbCount( )
		{

		}
		@Override
		public boolean onRecord(byte[] key, byte[] data) {
			_keycnt ++;

			return true;
		}
		public long getKeyCnt()
		{
			return _keycnt;
		}

	}
	
}
