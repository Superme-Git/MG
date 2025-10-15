#ifndef __CMonsterConfig__h__
#define __CMonsterConfig__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace npc
    {
        class CMonsterConfig
        {
            public:
            int id;
            std::wstring name;
            std::wstring title;
            int modelID;
            int becatch;
            int showhpbar;
            int showhpbarbig;
            int showlevel;
            int npctype;
            double bodytype;
            int area1colour;
            int area2colour;
            int petid;
            int school;
        };
        class CMonsterConfigTable  :public  TableBase 
        {
        public:
          CMonsterConfigTable(){ mErrorData.id=-1; }
          virtual ~CMonsterConfigTable()
          {
              std::map<int,CMonsterConfig*>::iterator it=mDatas.begin();
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
              std::map<int,CMonsterConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CMonsterConfig& getRecorder(int id)
            {
                std::map<int,CMonsterConfig*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            const CMonsterConfig& getRecorderByName(const std::wstring& name)
            {
                std::map<const std::wstring,CMonsterConfig*>::iterator it=mNames.find(name);
                if(it!=mNames.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CMonsterConfig*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/npc.cmonsterconfig.bin"; }
        private:
            std::map<int,CMonsterConfig*> mDatas;
            std::map<const std::wstring,CMonsterConfig*> mNames;
            CMonsterConfig mErrorData;
        };
        CMonsterConfigTable&  GetCMonsterConfigTableInstance();
    }
}
#endif // __CMonsterConfig__h__
