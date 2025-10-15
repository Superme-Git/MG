
package fire.pb.activity.award;

import fire.log.enums.YYLoggerTuJingEnum;

/**
	为ROLL点生成的道具，还没给玩家，ROLL结算后给 by changhao

 */
public class RolledAwardItem {

	public int itemid;
	
	public int itemnum;
	
	public YYLoggerTuJingEnum countertype;
	
	public int xiangguanid;
	
	public int awardid;

	public RolledAwardItem(int awardid, int itemid, int itemnum, YYLoggerTuJingEnum countertype, int xiangguanid){
		this.itemid = itemid;
		this.itemnum = itemnum;
		this.countertype = countertype;
		this.xiangguanid = xiangguanid;
		this.awardid = awardid;
	}
}