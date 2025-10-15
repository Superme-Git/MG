#ifndef __CStageInfo__h__
#define __CStageInfo__h__
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
        class CStageInfo
        {
            public:
            int id;
            int stagenum;
            int stagetype;
            int executetype;
            int delay;
            int actiontype;
            int actionlimittime;
            int movetype;
            int syncprotect;
            int targetx;
            int targety;
            int phantomid;
            int phantomalpha;
            int movetime;
            int effecttype;
            int postype;
            int hastrail;
            int youfangxiang;
            std::wstring effectname;
            int effectlayer;
            std::wstring effectsound;
            int resulttype;
            int resultlimittime;
            int resultonhittime;
            int teleporttype;
            int teletime;
            int blurtime;
            int teleportlimittime;
        };
        class CStageInfoTable  :public  TableBase 
        {
        public:
          CStageInfoTable(){ mErrorData.id=-1; }
          virtual ~CStageInfoTable()
          {
              std::map<int,CStageInfo*>::iterator it=mDatas.begin();
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
              std::map<int,CStageInfo*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CStageInfo& getRecorder(int id)
            {
                std::map<int,CStageInfo*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CStageInfo*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/battle.cstageinfo.bin"; }
        private:
            std::map<int,CStageInfo*> mDatas;
            CStageInfo mErrorData;
        };
        CStageInfoTable&  GetCStageInfoTableInstance();
    }
}
#endif // __CStageInfo__h__
