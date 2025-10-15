package fire.pb.mission.notify;

import mkdb.Procedure;
import mkdb.Transaction;

public class GiftBagMgr {
	
	static class SingletonHolder {
		static GiftBagMgr singleton = new GiftBagMgr();
	}

	public static GiftBagMgr getInstance() {
		return SingletonHolder.singleton;
	}
	
	//角色登录礼包检测
	public void roleLogin(final long roleId, final int level) {

		Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				// 处理邮件
				new fire.pb.compensation.PRoleLogin(roleId).call();
				return true;
			}
		};

		if (Transaction.current() == null) {
			p.submit();
		} else {
			mkdb.Procedure.pexecuteWhileCommit(p);
		}
	}

}
