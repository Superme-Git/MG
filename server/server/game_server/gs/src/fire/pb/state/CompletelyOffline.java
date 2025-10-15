package fire.pb.state;

/**
 * 彻底地下线,从场景中移除,结束离线保护状态
 * @author XGM
 */
public class CompletelyOffline implements Runnable {

	public final long roleId;

	public CompletelyOffline(long roleId) {
		this.roleId = roleId;
	}

	@Override
	public void run() {

		// 踢下线
		try {
			new PRoleOffline(roleId, PRoleOffline.TYPE_LINK_BROKEN).submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		gnet.link.Onlines.getInstance().kick(roleId, fire.pb.KickErrConst.ERR_GM_KICKOUT);

		// 关闭离线保护时间的计时器
		try {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					xbean.OfflineFuture efur = xtable.Offlinefuture.get(roleId);
					if (efur != null) {
						if (efur.getTimefuture() != null)
							efur.getTimefuture().cancel(true);
						xtable.Offlinefuture.remove(roleId);
					}
					// 退出离线保护
					pexecuteWhileCommit(new PEndOfflineProtect(roleId));
					return true;
				}
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
