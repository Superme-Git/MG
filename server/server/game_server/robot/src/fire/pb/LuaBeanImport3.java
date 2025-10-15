
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __LuaBeanImport3__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class LuaBeanImport3 extends __LuaBeanImport3__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786518;

	public int getType() {
		return 786518;
	}

	public fire.pb.ranklist.LevelRankData b1;
	public fire.pb.ranklist.PetGradeRankData b2;
	public fire.pb.ranklist.RankType b122;
	public fire.pb.ranklist.FactionRankRecord b5;
	public fire.pb.ranklist.RoleZongheRankRecord b6;
	public fire.pb.ranklist.RoleProfessionRankRecord b7;
	public fire.pb.ranklist.PvP5RankData b8;
	public fire.pb.RoleSex b42;
	public fire.pb.skill.AssistSkill b999;
	public fire.pb.ranklist.BingFengRankData b39;
	public fire.pb.ErrorCodes b43;
	public fire.pb.ranklist.FactionRankRecordEx b50;
	public fire.pb.ranklist.FactionRaidRankRecord b51;
	public fire.pb.mission.MissionExeTypes b52;
	public fire.pb.circletask.SpecialQuestState b57;
	public fire.pb.mission.MissionFinTypes b59;
	public fire.pb.SysConfigType b9;
	public fire.pb.mission.NpcExecuteTaskTypes b77;
	public fire.pb.mission.ReadTimeType b109;
	public fire.pb.circletask.RefreshDataType b93;
	public fire.pb.npc.TransmitTypes b89;
	public fire.pb.talk.ChannelType b3;
	public fire.pb.mission.MissionStatus b53;
	public fire.pb.move.RoleBasicOctets b102;
	public fire.pb.move.TeamInfoOctets b128;
	public fire.pb.move.ShowPetOctets b127;
	public fire.pb.FunOpenCloseType b217;
	public fire.pb.pet.PetError b13;
	public fire.pb.shop.ShopBuyType b192;
	public fire.pb.game.MoneyType b199;
	public fire.pb.ranklist.RedPackRankRecord b201;
	public fire.pb.ranklist.FlowerRankRecord b202;
	public fire.pb.ranklist.ClanFightRaceRank b203;
	public fire.pb.ranklist.ClanFightHistroyRank b204;

	public LuaBeanImport3() {
		b1 = new fire.pb.ranklist.LevelRankData();
		b2 = new fire.pb.ranklist.PetGradeRankData();
		b122 = new fire.pb.ranklist.RankType();
		b5 = new fire.pb.ranklist.FactionRankRecord();
		b6 = new fire.pb.ranklist.RoleZongheRankRecord();
		b7 = new fire.pb.ranklist.RoleProfessionRankRecord();
		b8 = new fire.pb.ranklist.PvP5RankData();
		b42 = new fire.pb.RoleSex();
		b999 = new fire.pb.skill.AssistSkill();
		b39 = new fire.pb.ranklist.BingFengRankData();
		b43 = new fire.pb.ErrorCodes();
		b50 = new fire.pb.ranklist.FactionRankRecordEx();
		b51 = new fire.pb.ranklist.FactionRaidRankRecord();
		b52 = new fire.pb.mission.MissionExeTypes();
		b57 = new fire.pb.circletask.SpecialQuestState();
		b59 = new fire.pb.mission.MissionFinTypes();
		b9 = new fire.pb.SysConfigType();
		b77 = new fire.pb.mission.NpcExecuteTaskTypes();
		b109 = new fire.pb.mission.ReadTimeType();
		b93 = new fire.pb.circletask.RefreshDataType();
		b89 = new fire.pb.npc.TransmitTypes();
		b3 = new fire.pb.talk.ChannelType();
		b53 = new fire.pb.mission.MissionStatus();
		b102 = new fire.pb.move.RoleBasicOctets();
		b128 = new fire.pb.move.TeamInfoOctets();
		b127 = new fire.pb.move.ShowPetOctets();
		b217 = new fire.pb.FunOpenCloseType();
		b13 = new fire.pb.pet.PetError();
		b192 = new fire.pb.shop.ShopBuyType();
		b199 = new fire.pb.game.MoneyType();
		b201 = new fire.pb.ranklist.RedPackRankRecord();
		b202 = new fire.pb.ranklist.FlowerRankRecord();
		b203 = new fire.pb.ranklist.ClanFightRaceRank();
		b204 = new fire.pb.ranklist.ClanFightHistroyRank();
	}

	public LuaBeanImport3(fire.pb.ranklist.LevelRankData _b1_, fire.pb.ranklist.PetGradeRankData _b2_, fire.pb.ranklist.RankType _b122_, fire.pb.ranklist.FactionRankRecord _b5_, fire.pb.ranklist.RoleZongheRankRecord _b6_, fire.pb.ranklist.RoleProfessionRankRecord _b7_, fire.pb.ranklist.PvP5RankData _b8_, fire.pb.RoleSex _b42_, fire.pb.skill.AssistSkill _b999_, fire.pb.ranklist.BingFengRankData _b39_, fire.pb.ErrorCodes _b43_, fire.pb.ranklist.FactionRankRecordEx _b50_, fire.pb.ranklist.FactionRaidRankRecord _b51_, fire.pb.mission.MissionExeTypes _b52_, fire.pb.circletask.SpecialQuestState _b57_, fire.pb.mission.MissionFinTypes _b59_, fire.pb.SysConfigType _b9_, fire.pb.mission.NpcExecuteTaskTypes _b77_, fire.pb.mission.ReadTimeType _b109_, fire.pb.circletask.RefreshDataType _b93_, fire.pb.npc.TransmitTypes _b89_, fire.pb.talk.ChannelType _b3_, fire.pb.mission.MissionStatus _b53_, fire.pb.move.RoleBasicOctets _b102_, fire.pb.move.TeamInfoOctets _b128_, fire.pb.move.ShowPetOctets _b127_, fire.pb.FunOpenCloseType _b217_, fire.pb.pet.PetError _b13_, fire.pb.shop.ShopBuyType _b192_, fire.pb.game.MoneyType _b199_, fire.pb.ranklist.RedPackRankRecord _b201_, fire.pb.ranklist.FlowerRankRecord _b202_, fire.pb.ranklist.ClanFightRaceRank _b203_, fire.pb.ranklist.ClanFightHistroyRank _b204_) {
		this.b1 = _b1_;
		this.b2 = _b2_;
		this.b122 = _b122_;
		this.b5 = _b5_;
		this.b6 = _b6_;
		this.b7 = _b7_;
		this.b8 = _b8_;
		this.b42 = _b42_;
		this.b999 = _b999_;
		this.b39 = _b39_;
		this.b43 = _b43_;
		this.b50 = _b50_;
		this.b51 = _b51_;
		this.b52 = _b52_;
		this.b57 = _b57_;
		this.b59 = _b59_;
		this.b9 = _b9_;
		this.b77 = _b77_;
		this.b109 = _b109_;
		this.b93 = _b93_;
		this.b89 = _b89_;
		this.b3 = _b3_;
		this.b53 = _b53_;
		this.b102 = _b102_;
		this.b128 = _b128_;
		this.b127 = _b127_;
		this.b217 = _b217_;
		this.b13 = _b13_;
		this.b192 = _b192_;
		this.b199 = _b199_;
		this.b201 = _b201_;
		this.b202 = _b202_;
		this.b203 = _b203_;
		this.b204 = _b204_;
	}

	public final boolean _validator_() {
		if (!b1._validator_()) return false;
		if (!b2._validator_()) return false;
		if (!b122._validator_()) return false;
		if (!b5._validator_()) return false;
		if (!b6._validator_()) return false;
		if (!b7._validator_()) return false;
		if (!b8._validator_()) return false;
		if (!b42._validator_()) return false;
		if (!b999._validator_()) return false;
		if (!b39._validator_()) return false;
		if (!b43._validator_()) return false;
		if (!b50._validator_()) return false;
		if (!b51._validator_()) return false;
		if (!b52._validator_()) return false;
		if (!b57._validator_()) return false;
		if (!b59._validator_()) return false;
		if (!b9._validator_()) return false;
		if (!b77._validator_()) return false;
		if (!b109._validator_()) return false;
		if (!b93._validator_()) return false;
		if (!b89._validator_()) return false;
		if (!b3._validator_()) return false;
		if (!b53._validator_()) return false;
		if (!b102._validator_()) return false;
		if (!b128._validator_()) return false;
		if (!b127._validator_()) return false;
		if (!b217._validator_()) return false;
		if (!b13._validator_()) return false;
		if (!b192._validator_()) return false;
		if (!b199._validator_()) return false;
		if (!b201._validator_()) return false;
		if (!b202._validator_()) return false;
		if (!b203._validator_()) return false;
		if (!b204._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(b1);
		_os_.marshal(b2);
		_os_.marshal(b122);
		_os_.marshal(b5);
		_os_.marshal(b6);
		_os_.marshal(b7);
		_os_.marshal(b8);
		_os_.marshal(b42);
		_os_.marshal(b999);
		_os_.marshal(b39);
		_os_.marshal(b43);
		_os_.marshal(b50);
		_os_.marshal(b51);
		_os_.marshal(b52);
		_os_.marshal(b57);
		_os_.marshal(b59);
		_os_.marshal(b9);
		_os_.marshal(b77);
		_os_.marshal(b109);
		_os_.marshal(b93);
		_os_.marshal(b89);
		_os_.marshal(b3);
		_os_.marshal(b53);
		_os_.marshal(b102);
		_os_.marshal(b128);
		_os_.marshal(b127);
		_os_.marshal(b217);
		_os_.marshal(b13);
		_os_.marshal(b192);
		_os_.marshal(b199);
		_os_.marshal(b201);
		_os_.marshal(b202);
		_os_.marshal(b203);
		_os_.marshal(b204);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		b1.unmarshal(_os_);
		b2.unmarshal(_os_);
		b122.unmarshal(_os_);
		b5.unmarshal(_os_);
		b6.unmarshal(_os_);
		b7.unmarshal(_os_);
		b8.unmarshal(_os_);
		b42.unmarshal(_os_);
		b999.unmarshal(_os_);
		b39.unmarshal(_os_);
		b43.unmarshal(_os_);
		b50.unmarshal(_os_);
		b51.unmarshal(_os_);
		b52.unmarshal(_os_);
		b57.unmarshal(_os_);
		b59.unmarshal(_os_);
		b9.unmarshal(_os_);
		b77.unmarshal(_os_);
		b109.unmarshal(_os_);
		b93.unmarshal(_os_);
		b89.unmarshal(_os_);
		b3.unmarshal(_os_);
		b53.unmarshal(_os_);
		b102.unmarshal(_os_);
		b128.unmarshal(_os_);
		b127.unmarshal(_os_);
		b217.unmarshal(_os_);
		b13.unmarshal(_os_);
		b192.unmarshal(_os_);
		b199.unmarshal(_os_);
		b201.unmarshal(_os_);
		b202.unmarshal(_os_);
		b203.unmarshal(_os_);
		b204.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LuaBeanImport3) {
			LuaBeanImport3 _o_ = (LuaBeanImport3)_o1_;
			if (!b1.equals(_o_.b1)) return false;
			if (!b2.equals(_o_.b2)) return false;
			if (!b122.equals(_o_.b122)) return false;
			if (!b5.equals(_o_.b5)) return false;
			if (!b6.equals(_o_.b6)) return false;
			if (!b7.equals(_o_.b7)) return false;
			if (!b8.equals(_o_.b8)) return false;
			if (!b42.equals(_o_.b42)) return false;
			if (!b999.equals(_o_.b999)) return false;
			if (!b39.equals(_o_.b39)) return false;
			if (!b43.equals(_o_.b43)) return false;
			if (!b50.equals(_o_.b50)) return false;
			if (!b51.equals(_o_.b51)) return false;
			if (!b52.equals(_o_.b52)) return false;
			if (!b57.equals(_o_.b57)) return false;
			if (!b59.equals(_o_.b59)) return false;
			if (!b9.equals(_o_.b9)) return false;
			if (!b77.equals(_o_.b77)) return false;
			if (!b109.equals(_o_.b109)) return false;
			if (!b93.equals(_o_.b93)) return false;
			if (!b89.equals(_o_.b89)) return false;
			if (!b3.equals(_o_.b3)) return false;
			if (!b53.equals(_o_.b53)) return false;
			if (!b102.equals(_o_.b102)) return false;
			if (!b128.equals(_o_.b128)) return false;
			if (!b127.equals(_o_.b127)) return false;
			if (!b217.equals(_o_.b217)) return false;
			if (!b13.equals(_o_.b13)) return false;
			if (!b192.equals(_o_.b192)) return false;
			if (!b199.equals(_o_.b199)) return false;
			if (!b201.equals(_o_.b201)) return false;
			if (!b202.equals(_o_.b202)) return false;
			if (!b203.equals(_o_.b203)) return false;
			if (!b204.equals(_o_.b204)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += b1.hashCode();
		_h_ += b2.hashCode();
		_h_ += b122.hashCode();
		_h_ += b5.hashCode();
		_h_ += b6.hashCode();
		_h_ += b7.hashCode();
		_h_ += b8.hashCode();
		_h_ += b42.hashCode();
		_h_ += b999.hashCode();
		_h_ += b39.hashCode();
		_h_ += b43.hashCode();
		_h_ += b50.hashCode();
		_h_ += b51.hashCode();
		_h_ += b52.hashCode();
		_h_ += b57.hashCode();
		_h_ += b59.hashCode();
		_h_ += b9.hashCode();
		_h_ += b77.hashCode();
		_h_ += b109.hashCode();
		_h_ += b93.hashCode();
		_h_ += b89.hashCode();
		_h_ += b3.hashCode();
		_h_ += b53.hashCode();
		_h_ += b102.hashCode();
		_h_ += b128.hashCode();
		_h_ += b127.hashCode();
		_h_ += b217.hashCode();
		_h_ += b13.hashCode();
		_h_ += b192.hashCode();
		_h_ += b199.hashCode();
		_h_ += b201.hashCode();
		_h_ += b202.hashCode();
		_h_ += b203.hashCode();
		_h_ += b204.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(b1).append(",");
		_sb_.append(b2).append(",");
		_sb_.append(b122).append(",");
		_sb_.append(b5).append(",");
		_sb_.append(b6).append(",");
		_sb_.append(b7).append(",");
		_sb_.append(b8).append(",");
		_sb_.append(b42).append(",");
		_sb_.append(b999).append(",");
		_sb_.append(b39).append(",");
		_sb_.append(b43).append(",");
		_sb_.append(b50).append(",");
		_sb_.append(b51).append(",");
		_sb_.append(b52).append(",");
		_sb_.append(b57).append(",");
		_sb_.append(b59).append(",");
		_sb_.append(b9).append(",");
		_sb_.append(b77).append(",");
		_sb_.append(b109).append(",");
		_sb_.append(b93).append(",");
		_sb_.append(b89).append(",");
		_sb_.append(b3).append(",");
		_sb_.append(b53).append(",");
		_sb_.append(b102).append(",");
		_sb_.append(b128).append(",");
		_sb_.append(b127).append(",");
		_sb_.append(b217).append(",");
		_sb_.append(b13).append(",");
		_sb_.append(b192).append(",");
		_sb_.append(b199).append(",");
		_sb_.append(b201).append(",");
		_sb_.append(b202).append(",");
		_sb_.append(b203).append(",");
		_sb_.append(b204).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

