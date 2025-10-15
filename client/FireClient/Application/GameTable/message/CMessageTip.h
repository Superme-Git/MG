#ifndef __CMessageTip__h__
#define __CMessageTip__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace message
    {
        class CMessageTip
        {
            public:
            int id;
            std::wstring type;
            std::wstring msg;
        };
        class CMessageTipTable  :public  TableBase 
        {
        public:
          CMessageTipTable(){ mErrorData.id=-1; }
          virtual ~CMessageTipTable()
          {
              std::map<int,CMessageTip*>::iterator it=mDatas.begin();
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
              std::map<int,CMessageTip*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CMessageTip& getRecorder(int id)
            {
                std::map<int,CMessageTip*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CMessageTip*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/message.cmessagetip.bin"; }
        private:
            std::map<int,CMessageTip*> mDatas;
            CMessageTip mErrorData;
        };
        CMessageTipTable&  GetCMessageTipTableInstance();
    }
}
#endif // __CMessageTip__h__
