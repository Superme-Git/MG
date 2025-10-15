package fire.pb.weibo;

import fire.pb.talk.MessageMgr;

/**
 * 玩家发了微博
 * @author nobody
 */
public class PSendWeiBoProc extends mkdb.Procedure{
	
	private  final long roleId;
	
	public PSendWeiBoProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.WeiBoNotify notify = xtable.Role2weibonotify.get(roleId);
		if(notify == null){
			notify = xbean.Pod.newWeiBoNotify();
			xtable.Role2weibonotify.insert(roleId, notify);
		}
		
		if(notify.getTakeawardflag() >= 1){
			return false;
		}
		notify.setTakeawardflag(1);
		MessageMgr.psendSystemMessageToRole(roleId, 145620, null);
		return true;
	}
}