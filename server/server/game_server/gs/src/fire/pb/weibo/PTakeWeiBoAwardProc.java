package fire.pb.weibo;

import fire.pb.item.BagTypes;
import fire.pb.item.Pack;

/**
 * 领悟发微博奖励
 * @author nobody
 */
public class PTakeWeiBoAwardProc extends mkdb.Procedure{
	
	private final long roleId;
	
	public PTakeWeiBoAwardProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.WeiBoNotify notify = xtable.Role2weibonotify.get(roleId);
		if(notify == null){
			notify = xbean.Pod.newWeiBoNotify();
			xtable.Role2weibonotify.insert(roleId, notify);
		}
		
		if(notify.getTakeawardflag() != 1){
			return false;
		}
		
		fire.pb.item.Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		int grade = bag.getRemainSize();
		if(grade<1){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,140655, null);
			return false;
		}
		int addItemResult = bag.doAddItem(39560 , 1, "发微博得奖励 ", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_weibo, 39560);	    
		if (addItemResult == 1){
			notify.setTakeawardflag(2);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145620, null);
			return true;
		}else
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,140655, null);
			return false;
	}
}
