package locojoy.game.mt3.servlet;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import locojoy.game.mt3.timer.TokenTimer;
import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.FileUtils;

/**
 * ��������������ֹͣ
 * 
 * @author liangyanpeng
 * 
 */
@WebListener
public class AppContext implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(AppContext.class);
	public static final String APP_CONF = "/log4j.properties";
//	private Timer timer = new Timer("token��name��ʱ��", true);
	/**
	 * Default constructor.
	 */
	public AppContext() {
		PropertyConfigurator.configure(FileUtils.getClassPath() + APP_CONF);
		LOG.debug("��ʼ��������");
	}

	/**
	 * ����ֹͣ
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		LOG.debug("<<<<<<����ֹͣ��ʼ>>>>>>");
//		timer.cancel();
		LOG.debug("<<<<<<����ֹͣ����>>>>>>");
	}

	/**
	 * ��������
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		LOG.debug(">>>>>>����������ʼ<<<<<<<");
		Conf.initConf();
//		if (!Conf.getIsDebug()) {
//			timer.scheduleAtFixedRate(new TokenTimer(), 500, 60 * 60 * 1000);
//			//		timer.scheduleAtFixedRate(new NameTimer(), 600, 60 * 1000); 
//		}
		LOG.debug(">>>>>>�����������<<<<<<<");
	}

}
