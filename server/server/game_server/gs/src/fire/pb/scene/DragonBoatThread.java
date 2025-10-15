package fire.pb.scene;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import mkdb.ThreadHelper;
import mkio.Protocol.Stub;

public class DragonBoatThread extends ThreadHelper {

	private final BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();
	
	public static final Logger logger=Logger.getLogger("MAPMAIN");
	
	private static DragonBoatThread _instance;	
	
	public static synchronized DragonBoatThread getInstance(){
		if(null == _instance){
			_instance = new DragonBoatThread("DragonBoatThread");
		}		
		return _instance;
	}	
	
	private DragonBoatThread(String name){
		super(name);
	}
	
	
	@Override
	public void run(){
		while(isRunning()){
			try{
				mkio.Protocol p = protocols.take();
				
				if(null != p){
					p.run();
				}
			}
			catch(final Exception e){
				logger.info("err", e);
			}
		}
		
	}
	
	/**
	 * 区分协议的类型，如果不是由DragonBoatThread来处理的地图协议，则dispatch
	 * @param p
	 * @param stub
	 * @return
	 * @throws Exception
	 */
	public boolean tryAdd(mkio.Protocol p, Stub stub) throws Exception {
		if (!Thread.currentThread().isAlive())
			return false;
		if (p.getType() >= (0xc0000 + 28000) && p.getType() < (0xc0000 + 29000)) {
			
			if(p.getType() != (0x0000 + 28019)){
				//System.out.println("收到消息： " + p.getType());
			}
			
			return add(p);
		} else {
			return false;
		}
	}
	
	public boolean add(mkio.Protocol p){
		return protocols.add(p);
	}
	
	public void clear(){
		protocols.clear();
	}

}
