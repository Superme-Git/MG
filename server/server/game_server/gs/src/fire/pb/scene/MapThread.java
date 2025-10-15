package fire.pb.scene;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;

import mkdb.ThreadHelper;
import mkio.Protocol.Stub;

public final class MapThread extends ThreadHelper implements Serializable {
	
	public static final long serialVersionUID = -3317906983263296005L;

	public static final Logger logger=Logger.getLogger("MAPMAIN");
	
//	public long  taketime = 0;
//	public long  runtime = 0;
//	public long procProtNum = 0;
//	
//	public static long checkmovenum = 0;
//	public static long checkmovep1 = 0;
//	public static long checkmovep2 = 0;
//	public static long checkmovep3 = 0;
//	public static long checkmovep4 = 0;
//	public static long checkmovep5 = 0;
//	public static long checkmovep6 = 0;
//	public static long checkmovep7 = 0;
//	public static long checkmovep8 = 0;
	public static String MAP_THREAD_NAME = "MapMain";
	private static MapThread instance;		
	public static synchronized MapThread getInstance(){
		if(null == instance){
			instance = new MapThread(MAP_THREAD_NAME);
		}		
		return instance;
	}	
	
	private MapThread(String name){
		super(name);
	}
	
	public Map<String, Long> prottimes = new ConcurrentHashMap<String, Long>();
	public Map<String, Long> protcounts = new ConcurrentHashMap<String, Long>();
	
	@Override
	public void run(){
		while(isRunning() || !protocols.isEmpty()){//添加对protocols size的判断，处理完所有已有协议后MapThread再停止
			try{
//				boolean isEmpty = true;
//				if(protocols.size() > 0)
//					isEmpty = false;
//				long takestart = System.currentTimeMillis();
//				mkio.Protocol p = protocols.take();不再用take，因为这时阻塞的MapThread是不能被notify的，会导致结束时无法正常关闭MapThread
				
				mkio.Protocol p = protocols.poll(5,TimeUnit.SECONDS);
//				long runstart = System.currentTimeMillis();
				if(null != p){
					//for test
//					String classname = p.getClass().getName();
//					Long time = prottimes.get(classname);
//					Long count = protcounts.get(classname);
//					if(time == null)
//						time = 0l;
//					if(count == null)
//						count = 0l;
//					long start = System.currentTimeMillis();
					
					p.run();
					
					//for test
//					long end = System.currentTimeMillis();
//					time += (end - start);
//					prottimes.put(classname, time);
//					protcounts.put(classname, count += 1);
					/*
					if (logger.isDebugEnabled() && time > 1000) {
						logger.debug("执行类->" + classname + ", 耗时：" + time + "毫秒, 执行：" + count + "次");
					}
					*/
					
				}
//				long runend = System.currentTimeMillis();
//				procProtNum ++;
//				if(!isEmpty)//只有栈不空的时候才统计（如果栈空taketime会很大）
//				{
//					taketime += (runstart - takestart);
//				}
//				runtime += (runend - runstart);
					
			}
			catch(final Exception e){
				logger.info("err", e);
			}
			catch(final VerifyError e){
				logger.info("err", e);
			}
		}
	}
	
	/**
	 * 区分协议的类型，如果不是由MapThread来处理的地图协议，则返回false，并不dispatch
	 * @param p
	 * @param stub
	 * @return
	 * @throws Exception
	 */
	public boolean tryAdd(mkio.Protocol p,Stub stub) throws Exception{
		if(p.getType()>(0xc0000 + 4000) && p.getType()<=(0xc0000+5000)){
//			if(p instanceof CCheckMove)
//				((CCheckMove) p).addTime = System.currentTimeMillis();
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
