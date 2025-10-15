
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendImpExamProv__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendImpExamProv extends __SSendImpExamProv__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795462;

	public int getType() {
		return 795462;
	}

	public fire.pb.npc.ImpExamBean impexamdata;
	public byte lost; // 0=继续 1=失败
	public java.lang.String titlename; // 获得的称号
	public java.util.HashMap<Integer,Integer> rightmap;

	public SSendImpExamProv() {
		impexamdata = new fire.pb.npc.ImpExamBean();
		titlename = "";
		rightmap = new java.util.HashMap<Integer,Integer>();
	}

	public SSendImpExamProv(fire.pb.npc.ImpExamBean _impexamdata_, byte _lost_, java.lang.String _titlename_, java.util.HashMap<Integer,Integer> _rightmap_) {
		this.impexamdata = _impexamdata_;
		this.lost = _lost_;
		this.titlename = _titlename_;
		this.rightmap = _rightmap_;
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
		_os_.marshal(lost);
		_os_.marshal(titlename, "UTF-16LE");
		_os_.compact_uint32(rightmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : rightmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		impexamdata.unmarshal(_os_);
		lost = _os_.unmarshal_byte();
		titlename = _os_.unmarshal_String("UTF-16LE");
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			rightmap.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendImpExamProv) {
			SSendImpExamProv _o_ = (SSendImpExamProv)_o1_;
			if (!impexamdata.equals(_o_.impexamdata)) return false;
			if (lost != _o_.lost) return false;
			if (!titlename.equals(_o_.titlename)) return false;
			if (!rightmap.equals(_o_.rightmap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += impexamdata.hashCode();
		_h_ += (int)lost;
		_h_ += titlename.hashCode();
		_h_ += rightmap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(impexamdata).append(",");
		_sb_.append(lost).append(",");
		_sb_.append("T").append(titlename.length()).append(",");
		_sb_.append(rightmap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

