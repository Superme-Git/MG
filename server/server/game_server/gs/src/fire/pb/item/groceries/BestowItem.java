package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.game.MoneyType;
import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.map.SceneManager;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventTimerGroupData;
import fire.pb.mission.treasuremap.EventTimerNpcData;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;

public class BestowItem extends GroceryItem {

	public BestowItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public BestowItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UsePetItemHandler();
	}
	
	private static class UsePetItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			if (usednum != 1)
				return Commontext.UseResult.FAIL;
			
			int shengwang = 100;
			int eventGroup = 8;
			EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventGroup);
			if (eventData == null)
				return Commontext.UseResult.FAIL;
			
			EventTimerGroupData eventGroupData = BaoTuMapManager.getInstance().getEventTimerGroupData(eventData, roleId);
			if (eventGroupData == null)
				return Commontext.UseResult.FAIL;
			
			xbean.Properties prop = xtable.Properties.get(roleId);
			if (prop == null)
				return Commontext.UseResult.FAIL;
			
			String rolename = prop.getRolename();
			
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			long realAdd = bag.addSysCurrency(shengwang,
					MoneyType.MoneyType_ShengWang, "awardID:" + 0,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_bestow, 0);
			
			if(realAdd > 0)
				MessageUtil.pSendAddOrRemoveCurrency(roleId, realAdd, MoneyType.MoneyType_ShengWang);
			
			if (eventGroupData.delaysec > 0) {
				mkdb.Executor.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						TimerNpcService.getInstance().createBestowTimerNpc(roleId, rolename, eventGroupData);
					}
				}, eventGroupData.delaysec, TimeUnit.SECONDS);
				
				List<String> param = new ArrayList<String>(1);
				param.add(String.valueOf(eventGroupData.delaysec));
				SceneManager.sendAll(MessageMgr.getMsgNotify(eventGroupData.delaynoticeid, 0, param));
				
				MessageMgr.pbroadcastMsgNotify(180022, 0, Arrays.asList(rolename));
			}
			else {
				mkdb.Executor.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						TimerNpcService.getInstance().createBestowTimerNpc(roleId, rolename, eventGroupData);
					}
				}, 10, TimeUnit.SECONDS);
				
				MessageMgr.pbroadcastMsgNotify(180022, 0, Arrays.asList(rolename));
			}
			
			return Commontext.UseResult.SUCC;
		}
	}

}
