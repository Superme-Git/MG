package src;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import xmerge.CheckDupKey;
import xmerge.ICheckDupKey;
import xmerge.Merger;
import xmerge.Repair;
import xmerge.XmergeConf;

/**
 * @note: 测试用例会先生成指定的数据库，然后调用testCheck()执行相应测试。
 * 			指定的数据库参数由Collection()的参数设置。具体见说明。
 */
@RunWith(Parameterized.class)
public class TestXmergeNotRef {

	static String SrcDir = "test/SrcTmp";
	static String DstDir = "test/DstTmp";
	static String ResultDir = "test/resultDB";
	static String XML = "test/test.xml";
	static String XMergeXML = "test/xmerge.xml";
	static String LibPath = "../bin";
	static String Foreigndb = "test/fdb";
	//static mkdb.XdbConf Conf = XmergeTestSuite.Conf;
	static mkdb.XdbConf Conf = new mkdb.XdbConf(XML);


	////////---重复建表太耗时间了保存个参数进行判断---////////
	static int _srckeytotal1; 
	static int _dstkeytotal1; 
	static int _dupkey1;
	static String _tablename1;

	static int _srckeytotal2; 
	static int _dstkeytotal2; 
	static int _dupkey2;
	static String _tablename2;

	static int _srckeytotal3; 
	static int _dstkeytotal3; 
	static int _dupkey3;
	static String _tablename3;

	static int _srckeytotal4; 
	static int _dstkeytotal4; 
	static int _dupkey4;
	static String _tablename4;

	static int _srckeytotal5; 
	static int _dstkeytotal5; 
	static int _dupkey5;
	static String _tablename5;
	////////------////////

	//把可以处理的表关联到map中
	private Map<String, IDoTable>  types = null;


	/** 暂时支持5种表同时建立生成，即所有注册的表。不支持参数数组[]，比较麻烦
	 * @param srctotalx : 源数据库表的数据条目
	 * @param dsttotalx : 目的数据库表的数据条目
	 * @param dupkeyx : 两个数据库表的重复条目
	 * @param tablenamex : 数据库表名
	 * 定义格式：
	 * {
	 * 		srckeytotal, int dstkeytotal, int dupkey, String tablename,
	 * 		.....
	 * },
	 * {
	 * 		srckeytotal, int dstkeytotal, int dupkey, String tablename,
	 * 		.....
	 * }
	 * 
	 */

	@SuppressWarnings({ "rawtypes" })
	@Parameters	
	public static Collection data(){
		Object[][] object = {

				//0  简单 测试，src表,dst表中的所有数据相同
				{ 
					1000, 1000, 1000, "table_int_int",
					1000, 1000, 1000, "table_string_string",
					1000, 1000, 1000, "table_int_xbean",
					1000, 1000, 1000, "table_long_map",
					1000, 1000, 1000, "table_short_set",
				},
				//1  测试大数据量情况.很耗时间，建大表。 > 1000000时， windows checkpoint会出错，还是小点吧
//				{ 
//					500000, 500000, 200000, "table_int_int",
//					500000, 500000, 200000, "table_string_string",
//					500000, 500000, 200000, "table_int_xbean",
//					500000, 500000, 200000, "table_long_map",
//					30000, 30000, 500, "table_short_set", //short key<= Short.MaxValue 32767, srctotal + dsttotal < 65535
//				},

				//2  测试dst中没任何数据的情况
				{ 
					50000, 0, 0, "table_int_int",
					50000, 0, 0, "table_string_string",
					50000, 0, 0, "table_int_xbean",
					50000, 0, 0, "table_long_map",
					20000, 0, 0, "table_short_set",
				},

				//3  测试src中没任何数据的情况
				{ 
					0, 50000, 0, "table_int_int",
					0, 50000, 0, "table_string_string",
					0, 50000, 0, "table_int_xbean",
					0, 50000, 0, "table_long_map",
					0, 20000, 0, "table_short_set",
				},

				//4 测试少量dupkey的情况
				{ 
					50000, 50000, 1, "table_int_int",
					50000, 50000, 1, "table_string_string",
					50000, 50000, 1, "table_int_xbean",
					50000, 50000, 1, "table_long_map",
					10000, 10000, 1, "table_short_set",
				},

				//5 测试src数据>>dst数据的情况
				{ 
					50000, 50, 1, "table_int_int",
					50000, 50, 2, "table_string_string",
					50000, 50, 3, "table_int_xbean",
					50000, 50, 4, "table_long_map",
					20000, 30, 5, "table_short_set",
				},

				//6 测试src数据>>dst数据的情况
				{ 
					50, 50000, 1, "table_int_int",
					50, 50000, 2, "table_string_string",
					50, 50000, 3, "table_int_xbean",
					50, 50000, 4, "table_long_map",
					30, 20000, 5, "table_short_set",
				},

				//7 测试正常点的数据的情况
				{ 
					45678, 12345, 1235, "table_int_int",
					75894, 64587, 2458, "table_string_string",
					78545, 68745, 2664, "table_int_xbean",
					78954, 68715, 4578, "table_long_map",
					12345, 14567, 1111, "table_short_set",
				},

				//8 测试没有任何重复的key
				{ 
					50000, 50000, 0, "table_int_int",
					50000, 50000, 0, "table_string_string",
					50000, 50000, 0, "table_int_xbean",
					50000, 50000, 0, "table_long_map",
					10000, 10000, 0, "table_short_set",
				},



		};
		return Arrays.asList(object);
	}



	public TestXmergeNotRef( 
			int srckeytotal1, int dstkeytotal1, int dupkey1, String tablename1,
			int srckeytotal2, int dstkeytotal2, int dupkey2, String tablename2,
			int srckeytotal3, int dstkeytotal3, int dupkey3, String tablename3,
			int srckeytotal4, int dstkeytotal4, int dupkey4, String tablename4,
			int srckeytotal5, int dstkeytotal5, int dupkey5, String tablename5			
	)
	{

		//--------暂时把注册的放这里吧，只支持以下表的定义-------------
		if ( null == types )
		{
			types = new HashMap<String, IDoTable>();
			types.put("table_int_int", new DoTableIntInt());
			types.put("table_string_string", new DoTableStringString());
			types.put("table_int_xbean", new DoTableIntXbean());
			types.put("table_long_map", new DoTableLongMap());
			types.put("table_short_set", new DoTableShortSet());
		}
		//-------------------------------------------

		assertTrue(this.isRegTable(tablename1));
		assertTrue(this.isRegTable(tablename2));
		assertTrue(this.isRegTable(tablename3));
		assertTrue(this.isRegTable(tablename4));
		assertTrue(this.isRegTable(tablename5));

		//当两次有参数变动的时候，这时整个mkdb数据库进行重建

		types.get(tablename1).init(srckeytotal1, dstkeytotal1, dupkey1);
		types.get(tablename2).init(srckeytotal2, dstkeytotal2, dupkey2);
		types.get(tablename3).init(srckeytotal3, dstkeytotal3, dupkey3);
		types.get(tablename4).init(srckeytotal4, dstkeytotal4, dupkey4);
		types.get(tablename5).init(srckeytotal5, dstkeytotal5, dupkey5);

		if ( _srckeytotal1 != srckeytotal1 || 
				_dstkeytotal1 != dstkeytotal1 ||
				_dupkey1 != dupkey1 ||
				_tablename1 != tablename1 ||
				_srckeytotal2 != srckeytotal2 || 
				_dstkeytotal2 != dstkeytotal2 ||
				_dupkey2 != dupkey2 ||
				_tablename2 != tablename2 ||			 
				_srckeytotal3 != srckeytotal3 || 
				_dstkeytotal3 != dstkeytotal3 ||
				_dupkey3 != dupkey3 ||
				_tablename3 != tablename3 ||
				_srckeytotal4 != srckeytotal4 || 
				_dstkeytotal4 != dstkeytotal4 ||
				_dupkey4 != dupkey4 ||
				_tablename4 != tablename4 ||
				_srckeytotal5 != srckeytotal5 || 
				_dstkeytotal5 != dstkeytotal5 ||
				_dupkey5 != dupkey5 ||
				_tablename5 != tablename5 			 			 
		)
		{
			this.clearDirs();		

			types.get(tablename1).filldata();
			types.get(tablename2).filldata();
			types.get(tablename3).filldata();
			types.get(tablename4).filldata();
			types.get(tablename5).filldata();

			_srckeytotal1 = srckeytotal1; 
			_dstkeytotal1 = dstkeytotal1;
			_dupkey1 = dupkey1;
			_tablename1 = tablename1;
			_srckeytotal2 = srckeytotal2; 
			_dstkeytotal2 = dstkeytotal2;
			_dupkey2 = dupkey2;
			_tablename2 = tablename2 ;			 
			_srckeytotal3 = srckeytotal3; 
			_dstkeytotal3 = dstkeytotal3;
			_dupkey3 = dupkey3;
			_tablename3 = tablename3;
			_srckeytotal4 = srckeytotal4; 
			_dstkeytotal4 = dstkeytotal4;
			_dupkey4 = dupkey4;
			_tablename4 = tablename4;
			_srckeytotal5 = srckeytotal5; 
			_dstkeytotal5 = dstkeytotal5;
			_dupkey5 = dupkey5;
			_tablename5 = tablename5;			 
		}



	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Conf = new mkdb.XdbConf(XML);//conf只能new一次，真是无语了
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		XmergeConf.SetConf(XMergeXML);


		XmergeConf.getInstance().setSrcDbHome(SrcDir);
		XmergeConf.getInstance().setDestDbHome(DstDir);
		XmergeConf.getInstance().setResultDbHome(ResultDir);
		XmergeConf.getInstance().setForeignInfoDbHome(Foreigndb);
		//this.addType("Table_int_int", new DoTableIntInt(0,0));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCheck() throws Exception
	{

		Dbx.start(LibPath);
		Dbx errdb = null;
		try{

			ICheckDupKey check= new CheckDupKey();

			check.setDupKeyDBPath("test/errDB");
			//模拟调用检测的过程
			if ( !check.checkDB(SrcDir, DstDir) )
			{
				for ( String tablename : types.keySet() )
				{	//Check == false，说明生成的数据库DupKeyCnt = 0，没有重复的key										
					assertTrue(types.get(tablename).getDupKeyCnt() == 0);
				}	
				return;
			}

			assertTrue(check.checkDB(SrcDir, DstDir));

			//打开errDb数据库，模拟修复的读取过程。

			errdb = Dbx.open(new File(check.getDupKeyDBPath()), DatabaseMetaData.getInstance());

			for ( String tablename : types.keySet() )
			{
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )			
				{	//如果有knownConflict的规则
					//assertEquals( check.getDupKeyCount(tablename), 0 );
					assertTrue( check.getDupKeyCount(tablename) == 0 );
					continue;
				}
				//判断check出来的数据是否与指定的dupkey相同				
				assertEquals(check.getDupKeyCount(tablename), types.get(tablename).getDupKeyCnt());
				//判断一下errDb中，生成的errDb表是否与check出来的dupkey相同
				Dbx.Table etb = errdb.openTable(tablename);
				DbCount dc = new DbCount();
				etb.walk(dc);
				//etb.browse(dc);
				etb.close();
				assertEquals(check.getDupKeyCount(tablename), dc.getKeyCnt());
			}
		}finally
		{
			if ( errdb != null )
				errdb.close();
			Dbx.stop();
		}


		//System.out.print("testCheck oK!!\n");

	}


	@Test
	public void testCheckRepair() throws Exception
	{

		Dbx.start(LibPath);
		Dbx errdb = null;
		try{

			ICheckDupKey check= new CheckDupKey();

			check.setDupKeyDBPath("test/errDB");
			//模拟调用检测的过程
			if ( !check.checkDB(SrcDir, DstDir) )
			{
				for ( String tablename : types.keySet() )
				{	//Check == false，说明生成的数据库DupKeyCnt = 0，没有重复的key										
					//assertEquals(types.get(tablename).getDupKeyCnt(), 0);
					assertTrue(types.get(tablename).getDupKeyCnt()== 0);
				}			
			}

			String srctmp = DstDir+"_tmp";
			xmerge.Util.copyFolder(DstDir, srctmp);


			//修复srctmp			
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());
			Repair worker = new Repair();
			assertTrue(worker.repairDb());

			//重新检测是否有dupkey的
			//check.setDupKeyDBPath("test/errDB");
			assertFalse(check.checkDB(worker.getResultDbHome(), SrcDir));

			//重新生成的errDB里面所有的DupKeyCount应该都为0
			errdb = Dbx.open(new File(check.getDupKeyDBPath()), DatabaseMetaData.getInstance());

			for ( String tablename : types.keySet() )
			{											
				//assertEquals(check.getDupKeyCount(tablename), 0);
				assertTrue(check.getDupKeyCount(tablename)== 0);
				//判断一下errDb中，生成的errDb表是否都是0
				Dbx.Table etb = errdb.openTable(tablename);
				DbCount dc = new DbCount();
				etb.walk(dc);
			//	etb.browse(dc);
				etb.close();
				assertEquals(check.getDupKeyCount(tablename), dc.getKeyCnt());
				dc = null;
			}
		}finally
		{			
			if( errdb != null )
				errdb.close();
			Dbx.stop();
		}
	}


	@Test
	public void testMergerCheckRepair() throws Exception
	{

		XmergeConf.SetConf(XMergeXML);
		Dbx.start(LibPath);		
		Dbx resultdb = null;
		Merger merger = new Merger();
		try{

			merger.SetSrcDB(SrcDir);
			merger.SetDestDB(DstDir, false);//由merger负责拷贝

			if(merger.doMerge()){
				for ( String tablename : types.keySet() )
				{	
					if ( XmergeConf.getInstance().isKnownConflict(tablename) )//应该不能加这个的，先放着			
					{	
						//如果有knownConflict的规则.直接pass						
						continue;
					}
					//Merger成功，说明生成的数据库DupKeyCnt = 0，没有重复的key										
					//assertEquals(types.get(tablename).getDupKeyCnt(), 0);
					assertTrue(types.get(tablename).getDupKeyCnt() == 0);
				}
				return;
			}

			ICheckDupKey check= new CheckDupKey();
			check.setDupKeyDBPath("test/errDB");
			//必然有dupkey才会执行到这里
			assertTrue(check.checkDB(SrcDir, DstDir));

			//拷贝Src目录			
			String srctmp = DstDir+"_tmp";

			xmerge.Util.copyFolder(DstDir, srctmp);

			//开始修复
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());
			Repair worker = new Repair();
			assertTrue( worker.repairDb());
			//再次合并.
			//再次check，确定没有DupKey
			assertFalse(check.checkDB(worker.getResultDbHome(), SrcDir));

			merger.SetSrcDB(SrcDir);
			merger.SetDestDB(worker.getResultDbHome(), true);//直接merger在DestDB上，不需要拷贝
			//修复完合并还失败???
			//merger.benchMerge(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance().getTable("table_string_string"));
			assertTrue(merger.doMerge());

			//判断一下合并是否是正确
			//merger.getResultDbHome();
			resultdb = Dbx.open(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance());

			for ( String tablename : types.keySet() )
			{	//Merger成功，说明生成的数据库DupKeyCnt = 0，没有重复的key										
				//assertEquals(types.get(tablename).getSrcKeyCnt(), 0);
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )//应该不能加这个的，先放着			
				{	//如果有knownConflict的规则.规则弄完的DB库是否是唯一Key??		
					//verify the table_short_set
					if ( tablename.equals("table_short_set"))
					{
						Dbx.Table shorttb = resultdb.openTable(tablename);
						WalkTableShortSet ws = new WalkTableShortSet( types.get(tablename).getDupkeyset() );						
						shorttb.walk(ws);
						shorttb.close();
					}
					continue;
				}
				Dbx.Table etb = resultdb.openTable(tablename);
				DbCount dc = new DbCount();
				etb.walk(dc);
				etb.close();
			
				//判断一下合并的结果是否正确
				assertEquals((long)(types.get(tablename).getSrcKeyCnt()+types.get(tablename).getDstKeyCnt()), dc.getKeyCnt());
				dc = null;
			}

		}finally
		{			
			Dbx.stop();
		}
		Conf.setDbhome(new File(merger.getResultDBDir()));
		mkdb.Xdb.getInstance().setConf(Conf);
		mkdb.Trace.set(mkdb.Trace.WARN);
		mkdb.Xdb.getInstance().start();
		mkdb.Xdb.getInstance().stop();

	}
	/*------------------辅助类--------------------------*/

	/**
	 * 处理table_int_int表，用于填充相应的数据。
	 */		
	class DoTableIntInt extends IDoTable
	{
		@Override
		public void write(int begin, int loop) {
			int end = begin + loop;
			Random rd = new Random(System.currentTimeMillis());			
			for ( Integer i = begin; i < end; i++ )
				Table_int_int.add(i, rd.nextInt());
		}

	}

	/**
	 * 处理table_int_xbean表，用于填充相应的数据。
	 */		
	class DoTableIntXbean extends IDoTable
	{
		@Override
		public void write(int begin, int loop) {
			int end = begin + loop;
			Random rd = new Random(System.currentTimeMillis());			
			for (Integer i = begin; i < end; i++) {					
				varXBean vb = Pod.newvarXBean();
				vb.setVint(rd.nextInt());

				vb.setVstring(Integer.toString(rd.nextInt()));					
				Set<Integer> si = vb.getVset();									
				Map<Integer, Integer> mi = vb.getVmap();													
				mi.put(i, rd.nextInt());
				si.add(rd.nextInt());
				Table_int_xbean.add(i, vb);
			}
		}

	}

	/**
	 * 处理table_long_map表，用于填充相应的数据。
	 */		
	class DoTableLongMap extends IDoTable
	{
		@Override
		public void write(int begin, int loop) {
			int end = begin + loop;
			Random rd = new Random(System.currentTimeMillis());			
			for ( Integer i = begin; i < end; i++ )
			{
				varMap vm = Pod.newvarMap();
				Map<Integer, Integer> mi = vm.getV();								
				mi.put(i, rd.nextInt());
				Table_long_map.add( (long)i, vm);
			}

		}

	}

	/**
	 * 处理table_short_set表，用于填充相应的数据。
	 */		
	class DoTableShortSet extends IDoTable
	{
		@Override
		public void write(int begin, int loop) {
			int end = begin + loop;
			@SuppressWarnings("unused")
			Random rd = new Random(System.currentTimeMillis());			
			for ( Integer i = begin; i < end; i++ )
			{
				varSet vs = Pod.newvarSet();
				Set<Integer> si = vs.getV();			
				si.add(i);				
				Table_short_set.add( Short.valueOf(i.shortValue()), vs);
			}
		}

	}

	/**
	 * 处理table_string_string表，用于填充相应的数据。
	 */		
	class DoTableStringString extends IDoTable
	{
		@Override
		public void write(int begin, int loop) {

			int end = begin + loop;
			Random rd = new Random(System.currentTimeMillis());			
			for ( Integer i = begin; i < end; i++ )
			{
				Table_string_string.add(i.toString(), Double.toString(rd.nextDouble()));
			}
		}
	}

	


	/**
	 *  table抽象类。所有新的table处理都必须继承IDoTable。参见DoTableIntInt.
	 */	

	interface IWrite
	{
		public void write(int begin, int loop );
	}
	abstract class  IDoTable implements IWrite 
	{
		protected int _srckeytotal = 0;
		protected int _dstkeytotal = 0;
		protected int _dupkey = 0;
		protected String _tablename = null; 
		Set<Integer> _dupkeyset = new HashSet<Integer>();
		public IDoTable(  )
		{

		}


		public void init( int srckeytotal,int dstkeytotal, int dupkey )
		{
			_srckeytotal = srckeytotal;
			_dstkeytotal = dstkeytotal;
			_dupkey = dupkey;

			if ( _dupkey > srckeytotal || _dupkey > dstkeytotal )
			{
				assertTrue(false);//重复的key>srckey,dstkey???给什么鬼参数
			}

		}
		public void filldata( ){


			//建立src目录下的db							

			_dupkeyset.clear();
			
			Conf.setDbhome(new File(SrcDir));
			mkdb.Xdb.getInstance().setConf(Conf);
			mkdb.Trace.set(mkdb.Trace.WARN);
			mkdb.Xdb.getInstance().start();		


			int perOper = 1024;
			for( Integer i = 0; i < _srckeytotal ; i += perOper )
			{	
				new DoProcedure( i, (i+perOper >= _srckeytotal? _srckeytotal-i : perOper), this ).call();
			}
			mkdb.Xdb.getInstance().stop();

			//建立dst目录下的db
			Conf.setDbhome(new File(DstDir));
			mkdb.Xdb.getInstance().setConf(Conf);
			mkdb.Trace.set(mkdb.Trace.WARN);
			mkdb.Xdb.getInstance().start();
			int diffkey = _dstkeytotal - _dupkey;
			int maxkey = _srckeytotal + 100 + diffkey; 
			//写入不同key的数据
			for( Integer i = _srckeytotal + 100; i < maxkey ; i += perOper )
			{	
				new DoProcedure( i, (i+perOper >= maxkey? maxkey-i : perOper), this ).call();
			}


			//写入dupkey的数据.暂时不考虑加入random. 
			//当重复的key < 1/2 _dstkeytotal，尝试加入random.当dupkey = _srckeytotal，用random命中容易死循环
			if(_dupkey < _srckeytotal/2)			
			{
				Random rd = new Random(System.currentTimeMillis());

				int num = 0;
				for( Integer i = 0; i < _dupkey; i++ )
				{
					//判断一下rd生成的num是否已经命中过
					do
					{
						num = rd.nextInt(_srckeytotal);
					}
					while( _dupkeyset.contains(num));
					_dupkeyset.add(num);

					new DoProcedure( num, 1, this ).call(); 
				}
			}
			else
			{
				for( Integer i = 0; i < _dupkey; i += perOper)
				{	
					new DoProcedure( i, (i+perOper >= _dupkey? _dupkey-i : perOper), this ).call();
				}
				
				for( Integer i = 0; i < _dupkey; i += 1)
				{
					_dupkeyset.add(i);
				}
			}

			mkdb.Xdb.getInstance().stop();

		}

		private class DoProcedure extends mkdb.Procedure
		{

			private int _begin;
			private int _loop;
			private IDoTable _dt = null;

			DoProcedure(int begin, int loop, IDoTable dt)
			{								
				this._begin = begin;
				this._loop = loop;
				_dt = dt;
			}
			protected boolean process() throws Exception {
				_dt.write(_begin, _loop );
				return true;
			}
		}

		public long getDupKeyCnt()
		{
			return _dupkey;
		}

		public long getSrcKeyCnt()
		{
			return _srckeytotal;
		}
		public long getDstKeyCnt()
		{
			return _dstkeytotal;
		}
		
		public Set<Integer> getDupkeyset()
		{
			return _dupkeyset;
		}

	}
	//计算一个Db里面有多少个key
	private class DbCount implements mkdb.Storage.IWalk {
		private long _keycnt = 0;

		@SuppressWarnings("unused")
		private long _cnt = 0;
		@SuppressWarnings("unused")
		private final long _checkpoint = 50000;
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
	
	public class WalkTableShortSet implements mkdb.Storage.IWalk {
		private long _keycnt = 0;

		@SuppressWarnings("unused")
		private long _cnt = 0;
		@SuppressWarnings("unused")
		private final long _checkpoint = 50000;
		private Set<Integer>  _dupkeyset;
		public WalkTableShortSet( Set<Integer> dupkeyset)
		{
			_dupkeyset = dupkeyset;
		}
		@Override
		public boolean onRecord(byte[] key, byte[] data) {
			
			Short skey = (Short)DatabaseMetaData.getInstance().getTable("table_short_set")
						.getKeyType().unmarshal(key); 
			xbean.varSet value = (xbean.varSet)DatabaseMetaData.getInstance().getTable("table_short_set")
						.getValueType().unmarshal(data);

			if ( _dupkeyset.contains(Integer.valueOf(skey.intValue())))
			{
				assertTrue( skey.intValue() % 2 == 1 );
				assertTrue( value.getV().contains(1234));
			}
			else
			{
				assertTrue( value.getV().contains(Integer.valueOf(skey.intValue())) );
			}
			
			return true;
		}
		public long getKeyCnt()
		{
			return _keycnt;
		}

	}

	private void clearDirs()
	{
		//清掉之前用例生成的数据库

		File src = new File(SrcDir);
		File dst = new File(DstDir);
		File result = new File(ResultDir);
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

		if ( !result.exists() )
		{
			result.mkdir();
			new File(result+"//dbdata").mkdir();
			new File(result+"//dblogs").mkdir();
		}

		if ( !backup.exists() )
			backup.mkdir();

		deleteDir(src);
		deleteDir(dst);
	}

	private boolean isRegTable(String tablename)
	{
		return types.containsKey(tablename);

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

	/**
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
			File a=new File(oldPath);
			String[] file=a.list();
			File temp=null;
			for (int i = 0; i < file.length; i++) {
				if(oldPath.endsWith(File.separator)){
					temp=new File(oldPath+file[i]);
				}
				else{
					temp=new File(oldPath+File.separator+file[i]);
				}

				if(temp.isFile()){
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" +
							(temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ( (len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if(temp.isDirectory()){//如果是子文件夹
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
		catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}


}
