#ifndef __CSkillInfo__h__
#define __CSkillInfo__h__
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
        class CSkillInfo
        {
            public:
            int id;
            std::wstring stagelist;
            std::wstring stagelist2;
        };
        class CSkillInfoTable  :public  TableBase 
        {
        public:
          CSkillInfoTable(){ mErrorData.id=-1; }
          virtual ~CSkillInfoTable()
          {
              std::map<int,CSkillInfo*>::iterator it=mDatas.begin();
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
              std::map<int,CSkillInfo*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CSkillInfo& getRecorder(int id)
            {
                std::map<int,CSkillInfo*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CSkillInfo*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/battle.cskillinfo.bin"; }
        private:
            std::map<int,CSkillInfo*> mDatas;
            CSkillInfo mErrorData;
        };
        CSkillInfoTable&  GetCSkillInfoTableInstance();
    }
}
#endif // __CSkillInfo__h__
