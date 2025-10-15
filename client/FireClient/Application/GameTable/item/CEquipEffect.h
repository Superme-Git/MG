#ifndef __CEquipEffect__h__
#define __CEquipEffect__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace item
    {
        class CEquipEffect
        {
            public:
            int id;
            int equipcolor;
             std::vector<int> baseEffectType;
             std::vector<int> baseEffect;
            int sexNeed;
             std::vector<int> scorecolor;
             std::vector<int> roleNeed;
            int bCanSale;
            int dbCanSale;
            int sellpricecoef;
            int endurecoef;
            int suiting;
            int weaponid;
            int weaponeffectid;
             std::vector<std::wstring> modelpathleft;
             std::vector<std::wstring> modelpathright;
             std::vector<std::wstring> socketleft;
             std::vector<std::wstring> socketright;
            std::wstring needCareer;
            int eequiptype;
        };
        class CEquipEffectTable  :public  TableBase 
        {
        public:
          CEquipEffectTable(){ mErrorData.id=-1; }
          virtual ~CEquipEffectTable()
          {
              std::map<int,CEquipEffect*>::iterator it=mDatas.begin();
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
              std::map<int,CEquipEffect*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CEquipEffect& getRecorder(int id)
            {
                std::map<int,CEquipEffect*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CEquipEffect*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/item.cequipeffect.bin"; }
        private:
            std::map<int,CEquipEffect*> mDatas;
            CEquipEffect mErrorData;
        };
        CEquipEffectTable&  GetCEquipEffectTableInstance();
    }
}
#endif // __CEquipEffect__h__
