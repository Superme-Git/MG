package fire.pb.gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fire.pb.Item;
import fire.pb.PAddExpProc;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemShuXing;
import fire.pb.item.groceries.SuperTreasureMapItem;
import fire.pb.item.groceries.TreasuremapItem;
import fire.pb.map.FindTreasureGift;
import fire.pb.map.SFindTreasureGift;
import fire.pb.map.SceneManager;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.GiftByMap;
import fire.pb.mission.treasuremap.GiftResult;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;

public class GM_cangbaotu extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;
		
		BuffAgent roleImpl = new BuffRoleImpl(roleId, true);
    	if (roleImpl.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER)){
        	return false;
        }
		
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if (team != null && (!team.isAbsentMember(roleId))) {
			if (!team.isTeamLeader(roleId)) {
				return false;
			}
		}
		
		HashMap<Integer, Integer> itemBaotu = new HashMap<Integer, Integer>();
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, true);
		for (Item item : bag.getPackInfo().items) {
			ItemBase bitem = bag.getItem(item.key);
			if (bitem == null)
				continue;
			
			if (!(bitem instanceof GroceryItem))
				continue;

			fire.pb.item.GroceryItemShuXing groceryattr = (fire.pb.item.GroceryItemShuXing)bitem.getItemAttr();
			if (groceryattr.getTypeid() == 118) {
				itemBaotu.put(item.key, 118);
			} else if (groceryattr.getTypeid() == 198) {
				itemBaotu.put(item.key, 198);
			}
		}
		
		new PCangBaoTu(roleId, itemBaotu).submit();
		
		return true;
	}

	private class PCangBaoTu extends mkdb.Procedure {
		private final long roleid;
		private final HashMap<Integer, Integer> itembaotu;
		
		PCangBaoTu(final long roleid, final HashMap<Integer, Integer> itembaotu) {
			this.roleid = roleid;
			this.itembaotu = itembaotu;
		}
		
		@Override
		protected boolean process() throws Exception {
			final int roleLevel = xtable.Properties.selectLevel(roleid);
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			for (int itemkey : itembaotu.keySet()) {
				int itemtype = itembaotu.get(itemkey);
				int itemid = 0;
				GiftResult giftRet = null;
				int tMapID = 0;
				if (itemtype == 118) {
					TreasuremapItem item = (TreasuremapItem) bag.getItem(itemkey);
					if (item == null)
						continue;
					
					xbean.ArchMap mapProp = item.getExtInfo();
					if (mapProp == null)
						continue;
					
					itemid = item.getItemId();
					tMapID = mapProp.getMapid();
					giftRet = BaoTuMapManager.getInstance().getGiftResult(item.getItemId(), roleLevel);
				} else {
					SuperTreasureMapItem item = (SuperTreasureMapItem) bag.getItem(itemkey);
					if (item == null)
						continue;
				
					xbean.ArchMap mapProp = item.getExtInfo();
					if (mapProp == null)
						continue;
					
					itemid = item.getItemId();
					tMapID = mapProp.getMapid();
					giftRet = BaoTuMapManager.getInstance().getGiftResult(item.getItemId(), roleLevel);
				}
				
				if (giftRet == null)
					continue;

				int awardid = 0;
				int awardRetId = 0;
				
				int awardType = giftRet.getAwardId();
			
				if (awardType > 100) {
					List<GiftByMap> itemAwardList = BaoTuMapManager.getInstance().getGiftMap(awardType);
					if (itemAwardList.size() == 1) {
						GiftByMap giftByMap = itemAwardList.get(0);
						if (giftByMap.groupAward.size() == 0) {
							awardRetId = 0;
						}
						else {
							awardRetId = giftByMap.getAwardId(roleLevel);
						}
						
						awardid = giftByMap.id;
					}
					else {
						for (GiftByMap giftByMap : itemAwardList) {
							if (giftByMap.mapId == tMapID) {
								awardRetId = giftByMap.getAwardId(roleLevel);
								awardid = giftByMap.id;
								break;
							}
						}
						
					}
				} else if(awardType < 10) {
					continue;
				}
				
				int suc = bag.removeItemWithKey(itemkey, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabaouse, 0, "Treasuremap used success");
				if (suc != 1) {
					continue;
				}
				
				if (awardRetId > 10) { //发物品
					FindTreasureGift TreasureGift = BaoTuMapManager.getInstance().mapGift.get(awardid);
					int realAdd = BagUtil.addItem(roleid, awardRetId, 1, "treasuremap", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabao, awardRetId);
					if (realAdd == 1) {
						ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(awardRetId);
						if (attr == null) {
							continue;
						}
						
						MessageMgr.psendMsgNotifyWhileCommit(roleid, TreasureGift.getItemMessageId, null);
						MessageUtil.psendAddItemWhileCommit(roleid, awardRetId, realAdd);
						
						if (attr.rare == 1) {
							List<String> param = BaoTuMapManager.getInstance().getItemNotice(awardRetId, roleid);
							SceneManager.sendAll(MessageMgr.getMsgNotify(TreasureGift.noticeId, 0, param));
						}
					}
					
					gmcheckNextid(itemid);
				}
				else if (awardRetId == 0) { //挖到金钱
					FindTreasureGift TreasureGift = BaoTuMapManager.getInstance().mapGift.get(awardid);
					//发银币
					fire.pb.activity.award.RewardMgr
							.getInstance().distributeAllAward(roleid, TreasureGift.getMoneyAwardId(), null,
									fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabao, TreasureGift.getMoneyAwardId(),
									PAddExpProc.OTHER_QUEST, "藏宝图奖励");
					gmcheckNextid(itemid);
				}
				
			
			
			}
			return true;
		}
		
		void gmcheckNextid(int itemid) {
			if (itemid == 331300) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				//331300, 331301
				int num = bag.getBagItemNum(331300);
		        if (0 < num) {
		        	ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(331300);
		        	List<String> para = new ArrayList<String>(1);
		        	para.add(attr.getName());
		        	MessageMgr.psendMsgNotifyWhileCommit(roleid, 150191, para);
		            return;
		        }
			}
		}
	}
	
	@Override
	String usage() {
		return null;
	}
}
