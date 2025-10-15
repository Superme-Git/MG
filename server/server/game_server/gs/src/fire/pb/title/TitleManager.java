
package fire.pb.title;

import org.apache.log4j.Logger;

public class TitleManager {
	private static final Logger logger = Logger.getLogger("AWARD");
	private static java.util.NavigableMap<Integer, STitleConfig>  titleMap;
	
	TitleManager(fire.pb.main.ConfigManager cm){
		titleMap = cm.getConf(fire.pb.title.STitleConfig.class);
		if (null == titleMap){
			throw new RuntimeException("缺少称谓表配置文件!!!");
		}
		if (logger.isInfoEnabled()) {
			logger.info("称谓表服务配置表加载完毕。称谓配置文件一共配置称谓 " + titleMap.size() + "个");
		}
	}
	

	//检查称谓系统中是否有titleid这个称号
	public static boolean existTitle(int titleid){
		if (null == titleMap)
			return false;
		return titleMap.containsKey(titleid);
	}
	

	//通过称谓id获得相关称谓信息
	public static STitleConfig getTitleConfigById(int titleid){
		if(null == titleMap)
			return null;
		
		return titleMap.get(titleid);
	}
	
}
