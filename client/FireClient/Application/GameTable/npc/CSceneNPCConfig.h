#ifndef __CSceneNPCConfig__h__
#define __CSceneNPCConfig__h__
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
        class CSceneNPCConfig
        {
            public:
            int id;
            int baseid;
            int mapid;
            int posx;
            int posy;
            int dir;
            int talkinterval;
            std::wstring talkintervalsays;
            int talkdistance;
            std::wstring talkdistancesays;
            int talkcharnum;
            std::wstring talkcharnumsays;
            int pacing;
            int movespeed;
            int mask;
            int transparent;
        };
        class CSceneNPCConfigTable  :public  TableBase 
        {
        public:
          CSceneNPCConfigTable(){ mErrorData.id=-1; }
          virtual ~CSceneNPCConfigTable()
          {
              std::map<int,CSceneNPCConfig*>::iterator it=mDatas.begin();
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
              std::map<int,CSceneNPCConfig*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CSceneNPCConfig& getRecorder(int id)
            {
                std::map<int,CSceneNPCConfig*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CSceneNPCConfig*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/npc.cscenenpcconfig.bin"; }
        private:
            std::map<int,CSceneNPCConfig*> mDatas;
            CSceneNPCConfig mErrorData;
        };
        CSceneNPCConfigTable&  GetCSceneNPCConfigTableInstance();
    }
}
#endif // __CSceneNPCConfig__h__
