package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Const;
import mkdb.Storage.IWalk;
import mkdb.Trace;
import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import xmerge.Invoke;

public class TestRefCreate {
	@Before
    public void setUp() {	
	}
    
	@After
    public void tearDown() throws Exception {
	}
	
	static int COUNT = 1;
	@Test
	public void testCreate() throws Exception {
		mkdb.Xdb.getInstance().setConf(new mkdb.XdbConf("./test/test.xml"));
		mkdb.Trace.set(Trace.FATAL);
		mkdb.Xdb.getInstance().start();	
		for (int i = 0; i < 10; i++) {
			new AddProc().submit().get();
		}
		mkdb.Xdb.getInstance().stop();
	}
	
	@Test
	public void testWalk() throws InterruptedException {
		mkdb.util.DatabaseMetaData curMeta;
		File dbHome;
		mkdb.util.Dbx db;
		
		curMeta = mkdb.util.DatabaseMetaData.getInstance();
		dbHome = new File("./test/dest_db");
		mkdb.util.Dbx.start("./test");		
		
		db = Dbx.open(dbHome, curMeta);
		for (DatabaseMetaData.Table tableMetaData : db.getMetaData().getTables()) {
			if (!tableMetaData.isPersistence())
				continue;
			db.openTable(tableMetaData.getName());
		}
		
		for (mkdb.util.Dbx.Table table : db.getTables()) {
			Walker walker = new Walker(curMeta.getTable(table.getName()));
			table.walk(walker);
			walker.finish();
		}
		db.close();
	}
	
	@Test
	public void testCopy() throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
		//Util.copyFolder("./test/mkdb", "./test/cdb");
		mkdb.Xdb.getInstance().setConf(new mkdb.XdbConf("./test/cdb.xml"));
		mkdb.Xdb.getInstance().start();	
		for (int i = 0; i < 1; i++) {
			new AddProc().submit().get();
		}
		mkdb.Xdb.getInstance().stop();
	}
	
	@Test
	public void testUniqName() throws Exception {
		mkdb.util.Dbx.start("./test");		
		DatabaseMetaData curMeta = DatabaseMetaData.getInstance();
		Dbx db = Dbx.open(new File("./test/mkdb"), curMeta);
		Dbx.Table sys = db.newTable("_sys_");
		int localId = 0;
		mkdb.Trace.info(sys != null);
		
		OctetsStream keyOfAutoKeys = new OctetsStream().marshal("mkdb.util.AutoKeys." + String.valueOf(localId), mkdb.Const.IO_CHARSET);
		OctetsStream valueOfAutoKeys = sys.find(keyOfAutoKeys);
		OctetsStream oldKey = new OctetsStream().marshal("mkdb.util.AutoKeys", Const.IO_CHARSET);
		OctetsStream oldValue = sys.find(oldKey);
		mkdb.util.AutoKeys autos;
		if (null != oldValue) {
			if (null != valueOfAutoKeys)
				throw new RuntimeException("autoKeys corrupt: new? old?");
			sys.replaceUnsafe(keyOfAutoKeys, oldValue);
			sys.removeUnsafe(oldKey);
			autos = new mkdb.util.AutoKeys(oldValue, localId, true);
			mkdb.Trace.info("find." + autos);
		} else {
			autos = new mkdb.util.AutoKeys(valueOfAutoKeys, localId, false);
			mkdb.Trace.info("no find."  + autos);
		}		
		
		mkdb.Trace.info(autos.getAutoKeyLong("autotable"));
		mkdb.Trace.info(autos.getAutoKeyLong("auto1"));
		mkdb.Trace.info("cur :" + Invoke.invokeMethod(autos.getAutoKeyLong("autotable"), "current", new Object[] {}));
		Long cur = (Long) Invoke.getProperty(autos.getAutoKeyLong("autotable"), "next");
		Invoke.setProperty(autos.getAutoKeyLong("autotable"), "next", cur + 4096);
		mkdb.Trace.info("after:" + Invoke.getProperty(autos.getAutoKeyLong("autotable"), "next"));
		
		OctetsStream newValue = autos.encodeValue(0);
		sys.replaceUnsafe(keyOfAutoKeys, newValue);
		mkdb.Trace.info(autos.getAutoKeyLong("autotable"));
		mkdb.Trace.info(autos.getAutoKeyLong("auto1"));
		
		sys.walk(new IWalk() {
			@Override
			public boolean onRecord(byte[] _key, byte[] _data) {
				OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
				OctetsStream value = OctetsStream.wrap(Octets.wrap(_data));
				
				String str = new String();
				try {
					str = key.unmarshal_String(mkdb.Const.IO_CHARSET);
					int localId = (int) str.charAt(str.length() - 1);
					mkdb.util.AutoKeys autos = new mkdb.util.AutoKeys(value, localId, false);
					mkdb.Trace.info("key:" + str + " value:" + autos);
				} catch (MarshalException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}			
		});
	}
	
	class AddProc extends mkdb.Procedure {
		@Override
		public boolean process() {
			for (int i = 0; i < COUNT; i++) {
				// gen stub key
				boolean fbooleanKey = false; // all be false, so we can allocateNewKey
				float floatKey = new Random().nextFloat();
				int fintKey = new Random().nextInt();
				long flongKey = new Random().nextLong();
				short fshortKey = Short.valueOf((short)new Random().nextInt(Short.MAX_VALUE / 2));
				String fstringKey = String.valueOf(new Random().nextLong());
				// 暂时不支持cbean, binary, any, boolean
//				 xcompare fcbeanKey = new xbean.xcompare(fbooleanKey, fshortKey, fintKey, flongKey, fstringKey);
//				 recursivecbean frcbeanKey = new xbean.recursivecbean(fcbeanKey);
				// insert stub record
//				 xtable.Fcbean.add(fcbeanKey, i);
//				 xtable.Frcbean.add(frcbeanKey, i);
				xtable.Fboolean.add(fbooleanKey, i);
				xtable.Ffloat.add(floatKey, i);
				xtable.Fint.add(fintKey, i);
				xtable.Flong.add(flongKey, i);
				xtable.Fshort.add(fshortKey, i);
				xtable.Fstring.add(fstringKey, i);
				// gen xbean with foreignKey
				xbean.nativetype nativeValue = xbean.Pod.newnativetype();
				nativeValue.setB(fbooleanKey);
				nativeValue.setS(fshortKey);
				nativeValue.setI(fintKey);
				nativeValue.setL(flongKey);
				nativeValue.setF(floatKey);
				nativeValue.setText(fstringKey);
//				 nativeValue.setCbean(fcbeanKey);
//				 nativeValue.setRc(frcbeanKey);
				
				xbean.basecontainer baseValue = xbean.Pod.newbasecontainer();
//				baseValue.getMap().put(fcbeanKey, nativeValue.copy());
//				baseValue.getTreemap().put(fcbeanKey, nativeValue.copy());
				baseValue.getSet().add(nativeValue.copy());
				baseValue.getLmap().put(fintKey, flongKey);
				baseValue.getSlist().add(fstringKey);
				
				floatKey = new Random().nextFloat();
				fintKey = new Random().nextInt();
				flongKey = new Random().nextLong();
				fshortKey = Short.valueOf((short)new Random().nextInt(Short.MAX_VALUE / 2));
				fstringKey = String.valueOf(new Random().nextLong());
				xtable.Ffloat.add(floatKey, i);
				xtable.Fint.add(fintKey, i);
				xtable.Flong.add(flongKey, i);
				xtable.Fshort.add(fshortKey, i);
				xtable.Fstring.add(fstringKey, i);
				
				xtable.Vfloat.add(fshortKey, fintKey);
				xtable.Vint.add(flongKey, flongKey);
				xtable.Vlong.add(fintKey, fshortKey);
				xtable.Vshort.add(floatKey, fstringKey);
				xtable.Vstring.add(fstringKey, floatKey);
				
				baseValue.getLmap().put(fintKey, flongKey);
				baseValue.getSlist().add(fstringKey);
				nativeValue.copy().setL(flongKey);
				baseValue.getSet().add(nativeValue.copy());
				
				baseValue.getXxbean().setB(fbooleanKey);
				baseValue.getXxbean().setS(fshortKey);
				baseValue.getXxbean().setI(fintKey);
				baseValue.getXxbean().setL(flongKey);
				baseValue.getXxbean().setF(floatKey);
				baseValue.getXxbean().setText(fstringKey);
				
				xbean.baseType value = xbean.Pod.newbaseType();
				value.setI(fintKey);
				value.getPrimitive().setB(fbooleanKey);
				value.getPrimitive().setS(fshortKey);
				value.getPrimitive().setI(fintKey);
				value.getPrimitive().setL(flongKey);
				value.getPrimitive().setF(floatKey);
				value.getPrimitive().setText(fstringKey);
//				value.getPrimitive().setCbean(fcbeanKey);
//				value.getPrimitive().setRc(frcbeanKey);
				
				value.getContainer().getMap().put(flongKey, baseValue.copy());
				value.getContainer().getTreemap().put(fstringKey, baseValue.copy());
				value.getContainer().getSet().add(baseValue.copy());
				value.getContainer().getFmap().put(fintKey, floatKey);
				value.getContainer().getSlist().add(fshortKey);
				
				floatKey = new Random().nextFloat();
				fintKey = new Random().nextInt();
				flongKey = new Random().nextLong();
				fshortKey = Short.valueOf((short)new Random().nextInt(Short.MAX_VALUE / 2));
				fstringKey = String.valueOf(new Random().nextLong());
				xtable.Ffloat.add(floatKey, i);
				xtable.Fint.add(fintKey, i);
				xtable.Flong.add(flongKey, i);
				xtable.Fshort.add(fshortKey, i);
				xtable.Fstring.add(fstringKey, i);
				
				xtable.Vfloat.add(fshortKey, fintKey);
				xtable.Vint.add(flongKey, flongKey);
				xtable.Vlong.add(fintKey, fshortKey);
				xtable.Vshort.add(floatKey, fstringKey);
				xtable.Vstring.add(fstringKey, floatKey);

				baseValue.getSlist().add(fstringKey);
				value.getContainer().getMap().put(flongKey, baseValue.copy());
				baseValue.getLmap().put(fintKey, flongKey);
				value.getContainer().getTreemap().put(fstringKey, baseValue.copy());
				baseValue.getXxbean().setF(floatKey);
				value.getContainer().getSet().add(baseValue.copy());	
				value.getContainer().getFmap().put(fintKey, floatKey);
				value.getContainer().getSlist().add(fshortKey);
				
				value.getContainer().getXxbean().getSet().add(nativeValue.copy());
				value.getContainer().getXxbean().getXxbean().setB(fbooleanKey);
				value.getContainer().getXxbean().getXxbean().setS(fshortKey);
				value.getContainer().getXxbean().getXxbean().setI(fintKey);
				value.getContainer().getXxbean().getXxbean().setL(flongKey);
				value.getContainer().getXxbean().getXxbean().setF(floatKey);
				value.getContainer().getXxbean().getXxbean().setText(fstringKey);
				
				// insert foreign record
//				xtable.Bcbean.add(fcbeanKey, value.copy());
//				xtable.Brcbean.add(frcbeanKey, value.copy());
//				xtable.Bboolean.add(fbooleanKey, value.copy());
				xtable.Bfloat.add(floatKey, value.copy());
				xtable.Bint.add(fintKey, value.copy());
				xtable.Blong.add(flongKey, value.copy());
				xtable.Bshort.add(fshortKey, value.copy());
				xtable.Bstring.add(fstringKey, value.copy());
				
				xtable.Autotable.insert(flongKey);
				xtable.Auto1.insert(flongKey);
			}
			return true;
		}
	}
	
	class Walker implements mkdb.Storage.IWalk {
		private final DatabaseMetaData.Table tableMeta;
		private long count = 0;

		Walker(DatabaseMetaData.Table tableMetaData) {
			this.tableMeta = tableMetaData;
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			try {
				++ count;
				if (count < 100000) {
					Object key = tableMeta.getKeyType().unmarshal(_key);
					Object value = tableMeta.getValueType().unmarshal(_data);
					mkdb.Trace.info(tableMeta.getName() + " key=" + key + " value=" + value);
				}
				//assertFalse(table.exist(new OctetsStream().marshal(2L)));
				//table.remove(OctetsStream.wrap(Octets.wrap(_key)));
				//table.insert(new OctetsStream().marshal(40960000L - 4096), new OctetsStream().marshal(11));
				//table.insertIf(new OctetsStream().marshal(409600L - 4096), new OctetsStream().marshal(11));
			} catch (Exception e) {
				e.printStackTrace();
			}			
			return true;
		}
		
		public void finish() {
			if (count >= 5)
				System.out.println("	...");
			System.out.println(tableMeta.getName() + "	total=" + count);
		}		
	}
	
	class A {
		int i = 0;
		String s = "a";
		
		public A(int i, String s) {
			this.i = i;
			this.s = s;
		}
		
		public void setI(int i) {
			this.i = i;
		}
		
		public void setS(String s) {
			this.s = s;
		}
		
		public String toString() {
			return i + ":" + s;
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Test
	public void testCons() {
		List<A> cons = new ArrayList<A>();
		cons.add(new A(1, "a"));
		cons.add(new A(2, "b"));
		cons.add(new A(3, "c"));
		cons.add(new A(1, "d"));
		System.out.println(cons.size());
		System.out.println(cons);
		for (A obj : cons) {
			obj.setI(4);
			obj.setS("e");
		}
		System.out.println(cons);
		Iterator itr = cons.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			if (obj.equals(1))
				itr.remove();
			System.out.println(cons.size());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testMap() {
		Map map = new HashMap<Integer, String>();
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
		System.out.println(map);
		Iterator itr = map.entrySet().iterator();
        while (itr.hasNext()) {
        	Map.Entry entry = (Map.Entry)itr.next();
        	Object key = entry.getKey();
        	if (key.equals(2)) {
				map.put(2, "f");
			}
        }
        System.out.println(map);
        
        String varPath = "<valueforeign>";
        System.out.println(nextPath(varPath).isEmpty());
	}
	
    private String nextPath(String path) {
		if (path.indexOf('.') != -1)
			return path.substring(path.indexOf('.') + 1);
		else 
			return "";
	}
}