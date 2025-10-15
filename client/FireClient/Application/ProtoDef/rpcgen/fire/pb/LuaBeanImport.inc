	// Protocol

	enum { PROTOCOL_TYPE = 786517 };

	fire::pb::item::BagTypes b1;
	fire::pb::circletask::ActiveQuestData b4;
	fire::pb::DataInit b8;
	fire::pb::talk::TipsMsgType b68;
	fire::pb::pet::PetTypeEnum b10;
	fire::pb::item::EquipItemType b152;
	fire::pb::attr::EffectType b28;
	fire::pb::attr::AttrType b27;
	fire::pb::Pet b168;
	fire::pb::item::IDType b29;
	fire::pb::npc::NpcServices b18;
	fire::pb::Petskill b169;
	fire::pb::circletask::SpecialQuestType b55;
	fire::pb::circletask::SpecialQuestID b58;
	fire::pb::circletask::CircTaskClass b191;
	fire::pb::shop::ShopBuyType b192;
	fire::pb::game::MoneyType b199;
	fire::pb::attr::RoleCurrency b300;
	fire::pb::skill::AssistSkill b208;
	fire::pb::skill::EquipSkill b209;
	fire::pb::Item b211;
	fire::pb::RoleInfo b215;
	fire::pb::move::RoleBasic b100;
	fire::pb::move::NpcBasic b101;
	fire::pb::talk::FunModelType b216;

	LuaBeanImport() {
		type = PROTOCOL_TYPE;
	}

	LuaBeanImport(const fire::pb::item::BagTypes & _b1_, const fire::pb::circletask::ActiveQuestData & _b4_, const fire::pb::DataInit & _b8_, const fire::pb::talk::TipsMsgType & _b68_, const fire::pb::pet::PetTypeEnum & _b10_, const fire::pb::item::EquipItemType & _b152_, const fire::pb::attr::EffectType & _b28_, const fire::pb::attr::AttrType & _b27_, const fire::pb::Pet & _b168_, const fire::pb::item::IDType & _b29_, const fire::pb::npc::NpcServices & _b18_, const fire::pb::Petskill & _b169_, const fire::pb::circletask::SpecialQuestType & _b55_, const fire::pb::circletask::SpecialQuestID & _b58_, const fire::pb::circletask::CircTaskClass & _b191_, const fire::pb::shop::ShopBuyType & _b192_, const fire::pb::game::MoneyType & _b199_, const fire::pb::attr::RoleCurrency & _b300_, const fire::pb::skill::AssistSkill & _b208_, const fire::pb::skill::EquipSkill & _b209_, const fire::pb::Item & _b211_, const fire::pb::RoleInfo & _b215_, const fire::pb::move::RoleBasic & _b100_, const fire::pb::move::NpcBasic & _b101_, const fire::pb::talk::FunModelType & _b216_)
		: b1(_b1_), b4(_b4_), b8(_b8_), b68(_b68_), b10(_b10_), b152(_b152_), b28(_b28_), b27(_b27_), b168(_b168_), b29(_b29_), b18(_b18_), b169(_b169_), b55(_b55_), b58(_b58_), b191(_b191_), b192(_b192_), b199(_b199_), b300(_b300_), b208(_b208_), b209(_b209_), b211(_b211_), b215(_b215_), b100(_b100_), b101(_b101_), b216(_b216_) {
		type = PROTOCOL_TYPE;
	}

	explicit LuaBeanImport(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
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

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << b1;
		_os_ << b4;
		_os_ << b8;
		_os_ << b68;
		_os_ << b10;
		_os_ << b152;
		_os_ << b28;
		_os_ << b27;
		_os_ << b168;
		_os_ << b29;
		_os_ << b18;
		_os_ << b169;
		_os_ << b55;
		_os_ << b58;
		_os_ << b191;
		_os_ << b192;
		_os_ << b199;
		_os_ << b300;
		_os_ << b208;
		_os_ << b209;
		_os_ << b211;
		_os_ << b215;
		_os_ << b100;
		_os_ << b101;
		_os_ << b216;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> b1;
		_os_ >> b4;
		_os_ >> b8;
		_os_ >> b68;
		_os_ >> b10;
		_os_ >> b152;
		_os_ >> b28;
		_os_ >> b27;
		_os_ >> b168;
		_os_ >> b29;
		_os_ >> b18;
		_os_ >> b169;
		_os_ >> b55;
		_os_ >> b58;
		_os_ >> b191;
		_os_ >> b192;
		_os_ >> b199;
		_os_ >> b300;
		_os_ >> b208;
		_os_ >> b209;
		_os_ >> b211;
		_os_ >> b215;
		_os_ >> b100;
		_os_ >> b101;
		_os_ >> b216;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new LuaBeanImport(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 32; }
