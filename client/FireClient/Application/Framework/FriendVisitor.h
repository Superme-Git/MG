#pragma once

struct stChatRecordUnit
{
	int64_t roleid;
	std::wstring name;
	int school;
	int isOnline;
	int friendLevel;
	int rolelevel;
	std::wstring time;
	std::wstring chatContent;
	int shape;
	int camp;
	short relation;
	int64_t factionID;
	std::wstring factionName;
	std::wstring voiceUUID;
	int voiceTime;
	std::wstring messageID;
};

/************************************************************************/
// 好友的访问
/************************************************************************/
class FriendVisitor
{
public:
	// 获取聊天记录的根目录
	static std::wstring sGetPrivyDir();
	// 获取聊天记录的角色目录
	static std::wstring sGetRoleDir(int64_t nSelfRoleID);

	static void loadChatRecord(int64_t nSelfRoleID, const std::wstring& strSelfName, int64_t roleID, std::vector<stChatRecordUnit>& records);

	static void loadRecentRoleList();

	static void writeRecentRoleList();

	static void SaveChatRecord(int64_t nSelfRoleID, std::wstring selfRoleName, int64_t contactRoleID);
};
