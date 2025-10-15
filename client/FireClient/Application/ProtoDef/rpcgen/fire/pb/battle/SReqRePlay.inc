	// Protocol

	enum { PROTOCOL_TYPE = 793736 };

	char candoit; // 0нч, 1сп
	std::wstring battlecameraurl;

	SReqRePlay() {
		type = PROTOCOL_TYPE;
		candoit = 0;
	}

	SReqRePlay(char _candoit_, const std::wstring & _battlecameraurl_)
		: candoit(_candoit_), battlecameraurl(_battlecameraurl_) {
		type = PROTOCOL_TYPE;
	}

	explicit SReqRePlay(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << candoit;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(battlecameraurl.length() * 2);
		for(size_t pos =0; pos < battlecameraurl.length(); pos++) {
		utf16 ch = (utf16)battlecameraurl[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> candoit;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		battlecameraurl.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		battlecameraurl.insert(battlecameraurl.end(), (wchar_t)*ch);
		}
		}
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		return _os_;
	}

	aio::Protocol * Clone() const { return new SReqRePlay(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
