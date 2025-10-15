package fire.pb.ranklist.provider;

import java.util.List;

import fire.pb.ranklist.SRequestRankList;

/***
 * 排行榜数据接口
 * @author changhao
 *
 */
public interface IRankListData {
	
	public List<com.locojoy.base.Octets> getMO(SRequestRankList response, int page); 

}
