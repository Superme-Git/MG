

package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.item.ItemShuXing;



public class BaotuBattleEndHandler extends BasicEndHandler {
	private final int awardID;
	public BaotuBattleEndHandler(final int awardId) {
		this.awardID = awardId;
	}
	
	@Override
	public boolean handleDeath() {
		// TODO Auto-generated method stub
		super.handleDeath();

		xbean.Fighter f = battle.getFighters().get(BattleField.HOST_FIRST_ID);
		if (f == null) {
			return true;
		}

		fire.pb.item.Pack bag = new fire.pb.item.Pack(f.getUniqueid(), false);
		int num = bag.getBagItemNum(331300);
		if (0 < num) {
			ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(331300);
			List<String> para = new ArrayList<String>(1);
			para.add(attr.getName());
			fire.pb.talk.MessageMgr.sendMsgNotify(f.getUniqueid(), 150191, para);
			return true;
		}
		return true;
	}

	@Override
	protected boolean handleAward() {
      if (!isLeaderAlive(true)) 
    	  return true;
      xbean.Fighter f = battle.getFighters().get(BattleField.HOST_FIRST_ID);
      if (f == null) {
    	  return true;
      }
      
      RewardMgr.getInstance().distributeAllAward(f.getUniqueid(), awardID, null,
    		  fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabao, 67, PAddExpProc.OTHER_QUEST, "藏宝图遭遇战斗");
      
//      fire.pb.item.Bag bag = new fire.pb.item.Bag(f.getUniqueid(), false);
		//331300, 331301
//	  int num = bag.getBagItemNum(331300);
//      if (0 < num) {
//      	ItemAttr attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(331300);
//		List<String> para = new ArrayList<String>(1);
//		para.add(attr.getName());
//		fire.pb.talk.Message.sendMsgNotify(f.getUniqueid(), 150191, para);
//		return true;
//      }
      
//      num = bag.getBagItemNum(331301);
//      if (0 < num) {
//      	ItemAttr attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(331301);
//		List<String> para = new ArrayList<String>(1);
//		para.add(attr.getName());
//		fire.pb.talk.Message.sendMsgNotify(f.getUniqueid(), 150191, para);
//		return true;
//      }
      return true;
	}

}

