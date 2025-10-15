	// Protocol

	enum { PROTOCOL_TYPE = 800452 };

	int skillid; // 技能ID
	int aimtype; // 技能使用目标类型 对自己角色使用==1，对自己战斗宠物使用==2，对正常队友角色使用==3
	int64_t aimid; // 技能使用目标ID，为队友角色ID

	CUseSceneSkill() {
		type = PROTOCOL_TYPE;
		skillid = 0;
		aimtype = 0;
		aimid = 0;
	}

	CUseSceneSkill(int _skillid_, int _aimtype_, int64_t _aimid_)
		: skillid(_skillid_), aimtype(_aimtype_), aimid(_aimid_) {
		type = PROTOCOL_TYPE;
	}

	explicit CUseSceneSkill(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << skillid;
		_os_ << aimtype;
		_os_ << aimid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> skillid;
		_os_ >> aimtype;
		_os_ >> aimid;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CUseSceneSkill &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CUseSceneSkill &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skillid - _o_.skillid;
		if (0 != _c_) return _c_;
		_c_ = aimtype - _o_.aimtype;
		if (0 != _c_) return _c_;
		_c_ = FireNet::signum(aimid - _o_.aimid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CUseSceneSkill(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
