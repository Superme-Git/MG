package mylogic;

import java.util.Calendar;
import java.util.Date;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class ClanFightsTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ClanFights srcClanfights = xbean.Pod.newClanFightsData(); 
		xbean.ClanFights dstClanfights = xbean.Pod.newClanFightsData(); 
				
		srcClanfights.unmarshal(src_value);
		dstClanfights.unmarshal(dest_value);
		
		long cur = System.currentTimeMillis();
		
		dstClanfights.setCreatetime(srcClanfights.getCreatetime());
		
		for (Long e : srcClanfights.getClan1vschan2().keySet())
		{
			if (e != null)
			{
				xbean.ClanFight clanfight = srcClanfights.getClan1vschan2().get(e);
				if (clanfight != null)
				{
					long dest = cur + e;
					dstClanfights.getClan1vschan2().put(dest, clanfight);		
				}			
			}				
		}
		
		long m00 = GetMonday0000ByTime(cur);
		long t2030 = GetTuesday2030ByTime(cur);
		long th2030 = GetThursday2030ByTime(cur);
		long m15 = GetMonday1500ByTime(cur);
		long w15 = GetWednesday1500ByTime(cur);
		
		java.util.Date d = new java.util.Date();
		d.setTime(srcClanfights.getCreatetime());
		String s = d.toLocaleString();
		
		if (cur > m00 && cur < t2030 && (m15 == dstClanfights.getCreatetime() || w15 == dstClanfights.getCreatetime()))
		{
			dstClanfights.setCreatetime(0);
		}
		
		if (cur > t2030 && cur < th2030 && w15 == dstClanfights.getCreatetime())
		{
			dstClanfights.setCreatetime(0);
		}
		
		dest_value.clear();
		dest_value.marshal(dstClanfights);
		
		return true;
	}
	
	public long GetMonday0000ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 5;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	public long GetTuesday2030ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -5);
			cal.set(Calendar.HOUR_OF_DAY, 20);
			cal.set(Calendar.MINUTE, 30);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 4;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 20);
			cal.set(Calendar.MINUTE, 30);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	/***
	 * 得到周四20:30 by changhao
	 * @return
	 */
	public static long GetThursday2030ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -3);
			cal.set(Calendar.HOUR_OF_DAY, 20);
			cal.set(Calendar.MINUTE, 30);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 2;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 20);
			cal.set(Calendar.MINUTE, 30);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	public long GetMonday1500ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 5;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
	public long GetWednesday1500ByTime(long time)
	{
		Calendar cal = Calendar.getInstance();
		Date date = new Date(time);
		cal.setTime(date);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -4);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 3;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 15);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		
		return cal.getTimeInMillis();
	}
	
}