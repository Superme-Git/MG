package mylogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class CoupleChartTable implements IMerge {
    @Override
    public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

//        xbean.CoupleChart srcChart = xbean.Pod.newCoupleChartData();
//        xbean.CoupleChart dstChart = xbean.Pod.newCoupleChartData();
//        srcChart.unmarshal(src_value);
//        dstChart.unmarshal(dest_value);
//        
//        
//        List<xbean.Family> srcLuxuryList = srcChart.getLuxurylist();
//        List<xbean.Family> dstLuxuryList = dstChart.getLuxurylist();
//        
//        int srcRank = srcChart.getWeddingranks();
//        int dstRank = dstChart.getWeddingranks();
//        
//        List<xbean.Family> allFamilies = new ArrayList<xbean.Family>();
//        allFamilies.addAll(srcLuxuryList);
//        allFamilies.addAll(dstLuxuryList);
//        
//        Map<Long,Integer> allMap = new HashMap<Long,Integer>();
//        
//        Collections.sort(allFamilies, new Comparator<xbean.Family>(){
//            public int compare(Family o1, Family o2) {
//                return o1.getWeddingtime() < o2.getWeddingtime() ? -1 : 1;
//            }
//        });
//        xbean.Family ff  = null;
//        for (int i = 0, j = allFamilies.size(); i < j ; i ++){
//            ff = allFamilies.get(i);
//            allMap.put(ff.getRoleid(), i + 1);
//            allMap.put(ff.getToroleid(), i + 1);
//        }
//        
//        dstChart.setWeddingranks(srcRank + dstRank);
//        dstChart.getLuxurylist().clear();
//        dstChart.getLuxurylist().addAll(allFamilies);
//        dstChart.getLuxuryrank().clear();
//        dstChart.getLuxuryrank().putAll(allMap);
//        
//        dest_value.clear();
//        dstChart.marshal(dest_value);
        return true;
    }
}
