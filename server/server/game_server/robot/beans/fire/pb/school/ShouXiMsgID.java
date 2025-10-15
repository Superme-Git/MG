
package fire.pb.school;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ShouXiMsgID implements Marshal , Comparable<ShouXiMsgID>{
	public final static int ChallengeLevelNotEnough = 140245; // 挑战首席-弟子是很危险的，40级以后再来挑战吧！”
	public final static int SchoolContriNotEnough = 140246; // 你的职业贡献度不足500点，无法发起挑战！
	public final static int InTeam = 140247; // 请离开队伍再来参加挑战吧！
	public final static int MoneyNotEnough = 140248; // 你身上的金钱好像不足10000两啊！
	public final static int ChallengeSuccess = 140249; // 挑战成功，你可以参与职业首席-弟子竞选了！
	public final static int ChallengeLost = 140250; // 挑战失败，希望你下次再接再厉!
	public final static int NotSuccess = 140251; // 您还未成功挑战首席-弟子，不能参加竞选。
	public final static int AlreadyCandidate = 140252; // 您已成功竞选！
	public final static int VoteLevelNotEnough = 140253; // 等级≥30级的玩家方可参与投票哦！
	public final static int TiLiNotEnough = 140254; // 您的体力不足100点，不能投票！
	public final static int AlreadyVote = 140255; // 您本周内已经投过票，不能再投了！
	public final static int GiveShouXiTitle = 140256; // 恭喜你获得了首席-弟子称号。
	public final static int CancelShouXiTitle = 140257; // 很抱歉，你已不再是首席-弟子。
	public final static int RefreshAbilityToMuch = 140258; // 休息一下，明天再来吧！
	public final static int ChallengeAffirm = 140259; // 挑战首席-弟子需要花费10000两金钱，是否确认挑战？
	public final static int Cantvote = 140260; // 现在不是投票时间哦。
	public final static int CantChallenge = 140261; // 现在不是挑战时间哦。


	public ShouXiMsgID() {
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
		if (_o1_ instanceof ShouXiMsgID) {
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

	public int compareTo(ShouXiMsgID _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

