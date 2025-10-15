package locojoy.game.mt3.utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * ���������ļ�
 * 
 * @author liangyanpeng
 *
 */
public class Conf {
	
	private static final Logger LOG = Logger.getLogger(Conf.class);
	
	public static Properties properties = new Properties();
	public static final String APP_CONF = "/conf.properties";

	/**
	 * ��ʼ����Ŀ���ó���
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
	 * @return ����ʶ��洢��ַ
	 */
	public static String getIatVoiceDir() {
		return properties.getProperty("iat_voice_dir");
	}
	
	/**
	 * @return ���������洢��ַ
	 */
	public static String getTalVoiceDir() {
		return properties.getProperty("tal_voice_dir");
	}
	
	/**
	 * @return ����ʶ���������ӵ�ַ
	 */
	public static String getDownIatLink() {
		return properties.getProperty("down_iat_link");
	}
	
	/**
	 * @return ���������������ӵ�ַ
	 */
	public static String getDownTalLink() {
		return properties.getProperty("down_tal_link");
	}
	
	/**
	 * @return ���������߳�����
	 */
	public static Integer getThreadNum() {
		return Integer.parseInt(properties.getProperty("thread_num"));
	}
	
	/**
	 * @return Ѷ��appid
	 */
	public static String getIflyAppid() {
		return properties.getProperty("iflyAppid");
	}
	
	/**
	 * @return Ѷ������ʶ���ַ
	 */
	public static String getIflyUrl() {
		return properties.getProperty("iflyUrl");
	}
	
	/**
	 * @return ʶ�����ִ��ļ���չ��
	 */
	public static String getWordageExtension() {
		return properties.getProperty("wordageExtension");
	}
	
	/**
	 * @return ����ģʽ     true����ģʽ   false����ģʽ
	 */
	public static boolean getIsDebug() {
		String  aa = properties.getProperty("debug", "0");
		int flag = Integer.parseInt(aa);
		return flag == 1 ? true : false;
	}
	
	/**
	 * @return ս���طŴ洢��ַ
	 */
	public static String getPlayBackDir() {
		return properties.getProperty("battle_playback_dir");
	}
	
	/**
	 * @return ����ʶ���������ӵ�ַ
	 */
	public static String getDownPlayBackLink() {
		return properties.getProperty("down_playback_link");
	}
	
	public static void main(String[] args) {
		Conf.initConf();
		System.out.println(Conf.getDownIatLink());
	}
}
