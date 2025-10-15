
#pragma once


namespace fire { 
namespace pb { 
namespace title { 

class TitleInfo : public FireNet::Marshal
{
public:
	int titleid;
	std::wstring name;
	int64_t availtime;

	TitleInfo() {
		titleid = 0;
		availtime = 0;
	}

	TitleInfo(int _titleid_, const std::wstring & _name_, int64_t _availtime_)
		: titleid(_titleid_), name(_name_), availtime(_availtime_) {
	}

	bool _validator_() const {
		if (titleid < 0) return false;
		if (availtime < -1) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << titleid;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(name.length() * 2);
		for(size_t pos =0; pos < name.length(); pos++) {
		utf16 ch = (utf16)name[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << availtime;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> titleid;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		name.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		name.insert(name.end(), (wchar_t)*ch);
		}
		}
		_os_ >> availtime;
		return _os_;
	}

};

} 
} 
} 
