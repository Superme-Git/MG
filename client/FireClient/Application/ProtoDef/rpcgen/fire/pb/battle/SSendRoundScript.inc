	// Protocol

	enum { PROTOCOL_TYPE = 793457 };

	std::list<fire::pb::battle::NewResultItem> playitem;
	std::list<fire::pb::battle::AIOperation> aiactions; // 随战斗脚本播放的客户端AI动作
	std::map<int, float> rolechangedattrs; // 回合结束时主角属性的变化（战斗中属性实时变化）
	std::map<int, float> petchangedattrs; // 回合结束时主角宠物属性的变化
	std::map<int, int> fighterfinallyhps; // 回合结束时战斗者血量的最终值
	std::map<int, int> fighterfinallymps; // 回合结束时战斗者兰量的最终值

	SSendRoundScript() {
		type = PROTOCOL_TYPE;
	}

	SSendRoundScript(const std::list<fire::pb::battle::NewResultItem> & _playitem_, const std::list<fire::pb::battle::AIOperation> & _aiactions_, const std::map<int, float> & _rolechangedattrs_, const std::map<int, float> & _petchangedattrs_, const std::map<int, int> & _fighterfinallyhps_, const std::map<int, int> & _fighterfinallymps_)
		: playitem(_playitem_), aiactions(_aiactions_), rolechangedattrs(_rolechangedattrs_), petchangedattrs(_petchangedattrs_), fighterfinallyhps(_fighterfinallyhps_), fighterfinallymps(_fighterfinallymps_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSendRoundScript(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::list<fire::pb::battle::NewResultItem>::const_iterator _i_ = playitem.begin(); _i_ != playitem.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		for (std::list<fire::pb::battle::AIOperation>::const_iterator _i_ = aiactions.begin(); _i_ != aiactions.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << FireNet::MarshalContainer(playitem);
		_os_ << FireNet::MarshalContainer(aiactions);
		_os_ << FireNet::MarshalContainer(rolechangedattrs);
		_os_ << FireNet::MarshalContainer(petchangedattrs);
		_os_ << FireNet::MarshalContainer(fighterfinallyhps);
		_os_ << FireNet::MarshalContainer(fighterfinallymps);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> FireNet::MarshalContainer(playitem);
		_os_ >> FireNet::MarshalContainer(aiactions);
		_os_ >> FireNet::MarshalContainer(rolechangedattrs);
		_os_ >> FireNet::MarshalContainer(petchangedattrs);
		_os_ >> FireNet::MarshalContainer(fighterfinallyhps);
		_os_ >> FireNet::MarshalContainer(fighterfinallymps);
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new SSendRoundScript(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
