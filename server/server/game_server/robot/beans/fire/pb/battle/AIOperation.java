
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** AI protocol
*/
public class AIOperation implements Marshal , Comparable<AIOperation>{
	public int actionseq; // 在一回合的脚本中，第几个ResultItem
	public int actionmoment; // -1:攻击者行动前；0：攻击者行动后;1-28：对应ID的战斗者死亡时
	public int actionfighterid; // 做动作的战斗者
	public int actionid; // 需要做的AIAction ID

	public AIOperation() {
	}

	public AIOperation(int _actionseq_, int _actionmoment_, int _actionfighterid_, int _actionid_) {
		this.actionseq = _actionseq_;
		this.actionmoment = _actionmoment_;
		this.actionfighterid = _actionfighterid_;
		this.actionid = _actionid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(actionseq);
		_os_.marshal(actionmoment);
		_os_.marshal(actionfighterid);
		_os_.marshal(actionid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		actionseq = _os_.unmarshal_int();
		actionmoment = _os_.unmarshal_int();
		actionfighterid = _os_.unmarshal_int();
		actionid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AIOperation) {
			AIOperation _o_ = (AIOperation)_o1_;
			if (actionseq != _o_.actionseq) return false;
			if (actionmoment != _o_.actionmoment) return false;
			if (actionfighterid != _o_.actionfighterid) return false;
			if (actionid != _o_.actionid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += actionseq;
		_h_ += actionmoment;
		_h_ += actionfighterid;
		_h_ += actionid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(actionseq).append(",");
		_sb_.append(actionmoment).append(",");
		_sb_.append(actionfighterid).append(",");
		_sb_.append(actionid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(AIOperation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = actionseq - _o_.actionseq;
		if (0 != _c_) return _c_;
		_c_ = actionmoment - _o_.actionmoment;
		if (0 != _c_) return _c_;
		_c_ = actionfighterid - _o_.actionfighterid;
		if (0 != _c_) return _c_;
		_c_ = actionid - _o_.actionid;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

