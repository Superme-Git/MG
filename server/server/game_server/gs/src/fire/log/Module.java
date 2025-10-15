package fire.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fire.log.beans.TotalValueState;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.main.LoginLimit;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

//保留LOG模块的Module接口，以后可能会在初始化过程中解析配置表里的日志参数来生成RemoteLogID里的所有常量
public class Module implements ModuleInterface {

	public static final int LIMIT_NORMAL_MODE = 0;  //正常模式
	
	public static final int LIMIT_WHITELIST_MODE = 1; //白名单模式
	 
	public static final int LIMIT_BLACLIST_MODE = 2; //黑名单模式
	
	private static Map<String, Integer> paramid2name = new HashMap<String, Integer>();
	
	public static Logger logger = Logger.getLogger("SYSTEM");
	
	private static TotalValueState logAnalyser;
	
	public static void analyseLogFromXml(){
		XStream xStream = new XStream(new DomDriver());
		File xmlFile = new File("scanfiles/logs/active.xml");
		System.out.println(xmlFile.getAbsolutePath());
		if(!xmlFile.exists())
			return;
		
		try{
			logAnalyser = (TotalValueState) xStream.fromXML(new FileInputStream(xmlFile));
		}catch (com.thoughtworks.xstream.converters.ConversionException e) {
			logger.error("运营日志文件Bean的格式有错" + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("运营日志文件目录不存在或者文件不存在！！！" + e.getMessage());
		}
	}
	
	public static TotalValueState getLogAnalyser(){
		if(null == logAnalyser){
			return new TotalValueState();
		}
		return logAnalyser;
	}
	
	//屏蔽字列表
	private static List<String> normalValidList;
	private static List<String> npcValidList;
	
	public static List<String> getNormalValidList(){
		return normalValidList;
	}
	
	public static List<String> getNpcValidList(){
		return npcValidList;
	}
	
	public static void removeNormalValidWord(String word){
		for(int i = 0;i < normalValidList.size();i++){
			if(normalValidList.get(i).equals(word))
				normalValidList.remove(i);
		}
	}
	public static void addNormalValidWord(String word){
		normalValidList.add(word);
	}
	
	/////////////////////////////////////////////////
	//火星文字符串
	public static final String SPECIAL_STRING = "。、丶〆╰つ╮ヽ∝°〝り≈ァ＇№↘↙←→のっ￡⊿△▽╭↓◇ジ§﹌ザ︶ㄣ☆⊕○◎⊙Θ〞∮づレ※しιふぶ︵┿┊√〃∷いらんごζ♂ベこ≮≯ㄟ灬ぇぃ︷ミ︿╯乆ゐ＊乷ξぷあ∠∫¢τヤぅべぜˇぺ゛゜「」↑↗↖─│╱╲╳¤♀〤〩ぁえぉおかがきぎくぐけげさざじすずせそぞただちぢてでとどなにぬねはばぱひびぴへほぼぽまみむめもゃやゅゆょよるれろゎわゑを";
	public static int[] SPECIAL_CHAR_ARRAY;
	
	@Override
	public void exit() {
		
		//回写屏蔽字文件
		fire.pb.util.StringValidateUtil.writeValiateListToFile(normalValidList, getValidFilePath() + "/chatshield.txt");
		fire.pb.util.StringValidateUtil.writeValiateListToFile(npcValidList, getValidFilePath() + "/nameshield.txt");

	}

	@Override
	public void init() throws Exception {
		//生成log接口和参数的java文件
		//createLogInterfaceAndParam();
		
		//读取屏蔽字文件
		normalValidList = fire.pb.util.StringValidateUtil.readValidateFile(getValidFilePath() + "/chatshield.txt");
		npcValidList = fire.pb.util.StringValidateUtil.readValidateFile(getValidFilePath() + "/nameshield.txt");
		
		//读取服务器检查userid
		initLoginLimitInfo();
		
		//火星文字符转换
		int length = SPECIAL_STRING.toCharArray().length;
		SPECIAL_CHAR_ARRAY = new int[length];
		for(int i=0; i<length; i++){
			SPECIAL_CHAR_ARRAY[i] = SPECIAL_STRING.charAt(i);
		}
		
	}
	

	/**
	 * 获取黑名单或者白名单列表
	 */
	private void initLoginLimitInfo() {
		fire.pb.main.ConfigManager manager = fire.pb.main.ConfigManager.getInstance();
		Map<Integer, LoginLimit> limitMap = manager.getConf(LoginLimit.class);
		if(null == limitMap){
			manager.setLoginLimitType(LIMIT_NORMAL_MODE);
			manager.getUserIDList().clear();
			return;
		}

		@SuppressWarnings("unused")
		int mySoneId = ConfigManager.getGsZoneId();
		boolean isMyServerInList = false;

		int serverid = Integer.parseInt(Gs.serverid);

		for(LoginLimit current : limitMap.values()){
//			if(current.getServerid() != mySoneId)
//				continue;

			if (current.getServerid() != serverid) {
				continue;
			}

			int type = current.getLoginlimittype();
			String[] userids = null;
			manager.setLoginLimitType(type);
			manager.setErrorType(current.getMsgid());
			if(type == LIMIT_WHITELIST_MODE){
				String whiteList = current.getWhitelist();
				if(whiteList == null || whiteList.equals(""))
					break;
				userids = whiteList.split(";");
			}else if(type == LIMIT_BLACLIST_MODE){
				String blackList = current.getBlacklist();
				if(blackList == null || blackList.equals(""))
					break;
				userids = current.getBlacklist().split(";");
			}
			
			isMyServerInList = true;
			manager.getUserIDList().clear();
			if(userids != null){
				for(int i=0; i<userids.length; i++){
					manager.getUserIDList().add(Integer.parseInt(userids[i]));
				}
			}
			break;
		}
		
		if(!isMyServerInList){
			manager.setLoginLimitType(LIMIT_NORMAL_MODE);
			manager.getUserIDList().clear();
		}
		
	}

	public static Integer getParamHashCodeByName(String name){
		return paramid2name.get(name);
	}
	
	private String getValidFilePath(){
		StringBuffer sb = new StringBuffer();
		//sb.append(System.getProperty("user.dir"));
		sb.append("gamedata/files");
		
		return sb.toString();
	}

	public static String getExpQuickPhotoHead(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("pathway,");
		stringBuffer.append("levelspace,");
		stringBuffer.append("value");
		return stringBuffer.toString();
	}
	
	
	@Override
	public ReloadResult reload() throws Exception
	{
		initLoginLimitInfo();
		return new ReloadResult(true);
	}
	
}
