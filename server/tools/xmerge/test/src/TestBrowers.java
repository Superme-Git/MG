package src;

import java.io.File;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

public class TestBrowers {

	final static String SrcDir = "test/SrcTmp";
	final static String DstDir = "test/DstTmp";

	final static String XML = "test/test.xml";
	final static String LibPath = "../bin";
	//static mkdb.XdbConf Conf = XmergeTestSuite.Conf;
	final static mkdb.XdbConf Conf = new mkdb.XdbConf(XML);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public static void main(String args[]) {
		new TestBrowers().testBrowers();
	}

	Random rd;

	@Test
	public void testBrowers() {
		//建立src目录下的table_int_int
		long seed = 1253089314675L; // System.currentTimeMillis();
		System.err.println("Random.seed=" + seed);
		rd = new Random(seed);

		this.clearDirs();

		Conf.setDbhome(new File(SrcDir));
		Conf.setLibdbPath(new File(LibPath));
		mkdb.Xdb.getInstance().setConf(Conf);
		mkdb.Trace.set(mkdb.Trace.DEBUG);
		mkdb.Xdb.getInstance().start();
		int srckeytotal = 50000;

		int perOper = 4096;
		for (Integer i = 0; i < srckeytotal; i += perOper) {
			new DoProcedure(i, (i + perOper >= srckeytotal ? srckeytotal - i : perOper)).call();
		}
		mkdb.Xdb.getInstance().stop();

		Conf.setCheckpointPeriod(0);
		//建立dst目录下的table_int_int
		Conf.setDbhome(new File(DstDir));
		mkdb.Xdb.getInstance().setConf(Conf);
		mkdb.Trace.set(mkdb.Trace.DEBUG);
		mkdb.Xdb.getInstance().start();

		int begin = 25000;
		int end = srckeytotal + begin;
		for (Integer i = begin; i < end; i += perOper) {
			new DoProcedure(i, (i + perOper >= end ? end - i : perOper)).call();
		}
		mkdb.Xdb.getInstance().stop();
		//开始进行打开操作
		Dbx.start(LibPath);

		Dbx sdb = Dbx.open(new File(SrcDir), DatabaseMetaData.getInstance());
		Dbx ddb = Dbx.open(new File(DstDir), DatabaseMetaData.getInstance());

		for (int i = 0; i < 100; i++) {
			Dbx.Table stb = sdb.openTable("table_int_int");
			Dbx.Table dtb = ddb.openTable("table_int_int");

			//System.out.println(i);

			Walker walker = new Walker(dtb);
			//stb._walk(walker); //正常通过 
			stb._browse(walker); //中间会卡死
			walker.finish();

			stb.close(); //_browse情况下，注释掉close语句后就不会卡死.
			dtb.close(); //怀疑checkpoint会对_brower产生影响
		}

	}

	public class Walker implements mkdb.Storage.IWalk {

		Dbx.Table _dtb = null;
		OctetsStream value = null;

		Walker(Dbx.Table dtb) {
			//this.tableMetaData = tableMetaData;
			_dtb = dtb;
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {

			OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
			value = _dtb._find(key);

			return true;
		}

		public void finish() {

		}

	}

	private class DoProcedure extends mkdb.Procedure {

		private int _begin;
		private int _loop;

		DoProcedure(int begin, int loop) {
			this._begin = begin;
			this._loop = loop;
		}

		protected boolean process() throws Exception {

			int end = _begin + _loop;
			for (Integer i = _begin; i < end; i++)
				Table_int_int.add(i, rd.nextInt());
			return true;
		}
	}

	private void clearDirs() {
		//清掉之前用例生成的数据库

		File src = new File(SrcDir);
		File dst = new File(DstDir);

		if (!src.exists()) {
			src.mkdir();
			new File(SrcDir + "//dbdata").mkdir();
			new File(SrcDir + "//dblogs").mkdir();
		}
		if (!dst.exists()) {
			dst.mkdir();
			new File(DstDir + "//dbdata").mkdir();
			new File(DstDir + "//dblogs").mkdir();
		}

		deleteDir(src);
		deleteDir(dst);
	}

	private static void deleteDir(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return; // 检查参数 
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件 
			else if (file.isDirectory())
				deleteDir(file); // 递规的方式删除文件夹 
		}
		// dir.delete();// 删除目录本身 
	}

}
