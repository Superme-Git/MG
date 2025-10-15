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

				//0  �� ���ԣ�
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
				//1  refkey��dupkey����Ϊ0ʱ�� ��.(���� foreign value�����У���refkey=0ʱ����������ref valueֵ������ʼΪ0)
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
				
				//2 refkey�ڵ����������û�е����
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
				
				//3 refkey�ڵڶ�������û�е����
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
				
				//4 ���е�key����refkey
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
				
				//5�����������ԣ��ǳ���ʱ��
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
				
				//6���������н����ʱ�� ��������Ƚ϶���
				{ 
					// key 					table_string_long
					//	   \								  \
					//		| "key,value" :                    | table_fstring_flong "key:string,value:long" 
					//     /								  /
					// key						talbe_long_map		
					//�����������ֱ�����ͬ����Ȼ�������⣬��ʱ��������̫�����ˡ�
					"table_string_long", 
							5000, 1000, 1000, 
							0, 0, 0,	
					"table_long_map", 
							5000, 1000, 1000, 
							0, 0, 0,
											 					
				},
				
				//7 ���棬keyȫΪrefʱ�����
				{ 
					// key 					table_string_long
					//	   \								  \
					//		| "key,value" :                    | table_fstring_flong "key:string,value:long" 
					//     /								  /
					// key						talbe_long_map		
					//�����������ֱ�����ͬ����Ȼ�������⣬��ʱ��������̫�����ˡ�
					"table_string_long", 
							50000, 0, 50000, 
							0, 0, 0,	
					"table_long_map", 
							50000, 0, 50000, 
							0, 0, 0,
											 					
				},
				
				//8���棬keyȫΪ��ͬʱ�����.���� foreign value�����У���������ref valueֵ������ʼΪ0
				{ 
					// key 					table_string_long
					//	   \								  \
					//		| "key,value" :                    | table_fstring_flong "key:string,value:long" 
					//     /								  /
					// key						talbe_long_map		
					//�����������ֱ�����ͬ����Ȼ�������⣬��ʱ��������̫�����ˡ�
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
	 * ��������ݴ���һ��foreign��·��root�ڵ㿪ʼ�����ݿ�����ɣ�������root�����������·��
	 * ��ʱ֧������root��·�������Լ��Ӱ�
	 * ֧�ֵı���μ���TestXmergeRefHelper.initial()����Ҫ�����ݿ���Լ���
	 * ������SrcDir,��DstDir���ݿ�.��·��֮��Ĺ�ϵΪref, src��dst��֮��Ĺ�ϵΪdup. src��dst����key��Ŀ��ͬ
	 * 
	 * һ�����е�keybase = dupkey(�ظ�key��������refkey) + none-dupkey(���ظ�key) + refkey(����key)
	 * ÿ�������һ����ͬ��·����key���Ϊ: key = keybase - keydec, dup = dupbase - dupdec, ref - refbase - refdec;  
	 * @param root0 : Foreign�е�root��tablename
	 * @param keybase0 : ��key�ĸ���
	 * @param dupkeybase0 ����dst�����ظ���key����
	 * @param refkeybase0 ��ref�����ĸ���
	 * @param keydec0 �� Keybase���Ź�������ݼ�����
	 * @param dupkeydec0 ��dupkeybase���Ź�������ĵݼ�����
	 * @param refkeydec0 ��refcnt���Ź�������ĵݼ�����
	 * 
	 * 
	 * @param root1 ��ͬ��
	 * @param keybase1��ͬ��
	 * @param dupkeybase1��ͬ��
	 * @param refkeybase1��ͬ��
	 * @param keydec1��ͬ��
	 * @param dupkeydec1��ͬ��
	 * @param refkeydec1��ͬ��
	 */
	@SuppressWarnings("static-access")
	public TestXmergeRef( 
			String root0, int keybase0, int dupkeybase0, int refkeybase0,
						  int keydec0, int dupkeydec0, int refkeydec0,
			String root1, int keybase1, int dupkeybase1, int refkeybase1,
						  int keydec1, int dupkeydec1, int refkeydec1 
	)	
	{
		//��ʼ��޵Ľ���������
		TestXmergeRefHelper helper = new TestXmergeRefHelper();
		helper.initial();
		TestXmergeRefHelper.clearDirs();
		final int RefBase0 = 20000000;//�������������
		final int RefBase1 = 20000000;//�������������
		
		_keycur = keybase0;
		_dupkeycur = dupkeybase0;
		_refkeycur = refkeybase0;
		_keydec = keydec0;
		_dupkeydec = dupkeydec0;
		_refkeydec = refkeydec0;
		//��ʼ�����е�ref����,vector
	
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
		
		////////////�ڶ�����
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
			merger.SetDestDB(TestXmergeRefHelper.DstDir, false);//��merger���𿽱�

			if(merger.doMerge()){
				//������dupkey
				//assertTrue(false);
				
				return;
			}

			ICheckDupKey check= new CheckDupKey();
			check.setDupKeyDBPath("test/errDB");
			//��Ȼ��dupkey�Ż�ִ�е�����
			assertTrue(check.checkDB(TestXmergeRefHelper.SrcDir, TestXmergeRefHelper.DstDir));
		
			//��ʼ�޸�
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());
			Repair worker = new Repair();
			assertTrue( worker.repairDb());
			//�ٴκϲ�.
			//�ٴ�check��ȷ��û��DupKey
			assertFalse(check.checkDB(worker.getResultDbHome(), TestXmergeRefHelper.SrcDir));

			merger.SetSrcDB(TestXmergeRefHelper.SrcDir);
			merger.SetDestDB(worker.getResultDbHome(), true);//ֱ��merger��DestDB�ϣ�����Ҫ����
			//�޸���ϲ���ʧ��???
			//merger.benchMerge(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance().getTable("table_string_string"));
			assertTrue(merger.doMerge());

			//�ж�һ�ºϲ��Ƿ�����ȷ
			resultdb = Dbx.open(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance());
//
			for ( String tablename : TestXmergeRefHelper.types.keySet() )
			{
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )//Ӧ�ò��ܼ�����ģ��ȷ���			
				{	//�����knownConflict�Ĺ���.����Ū���DB���Ƿ���ΨһKey??					
					continue;
				}
				//Merger�ɹ���˵�����ɵ����ݿ�DupKeyCnt = 0��û���ظ���key				
				assertEquals(check.getDupKeyCount(tablename), 0);
				
				Dbx.Table etb = resultdb.openTable(tablename);
				DbCount dc = new DbCount();

				etb.walk(dc);
				etb.close();
				//����srcdir���dstdir���keytotal������ͬ��
				//System.out.print(tablename + " : walk.......");
				assertEquals((long)(TestXmergeRefHelper.types.get(tablename).getKeyTotal()*2), dc.getKeyCnt());
				dc = null;
			}

		}finally
		{			
			Dbx.stop();
		}

	}
	
	///////////////////////////////////������///////////////////////
	static public void recCreatTable( TableDepends td, Vector<Integer>refkey, TestXmergeRefHelper.REF_TYPES type )
	{
		//��root��
		creatTable(td, _keycur, _dupkeycur, refkey, type);
		
		for( TableDepends id : td.getRefKeyTables().values() )
		{		
			//ѭ����KeyRef��
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
			//ѭ����Value��
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
		//ȷ�ϱ��д���Ѿ���ע��
		assertTrue( TestXmergeRefHelper.isRegTable(td.getName()) );
		TestXmergeRefHelper.getDoTable(td.getName()).init(0, keytotal, dupkey, refkey, type);
		TestXmergeRefHelper.getDoTable(td.getName()).filldata();				
		
	}

	//����һ��Db�����ж��ٸ�key
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
