
package fire.pb.fushi.redpack;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRedPackHisView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRedPackHisView extends __SSendRedPackHisView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812539;

	public int getType() {
		return 812539;
	}

	public int modeltype; // 红包类型
	public java.lang.String redpackid; // 红包Id
	public java.lang.String redpackdes; // 红包寄语
	public int redpackallnum; // 红包总个数
	public int redpackallmoney; // 红包总金额
	public long time; // 时间
	public java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleHisInfo> redpackrolehisinfolist; // 红包历史信息

	public SSendRedPackHisView() {
		redpackid = "";
		redpackdes = "";
		redpackrolehisinfolist = new java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleHisInfo>();
	}

	public SSendRedPackHisView(int _modeltype_, java.lang.String _redpackid_, java.lang.String _redpackdes_, int _redpackallnum_, int _redpackallmoney_, long _time_, java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleHisInfo> _redpackrolehisinfolist_) {
		this.modeltype = _modeltype_;
		this.redpackid = _redpackid_;
		this.redpackdes = _redpackdes_;
		this.redpackallnum = _redpackallnum_;
		this.redpackallmoney = _redpackallmoney_;
		this.time = _time_;
		this.redpackrolehisinfolist = _redpackrolehisinfolist_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.redpack.RedPackRoleHisInfo _v_ : redpackrolehisinfolist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(redpackid, "UTF-16LE");
		_os_.marshal(redpackdes, "UTF-16LE");
		_os_.marshal(redpackallnum);
		_os_.marshal(redpackallmoney);
		_os_.marshal(time);
		_os_.compact_uint32(redpackrolehisinfolist.size());
		for (fire.pb.fushi.redpack.RedPackRoleHisInfo _v_ : redpackrolehisinfolist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		redpackid = _os_.unmarshal_String("UTF-16LE");
		redpackdes = _os_.unmarshal_String("UTF-16LE");
		redpackallnum = _os_.unmarshal_int();
		redpackallmoney = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.redpack.RedPackRoleHisInfo _v_ = new fire.pb.fushi.redpack.RedPackRoleHisInfo();
			_v_.unmarshal(_os_);
			redpackrolehisinfolist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRedPackHisView) {
			SSendRedPackHisView _o_ = (SSendRedPackHisView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (!redpackdes.equals(_o_.redpackdes)) return false;
			if (redpackallnum != _o_.redpackallnum) return false;
			if (redpackallmoney != _o_.redpackallmoney) return false;
			if (time != _o_.time) return false;
			if (!redpackrolehisinfolist.equals(_o_.redpackrolehisinfolist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += redpackid.hashCode();
		_h_ += redpackdes.hashCode();
		_h_ += redpackallnum;
		_h_ += redpackallmoney;
		_h_ += (int)time;
		_h_ += redpackrolehisinfolist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append("T").append(redpackid.length()).append(",");
		_sb_.append("T").append(redpackdes.length()).append(",");
		_sb_.append(redpackallnum).append(",");
		_sb_.append(redpackallmoney).append(",");
		_sb_.append(time).append(",");
		_sb_.append(redpackrolehisinfolist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

