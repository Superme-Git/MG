
#pragma once


namespace fire { 
namespace pb { 
namespace ranklist { 

class LevelRankData : public FireNet::Marshal
{
public:
	int64_t roleid; // roleid
	std::wstring nickname; // 名字
	int level; // 等级
	int school; // 职业id
	int rank; // 排名

	LevelRankData() {
		roleid = 0;
		level = 0;
		school = 0;
		rank = 0;
	}

	LevelRankData(int64_t _roleid_, const std::wstring & _nickname_, int _level_, int _school_, int _rank_)
		: roleid(_roleid_), nickname(_nickname_), level(_level_), school(_school_), rank(_rank_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(nickname.length() * 2);
		for(size_t pos =0; pos < nickname.length(); pos++) {
		utf16 ch = (utf16)nickname[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << level;
		_os_ << school;
		_os_ << rank;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		nickname.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		nickname.insert(nickname.end(), (wchar_t)*ch);
		}
		}
		_os_ >> level;
		_os_ >> school;
		_os_ >> rank;
		return _os_;
	}

};

} 
} 
} 
