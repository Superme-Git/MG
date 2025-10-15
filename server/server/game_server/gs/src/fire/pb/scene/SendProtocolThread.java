package fire.pb.scene;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import mkdb.ThreadHelper;

public final class SendProtocolThread extends ThreadHelper {
	public static final Logger logger=Logger.getLogger("SENDPROTOCOL");
	public static String MAP_THREAD_NAME = "SendMapProtocol";
	private static SendProtocolThread instance;		
	public static synchronized SendProtocolThread getInstance(){
		if(null == instance){
			instance = new SendProtocolThread(MAP_THREAD_NAME);
		}		
		return instance;
	}	
	
	private SendProtocolThread(String name){
		super(name);
	}
	
	@Override
	public void run(){
		while(isRunning() || !protocols.isEmpty()){
			try{
				
				MapProtocol p = protocols.poll(5,TimeUnit.SECONDS);
				if(p != null)
				{
					if(p.roleId != null)
						gnet.link.Onlines.getInstance().send(p.roleId, p.p);
					else if(p.roleIds != null && p.roleIds.size() > 0)
						gnet.link.Onlines.getInstance().send(p.roleIds, p.p);
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
	
	public void send(long roleId, mkio.Protocol p)
	{
		add(new MapProtocol(roleId, p));
	}
	
	public void send(Set<Long> roleIds, mkio.Protocol p)
	{
		add(new MapProtocol(roleIds, p));
	}	
	
	public synchronized boolean add(MapProtocol p){
		return protocols.add(p);
	}
	
	public synchronized void clear(){
		protocols.clear();
	}
	
	private final java.util.concurrent.BlockingQueue<MapProtocol> protocols = 
		new java.util.concurrent.LinkedBlockingQueue<MapProtocol>();

}
