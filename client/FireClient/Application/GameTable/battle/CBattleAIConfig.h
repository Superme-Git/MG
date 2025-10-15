#ifndef __CBattleAIConfig__h__
#define __CBattleAIConfig__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace battle
    {
        class CBattleAIConfig
        {
            public:
            int id;
            std::wstring talkinfo;
            int talkshow;
            std::wstring tipsinfo;
            int appearchange;
            std::wstring playeffect;
            int playsound;
            int changeground;
            int changegroundeffect;
            std::wstring speak;
            int speakshow;
            std::wstring speaktime;
        };
        class CBattleAIConfigTable  :public  TableBase 
        {
        public:
          CBattleAIConfigTable(){ mErrorData.id=-1; }
          virtual ~CBattleAIConfigTable()
          {
              std::map<int,CBattleAIConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
          }
        private:
        std::wstring ReadString(LJFM::LJFMF& file)
        {
            int stringLength;
            file.Read(&stringLength,sizeof(int));
            char* buf=(char*)malloc(stringLength+1);
            file.Read(buf,stringLength);
            buf[stringLength]=0;
            std::wstring r=s2ws(buf);
            free(buf);
            return r;
        }
        public:
            bool SetupData(const std::wstring& pathFileName);
            virtual void ReleaseData()
          {
              std::map<int,CBattleAIConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CBattleAIConfig& getRecorder(int id)
            {
                std::map<int,CBattleAIConfig*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CBattleAIConfig*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/battle.cbattleaiconfig.bin"; }
        private:
            std::map<int,CBattleAIConfig*> mDatas;
            CBattleAIConfig mErrorData;
        };
        CBattleAIConfigTable&  GetCBattleAIConfigTableInstance();
    }
}
#endif // __CBattleAIConfig__h__
