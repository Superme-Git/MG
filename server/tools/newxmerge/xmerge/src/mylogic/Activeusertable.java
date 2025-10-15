package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

public class Activeusertable implements xmerge.IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.ActiveUserInfo xbeanSrc = xbean.Pod.newActiveUserInfoData();
		xbean.ActiveUserInfo xbeanDest = xbean.Pod.newActiveUserInfoData();
		xbeanSrc.unmarshal(src_value);
		xbeanDest.unmarshal(dest_value);
		
		if (xbeanDest.getConvertchargetotalamouttolong()==xbeanSrc.getConvertchargetotalamouttolong()){
			xbeanDest.setChargetotalamount(xbeanDest.getChargetotalamount()+xbeanSrc.getChargetotalamount());
			xbeanDest.setChargetotalrmb(xbeanDest.getChargetotalrmb()+xbeanSrc.getChargetotalrmb());
			xbeanDest.setChargetotalrmbuntillastday(xbeanDest.getChargetotalrmbuntillastday()+xbeanSrc.getChargetotalrmbuntillastday());
			xbeanDest.setChargetotalamountlong(xbeanDest.getChargetotalamountlong()+xbeanSrc.getChargetotalamountlong());
			xbeanDest.setChargetotalrmblong(xbeanDest.getChargetotalrmblong()+xbeanSrc.getChargetotalrmblong());
			xbeanDest.setChargetotalrmbuntillastdaylong(xbeanDest.getChargetotalrmbuntillastdaylong()+xbeanSrc.getChargetotalrmbuntillastdaylong());		
		}else if (xbeanDest.getConvertchargetotalamouttolong()==1) {
			xbeanDest.setChargetotalamountlong(xbeanDest.getChargetotalamountlong()+xbeanSrc.getChargetotalamount());
			xbeanDest.setChargetotalrmblong(xbeanDest.getChargetotalrmblong()+xbeanSrc.getChargetotalrmb());
			xbeanDest.setChargetotalrmbuntillastdaylong(xbeanDest.getChargetotalrmbuntillastdaylong()+xbeanSrc.getChargetotalrmbuntillastday());
		}else {
			xbeanDest.setChargetotalamountlong(xbeanDest.getChargetotalamount()+xbeanSrc.getChargetotalamountlong());
			xbeanDest.setChargetotalrmblong(xbeanDest.getChargetotalrmb()+xbeanSrc.getChargetotalrmblong());
			xbeanDest.setChargetotalrmbuntillastdaylong(xbeanDest.getChargetotalrmbuntillastday()+xbeanSrc.getChargetotalrmbuntillastdaylong());
		}
		
		xbeanDest.setFirsttimeactiveday(min(xbeanDest.getFirsttimeactiveday(),xbeanSrc.getFirsttimeactiveday()));// 第一次活跃的 当前天
		xbeanDest.setFirsttimeactiveweek(min(xbeanDest.getFirsttimeactiveweek(), xbeanSrc.getFirsttimeactiveweek()));// 第一次活跃的 当前周
		xbeanDest.setFirsttimechargeday(min(xbeanDest.getFirsttimechargeday(), xbeanSrc.getFirsttimechargeday()));
		xbeanDest.setFirsttimechargeweek(min(xbeanDest.getFirsttimechargeweek(), xbeanSrc.getFirsttimechargeweek()));
		xbeanDest.setFirsttimeenter(min(xbeanDest.getFirsttimeenter(), xbeanSrc.getFirsttimeenter()));// 第一次进入服务器 当前天
		
		int lasttimeactiveday1 = max(xbeanDest.getLasttimeactiveday1(), xbeanSrc.getLasttimeactiveday1());// 最近两次活跃的 当前天 day1比day2新,下同
		xbeanDest.setLasttimeactiveday1(lasttimeactiveday1);
		int lasttimeactiveday2 = max(xbeanDest.getLasttimeactiveday2(), xbeanSrc.getLasttimeactiveday2());
		int tmpMin = min(xbeanDest.getLasttimeactiveday1(), xbeanSrc.getLasttimeactiveday1());
		if (lasttimeactiveday2<tmpMin) 
			xbeanDest.setLasttimeactiveday2(tmpMin);
		else
		    xbeanDest.setLasttimeactiveday2(lasttimeactiveday2);
		
		int lasttimeactiveweek1 = max(xbeanDest.getLasttimeactiveweek1(), xbeanSrc.getLasttimeactiveweek1());// 最近两次活跃的 当前周 week1比week2新,下同
		xbeanDest.setLasttimeactiveweek1(lasttimeactiveweek1);
		int lasttimeactiveweek2 = max(xbeanDest.getLasttimeactiveweek2(), xbeanSrc.getLasttimeactiveweek2());
		int tmpweekMin = min(xbeanDest.getLasttimeactiveweek1(), xbeanSrc.getLasttimeactiveweek1());
		if (lasttimeactiveweek2<tmpweekMin) 
			xbeanDest.setLasttimeactiveweek2(tmpweekMin);
		else
		    xbeanDest.setLasttimeactiveweek2(lasttimeactiveweek2);
		
		if (xbeanDest.getLasttimechargeday()<xbeanSrc.getLasttimechargeday()) //如果dest的chargeday大,那就不用set了,另两种情况要设置
			xbeanDest.setLasttimechargedaytotalamount(xbeanSrc.getLasttimechargedaytotalamount());
		if (xbeanDest.getLasttimechargeday()==xbeanSrc.getLasttimechargeday())
			xbeanDest.setLasttimechargedaytotalamount(xbeanDest.getLasttimechargedaytotalamount()+xbeanSrc.getLasttimechargedaytotalamount());
		
		xbeanDest.setLasttimechargeday(max(xbeanDest.getLasttimechargeday(), xbeanSrc.getLasttimechargeday()));
		int lasttimechargeday1 = max(xbeanDest.getLasttimechargeday1(), xbeanSrc.getLasttimechargeday1());// 最近2次充值的当前天
		xbeanDest.setLasttimechargeday1(lasttimechargeday1);
		int lasttimechargeday2 = max(xbeanDest.getLasttimechargeday2(), xbeanSrc.getLasttimechargeday2());// 最近2次充值的当前天
		int tmpChargeminday = min(xbeanDest.getLasttimechargeday1(), xbeanSrc.getLasttimechargeday1());
		if (lasttimechargeday2<tmpChargeminday)
			xbeanDest.setLasttimechargeday2(tmpChargeminday);
		else
			xbeanDest.setLasttimechargeday2(lasttimechargeday2);
		
		if (xbeanDest.getLasttimechargeweek()<xbeanSrc.getLasttimechargeweek()) //如果dest的chargeweek大,那就不用set了,另两种情况要设置
			xbeanDest.setLasttimechargeweektotalamount(xbeanSrc.getLasttimechargeweektotalamount());
		if (xbeanDest.getLasttimechargeweek()==xbeanSrc.getLasttimechargeweek())
			xbeanDest.setLasttimechargeweektotalamount(xbeanDest.getLasttimechargeweektotalamount()+xbeanSrc.getLasttimechargeweektotalamount());
		
		xbeanDest.setLasttimechargeweek(max(xbeanDest.getLasttimechargeweek(), xbeanSrc.getLasttimechargeweek()));
		int lasttimechargeweek1 = max(xbeanDest.getLasttimechargeweek1(), xbeanSrc.getLasttimechargeweek1());// 最近2次充值的当前周
		xbeanDest.setLasttimechargeweek1(lasttimechargeweek1);
		int lasttimechargeweek2 = max(xbeanDest.getLasttimechargeweek2(), xbeanSrc.getLasttimechargeweek2());// 最近2次充值的当前周
		int tmpChargeminweek = min(xbeanDest.getLasttimechargeweek1(), xbeanSrc.getLasttimechargeweek1());
		if (lasttimechargeweek2<tmpChargeminweek)
			xbeanDest.setLasttimechargeweek2(tmpChargeminweek);
		else
			xbeanDest.setLasttimechargeweek2(lasttimechargeday2);
		
	   
		xbeanDest.setLasttimefushiconsumeday(max(xbeanDest.getLasttimefushiconsumeday(), xbeanSrc.getLasttimefushiconsumeday())); 
		xbeanDest.setLasttimefushiconsumedaytotalamount(xbeanDest.getLasttimefushiconsumedaytotalamount()+xbeanSrc.getLasttimefushiconsumedaytotalamount());
		xbeanDest.setLasttimefushiconsumeweek(max(xbeanDest.getLasttimefushiconsumeweek(), xbeanSrc.getLasttimefushiconsumeweek()));
		xbeanDest.setLasttimefushiconsumeweektotalamount(xbeanDest.getLasttimefushiconsumeweektotalamount()+xbeanSrc.getLasttimefushiconsumeweektotalamount());
		
		xbeanDest.setFushiconsumetotalamount(xbeanDest.getFushiconsumetotalamount()+xbeanSrc.getFushiconsumetotalamount());
		xbeanDest.setCashfushiconsumetotalamount(xbeanDest.getCashfushiconsumetotalamount()+xbeanSrc.getCashfushiconsumetotalamount());
		xbeanDest.setFushibuytotalamount(xbeanDest.getFushibuytotalamount()+xbeanSrc.getFushibuytotalamount());
		xbeanDest.setFushiselltotalamount(xbeanDest.getFushiselltotalamount()+xbeanSrc.getFushiselltotalamount());
		xbeanDest.setCashfushi(xbeanDest.getCashfushi()+xbeanSrc.getCashfushi());
		xbeanDest.setBindfushi(xbeanDest.getBindfushi()+xbeanSrc.getBindfushi());
		xbeanDest.setSysfushi(xbeanDest.getSysfushi()+xbeanSrc.getSysfushi());
		xbeanDest.setFushiinplatform(xbeanDest.getFushiinplatform()+xbeanSrc.getFushiinplatform());
		
	    xbeanDest.setLasttimeenter(max(xbeanDest.getLasttimeenter(), xbeanSrc.getLasttimeenter()));//最近一次进入服务器 当前天
	    
	    dest_value.clear();
	    xbeanDest.marshal(dest_value);
		return true;
	}

	/**
	 * min:(这里用一句话描述这个方法的作用)
	 *
	 * @param firsttimeactiveday
	 * @param firsttimeactiveday2
	 * @return    
	 * int    
	 * @throws 
	 * @since  　
	*/
	
	private int min(int num1, int num2) {
		return num1<num2?num1:num2;
	}
    private int max(int num1,int num2){
    	return num1<num2?num2:num1;
    }
}

