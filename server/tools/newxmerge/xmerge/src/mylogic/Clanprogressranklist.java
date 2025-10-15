package mylogic;

import java.util.List;

import xbean.ClanProgressRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Clanprogressranklist implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ClanProgressRankList srcClanProgressRankList = xbean.Pod.newClanProgressRankListData(); 
		xbean.ClanProgressRankList dstClanProgressRankList = xbean.Pod.newClanProgressRankListData(); 
		srcClanProgressRankList.unmarshal(src_value);
		dstClanProgressRankList.unmarshal(dest_value);
		
		java.util.List<xbean.ClanProgressRankRecord> recordList = srcClanProgressRankList.getRecords();
		for(xbean.ClanProgressRankRecord current : recordList){
			tryInsertToDes(current, dstClanProgressRankList.getRecords());
		}
		
		int size = dstClanProgressRankList.getRecords().size();
		if(size > Constant.CLAN_PROGRESS_LIST_NUM){
			for(int i=Constant.CLAN_PROGRESS_LIST_NUM; i<size; i++){
				dstClanProgressRankList.getRecords().remove(Constant.CLAN_PROGRESS_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dstClanProgressRankList.marshal(dest_value);
		return true;
	}
	
	
	private void tryInsertToDes(ClanProgressRankRecord current,	List<ClanProgressRankRecord> records) {
		for(int i=records.size()-1; i>=0; i--){
			if(compareTo(current, records.get(i))){
				if(i==0){
					records.add(0, current);
				}
				continue;
			}else{
				records.add(i+1, current);
				break;
			}
		}
		
	}

	private boolean compareTo(ClanProgressRankRecord currentRecord, ClanProgressRankRecord otherRecord) {
		if (currentRecord.getCopyid() > otherRecord.getCopyid())
		{
			return true;
		}
		
		if (currentRecord.getCopyid() == otherRecord.getCopyid())
		{
			if (currentRecord.getProgress() > otherRecord.getProgress())
			{
				return true;
			}
			else
			{
				if (currentRecord.getProgress() == otherRecord.getProgress())
				{
					if (currentRecord.getProgress() == 10)
					{
						if (currentRecord.getBosshp() < otherRecord.getBosshp())
						{
							return true;
						}
						else
						{
							if (currentRecord.getBosshp() == otherRecord.getBosshp())
							{
								if (currentRecord.getTime() < otherRecord.getTime())
								{
									return true;
								}
								else
								{
									if (currentRecord.getTime() == otherRecord.getTime())
									{
										return currentRecord.getTriggertime() < otherRecord.getTriggertime();
									}
								}									
							}
						}
					}
					else
					{
						if (currentRecord.getTime() < otherRecord.getTime())
						{
							return true;
						}
						else
						{
							if (currentRecord.getTime() == otherRecord.getTime())
							{
								return currentRecord.getTriggertime() < otherRecord.getTriggertime();
							}
						}						
					}
				}				
			}
		}

		return false;
	}
	
	
	
}