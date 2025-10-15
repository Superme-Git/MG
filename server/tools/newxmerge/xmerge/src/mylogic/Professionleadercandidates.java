package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Professionleadercandidates implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ProfessionLeaderCand srcProfessionLeaderCand = xbean.Pod.newProfessionLeaderCandData();
		xbean.ProfessionLeaderCand dstProfessionLeaderCand = xbean.Pod.newProfessionLeaderCandData();
		srcProfessionLeaderCand.unmarshal(src_value);
		dstProfessionLeaderCand.unmarshal(dest_value);
		
		dest_value.clear();
		dstProfessionLeaderCand.marshal(dest_value);
		return true;
	}
	
}