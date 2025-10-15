package fire.log;

import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import mkdb.Procedure.Task;
import mkio.Manager;
import mkio.Mkio;

import org.apache.log4j.Logger;


/**
 * 这个仅仅是用来给运营记录日志用的。调试用的日志不需要用这个纪录。请用Log4j
 * 
 *
 */
public class LogManager extends Manager {
	public static Logger logger = Logger.getLogger("SYSTEM");

	public static Long showLogGMRoleid = null;
	public static boolean isShowLog = false;
	
	private static LogManager instance = new LogManager();

	private String gameserverid = "";//LogManager.LOG_GAMEID+" "+LogManager.LOG_SERVERID; //游戏ID
	private long logid = 0;
	
	public LogManager() {
		synchronized (LogManager.class) {
			instance = this;
			this.logid = 0;
			gameserverid = ConfigManager.getGameId()+" "+Gs.serverid; //游戏ID
		}
	}

	public static LogManager getInstance() {
		synchronized (LogManager.class) {
			return instance;
		}
	}
	
	@Override
	protected void addMkio(Mkio mkio) {
	}

	@Override
	public Mkio get() {
		return null;
	}

	@Override
	protected void removeMkio(Mkio mkio, Throwable e) {

	}

	@Override
	public int size() {
		return 0;

	}

	

	
	public String getGameserverid() {
		return gameserverid;
	}

	public void setGameserverid(String gameserverid) {
		this.gameserverid = gameserverid;
	}

	public long getLogid() {
		return logid;
	}

	public void setLogid(long logid) {
		this.logid = logid;
	}

	public class LogWhileProcedureEnd extends Task {

		private String message;
		
	
		public LogWhileProcedureEnd(String message, int priority) {
			super();
			this.message = message;

		}
		
		@Override
		public void run() {
			logger.error(message);

		}
		
	}
}