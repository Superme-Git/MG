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
 * @note: ����������������ָ�������ݿ⣬Ȼ�����testCheck()ִ����Ӧ���ԡ�
 * 			ָ�������ݿ������Collection()�Ĳ������á������˵����
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


	////////---�ظ�����̫��ʱ���˱�������������ж�---////////
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

	//�ѿ��Դ���ı������map��
	private Map<String, IDoTable>  types = null;


	/** ��ʱ֧��5�ֱ�ͬʱ�������ɣ�������ע��ı���֧�ֲ�������[]���Ƚ��鷳
	 * @param srctotalx : Դ���ݿ���������Ŀ
	 * @param dsttotalx : Ŀ�����ݿ���������Ŀ
	 * @param dupkeyx : �������ݿ����ظ���Ŀ
	 * @param tablenamex : ���ݿ����
	 * �����ʽ��
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

				//0  �� ���ԣ�src��,dst���е�����������ͬ
				{ 
					1000, 1000, 1000, "table_int_int",
					1000, 1000, 1000, "table_string_string",
					1000, 1000, 1000, "table_int_xbean",
					1000, 1000, 1000, "table_long_map",
					1000, 1000, 1000, "table_short_set",
				},
				//1  ���Դ����������.�ܺ�ʱ�䣬����� > 1000000ʱ�� windows checkpoint���������С���
//				{ 
//					500000, 500000, 200000, "table_int_int",
//					500000, 500000, 200000, "table_string_string",
//					500000, 500000, 200000, "table_int_xbean",
//					500000, 500000, 200000, "table_long_map",
//					30000, 30000, 500, "table_short_set", //short key<= Short.MaxValue 32767, srctotal + dsttotal < 65535
//				},

				//2  ����dst��û�κ����ݵ����
				{ 
					50000, 0, 0, "table_int_int",
					50000, 0, 0, "table_string_string",
					50000, 0, 0, "table_int_xbean",
					50000, 0, 0, "table_long_map",
					20000, 0, 0, "table_short_set",
				},

				//3  ����src��û�κ����ݵ����
				{ 
					0, 50000, 0, "table_int_int",
					0, 50000, 0, "table_string_string",
					0, 50000, 0, "table_int_xbean",
					0, 50000, 0, "table_long_map",
					0, 20000, 0, "table_short_set",
				},

				//4 ��������dupkey�����
				{ 
					50000, 50000, 1, "table_int_int",
					50000, 50000, 1, "table_string_string",
					50000, 50000, 1, "table_int_xbean",
					50000, 50000, 1, "table_long_map",
					10000, 10000, 1, "table_short_set",
				},

				//5 ����src����>>dst���ݵ����
				{ 
					50000, 50, 1, "table_int_int",
					50000, 50, 2, "table_string_string",
					50000, 50, 3, "table_int_xbean",
					50000, 50, 4, "table_long_map",
					20000, 30, 5, "table_short_set",
				},

				//6 ����src����>>dst���ݵ����
				{ 
					50, 50000, 1, "table_int_int",
					50, 50000, 2, "table_string_string",
					50, 50000, 3, "table_int_xbean",
					50, 50000, 4, "table_long_map",
					30, 20000, 5, "table_short_set",
				},

				//7 ��������������ݵ����
				{ 
					45678, 12345, 1235, "table_int_int",
					75894, 64587, 2458, "table_string_string",
					78545, 68745, 2664, "table_int_xbean",
					78954, 68715, 4578, "table_long_map",
					12345, 14567, 1111, "table_short_set",
				},

				//8 ����û���κ��ظ���key
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

		//--------��ʱ��ע��ķ�����ɣ�ֻ֧�����±�Ķ���-------------
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

		//�������в����䶯��ʱ����ʱ����mkdb���ݿ�����ؽ�

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
		//Conf = new mkdb.XdbConf(XML);//confֻ��newһ�Σ�����������
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
			//ģ����ü��Ĺ���
			if ( !check.checkDB(SrcDir, DstDir) )
			{
				for ( String tablename : types.keySet() )
				{	//Check == false��˵�����ɵ����ݿ�DupKeyCnt = 0��û���ظ���key										
					assertTrue(types.get(tablename).getDupKeyCnt() == 0);
				}	
				return;
			}

			assertTrue(check.checkDB(SrcDir, DstDir));

			//��errDb���ݿ⣬ģ���޸��Ķ�ȡ���̡�

			errdb = Dbx.open(new File(check.getDupKeyDBPath()), DatabaseMetaData.getInstance());

			for ( String tablename : types.keySet() )
			{
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )			
				{	//�����knownConflict�Ĺ���
					//assertEquals( check.getDupKeyCount(tablename), 0 );
					assertTrue( check.getDupKeyCount(tablename) == 0 );
					continue;
				}
				//�ж�check�����������Ƿ���ָ����dupkey��ͬ				
				assertEquals(check.getDupKeyCount(tablename), types.get(tablename).getDupKeyCnt());
				//�ж�һ��errDb�У����ɵ�errDb���Ƿ���check������dupkey��ͬ
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
			//ģ����ü��Ĺ���
			if ( !check.checkDB(SrcDir, DstDir) )
			{
				for ( String tablename : types.keySet() )
				{	//Check == false��˵�����ɵ����ݿ�DupKeyCnt = 0��û���ظ���key										
					//assertEquals(types.get(tablename).getDupKeyCnt(), 0);
					assertTrue(types.get(tablename).getDupKeyCnt()== 0);
				}			
			}

			String srctmp = DstDir+"_tmp";
			xmerge.Util.copyFolder(DstDir, srctmp);


			//�޸�srctmp			
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());
			Repair worker = new Repair();
			assertTrue(worker.repairDb());

			//���¼���Ƿ���dupkey��
			//check.setDupKeyDBPath("test/errDB");
			assertFalse(check.checkDB(worker.getResultDbHome(), SrcDir));

			//�������ɵ�errDB�������е�DupKeyCountӦ�ö�Ϊ0
			errdb = Dbx.open(new File(check.getDupKeyDBPath()), DatabaseMetaData.getInstance());

			for ( String tablename : types.keySet() )
			{											
				//assertEquals(check.getDupKeyCount(tablename), 0);
				assertTrue(check.getDupKeyCount(tablename)== 0);
				//�ж�һ��errDb�У����ɵ�errDb���Ƿ���0
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
			merger.SetDestDB(DstDir, false);//��merger���𿽱�

			if(merger.doMerge()){
				for ( String tablename : types.keySet() )
				{	
					if ( XmergeConf.getInstance().isKnownConflict(tablename) )//Ӧ�ò��ܼ�����ģ��ȷ���			
					{	
						//�����knownConflict�Ĺ���.ֱ��pass						
						continue;
					}
					//Merger�ɹ���˵�����ɵ����ݿ�DupKeyCnt = 0��û���ظ���key										
					//assertEquals(types.get(tablename).getDupKeyCnt(), 0);
					assertTrue(types.get(tablename).getDupKeyCnt() == 0);
				}
				return;
			}

			ICheckDupKey check= new CheckDupKey();
			check.setDupKeyDBPath("test/errDB");
			//��Ȼ��dupkey�Ż�ִ�е�����
			assertTrue(check.checkDB(SrcDir, DstDir));

			//����SrcĿ¼			
			String srctmp = DstDir+"_tmp";

			xmerge.Util.copyFolder(DstDir, srctmp);

			//��ʼ�޸�
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());
			Repair worker = new Repair();
			assertTrue( worker.repairDb());
			//�ٴκϲ�.
			//�ٴ�check��ȷ��û��DupKey
			assertFalse(check.checkDB(worker.getResultDbHome(), SrcDir));

			merger.SetSrcDB(SrcDir);
			merger.SetDestDB(worker.getResultDbHome(), true);//ֱ��merger��DestDB�ϣ�����Ҫ����
			//�޸���ϲ���ʧ��???
			//merger.benchMerge(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance().getTable("table_string_string"));
			assertTrue(merger.doMerge());

			//�ж�һ�ºϲ��Ƿ�����ȷ
			//merger.getResultDbHome();
			resultdb = Dbx.open(new File(merger.getResultDBDir()), DatabaseMetaData.getInstance());

			for ( String tablename : types.keySet() )
			{	//Merger�ɹ���˵�����ɵ����ݿ�DupKeyCnt = 0��û���ظ���key										
				//assertEquals(types.get(tablename).getSrcKeyCnt(), 0);
				if ( XmergeConf.getInstance().isKnownConflict(tablename) )//Ӧ�ò��ܼ�����ģ��ȷ���			
				{	//�����knownConflict�Ĺ���.����Ū���DB���Ƿ���ΨһKey??		
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
			
				//�ж�һ�ºϲ��Ľ���Ƿ���ȷ
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
	/*------------------������--------------------------*/

	/**
	 * ����table_int_int�����������Ӧ�����ݡ�
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
	 * ����table_int_xbean�����������Ӧ�����ݡ�
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
	 * ����table_long_map�����������Ӧ�����ݡ�
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
	 * ����table_short_set�����������Ӧ�����ݡ�
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
	 * ����table_string_string�����������Ӧ�����ݡ�
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
	 *  table�����ࡣ�����µ�table��������̳�IDoTable���μ�DoTableIntInt.
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
				assertTrue(false);//�ظ���key>srckey,dstkey???��ʲô�����
			}

		}
		public void filldata( ){


			//����srcĿ¼�µ�db							

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

			//����dstĿ¼�µ�db
			Conf.setDbhome(new File(DstDir));
			mkdb.Xdb.getInstance().setConf(Conf);
			mkdb.Trace.set(mkdb.Trace.WARN);
			mkdb.Xdb.getInstance().start();
			int diffkey = _dstkeytotal - _dupkey;
			int maxkey = _srckeytotal + 100 + diffkey; 
			//д�벻ͬkey������
			for( Integer i = _srckeytotal + 100; i < maxkey ; i += perOper )
			{	
				new DoProcedure( i, (i+perOper >= maxkey? maxkey-i : perOper), this ).call();
			}


			//д��dupkey������.��ʱ�����Ǽ���random. 
			//���ظ���key < 1/2 _dstkeytotal�����Լ���random.��dupkey = _srckeytotal����random����������ѭ��
			if(_dupkey < _srckeytotal/2)			
			{
				Random rd = new Random(System.currentTimeMillis());

				int num = 0;
				for( Integer i = 0; i < _dupkey; i++ )
				{
					//�ж�һ��rd���ɵ�num�Ƿ��Ѿ����й�
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
		//���֮ǰ�������ɵ����ݿ�

		File src = new File(SrcDir);
		File dst = new File(DstDir);
		File result = new File(ResultDir);
		File backup = new File("xbackup");//����xml���ˣ��鷳

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
			return; // ������ 
		for (File file : dir.listFiles()) { 
			if (file.isFile()) 
				file.delete(); // ɾ�������ļ� 
			else if (file.isDirectory()) 
				deleteDir(file); // �ݹ�ķ�ʽɾ���ļ��� 
		} 
		// dir.delete();// ɾ��Ŀ¼���� 
	} 

	/**
	 * ���������ļ�������
	 * @param oldPath String ԭ�ļ�·�� �磺c:/fqf
	 * @param newPath String ���ƺ�·�� �磺f:/fqf/ff
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); //����ļ��в����� �������ļ���
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
				if(temp.isDirectory()){//��������ļ���
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
		catch (Exception e) {
			System.out.println("���������ļ������ݲ�������");
			e.printStackTrace();

		}

	}


}
