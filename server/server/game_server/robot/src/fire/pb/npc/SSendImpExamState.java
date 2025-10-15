
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendImpExamState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendImpExamState extends __SSendImpExamState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795463;

	public int getType() {
		return 795463;
	}

	public fire.pb.npc.ImpExamBean impexamdata;
	public long historymintime; // 历史最短用时
	public int historymaxright; // 历史最多答对题目数
	public java.lang.String titlename; // 获得的称号
	public byte lost; // 0=继续 1=失败
	public long impexamusetime; // 当前考试用时

	public SSendImpExamState() {
		impexamdata = new fire.pb.npc.ImpExamBean();
		titlename = "";
	}

	public SSendImpExamState(fire.pb.npc.ImpExamBean _impexamdata_, long _historymintime_, int _historymaxright_, java.lang.String _titlename_, byte _lost_, long _impexamusetime_) {
		this.impexamdata = _impexamdata_;
		this.historymintime = _historymintime_;
		this.historymaxright = _historymaxright_;
		this.titlename = _titlename_;
		this.lost = _lost_;
		this.impexamusetime = _impexamusetime_;
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
		_os_.marshal(historymintime);
		_os_.marshal(historymaxright);
		_os_.marshal(titlename, "UTF-16LE");
		_os_.marshal(lost);
		_os_.marshal(impexamusetime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		impexamdata.unmarshal(_os_);
		historymintime = _os_.unmarshal_long();
		historymaxright = _os_.unmarshal_int();
		titlename = _os_.unmarshal_String("UTF-16LE");
		lost = _os_.unmarshal_byte();
		impexamusetime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendImpExamState) {
			SSendImpExamState _o_ = (SSendImpExamState)_o1_;
			if (!impexamdata.equals(_o_.impexamdata)) return false;
			if (historymintime != _o_.historymintime) return false;
			if (historymaxright != _o_.historymaxright) return false;
			if (!titlename.equals(_o_.titlename)) return false;
			if (lost != _o_.lost) return false;
			if (impexamusetime != _o_.impexamusetime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += impexamdata.hashCode();
		_h_ += (int)historymintime;
		_h_ += historymaxright;
		_h_ += titlename.hashCode();
		_h_ += (int)lost;
		_h_ += (int)impexamusetime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(impexamdata).append(",");
		_sb_.append(historymintime).append(",");
		_sb_.append(historymaxright).append(",");
		_sb_.append("T").append(titlename.length()).append(",");
		_sb_.append(lost).append(",");
		_sb_.append(impexamusetime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

