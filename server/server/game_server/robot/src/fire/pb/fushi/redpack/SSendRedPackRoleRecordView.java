
package fire.pb.fushi.redpack;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRedPackRoleRecordView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRedPackRoleRecordView extends __SSendRedPackRoleRecordView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812541;

	public int getType() {
		return 812541;
	}

	public int modeltype; // 0发出 1收到
	public int firstpageflag; // 用来处理分页 0表示第一页   1表示其他页
	public int redpackallnum; // 红包总个数
	public long redpackallmoney; // 红包总金额
	public long redpackallfushi; // 红包总符石
	public java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleRecord> redpackrolerecord; // 记录信息

	public SSendRedPackRoleRecordView() {
		redpackrolerecord = new java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleRecord>();
	}

	public SSendRedPackRoleRecordView(int _modeltype_, int _firstpageflag_, int _redpackallnum_, long _redpackallmoney_, long _redpackallfushi_, java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleRecord> _redpackrolerecord_) {
		this.modeltype = _modeltype_;
		this.firstpageflag = _firstpageflag_;
		this.redpackallnum = _redpackallnum_;
		this.redpackallmoney = _redpackallmoney_;
		this.redpackallfushi = _redpackallfushi_;
		this.redpackrolerecord = _redpackrolerecord_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.redpack.RedPackRoleRecord _v_ : redpackrolerecord)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(firstpageflag);
		_os_.marshal(redpackallnum);
		_os_.marshal(redpackallmoney);
		_os_.marshal(redpackallfushi);
		_os_.compact_uint32(redpackrolerecord.size());
		for (fire.pb.fushi.redpack.RedPackRoleRecord _v_ : redpackrolerecord) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		firstpageflag = _os_.unmarshal_int();
		redpackallnum = _os_.unmarshal_int();
		redpackallmoney = _os_.unmarshal_long();
		redpackallfushi = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.redpack.RedPackRoleRecord _v_ = new fire.pb.fushi.redpack.RedPackRoleRecord();
			_v_.unmarshal(_os_);
			redpackrolerecord.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRedPackRoleRecordView) {
			SSendRedPackRoleRecordView _o_ = (SSendRedPackRoleRecordView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (firstpageflag != _o_.firstpageflag) return false;
			if (redpackallnum != _o_.redpackallnum) return false;
			if (redpackallmoney != _o_.redpackallmoney) return false;
			if (redpackallfushi != _o_.redpackallfushi) return false;
			if (!redpackrolerecord.equals(_o_.redpackrolerecord)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += firstpageflag;
		_h_ += redpackallnum;
		_h_ += (int)redpackallmoney;
		_h_ += (int)redpackallfushi;
		_h_ += redpackrolerecord.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(firstpageflag).append(",");
		_sb_.append(redpackallnum).append(",");
		_sb_.append(redpackallmoney).append(",");
		_sb_.append(redpackallfushi).append(",");
		_sb_.append(redpackrolerecord).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

