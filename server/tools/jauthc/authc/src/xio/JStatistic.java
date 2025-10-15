package xio;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Deprecated
public class JStatistic 
{
	private Date _beginTime = null;
	private Date _endTime = null;
	private boolean _running = false;
	
	private ConcurrentHashMap<Integer, AtomicLong> _pRecvTypeCnt = 
		new ConcurrentHashMap<Integer, AtomicLong>();
	private ConcurrentHashMap<Integer, AtomicLong> _pSendTypeCnt = 
		new ConcurrentHashMap<Integer, AtomicLong>();
	
	private long _recvBytes = 0;
	private long _sendBytes = 0;
	
	private Map<Integer, xio.Protocol> _proSet = new HashMap<Integer, xio.Protocol>();
	
	void addWatchProtocol(Collection<xio.Protocol> pros)
	{
		for ( xio.Protocol p : pros )
		{
			this.addWatchProtocol(p);
		}
	}
	
	void addWatchProtocol(xio.Protocol p)
	{
		_proSet.put(p.getType(), p);
	}
	
	void start()
	{
		
		if (_running)
		{
			mkdb.Trace.info("Statisis is RUNNING!");
			return;
		}
		
		_beginTime = new Date(System.currentTimeMillis());
		_endTime = new Date(System.currentTimeMillis());
		_running = true;

		{//Protocol Type Cnt
			if (_proSet.isEmpty())
			{
				mkdb.Trace.debug("Not Protocols ?");
			}
			_pRecvTypeCnt.clear();
			_pSendTypeCnt.clear();
			for( xio.Protocol p : _proSet.values())
			{
				_pRecvTypeCnt.put(new Integer(p.getType()), new AtomicLong(0));
				_pSendTypeCnt.put(new Integer(p.getType()), new AtomicLong(0));
			}
		}
	}
	
	void close()
	{
		if ( _running )
		{
			_running = false;
			_endTime = new Date(System.currentTimeMillis());
		}
	}
	
	public String toString()
	{
		if( _running )
		{
			return "The JStatisis is RUNNING";
		}
		
		long last = _endTime.getTime() - _beginTime.getTime();
		StringBuilder sb = new StringBuilder();
		
		sb.append(" \n ----------------------------------------- \n");
		sb.append(" JAuthc NetWork Statistic(" + _beginTime.toString() + 
				" To "+ _endTime.toString() + ") Last for " + last +"(mill) \n") ;
		
		long sendCnt = 0;
		long recvCnt = 0;
		sb.append("	Send Protocol Info: \n");
		for ( Integer type : _pSendTypeCnt.keySet())
		{
			if( _pSendTypeCnt.get(type).longValue() != 0)
			{
				sendCnt += _pSendTypeCnt.get(type).longValue();
				sb.append( "\t\t" + _proSet.get(type).getClass().getName() 
					+ "(" + type + ") | Count: " + _pSendTypeCnt.get(type) + "\n");
			}
		}
		sb.append("	Recv Protocol Info: \n");
		for ( Integer type : _pRecvTypeCnt.keySet())
		{
			if( _pRecvTypeCnt.get(type).longValue() != 0 )
			{
				recvCnt += _pRecvTypeCnt.get(type).longValue();
				sb.append( "\t\t" + _proSet.get(type).getClass().getName() 
					+ "(" + type + ") | Count: " + _pRecvTypeCnt.get(type) + "\n");
			}
		}
		
		sb.append(" Bytes of Protocol: \n");
		sb.append("\t\tAll Send Type Cnt: " + sendCnt + "\n" 
				+ "\t\tAll Send Bytes: " + _sendBytes + " Mbps: " + new Float(_sendBytes*8/(float)(last*1000)) + "\n");
		sb.append("\t\tAll Recv Type Cnt: " + recvCnt + "\n" 
				+ "\t\tAll Recv Bytes: " + _recvBytes + " Mbps: " + new Float(_recvBytes*8/(float)(last*1000)) + "\n");
		
		return sb.toString();
	}

	
	public void incRecvProType(int type)
	{
		if( _running && _pRecvTypeCnt.containsKey(type))
			_pRecvTypeCnt.get(type).incrementAndGet();
	}
	
	public void incSendProType(int type)
	{
		if ( _running && _pSendTypeCnt.containsKey(type))
			_pSendTypeCnt.get(type).incrementAndGet();
	}
	
	public void addSendBytes(long bytes)
	{
		if ( _running )
			_sendBytes += bytes;
	}
	
	public void addRecvBytes(long bytes)
	{
		if ( _running )
			_recvBytes += bytes;
	}
	
	
}
