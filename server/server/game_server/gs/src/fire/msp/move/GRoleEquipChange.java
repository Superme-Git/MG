package fire.msp.move;

import java.util.HashSet;
import java.util.Set;

import fire.pb.item.EquipItemType;
import fire.pb.move.SRoleComponentsChange;
import fire.pb.move.SpriteComponents;
import fire.pb.scene.manager.SceneTeamManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRoleEquipChange__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRoleEquipChange extends __GRoleEquipChange__ {
	@Override
	protected void process() {
		// protocol handle
		final byte changetype = getType(pos);
		if (changetype == 0 && ride == -1) // 坐骑没有变化 by changhao
			return;
		Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)
			return;
		if (null == role.getScene())
			return;

		if (changetype != 0) // 装备变化 by changhao
		{
			role.components.put(changetype, itemid);
			if (changetype == SpriteComponents.SPRITE_WEAPON) {
				if (itemid != 0)
					role.components.put((byte) SpriteComponents.SPRITE_WEAPONCOLOR,	itemcolor);
				else
					role.components.remove((byte) SpriteComponents.SPRITE_WEAPONCOLOR);
			}
			// 这里特殊处理一下时装2
			if (changetype == SpriteComponents.ROLE_COLOR1) {
				xbean.Properties prop = xtable.Properties.select(roleid);
				role.components.put((byte) SpriteComponents.ROLE_COLOR2,
						prop.getRolecolor2());
			}
		}

		if (ride != -1) // 坐骑变化 by changhao
		{
			role.components.put((byte) SpriteComponents.SPRITE_HORSEDRESS, ride);
		}
		
		if (effect != -1) {
			role.components.put((byte)SpriteComponents.SPRITE_EQUIP_EFFECT, effect);
		}

		role.marshal();
		if (role.checkVisible()) {
			SRoleComponentsChange send = new SRoleComponentsChange();
			send.roleid = roleid;

			if (changetype != 0) {
				send.components.put(changetype, itemid);
				if (changetype == SpriteComponents.SPRITE_WEAPON) {
					if (itemid != 0)
						send.components.put((byte) SpriteComponents.SPRITE_WEAPONCOLOR,	itemcolor);
					else
						send.components.put((byte) SpriteComponents.SPRITE_WEAPONCOLOR, 0);
				}
				// 这里特殊处理一下时装2
				if (changetype == SpriteComponents.ROLE_COLOR1) {
					xbean.Properties prop = xtable.Properties.select(roleid);
					send.components.put((byte) SpriteComponents.ROLE_COLOR2, prop.getRolecolor2());
				}
				if (effect != -1) {
					send.components.put((byte)SpriteComponents.SPRITE_EQUIP_EFFECT, effect);
				}
			}

			if (ride != -1) {
				send.components.put((byte) SpriteComponents.SPRITE_HORSEDRESS, ride);
			}
			send.spritetype = 0;
			role.sendAround(send);
			// 发送给队员,如果有的话
			fire.pb.scene.movable.SceneTeam team = SceneTeamManager.getInstance().getTeamByID(roleid);
			if (team == null) {
				gnet.link.Onlines.getInstance().send(roleid, send);
				return;
			}
			Set<Long> roleids = new HashSet<Long>();
			for (Role member : team.getMembers()) {
				roleids.add(member.getRoleID());
			}
			
			if (!roleids.isEmpty())
				gnet.link.Onlines.getInstance().send(roleids, send);
		}
	}

	/**
	 * WEAPON		= 0; //武器
		ACCESSORY	= 2; //饰品
		ARMOR 		= 3; //铠甲
		BELT 		= 4; //腰带
		BOOT		= 5; //靴子
		HEADDRESS	= 6; //头饰
	 * @param pos
	 * @return
	 */
	private final byte getType(final int pos) {
		switch (pos) {
		case EquipItemType.ARMS:
			return SpriteComponents.SPRITE_WEAPON;
		case EquipItemType.TIRE:
			return SpriteComponents.SPRITE_HEADDRESS;
		case EquipItemType.LORICAE:
		case EquipItemType.WAISTBAND:
		case EquipItemType.BOOT:
		case EquipItemType.ADORN:
		{
			return SpriteComponents.SPRITE_FASHION;
		}
//		case EquipItemType.EQUIP_EFFECT:
//			return SpriteComponents.SPRITE_EQUIP_EFFECT;
		case EquipItemType.ROLE_COLOR1:
			return SpriteComponents.ROLE_COLOR1;
		case EquipItemType.ROLE_COLOR2:
			return SpriteComponents.ROLE_COLOR2;
		default:
			return 0;
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724929;

	public int getType() {
		return 724929;
	}

	public long roleid;
	public int itemid;
	public int itemcolor;
	public int pos;
	public int ride;
	public int effect;

	public GRoleEquipChange() {
	}

	public GRoleEquipChange(long _roleid_, int _itemid_, int _itemcolor_, int _pos_, int _ride_, int _effect_) {
		this.roleid = _roleid_;
		this.itemid = _itemid_;
		this.itemcolor = _itemcolor_;
		this.pos = _pos_;
		this.ride = _ride_;
		this.effect = _effect_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(itemid);
		_os_.marshal(itemcolor);
		_os_.marshal(pos);
		_os_.marshal(ride);
		_os_.marshal(effect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		itemid = _os_.unmarshal_int();
		itemcolor = _os_.unmarshal_int();
		pos = _os_.unmarshal_int();
		ride = _os_.unmarshal_int();
		effect = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRoleEquipChange) {
			GRoleEquipChange _o_ = (GRoleEquipChange)_o1_;
			if (roleid != _o_.roleid) return false;
			if (itemid != _o_.itemid) return false;
			if (itemcolor != _o_.itemcolor) return false;
			if (pos != _o_.pos) return false;
			if (ride != _o_.ride) return false;
			if (effect != _o_.effect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += itemid;
		_h_ += itemcolor;
		_h_ += pos;
		_h_ += ride;
		_h_ += effect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(itemcolor).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(ride).append(",");
		_sb_.append(effect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GRoleEquipChange _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = itemcolor - _o_.itemcolor;
		if (0 != _c_) return _c_;
		_c_ = pos - _o_.pos;
		if (0 != _c_) return _c_;
		_c_ = ride - _o_.ride;
		if (0 != _c_) return _c_;
		_c_ = effect - _o_.effect;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
