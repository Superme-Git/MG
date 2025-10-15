
package fire.pb.cross;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SendRoleData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SendRoleData extends __SendRoleData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819066;

	public int getType() {
		return 819066;
	}

	public long roleid;
	public java.lang.String tablename;
	public com.locojoy.base.Octets valuedata;
	public com.locojoy.base.Octets keydata;
	public byte isemptytable; // 是否为空表，1是
	public java.util.LinkedList<fire.pb.cross.TableData> relationdata; // 相关联数据

	public SendRoleData() {
		tablename = "";
		valuedata = new com.locojoy.base.Octets();
		keydata = new com.locojoy.base.Octets();
		relationdata = new java.util.LinkedList<fire.pb.cross.TableData>();
	}

	public SendRoleData(long _roleid_, java.lang.String _tablename_, com.locojoy.base.Octets _valuedata_, com.locojoy.base.Octets _keydata_, byte _isemptytable_, java.util.LinkedList<fire.pb.cross.TableData> _relationdata_) {
		this.roleid = _roleid_;
		this.tablename = _tablename_;
		this.valuedata = _valuedata_;
		this.keydata = _keydata_;
		this.isemptytable = _isemptytable_;
		this.relationdata = _relationdata_;
	}

	public final boolean _validator_() {
		for (fire.pb.cross.TableData _v_ : relationdata)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(tablename, "UTF-16LE");
		_os_.marshal(valuedata);
		_os_.marshal(keydata);
		_os_.marshal(isemptytable);
		_os_.compact_uint32(relationdata.size());
		for (fire.pb.cross.TableData _v_ : relationdata) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		tablename = _os_.unmarshal_String("UTF-16LE");
		valuedata = _os_.unmarshal_Octets();
		keydata = _os_.unmarshal_Octets();
		isemptytable = _os_.unmarshal_byte();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.cross.TableData _v_ = new fire.pb.cross.TableData();
			_v_.unmarshal(_os_);
			relationdata.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SendRoleData) {
			SendRoleData _o_ = (SendRoleData)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!tablename.equals(_o_.tablename)) return false;
			if (!valuedata.equals(_o_.valuedata)) return false;
			if (!keydata.equals(_o_.keydata)) return false;
			if (isemptytable != _o_.isemptytable) return false;
			if (!relationdata.equals(_o_.relationdata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += tablename.hashCode();
		_h_ += valuedata.hashCode();
		_h_ += keydata.hashCode();
		_h_ += (int)isemptytable;
		_h_ += relationdata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(tablename.length()).append(",");
		_sb_.append("B").append(valuedata.size()).append(",");
		_sb_.append("B").append(keydata.size()).append(",");
		_sb_.append(isemptytable).append(",");
		_sb_.append(relationdata).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

