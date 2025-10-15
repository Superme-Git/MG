
package fire.pb.fushi.redpack;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRedPackView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRedPackView extends __SSendRedPackView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812533;

	public int getType() {
		return 812533;
	}

	public int modeltype; // 红包类型
	public int firstpageflag; // 用来处理分页 0表示第一页   1表示其他页
	public java.util.ArrayList<fire.pb.fushi.redpack.RedPackInfo> redpackinfolist; // 红包信息
	public fire.pb.fushi.redpack.SRRedPackNum daysrnum; // 今天收送红包数量

	public SSendRedPackView() {
		redpackinfolist = new java.util.ArrayList<fire.pb.fushi.redpack.RedPackInfo>();
		daysrnum = new fire.pb.fushi.redpack.SRRedPackNum();
	}

	public SSendRedPackView(int _modeltype_, int _firstpageflag_, java.util.ArrayList<fire.pb.fushi.redpack.RedPackInfo> _redpackinfolist_, fire.pb.fushi.redpack.SRRedPackNum _daysrnum_) {
		this.modeltype = _modeltype_;
		this.firstpageflag = _firstpageflag_;
		this.redpackinfolist = _redpackinfolist_;
		this.daysrnum = _daysrnum_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.redpack.RedPackInfo _v_ : redpackinfolist)
			if (!_v_._validator_()) return false;
		if (!daysrnum._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(firstpageflag);
		_os_.compact_uint32(redpackinfolist.size());
		for (fire.pb.fushi.redpack.RedPackInfo _v_ : redpackinfolist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(daysrnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		firstpageflag = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.redpack.RedPackInfo _v_ = new fire.pb.fushi.redpack.RedPackInfo();
			_v_.unmarshal(_os_);
			redpackinfolist.add(_v_);
		}
		daysrnum.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRedPackView) {
			SSendRedPackView _o_ = (SSendRedPackView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (firstpageflag != _o_.firstpageflag) return false;
			if (!redpackinfolist.equals(_o_.redpackinfolist)) return false;
			if (!daysrnum.equals(_o_.daysrnum)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += firstpageflag;
		_h_ += redpackinfolist.hashCode();
		_h_ += daysrnum.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(firstpageflag).append(",");
		_sb_.append(redpackinfolist).append(",");
		_sb_.append(daysrnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

