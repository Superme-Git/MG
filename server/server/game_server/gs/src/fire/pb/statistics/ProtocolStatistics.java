package fire.pb.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;

import mkio.Engine;

public class ProtocolStatistics
{
	public static mkdb.util.Counter countSend
	= new mkdb.util.Counter(Engine.mbeans(), "xdb", "Sends.send");
	public static mkdb.util.Counter countBroadcast
	= new mkdb.util.Counter(Engine.mbeans(), "xdb", "BroadCasts.send");
	public static mkdb.util.Counter countSendSize
	= new mkdb.util.Counter(Engine.mbeans(), "xdb", "Sends.sendSize");
	
	public static mkdb.util.Counter countRecv
	= new mkdb.util.Counter(Engine.mbeans(), "xdb", "Sends.recv");
	public static mkdb.util.Counter countRecvSize
	= new mkdb.util.Counter(Engine.mbeans(), "xdb", "Sends.recvSize");
	
	
	public static Map<String, StatisticAttr> sendprotocols = new HashMap<String, StatisticAttr>();
	public static Map<String, StatisticAttr> recvprotocols = new HashMap<String, StatisticAttr>();

	
	public static void process()
	{
		processSends();
		processRecvs();
	}
	
	private static void processSends()
	{
		MBeanInfo info = countSendSize.getMBeanInfo();
		for(MBeanAttributeInfo attrinfo: info.getAttributes())
		{
			String classname = attrinfo.getName();
			StatisticAttr statis = sendprotocols.get(classname);
			if(statis == null)
			{
				statis = new StatisticAttr(classname);
				sendprotocols.put(classname, statis);
			}
			
			int newnum = 0;
			AtomicLong atomicnum =  countSend.get(classname);
			if(atomicnum == null)
				atomicnum = countBroadcast.get(classname);
			if(atomicnum != null)
				newnum = (int)atomicnum.get(); 
			
			long newsize = 0;
			AtomicLong atomicsize =  countSendSize.get(classname);
			if(atomicsize != null)
				newsize = atomicsize.get(); 
			
			statis.lastperiod_num = newnum - statis.totalnum;
			statis.lastperiod_size = newsize - statis.totalsize;
			statis.lastperiod_avgsize = (int)statis.lastperiod_size / statis.lastperiod_num;
			
			statis.totalnum = newnum;
			statis.totalsize = newsize;
			statis.avgsize = (int)newsize / newnum;
		}
	}
	
	private static void processRecvs()
	{
		MBeanInfo info = countRecv.getMBeanInfo();
		for(MBeanAttributeInfo attrinfo: info.getAttributes())
		{
			String classname = attrinfo.getName();
			StatisticAttr statis = recvprotocols.get(classname);
			if(statis == null)
			{
				statis = new StatisticAttr(classname);
				recvprotocols.put(classname, statis);
			}
			
			int newnum = 0;
			AtomicLong atomicnum =  countRecv.get(classname);
			if(atomicnum != null)
				newnum = (int)atomicnum.get(); 
			
			long newsize = 0;
			AtomicLong atomicsize =  countRecvSize.get(classname);
			if(atomicsize != null)
				newsize = atomicsize.get(); 
			
			statis.lastperiod_num = newnum - statis.totalnum;
			statis.lastperiod_size = newsize - statis.totalsize;
			statis.lastperiod_avgsize = (int)statis.lastperiod_size / statis.lastperiod_num;
			
			statis.totalnum = newnum;
			statis.totalsize = newsize;
			statis.avgsize = (int)newsize / newnum;
		}
	}
	
	public static class StatisticAttr
	{
		public final String classname;
		public StatisticAttr(String classname)
		{
			this.classname = classname;
		}
		
		
		public int totalnum;//总个数
		public long totalsize;//总大小
		public int avgsize;//平均大小
		
		public int lastperiod_num;//上一个统计区间的个数
		public long lastperiod_size;//上一个统计区间的大小
		public int lastperiod_avgsize;//上一个统计区间的平均大小
	}
	
	
}
