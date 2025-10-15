

package fire.pb.npc;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import mkdb.Procedure;


public class PReceiveAccountInfoAward extends Procedure {

	private final long roleid;

	private final int serviceid;

	public PReceiveAccountInfoAward(long roleid, int serviceid) {

		super();
		this.roleid = roleid;
		this.serviceid = serviceid;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null)
			return true;
		int awardid = 0;
		int lianyunkey = 0;
		if (serviceid == 1078){
			lianyunkey = 16;
			awardid = 1688;
		}
		if (serviceid == 1079){
			lianyunkey = 32;
			awardid = 1689;
		}
        xbean.LianyunAwardInfo lianyunAwardInfo = xtable.Lianyunaward.get(prop.getUserid());
        Long awardTime = lianyunAwardInfo.getAwards().get(lianyunkey);
        if (awardTime!=null&&awardTime==0){
        	RewardMgr.getInstance().distributeAllAward(roleid, awardid, null, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_npcjiangli, awardid, PAddExpProc.OTHER, "补填账号信息奖励");
            lianyunAwardInfo.getAwards().put(lianyunkey, System.currentTimeMillis());
        }
		return true;
	}

}
