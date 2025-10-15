package fire.pb;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import mkdb.ThreadHelper;

public final class EnterWorldThread extends ThreadHelper {
	public static final Logger logger=Logger.getLogger(EnterWorldThread.class);
	
	public static String THREAD_NAME = "EnterWorldThread";
	private static EnterWorldThread instance;		
	public static synchronized EnterWorldThread getInstance(){
		if(null == instance){
			instance = new EnterWorldThread(THREAD_NAME);
		}		
		return instance;
	}	
	
	private EnterWorldThread(String name){
		super(name);
	}
	
	@Override
	public void run(){
		while(isRunning() || !protocols.isEmpty()){//添加对protocols size的判断，处理完所有已有协议后EnterWorldThread再停止
			try{
				mkio.Protocol p = protocols.poll(5,TimeUnit.SECONDS);
				if(null != p){
					p.run();
				}
				//sleep(1000);//休息1秒
			}
			catch(final Exception e){
				logger.info("err", e);
			}
		}
	}
	
	/**
	 * 区分协议的类型，如果不是由CEnterWorld则返回false，并不dispatch
	 * @param p
	 * @param stub
	 * @return
	 * @throws Exception
	 */
	public boolean tryAdd(mkio.Protocol p) throws Exception{
		if(p instanceof CEnterWorld){
			add(p);
			return true;
		}
		else
			return false;
		
	}
	
	public boolean add(mkio.Protocol p){
		return protocols.add(p);
	}
	
	public void clear(){
		protocols.clear();
	}
	
	private final java.util.concurrent.BlockingQueue<mkio.Protocol> protocols = 
		new java.util.concurrent.LinkedBlockingQueue<mkio.Protocol>();

}
