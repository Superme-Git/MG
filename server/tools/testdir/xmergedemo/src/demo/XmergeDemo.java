package demo;

import org.junit.*;

import com.goldhuman.Common.Octets;
import com.goldhuman.Common.Marshal.OctetsStream;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import xbean.acount;
import xdb.util.DatabaseMetaData;
import xdb.util.Dbx;
import xmerge.IAllocator;
import xmerge.IMerge;
import xmerge.Xmerge;

public class XmergeDemo {
	static int COUNT = 10;
	
	public void setUp() {
		xdb.Xdb.getInstance().stop();
	}
	
	public void createDb(String config) throws InterruptedException, ExecutionException {
		xdb.Xdb.getInstance().setConf(new xdb.XdbConf(config));
		xdb.Xdb.getInstance().getConf().setLibdbPath(new File("../../bin"));
		xdb.Xdb.getInstance().start();	
		new AddProc().submit().get();
		xdb.Xdb.getInstance().stop();
	}
 	
	class AddProc extends xdb.Procedure {
		@Override
		public boolean process() {
			
			for (int i = 0; i < COUNT; i++) {
				xtable.Task.add(i, i);
				// ÕÊ»§
				xbean.acount useracount = xbean.Pod.newacount();
				useracount.setYuanbao(new Random().nextInt(1000));
				// role2name
				String roleName = String.valueOf(new Random().nextInt(COUNT));
				String family = String.valueOf(new Random().nextInt(COUNT));
				int sp = beginAndSavepoint();
				if (xtable.Rolename.add(roleName, Long.valueOf(i)) && xtable.Family.add(family, Long.valueOf(i))) {
				} else {
					rollback(sp);
					continue;
				}
				commit();
				Long roleId = xtable.Role2name.insert(roleName);
				xtable.Name2family.add(roleName, family);
				useracount.getRoles().add(roleId);
				xtable.Useraccount.add(Long.valueOf(i), useracount);
			}
			return true;
		}
	}
	
	@Test
	public void testXmerge() throws InterruptedException, ExecutionException {		
		createDb("src.xml");
		createDb("dest.xml");
		
		xdb.Trace.set(xdb.Trace.DEBUG);
		xdb.util.DatabaseMetaData curMeta;	
		Xmerge tester = new Xmerge("xmerge.xml");
		curMeta = xdb.util.DatabaseMetaData.getInstance();
		
		tester.addMerger("useraccount", new IMerge() {
			@Override
			public boolean merge(OctetsStream src_key, OctetsStream src_value,
					OctetsStream dest_value) throws Exception {
				try {
					xbean.acount srcValue = (acount) DatabaseMetaData.getInstance().getTable("useraccount").getValueType().unmarshal(src_value);
					xbean.acount destValue = (acount) DatabaseMetaData.getInstance().getTable("useraccount").getValueType().unmarshal(dest_value);
					xdb.Trace.info(srcValue + ":" + destValue);
					destValue.setYuanbao(destValue.getYuanbao() + srcValue.getYuanbao());
					for (Long roleId : srcValue.getRoles()) {
						destValue.getRoles().add(roleId);
					}
					xdb.Trace.info(destValue);
					dest_value.swap(DatabaseMetaData.getInstance().getTable("useraccount").getValueType().marshal(destValue));
				} catch (Exception e) {
					xdb.Trace.info("merge fail.");
					e.printStackTrace();
				}
				return true;
			}			
		}); 
		for (DatabaseMetaData.Table tableMetaData : curMeta.getTables()) {
			if (!tableMetaData.isPersistence())
				continue;
			if (!tableMetaData.isAutoKey()) {
				switch(tableMetaData.getKeyType().getTypeId()) {
				case LONG:
					tester.addAllocator(tableMetaData.getName(), new IAllocator() {
						@Override
						public Object allocateKey(String arg0, Object arg1) {
							return new Random().nextLong();
						}						
					});
					break;
				case INT:
					tester.addAllocator(tableMetaData.getName(), new IAllocator() {
						@Override
						public Object allocateKey(String arg0, Object arg1) {
							return new Random().nextInt(1000);
						}						
					});
					break;
				case STRING:
					tester.addAllocator(tableMetaData.getName(), new IAllocator() {
						@Override
						public Object allocateKey(String arg0, Object arg1) {
							return String.valueOf(new Random().nextInt(100000));
						}						
					});
					break;
				default:
					throw new RuntimeException("unsupport type.");
				}
			}
		}
		tester.run();
	}
	
//	@Test
	public void testWalk() throws InterruptedException, ExecutionException {		
		xdb.util.DatabaseMetaData curMeta;
		File dbHome;
		xdb.util.Dbx db;
		
		curMeta = xdb.util.DatabaseMetaData.getInstance();
		dbHome = new File("test/result_db");
		xdb.util.Dbx.start("../../bin");		
		
		db = Dbx.open(dbHome, curMeta);
		for (DatabaseMetaData.Table tableMetaData : db.getMetaData().getTables()) {
			if (!tableMetaData.isPersistence())
				continue;
			db.openTable(tableMetaData.getName());
		}
		
		for (xdb.util.Dbx.Table table : db.getTables()) {
			Walker walker = new Walker(curMeta.getTable(table.getName()));
			table.walk(walker);
			walker.finish();
		}
		db.close();
	}
	class Walker implements xdb.Storage.IWalk {
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
					xdb.Trace.info(tableMeta.getName() + " key=" + key + " value=" + value);
				}
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
}