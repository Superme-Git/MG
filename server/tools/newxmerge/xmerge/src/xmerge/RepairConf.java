package xmerge;

import java.io.File;
import java.util.Map;

import xmerge.ForeignMeta.TableDepends;

public class RepairConf {
	public static final long MAX_ALLOCATE_COUNT = 1024;
	private String tempErrorKeysDbHome;
	private String workDbHome;
	private String otherDbHome;
	
	private TableStatistics statistic = new TableStatistics();
	private ForeignMeta refMeta = new ForeignMeta();
	private static RepairConf conf = new RepairConf();  
	
	private RepairConf() {}
	public static RepairConf getInstance() { return conf; }
	
	public void initialize() {
		conf.setTempErrorKeysDbHome("temp_db");
		File tempDbDir = new File(conf.tempErrorKeysDbHome);
		tempDbDir.mkdir();
		new File(tempDbDir, mkdb.MkdbConf.DATADIR).mkdir();	
		refMeta.initDepRoots();
	}
	
	public ForeignMeta getForeignMeta() { return refMeta; }
	public Map<String, TableDepends> getDepRoots() {  return refMeta.getDepRoots(); }
	public TableStatistics getStatistic() { return statistic; }
	public void setStatistic(TableStatistics statistic) { this.statistic = statistic; }	
	
	public String getResultDbHome() { return XmergeConf.getInstance().getResultDbHome(); }
	public String getTempErrorKeysDbHome() { return tempErrorKeysDbHome; }	
	public String getWorkDbHome() { return workDbHome; }
	public String getOtherDbHome() { return otherDbHome; }
	
	public void setTempErrorKeysDbHome(String tempErrorKeysDbHome) { 
		this.tempErrorKeysDbHome = tempErrorKeysDbHome;
	}
	public void setWorkDbHome(String dbHome) { this.workDbHome = dbHome; }
	public void setOtherDbHome(String dbHome) { this.otherDbHome = dbHome; }
}