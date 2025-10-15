


import java.io.File;

import java.util.HashMap;
import java.util.Map;

import java.util.Set;


import xbean.Pod;
import xbean.cxxx;
import xbean.varMap;
import xbean.varSet;
import xbean.varXBean;
import xdb.TTable.IWalk;
import xtable.Table_int_xbean;
import xtable.Table_long_map;
import xtable.Table_short_set;

/**
 * 
 */

/**
 * @author Cai Jiacheng
 * @note 只支持ant build.xml运行测试
 */

//@RunWith(Parameterized.class)
public class TestXtransformVerify {

	private String LibPath = "../bin";

	private Map<String, IDoDb>  types = new HashMap<String, IDoDb>();

	final String XML = "test.xml";
	final String SrcDir = "src_db";
	public final String DstDir = "dest_db";

	private String _tablename[];
	private int _keytotal[]; 

	//	@Parameters	
	//	public static Collection data(){
	//		Object[][] object = {
	//			{ 
	//				"table_int_xbean", 100000,
	//				"table_long_map", 105421, 
	//				"table_short_set", 34456
	//			},
	//		};
	//		return Arrays.asList(object);
	//	}

	public TestXtransformVerify( String[] args
	)
	{

		{//添加定义的IDoDB
			types.put("table_int_xbean", new DoTableIntXbean());
			types.put("table_long_map", new DoTableLongMap());
			types.put("table_short_set", new DoTableShortSet());
			types.put("table_autokey", new DoTableAutoKey());
			types.put("table_cbean_cxbean", new DoTableCBeanValue());
			types.put("table_cbean_cxbeanint", new DoTableCBeanKey());
		}
		LibPath = args[args.length-1];
		if( args.length-1 % 2 == 1 )
		{
			throw new Error("args length is not right");
		}

		_tablename = new String[(args.length) / 2];
		_keytotal = new int[(args.length) / 2];

		for( int i=0; i<args.length-1; )
		{
			_tablename[i/2] = args[i++];
			assert(isRegTable(_tablename[i/2]));
			_keytotal[i/2] = Integer.valueOf(args[i++]);

		}


	}




	/**
	 * 较验生成的数据库是否OK
	 * @throws Exception 
	 * @throws Exception
	 */



	public static enum Op {
		WR,
		RD
	};

	private class DoProcedure extends xdb.Procedure
	{

		private int _begin;
		private int _loop;
		private IDoDb _dt = null;
		private Op _op = Op.WR;;
		DoProcedure(int begin, int loop, IDoDb dt, Op op )
		{								
			this._begin = begin;
			this._loop = loop;
			_dt = dt;
			_op = op;
			assert(true);
		}
		protected boolean process() throws Exception {
			if ( _op == Op.WR )
				_dt.write(_begin, _loop );
			else
				_dt.read(_begin, _loop);
			return true;
		}
	}

	interface IDoDb 
	{
		void write( int begin, int loop );
		void read(int begin, int loop);
	}


	class DoTableCBeanKey implements IDoDb
	{		
		public DoTableCBeanKey()
		{

		}

		public void write(int begin, int loop) {


		}


		@Override
		public void read(int begin, int loop) {
			int end = begin + loop;
			for (Integer i = begin; i < end; i++) {
				cxxx ck = new cxxx(i,"", i);
				xbean.cxbean_int value = xtable.Table_cbean_cxbeanint.get(ck);

				assert(value != null);

				assert(value.getA() == i);


			}


			return;
		}
	}

	class DoTableCBeanValue implements IDoDb
	{		
		public DoTableCBeanValue()
		{

		}

		public void write(int begin, int loop) {


		}


		@Override
		public void read(int begin, int loop) {

			int end = begin + loop;
			for (Integer i = begin; i < end; i++) {					
				cxxx ck = new cxxx(i,"", i);
				xbean.cxbean value = xtable.Table_cbean_cxbean.get(ck);
				assert(value != null);
				//assert(value.getA() == i);
				cxxx vc = value.getB();
				assert ( vc.getIid() == i ); 
				assert ( vc.getEtype() == i);

				//				
				//				value.setA(i);
				//				value.setB(new cxxx(i+1, i+1));
				//								
				//				xtable.Table_cbean_cxbean.add(ck, value);
				//				
			}
		}
	}


	class DoTableAutoKey implements IDoDb
	{		
		public void write(int begin, int loop) {

			throw new Error("not support");

		}


		@Override
		public void read(int begin, int loop) {

			int end = begin + loop;

			for (Integer i = begin; i < end; i++) {

				//	assert( xtable.Table_autokey.get( (long)i ).equals(i.toString()));
				//	xtable.Table_autokey.
				xtable.Table_autokey.insert(i.toString());
			}
		}
	}

	class DoTableIntXbean implements IDoDb
	{		
		public void write(int begin, int loop) {

			throw new Error("not support");
		}

		@Override
		public void read(int begin, int loop) {

			int end = begin + loop;

			for (Integer i = begin; i < end; i++) {

				varXBean vb = Table_int_xbean.get(i);
				assert(vb != null);
				//<variable name="vint" type="int"/>//测试用，不可改变
				//assertEquals(vb.getVint(), i);

				assert(vb.getVint() == i);

				//vyyy
				xbean.yyy y = vb.getVyyy();

				Set<Integer> sy = y.getA();

				//assertTrue(sy.contains(i));
				assert(sy.contains(i));
				//				for( Integer j = 0; j < i; j++ )
				//					assertTrue(sy.contains(j));
			}
		}
	}

	class DoTableLongMap implements IDoDb
	{		
		public void write(int begin, int loop) {

			throw new Error("not support");
		}

		@Override
		public void read(int begin, int loop) {

			int end = begin + loop;

			for (Integer i = begin; i < end; i++) {

				varMap mi = Table_long_map.get(i.longValue());
				//assertTrue(mi.getV().containsKey(i));
				assert(mi.getV().containsKey(i));
				assert(mi.getV().get(i).equals(i));

				assert(mi.getV1().containsKey(i));
				assert(mi.getV1().get(i).equals(i));
				
				assert(mi.getV2().containsKey(i));
				assert(mi.getV2().get(i).equals(i));
				
				assert(mi.getV3().contains(i));
				assert(mi.getV4().contains(i));
				
				assert(mi.getV7().contains(i.longValue()));
				assert(mi.getV8().contains(i.longValue()));
				
				assert(mi.getC().equals("aaa"));
				//assertEquals(mi.getV().get(i), i);
				//				for( Integer j = 0; j < i ; j++ )
				//				{
				//					assertTrue(mi.getV().containsKey(j));
				//					assertEquals(mi.getV().get(j), i);
				//				}				
			}
		}
	}


	class DoTableShortSet implements IDoDb
	{		
		public void write(int begin, int loop) {
			throw new Error("not support");
		}

		@Override
		public void read(int begin, int loop) {

			int end = begin + loop;

			for (int i = begin; i < end; i++) {

				varSet si = Table_short_set.get((short)i);

				//assertTrue(si.getV().contains(i));
				assert(si.getV().contains(i));

				//				for( Integer j = 0; j < i ; j++ )
				//				{
				//					assertTrue(si.getV().contains(j));
				//				}				
			}
		}
	}

	public void creatDb() throws Exception
	{
		throw new Error("not support");

	}

	public void verifyDb(String db) throws Exception
	{
		xdb.XdbConf Conf = new xdb.XdbConf(XML);
		Conf.setDbhome(new File(db));
		Conf.setLibdbPath(new File(LibPath));
		xdb.Xdb.getInstance().setConf(Conf);		
		xdb.Xdb.getInstance().start();




		int perOper = 4096;

		for( Integer num = 0; num < _tablename.length; num ++ )
		{
			if ( _tablename[num].equals("table_autokey") )
			{
				System.out.print("Talbe_autokey Test: .........\n");
				AutoKeyWalk aw = new AutoKeyWalk();

				xtable.Table_autokey.getTable().walk(aw);

				assert(aw.getKeyCount() ==(long)_keytotal[num]);

			}
			for ( Integer i = 0 ; i < _keytotal[num]; i+=perOper )
			{
				new DoProcedure( i, (i+perOper >= _keytotal[num]? _keytotal[num]-i : perOper), types.get(_tablename[num]), Op.RD ).submit().get();
			}
		}
		xdb.Xdb.getInstance().stop();
	}

	class AutoKeyWalk implements xdb.TTable.IWalk<Long, String>
	{


		private long cnt = 0;
		@Override
		public boolean onRecord(Long k, String v) {

			String value = "1234";
			assert(v.equals(value));
			cnt ++;
			return true;
		}
		public long getKeyCount()
		{
			return cnt;
		}

	}

	private void clearDirs()
	{
		//清掉之前用例生成的数据库

		File src = new File(SrcDir);
		File dst = new File(DstDir);
		File backup = new File("xbackup");//不从xml读了，麻烦

		if ( !src.exists() )
		{
			src.mkdir();
			new File(SrcDir+"//dbdata").mkdir();
			new File(SrcDir+"//dblogs").mkdir();
		}
		if ( !dst.exists() )
		{
			dst.mkdir();
			new File(DstDir+"//dbdata").mkdir();
			new File(DstDir+"//dblogs").mkdir();
		}

		if ( !backup.exists() )
			backup.mkdir();

		deleteDir(src);
		deleteDir(dst);
	}

	private boolean isRegTable(String name)
	{
		if ( types.get(name) == null )
			return false;
		return true;
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
