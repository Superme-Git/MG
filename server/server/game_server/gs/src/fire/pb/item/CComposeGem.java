
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CComposeGem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CComposeGem extends __CComposeGem__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid( this );
		if (roleId < 0)
			return;
		
		new PComposeGem(roleId, buserongheji, targetgemitemid, baggems, shopgems).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787784;

	public int getType() {
		return 787784;
	}

	public byte buserongheji; // 是否使用融合剂 0:不使用 1:使用
	public int targetgemitemid; // 合成宝石的ID
	public java.util.ArrayList<fire.pb.item.ComposeGemInfoBean> baggems; // 使用的背包宝石
	public java.util.ArrayList<fire.pb.item.ComposeGemInfoBean> shopgems; // 使用的商店宝石

	public CComposeGem() {
		baggems = new java.util.ArrayList<fire.pb.item.ComposeGemInfoBean>();
		shopgems = new java.util.ArrayList<fire.pb.item.ComposeGemInfoBean>();
	}

	public CComposeGem(byte _buserongheji_, int _targetgemitemid_, java.util.ArrayList<fire.pb.item.ComposeGemInfoBean> _baggems_, java.util.ArrayList<fire.pb.item.ComposeGemInfoBean> _shopgems_) {
		this.buserongheji = _buserongheji_;
		this.targetgemitemid = _targetgemitemid_;
		this.baggems = _baggems_;
		this.shopgems = _shopgems_;
	}

	public final boolean _validator_() {
		for (fire.pb.item.ComposeGemInfoBean _v_ : baggems)
			if (!_v_._validator_()) return false;
		for (fire.pb.item.ComposeGemInfoBean _v_ : shopgems)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(buserongheji);
		_os_.marshal(targetgemitemid);
		_os_.compact_uint32(baggems.size());
		for (fire.pb.item.ComposeGemInfoBean _v_ : baggems) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(shopgems.size());
		for (fire.pb.item.ComposeGemInfoBean _v_ : shopgems) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		buserongheji = _os_.unmarshal_byte();
		targetgemitemid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.item.ComposeGemInfoBean _v_ = new fire.pb.item.ComposeGemInfoBean();
			_v_.unmarshal(_os_);
			baggems.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.item.ComposeGemInfoBean _v_ = new fire.pb.item.ComposeGemInfoBean();
			_v_.unmarshal(_os_);
			shopgems.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CComposeGem) {
			CComposeGem _o_ = (CComposeGem)_o1_;
			if (buserongheji != _o_.buserongheji) return false;
			if (targetgemitemid != _o_.targetgemitemid) return false;
			if (!baggems.equals(_o_.baggems)) return false;
			if (!shopgems.equals(_o_.shopgems)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)buserongheji;
		_h_ += targetgemitemid;
		_h_ += baggems.hashCode();
		_h_ += shopgems.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buserongheji).append(",");
		_sb_.append(targetgemitemid).append(",");
		_sb_.append(baggems).append(",");
		_sb_.append(shopgems).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

