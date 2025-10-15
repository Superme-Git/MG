package fire.pb.activity.winner;

import mkdb.Procedure;
import fire.pb.item.BagTypes;
import fire.pb.item.Pack;

public class PReceiveWinnerAward extends Procedure {

	public static final int ONE_AWARD = 1;

	public static final int TOW_AWARD = 2;

	public static final int THREE_AWARD = 3;

	private final long roleid;

	private final int awardType;

	public PReceiveWinnerAward(long roleid, int awardType) {
		super();
		this.roleid = roleid;
		this.awardType = awardType;
	}

	@Override
	protected boolean process() throws Exception {
		int awardid=0;
		if(awardType==ONE_AWARD){
			awardid=WinnerManager.getAwardListOne();
		}else if(awardType==ONE_AWARD){
			awardid=WinnerManager.getAwardListTow();
		}else{
			awardid=WinnerManager.getAwardListThree();
		}
		WinnerRoleRecord record =WinnerManager.winnerrolerecords.get(roleid);
		if(record==null){
			return false;
		}
		WinnerManager.logger.info("角色id "+roleid+"\t领取冠军试炼奖励，名次"+awardType);
		if(record.getAwardflag()==1){
			return false;
		}
		Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);
		if (bag.isFull())
		{
			fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);
			if(tempBag.doAddItem(awardid, 1, "冠军试炼奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guanjunshilianget, 2)!=1){
				WinnerManager.logger.info("玩家roleid "+roleid+"冠军试炼奖励出错2");
			}
		}
		else
		{
			if(bag.doAddItem(awardid, 1, "冠军试炼奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guanjunshilianget, 2)!=1){
				WinnerManager.logger.info("玩家roleid "+roleid+"冠军试炼奖励出错2");
			}
		}
		WinnerManager.logger.info("角色id "+roleid+"\t领取冠军试炼奖励，名次"+awardType+"\t奖励领取成功");
		return true;
	}

}
