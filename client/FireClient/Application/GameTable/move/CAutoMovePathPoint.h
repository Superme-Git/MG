#ifndef __CAutoMovePathPoint__h__
#define __CAutoMovePathPoint__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace move
    {
        class CAutoMovePathPoint
        {
            public:
            int id;
            std::wstring ridename;
            std::wstring effectname;
            int cameraz;
            int speed;
            int sceneid;
            int pointcount;
             std::vector<std::wstring> points;
        };
        class CAutoMovePathPointTable  :public  TableBase 
        {
        public:
          CAutoMovePathPointTable(){ mErrorData.id=-1; }
          virtual ~CAutoMovePathPointTable()
          {
              std::map<int,CAutoMovePathPoint*>::iterator it=mDatas.begin();
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
              std::map<int,CAutoMovePathPoint*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CAutoMovePathPoint& getRecorder(int id)
            {
                std::map<int,CAutoMovePathPoint*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CAutoMovePathPoint*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/move.cautomovepathpoint.bin"; }
        private:
            std::map<int,CAutoMovePathPoint*> mDatas;
            CAutoMovePathPoint mErrorData;
        };
        CAutoMovePathPointTable&  GetCAutoMovePathPointTableInstance();
    }
}
#endif // __CAutoMovePathPoint__h__
