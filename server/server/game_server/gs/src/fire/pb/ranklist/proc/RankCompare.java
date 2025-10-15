package fire.pb.ranklist.proc;

import mkdb.Bean;
import xbean.RoleLevelListRecord;

/***
 * 排行榜比较函数
 * @author changhao
 *
 */
public class RankCompare {
	public static boolean CompareTo(mkdb.Bean current, mkdb.Bean other)
	{
		if (current instanceof xbean.RoleLevelListRecord) {
			return compareToRoleLeveListBean(current, other);
		}

		if (current instanceof xbean.PetScoreListRecord) {
			return compareToPetScoreListBean(current, other);
		}

		if (current instanceof xbean.ClanRankRecord) { // 公会等级比较 by changhao
			return compareToFactionRankRecord(current, other);
		}

		if (current instanceof xbean.RoleZongheRankRecord) {
			return compareToRoleZongheRecord(current, other);
		}

		if (current instanceof xbean.RoleProfessionRankRecord) { // 职业比较 by
																	// changhao
			return compareToProcessRecord(current, other);
		}

		if (current instanceof xbean.ClanProgressRankRecord) { // 工会进度比较 by
																	// changhao
			return compareToProcessClanProgressRecord(current, other);
		}

		if (current instanceof xbean.ClanZongHeRankRecord) { // 工会综合比较 by
																// changhao
			return compareToClanZongHeRankRecord(current, other);
		}

		if (current instanceof xbean.RoleRankRecord) { // 人物评分比较 by changhao
			return compareToRoleRecord(current, other);
		}
		
		if (current instanceof xbean.BingFengListRecord) {
			return compareToRoleBingFengListBean(current, other);
		}
		
		if (current instanceof xbean.RedPackRecord) { //红包排行榜 by changhao
			return compareToRedPackRankRecord(current, other);
		}
		
		if (current instanceof xbean.GiveFlowerRecord) { //送花排行榜 by changhao
			return compareToGiveFlowerRankRecord(current, other);
		}

		if (current instanceof xbean.ReceFlowerRecord) { //收花排行榜 by changhao
			return compareToReceFlowerRecord(current, other);
		}
		
		

		return false;
	}
	

	private static boolean compareToRoleBingFengListBean(Bean current,
			Bean other) {
		xbean.BingFengListRecord currentRecord = (xbean.BingFengListRecord) current;
		xbean.BingFengListRecord otherRecord = (xbean.BingFengListRecord) other;

		if (currentRecord.getMarshaldata().getStage() > otherRecord.getMarshaldata().getStage())
			return true;
		
		if (currentRecord.getMarshaldata().getStage() < otherRecord.getMarshaldata().getStage())
			return false;
		
		if (currentRecord.getMarshaldata().getRounds() < otherRecord.getMarshaldata().getRounds())
			return true;
		
		if (currentRecord.getMarshaldata().getRounds() > otherRecord.getMarshaldata().getRounds())
			return false;
		
		if (currentRecord.getMarshaldata().getTimes() < otherRecord.getMarshaldata().getTimes())
			return true;

		return false;
	}


	private static boolean compareToPetScoreListBean(Bean current, Bean other) {
		xbean.PetScoreListRecord currentRecord = (xbean.PetScoreListRecord) current;
		xbean.PetScoreListRecord otherRecord = (xbean.PetScoreListRecord) other;

		if (currentRecord.getMarshaldata().getPetgrade() > otherRecord
				.getMarshaldata().getPetgrade()) {
			return true;
		}

		return false;
	}

	public static boolean isReplace(mkdb.Bean oldBean, mkdb.Bean newBean) {

		if (oldBean instanceof xbean.RoleLevelListRecord) {
			xbean.RoleLevelListRecord src = ((xbean.RoleLevelListRecord) oldBean);
			xbean.RoleLevelListRecord des = ((xbean.RoleLevelListRecord) newBean);

			if (src.getMarshaldata().getRoleid() != des.getMarshaldata()
					.getRoleid()) {
				return false;
			}

			if (src.getMarshaldata().getRolename() != des.getMarshaldata()
					.getRolename()){
				return true;
			}
			
			if (src.getMarshaldata().getLevel() >= des.getMarshaldata()
					.getLevel()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSame(mkdb.Bean current, mkdb.Bean other) {
		if (current instanceof xbean.RoleZongheRankRecord) {
			return ((xbean.RoleZongheRankRecord) current).getRoleid() == ((xbean.RoleZongheRankRecord) other)
					.getRoleid();
		}

		if (current instanceof xbean.RoleProfessionRankRecord) {
			return ((xbean.RoleProfessionRankRecord) current).getRoleid() == ((xbean.RoleProfessionRankRecord) other)
					.getRoleid();
		}

		if (current instanceof xbean.ClanProgressRankRecord) {
			return ((xbean.ClanProgressRankRecord) current).getClankey() == ((xbean.ClanProgressRankRecord) other)
					.getClankey();
		}

		if (current instanceof xbean.ClanZongHeRankRecord) {
			return ((xbean.ClanZongHeRankRecord) current).getClankey() == ((xbean.ClanZongHeRankRecord) other)
					.getClankey();
		}

		if (current instanceof xbean.ClanRankRecord) {
			return ((xbean.ClanRankRecord) current).getClankey() == ((xbean.ClanRankRecord) other)
					.getClankey();
		}

		if (current instanceof xbean.RoleRankRecord) {
			return ((xbean.RoleRankRecord) current).getRoleid() == ((xbean.RoleRankRecord) other)
					.getRoleid();
		}

		if (current instanceof xbean.RoleLevelListRecord) {
			return ((xbean.RoleLevelListRecord) current).getMarshaldata()
					.getRoleid() == ((xbean.RoleLevelListRecord) other)
					.getMarshaldata().getRoleid();
		}

		if (current instanceof xbean.ApprenticelListRecord) {
			return ((xbean.ApprenticelListRecord) current).getMarshaldata()
					.getRoleid() == ((xbean.ApprenticelListRecord) other)
					.getMarshaldata().getRoleid();
		}

		if (current instanceof xbean.BingFengListRecord) {
			return ((xbean.BingFengListRecord) current).getMarshaldata()
					.getRoleid() == ((xbean.BingFengListRecord) other)
					.getMarshaldata().getRoleid();
		}

		if (current instanceof xbean.PetScoreListRecord) {
			return ((xbean.PetScoreListRecord) current).getMarshaldata()
					.getUniquepetid() == ((xbean.PetScoreListRecord) other)
					.getMarshaldata().getUniquepetid();
		}

		if (current instanceof xbean.ClanRankRecord) {
			return ((xbean.ClanRankRecord) current).getClankey() == ((xbean.ClanRankRecord) other)
					.getClankey();
		}

		if (current instanceof xbean.MasterRankRecord) {
			return ((xbean.MasterRankRecord) current).getRoleid() == ((xbean.MasterRankRecord) other)
					.getRoleid();
		}
		
		if (current instanceof xbean.RedPackRecord)
		{
			return ((xbean.RedPackRecord) current).getMarshaldata().getRoleid() == ((xbean.RedPackRecord) other).getMarshaldata().getRoleid();
		}
		
		if (current instanceof xbean.GiveFlowerRecord)
		{
			return ((xbean.GiveFlowerRecord) current).getMarshaldata().getRoleid() == ((xbean.GiveFlowerRecord) other).getMarshaldata().getRoleid();
		}
		
		if (current instanceof xbean.ReceFlowerRecord)
		{
			return ((xbean.ReceFlowerRecord) current).getMarshaldata().getRoleid() == ((xbean.ReceFlowerRecord) other).getMarshaldata().getRoleid();
		}

		return false;
	}

	private static boolean compareToRoleLeveListBean(Bean current, Bean other) {
		xbean.RoleLevelListRecord currentRecord = (xbean.RoleLevelListRecord) current;
		xbean.RoleLevelListRecord otherRecord = (RoleLevelListRecord) other;

		if (currentRecord.getMarshaldata().getLevel() > otherRecord
				.getMarshaldata().getLevel()) {
			return true;
		}
		return false;
	}

	private static boolean compareToFactionRankRecord(Bean current, Bean other) {
		xbean.ClanRankRecord currentRecord = (xbean.ClanRankRecord) current;
		xbean.ClanRankRecord otherRecord = (xbean.ClanRankRecord) other;
		if (currentRecord.getLevel() > otherRecord.getLevel()) {
			return true;
		}

		if (currentRecord.getLevel() == otherRecord.getLevel()) {
			return currentRecord.getTriggertime() < otherRecord
					.getTriggertime();
		}

		return false;
	}
	
	/**
	 * 红包排行榜 by changhao
	 * @param current
	 * @param other
	 * @return
	 */
	private static boolean compareToRedPackRankRecord(Bean current, Bean other)
	{
		xbean.RedPackRecord currentRecord = (xbean.RedPackRecord) current;
		xbean.RedPackRecord otherRecord = (xbean.RedPackRecord) other;
		if (currentRecord.getMarshaldata().getNum() > otherRecord.getMarshaldata().getNum())
		{
			return true;
		}

		if (currentRecord.getMarshaldata().getNum() == otherRecord.getMarshaldata().getNum())
		{
			return currentRecord.getTriggertime() < otherRecord.getTriggertime();
		}

		return false;
	}
	
	/**
	 * 收花排行榜 by changhao
	 * @param current
	 * @param other
	 * @return
	 */
	private static boolean compareToReceFlowerRecord(Bean current, Bean other)
	{
		xbean.ReceFlowerRecord currentRecord = (xbean.ReceFlowerRecord) current;
		xbean.ReceFlowerRecord otherRecord = (xbean.ReceFlowerRecord) other;
		if (currentRecord.getMarshaldata().getNum() > otherRecord.getMarshaldata().getNum())
		{
			return true;
		}

		if (currentRecord.getMarshaldata().getNum() == otherRecord.getMarshaldata().getNum())
		{
			return currentRecord.getTriggertime() < otherRecord.getTriggertime();
		}

		return false;
	}
	
	/**
	 * 送花排行榜 by changhao
	 * @param current
	 * @param other
	 * @return
	 */
	private static boolean compareToGiveFlowerRankRecord(Bean current, Bean other)
	{
		xbean.GiveFlowerRecord currentRecord = (xbean.GiveFlowerRecord) current;
		xbean.GiveFlowerRecord otherRecord = (xbean.GiveFlowerRecord) other;
		if (currentRecord.getMarshaldata().getNum() > otherRecord.getMarshaldata().getNum())
		{
			return true;
		}

		if (currentRecord.getMarshaldata().getNum() == otherRecord.getMarshaldata().getNum())
		{
			return currentRecord.getTriggertime() < otherRecord.getTriggertime();
		}

		return false;
	}

	private static boolean compareToRoleZongheRecord(Bean current, Bean other) {
		xbean.RoleZongheRankRecord currentRecord = (xbean.RoleZongheRankRecord) current;
		xbean.RoleZongheRankRecord otherRecord = (xbean.RoleZongheRankRecord) other;

		if (currentRecord.getScore() > otherRecord.getScore()) {
			return true;
		}

		if (currentRecord.getScore() == otherRecord.getScore()) {
			if (currentRecord.getLevel() < otherRecord.getLevel()) {
				return true;
			} else {
				if (currentRecord.getLevel() == otherRecord.getLevel()) {
					if (currentRecord.getTriggertime() < otherRecord
							.getTriggertime()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static boolean compareToRoleRecord(Bean current, Bean other) {
		xbean.RoleRankRecord currentRecord = (xbean.RoleRankRecord) current;
		xbean.RoleRankRecord otherRecord = (xbean.RoleRankRecord) other;

		if (currentRecord.getScore() > otherRecord.getScore()) {
			return true;
		}

		if (currentRecord.getScore() == otherRecord.getScore()) {
			if (currentRecord.getLevel() < otherRecord.getLevel()) {
				return true;
			} else {
				if (currentRecord.getLevel() == otherRecord.getLevel()) {
					if (currentRecord.getTriggertime() < otherRecord
							.getTriggertime()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static boolean compareToProcessRecord(Bean current, Bean other) {
		xbean.RoleProfessionRankRecord currentRecord = (xbean.RoleProfessionRankRecord) current;
		xbean.RoleProfessionRankRecord otherRecord = (xbean.RoleProfessionRankRecord) other;

		if (currentRecord.getScore() > otherRecord.getScore()) {
			return true;
		}

		if (currentRecord.getScore() == otherRecord.getScore()) {
			if (currentRecord.getLevel() < otherRecord.getLevel()) {
				return true;
			} else {
				if (currentRecord.getLevel() == otherRecord.getLevel()) {
					if (currentRecord.getTriggertime() < otherRecord
							.getTriggertime()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static boolean compareToProcessClanProgressRecord(Bean current,
			Bean other) {
		xbean.ClanProgressRankRecord currentRecord = (xbean.ClanProgressRankRecord) current;
		xbean.ClanProgressRankRecord otherRecord = (xbean.ClanProgressRankRecord) other;
		
		if (currentRecord.getCopyid() > otherRecord.getCopyid())
		{
			return true;
		}
		
		if (currentRecord.getCopyid() == otherRecord.getCopyid())
		{
			if (currentRecord.getProgress() > otherRecord.getProgress())
			{
				return true;
			}
			else
			{
				if (currentRecord.getProgress() == otherRecord.getProgress())
				{
					if (currentRecord.getProgress() == 10)
					{
						if (currentRecord.getBosshp() < otherRecord.getBosshp())
						{
							return true;
						}
						else
						{
							if (currentRecord.getBosshp() == otherRecord.getBosshp())
							{
								if (currentRecord.getTime() < otherRecord.getTime())
								{
									return true;
								}
								else
								{
									if (currentRecord.getTime() == otherRecord.getTime())
									{
										return currentRecord.getTriggertime() < otherRecord.getTriggertime();
									}
								}									
							}
						}
					}
					else
					{
						if (currentRecord.getTime() < otherRecord.getTime())
						{
							return true;
						}
						else
						{
							if (currentRecord.getTime() == otherRecord.getTime())
							{
								return currentRecord.getTriggertime() < otherRecord.getTriggertime();
							}
						}						
					}
				}				
			}
		}

		return false;
	}

	private static boolean compareToClanZongHeRankRecord(Bean current,
			Bean other) {
		xbean.ClanZongHeRankRecord currentRecord = (xbean.ClanZongHeRankRecord) current;
		xbean.ClanZongHeRankRecord otherRecord = (xbean.ClanZongHeRankRecord) other;
		if (currentRecord.getZonghe() > otherRecord.getZonghe()) {
			return true;
		}

		if (currentRecord.getZonghe() == otherRecord.getZonghe()) {
			return currentRecord.getTriggertime() < otherRecord
					.getTriggertime();
		}

		return false;
	}
}
