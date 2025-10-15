	// Protocol

	enum { PROTOCOL_TYPE = 793435 };

	int time; // 回合可操作的时间，单位为秒，默认为30
	int environment; // 战场环境，参考BattleEnviroment
	std::map<int, int> aiactions; // 回合读秒前需要播放的AIAction key=fighter id; value=aiaction id;

	SSendRoundStart() {
		type = PROTOCOL_TYPE;
		time = 0;
		environment = 0;
	}

	SSendRoundStart(int _time_, int _environment_, const std::map<int, int> & _aiactions_)
		: time(_time_), environment(_environment_), aiactions(_aiactions_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSendRoundStart(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << time;
		_os_ << environment;
		_os_ << FireNet::MarshalContainer(aiactions);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> time;
		_os_ >> environment;
		_os_ >> FireNet::MarshalContainer(aiactions);
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new SSendRoundStart(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
