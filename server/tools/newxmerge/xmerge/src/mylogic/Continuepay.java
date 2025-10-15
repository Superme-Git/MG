package mylogic;

import java.util.HashSet;
import java.util.Set;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;


public class Continuepay implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

//		xbean.ContinuePay srcContinuePay = xbean.Pod.newContinuePayData(); 
//		xbean.ContinuePay destContinuePay = xbean.Pod.newContinuePayData(); 
//		srcContinuePay.unmarshal(src_value);
//		destContinuePay.unmarshal(dest_value);
//		
//		//充值送礼
//		for (xbean.ChargeActivities	chargeActivity : destContinuePay.getChargeactivities()) {
//			xbean.ChargeActivities srcChargeActivity = getSameChargeActivity(srcContinuePay,chargeActivity);
//			if (srcChargeActivity == null)//如果没找到一样的活动,continue
//				continue;
//			chargeActivity.setChargermb(chargeActivity.getChargermb()+srcChargeActivity.getChargermb());
//			Set<Integer> tmpSet = new HashSet<Integer>();
//			tmpSet.addAll(chargeActivity.getChargestatus());
//			tmpSet.addAll(srcChargeActivity.getChargestatus());
//			chargeActivity.getChargestatus().clear();
//			chargeActivity.getChargestatus().addAll(tmpSet);
//		}
//		//限时出售
//		destContinuePay.getLimititemactivities().clear();
//		//消费送礼
//		for (xbean.ConsumeActivities consumeActivity : destContinuePay.getConsumeactivities()) {
//			xbean.ConsumeActivities srcConsumeActivity = getSameConsumeActivity(srcContinuePay,consumeActivity);
//			if (srcConsumeActivity == null) 
//				continue;
//			consumeActivity.setConsumenum(consumeActivity.getConsumenum()+srcConsumeActivity.getConsumenum());
//			Set<Integer> tmpSet = new HashSet<Integer>();
//			tmpSet.addAll(consumeActivity.getConsumestatus());
//			tmpSet.addAll(srcConsumeActivity.getConsumestatus());
//			consumeActivity.getConsumestatus().clear();
//			consumeActivity.getConsumestatus().addAll(tmpSet);
//		}
//		
//		dest_value.clear();
//		destContinuePay.marshal(dest_value);
		return true;
	}

//	private ConsumeActivities getSameConsumeActivity(ContinuePay srcContinuePay, ConsumeActivities consumeActivity) {
//
//		for (xbean.ConsumeActivities activity: srcContinuePay.getConsumeactivities()) {
//			if (activity.getStarttime()==consumeActivity.getStarttime()&& activity.getEndtime()==consumeActivity.getEndtime()) 
//				return activity;
//		}
//		return null;
//	}

	
//	private ChargeActivities getSameChargeActivity(ContinuePay srcContinuePay, ChargeActivities chargeActivity) {
//
//		for (xbean.ChargeActivities activity : srcContinuePay.getChargeactivities()) {
//			if (activity.getStarttime()==chargeActivity.getStarttime() && activity.getEndtime()==chargeActivity.getEndtime())
//				return activity;
//		}
//		return null;
//	}

}

