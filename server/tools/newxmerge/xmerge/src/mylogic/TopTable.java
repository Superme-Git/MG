package mylogic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

/**
 * 科举榜合并
 * @author ldf
 *
 */
public class TopTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
//		xbean.Top src_index = xbean.Pod.newTopData();
//		xbean.Top des_index = xbean.Pod.newTopData();
//		
//		src_index.unmarshal(src_value);
//		des_index.unmarshal(dest_value);
//		
//		List<Integer> srckejuList = src_index.getKeju();
//		for(Integer key : srckejuList){
//			if(!des_index.getKeju().contains(key)){
//				des_index.getKeju().add(key);
//			}
//		}
//		Collections.sort(des_index.getKeju(), new MyComparator());
//		
//		List<Integer> srcwulindahuiList = src_index.getWulindahui();
//		for(Integer key : srcwulindahuiList){
//			if(!des_index.getWulindahui().contains(key)){
//				des_index.getWulindahui().add(key);
//			}
//		}
//		Collections.sort(des_index.getWulindahui(), new MyComparator());
//		
//		des_index.getCarve().clear();
//		dest_value.clear();
//		des_index.marshal(dest_value);
		
		return true;
	}
	
	
	
	public class MyComparator implements Comparator<Integer>{
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1>o2?-1:1;
		}
	}
}
