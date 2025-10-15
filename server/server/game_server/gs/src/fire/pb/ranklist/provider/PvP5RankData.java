package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.battle.pvp5.PvP5RankVisitor;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

/**
 * PvP5排行数据
 * @author XGM
 */
public class PvP5RankData extends RankData implements IRankListData {

	PvP5RankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {

		xbean.PvP5ScoreRankList rankList = null;

		int grade = PvP5RankVisitor.getLastRankGrade(getRankType());
		if (grade != -1) {
			// 上届榜单
			rankList = xtable.Pvp5scorelist.select(grade);
		} else {
			grade = PvP5RankVisitor.getHistoryRankGrade(getRankType());
			if (grade != -1) {
				// 历史榜单
				rankList = xtable.Pvp5historyscorelist.select(grade);
			}
		}

		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		if (rankList != null) {

			List<xbean.PvP5ScoreRecord> records = rankList.getRecords();
			int recordSize = records.size();
			// 榜单需要显示的人数
			int recordShowMax = RankListManager.getInstance().getPersonNumMaxByType(getRankType());
			int size = Math.min(recordSize, recordShowMax);

			int start = page * RankListManager.PAGE_SIZE;
			int end = (page + 1) * RankListManager.PAGE_SIZE;
			int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < size ? 0 : 1;

			// 填充榜单
			for (int i = 0; i < size; i++) {
				xbean.PvP5ScoreRecord record = records.get(i);
				if (i >= start && i < end) {
					OctetsStream os = new OctetsStream();
					os.marshal(i + 1);
					os.marshal(record.getRoleid());
					os.marshal(record.getRolename(), "UTF-16LE");
					os.marshal(record.getScore());
					os.marshal(record.getSchool());
					octets.add(os);
				}
				if (record.getRoleid() == requestRoleid) {
					response.myrank = i + 1;
					response.extdata = record.getScore();
				}
			}

			response.hasmore = hasMorePage;
			response.page = page;
		}
		return octets;
	}
}
