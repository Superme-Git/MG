
#pragma once

#include "fire/pb/clan/ClanDMapInfo.hpp"
#include "fire/pb/clan/ClanMember.hpp"
#include "fire/pb/clan/ClanSkill.hpp"
#include <map>

namespace fire { 
namespace pb { 
namespace clan { 

class ClanInfo : public FireNet::Marshal
{
public:
	std::wstring clanname; // 公会名称
	int64_t clanid; // 公会id
	int64_t clankey; // 公会key
	int clanlevel; // 公会等级
	int membersnum; // 成员人数
	std::wstring clanmaster; // 帮主
	std::wstring clanvicemaster; // 副帮主
	std::wstring clancreator; // 创始人
	std::wstring clanrank; // 公会排名
	std::wstring clanaim; // 公会宗旨
	std::map<int64_t, fire::pb::clan::ClanMember> memberlist;
	std::map<int, fire::pb::clan::ClanSkill> clanskilllist; // 公会技能列表
	std::map<int64_t, fire::pb::clan::ClanDMapInfo> clandsceneids; // 公会所有内景副本地图场景id

	ClanInfo() {
		clanid = 0;
		clankey = 0;
		clanlevel = 0;
		membersnum = 0;
	}

	ClanInfo(const std::wstring & _clanname_, int64_t _clanid_, int64_t _clankey_, int _clanlevel_, int _membersnum_, const std::wstring & _clanmaster_, const std::wstring & _clanvicemaster_, const std::wstring & _clancreator_, const std::wstring & _clanrank_, const std::wstring & _clanaim_, const std::map<int64_t, fire::pb::clan::ClanMember> & _memberlist_, const std::map<int, fire::pb::clan::ClanSkill> & _clanskilllist_, const std::map<int64_t, fire::pb::clan::ClanDMapInfo> & _clandsceneids_)
		: clanname(_clanname_), clanid(_clanid_), clankey(_clankey_), clanlevel(_clanlevel_), membersnum(_membersnum_), clanmaster(_clanmaster_), clanvicemaster(_clanvicemaster_), clancreator(_clancreator_), clanrank(_clanrank_), clanaim(_clanaim_), memberlist(_memberlist_), clanskilllist(_clanskilllist_), clandsceneids(_clandsceneids_) {
	}

	bool _validator_() const {
		for (std::map<int64_t, fire::pb::clan::ClanMember>::const_iterator _i_ = memberlist.begin(); _i_ != memberlist.end(); ++_i_) {
			if (!_i_->second._validator_()) return false;
		}
		for (std::map<int, fire::pb::clan::ClanSkill>::const_iterator _i_ = clanskilllist.begin(); _i_ != clanskilllist.end(); ++_i_) {
			if (!_i_->second._validator_()) return false;
		}
		for (std::map<int64_t, fire::pb::clan::ClanDMapInfo>::const_iterator _i_ = clandsceneids.begin(); _i_ != clandsceneids.end(); ++_i_) {
			if (!_i_->second._validator_()) return false;
		}
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(clanname.length() * 2);
		for(size_t pos =0; pos < clanname.length(); pos++) {
		utf16 ch = (utf16)clanname[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << clanid;
		_os_ << clankey;
		_os_ << clanlevel;
		_os_ << membersnum;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(clanmaster.length() * 2);
		for(size_t pos =0; pos < clanmaster.length(); pos++) {
		utf16 ch = (utf16)clanmaster[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(clanvicemaster.length() * 2);
		for(size_t pos =0; pos < clanvicemaster.length(); pos++) {
		utf16 ch = (utf16)clanvicemaster[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(clancreator.length() * 2);
		for(size_t pos =0; pos < clancreator.length(); pos++) {
		utf16 ch = (utf16)clancreator[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(clanrank.length() * 2);
		for(size_t pos =0; pos < clanrank.length(); pos++) {
		utf16 ch = (utf16)clanrank[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(clanaim.length() * 2);
		for(size_t pos =0; pos < clanaim.length(); pos++) {
		utf16 ch = (utf16)clanaim[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << FireNet::MarshalContainer(memberlist);
		_os_ << FireNet::MarshalContainer(clanskilllist);
		_os_ << FireNet::MarshalContainer(clandsceneids);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		clanname.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		clanname.insert(clanname.end(), (wchar_t)*ch);
		}
		}
		_os_ >> clanid;
		_os_ >> clankey;
		_os_ >> clanlevel;
		_os_ >> membersnum;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		clanmaster.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		clanmaster.insert(clanmaster.end(), (wchar_t)*ch);
		}
		}
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		clanvicemaster.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		clanvicemaster.insert(clanvicemaster.end(), (wchar_t)*ch);
		}
		}
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		clancreator.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		clancreator.insert(clancreator.end(), (wchar_t)*ch);
		}
		}
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		clanrank.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		clanrank.insert(clanrank.end(), (wchar_t)*ch);
		}
		}
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		clanaim.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		clanaim.insert(clanaim.end(), (wchar_t)*ch);
		}
		}
		_os_ >> FireNet::MarshalContainer(memberlist);
		_os_ >> FireNet::MarshalContainer(clanskilllist);
		_os_ >> FireNet::MarshalContainer(clandsceneids);
		return _os_;
	}

};

} 
} 
} 
