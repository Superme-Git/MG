package fire.pb.gm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.map.SceneManager;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventTimerGroupData;
import fire.pb.mission.treasuremap.EventTimerNpcData;
import fire.pb.mission.treasuremap.GiftByEvent;
import fire.pb.talk.MessageMgr;

public class GM_shuaguai extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			usage();
			return false;
		}
		final long roleid = getGmroleid();
		final int giftid = Integer.parseInt(args[0]);

		GiftByEvent eventAward = BaoTuMapManager.getInstance().getEventGift(giftid);
		if (giftid >= 4 && giftid <= 6) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					boolean isSucc = TimerNpcService.getInstance().GMResetActNpc(giftid);
					if (isSucc) {
						sendToGM("命令执行成功");
					}else {
						sendToGM("命令执行失败");
					}
					return true;
				}
			}.submit();
			return true;
		}

		if (eventAward != null) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventAward.group);
					if (eventData == null) {
						CircleTaskManager.logger.error("事件刷怪组数据读取错误," + eventAward.name);
						return false;
					}

					EventTimerGroupData eventGroupData = BaoTuMapManager
							.getInstance().getEventTimerGroupData(eventData, roleid);
					if (eventGroupData == null) {
						CircleTaskManager.logger.error("事件刷怪组详细数据读取错误,"
								+ eventAward.name);
						return false;
					}
					
					if (eventGroupData.delaysec > 0) {
						mkdb.Executor.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								TimerNpcService.getInstance().createTimerNpcByData(eventGroupData, eventAward.noticeId, roleid);
							}
						}, eventGroupData.delaysec, TimeUnit.SECONDS);
						
						List<String> param = new ArrayList<String>(1);
						param.add(String.valueOf(eventGroupData.delaysec));
						SceneManager.sendAll(MessageMgr.getMsgNotify(eventGroupData.delaynoticeid, 0, param));
					}
					else {
						TimerNpcService.getInstance().createTimerNpcByData(eventGroupData,	eventAward.noticeId, roleid);
					}
					
					MessageMgr.psendMsgNotify(roleid, eventAward.messageId, null);
					
					return true;
				}
				
			}.submit();
			
		}

		return true;
	}

	@Override
	String usage() {
		return "//shuaguai";
	}

}
