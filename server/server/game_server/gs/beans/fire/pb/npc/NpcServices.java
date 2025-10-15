
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** NpcC服务对应标号
*/
public class NpcServices implements Marshal , Comparable<NpcServices>{
	public final static int FORTUNE_WHEEL = 14; // 幸运大转盘
	public final static int SEND_MAIL = 69; // 送信: (动态服务)
	public final static int CHECK_CHIEF_ABILITY = 96; // 查看首席-弟子能力
	public final static int CHALLENGE_CHIEF = 97; // 挑战首席-弟子
	public final static int RUN_FOR_CHIEF = 98; // 我要竞选
	public final static int VOTING = 99; // 我要投票
	public final static int REFRESH_CHIEF_ABILITY = 100; // 我要刷新首席-弟子能力
	public final static int ATTEND_IMPEXAM = 136; // 我要参加智慧试炼
	public final static int LEARN_IMPEXAM_RULE = 137; // 我想了解智慧试炼比赛规则
	public final static int WINNER_START = 166; // 准备好了,请告诉我们第一关的挑战地点
	public final static int CHECK_PERSONAL_CREDIT = 167; // 我想查询下自己的活动积分
	public final static int ADD_PET_LIFE = 168; // 我想增加宠物寿命
	public final static int FIGHT_WINNER = 171; // 挑战冠军试炼对手
	public final static int ENTER_BINGFENG = 355; // 进入冰封王座
	public final static int BACK_COPY_SERVICE = 1469; // 回到副本副本
	public final static int CIRCTASK_SCHOOL1 = 3000; // 新循环任务-职业
	public final static int CIRCTASK_SCHOOL2 = 3001; // 新循环任务-职业
	public final static int CIRCTASK_SCHOOL3 = 3002; // 新循环任务-职业
	public final static int CIRCTASK_SCHOOL4 = 3003; // 新循环任务-职业
	public final static int CIRCTASK_SCHOOL5 = 3004; // 新循环任务-职业
	public final static int CIRCTASK_SCHOOL6 = 3005; // 新循环任务-职业
	public final static int CIRCTASK_SCHOOL_SUBMIT1 = 3010; // 新循环任务提交-职业
	public final static int CIRCTASK_SCHOOL_SUBMIT2 = 3011; // 新循环任务提交-职业
	public final static int CIRCTASK_SCHOOL_SUBMIT3 = 3012; // 新循环任务提交-职业
	public final static int CIRCTASK_SCHOOL_SUBMIT4 = 3013; // 新循环任务提交-职业
	public final static int CIRCTASK_SCHOOL_SUBMIT5 = 3014; // 新循环任务提交-职业
	public final static int CIRCTASK_SCHOOL_SUBMIT6 = 3015; // 新循环任务提交-职业
	public final static int CATCH_IT = 4000; // 循环任务-捉鬼
	public final static int CATCH_IT_SUBMIT = 4001; // 循环任务-捉鬼
	public final static int CATCH_IT_QUERY = 4002; // 循环任务-查询捉鬼
	public final static int CATCH_IT_BJ = 4003; // 循环任务-便捷捉鬼
	public final static int CATCH_IT_Battle = 4004; // 循环任务-捉鬼进入战斗
	public final static int TIMER_NPC_Battle = 4005; // 定时刷怪进入战斗
	public final static int EVENT_NPC_Battle = 4006; // 事件刷怪进入战斗
	public final static int SUBMIT_CIRCTASK = 5000; // 提交循环任务
	public final static int RENXING_CIRCTASK = 5001; // 任性一下循环任务
	public final static int BUY_MEDICINE = 30000; // 购买药品
	public final static int BUY_GOODS = 30001; // 购买物品
	public final static int BUY_EQUIP = 30002; // 购买装备
	public final static int BUY_PET = 30003; // 购买宠物
	public final static int OPEN_SHANGHUI = 30004; // 打开商会
	public final static int QUERY_LINEINST = 100049; // 查询精英副本次数
	public final static int RESET_LINEINST = 100187; // 重置副本
	public final static int ONE_LIEVEL_TITLE = 900054; // 一级称谓 by changhao
	public final static int TWO_LIEVEL_TITLE = 900055; // 二级称谓 by changhao
	public final static int THREE_LIEVEL_TITLE = 900056; // 三级称谓 by changhao
	public final static int LEADER_SEE_CAMPAIGN_LIST = 900057; // 查看竞选名单 by changhao
	public final static int LEADER_MY_CAMPAIGN = 900058; // 我的竞选 by changhao
	public final static int LEADER_CHALLENGE = 900059; // 挑战领袖 by changhao
	public final static int LEADER_CAMPAIGN = 900060; // 竞选领袖 by changhao
	public final static int PET_STORE = 100012; // 宠物仓库
	public final static int EXCHANGE_CODE = 100600; // 兑换码
	public final static int IMPEXAM_STATE = 100704; // 智慧试炼
	public final static int LOOK_YAO_QIAN = 200101; // 照顾摇钱树
	public final static int ENTER_1V1_PVP = 910000; // 送我去竞技场
	public final static int LEAVE_1V1_PVP = 910004; // 离开竞技场
	public final static int ENTER_3V3_PVP = 910010; // 送我去3v3竞技场
	public final static int LEAVE_3V3_PVP = 910014; // 离开3v3竞技场
	public final static int ENTER_5V5_PVP = 910020; // 送我去5v5竞技场
	public final static int LEAVE_5V5_PVP = 910024; // 离开5v5竞技场
	public final static int WATCH_NPC_BATTLE = 910115; // 定时表明雷怪观战
	public final static int WATCH_EVENTNPC_BATTLE = 910116; // 事件表明雷怪观战
	public final static int WATCH_INST_NPC_BATTLE = 910201; // 公会副本观战按钮服务
	public final static int END_INST_NPC_BATTLE = 910202; // 公会副本终止战斗服务
	public final static int ENTER_CLAN_FIGHT = 910030; // 进入公会战 by changhao


	public NpcServices() {
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
		if (_o1_ instanceof NpcServices) {
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

	public int compareTo(NpcServices _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

