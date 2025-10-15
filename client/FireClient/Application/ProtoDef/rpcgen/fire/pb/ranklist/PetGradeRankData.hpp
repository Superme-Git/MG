
#pragma once


namespace fire { 
namespace pb { 
namespace ranklist { 

class PetGradeRankData : public FireNet::Marshal
{
public:
	int64_t roleid; // roleid
	int64_t uniquepetid; // 宠物的唯一id
	std::wstring nickname; // 名字
	std::wstring petname; // 宠物名字
	int petgrade; // 宠物评分
	int rank; // 排名
	int colour; // 宠物颜色

	PetGradeRankData() {
		roleid = 0;
		uniquepetid = 0;
		petgrade = 0;
		rank = 0;
		colour = 0;
	}

	PetGradeRankData(int64_t _roleid_, int64_t _uniquepetid_, const std::wstring & _nickname_, const std::wstring & _petname_, int _petgrade_, int _rank_, int _colour_)
		: roleid(_roleid_), uniquepetid(_uniquepetid_), nickname(_nickname_), petname(_petname_), petgrade(_petgrade_), rank(_rank_), colour(_colour_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << roleid;
		_os_ << uniquepetid;
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
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(petname.length() * 2);
		for(size_t pos =0; pos < petname.length(); pos++) {
		utf16 ch = (utf16)petname[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << petgrade;
		_os_ << rank;
		_os_ << colour;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> roleid;
		_os_ >> uniquepetid;
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
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		petname.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		petname.insert(petname.end(), (wchar_t)*ch);
		}
		}
		_os_ >> petgrade;
		_os_ >> rank;
		_os_ >> colour;
		return _os_;
	}

};

} 
} 
} 
