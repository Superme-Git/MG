package mylogic;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class EquipPaperPrice implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.EquipPaperPrice src_price = xbean.Pod.newEquipPaperPriceData();
//		xbean.EquipPaperPrice des_price = xbean.Pod.newEquipPaperPriceData();
//		
//		src_price.unmarshal(src_value);
//		des_price.unmarshal(dest_value);
//		
//		Map<Integer, Integer> src_list = src_price.getPaperprice();
//		Map<Integer, Integer> des_list = des_price.getPaperprice();
//		Iterator<Entry<Integer, Integer>> iterator = src_list.entrySet().iterator();
//		while(iterator.hasNext()){
//			Entry<Integer, Integer> current = iterator.next();
//			if(des_list.containsKey(current.getKey())){
//				int srcValue = current.getValue();
//				int desValue = des_list.get(current.getKey());
//				int realyValue = (srcValue+desValue)/2;
//				des_list.put(current.getKey(), realyValue);
//			}else{
//				des_list.put(current.getKey(), current.getValue());
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(des_price);
		
		return true;
	}

}
