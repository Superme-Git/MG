
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __LuaBeanImport__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class LuaBeanImport extends __LuaBeanImport__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786517;

	public int getType() {
		return 786517;
	}

	public fire.pb.item.BagTypes b1;
	public fire.pb.circletask.ActiveQuestData b4;
	public fire.pb.DataInit b8;
	public fire.pb.talk.TipsMsgType b68;
	public fire.pb.pet.PetTypeEnum b10;
	public fire.pb.item.EquipItemType b152;
	public fire.pb.attr.EffectType b28;
	public fire.pb.attr.AttrType b27;
	public fire.pb.Pet b168;
	public fire.pb.item.IDType b29;
	public fire.pb.npc.NpcServices b18;
	public fire.pb.Petskill b169;
	public fire.pb.circletask.SpecialQuestType b55;
	public fire.pb.circletask.SpecialQuestID b58;
	public fire.pb.circletask.CircTaskClass b191;
	public fire.pb.shop.ShopBuyType b192;
	public fire.pb.game.MoneyType b199;
	public fire.pb.attr.RoleCurrency b300;
	public fire.pb.skill.AssistSkill b208;
	public fire.pb.skill.EquipSkill b209;
	public fire.pb.Item b211;
	public fire.pb.RoleInfo b215;
	public fire.pb.move.RoleBasic b100;
	public fire.pb.move.NpcBasic b101;
	public fire.pb.talk.FunModelType b216;

	public LuaBeanImport() {
		b1 = new fire.pb.item.BagTypes();
		b4 = new fire.pb.circletask.ActiveQuestData();
		b8 = new fire.pb.DataInit();
		b68 = new fire.pb.talk.TipsMsgType();
		b10 = new fire.pb.pet.PetTypeEnum();
		b152 = new fire.pb.item.EquipItemType();
		b28 = new fire.pb.attr.EffectType();
		b27 = new fire.pb.attr.AttrType();
		b168 = new fire.pb.Pet();
		b29 = new fire.pb.item.IDType();
		b18 = new fire.pb.npc.NpcServices();
		b169 = new fire.pb.Petskill();
		b55 = new fire.pb.circletask.SpecialQuestType();
		b58 = new fire.pb.circletask.SpecialQuestID();
		b191 = new fire.pb.circletask.CircTaskClass();
		b192 = new fire.pb.shop.ShopBuyType();
		b199 = new fire.pb.game.MoneyType();
		b300 = new fire.pb.attr.RoleCurrency();
		b208 = new fire.pb.skill.AssistSkill();
		b209 = new fire.pb.skill.EquipSkill();
		b211 = new fire.pb.Item();
		b215 = new fire.pb.RoleInfo();
		b100 = new fire.pb.move.RoleBasic();
		b101 = new fire.pb.move.NpcBasic();
		b216 = new fire.pb.talk.FunModelType();
	}

	public LuaBeanImport(fire.pb.item.BagTypes _b1_, fire.pb.circletask.ActiveQuestData _b4_, fire.pb.DataInit _b8_, fire.pb.talk.TipsMsgType _b68_, fire.pb.pet.PetTypeEnum _b10_, fire.pb.item.EquipItemType _b152_, fire.pb.attr.EffectType _b28_, fire.pb.attr.AttrType _b27_, fire.pb.Pet _b168_, fire.pb.item.IDType _b29_, fire.pb.npc.NpcServices _b18_, fire.pb.Petskill _b169_, fire.pb.circletask.SpecialQuestType _b55_, fire.pb.circletask.SpecialQuestID _b58_, fire.pb.circletask.CircTaskClass _b191_, fire.pb.shop.ShopBuyType _b192_, fire.pb.game.MoneyType _b199_, fire.pb.attr.RoleCurrency _b300_, fire.pb.skill.AssistSkill _b208_, fire.pb.skill.EquipSkill _b209_, fire.pb.Item _b211_, fire.pb.RoleInfo _b215_, fire.pb.move.RoleBasic _b100_, fire.pb.move.NpcBasic _b101_, fire.pb.talk.FunModelType _b216_) {
		this.b1 = _b1_;
		this.b4 = _b4_;
		this.b8 = _b8_;
		this.b68 = _b68_;
		this.b10 = _b10_;
		this.b152 = _b152_;
		this.b28 = _b28_;
		this.b27 = _b27_;
		this.b168 = _b168_;
		this.b29 = _b29_;
		this.b18 = _b18_;
		this.b169 = _b169_;
		this.b55 = _b55_;
		this.b58 = _b58_;
		this.b191 = _b191_;
		this.b192 = _b192_;
		this.b199 = _b199_;
		this.b300 = _b300_;
		this.b208 = _b208_;
		this.b209 = _b209_;
		this.b211 = _b211_;
		this.b215 = _b215_;
		this.b100 = _b100_;
		this.b101 = _b101_;
		this.b216 = _b216_;
	}

	public final boolean _validator_() {
		if (!b1._validator_()) return false;
		if (!b4._validator_()) return false;
		if (!b8._validator_()) return false;
		if (!b68._validator_()) return false;
		if (!b10._validator_()) return false;
		if (!b152._validator_()) return false;
		if (!b28._validator_()) return false;
		if (!b27._validator_()) return false;
		if (!b168._validator_()) return false;
		if (!b29._validator_()) return false;
		if (!b18._validator_()) return false;
		if (!b169._validator_()) return false;
		if (!b55._validator_()) return false;
		if (!b58._validator_()) return false;
		if (!b191._validator_()) return false;
		if (!b192._validator_()) return false;
		if (!b199._validator_()) return false;
		if (!b300._validator_()) return false;
		if (!b208._validator_()) return false;
		if (!b209._validator_()) return false;
		if (!b211._validator_()) return false;
		if (!b215._validator_()) return false;
		if (!b100._validator_()) return false;
		if (!b101._validator_()) return false;
		if (!b216._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(b1);
		_os_.marshal(b4);
		_os_.marshal(b8);
		_os_.marshal(b68);
		_os_.marshal(b10);
		_os_.marshal(b152);
		_os_.marshal(b28);
		_os_.marshal(b27);
		_os_.marshal(b168);
		_os_.marshal(b29);
		_os_.marshal(b18);
		_os_.marshal(b169);
		_os_.marshal(b55);
		_os_.marshal(b58);
		_os_.marshal(b191);
		_os_.marshal(b192);
		_os_.marshal(b199);
		_os_.marshal(b300);
		_os_.marshal(b208);
		_os_.marshal(b209);
		_os_.marshal(b211);
		_os_.marshal(b215);
		_os_.marshal(b100);
		_os_.marshal(b101);
		_os_.marshal(b216);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		b1.unmarshal(_os_);
		b4.unmarshal(_os_);
		b8.unmarshal(_os_);
		b68.unmarshal(_os_);
		b10.unmarshal(_os_);
		b152.unmarshal(_os_);
		b28.unmarshal(_os_);
		b27.unmarshal(_os_);
		b168.unmarshal(_os_);
		b29.unmarshal(_os_);
		b18.unmarshal(_os_);
		b169.unmarshal(_os_);
		b55.unmarshal(_os_);
		b58.unmarshal(_os_);
		b191.unmarshal(_os_);
		b192.unmarshal(_os_);
		b199.unmarshal(_os_);
		b300.unmarshal(_os_);
		b208.unmarshal(_os_);
		b209.unmarshal(_os_);
		b211.unmarshal(_os_);
		b215.unmarshal(_os_);
		b100.unmarshal(_os_);
		b101.unmarshal(_os_);
		b216.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LuaBeanImport) {
			LuaBeanImport _o_ = (LuaBeanImport)_o1_;
			if (!b1.equals(_o_.b1)) return false;
			if (!b4.equals(_o_.b4)) return false;
			if (!b8.equals(_o_.b8)) return false;
			if (!b68.equals(_o_.b68)) return false;
			if (!b10.equals(_o_.b10)) return false;
			if (!b152.equals(_o_.b152)) return false;
			if (!b28.equals(_o_.b28)) return false;
			if (!b27.equals(_o_.b27)) return false;
			if (!b168.equals(_o_.b168)) return false;
			if (!b29.equals(_o_.b29)) return false;
			if (!b18.equals(_o_.b18)) return false;
			if (!b169.equals(_o_.b169)) return false;
			if (!b55.equals(_o_.b55)) return false;
			if (!b58.equals(_o_.b58)) return false;
			if (!b191.equals(_o_.b191)) return false;
			if (!b192.equals(_o_.b192)) return false;
			if (!b199.equals(_o_.b199)) return false;
			if (!b300.equals(_o_.b300)) return false;
			if (!b208.equals(_o_.b208)) return false;
			if (!b209.equals(_o_.b209)) return false;
			if (!b211.equals(_o_.b211)) return false;
			if (!b215.equals(_o_.b215)) return false;
			if (!b100.equals(_o_.b100)) return false;
			if (!b101.equals(_o_.b101)) return false;
			if (!b216.equals(_o_.b216)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += b1.hashCode();
		_h_ += b4.hashCode();
		_h_ += b8.hashCode();
		_h_ += b68.hashCode();
		_h_ += b10.hashCode();
		_h_ += b152.hashCode();
		_h_ += b28.hashCode();
		_h_ += b27.hashCode();
		_h_ += b168.hashCode();
		_h_ += b29.hashCode();
		_h_ += b18.hashCode();
		_h_ += b169.hashCode();
		_h_ += b55.hashCode();
		_h_ += b58.hashCode();
		_h_ += b191.hashCode();
		_h_ += b192.hashCode();
		_h_ += b199.hashCode();
		_h_ += b300.hashCode();
		_h_ += b208.hashCode();
		_h_ += b209.hashCode();
		_h_ += b211.hashCode();
		_h_ += b215.hashCode();
		_h_ += b100.hashCode();
		_h_ += b101.hashCode();
		_h_ += b216.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(b1).append(",");
		_sb_.append(b4).append(",");
		_sb_.append(b8).append(",");
		_sb_.append(b68).append(",");
		_sb_.append(b10).append(",");
		_sb_.append(b152).append(",");
		_sb_.append(b28).append(",");
		_sb_.append(b27).append(",");
		_sb_.append(b168).append(",");
		_sb_.append(b29).append(",");
		_sb_.append(b18).append(",");
		_sb_.append(b169).append(",");
		_sb_.append(b55).append(",");
		_sb_.append(b58).append(",");
		_sb_.append(b191).append(",");
		_sb_.append(b192).append(",");
		_sb_.append(b199).append(",");
		_sb_.append(b300).append(",");
		_sb_.append(b208).append(",");
		_sb_.append(b209).append(",");
		_sb_.append(b211).append(",");
		_sb_.append(b215).append(",");
		_sb_.append(b100).append(",");
		_sb_.append(b101).append(",");
		_sb_.append(b216).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

