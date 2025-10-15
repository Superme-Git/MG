#ifndef __CSceneNPCBase__h__
#define __CSceneNPCBase__h__
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
        class CSceneNPCBase
        {
            public:
            int id;
            int shapeid;
            std::wstring name;
            int type;
            std::wstring npcappear;
            std::wstring npcdisappear;
            int scale;
            int area1colour;
             std::vector<int> chatidlist;
            int mask;
            int transparent;
        };
        class CSceneNPCBaseTable  :public  TableBase 
        {
        public:
          CSceneNPCBaseTable(){ mErrorData.id=-1; }
          virtual ~CSceneNPCBaseTable()
          {
              std::map<int,CSceneNPCBase*>::iterator it=mDatas.begin();
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
              std::map<int,CSceneNPCBase*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CSceneNPCBase& getRecorder(int id)
            {
                std::map<int,CSceneNPCBase*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            const CSceneNPCBase& getRecorderByName(const std::wstring& name)
            {
                std::map<const std::wstring,CSceneNPCBase*>::iterator it=mNames.find(name);
                if(it!=mNames.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CSceneNPCBase*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/npc.cscenenpcbase.bin"; }
        private:
            std::map<int,CSceneNPCBase*> mDatas;
            std::map<const std::wstring,CSceneNPCBase*> mNames;
            CSceneNPCBase mErrorData;
        };
        CSceneNPCBaseTable&  GetCSceneNPCBaseTableInstance();
    }
}
#endif // __CSceneNPCBase__h__
