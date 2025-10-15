package fire.pb.ranklist.proc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mkdb.Procedure;
import xbean.RoleLevelListRecord;

public class RoleLevelSort extends Procedure {
	
	@Override
	public boolean process(){
		xbean.RoleLevelRankList rankList = xtable.Rolelevellist.select(1);
		if(null == rankList)
			return true;
		
		List<xbean.RoleLevelListRecord> sortList = new ArrayList<RoleLevelListRecord>();
		for(xbean.RoleLevelListRecord record : rankList.getRecords()){
			xbean.RoleLevelListRecord recordcopy = xbean.Pod.newRoleLevelListRecord();
			recordcopy.getMarshaldata().setLevel(record.getMarshaldata().getLevel());
			recordcopy.getMarshaldata().setRank(record.getMarshaldata().getRank());
			recordcopy.getMarshaldata().setRoleid(record.getMarshaldata().getRoleid());
			recordcopy.getMarshaldata().setRolename(record.getMarshaldata().getRolename());
			recordcopy.getMarshaldata().setSchool(record.getMarshaldata().getSchool());
			sortList.add(recordcopy);
		}
		
		if(0 == sortList.size())
			return true;
		
		sortLevel(sortList);
		
		if(sortList.size() == rankList.getRecords().size()){
			xtable.Rolelevellist.remove(1);
			xbean.RoleLevelRankList newrankList = xbean.Pod.newRoleLevelRankList();
			newrankList.getRecords().addAll(sortList);
			xtable.Rolelevellist.insert(1, newrankList);
		}
		
		return true;
	}

	private void sortLevel(List<RoleLevelListRecord> sortList) {
		Collections.sort(sortList, new java.util.Comparator<xbean.RoleLevelListRecord>(){

			@Override
			public int compare(RoleLevelListRecord o1, RoleLevelListRecord o2) {
				if(o2.getMarshaldata().getLevel() < o1.getMarshaldata().getLevel()){
					return -1;
				}
				
				if(o2.getMarshaldata().getLevel() > o1.getMarshaldata().getLevel()){
					return 1;
				}
				
				if(o2.getMarshaldata().getLevel() == o1.getMarshaldata().getLevel()){
					long role2 = o2.getMarshaldata().getRoleid();
					long role1 = o1.getMarshaldata().getRoleid();
					
					xbean.Properties roleP2 = xtable.Properties.select(role2);
					xbean.Properties roleP1 = xtable.Properties.select(role1);
					
					if(roleP2 == null || roleP1 == null)
						return -1;
					
					if(roleP2.getAllexp() <= roleP1.getAllexp()){
						return -1;
					}else {
						return 1;
					}
					
				}
				
				return 0;
			}
			
		});
		
		
	}

}
