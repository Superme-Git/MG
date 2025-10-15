#ifndef __CEffectConfig__h__
#define __CEffectConfig__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace effect
    {
        class CEffectConfig
        {
            public:
            int id;
            std::wstring classname;
            int xianshi;
            int color;
            int order;
            int effectid;
            int reducecolor;
            int reduceeffectid;
            int width;
            double ablLimit;
            double pctLimit;
        };
        class CEffectConfigTable  :public  TableBase 
        {
        public:
          CEffectConfigTable(){ mErrorData.id=-1; }
          virtual ~CEffectConfigTable()
          {
              std::map<int,CEffectConfig*>::iterator it=mDatas.begin();
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
              std::map<int,CEffectConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CEffectConfig& getRecorder(int id)
            {
                std::map<int,CEffectConfig*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CEffectConfig*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/effect.ceffectconfig.bin"; }
        private:
            std::map<int,CEffectConfig*> mDatas;
            CEffectConfig mErrorData;
        };
        CEffectConfigTable&  GetCEffectConfigTableInstance();
    }
}
#endif // __CEffectConfig__h__
