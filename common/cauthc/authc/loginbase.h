#ifndef __FIRENET_LOGINBASE_H__
#define __FIRENET_LOGINBASE_H__
#pragma once

//#define TEMP_LOG

namespace LOGINBASE 
{

	namespace helper
	{
#ifdef TEMP_LOG
		class TempLog
		{
			FILE*	mFile;

			TempLog()
				: mFile(NULL)
			{
				fopen_s(&mFile, "temp.log", "w");
			}
			~TempLog()
			{
				if (mFile)
					fclose(mFile);
			}

			void OutOctets(const char* name, const FireNet::Octets& data)
			{
				fprintf(mFile, "%s = \"", name);
				size_t size = data.size();
				const unsigned char *p = (const unsigned char *)data.begin();
				for (size_t i = 0; i < size; i++)
					fprintf(mFile, "%02x ", *p++);
				fprintf(mFile, "\" ");
			}

		public:
			void LinkLogOut(unsigned int mSID, const std::string& UserName, const FireNet::Octets& password, const FireNet::Octets& nonce, const FireNet::Octets& key)
			{
				if (NULL == mFile)
					return;
				time_t	now = time(NULL);
				struct tm ts;
				localtime_s(&ts, &now);
				char time[_MAX_PATH];
				sprintf_s(time, _MAX_PATH, "%d-%d-%d %d:%d:%d ", ts.tm_year + 1900, ts.tm_mon + 1, ts.tm_mday, ts.tm_hour, ts.tm_min, ts.tm_sec);


				fprintf_s(m_file, "%s mSID = %d UserName = %s ", time, mSID, UserName.c_str());
				OutOctets("password", password);
				OutOctets("nonce", nonce);
				OutOctets("key", key);
				fprintf(m_file, "\n");

				fflush(mFile);
			}
			static TempLog& GetInstance()
			{
				static TempLog	log;
				return log;
			}
		};

		void LinkLogOut(unsigned int mSID, const std::string& UserName, const FireNet::Octets& password, const FireNet::Octets& nonce, const FireNet::Octets& key)
		{
			TempLog::GetInstance().LinkLogOut(mSID, UserName, password, nonce, key);
		}

#else
		void LinkLogOut(unsigned int mSID, const std::string& UserName, const FireNet::Octets& password, const FireNet::Octets& nonce, const FireNet::Octets& key) {}
#endif


	} // namespace helper 

	class INetNotify
	{
		friend class CLogin;
	public:
		INetNotify() {}
		virtual ~INetNotify() {}
	protected:
		virtual void NetLoginModule_OnGetUserNameAndPassword(std::string& UserName, std::string& password) = 0;
		virtual void NetLoginModule_OnChallenge(const FireNet::Octets& responce) = 0;
		virtual void NetLoginModule_OnSetOutSecurity(const FireNet::Octets& key) = 0;
		virtual void NetLoginModule_OnSetInSecurity(const FireNet::Octets& key) = 0;
		virtual void NetLoginModule_OnSendKeyExchange(const FireNet::Octets& nonce) = 0;
	};

	class CLogin
	{
		INetNotify*		m_pQuery;
	public:
		CLogin(INetNotify& query)
			: m_pQuery(&query)
		{}
		~CLogin() { }
	public:
		static bool GenerateKeyByPassword(const std::string& identity, const FireNet::Octets& password, const FireNet::Octets& nonce, FireNet::Octets& key)
		{
#ifdef _WIN32
			SECURITY_WIN32::HMAC_MD5Hash		hash;
#else
			SECURITY_LINUX::HMAC_MD5Hash hash;
#endif
			hash.SetParameter((const unsigned char*)password.begin(), password.size());
			hash.UpdateSelf((const unsigned char*)identity.c_str(), identity.length());
			hash.UpdateSelf((const unsigned char*)nonce.begin(), nonce.size());
			memcpy(key.resize(SIZE_MD5).begin(), hash.Final(), SIZE_MD5);

			return true;
		}

		static void RandomUpdate(unsigned char* strData, int nSize)
		{
			srand((unsigned int)time(NULL));
			for (int i = 0; i < nSize; i++)
				strData[i] = (unsigned char)(rand() & 0xFF);
		}

		static FireNet::Octets& MakePassword(const std::string& strUsername, const std::string& strPassword, FireNet::Octets& key)
		{
#ifdef _WIN32
			SECURITY_WIN32::MD5Hash md5;
#else
			SECURITY_LINUX::MD5Hash md5;
#endif
			md5.UpdateSelf((const unsigned char*)strUsername.c_str(), strUsername.length());
			md5.UpdateSelf((const unsigned char*)strPassword.c_str(), strPassword.length());
			memcpy(key.resize(SIZE_MD5).begin(), md5.Final(), SIZE_MD5);
			return key;
		}

	public:
		bool ProcessChallenge(const FireNet::Octets& nonce)
		{
			if (NULL == m_pQuery)
				return false;
			if (SIZE_CHALLENGE != nonce.size())
				return false;

			std::string	strUsername, strPassword;
			m_pQuery->NetLoginModule_OnGetUserNameAndPassword(strUsername, strPassword);

			FireNet::Octets		responce;
			FireNet::Octets		password;
#ifdef _WIN32
			SECURITY_WIN32::HMAC_MD5Hash hmac;
#else
			SECURITY_LINUX::HMAC_MD5Hash hmac;
#endif

			MakePassword(strUsername, strPassword, password);
			hmac.SetParameter((const unsigned char*)password.begin(), password.size());
			hmac.UpdateSelf((const unsigned char*)nonce.begin(), nonce.size());
			memcpy(responce.resize(SIZE_MD5).begin(), hmac.Final(), SIZE_MD5);

			m_pQuery->NetLoginModule_OnChallenge(responce);
			return true;
		}
		bool ProcessKeyExchange(const FireNet::Octets& nonce, unsigned int mSID)
		{
			if (NULL == m_pQuery)
				return false;
			std::string			strUsername, strPassword;
			FireNet::Octets		random;
#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)
			std::string strUserNameTemp = "";
			m_pQuery->NetLoginModule_OnGetUserNameAndPassword(strUserNameTemp, strPassword);
			size_t pos = strUserNameTemp.find_first_of(",");
			if (pos != std::wstring::npos)
			{
				strUsername = strUserNameTemp.substr(0, pos);
			}
#else
			m_pQuery->NetLoginModule_OnGetUserNameAndPassword(strUsername, strPassword);
#endif

			FireNet::Octets		password;
			FireNet::Octets		key;

#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)

#else
#define PASSSESSION "ewogICJhdmF0YXIiIDogIiIsCiAgImNoYW5uZWwiIDogImxvY29qb3kiLAogICJwbGF0Zm9ybWlkIiA6ICIyMTIwOTQ5MTYiLAogICJuaWNrIiA6ICIiLAogICJsb2dpbnRpbWUiIDogIjIwMTUtMTAtMTIgMDA6MDA6MTkiLAogICJjcGluZm8iIDogIiIsCiAgInNpZ24iIDogIjAzZjE0NGY4NGUzMWIzNGUyMWNhZTg3NzkxYTEwZmE1IiwKICAic2V4IiA6ICIiLAogICJhcHBpZCIgOiAiMTAwMTIiLAogICJuYW1lIiA6ICIiCn0="
            //strPassword = PASSSESSION;
#endif

			MakePassword(strUsername, strPassword, password);
			GenerateKeyByPassword(strUsername, password, nonce, key);
			m_pQuery->NetLoginModule_OnSetOutSecurity(key);

			helper::LinkLogOut(mSID, strUsername, password, nonce, key);

			RandomUpdate((unsigned char*)random.resize(SIZE_CHALLENGE).begin(), SIZE_CHALLENGE);
			GenerateKeyByPassword(strUsername, password, random, key);

			helper::LinkLogOut(mSID, strUsername, password, random, key);

			m_pQuery->NetLoginModule_OnSetInSecurity(key);

			m_pQuery->NetLoginModule_OnSendKeyExchange(random);
			return true;
		}
	};

} // namespace LOGIN {

#endif