package mylogic;

import java.util.List;

import xbean.ClanProgressRankRecord;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class ClanFightHistroyListTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {

 		xbean.ClanFightHistroyRankList src = xbean.Pod.newClanFightHistroyRankListData(); 
		xbean.ClanFightHistroyRankList dst = xbean.Pod.newClanFightHistroyRankListData(); 
		src.unmarshal(src_value);
		dst.unmarshal(dest_value);
		
		java.util.List<xbean.ClanFightHistroyRankRecord> recordList = src.getRecords();
		for(xbean.ClanFightHistroyRankRecord current : recordList){
			tryInsertToDes(current, dst.getRecords());
		}
		
		dest_value.clear();
		dst.marshal(dest_value);
		return true;		
	}
	
	private void tryInsertToDes(xbean.ClanFightHistroyRankRecord current,	List<xbean.ClanFightHistroyRankRecord> records) {
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

	private boolean compareTo(xbean.ClanFightHistroyRankRecord currentRecord, xbean.ClanFightHistroyRankRecord otherRecord)
	{
		if (currentRecord.getMarshaldata().getTotalscroe() > otherRecord.getMarshaldata().getTotalscroe())
		{
			return true;
		}
		
		if (currentRecord.getMarshaldata().getTotalscroe() == otherRecord.getMarshaldata().getTotalscroe())
		{
			float currentwinradio = 0;
			float otherwinradio = 0;
			
			if (currentRecord.getMarshaldata().getFightcount() != 0)
				currentwinradio = (float)currentRecord.getMarshaldata().getWincount() / (float)currentRecord.getMarshaldata().getFightcount();
			
			if (otherRecord.getMarshaldata().getFightcount() != 0)
				otherwinradio = (float)otherRecord.getMarshaldata().getWincount() / (float)otherRecord.getMarshaldata().getFightcount();
			
			if (currentwinradio > otherwinradio)
			{
				return true;
			}
			
			if (currentwinradio == otherwinradio)
			{
				if (currentRecord.getMarshaldata().getWincount() > otherRecord.getMarshaldata().getWincount())
				{
					return true;
				}
				
				if (currentRecord.getMarshaldata().getWincount() == otherRecord.getMarshaldata().getWincount())
				{
					if (currentRecord.getMarshaldata().getClanid() < otherRecord.getMarshaldata().getClanid())
					{
						return true;
					}		
				}
			}
		}

		return false;
	}
	
}