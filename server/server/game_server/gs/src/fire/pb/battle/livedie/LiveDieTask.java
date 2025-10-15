package fire.pb.battle.livedie;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import fire.log.Logger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.talk.MessageMgr;
import fire.pb.timer.Module;
import fire.pb.title.PRemoveTitleProc;
import fire.pb.title.Title;

public class LiveDieTask  extends TimerTask{
	@Override
	public void run() {
		ConcurrentHashMap<Long, Long> map=new ConcurrentHashMap<Long, Long>();
		map.putAll(LiveDieMange.liveDieRoleids);
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				//6.应战玩家点击应战，1小时内没有去生死战管理员出点击迎战，则判为他不敢应战
				for(Entry<Long,Long> entry:map.entrySet()){
					long hostid=entry.getKey();
					long guestid=entry.getValue();
					mkdb.Procedure proc=new mkdb.Procedure(){
						@Override
						protected boolean process() throws Exception {
							//获得战书信息
							//获得下战书信息，判断是否过期
							//加锁
							List<Long> list = new ArrayList<Long>();
							list.add(hostid);
							list.add(guestid);
							mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));
							xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(hostid);
							if(hostliveDieRoleInfo!=null){
								if(System.currentTimeMillis()-hostliveDieRoleInfo.getInvitationtime()>LiveDieMange.getLiveDieTime()){
									//被挑战方失败
									xtable.Livedie2key.remove(guestid);
									xtable.Livedieroleinfotab.remove(hostid);
									//对方不敢迎战则把钱还给她
									fire.pb.item.Pack bag = new fire.pb.item.Pack(hostid, false);
									int cost=LiveDieMange.getLiveDieCostMoney();
									bag.addSysMoney(cost, "生死战退回押金", YYLoggerTuJingEnum.tujing_Value_shengsizhanyajin, 0);
									//给好友提示
									String hostname=new PropRole(hostid,true).getName();
									String guestname=new PropRole(guestid,true).getName();
									MessageMgr.psendSystemMessageToRole(hostid, 162087, Arrays.asList(guestname));
									MessageMgr.sendSystemMsg(162068,Arrays.asList(guestname,hostname));
									logger.info("生死战退回押金"+"roleid\t"+cost);
								}
							}
							return true;
						}
					};
					mkdb.Procedure.pexecuteWhileCommit(proc);
				}
				return true;
			};
		}.submit();
		
//		//处理限时称号逻辑
//		ConcurrentHashMap<Long, Long> titlemap=new ConcurrentHashMap<Long, Long>();
//		titlemap.putAll(Title.titleroleids);
//		long time=System.currentTimeMillis();
//		for(long roleId:titlemap.values()){
//			List<Integer> titleIds = new ArrayList<Integer>();
//			xbean.Properties pro = xtable.Properties.select(roleId);
//			for (xbean.TitleInfo titleinfo : pro.getTitles().values()) {
//				if (titleinfo.getAvailtime() > 0)
//					if (titleinfo.getAvailtime() < time)
//						titleIds.add(titleinfo.getTitleid());
//			}
//			for (Integer titleid : titleIds) {
//				Title.titleroleids.remove(roleId);
//				mkdb.Procedure.pexecuteWhileCommit(new PRemoveTitleProc(roleId,
//						titleid));
//			}
//		}
		
	}
	public static final Logger logger = Logger.getLogger("BATTLE");
	
}