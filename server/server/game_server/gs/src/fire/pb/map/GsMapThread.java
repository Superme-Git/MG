package fire.pb.map;

import java.util.concurrent.TimeUnit;

import fire.msp.move.MMoveCheckHideBattle;
import fire.msp.move.MUpdateUnitPos;
import mkdb.ThreadHelper;

import org.apache.log4j.Logger;

public class GsMapThread extends ThreadHelper
{
	public static final Logger logger=Logger.getLogger("MAPMAIN");
	public static String THREAD_NAME = "GsMapThread";
	private static GsMapThread instance;		
	public static synchronized GsMapThread getInstance(){
		if(null == instance){
			instance = new GsMapThread(THREAD_NAME);
		}		
		return instance;
	}	
	
	private GsMapThread(String name){
		super(name);
	}
	
	@Override
	public void run(){
		while(isRunning() || !protocols.isEmpty()){
			try{
				mkio.Protocol p = protocols.poll(5,TimeUnit.SECONDS);
				if(null != p){
					p.run();
				}
			}
			catch(final Exception e){
				logger.info("err", e);
			}
			catch(final VerifyError e){
				logger.info("err", e);
			}
		}
	}
	
	public boolean add(mkio.Protocol p){
		return protocols.add(p);
	}
	
	public void clear(){
		protocols.clear();
	}
	
	/**
	 * 区分协议的类型，如果不是由MapThread来处理的地图协议，则返回false，并不dispatch
	 * @param p
	 * @param stub
	 * @return
	 * @throws Exception
	 */
	public boolean tryAdd(mkio.Protocol p){
		if(p instanceof MUpdateUnitPos || p instanceof MMoveCheckHideBattle){
			add(p);
			return true;
		}
		else
			return false;
		
	}
	
	private final java.util.concurrent.BlockingQueue<mkio.Protocol> protocols = 
		new java.util.concurrent.LinkedBlockingQueue<mkio.Protocol>();

	
}
