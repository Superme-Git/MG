
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPrenticesList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPrenticesList extends __SPrenticesList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816481;

	public int getType() {
		return 816481;
	}

	public java.util.ArrayList<fire.pb.master.PInfo> prenticelist; // 当前的徒弟
	public java.util.ArrayList<fire.pb.master.PBaseInfo> chushilist; // 出师徒弟列表 做多发50个
	public long shide; // 师德值

	public SPrenticesList() {
		prenticelist = new java.util.ArrayList<fire.pb.master.PInfo>();
		chushilist = new java.util.ArrayList<fire.pb.master.PBaseInfo>();
	}

	public SPrenticesList(java.util.ArrayList<fire.pb.master.PInfo> _prenticelist_, java.util.ArrayList<fire.pb.master.PBaseInfo> _chushilist_, long _shide_) {
		this.prenticelist = _prenticelist_;
		this.chushilist = _chushilist_;
		this.shide = _shide_;
	}

	public final boolean _validator_() {
		for (fire.pb.master.PInfo _v_ : prenticelist)
			if (!_v_._validator_()) return false;
		for (fire.pb.master.PBaseInfo _v_ : chushilist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(prenticelist.size());
		for (fire.pb.master.PInfo _v_ : prenticelist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(chushilist.size());
		for (fire.pb.master.PBaseInfo _v_ : chushilist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(shide);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.PInfo _v_ = new fire.pb.master.PInfo();
			_v_.unmarshal(_os_);
			prenticelist.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.PBaseInfo _v_ = new fire.pb.master.PBaseInfo();
			_v_.unmarshal(_os_);
			chushilist.add(_v_);
		}
		shide = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPrenticesList) {
			SPrenticesList _o_ = (SPrenticesList)_o1_;
			if (!prenticelist.equals(_o_.prenticelist)) return false;
			if (!chushilist.equals(_o_.chushilist)) return false;
			if (shide != _o_.shide) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += prenticelist.hashCode();
		_h_ += chushilist.hashCode();
		_h_ += (int)shide;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prenticelist).append(",");
		_sb_.append(chushilist).append(",");
		_sb_.append(shide).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

