package src;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;



import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import xmerge.CheckDupKey;
import xmerge.ForeignMeta;
import xmerge.ICheckDupKey;
import xmerge.Merger;
import xmerge.Repair;
import xmerge.XmergeConf;
import xmerge.ForeignMeta.TableDepends;


/**
 * @author Cai Jiacheng
 * @note: 
 */
@RunWith(Parameterized.class)
public class TestXmergeRef {

	
	@SuppressWarnings({ "rawtypes" })
	@Parameters	
	public static Collection data(){
		Object[][] object = {

				//0  简单 测试，
				{ 
					// key - key - key
					//	   \
					//	     value										
					
					"table_int_int", 
							5000, 1000, 1000, 
							100, 100, 50,
					"table_string_string", 
							5000, 1000, 1000, 
							100, 100, 50,							 					
				},
				//1  refkey与dupkey各有为0时的 测.(由于 foreign value必须有，当refkey=0时，所以所有ref value值都被初始为0)
				{ 
					// key - key - key
					//	   \
					//	     value										
					
					"table_int_int",  
							5000, 0, 1000, 
							100, 0, 100,
					"table_string_string", 
							5000, 1000, 0, 
							100, 100, 0,							 					
				},
				
				//2 refkey在第三个链表就没有的情况
				{ 
					// key - key - key
					//	   \
					//	     value										
					
					"table_int_int", 
							5000, 1000, 1000, 
							100, 100, 500,
					"table_string_string", 
							5000, 1000, 1000, 
							100, 100, 500,							 					
				},
				
				//3 refkey在第二个链就没有的情况
				{ 
					// key - key - key
					//	   \
					//	     value										
					
					"table_int_int", 
							5000, 1000, 1000, 
							100, 100, 1000,
					"table_string_string", 
							5000, 1000, 1000, 
							100, 100, 1000,							 					
				},
				
				//4 所有的key都是refkey
				{ 
					// key - key - key
					//	   \
					//	     value										
					
					"table_int_int", 
							5000, 0, 5000, 
							100, 0, 100,
					"table_string_string", 
							5000, 0, 5000, 
							100, 0, 500,							 					
				},
				
				//5大数据量测试，非常费时间
//				{ 
//					// key - key - key
//					//	   \
//					//	     value										
//					
//					"table_int_int", 
//							500000, 100000, 50000, 
//							5000, 1000, 100,
//					"table_string_string", 
//							500000, 100000, 50000, 
//							5000, 1000, 200,							 					
//				},
				
				//6当两条链有交叉的时候， 这种情况比较恶心
				{ 
					// key 					table_string_long
					//	   \								  \
					//		| "key,value" :                    | table_fstring_flong "key:string,value:long" 
					//     /								  /
					// key						talbe_long_map		
					//上下链的数字保持相同，不然会有问题，暂时先这样，太复杂了。
					"table_string_long", 
							5000, 1000, 1000, 
							0, 0, 0,	
					"table_long_map", 
							5000, 1000, 1000, 
							0, 0, 0,
											 					
				},
				
				//7 交叉，key全为ref时的情况
				{ 
					// key 					table_string_long
					//	   \								  \
					//		| "key,value" :                    | table_fstring_flong "key:string,value:long" 
					//     /								  /
					// key						talbe_long_map		
					//上下链的数字保持相同，不然会有问题，暂时先这样，太复杂了。
					"table_string_long", 
							50000, 0, 50000, 
							0, 0, 0,	
					"table_long_map", 
							50000, 0, 50000, 
							0, 0, 0,
											 					
				},
				
				//8交叉，key全为不同时的情况.由于 foreign value必须有，所以所有ref value值都被初始为0
				{ 
					// key 					table_string_long
					//	   \								  \
					//		| "key,value" :                    | table_fstring_flong "key:string,value:long" 
					//     /								  /
					// key						talbe_long_map		
					//上下链的数字保持相同，不然会有问题，暂时先这样，太复杂了。
					"table_string_long", 
							50000, 0, 0, 
							0, 0, 0,	
					"table_long_map", 
							50000, 0, 0, 
							0, 0, 0,
											 					
				},
				
				
				
				
		};
		return Arrays.asList(object);
	}
		
	
	/**
	 * 下面的数据代表一个foreign链路从root节点开始的数据库表生成，会生成root下面的所有链路。
	 * 暂时支持两个root链路，多了自己加吧
	 * 支持的表项参见：TestXmergeRefHelper.initial()。需要的数据库表自己加
	 * 会生成SrcDir,与DstDir数据库.链路表之间的关系为ref, src与dst表之间的关系为dup. src与dst表中key数目相同
	 * 
	 * 一个表中的keybase = dupkey(重复key，但不是refkey) + none-dupkey(不重复key) + refkey(关联key)
	 * 每个表的下一个表（同链路）的key情况为: key = keybase - keydec, dup = dupbase - dupdec, ref - refbase - refdec;  
	 * @param root0 : Foreign中的root的tablename
	 * @param keybase0 : 总key的个数
	 * @param dupkeybase0 ：与dst表中重复的key个数
	 * @param refkeybase0 ：ref关联的个数
	 * @param keydec0 ： Keybase随着关联表项递减因子
	 * @param dupkeydec0 ：dupkeybase随着关联表项的递减因子
	 * @param refkeydec0 ：refcnt随着关联表项的递减因子
	 * 
	 * 
	 * @param root1 ：同上
	 * @param keybase1：同上
	 * @param dupkeybase1：同上
	 * @param refkeybase1：同上
	 * @param keydec1：同上
	 * @param dupkeydec1：同上
	 * @param refkeydec1：同上
	 */
	@SuppressWarnings("static-access")
	public TestXmergeRef( 
			String root0, int keybase0, int dupkeybase0, int refkeybase0,
						  int keydec0, int dupkeydec0, int refkeydec0,
			String root1, int keybase1, int dupkeybase1, int refkeybase1,
						  int keydec1, int dupkeydec1, int refkeydec1 
	)	
	{
		//开始艰巨的建表任务了
		TestXmergeRefHelper helper = new TestXmergeRefHelper();
		helper.initial();
		TestXmergeRefHelper.clearDirs();
		final int RefBase0 = 20000000;//尽量别动这个数据
		final int RefBase1 = 20000000;//尽量别动这个数据
		
		_keycur = keybase0;
		_dupkeycur = dupkeybase0;
		_refkeycur = refkeybase0;
		_keydec = keydec0;
		_dupkeydec = dupkeydec0;
		_refkeydec = refkeydec0;
		//初始化所有的ref数据,vector
	
		_refkey.clear();
		for( int i=0; i<refkeybase0; i++)
		{
			_refkey.add(i, RefBase0 + i);
		}
						
		ForeignMeta fm = new ForeignMeta();
		fm.initDepRoots();
	
		TableDepends td = fm.getDepRoots().get(root0);
		assertTrue( td != null );
		this.recCreatTable(td, _refkey, TestXmergeRefHelper.REF_TYPES.NONE);
		
		////////////第二条链
		_keycur = keybase1;
		_dupkeycur = dupkeybase1;
		_refkeycur = refkeybase1;
		_keydec = keydec1;
		_dupkeydec = dupkeydec1;
		_refkeydec = refkeydec1;
		
		_refkey.clear();
		for( int i=0; i<refkeybase1; i++)
		{
			_refkey.add(i, RefBase1 + i);
		}
		
		td = fm.getDepRoots().get(root1);
		assertTrue( td != null );
		this.recCreatTable(td, _refkey, TestXmergeRefHelper.REF_TYPES.NONE);					
	}	
	
	static int _keycur = 0;
	static int _dupkeycur = 0;
	static int _refkeycur = 0;
	static int _keydec = 0;
	static int _dupkeydec = 0;
	static int _refkeydec = 0;
	static Vector<Integer> _refkey = new Vector<Integer>();	
	private static String XMergeXML = "test/xmerge.xml";
	static String LibPath = "../bin";
	//static mkdb.XdbConf Conf = XmergeTestSuite.Conf;

	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRef() throws Exception
	{
		XmergeConf.SetConf(XMergeXML);
		XmergeConf.getInstance().setSrcDbHome(TestXmergeRefHelper.SrcDir);
		XmergeConf.getInstance().setDestDbHome(TestXmergeRefHelper.DstDir);
		XmergeConf.getInstance().setResultDbHome(TestXmergeRefHelper.ResultDir);
		XmergeConf.getInstance().setForeignInfoDbHome(TestXmergeRefHelper.Foreigndb);
		Dbx.start(LibPath);		
		Dbx resultdb = null;
		try{
			Merger merger = new Merger();
			merger.SetSrcDB(TestXmergeRefHelper.SrcDir);
			merger.SetDestDB(TestXmergeRefHelper.DstDir, false);//由merger负责拷贝

			if(merger.doMerge()){
				//必须有dupkey
				//assertTrue(false);
				
				return;
			}

			ICheckDupKey check= new CheckDupKey();
			check.setDupKeyDBPath("test/errDB");
			//必然有dupkey才会执行到这里
			assertTrue(check.checkDB(TestXmergeRefHelper.SrcDir, TestXmergeRefHelper.DstDir));
		
			//开始修复
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());
			Repair worker = new Repair();
			assertTrue( worker.repairDb());
			//再次合并.
			//再次check，确定没有DupKey
			assertFalse(check.checkDB(worker.getResultDbHome(), TestXmergeRefHelper.SrcDir));

			merger.SetSrcDB(TestXmergeRefHelper.SrcDir);
			merger.SetDestDB(worker.getResultDbHome(), true);//直接merger在DestDB上，不需要拷贝
			//修复完合并还失败???
			//merger.benchMerge(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance().getTable("table_string_string"));
			assertTrue(merger.doMerge());

			//判断一下合并是否是正确
			resultdb = Dbx.open(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance());
//
			for ( String tablename : TestXmergeRefHelper.types.keySet() )
			{
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )//应该不能加这个的，先放着			
				{	//如果有knownConflict的规则.规则弄完的DB库是否是唯一Key??					
					continue;
				}
				//Merger成功，说明生成的数据库DupKeyCnt = 0，没有重复的key				
				assertEquals(check.getDupKeyCount(tablename), 0);
				
				Dbx.Table etb = resultdb.openTable(tablename);
				DbCount dc = new DbCount();

				etb.walk(dc);
				etb.close();
				//现在srcdir表和dstdir表的keytotal都是相同的
				//System.out.print(tablename + " : walk.......");
				assertEquals((long)(TestXmergeRefHelper.types.get(tablename).getKeyTotal()*2), dc.getKeyCnt());
				dc = null;
			}

		}finally
		{			
			Dbx.stop();
		}

	}
	
	///////////////////////////////////辅助类///////////////////////
	static public void recCreatTable( TableDepends td, Vector<Integer>refkey, TestXmergeRefHelper.REF_TYPES type )
	{
		//建root表
		creatTable(td, _keycur, _dupkeycur, refkey, type);
		
		for( TableDepends id : td.getRefKeyTables().values() )
		{		
			//循环建KeyRef表
			_keycur = _keycur - _keydec;
			_dupkeycur = _dupkeycur - _dupkeydec;
			_refkeycur = _refkeycur - _refkeydec;
			
			if ( _keycur < 0 )
			{
				assertFalse(true);
			}
			if ( _dupkeycur < 0 )
				_dupkeycur = 0;
			
			if ( _refkeycur < 0 )
				_refkeycur = 0;
			
					
			Vector<Integer> reftmp = new Vector<Integer>();
			for( int i=0; i<_refkeycur; i++)
			{
				reftmp.add(i, refkey.elementAt(i));
			}
			
			recCreatTable(id, reftmp, TestXmergeRefHelper.REF_TYPES.KEY);
		}
		
		for( TableDepends id : td.getRefValueTables().values() )
		{
			//循环建Value表
			_keycur = _keycur - _keydec;
			_dupkeycur = _dupkeycur - _dupkeydec;
			_refkeycur = _refkeycur - _refkeydec;
			
			if ( _keycur < 0 )
			{
				assertFalse(true);
			}
			if ( _dupkeycur < 0 )
				_dupkeycur = 0;
			
			if ( _refkeycur < 0 )
				_refkeycur = 0;
			
			Vector<Integer> reftmp = new Vector<Integer>();
			for( int i=0; i<_refkeycur; i++)
			{
				reftmp.add(i, refkey.elementAt(i));
			}

			recCreatTable(id, reftmp, TestXmergeRefHelper.REF_TYPES.VALUE);
		}		
	}
	
	static public void creatTable(TableDepends td, int keytotal, int dupkey, Vector<Integer>refkey, TestXmergeRefHelper.REF_TYPES type )
	{
		//确认表的写入已经被注册
		assertTrue( TestXmergeRefHelper.isRegTable(td.getName()) );
		TestXmergeRefHelper.getDoTable(td.getName()).init(0, keytotal, dupkey, refkey, type);
		TestXmergeRefHelper.getDoTable(td.getName()).filldata();				
		
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
	
	

	
	
}
