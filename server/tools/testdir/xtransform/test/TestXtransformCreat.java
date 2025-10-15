


import java.io.File;

import java.util.HashMap;
import java.util.Map;

import java.util.Set;


import xbean.Pod;
import xbean.cxxx;
import xbean.varMap;
import xbean.varSet;
import xbean.varXBean;
import xbean.__.cxbean;
import xbean.__.cxbean_int;
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
public class TestXtransformCreat {

	private Map<String, IDoDb>  types = new HashMap<String, IDoDb>();
	
	final String XML = "oldtest.xml";
	private String LibPath = "../bin";
	final String SrcDir = "src_db";
	public final String DstDir = "dest_db";

	
	private String _tablename[];
	private int _keytotal[]; 

	
	public TestXtransformCreat( String[] args )
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
			int end = begin + loop;
			for (Integer i = begin; i < end; i++) {					
				cxxx ck = new cxxx(i, i);
				xbean.cxbean_int value = Pod.newcxbean_int();		
				value.setA(i);				
				xtable.Table_cbean_cxbeanint.add(ck, value);
				
			}
				
		}
			

		@Override
		public void read(int begin, int loop) {
		    	
			return;
		}
	}
	
	class DoTableCBeanValue implements IDoDb
	{		
		public DoTableCBeanValue()
		{

		}
		
		public void write(int begin, int loop) {
			int end = begin + loop;
			for (Integer i = begin; i < end; i++) {					
				cxxx ck = new cxxx(i, i);
				xbean.cxbean value = Pod.newcxbean();
				
				value.setA(i);
				value.setB(new cxxx(i, i));
								
				xtable.Table_cbean_cxbean.add(ck, value);
				
			}
				
		}
			

		@Override
		public void read(int begin, int loop) {
		    	
			return;
		}
	}
	
	
	class DoTableAutoKey implements IDoDb
	{		
		public DoTableAutoKey()
		{

		}
		
		public void write(int begin, int loop) {
			String value = "1234";
			int end = begin + loop;
			for (Integer i = begin; i < end; i++) {					
				//xtable.Table_autokey.insert(i.toString());
				xtable.Table_autokey.insert(value);
			}
				
		}
			

		@Override
		public void read(int begin, int loop) {
		    	
			int end = begin + loop;

			for (Integer i = begin; i < end; i++) {

		
			}
		}
	}
	
	class DoTableIntXbean implements IDoDb
	{		
		public void write(int begin, int loop) {
			
			int end = begin + loop;
			for (Integer i = begin; i < end; i++) {					
				varXBean vb = Pod.newvarXBean();
				//vint
				vb.setVint(i);
				//vstring
				vb.setVstring(i.toString());
				//vset
				Set<Integer> si = vb.getVset();
				//vmap
				Map<Integer, Integer> mi = vb.getVmap();													
				mi.put(i, i);
				si.add(i);
				xbean.xxx x = vb.getVxxx();
				
				x.setC(i.toString());
				x.setD(i.toString());
				Set<Integer> sx = x.getA();
				sx.add(i);												
				//vyyy
				xbean.yyy y = vb.getVyyy();
				y.setB(i);
				
				Set<Integer> sy = y.getA();
				
				sy.add(i);
				//System.out.println(i);
				y.setC(i.toString());
				
				xbean.delXbean dx = Pod.newdelXbean();
				dx.setA(111);				
				dx.getB().setA(111);
				vb.getBugdelxbean().put(111, dx);
				//vb.getBugdelxbean().getB().setA(111);

				
				//ok
				Table_int_xbean.add(i, vb);
			}
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
			
			int end = begin + loop;
			for ( Integer i = begin; i < end; i++ )
			{
				varMap vm = Pod.newvarMap();
				Map<Integer, Integer> mi = vm.getV();
				mi.put(i, i);
				
				Map<Integer, Integer> mi1 = vm.getV1();
				mi1.put(i, i);
				
				Map<Integer, Integer> mi2 = vm.getV2();
				mi2.put(i, i);
				
				java.util.List<Integer> v3 = vm.getV3();
				v3.add(i);
				
				java.util.List<Integer> v4 = vm.getV4();
				v4.add(i);
				
				Long j = i.longValue();
				
				java.util.Set<Long> v7 = vm.getV7();
				v7.add(j);
				
				java.util.Set<Long> v8 = vm.getV8();
				v8.add(j);
				
				vm.setC("aaa");
				vm.setD("bbb");
//				for( Integer j = 0; j < i ; j++ )
//					mi.put(j, i);
				Table_long_map.add( (long)i, vm);
			}
		}

		@Override
		public void read(int begin, int loop) {
		    	
			int end = begin + loop;

			for (Integer i = begin; i < end; i++) {

				varMap mi = Table_long_map.get(i.longValue());
				//assertTrue(mi.getV().containsKey(i));
				assert(mi.getV().containsKey(i));
				assert(mi.getV().get(i) ==  i);
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
			int end = begin + loop;
			for ( int i = begin; i < end; i++ )
			{
				varSet vs = Pod.newvarSet();
				Set<Integer> si = vs.getV();
				
				si.add(i);
//				for( Integer j=0; j<i; j++)
//					si.add(j);				
				Table_short_set.add((short)i, vs);
			}
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
		this.clearDirs();
		xdb.XdbConf Conf = new xdb.XdbConf(XML);
		Conf.setDbhome(new File(SrcDir));
		Conf.setLibdbPath(new File(LibPath));
		xdb.Xdb.getInstance().setConf(Conf);		
		xdb.Xdb.getInstance().start();
		
		int perOper = 4096;
		
		for( Integer num = 0; num < _tablename.length; num ++ )
		{
			for ( Integer i = 0 ; i < _keytotal[num]; i+=perOper )
				new DoProcedure( i, (i+perOper >= _keytotal[num]? _keytotal[num]-i : perOper), types.get(_tablename[num]), Op.WR ).submit().get(); 
		}
		
		xdb.Xdb.getInstance().stop();
		
	}
	
	public void verifyDb(String db) throws Exception
	{
		throw new Error("not support");
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
