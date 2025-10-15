package fire.pb.main;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;

import org.apache.log4j.Logger;

import mkdb.TTable;
import mkdb.Table;
import mkdb.Mkdb;
import mkdb.MkdbConf;
import mkdb.util.UniqName;

public class MkdbModule implements ModuleInterface {
	private static Logger logger = Logger.getLogger("SYSTEM");
	private Mkdb mkdb;

	@Override
	public void exit() {
		// mkdb.util.ConcurrentCaches.stop();
		if (mkdb != null)
			mkdb.stop();
		logger.info("mkdb已关闭");
	}

	@Override
	public void init() throws Exception {

		final MkdbConf conf = new mkdb.MkdbConf("gsx.mkdb.xml");
		mkdb = Mkdb.getInstance();
		mkdb.setConf(conf);
		conf.getDbHome().mkdirs();
		conf.getBackupDir().mkdirs();
		UniqName.initialize();
		// if file mkdb.inuse exist,delete it
		File inUse = new File("mkdb/mkdb.inuse");
		if (inUse.exists())
			inUse.delete();

		mkdb.start();
		// mkdb.util.ConcurrentCaches.start();
		new fire.pb.WorldEventTab().start();
	}

	@Override
	public ReloadResult reload() throws Exception {
		return new ReloadResult(false, "module" + this.getClass().getName() + "not support reload");
	}

	public void printTableCacheInfo(long useMemory, long freeMemory) {
		try {
			Calendar cal = Calendar.getInstance();
			StringBuilder sb = new StringBuilder();
			sb.append("Cache");
			sb.append(String.valueOf(cal.get(Calendar.MONTH) + 1)).append("_");
			sb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH))).append("_");
			sb.append(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))).append("_");
			sb.append(String.valueOf(cal.get(Calendar.MINUTE))).append("_");
			sb.append(String.valueOf(useMemory)).append("_");
			sb.append(String.valueOf(freeMemory)).append("_");
			PrintWriter fw = null;
			try {
				File cacheFilesFolder = new File("cachefiles");
				if (!cacheFilesFolder.exists())
					cacheFilesFolder.mkdir();
				if (cacheFilesFolder.list().length > 100) {
					for (File subFile : cacheFilesFolder.listFiles()) {
						subFile.delete(); // 子文件肯定不会是文件夹
					}
				}
				fw = new PrintWriter(new File(cacheFilesFolder, sb.toString()));
				// 表头
				fw.write(addBlank("TableName", 32) + "\t");
				fw.write(addBlank("Memory", 6) + "\t");
				fw.write("Cache Cap" + "\t");
				fw.write("Cache Size" + "\t");
				fw.write("Count Add" + "\t");
				fw.write("Count Add Miss" + "\t");
				fw.write("Count Add Stor" + "\t");
				fw.write("Count Get" + "\t");
				fw.write("Count Get Miss" + "\t");
				fw.write("Count Get Stor" + "\t");
				fw.write("Count RMV" + "\t");
				fw.write("Count RMV Miss" + "\t");
				fw.write("Count RMV Stor" + "\t");
				fw.write("\n");
				Collection<Table> tables = mkdb.getTables().getTables();
				for (Table table : tables) {
					if (!(table instanceof TTable))
						continue;
					@SuppressWarnings("rawtypes")
					TTable tTable = (TTable) table;
					fw.write(addBlank(table.getName(), 32) + "\t");
					fw.write(addBlank(String.valueOf(table.getPersistence()), 6) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCacheCapacity()), 9) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCacheSize()), 10) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountAdd()), 9) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountAddMiss()), 14) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountAddStorageMiss()), 14) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountGet()), 9) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountGetMiss()), 14) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountGetStorageMiss()), 14) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountRemove()), 9) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountRemoveMiss()), 14) + "\t");
					fw.write(addBlank(String.valueOf(tTable.getCountRemoveStorageMiss()), 14) + "\t");
					fw.write("\n");
				}
				fw.flush();
			} catch (Exception e) {
				logger.error("print table cache failed", e);

			} finally {
				if (fw != null)
					fw.close();
			}
		} catch (Throwable e) {
			logger.error("print table cache failed", e);
		}
	}

	private String addBlank(String str, int len) {
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() < len) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
