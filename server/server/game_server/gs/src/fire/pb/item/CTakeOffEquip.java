
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CTakeOffEquip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CTakeOffEquip extends __CTakeOffEquip__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid( this );
		if (roleId < 0)
			return;
		
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 131451, null);
			return;
		}
		// protocol handle
		new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
				final Equip srcbag = new Equip(roleId, false);
				final ItemMaps dstbag = new Pack(roleId, false);
				
				ItemBase bi = srcbag.TransOut(equipkey, -1, "卸下装备");
				if (bi == null)
					return false;
				ItemBase dstitem;
				java.util.List<Integer> freepos = dstbag.getFreepos();
				if (freepos.size() > 0) {
					posinpack = freepos.get(0);
				} else {
					return false;
				}
				if (posinpack != -1)
					dstitem = dstbag.getItemByPos(posinpack);
				else
					dstitem = null;
				if (dstitem != null) {
					return false;
				}
				if (!dstbag.TransIn(bi, posinpack))
					return false;
				if (bi instanceof EquipItem)
					srcbag.onUnequip((EquipItem) bi);
				
				return true;
			}
			
		}.submit();
	}
	

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787446;

	public int getType() {
		return 787446;
	}

	public int equipkey;
	public int posinpack;

	public CTakeOffEquip() {
	}

	public CTakeOffEquip(int _equipkey_, int _posinpack_) {
		this.equipkey = _equipkey_;
		this.posinpack = _posinpack_;
	}

	public final boolean _validator_() {
		if (equipkey < 1) return false;
		if (posinpack < -1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(equipkey);
		_os_.marshal(posinpack);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		equipkey = _os_.unmarshal_int();
		posinpack = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CTakeOffEquip) {
			CTakeOffEquip _o_ = (CTakeOffEquip)_o1_;
			if (equipkey != _o_.equipkey) return false;
			if (posinpack != _o_.posinpack) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += equipkey;
		_h_ += posinpack;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(equipkey).append(",");
		_sb_.append(posinpack).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CTakeOffEquip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = equipkey - _o_.equipkey;
		if (0 != _c_) return _c_;
		_c_ = posinpack - _o_.posinpack;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

