package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.Misc;

public class BattleNpcInfo {
	public List<Integer> npcIdLst;
	public List<Integer> npcRateLst;
	
	public BattleNpcInfo(final String npcInfo) {
		npcIdLst = new ArrayList<Integer>();
		npcRateLst = new ArrayList<Integer>();
		
		if (npcInfo != null) {
			String[] strNpcInfo = npcInfo.split(";");
			for (String strInfo : strNpcInfo) {
				String[] strTmp = strInfo.split("@");
				npcIdLst.add(Integer.parseInt(strTmp[0]));
				npcRateLst.add(Integer.parseInt(strTmp[1]));
			}
		}
	}
	
	public int getNpcId() {
		int nIndex = Misc.getProbability(npcRateLst);
		if (nIndex < npcIdLst.size()) {
			return npcIdLst.get(nIndex);
		}
		return -1;
	}

}
