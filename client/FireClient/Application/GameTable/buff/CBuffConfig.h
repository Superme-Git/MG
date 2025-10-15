#ifndef __CBuffConfig__h__
#define __CBuffConfig__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace buff
    {
        class CBuffConfig
        {
            public:
            int id;
            std::wstring name;
            int inbattle;
            std::wstring strshowname;
            int shapeid;
            std::wstring discribe;
            std::wstring effect;
            int effectsort;
            std::wstring wordeffect;
            int wordsort;
            int cleartype;
             std::vector<int> specialshow;
        };
        class CBuffConfigTable  :public  TableBase 
        {
        public:
          CBuffConfigTable(){ mErrorData.id=-1; }
          virtual ~CBuffConfigTable()
          {
              std::map<int,CBuffConfig*>::iterator it=mDatas.begin();
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
              std::map<int,CBuffConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CBuffConfig& getRecorder(int id)
            {
                std::map<int,CBuffConfig*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            const CBuffConfig& getRecorderByName(const std::wstring& name)
            {
                std::map<const std::wstring,CBuffConfig*>::iterator it=mNames.find(name);
                if(it!=mNames.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CBuffConfig*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/buff.cbuffconfig.bin"; }
        private:
            std::map<int,CBuffConfig*> mDatas;
            std::map<const std::wstring,CBuffConfig*> mNames;
            CBuffConfig mErrorData;
        };
        CBuffConfigTable&  GetCBuffConfigTableInstance();
    }
}
#endif // __CBuffConfig__h__
