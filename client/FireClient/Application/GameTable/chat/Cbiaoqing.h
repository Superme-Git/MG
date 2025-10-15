#ifndef __Cbiaoqing__h__
#define __Cbiaoqing__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace chat
    {
        class Cbiaoqing
        {
            public:
            int id;
            int imagenum;
            double time;
            std::wstring tips;
            std::wstring key;
        };
        class CbiaoqingTable  :public  TableBase 
        {
        public:
          CbiaoqingTable(){ mErrorData.id=-1; }
          virtual ~CbiaoqingTable()
          {
              std::map<int,Cbiaoqing*>::iterator it=mDatas.begin();
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
              std::map<int,Cbiaoqing*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const Cbiaoqing& getRecorder(int id)
            {
                std::map<int,Cbiaoqing*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,Cbiaoqing*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/chat.cbiaoqing.bin"; }
        private:
            std::map<int,Cbiaoqing*> mDatas;
            Cbiaoqing mErrorData;
        };
        CbiaoqingTable&  GetCbiaoqingTableInstance();
    }
}
#endif // __Cbiaoqing__h__
