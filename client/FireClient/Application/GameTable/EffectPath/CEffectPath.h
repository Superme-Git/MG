#ifndef __CEffectPath__h__
#define __CEffectPath__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace EffectPath
    {
        class CEffectPath
        {
            public:
            int id;
            std::wstring Name;
            std::wstring Patn;
        };
        class CEffectPathTable  :public  TableBase 
        {
        public:
          CEffectPathTable(){ mErrorData.id=-1; }
          virtual ~CEffectPathTable()
          {
              std::map<int,CEffectPath*>::iterator it=mDatas.begin();
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
              std::map<int,CEffectPath*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CEffectPath& getRecorder(int id)
            {
                std::map<int,CEffectPath*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CEffectPath*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/EffectPath.ceffectpath.bin"; }
        private:
            std::map<int,CEffectPath*> mDatas;
            CEffectPath mErrorData;
        };
        CEffectPathTable&  GetCEffectPathTableInstance();
    }
}
#endif // __CEffectPath__h__
