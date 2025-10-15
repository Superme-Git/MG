package fire.pb.ranklist.proc;

import java.util.List;

import fire.pb.DataInit;
import fire.pb.PropRole;
import fire.pb.instance.SBingFengJiangLiCfg;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/***
 * 冰封排行
 * @author changhao
 *
 */
public class PTryInsertBingFeng extends Procedure {

	private List<BingFingWangZuoRankData> datas;

	private int rankId;

	private int maxrankrolelv = DataInit.ROLE_LEVEL_MAX;

	public PTryInsertBingFeng(List<BingFingWangZuoRankData> datas, int rankId) {
		this.datas = datas;
		this.rankId = rankId;
	}

	@Override
	public boolean process() {
		lock(mkdb.Lockeys.get(xtable.Locks.BINGFENGLISTS, new Object[] { rankId }));
		Object[] rolelocks = new Object[datas.size()];
		for (int i = 0; i < rolelocks.length; i++)
			rolelocks[i] = datas.get(i).roleId;
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, rolelocks));
		
		int schoolId = datas.get(0).schoolId;
		SBingFengJiangLiCfg cfg = BingFengLandMgr.getInstance().getBingFengAwardCfg(rankId);
		if (cfg == null)
			return false;

		maxrankrolelv = cfg.ranklv;
		xbean.BingFengRank rank = xtable.Bingfenglists.get(rankId);

		if (null == rank) {
			rank = xbean.Pod.newBingFengRank();
			xtable.Bingfenglists.insert(rankId, rank);
		}
		
		xbean.BingFengRankList rankList = rank.getRanks().get(schoolId);
		if (rankList == null) {
			rankList = xbean.Pod.newBingFengRankList();
			rank.getRanks().put(schoolId, rankList);
		}

		for (BingFingWangZuoRankData data : datas)
			insert(data, rankList);

		return true;
	}

	private void insert(BingFingWangZuoRankData data, xbean.BingFengRankList list) {
		PropRole role = new PropRole(data.roleId, true);
		if (role.getLevel() > maxrankrolelv) {
			MessageMgr.psendMsgNotify(data.roleId, 143247, null);
			return;// 超出该榜的最大等级，则不上榜
		}

		xbean.BingFengListRecord record = xbean.Pod.newBingFengListRecord();
		record.setTime(System.currentTimeMillis());

		xbean.MarshalBingFengRecord marshRecord = record.getMarshaldata();
		marshRecord.setRoleid(data.roleId);
		marshRecord.setRolename(role.getName());
		marshRecord.setRounds(data.round);
		marshRecord.setTimes(data.second);
		marshRecord.setStage(data.saveId);
		marshRecord.setShool(data.schoolId);

		RankListManager.getInstance().tryInsertRecord(rankId, list.getRecords(), record);
	}

	public static class BingFingWangZuoRankData {
		public final long roleId;
		public final int saveId;
		public final int schoolId; //新加了职业分类
		public final int round;
		public final int second; // 秒

		public BingFingWangZuoRankData(long roleId, int saveId, int schoolId, int round, int second) {
			this.roleId = roleId;
			this.saveId = saveId;
			this.schoolId = schoolId;
			this.round = round;
			this.second = second;
		}
	}

}
