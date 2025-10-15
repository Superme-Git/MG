	// Protocol

	enum { PROTOCOL_TYPE = 793734 };

	int64_t battleid; // 战斗信息表id
	char ishave; // 0无, 1有
	int sizebeforezip; // 压缩前长度
	int64_t sizeafterzip; // 压缩后长度
	std::wstring battlecameraurl; // 0拒绝  1接受

	SSendCameraUrl() {
		type = PROTOCOL_TYPE;
		battleid = 0;
		ishave = 0;
		sizebeforezip = 0;
		sizeafterzip = 0;
	}

	SSendCameraUrl(int64_t _battleid_, char _ishave_, int _sizebeforezip_, int64_t _sizeafterzip_, const std::wstring & _battlecameraurl_)
		: battleid(_battleid_), ishave(_ishave_), sizebeforezip(_sizebeforezip_), sizeafterzip(_sizeafterzip_), battlecameraurl(_battlecameraurl_) {
		type = PROTOCOL_TYPE;
	}

	explicit SSendCameraUrl(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		if (!_validator_()) {
			throw Marshal::Exception();
		}
		_os_ << battleid;
		_os_ << ishave;
		_os_ << sizebeforezip;
		_os_ << sizeafterzip;
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
		_os_ >> battleid;
		_os_ >> ishave;
		_os_ >> sizebeforezip;
		_os_ >> sizeafterzip;
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

	aio::Protocol * Clone() const { return new SSendCameraUrl(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 65535; }
