
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CModifyDepotName__ extends mkio.Protocol { }

/** 修改仓库名称
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CModifyDepotName extends __CModifyDepotName__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787771;

	public int getType() {
		return 787771;
	}

	public int depotindex;
	public java.lang.String depotname;

	public CModifyDepotName() {
		depotname = "";
	}

	public CModifyDepotName(int _depotindex_, java.lang.String _depotname_) {
		this.depotindex = _depotindex_;
		this.depotname = _depotname_;
	}

	public final boolean _validator_() {
		if (depotindex < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(depotindex);
		_os_.marshal(depotname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		depotindex = _os_.unmarshal_int();
		depotname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CModifyDepotName) {
			CModifyDepotName _o_ = (CModifyDepotName)_o1_;
			if (depotindex != _o_.depotindex) return false;
			if (!depotname.equals(_o_.depotname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += depotindex;
		_h_ += depotname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(depotindex).append(",");
		_sb_.append("T").append(depotname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

