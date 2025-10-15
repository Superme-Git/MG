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
	 * ����table_fstring_flong�����������Ӧ�����ݡ�
	 */
	class DoTableFStringFLong extends IDoTable {

		@Override
		public void writeKeyRef(int key) {

			// Table_string_long.add(Integer.valueOf(key).toString(), (long)key
			// );
			if (Table_fstring_flong.get(Integer.valueOf(key).toString()) != null) {
				// �Ѿ�������
				return;
			}
			Table_fstring_flong.add(Integer.valueOf(key).toString(), (long) key);
			// Table_long_map.add((long)key, vm);
		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update��ʽ

			if (Table_fstring_flong.get(Integer.valueOf(key).toString()) != null) {
				// update֮
				Table_fstring_flong.getTable().update(Integer.valueOf(key).toString(), (long) refvalue);
				return;
			}
			Table_fstring_flong.add(Integer.valueOf(key).toString(), (long) refvalue);
		}

	}

	/**
	 * ����table_string_long�����������Ӧ�����ݡ�
	 */
	class DoTableStringLong extends IDoTable {

		@Override
		public void writeKeyRef(int key) {

			Table_string_long.add(Integer.valueOf(key).toString(), (long) key);
			// Table_long_map.add((long)key, vm);
		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update��ʽ
			throw new Error("impssible!!!!");
		}

	}

	/**
	 * ����table_long_map�����������Ӧ�����ݡ�
	 */
	class DoTableLongMap extends IDoTable {

		@Override
		public void writeKeyRef(int key) {

			if (Table_long_map.get((long) key) != null) {
				// �Ѿ�������
				return;
			}
			varMap vm = Pod.newvarMap();
			Map<Integer, Integer> mi = vm.getV();
			mi.put(key, key);
			Table_long_map.add((long) key, vm);
		}

		@Override
		public void writeValueRef(int key, int refvalue) {
			// update��ʽ
			throw new Error("impssible!!!!");
		}

	}

	/**
	 * ����table_fint_fvalue�����������Ӧ�����ݡ�
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
			// update��ʽ
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
	 * ����table_fint_fxxx�����������Ӧ�����ݡ�
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
			// update��ʽ
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
	 * ����table_ffint_long�����������Ӧ�����ݡ�
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
	 * ����table_ffint_Float�����������Ӧ�����ݡ�
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
	 * ����table_fint_int�����������Ӧ�����ݡ�
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
	 * ����table_fint_int�����������Ӧ�����ݡ�
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
	 * ����table_int_int�����������Ӧ�����ݡ�
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
	 * ����table_string_string�����������Ӧ�����ݡ�
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
		// ��д��ص�
		// ����key foreign�������д���ʱ�Ļص� ���ʱ��ֻ��д�����.
		public void writeKeyRef(int key);
		// ��value foreign�������д���ʱ��
		// ��exist=true��(�����ȴ���key foreign, Ҳ����value foreign,
		// �������get,update���е�<key,value>ֵ����Ȼ���������Ԥ�ϵ����

		public void writeValueRef(int key, int refvalue);// ������update
		// ���key foreign��value foreignͬʱ�����أ�������ֻ��update��
		// public
	}

	abstract class IDoTable implements IWrite {

		protected String _tablename = null;

		protected int _keybegin = 0;
		protected int _keytotal = 0;
		protected int _dupkey = 0;
		protected Vector<Integer> _refset = null;// Ĭ��ǰ�᣺ vector<Integer> !=
													// <keybegin,
													// keybegin+total>֮��
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

			// ����srcĿ¼�µ�db

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

			// ����dstĿ¼�µ�db
			Conf.setDbhome(new File(DstDir));
			mkdb.Xdb.getInstance().setConf(Conf);
			// mkdb.Trace.set(mkdb.Trace.FATAL);
			mkdb.Xdb.getInstance().start();
			int diffkey = _keytotal - _dupkey - _refset.size();
			if (diffkey < 0) {
				throw new Error("dupkey + refset.size > keytotal??????");
			}
			int maxkey = _keybegin + _keytotal + diffkey;
			// д�벻ͬkey������
			for (Integer i = _keybegin + _keytotal; i < maxkey; i += perOper) {
				new DoProcedure(i, (i + perOper >= maxkey ? maxkey - i : perOper), this).call();
			}

			end = _refset.size();
			for (Integer i = 0; i < end; i++) {
				new DoProcedure(_refset.elementAt(i), 1, this).call();
			}

			// ֱ��_keybegin��ʼ����һƬkey��ͬ
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
						// Randomȡ��vector�е�refֵ ��Ȼ���ӽ�ȥ
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
				// //�������update����Ȼ����Ը���
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
		// ���֮ǰ�������ɵ����ݿ�

		File src = new File(SrcDir);
		File dst = new File(DstDir);
		File result = new File(ResultDir);
		File backup = new File("xbackup");// ����xml���ˣ��鷳

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
			return; // ������
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // ɾ�������ļ�
			else if (file.isDirectory())
				deleteDir(file); // �ݹ�ķ�ʽɾ���ļ���
		}
		// dir.delete();// ɾ��Ŀ¼����
	}

}
