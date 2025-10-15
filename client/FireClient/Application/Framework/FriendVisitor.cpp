#include "stdafx.h"
#include "FriendVisitor.h"
#include "utils/Utils.h"
#include "../../dependencies/LJXML//Include/LJXML.hpp"
#include "ConfigManager.h"
#include "MainRoleDataManager.h"

namespace NS_FriendVisitor
{
	static const int s_nSaveRecordMax = 50;	// 聊天记录的存储上限

	// chat record
	static std::wstring strRecord = L"Record";
	static std::wstring strRoleID = L"RoleID";
	static std::wstring strTime = L"Time";
	static std::wstring strContent = L"Content";
	static std::wstring strVoiceUid = L"Uuid";
	static std::wstring strVoiceTime = L"VoiceTime";
	static std::wstring strMessageID = L"MessageID";

	// recent list
	static std::wstring strRecentListFileName = L"recentlist";
	static std::wstring strRecent = L"Recent";
	static std::wstring strRecentRoleID = L"RoleID";
	static std::wstring strHasNotReadMsg = L"HasNotReadMsg";
}

//----------------------------------------------------------------------//
std::wstring FriendVisitor::sGetPrivyDir()
{
	std::wstring strDocDir = MHSD_UTILS::GetDocDir();
	return MHSD_UTILS::CreateDir(strDocDir + L"/" + L"privy");
}

//----------------------------------------------------------------------//
std::wstring FriendVisitor::sGetRoleDir(int64_t nSelfRoleID)
{
	std::wstring strPrivyDir = sGetPrivyDir();
	return MHSD_UTILS::CreateDir(strPrivyDir + L"/" + StringCover::NumTowstring(nSelfRoleID));
}

void FriendVisitor::SaveChatRecord(int64_t selfRoleID, std::wstring selfRoleName, int64_t contactRoleID)
{
	if (contactRoleID == 0)
		return;

	LJXML::LJXML_Tool<LJXML::Char>::list_type listMemory;

	LJXML::LJXML_Doc<LJXML::Char> aDoc;

	int64_t num = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("FriendManager.GetRecordNumByRoleID__", contactRoleID);

	size_t nBeginIndex = 1;
	if (num >= NS_FriendVisitor::s_nSaveRecordMax)
	{
		nBeginIndex = num - NS_FriendVisitor::s_nSaveRecordMax;
	}

	for (int i = nBeginIndex; i <= num; ++i)
	{

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(contactRoleID);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(i);
		std::string rName = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleRecordName__", 2);
		std::wstring recordName = s2ws(rName);

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(contactRoleID);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(i);
		std::string rTime = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleRecordTime__", 2);

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(contactRoleID);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(i);
		std::string rVoiceUuid = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleRecordVoiceUuid__", 2);

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(contactRoleID);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(i);
		std::string rVoiceTime = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleRecordVoiceTime__", 2);

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(contactRoleID);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(i);
		std::string rContent = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleRecordContent__", 2);

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(contactRoleID);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(i);
		std::string rMessageID = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleRecordMessageID__", 2);

		std::wstring chatContent = s2ws(rContent);

		auto pElement = LJXML::LJXML_Tool<LJXML::Char>::CreateElement(&aDoc, NS_FriendVisitor::strRecord);

		// RoleID
		if (recordName != selfRoleName)
		{
			listMemory.push_back(StringCover::NumTowstring(contactRoleID));
		}
		else
		{
			listMemory.push_back(StringCover::NumTowstring(selfRoleID));
		}

		LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strRoleID, listMemory.back());

		// Time
		listMemory.push_back(s2ws(rTime));
		LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strTime, listMemory.back());

		//voice Uid
		if (rVoiceUuid.length() > 0)
		{
			listMemory.push_back(s2ws(rVoiceUuid));
			LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strVoiceUid, listMemory.back());

			listMemory.push_back(StringCover::NumTowstring(s2ws(rVoiceTime)));
			LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strVoiceTime, listMemory.back());
		}

		//messageid
		listMemory.push_back(s2ws(rMessageID));
		LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strMessageID, listMemory.back());

		// Content
		if (gGetGameConfigManager()->GetConfigValue(L"friendchatencrypt") == 1)
		{
			listMemory.push_back(StringCover::WString2Hex(chatContent.c_str(), chatContent.length()));
			LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strContent, listMemory.back());
		}
		else
		{
			listMemory.push_back(chatContent);
			LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pElement, NS_FriendVisitor::strContent, listMemory.back());
		}
	}

	std::wstring strDir = sGetRoleDir(GetMainRoleDataNumValue<int64_t>("roleid"));
	if (!strDir.empty())
	{
		std::wstring strFileName = StringCover::NumTowstring(contactRoleID);
		std::wstring strFullFileName = strDir + L"/" + strFileName;
		aDoc.SaveToFile(StringCover::to_string(strFullFileName));
	}
}

void FriendVisitor::loadChatRecord(int64_t nSelfRoleID, const std::wstring& strSelfName, int64_t roleID, std::vector<stChatRecordUnit>& records)
{
	LJXML::LJXML_Doc<LJXML::Char> aDoc;

	std::wstring strDir = sGetRoleDir(nSelfRoleID);
	std::wstring strFileName = StringCover::NumTowstring(roleID);
	std::wstring strFullFileName = strDir + L"/" + strFileName;
	try
	{
		bool bRet = aDoc.LoadFromFile(StringCover::to_string(strFullFileName));
		if (bRet == false)
			return;
	}
	catch (const rapidxml::parse_error& e)
	{
		std::string tmp(e.what());
		SDLOG_WARN((L"Load chat record fail!! file name:" + strFullFileName).c_str());
		MHSD_UTILS::RemoveFile(strFullFileName);
		return;
	}

	LJXML::LJXML_Node<LJXML::Char>* pFirstNode = (LJXML::LJXML_Node<LJXML::Char>*)aDoc.first_node();
	if (pFirstNode == NULL)
		return;

	LJXML::LJXML_NodeList aNodeList;
	pFirstNode->GetSibNodeList(aNodeList);
	for (size_t i = 0; i < aNodeList.size(); ++i)
	{
		LJXML::LJXML_NodeList aChildList;
		aNodeList[i]->GetSubNodeList(aChildList);

		stChatRecordUnit newChatRecord;
		for (size_t n = 0; n < aChildList.size(); ++n)
		{
			LJXML::LJXML_Node<LJXML::Char>* pChildNode = aChildList[n];
			if (pChildNode->name() == NS_FriendVisitor::strRoleID)
			{
				newChatRecord.roleid = _wtoi64(pChildNode->value());
			}
			else if (pChildNode->name() == NS_FriendVisitor::strTime)
			{
				newChatRecord.time = pChildNode->value();
			}
			else if (pChildNode->name() == NS_FriendVisitor::strVoiceUid)
			{
				newChatRecord.voiceUUID = pChildNode->value();
			}
			else if (pChildNode->name() == NS_FriendVisitor::strVoiceTime)
			{
				newChatRecord.voiceTime = _wtoi64(pChildNode->value());
			}
			else if (pChildNode->name() == NS_FriendVisitor::strMessageID)
			{
				newChatRecord.messageID = pChildNode->value();
			}
			else if (pChildNode->name() == NS_FriendVisitor::strContent)
			{
				newChatRecord.chatContent = pChildNode->value();
				if (newChatRecord.chatContent[0] != L'<')
				{
					newChatRecord.chatContent = StringCover::Hex2WString(newChatRecord.chatContent.c_str(), newChatRecord.chatContent.length());
				}
			}
		}

		// add record
		if (newChatRecord.roleid != 0)
		{
			if (newChatRecord.roleid == nSelfRoleID)
			{
				newChatRecord.name = strSelfName;
				newChatRecord.roleid = roleID;
			}
			const_cast<std::vector<stChatRecordUnit>&>(records).push_back(newChatRecord);
		}
	}
}

void FriendVisitor::writeRecentRoleList()
{
	LJXML::LJXML_Tool<LJXML::Char>::list_type listMemory;

	LJXML::LJXML_Doc<LJXML::Char> aDoc;

	int nSaveCount = 0;
	int nCount = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("FriendManager.GetRecentChatListNum");
	for (int i = 1; i <= nCount; ++i)
	{
		int64_t nRoleID = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("FriendManager.GetRecentChatRoleIDByIdx", i);
		int nNotReadMsgNum = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("FriendManager.GetRoleNotReadMsgNum", nRoleID);
		int nReadMsgNum = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("FriendManager.GetChatRecordNumByRoleID", nRoleID);

		bool notLoad = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("FriendManager.RoleHasNotLoadChatRecord", nRoleID);
		bool isMyFriend = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("FriendManager.isMyFriend__", nRoleID);

		// have chat record or friend
		if ((nNotReadMsgNum + nReadMsgNum > 0) || notLoad || isMyFriend)
		{
			int nCount = 0;

			listMemory.push_back(NS_FriendVisitor::strRecentRoleID);
			++nCount;
			listMemory.push_back(StringCover::NumTowstring(nRoleID));
			++nCount;

			listMemory.push_back(NS_FriendVisitor::strHasNotReadMsg);
			++nCount;
			listMemory.push_back(StringCover::NumTowstring(nNotReadMsgNum > 0 ? 1 : 0));
			++nCount;

			auto itBegin = listMemory.end();
			auto itEnd = listMemory.end();

			while (nCount-- && itBegin != listMemory.begin())
			{
				--itBegin;
			}
			LJXML::LJXML_Tool<LJXML::Char>::CreateElement(&aDoc, NS_FriendVisitor::strRecent, L"", itBegin, itEnd);

			++nSaveCount;
		}
	}

	if (nSaveCount > 0)
	{
		std::wstring strDir = sGetRoleDir(GetMainRoleDataNumValue<int64_t>("roleid"));
		if (!strDir.empty())
		{
			std::wstring strFullFileName = strDir + L"/" + NS_FriendVisitor::strRecentListFileName;
			aDoc.SaveToFile(StringCover::to_string(strFullFileName));
		}
	}
}


void FriendVisitor::loadRecentRoleList()
{
	LJXML::LJXML_Doc<LJXML::Char> aDoc;

	std::wstring strDir = sGetRoleDir(GetMainRoleDataNumValue<int64_t>("roleid"));
	std::wstring strFullFileName = strDir + L"/" + NS_FriendVisitor::strRecentListFileName;
	bool bRet = aDoc.LoadFromFile(StringCover::to_string(strFullFileName));
	if (bRet == false)
		return;

	LJXML::LJXML_Node<LJXML::Char>* pFirstNode = (LJXML::LJXML_Node<LJXML::Char>*)aDoc.first_node();
	if (pFirstNode == NULL)
		return;

	LJXML::LJXML_NodeList aNodeList;
	pFirstNode->GetSibNodeList(aNodeList);
	for (size_t i = 0; i < aNodeList.size(); ++i)
	{
		int64_t nRoleID = 0;
		int nNotReadMsgNum = 0;

		LJXML::LJXML_Node<LJXML::Char>* pNode = aNodeList[i];

		if (pNode == NULL)
			return;

		LJXML::LJXML_AttrList aAttrList;
		pNode->GetSubAttrList(aAttrList);
		for (size_t i = 0; i < aAttrList.size(); ++i)
		{
			LJXML::LJXML_Attr<LJXML::Char>* pAttr = aAttrList[i];
			if (pAttr->name() == NS_FriendVisitor::strRecentRoleID)
			{
				nRoleID = _wtoi64(pAttr->value());
			}
			else if (pAttr->name() == NS_FriendVisitor::strHasNotReadMsg)
			{
				nNotReadMsgNum = _wtoi(pAttr->value());
			}
		}

		if (nRoleID != 0)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(nRoleID);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(false);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("FriendManager.AddLastChat__", 2);
		}
	}
}
