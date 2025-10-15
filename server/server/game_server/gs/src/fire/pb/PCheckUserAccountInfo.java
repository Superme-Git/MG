

package fire.pb;

import java.util.List;

import mkdb.Procedure;


public class PCheckUserAccountInfo extends Procedure {

	private final int userid;

	private final long roleid;

	public PCheckUserAccountInfo(int userid, long roleid) {

		super();
		this.userid = userid;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(userid);
		if (auUserInfo != null) {
			if ((auUserInfo.getAlgorithm() & 0x30) == 0x30) {// 两个奖励都给
				xbean.LianyunAwardInfo lianyunAwardInfo = getLianyunAwardInfo();
				if (!lianyunAwardInfo.getAwards().containsKey(16)) {

					lianyunAwardInfo.getAwards().put(16, 0L);
				}
				if (!lianyunAwardInfo.getAwards().containsKey(32)) {
					// 给奖励
					lianyunAwardInfo.getAwards().put(32, 0L);
				}
				return true;
			} else if ((auUserInfo.getAlgorithm() & 0x12) == 0x12) {// 奖励身份证+姓名,但需要补填账号+密码+邮箱
				xbean.LianyunAwardInfo lianyunAwardInfo = getLianyunAwardInfo();
				if (!lianyunAwardInfo.getAwards().containsKey(16)) {
					// 给奖励
					lianyunAwardInfo.getAwards().put(16, 0L);
				}

				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 144385, null);
				return true;
			}
			if ((auUserInfo.getAlgorithm() & 0x03) == 0x03) {// 需要补填身份证+姓名,账号+密码+邮箱
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 144385, null);
				return true;
			}
			if ((auUserInfo.getAlgorithm() & 0x01) == 0x01) {// 需要补填身份证+姓名
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 144385, null);
			} else if ((auUserInfo.getAlgorithm() & 0x10) == 0x10) {// 身份证+姓名补填的奖励
				xbean.LianyunAwardInfo lianyunAwardInfo = getLianyunAwardInfo();
				if (!lianyunAwardInfo.getAwards().containsKey(16)) {

					lianyunAwardInfo.getAwards().put(16, 0L);
				}
			}
			if ((auUserInfo.getAlgorithm() & 0x02) == 0x02) {// 需要补填账号+密码+邮箱
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 144385, null);
			} else if ((auUserInfo.getAlgorithm() & 0x20) == 0x20) {// 账号+密码+邮箱的奖励
				xbean.LianyunAwardInfo lianyunAwardInfo = getLianyunAwardInfo();
				if (!lianyunAwardInfo.getAwards().containsKey(32)) {
					// 给奖励
					lianyunAwardInfo.getAwards().put(32, 0L);
				}
			}
			
		}
		return true;
	}

	private xbean.LianyunAwardInfo getLianyunAwardInfo() {

		xbean.LianyunAwardInfo lianyunAwardInfo = xtable.Lianyunaward.get(userid);
		if (lianyunAwardInfo == null) {
			lianyunAwardInfo = xbean.Pod.newLianyunAwardInfo();
			xtable.Lianyunaward.insert(userid, lianyunAwardInfo);
		}
		return lianyunAwardInfo;
	}

	public static void addDynamicService(long roleid, List<Integer> result) {

		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null) {
			xbean.LianyunAwardInfo lianyunAwardInfo = xtable.Lianyunaward.select(prop.getUserid());
			if (lianyunAwardInfo != null) {
				Long awardTime = lianyunAwardInfo.getAwards().get(16);
				if (awardTime != null && awardTime == 0)
					result.add(1078);
				awardTime = lianyunAwardInfo.getAwards().get(32);
				if (awardTime != null && awardTime == 0)
					result.add(1079);
			}
		}
	}
}
