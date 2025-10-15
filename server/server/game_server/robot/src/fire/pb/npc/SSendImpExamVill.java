
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendImpExamVill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendImpExamVill extends __SSendImpExamVill__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795461;

	public int getType() {
		return 795461;
	}

	public fire.pb.npc.ImpExamBean impexamdata;
	public int historyright; // 历史最多答对题目数
	public byte isover; // 结束 0=没结束 1=结束

	public SSendImpExamVill() {
		impexamdata = new fire.pb.npc.ImpExamBean();
	}

	public SSendImpExamVill(fire.pb.npc.ImpExamBean _impexamdata_, int _historyright_, byte _isover_) {
		this.impexamdata = _impexamdata_;
		this.historyright = _historyright_;
		this.isover = _isover_;
	}

	public final boolean _validator_() {
		if (!impexamdata._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(impexamdata);
		_os_.marshal(historyright);
		_os_.marshal(isover);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		impexamdata.unmarshal(_os_);
		historyright = _os_.unmarshal_int();
		isover = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendImpExamVill) {
			SSendImpExamVill _o_ = (SSendImpExamVill)_o1_;
			if (!impexamdata.equals(_o_.impexamdata)) return false;
			if (historyright != _o_.historyright) return false;
			if (isover != _o_.isover) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += impexamdata.hashCode();
		_h_ += historyright;
		_h_ += (int)isover;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(impexamdata).append(",");
		_sb_.append(historyright).append(",");
		_sb_.append(isover).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendImpExamVill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = impexamdata.compareTo(_o_.impexamdata);
		if (0 != _c_) return _c_;
		_c_ = historyright - _o_.historyright;
		if (0 != _c_) return _c_;
		_c_ = isover - _o_.isover;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

