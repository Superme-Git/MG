#ifndef __CParnterNpcConfig__h__
#define __CParnterNpcConfig__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace instance
    {
        class CParnterNpcConfig
        {
            public:
            int id;
            std::wstring name;
            int schoolid;
            int type;
            int modelid;
            int serverid;
            int bufferid;
            int follownpcid;
            std::wstring jiadian;
            std::wstring skillname;
            std::wstring roleintro;
        };
        class CParnterNpcConfigTable  :public  TableBase 
        {
        public:
          CParnterNpcConfigTable(){ mErrorData.id=-1; }
          virtual ~CParnterNpcConfigTable()
          {
              std::map<int,CParnterNpcConfig*>::iterator it=mDatas.begin();
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
              std::map<int,CParnterNpcConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CParnterNpcConfig& getRecorder(int id)
            {
                std::map<int,CParnterNpcConfig*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            const CParnterNpcConfig& getRecorderByName(const std::wstring& name)
            {
                std::map<const std::wstring,CParnterNpcConfig*>::iterator it=mNames.find(name);
                if(it!=mNames.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CParnterNpcConfig*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/instance.cparnternpcconfig.bin"; }
        private:
            std::map<int,CParnterNpcConfig*> mDatas;
            std::map<const std::wstring,CParnterNpcConfig*> mNames;
            CParnterNpcConfig mErrorData;
        };
        CParnterNpcConfigTable&  GetCParnterNpcConfigTableInstance();
    }
}
#endif // __CParnterNpcConfig__h__
