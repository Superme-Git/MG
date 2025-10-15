package fire.pb.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import fire.script.JavaScript;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;

public class Parser
{
	
	public static final Logger logger = Logger.getLogger(Parser.class);
	
	
	public static ID2Odds parseIdAndOdds(String str)
	{
		try
		{
			if(str == null || str.equals(""))
				return null;
			String[] strs = str.split(";");
			ID2Odds id2odds = new ID2Odds(strs.length);
			for (int i = 0 ; i < strs.length;i++)
			{
				String[] rands = strs[i].split("@");
				id2odds.ids[i] = Integer.valueOf(rands[0]);
				id2odds.odds[i] = Integer.valueOf(rands[1]);
			}
			return id2odds;
		}
		catch(Exception e)
		{
			logger.error("Parse IdAndOdds error. String = " + str , e);
			e.printStackTrace();
		}
		return null;
		 
	}
	
	
	public static ID2Odds parseIdAndOddsWithComma(String str)
	{
		try
		{
			if(str == null || str.equals(""))
				return null;
			String[] strs = str.split(",");
			ID2Odds id2odds = new ID2Odds(strs.length);
			for (int i = 0 ; i < strs.length;i++)
			{
				String[] rands = strs[i].split("@");
				id2odds.ids[i] = Integer.valueOf(rands[0]);
				id2odds.odds[i] = Integer.valueOf(rands[1]);
			}
			return id2odds;
		}
		catch(Exception e)
		{
			logger.error("Parse IdAndOdds error. String = " + str , e);
			e.printStackTrace();
		}
		return null;
		 
	}
	
	
	public static class ID2Odds
	{
		public ID2Odds(int num )
		{
			ids = new int[num];
			odds = new int[num];
		}
		
		public final int[] ids;
		public final int[] odds;
		
		public Integer getRandomId()
		{
			int index = Misc.getProbability(odds);
			if(index == -1)
				return null;

			return ids[index];
		}
	}
	
	
	public static Map<Integer, Float> parseEffects(String effectstrs) throws Exception
	{
		Map<Integer, Float> effectMap = new HashMap<Integer, Float>();
		if (effectstrs != null && !effectstrs.equals(""))
		{
			String[] effectStr = effectstrs.split(";");
			for (int i = 0; i < effectStr.length; i++)
			{
				String[] tmpstrs = effectStr[i].split("=");
				if (tmpstrs.length < 2)
					throw new Exception("Wrong effects str.");

				int effectId = fire.pb.effect.Module.getInstance().getIdByName(tmpstrs[0].trim());// 第一个“=”之前的字符串是Effectname
				Float effectValue = Float.valueOf((effectStr[i].substring(tmpstrs[0].length() + 1)).trim());// 取第一个“=”之后的字符串，是效果值
				effectMap.put(effectId, effectValue);
			}
		}
		return effectMap;
	}
	
	
	public static Map<Integer,JavaScript> parseFightJsEffects(String effectstrs) throws Exception
	{
		Map<Integer,JavaScript> effectMap = new HashMap<Integer, JavaScript>();
		if(effectstrs !=null && !effectstrs.equals(""))
		{
			try
			{
				String[] effectStr = effectstrs.split(";");
				for (int i = 0; i < effectStr.length; i++)
				{
					String[] tmpstrs = effectStr[i].split("=");
					if (tmpstrs.length < 2)
						throw new Exception("Wrong effects str：	  "+ effectstrs);
	
					int effectId = fire.pb.effect.Module.getInstance().getIdByName(tmpstrs[0].trim());// 第一个“=”之前的字符串是Effectname
					String effectValueJS = effectStr[i].substring(tmpstrs[0].length() + 1);// 取第一个“=”之后的字符串，是JS公式
					JavaScript compiledJS = new JavaScript(effectValueJS);
					effectMap.put(effectId, compiledJS);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error("ERROR: ParseFightJsEffects fail. effectstrs = " + effectstrs);
			}
	
		}
		return effectMap;
	}
	
	
	public static Octets getStringMarshalOctets(String content){
		Octets octets = null;
		try {
			octets = new Octets(content.getBytes("UTF-16LE"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("服务器转换字符串Octets对象出错！");
			
		}
		return octets;
	}
	
	
    public static String convertStackTrace2String(Exception e) {
    	String tracestr = e.toString();
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++)
			tracestr += ("\tat " + trace[i] + "\n");
		return tracestr;
    }
	
    public static String convertStackTrace2String(StackTraceElement[] trace) {
    	String tracestr = "";
		for (int i = 0; i < trace.length; i++)
			tracestr += ("\tat " + trace[i] + "\n");
		return tracestr;
    }

	
	
	public static String convertfireStackTrace2String(StackTraceElement[] trace) {

    	StringBuilder tracestr = new StringBuilder();
		for (int i = 0; i < trace.length; i++){
			if (trace[i].toString().indexOf("fire")>-1)
				tracestr.append(trace[i]);
		}
		return tracestr.toString();
	}
}
