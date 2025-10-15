package mylogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.locojoy.base.Marshal.OctetsStream;

public class Antiquetotaltable implements xmerge.IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

//		xbean.AntiqueTotalInfoList srcInfoList = xbean.Pod.newAntiqueTotalInfoListData();
//		xbean.AntiqueTotalInfoList dstInfoList = xbean.Pod.newAntiqueTotalInfoListData();
//		
//		srcInfoList.unmarshal(src_value);
//		dstInfoList.unmarshal(dest_value);
//		
//		Map<Long, Integer> srcMap = new HashMap<Long, Integer>();
//		for (int i = 0; i < srcInfoList.getInfos().size(); i++) {
//			xbean.AntiqueTotalInfo info = srcInfoList.getInfos().get(i);
//			srcMap.put(info.getMarshaldata().getRoleid(), i);
//		}
//		Map<Long, Integer> dstMap = new HashMap<Long, Integer>();
//		for (int i = 0; i < dstInfoList.getInfos().size(); i++) {
//			xbean.AntiqueTotalInfo info = dstInfoList.getInfos().get(i);
//			dstMap.put(info.getMarshaldata().getRoleid(), i);
//		}
//		//如果有玩家在两个数据库里面都上榜了,则合并他们的数据,否则,把src数据库里的数据并入dst
//		Set<Long> dstKeyset = dstMap.keySet();
//		for (Entry<Long, Integer> entry : srcMap.entrySet()) {
//			long key = entry.getKey();
//			int index = entry.getValue();
//			if (dstKeyset.contains(Long.valueOf(key))){
//				xbean.AntiqueTotalInfo srcinfo=srcInfoList.getInfos().get(index);
//				xbean.AntiqueTotalInfo dstinfo = dstInfoList.getInfos().get(dstMap.get(key));
//				dstinfo.getMarshaldata().setTotalprice(srcinfo.getMarshaldata().getTotalprice()+dstinfo.getMarshaldata().getTotalprice());
//				dstinfo.getMarshaldata().setAntiquenum(srcinfo.getMarshaldata().getAntiquenum()+dstinfo.getMarshaldata().getAntiquenum());
//			}else {
//				xbean.AntiqueTotalInfo srcinfo=srcInfoList.getInfos().get(index);
//				dstInfoList.getInfos().add(srcinfo);
//			}
//		}
//		//两个数据库加起来可能超过40个的上限了,排序之后要把多余的去掉
//		List<xbean.AntiqueTotalInfo> tmpList = new ArrayList<xbean.AntiqueTotalInfo>();
//		tmpList.addAll(dstInfoList.getInfos());
//		Collections.sort(tmpList, new AntiqueTotalBoardComparator());
//		dstInfoList.getInfos().clear();
//		for (int i = 0; i < AntiqueConst.ANTIQUE_TOTAL_BOARD_SIZE&&i<tmpList.size(); i++) {
//			dstInfoList.getInfos().add(tmpList.get(i));
//		}
//		
//		dest_value.clear();
//		dstInfoList.marshal(dest_value);
		return true;
	}

}

