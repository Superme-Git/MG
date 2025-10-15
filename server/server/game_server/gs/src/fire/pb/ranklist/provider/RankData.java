package fire.pb.ranklist.provider;

import java.util.List;


public abstract class RankData {

	long  requestRoleid;
	
	int rankType;
	
	RankData(int rankype, long  requestRoleid){
		this.rankType = rankype;
		this.requestRoleid = requestRoleid;
	}

	public List<com.locojoy.base.Octets> getMarshalOctets() {
		return null;
	}
	
	public int getRankType() {
		return rankType;
	}

}
