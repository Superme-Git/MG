	// Protocol

	enum { PROTOCOL_TYPE = 790439 };

	enum {
		DIR_UP = 0, // 向上
		DIR_RIGHTUP = 1, // 右上
		DIR_RIGHT = 2, // 向右
		DIR_RIGHTDOWN = 3, // 右下
		DIR_DOWN = 4, // 向下
		DIR_LEFTDOWN = 5, // 左下
		DIR_LEFT = 6, // 向左
		DIR_LEFTUP = 7, // 左上
	};

	int direction; // 转向的方向

	CRoleTurn() {
		type = PROTOCOL_TYPE;
		direction = 0;
	}

	explicit CRoleTurn(int _direction_)
		: direction(_direction_) {
		type = PROTOCOL_TYPE;
	}

	explicit CRoleTurn(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << direction;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> direction;
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	bool operator<(const CRoleTurn &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CRoleTurn &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = direction - _o_.direction;
		if (0 != _c_) return _c_;
		return _c_;
	}

	aio::Protocol * Clone() const { return new CRoleTurn(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
