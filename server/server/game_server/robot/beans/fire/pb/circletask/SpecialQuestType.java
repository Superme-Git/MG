
package fire.pb.circletask;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 特殊任务的类型,包括职业任务中的各种不同类型以及天-尊和其他特殊任务
*/
public class SpecialQuestType implements Marshal , Comparable<SpecialQuestType>{
	public final static int Mail = 801001; // 送信
	public final static int Patrol = 801002; // 巡逻
	public final static int BuyItem = 801003; // 买道具
	public final static int CatchPet = 801004; // 抓守护
	public final static int Demonstrate = 801005; // 示威
	public final static int DemonstrateEye = 801006; // 示威
	public final static int Rescue = 801007; // 援救
	public final static int Tame = 801008; // 降服
	public final static int CaiJi = 801010; // 采集
	public final static int CaiJiFinish = 801030; // 采集完成
	public final static int ChuanDiXiaoXi = 801011; // 传递消息
	public final static int KillMonster = 801012; // 打怪
	public final static int KillMonsterFinish = 801032; // 打怪完成
	public final static int FindItem = 801013; // 打怪寻物
	public final static int FindItemFinish = 801033; // 打怪寻物完成
	public final static int Answer = 801014; // 答题
	public final static int AnswerFinish = 801034; // 答题完成
	public final static int CatchIt_Normal = 1030001; // 捉鬼
	public final static int CatchIt_Increase = 1030002; // 鬼王


	public SpecialQuestType() {
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
		if (_o1_ instanceof SpecialQuestType) {
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

	public int compareTo(SpecialQuestType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

