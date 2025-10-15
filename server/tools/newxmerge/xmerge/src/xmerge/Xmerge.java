package xmerge;

import java.io.File;

import validator.ForeignValidator;
import mkdb.util.Dbx;

public class Xmerge {
	private XmergeConf conf;
	private Merger merger;
	private ICheckDupKey checker;
	private Repair repairer;
	private boolean checkOnly = false;
	private boolean makeForeignDbInside = false;
	
	public Xmerge(String confHome) {
		XmergeConf.SetConf(confHome);
		this.conf = XmergeConf.getInstance();
		this.merger = new Merger();
		this.checker = new CheckDupKey();
		this.repairer = new Repair();
	}
	
	public void addMerger(String tableName, IMerge merger) {
		this.merger.addIMerge(tableName, merger);
	}
	
	public void addAllocator(String tableName, IAllocator allocator) {
		repairer.addAllocator(tableName, allocator);
	}
	
	public void setSrcDbHome(String dbHome) { conf.setSrcDbHome(dbHome); }
	public void setDestDbHome(String dbHome) { conf.setDestDbHome(dbHome); }	
	public void setResultDbHome(String dbHome) { conf.setResultDbHome(dbHome); }	
	public void setForeignDbHome(String dbHome) { conf.setForeignInfoDbHome(dbHome); }
	public void setForeignTableName(String tableName) { conf.setForeignInfoTableName(tableName); }
	
	private void destory() {
		if (!checkOnly && null != conf.getErrorKeysDbHome()) {
			Util.deleteDir(new File(conf.getErrorKeysDbHome()));
		}
		if (makeForeignDbInside && null != conf.getForeignInfoDbHome()) {
			xmerge.Util.deleteDir(new File(conf.getForeignInfoDbHome()));
		}
		xmerge.Util.deleteDir(new File(RepairConf.getInstance().getTempErrorKeysDbHome()));
	}
	
	public void run() {
		run(false);
	}
	
	public boolean run(boolean checkOnly) {
		Dbx.start(conf.getLibPath());
		this.checkOnly = checkOnly;
		
		try {
			if (checkOnly) {
				return checkDb();
			}
			mkdb.Trace.info("Merge Start");
			if (mergeOnce()) {
				mkdb.Trace.info("Perfect... ... Merge OK, Output DB is " + merger.getResultDBDir());
				return true;
			}
			mkdb.Trace.info("Merge Error goto Check... ...");
			mkdb.Trace.info("merge error.both db has been backuped at Folder backup");
			merger.rollbackAll();			
			
			if (!checkDb()) {
				return false;
			}
			mkdb.Trace.info("Check OK goto Repair... ...");
			conf.setErrorKeysDbHome(checker.getDupKeyDBPath());
			if(null == conf.getForeignInfoDbHome())
				makeForeignDb();				
			
			if (!repairer.repairDb()) {
				mkdb.Trace.info("Repair Error...");
				return false;
			}
			mkdb.Trace.info("Repair OK goto merge... ...");
			
			if (!mergeAgain()) {
				mkdb.Trace.info("merge error.both db has been backuped at Folder backup");
				merger.rollbackAll();
				return false;
			}
			mkdb.Trace.info("Merge after repair OK... ...");
			mkdb.Trace.info("OUTPUTDIR is " + merger.getResultDBDir());
			return true;			
		} catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			Dbx.stop();
			destory();
		}
	}

	private boolean mergeOnce() throws Exception {
		merger.SetSrcDB(conf.getSrcDbHome());
		merger.SetDestDB(conf.getDestDbHome(), false);
		mkdb.Trace.info("mergeOnce:src1db:"+merger.getSrcDBDir()+"src2db:"+merger.getDestDBDir()+"resultdb:"+merger.getResultDBDir());
		if (merger.doMerge()) {
			return true;
		}
		mkdb.Trace.info("mergeOnce Error...");
		return false;
	}
	
	private boolean mergeAgain() throws Exception {
		merger.SetSrcDB(conf.getSrcDbHome());
		merger.SetDestDB(repairer.getResultDbHome(), true);
		mkdb.Trace.info("mergeAgain:src1db:"+merger.getSrcDBDir()+"src2db:"+merger.getDestDBDir()+"resultdb:"+merger.getResultDBDir());
		if (merger.doMerge()) {
			return true;
		}
		mkdb.Trace.info("mergeAgain Error...");		
		return false;
	}

	private void makeForeignDb() {
		ForeignValidator validator = new ForeignValidator();
		validator.setDB(checker.getDupKeyDBPath());
		validator.setOutputDir(XmergeConf.ERRKEYFOREIGNDIR);
		mkdb.Trace.info("validate errKeyDB start... ...");
		validator.doValidate();
		mkdb.Trace.info("validate errKeyDB over... ...");
		conf.setForeignInfoDbHome(XmergeConf.ERRKEYFOREIGNDIR);
		conf.setForeignInfoTableName(XmergeConf.FOREIGN_TABLE_NAME);
		makeForeignDbInside = true;
	}

	private boolean checkDb() {
		if (checker.checkDB(conf.getSrcDbHome(), conf.getDestDbHome())) {
			mkdb.Trace.info("Check output errKeyDB: " + checker.getDupKeyDBPath());
			return true;
		}
		mkdb.Trace.info("Check Error...");
		return false;
	}	
}