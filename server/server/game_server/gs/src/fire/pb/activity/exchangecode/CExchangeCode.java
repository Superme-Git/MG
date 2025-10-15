
package fire.pb.activity.exchangecode;

//import fire.pb.npc.NpcServiceManager;
//import fire.pb.npc.NpcServices;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CExchangeCode__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CExchangeCode extends __CExchangeCode__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		final int userid = ((gnet.link.Dispatch) this.getContext()).userid;
		if (roleid < 0)
			return;
            
		//if (!NpcServiceManager.getInstance().hasServiceByNpcKey(roleid, npckey, NpcServices.EXCHANGE_CODE))
		//	return;
        
        new PExchangeCode(roleid, userid, exchangecode).submit();
	}
	
	

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819198;

	public int getType() {
		return 819198;
	}

	public java.lang.String exchangecode; // 兑换码
	public long npckey; // 兑换码npc的npckey

	public CExchangeCode() {
		exchangecode = "";
	}

	public CExchangeCode(java.lang.String _exchangecode_, long _npckey_) {
		this.exchangecode = _exchangecode_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		if (npckey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(exchangecode, "UTF-16LE");
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		exchangecode = _os_.unmarshal_String("UTF-16LE");
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CExchangeCode) {
			CExchangeCode _o_ = (CExchangeCode)_o1_;
			if (!exchangecode.equals(_o_.exchangecode)) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += exchangecode.hashCode();
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(exchangecode.length()).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

