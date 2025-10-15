package locojoy.game.mt3.utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 加载配置文件
 * 
 * @author liangyanpeng
 *
 */
public class Conf {
	
	private static final Logger LOG = Logger.getLogger(Conf.class);
	
	public static Properties properties = new Properties();
	public static final String APP_CONF = "/conf.properties";

	/**
	 * 初始化项目配置常量
	 */
	public static void initConf() {
		Reader reader = null;
		try {
			reader = new FileReader(new File(FileUtils.getClassPath() + APP_CONF));
			properties.load(reader);
		} catch (Exception e) {
			LOG.error("Read " + APP_CONF + " ", e);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (Exception e) {
					LOG.error("Close configuration " + APP_CONF + " stream ", e);;
				}
			}
		}
	}
	
	/**
	 * @return 语音识别存储地址
	 */
	public static String getIatVoiceDir() {
		return properties.getProperty("iat_voice_dir");
	}
	
	/**
	 * @return 聊天语音存储地址
	 */
	public static String getTalVoiceDir() {
		return properties.getProperty("tal_voice_dir");
	}
	
	/**
	 * @return 语音识别下载链接地址
	 */
	public static String getDownIatLink() {
		return properties.getProperty("down_iat_link");
	}
	
	/**
	 * @return 聊天语音下载链接地址
	 */
	public static String getDownTalLink() {
		return properties.getProperty("down_tal_link");
	}
	
	/**
	 * @return 语音翻译线程数量
	 */
	public static Integer getThreadNum() {
		return Integer.parseInt(properties.getProperty("thread_num"));
	}
	
	/**
	 * @return 讯飞appid
	 */
	public static String getIflyAppid() {
		return properties.getProperty("iflyAppid");
	}
	
	/**
	 * @return 讯飞语音识别地址
	 */
	public static String getIflyUrl() {
		return properties.getProperty("iflyUrl");
	}
	
	/**
	 * @return 识别文字存文件扩展名
	 */
	public static String getWordageExtension() {
		return properties.getProperty("wordageExtension");
	}
	
	/**
	 * @return 服务模式     true生产模式   false调试模式
	 */
	public static boolean getIsDebug() {
		String  aa = properties.getProperty("debug", "0");
		int flag = Integer.parseInt(aa);
		return flag == 1 ? true : false;
	}
	
	/**
	 * @return 战斗回放存储地址
	 */
	public static String getPlayBackDir() {
		return properties.getProperty("battle_playback_dir");
	}
	
	/**
	 * @return 语音识别下载链接地址
	 */
	public static String getDownPlayBackLink() {
		return properties.getProperty("down_playback_link");
	}
	
	public static void main(String[] args) {
		Conf.initConf();
		System.out.println(Conf.getDownIatLink());
	}
}
