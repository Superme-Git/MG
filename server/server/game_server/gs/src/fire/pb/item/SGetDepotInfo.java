
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetDepotInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetDepotInfo extends __SGetDepotInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787770;

	public int getType() {
		return 787770;
	}

	public int pageid; // 从1开始
	public fire.pb.Bag baginfo;

	public SGetDepotInfo() {
		baginfo = new fire.pb.Bag();
	}

	public SGetDepotInfo(int _pageid_, fire.pb.Bag _baginfo_) {
		this.pageid = _pageid_;
		this.baginfo = _baginfo_;
	}

	public final boolean _validator_() {
		if (!baginfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pageid);
		_os_.marshal(baginfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pageid = _os_.unmarshal_int();
		baginfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetDepotInfo) {
			SGetDepotInfo _o_ = (SGetDepotInfo)_o1_;
			if (pageid != _o_.pageid) return false;
			if (!baginfo.equals(_o_.baginfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pageid;
		_h_ += baginfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pageid).append(",");
		_sb_.append(baginfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

