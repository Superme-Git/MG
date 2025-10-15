package cn.com.zc.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.com.zc.db.bean.PetScoreConfig;
import cn.com.zc.db.bean.PetinfoListBean;
import cn.com.zc.db.bean.RoleLevelAndMoneyBean;
import cn.com.zc.db.bean.RoseListBean;
import cn.com.zc.db.bean.mbean.export.ActiveRole;
import cn.com.zc.db.bean.mbean.export.ActiveUser;
import cn.com.zc.db.bean.mbean.export.ChargeUser;
import cn.com.zc.db.bean.mbean.export.ConsumeRole;
import cn.com.zc.db.bean.mbean.export.TotalValueState;
import cn.com.zc.db.util.FileManager;
import cn.com.zc.db.util.FileParser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class Main {
	
	
	public static BufferedWriter roleWriter = null;
	public static BufferedWriter userWriter = null;
	public static String SCANE_LOG_DIR_PATH = "scanlogs";
	public static String serverid = "0";
	
	public static int counter = 0;
	
	public List<PetScoreConfig> petConfig = null;
	
	public static void addCounter(){
		counter ++;
	}
	
	public void setPetScoreConfig(List<PetScoreConfig> petConfig){
		this.petConfig = petConfig;
	}
	
	public List<PetScoreConfig> getPetScoreConfig(){
		if(null == petConfig){
			setPetScoreConfig(FileParser.getPetScoreConfig());
		}
		
		return petConfig;
	}
	
	public static void main(String[] args) {
		if(args.length < 6){
			mkdb.Trace.error("24小时备份库扫描程序虚拟机参数为空");
			return;
		}
		File gsxdbproperties = new File("/root/server/gs/gsxdb.properties");
		if (gsxdbproperties.exists()){
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(gsxdbproperties)));
			    String line = br.readLine();
			    int loopnum = 20;
			    while (line!=null&&loopnum>0) {
			    	loopnum--;
			    	line = line.trim();
					if (line.contains("zoneid")){
						serverid = line.substring(line.length()-4);
						break;
					}else {
						line = br.readLine();
					}
				} 
			} catch (Exception e) {
				mkdb.Trace.error("gsxdbproperties file not exist",e);		
			}finally{
				if (br!=null)
					try {
						br.close();
					} catch (IOException e) {
						mkdb.Trace.error("gsxdbproperties file not exist",e);							
					}
			}
			
		}else {
			mkdb.Trace.info("gsxdbproperties file not exist");
		}
		//在此捕捉一些运行时异常来打印log
		//这样每天查看log就可以知道扫描备份库是否有错
		try{
			executeScan(args);
		}catch (Exception e) {
			mkdb.Trace.error("扫描备份库出错 ： ", e);
		}
		
	}
	

	private static void executeScan(String[] args) {
		String basePath = args[0];
		String configPath = args[1];
		String outFilePath = args[2];
		int argsInt = Integer.parseInt(args[3]);
		String xbackFilePath = args[4];
		String rankListFilePath = args[5];
		DbxManager manager = DbxManager.getInstance();
		manager.initDbx(basePath, configPath, argsInt, xbackFilePath);
		if(!manager.isDbxStarted())
			return;
		
		if(manager.isScanedRecently(argsInt))
		{
			mkdb.Trace.info("最近的数据有扫描过，不需要再次扫描，查看日志：" + manager.getXdbBakName() + ".log");
			return;
		}
		
		String logBaseDir = initBaseDir(rankListFilePath);
		
		//TMP给策划生成csv文件
		//createLogTmpCSVFile();
		FileManager.getInstance().setFileBasePath(outFilePath);
		
		manager.doWalker();
		
		//回写快照文件
		if((argsInt & Constant.QUICK_PHOTO) == Constant.QUICK_PHOTO || argsInt == 0){
			FileManager.getInstance().writeQuickPhotoFile();
		}
	
		//TMP刷新策划的csv文件
		//closeFileStream();
		
		//生成策划Log文件
		if((argsInt & Constant.STATE_LOG) == Constant.STATE_LOG || argsInt == 0){
			createLogStateFile(logBaseDir);
		}

		if((argsInt & Constant.SORT_ROLE_LEVEL) == Constant.SORT_ROLE_LEVEL ||(argsInt & Constant.SORT_MONEY) == Constant.SORT_MONEY || argsInt == 0){
			createRanListFile(logBaseDir, 2);
		}
		
		if((argsInt & Constant.SORT_ROSE_NUM) == Constant.SORT_ROSE_NUM || argsInt == 0){
			createRanListFile(logBaseDir, 3);
		}
		
		if((argsInt & Constant.PET_SCORE) == Constant.PET_SCORE || argsInt == 0){
			createRanListFile(logBaseDir, 4);
		}
		
		//评分
		if ((argsInt & Constant.QUICK_PHOTO) == Constant.QUICK_PHOTO
				|| argsInt == 0){
			ScoreSnapShotHelper.toFile(FileManager.getInstance().getQuickPhotoFilePath(),"score.csv.begin");
		}
		
		//记录本次扫描
		logExecute(manager.getXdbBakName(), argsInt);
		
	}

	private static String initBaseDir(String rankListFilePath) {
		File dir = new File(rankListFilePath + FileManager.getInstance().getRankListFilePath());
		if(!dir.exists() || !dir.isDirectory()){
			dir.mkdir();
		}
		
		return dir.getAbsolutePath();
	}


	private static void createRanListFile(String basePath, int type) {
//		File Dir = new File(basePath + "/ranklist/");
//		if(!Dir.exists() || !Dir.isDirectory())
//			Dir.mkdir();
		
		XStream xStream = new XStream(new DomDriver());
		setClassAlias(xStream);
		File file = new File(basePath + getFileNameByType(type)); 
		OutputStream os = null;
		try{
			if(!file.exists())
				file.createNewFile();
				
			os = new FileOutputStream(file);
			os.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n".getBytes());
			
		}catch (Exception e) {
		}
		
		switch (type) {
		case 3:
			RoseListBean.getInstance().setTick(getCurrentTick());
			xStream.toXML(RoseListBean.getInstance(), os);
			break;

		case 2:
			RoleLevelAndMoneyBean bean = BeanCollection.getInstance().fillRoleLevelAndMoney();
			xStream.toXML(bean, os);
			break;
			
		case 4:
			PetinfoListBean.getInstance().setTick(getCurrentTick());
			xStream.toXML(PetinfoListBean.getInstance(), os);
			break;
			
		default:
			break;
		}
		
		if(os != null){
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private static String getFileNameByType(int type) {
		switch (type) {
		case 1:
			return "/rolemoney.xml";

		case 2:
			return "/rolelevel.xml";
			
		case 3:
			return "/rosenum.xml";
			
		case 4:
			return "/petscore.xml";
			
		default:
			break;
		}
		
		return "";
	}

	private static String getCurrentTick() {
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy年M月d日 HH:mm");
		String currentTime = fomat.format(System.currentTimeMillis());
		return currentTime;
	}

	private static void createLogStateFile(String path) {
		TotalValueState state = StatisDateManager.getInstance().fillExportBean();
		XStream xStream = new XStream(new DomDriver());
		setClassAlias(xStream);
//		File activeDir = new File(path + "/logs/");
//		if(!activeDir.exists() || !activeDir.isDirectory())
//			activeDir.mkdir();
		
		File active = new File(path + "/active.xml");
		try {
			if(active.exists()){
				active.delete();
			}
			active.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		OutputStream os = null;
		try {
			os = new FileOutputStream(active);
			xStream.toXML(state, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(os != null){
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private static void setClassAlias(XStream xStream) {
		xStream.alias("fire.log.beans.TotalValueState", TotalValueState.class);
		xStream.alias("fire.log.beans.ConsumeRole", ConsumeRole.class);
		xStream.alias("fire.log.beans.ChargeUser", ChargeUser.class);
		xStream.alias("fire.log.beans.ActiveUser", ActiveUser.class);
		xStream.alias("fire.log.beans.ActiveRole", ActiveRole.class);
		xStream.alias("fire.gsp.ranklist.proc.RoleLevelAndMoneyBean", cn.com.zc.db.bean.RoleLevelAndMoneyBean.class);
		xStream.alias("fire.gsp.ranklist.proc.RoseListBean", cn.com.zc.db.bean.RoseListBean.class);
		xStream.alias("fire.gsp.ranklist.proc.RoseRecordBean", cn.com.zc.db.bean.RoseRecordBean.class);
		xStream.alias("fire.gsp.ranklist.proc.RoleMoneyBean", cn.com.zc.db.bean.RoleMoneyBean.class);
		xStream.alias("fire.gsp.ranklist.proc.RoleLevelBean", cn.com.zc.db.bean.RoleLevelBean.class);
		xStream.alias("fire.gsp.ranklist.proc.PetinfoListBean", cn.com.zc.db.bean.PetinfoListBean.class);
		xStream.alias("fire.gsp.ranklist.proc.PetInfoBean", cn.com.zc.db.bean.PetInfoBean.class);
		
	}

	private static void closeFileStream() {
			try {
				roleWriter.flush();
				userWriter.flush();
				roleWriter.close();
				userWriter.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}

	private static void createLogTmpCSVFile() {
		//FileManager.getInstance().writeQuickPhotoFile();
		
		File activeRoleFile = new File("ActiveRole.txt");
		if(activeRoleFile.exists()){
			activeRoleFile.delete();
		}
		
		File activeUserFile = new File("ActiveUser.txt");
		if(activeUserFile.exists()){
			activeUserFile.delete();
		}

		
		try {
			activeRoleFile.createNewFile();
			activeUserFile.createNewFile();
			
			roleWriter = new BufferedWriter(new FileWriter(activeRoleFile));
			userWriter = new BufferedWriter(new FileWriter(activeUserFile));
			
			roleWriter.write(getRoleFirstLine() + "\n");
			userWriter.write(getUserFirstLine() + "\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static String getUserFirstLine() {
		StringBuffer sb = new StringBuffer();
		sb.append("userid" + ",");
		sb.append("firsttimeEnter" + ",");
		sb.append("lasttimeEnter" + ",");
		sb.append("lasttimeActiveDay1" + ",");
		sb.append("lasttimeActiveDay2" + ",");
		sb.append("lasttimeActiveWeek1" + ",");
		sb.append("lasttimeActiveWeek2" + ",");
		sb.append("firsttimeActiveDay" + ",");
		sb.append("firsttimeActiveWeek" + ",");
		sb.append("lasttimeChargeDay1" + ",");
		sb.append("lasttimeChargeDay2" + ",");
		sb.append("lasttimeChargeWeek1" + ",");
		sb.append("lasttimeChargeWeek2" + ",");
		sb.append("firsttimeChargeDay" + ",");
		sb.append("firsttimeChargeWeek" + ",");
		sb.append("lasttimeChargeDay" + ",");
		sb.append("lasttimeChargeDayTotalAmount" + ",");
		sb.append("lasttimeChargeWeek" + ",");
		sb.append("lasttimeChargeWeekTotalAmount" + ",");
		sb.append("chargeTotalAmount" + ",");
		
		return sb.toString();
	}
	
	private static String getRoleFirstLine(){
		StringBuffer sb = new StringBuffer();
		sb.append("roleid" + ",");
		sb.append("createTime" + ",");
		sb.append("enterWorldTime" + ",");
		sb.append("dayOnline" + ",");
		sb.append("dayOnlineTime" + ",");
		sb.append("weekOnline" + ",");
		sb.append("weekOnlineTime" + ",");
		sb.append("lasttimeActiveDay1" + ",");
		sb.append("lasttimeActiveDay1OnlineTime" + ",");
		sb.append("lasttimeActiveDay2" + ",");
		sb.append("lasttimeActiveWeek1" + ",");
		sb.append("lasttimeActiveWeek1OnlineTime" + ",");
		sb.append("lasttimeActiveWeek2" + ",");
		sb.append("firsttimeActiveDay" + ",");
		sb.append("firsttimeActiveWeek" + ",");
		sb.append("fushiConsumeTotalAmount" + ",");
		sb.append("lasttimeFushiConsumeDay" + ",");
		sb.append("lasttimeFushiConsumeDayTotalAmount" + ",");
		sb.append("lasttimeFushiConsumeWeek" + ",");
		sb.append("lasttimeFushiConsumeWeekTotalAmount" + ",");
		sb.append("fushiBuyTotalAmount" + ",");
		sb.append("fushiSellTotalAmount" + ",");
		
		return sb.toString();
	}
	
	
	private static void logExecute(String xbackfilename, int arg)
	{
		File dir = new File(SCANE_LOG_DIR_PATH);
		if(!dir.exists())
			if(!dir.mkdirs())
				return;
		File logfile = new File(SCANE_LOG_DIR_PATH + File.separator + xbackfilename + ".log");
		if(logfile.exists())
			logfile.delete();
		FileWriter writer = null;
		try
		{
			if(!logfile.createNewFile())
				return;
			writer = new FileWriter(logfile);
			
			writer.write(String.valueOf(arg));
			writer.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (writer != null)
					writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}

