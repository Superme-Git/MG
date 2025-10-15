	// Protocol

	enum { PROTOCOL_TYPE = 793433 };

	int enemyside;
	int friendsformation; // 己方的光环
	int enemyformation; // 敌人的光环
	int friendsformationlevel; // 己方的光环
	int enemyformationlevel; // 敌人的光环
	int battleconfigid; // 战斗配置ID，默认为-1，即不是通过战斗配置来生成的战斗
	int battletype; // 枚举值见BattleType
	int roundnum; // 回合数，默认是0
	char background; // 战斗底图id
	char backmusic; // 战斗背景音乐id

	SSendBattleStart() {
		type = PROTOCOL_TYPE;
		enemyside = 0;
		friendsformation = 0;
		enemyformation = 0;
		friendsformationlevel = 0;
		enemyformationlevel = 0;
		battleconfigid = 0;
		battletype = 0;
		roundnum = 0;
		background = 0;
		backmusic = 0;
	}

	SSendBattleStart(int _enemyside_, int _friendsformation_, int _enemyformation_, int _friendsformationlevel_, int _enemyformationlevel_, int _battleconfigid_, int _battletype_, int _roundnum_, char _background_, char _backmusic_)
		: enemyside(_enemyside_), friendsformation(_friendsformation_), enemyformation(_enemyformation_), friendsformationlevel(_friendsformationlevel_), enemyformationlevel(_enemyformationlevel_), battleconfigid(_battleconfigid_), battletype(_battletype_), roundnum(_roundnum_), background(_background_), backmusic(_backmusic_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSendBattleStart(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << enemyside;
		_os_ << friendsformation;
		_os_ << enemyformation;
		_os_ << friendsformationlevel;
		_os_ << enemyformationlevel;
		_os_ << battleconfigid;
		_os_ << battletype;
		_os_ << roundnum;
		_os_ << background;
		_os_ << backmusic;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> enemyside;
		_os_ >> friendsformation;
		_os_ >> enemyformation;
		_os_ >> friendsformationlevel;
		_os_ >> enemyformationlevel;
		_os_ >> battleconfigid;
		_os_ >> battletype;
		_os_ >> roundnum;
		_os_ >> background;
		_os_ >> backmusic;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const SSendBattleStart &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SSendBattleStart &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = enemyside - _o_.enemyside;
		if (0 != _c_) return _c_;
		_c_ = friendsformation - _o_.friendsformation;
		if (0 != _c_) return _c_;
		_c_ = enemyformation - _o_.enemyformation;
		if (0 != _c_) return _c_;
		_c_ = friendsformationlevel - _o_.friendsformationlevel;
		if (0 != _c_) return _c_;
		_c_ = enemyformationlevel - _o_.enemyformationlevel;
		if (0 != _c_) return _c_;
		_c_ = battleconfigid - _o_.battleconfigid;
		if (0 != _c_) return _c_;
		_c_ = battletype - _o_.battletype;
		if (0 != _c_) return _c_;
		_c_ = roundnum - _o_.roundnum;
		if (0 != _c_) return _c_;
		_c_ = background - _o_.background;
		if (0 != _c_) return _c_;
		_c_ = backmusic - _o_.backmusic;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new SSendBattleStart(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
