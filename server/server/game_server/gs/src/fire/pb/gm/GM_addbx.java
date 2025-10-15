package fire.pb.gm;

import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.circletask.CircleTaskEventGroup;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventTimerGroupData;
import fire.pb.mission.treasuremap.EventTimerNpcData;
import fire.pb.mission.treasuremap.GiftByEvent;
import fire.pb.talk.MessageMgr;

public class GM_addbx extends GMCommand {

	@Override
	boolean exec(String[] args) {

		final long roleid = getGmroleid();
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				CircleTaskEventGroup eventGroup = CircleTaskManager.getInstance().getEventGroup(10);
				if (eventGroup != null) {
					java.util.List<Integer> groupList = eventGroup.getEventListByGm();
					if (groupList != null) {
						for (Integer giftid : groupList) {
							GiftByEvent eventAward = BaoTuMapManager.getInstance().getEventGift(giftid);
							if (eventAward == null) {
								CircleTaskManager.logger.error("触发事件id错误:" + giftid);
							} else {
								EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventAward.group);
								if (eventData == null) {
									CircleTaskManager.logger.error("事件刷怪组数据读取错误,"
											+ eventAward.name);
									return false;
								}

								EventTimerGroupData eventGroupData = BaoTuMapManager.getInstance().getEventTimerGroupData(eventData, roleid);
								if (eventGroupData == null) {
									CircleTaskManager.logger.error("事件刷怪组详细数据读取错误,"	+ eventAward.name);
									return false;
								}
								TimerNpcService.getInstance().createTimerNpcByData(
										eventGroupData, eventAward.noticeId, roleid);
								MessageMgr.psendMsgNotify(roleid, eventAward.messageId,	null);
							}
						}
					}
				}
				
				return true;
			}
			
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "addbx";
	}

}
