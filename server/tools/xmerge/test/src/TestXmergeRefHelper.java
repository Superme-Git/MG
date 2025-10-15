package src;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class TestXmergeRefHelper {

	public static String SrcDir = "test/SrcTmp";
	public static String DstDir = "test/DstTmp";
	public static String ResultDir = "test/resultDB";
	public static String XML = "test/test.xml";
	public static String Foreigndb = "test/fdb";
	private static mkdb.XdbConf Conf = new mkdb.XdbConf(XML);

	public static enum REF_TYPES {
		KEY, VALUE, KEY_VALUE, NONE
		// ROOT
	};

	public static Map<String, IDoTable> types = new HashMap<String, IDoTable>();

	public static IDoTable getDoTable(String tablename) {
		return types.get(tablename);
	}

	public static boolean isRegTable(String tablename) {
		return types.containsKey(tablename);
	}

	public void initial() {
		// put map
		types.clear();

		types.put("table_fint_int", new DoTableFintInt());
		types.put("table_fstring_int", new DoTableFstringInt());
		types.put("table_int_int", new DoTableIntInt());
		types.put("table_string_string", new DoTableStringString());
		types.put("table_ffint_long", new DoTableFfintLong());
		types.put("table_ffstring_float", new DoTableFfstringFloat());

		types.put("table_fint_fvalue", new DoTableFfintfvalue());
		types.put("table_fint_fxxx", new DoTableFfintfxxx());

		types.put("table_fstring_flong", new DoTableFStringFLong());
		types.put("table_string_long", new DoTableStringLong());
		types.put("table_long_map", new DoTableLongMap());

	}

	/**
	 * 处理table_fstring_flong表，用于填充相应的数据。
	 */
	class DoTableFStringFLong extends IDoTable {

		@Override
		public void writeKeyRef(int key) {

			// Table_string_long.add(Integer.valueOf(key).toString(), (long)key
			// );
			if (Table_fstring_flong.get(Integer.valueOf(key).toString()) != null) {
				// 已经存在了
				return;
			}
			Table_fstring_flong.add(Integer.valueOf(key).toString(), (long) key);
			// Table_long_map.add((long)key, vm);
		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update方式

			if (Table_fstring_flong.get(Integer.valueOf(key).toString()) != null) {
				// update之
				Table_fstring_flong.getTable().update(Integer.valueOf(key).toString(), (long) refvalue);
				return;
			}
			Table_fstring_flong.add(Integer.valueOf(key).toString(), (long) refvalue);
		}

	}

	/**
	 * 处理table_string_long表，用于填充相应的数据。
	 */
	class DoTableStringLong extends IDoTable {

		@Override
		public void writeKeyRef(int key) {

			Table_string_long.add(Integer.valueOf(key).toString(), (long) key);
			// Table_long_map.add((long)key, vm);
		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update方式
			throw new Error("impssible!!!!");
		}

	}

	/**
	 * 处理table_long_map表，用于填充相应的数据。
	 */
	class DoTableLongMap extends IDoTable {

		@Override
		public void writeKeyRef(int key) {

			if (Table_long_map.get((long) key) != null) {
				// 已经存在了
				return;
			}
			varMap vm = Pod.newvarMap();
			Map<Integer, Integer> mi = vm.getV();
			mi.put(key, key);
			Table_long_map.add((long) key, vm);
		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update方式
			throw new Error("impssible!!!!");
		}

	}

	/**
	 * 处理table_fint_fvalue表，用于填充相应的数据。
	 */
	class DoTableFfintfvalue extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			// Random rd = new Random(System.currentTimeMillis());
			throw new Error("impssible!!!!");
			// Table_ffint_long.add(key, (long)key);

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update方式
			xbean.fvalue f = Table_fint_fvalue.get(key);

			if (f == null) {

				xbean.fvalue fx = Pod.newfvalue();
				fx.setC(Integer.valueOf(refvalue).toString());
				Table_fint_fvalue.add(key, fx);
			} else {
				f.setC(Integer.valueOf(refvalue).toString());
				// Table_fint_fxxx.getTable().update(key, f);
			}
		}
	}

	/**
	 * 处理table_fint_fxxx表，用于填充相应的数据。
	 */
	class DoTableFfintfxxx extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			// Random rd = new Random(System.currentTimeMillis());
			throw new Error("impssible!!!!");
			// Table_ffint_long.add(key, (long)key);

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update方式
			xbean.fxxx f = Table_fint_fxxx.get(key);

			if (f == null) {
				xbean.fxxx fx = Pod.newfxxx();
				Set<Integer> si = fx.getA();
				si.add(refvalue);
				Table_fint_fxxx.add(key, fx);
			} else {
				Set<Integer> si = f.getA();
				si.add(key);
				// Table_fint_fxxx.getTable().update(key, f);
			}
		}
	}

	/**
	 * 处理table_ffint_long表，用于填充相应的数据。
	 */
	class DoTableFfintLong extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			// Random rd = new Random(System.currentTimeMillis());

			Table_ffint_long.add(key, (long) key);

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// Auto-generated method stub
			throw new Error("impssible!!!!");
		}
	}

	/**
	 * 处理table_ffint_Float表，用于填充相应的数据。
	 */
	class DoTableFfstringFloat extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			Random rd = new Random(System.currentTimeMillis());
			Integer i = key;
			Table_ffstring_float.add(i.toString(), rd.nextFloat());

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// Auto-generated method stub
			throw new Error("impssible!!!!");
		}
	}

	/**
	 * 处理table_fint_int表，用于填充相应的数据。
	 */
	class DoTableFintInt extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			@SuppressWarnings("unused")
			Random rd = new Random(System.currentTimeMillis());
			Table_fint_int.add(key, key);

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// Auto-generated method stub
			throw new Error("impssible!!!!");
		}
	}

	/**
	 * 处理table_fint_int表，用于填充相应的数据。
	 */
	class DoTableFstringInt extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			Integer i = key;
			Table_fstring_int.add(i.toString(), key);

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// Auto-generated method stub
			throw new Error("impssible!!!!");
		}
	}

	/**
	 * 处理table_int_int表，用于填充相应的数据。
	 */
	class DoTableIntInt extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			// Random rd = new Random(System.currentTimeMillis());
			Table_int_int.add(key, key);

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// Auto-generated method stub
			throw new Error("impssible!!!!");
		}
	}

	/**
	 * 处理table_string_string表，用于填充相应的数据。
	 */
	class DoTableStringString extends IDoTable {

		@Override
		public void writeKeyRef(int key) {
			// Auto-generated method stub
			// Random rd = new Random(System.currentTimeMillis());
			Integer i = key;
			// Table_fstring_int.add(i.toString(), key);
			Table_string_string.add(i.toString(), i.toString());

		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// Auto-generated method stub
			throw new Error("impssible!!!!");
		}
	}

	interface IWrite {
		// 表写入回调
		// 当有key foreign的情况下写入的时的回调 这个时候只管写入就行.
		public void writeKeyRef(int key);
		// 当value foreign的情况下写入的时候
		// 当exist=true，(这个表既存在key foreign, 也存在value foreign,
		// 必须采用get,update其中的<key,value>值，不然会产生不可预断的情况

		public void writeValueRef(int key, int refvalue);// 必须用update
		// 如果key foreign，value foreign同时存在呢？？？那只能update了
		// public
	}

	abstract class IDoTable implements IWrite {

		protected String _tablename = null;

		protected int _keybegin = 0;
		protected int _keytotal = 0;
		protected int _dupkey = 0;
		protected Vector<Integer> _refset = null;// 默认前提： vector<Integer> !=
													// <keybegin,
													// keybegin+total>之间
		protected REF_TYPES _reftype = REF_TYPES.NONE;
		protected Random _rd = new Random(System.currentTimeMillis());

		public void init(int keybegin, int keytotal, int dupkey, Vector<Integer> refset, REF_TYPES reftype) {

			_keybegin = keybegin;
			_keytotal = keytotal;
			_dupkey = dupkey;
			_refset = refset;
			_reftype = reftype;

		}

		public void filldata() {

			// 建立src目录下的db

			Conf.setDbhome(new File(SrcDir));
			mkdb.Xdb.getInstance().setConf(Conf);
			// mkdb.Trace.set(mkdb.Trace.FATAL);
			mkdb.Xdb.getInstance().start();
			int end = _keybegin + _keytotal - _refset.size();
			int perOper = 1024;
			for (Integer i = _keybegin; i < end; i += perOper) {
				new DoProcedure(i, (i + perOper >= end ? end - i : perOper), this).call();
			}
			end = _refset.size();
			for (Integer i = 0; i < end; i++) {
				new DoProcedure(_refset.elementAt(i), 1, this).call();
			}

			mkdb.Xdb.getInstance().stop();

			// 建立dst目录下的db
			Conf.setDbhome(new File(DstDir));
			mkdb.Xdb.getInstance().setConf(Conf);
			// mkdb.Trace.set(mkdb.Trace.FATAL);
			mkdb.Xdb.getInstance().start();
			int diffkey = _keytotal - _dupkey - _refset.size();
			if (diffkey < 0) {
				throw new Error("dupkey + refset.size > keytotal??????");
			}
			int maxkey = _keybegin + _keytotal + diffkey;
			// 写入不同key的数据
			for (Integer i = _keybegin + _keytotal; i < maxkey; i += perOper) {
				new DoProcedure(i, (i + perOper >= maxkey ? maxkey - i : perOper), this).call();
			}

			end = _refset.size();
			for (Integer i = 0; i < end; i++) {
				new DoProcedure(_refset.elementAt(i), 1, this).call();
			}

			// 直接_keybegin开始的这一片key相同
			end = _keybegin + _dupkey;
			for (Integer i = _keybegin; i < end; i += perOper) {
				new DoProcedure(i, (i + perOper >= end ? end - i : perOper), this).call();
			}

			mkdb.Xdb.getInstance().stop();

		}

		private class DoProcedure extends mkdb.Procedure {

			private int _begin;
			private int _loop;
			private IDoTable _dt = null;

			DoProcedure(int begin, int loop, IDoTable dt) {
				this._begin = begin;
				this._loop = loop;
				_dt = dt;
			}

			protected boolean process() throws Exception {
				int end = _begin + _loop;
				if (_dt._reftype == REF_TYPES.KEY || _dt._reftype == REF_TYPES.NONE) {
					for (int i = _begin; i < end; i++) {
						_dt.writeKeyRef(i);
					}
				} else if (_dt._reftype == REF_TYPES.VALUE) {
					for (int i = _begin; i < end; i++) {
						// Random取出vector中的ref值 ，然后扔进去
						if (_dt._refset.size() == 0)
							_dt.writeValueRef(i, 0);
						else
							_dt.writeValueRef(i, _dt._refset.elementAt(_dt._rd.nextInt(_dt._refset.size())));
					}
				}
				// else if ( _dt._reftype == REF_TYPES.KEY_VALUE )
				// {
				// for ( int i = _begin; i < end; i++ )
				// {
				// _dt.writeKeyRef(i);
				// //里面必须update，不然后果自负了
				// _dt.writeValueRef(i,
				// _dt._refset.elementAt(_dt._rd.nextInt(_dt._refset.capacity())),
				// true);
				// }
				// }
				else
					throw new Error("RefTypes Write Error");

				return true;
			}
		}

		public long getKeyTotal() {
			return _keytotal;
		}

		public long getDupKeyCnt() {
			return _dupkey;
		}

		public long getRefCnt() {
			if (_refset == null)
				return 0;
			else
				return _refset.size();
		}

	}

	public static void clearDirs() {
		// 清掉之前用例生成的数据库

		File src = new File(SrcDir);
		File dst = new File(DstDir);
		File result = new File(ResultDir);
		File backup = new File("xbackup");// 不从xml读了，麻烦

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

		if (!result.exists()) {
			result.mkdir();
			new File(result + "//dbdata").mkdir();
			new File(result + "//dblogs").mkdir();
		}

		if (!backup.exists())
			backup.mkdir();

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
