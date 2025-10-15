
#pragma once


namespace fire { 
namespace pb { 

class UserInfoUnit : public FireNet::Marshal
{
public:
	std::wstring key;
	std::wstring value;

	UserInfoUnit() {
	}

	UserInfoUnit(const std::wstring & _key_, const std::wstring & _value_)
		: key(_key_), value(_value_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(key.length() * 2);
		for(size_t pos =0; pos < key.length(); pos++) {
		utf16 ch = (utf16)key[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(value.length() * 2);
		for(size_t pos =0; pos < value.length(); pos++) {
		utf16 ch = (utf16)value[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		key.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		key.insert(key.end(), (wchar_t)*ch);
		}
		}
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		value.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		value.insert(value.end(), (wchar_t)*ch);
		}
		}
		return _os_;
	}

};

} 
} 
