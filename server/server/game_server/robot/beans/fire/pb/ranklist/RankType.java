
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 排行榜类型
*/
public class RankType implements Marshal , Comparable<RankType>{
	public final static int LEVEL_RANK = 1; // 等级排行
	public final static int PET_GRADE_RANK = 9; // 宠物综合能力（评分）排行榜
	public final static int FACTION_RANK = 31; // 公会排行榜
	public final static int ROLE_ZONGHE_RANK = 32; // 人物综合实力排行榜
	public final static int SINGLE_COPY_RANK1 = 21; // 冰封王座单人副本40-49级
	public final static int SINGLE_COPY_RANK2 = 22; // 冰封王座单人副本50-69级
	public final static int SINGLE_COPY_RANK3 = 23; // 冰封王座单人副本70-89级
	public final static int SINGLE_COPY_RANK4 = 24; // 冰封王座单人副本90级
	public final static int TEAM_COPY_RANK1 = 27; // 冰封王座组队副本50-69级
	public final static int TEAM_COPY_RANK2 = 28; // 冰封王座组队副本70-89级
	public final static int ROLE_RANK = 38; // 角色评分 by changhao
	public final static int PROFESSION_WARRIOR_RANK = 43; // 战士 by changhao
	public final static int PROFESSION_MAGIC_RANK = 44; // 法师 by changhao
	public final static int PROFESSION_PRIEST_RANK = 45; // 牧师 by changhao
	public final static int PROFESSION_PALADIN_RANK = 46; // 圣骑 by changhao
	public final static int PROFESSION_HUNTER_RANK = 47; // 猎人 by changhao
	public final static int PROFESSION_DRUID_RANK = 48; // 德鲁伊 by changhao
	public final static int FACTION_RANK_LEVEL = 49; // 公会等级 by changhao
	public final static int FACTION_ZONGHE = 50; // 公会综合实力 by changhao
	public final static int FACTION_MC = 51; // 熔火之心 by changhao
	public final static int FACTION_NAXX = 52; // 纳克萨玛斯 by changhao
	public final static int PROFESSION_ROGUE_RANK = 60; // 盗贼 by changhao
	public final static int PROFESSION_SAMAN_RANK = 61; // 萨满 by changhao
	public final static int PROFESSION_WARLOCK_RANK = 62; // 术士 by changhao
	public final static int FACTION_COPY = 70; // 公会副本 by changhao
	public final static int PVP5_LAST_GRADE1 = 81; // 5v5竞技场上届初级组
	public final static int PVP5_LAST_GRADE2 = 82; // 5v5竞技场上届中级组
	public final static int PVP5_LAST_GRADE3 = 83; // 5v5竞技场上届高级组
	public final static int PVP5_HISTORY_GRADE1 = 84; // 5v5竞技场历史初级组
	public final static int PVP5_HISTORY_GRADE2 = 85; // 5v5竞技场历史中级组
	public final static int PVP5_HISTORY_GRADE3 = 86; // 5v5竞技场历史高级组
	public final static int RED_PACK_1 = 101; // 红包榜 普通服 by changhao
	public final static int RED_PACK_2 = 102; // 红包榜 点卡服 by changhao
	public final static int FLOWER_RECEIVE = 111; // 收花榜 by changhao
	public final static int FLOWER_GIVE = 112; // 送花榜 by changhao
	public final static int CLAN_FIGHT_2 = 120; // 公会战竞赛排名周二那场 by changhao
	public final static int CLAN_FIGHT_4 = 121; // 公会战竞赛排名周四那场 by changhao
	public final static int CLAN_FIGHT_WEEK = 122; // 公会战竞赛排名本轮 by changhao
	public final static int CLAN_FIGHT_HISTROY = 123; // 公会战历史排名 by changhao


	public RankType() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RankType) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(RankType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

