#include "TableDataManager.h"
#include "utils/StringUtil.h"
#include "CCLuaEngine.h"
namespace GameTable
{
  namespace battle  {
    CStageInfoTable&  GetCStageInfoTableInstance()
    {
        static CStageInfoTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CStageInfoTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=1835448)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CStageInfo* object=new CStageInfo;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->stagenum,sizeof(object->stagenum));
            file.Read(&object->stagetype,sizeof(object->stagetype));
            file.Read(&object->executetype,sizeof(object->executetype));
            file.Read(&object->delay,sizeof(object->delay));
            file.Read(&object->actiontype,sizeof(object->actiontype));
            file.Read(&object->actionlimittime,sizeof(object->actionlimittime));
            file.Read(&object->movetype,sizeof(object->movetype));
            file.Read(&object->syncprotect,sizeof(object->syncprotect));
            file.Read(&object->targetx,sizeof(object->targetx));
            file.Read(&object->targety,sizeof(object->targety));
            file.Read(&object->phantomid,sizeof(object->phantomid));
            file.Read(&object->phantomalpha,sizeof(object->phantomalpha));
            file.Read(&object->movetime,sizeof(object->movetime));
            file.Read(&object->effecttype,sizeof(object->effecttype));
            file.Read(&object->postype,sizeof(object->postype));
            file.Read(&object->hastrail,sizeof(object->hastrail));
            file.Read(&object->youfangxiang,sizeof(object->youfangxiang));
            object->effectname=ReadString(file);
            file.Read(&object->effectlayer,sizeof(object->effectlayer));
            object->effectsound=ReadString(file);
            file.Read(&object->resulttype,sizeof(object->resulttype));
            file.Read(&object->resultlimittime,sizeof(object->resultlimittime));
            file.Read(&object->resultonhittime,sizeof(object->resultonhittime));
            file.Read(&object->teleporttype,sizeof(object->teleporttype));
            file.Read(&object->teletime,sizeof(object->teletime));
            file.Read(&object->blurtime,sizeof(object->blurtime));
            file.Read(&object->teleportlimittime,sizeof(object->teleportlimittime));
            mDatas.insert(std::pair<int,CStageInfo*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace battle  {
    CStageInfo2Table&  GetCStageInfo2TableInstance()
    {
        static CStageInfo2Table object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CStageInfo2Table::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=1835448)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CStageInfo2* object=new CStageInfo2;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->stagenum,sizeof(object->stagenum));
            file.Read(&object->stagetype,sizeof(object->stagetype));
            file.Read(&object->executetype,sizeof(object->executetype));
            file.Read(&object->delay,sizeof(object->delay));
            file.Read(&object->actiontype,sizeof(object->actiontype));
            file.Read(&object->actionlimittime,sizeof(object->actionlimittime));
            file.Read(&object->movetype,sizeof(object->movetype));
            file.Read(&object->syncprotect,sizeof(object->syncprotect));
            file.Read(&object->targetx,sizeof(object->targetx));
            file.Read(&object->targety,sizeof(object->targety));
            file.Read(&object->phantomid,sizeof(object->phantomid));
            file.Read(&object->phantomalpha,sizeof(object->phantomalpha));
            file.Read(&object->movetime,sizeof(object->movetime));
            file.Read(&object->effecttype,sizeof(object->effecttype));
            file.Read(&object->postype,sizeof(object->postype));
            file.Read(&object->hastrail,sizeof(object->hastrail));
            file.Read(&object->youfangxiang,sizeof(object->youfangxiang));
            object->effectname=ReadString(file);
            file.Read(&object->effectlayer,sizeof(object->effectlayer));
            object->effectsound=ReadString(file);
            file.Read(&object->resulttype,sizeof(object->resulttype));
            file.Read(&object->resultlimittime,sizeof(object->resultlimittime));
            file.Read(&object->resultonhittime,sizeof(object->resultonhittime));
            file.Read(&object->teleporttype,sizeof(object->teleporttype));
            file.Read(&object->teletime,sizeof(object->teletime));
            file.Read(&object->blurtime,sizeof(object->blurtime));
            file.Read(&object->teleportlimittime,sizeof(object->teleportlimittime));
            mDatas.insert(std::pair<int,CStageInfo2*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace battle  {
    CSkillInfoTable&  GetCSkillInfoTableInstance()
    {
        static CSkillInfoTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CSkillInfoTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=196623)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CSkillInfo* object=new CSkillInfo;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->stagelist=ReadString(file);
            object->stagelist2=ReadString(file);
            mDatas.insert(std::pair<int,CSkillInfo*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace battle  {
    CBattleAIConfigTable&  GetCBattleAIConfigTableInstance()
    {
        static CBattleAIConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CBattleAIConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=786537)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CBattleAIConfig* object=new CBattleAIConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->talkinfo=ReadString(file);
            file.Read(&object->talkshow,sizeof(object->talkshow));
            object->tipsinfo=ReadString(file);
            file.Read(&object->appearchange,sizeof(object->appearchange));
            object->playeffect=ReadString(file);
            file.Read(&object->playsound,sizeof(object->playsound));
            file.Read(&object->changeground,sizeof(object->changeground));
            file.Read(&object->changegroundeffect,sizeof(object->changegroundeffect));
            object->speak=ReadString(file);
            file.Read(&object->speakshow,sizeof(object->speakshow));
            object->speaktime=ReadString(file);
            mDatas.insert(std::pair<int,CBattleAIConfig*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace buff  {
    CBuffConfigTable&  GetCBuffConfigTableInstance()
    {
        static CBuffConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CBuffConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=786537)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CBuffConfig* object=new CBuffConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->name=ReadString(file);
            file.Read(&object->inbattle,sizeof(object->inbattle));
            object->strshowname=ReadString(file);
            file.Read(&object->shapeid,sizeof(object->shapeid));
            object->discribe=ReadString(file);
            object->effect=ReadString(file);
            file.Read(&object->effectsort,sizeof(object->effectsort));
            object->wordeffect=ReadString(file);
            file.Read(&object->wordsort,sizeof(object->wordsort));
            file.Read(&object->cleartype,sizeof(object->cleartype));
            int specialshowLength;
            file.Read(&specialshowLength,sizeof(int));
            for(j=0;j<specialshowLength;j++)
            {
                int specialshowData;
                file.Read(&specialshowData,sizeof(int));
                object->specialshow.push_back(specialshowData);
            }
            mDatas.insert(std::pair<int,CBuffConfig*>(object->id,object));
            mNames.insert(std::pair<const std::wstring,CBuffConfig*>(object->name,object));
        }
        file.Close();
        return true;
    }
  }
  namespace chat  {
    CbiaoqingTable&  GetCbiaoqingTableInstance()
    {
        static CbiaoqingTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CbiaoqingTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=327708)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            Cbiaoqing* object=new Cbiaoqing;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->imagenum,sizeof(object->imagenum));
            file.Read(&object->time,sizeof(object->time));
            object->tips=ReadString(file);
            object->key=ReadString(file);
            mDatas.insert(std::pair<int,Cbiaoqing*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace chat  {
    CquickchatTable&  GetCquickchatTableInstance()
    {
        static CquickchatTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CquickchatTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=131080)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            Cquickchat* object=new Cquickchat;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->tips=ReadString(file);
            mDatas.insert(std::pair<int,Cquickchat*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace common  {
    CCommonTable&  GetCCommonTableInstance()
    {
        static CCommonTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CCommonTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=131080)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CCommon* object=new CCommon;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->value=ReadString(file);
            mDatas.insert(std::pair<int,CCommon*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace effect  {
    CEffectConfigTable&  GetCEffectConfigTableInstance()
    {
        static CEffectConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CEffectConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=720980)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CEffectConfig* object=new CEffectConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->classname=ReadString(file);
            file.Read(&object->xianshi,sizeof(object->xianshi));
            file.Read(&object->color,sizeof(object->color));
            file.Read(&object->order,sizeof(object->order));
            file.Read(&object->effectid,sizeof(object->effectid));
            file.Read(&object->reducecolor,sizeof(object->reducecolor));
            file.Read(&object->reduceeffectid,sizeof(object->reduceeffectid));
            file.Read(&object->width,sizeof(object->width));
            file.Read(&object->ablLimit,sizeof(object->ablLimit));
            file.Read(&object->pctLimit,sizeof(object->pctLimit));
            mDatas.insert(std::pair<int,CEffectConfig*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace effect  {
    CcoloreffectTable&  GetCcoloreffectTableInstance()
    {
        static CcoloreffectTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CcoloreffectTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=327700)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            Ccoloreffect* object=new Ccoloreffect;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->red,sizeof(object->red));
            file.Read(&object->yellow,sizeof(object->yellow));
            file.Read(&object->blue,sizeof(object->blue));
            file.Read(&object->green,sizeof(object->green));
            mDatas.insert(std::pair<int,Ccoloreffect*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace effect  {
    CUseItemEffectTable&  GetCUseItemEffectTableInstance()
    {
        static CUseItemEffectTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CUseItemEffectTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=131080)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CUseItemEffect* object=new CUseItemEffect;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->destWindow=ReadString(file);
            mDatas.insert(std::pair<int,CUseItemEffect*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace EffectPath  {
    CEffectPathTable&  GetCEffectPathTableInstance()
    {
        static CEffectPathTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CEffectPathTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=196623)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CEffectPath* object=new CEffectPath;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->Name=ReadString(file);
            object->Patn=ReadString(file);
            mDatas.insert(std::pair<int,CEffectPath*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace EffectPath  {
    CEffectPathNoneDramaTable&  GetCEffectPathNoneDramaTableInstance()
    {
        static CEffectPathNoneDramaTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CEffectPathNoneDramaTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=131080)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CEffectPathNoneDrama* object=new CEffectPathNoneDrama;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->Path=ReadString(file);
            mDatas.insert(std::pair<int,CEffectPathNoneDrama*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace gm  {
    CbattlelistTable&  GetCbattlelistTableInstance()
    {
        static CbattlelistTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CbattlelistTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=131077)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            Cbattlelist* object=new Cbattlelist;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->positionsid,sizeof(object->positionsid));
            mDatas.insert(std::pair<int,Cbattlelist*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace instance  {
    CParnterNpcConfigTable&  GetCParnterNpcConfigTableInstance()
    {
        static CParnterNpcConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CParnterNpcConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=720985)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CParnterNpcConfig* object=new CParnterNpcConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->name=ReadString(file);
            file.Read(&object->schoolid,sizeof(object->schoolid));
            file.Read(&object->type,sizeof(object->type));
            file.Read(&object->modelid,sizeof(object->modelid));
            file.Read(&object->serverid,sizeof(object->serverid));
            file.Read(&object->bufferid,sizeof(object->bufferid));
            file.Read(&object->follownpcid,sizeof(object->follownpcid));
            object->jiadian=ReadString(file);
            object->skillname=ReadString(file);
            object->roleintro=ReadString(file);
            mDatas.insert(std::pair<int,CParnterNpcConfig*>(object->id,object));
            mNames.insert(std::pair<const std::wstring,CParnterNpcConfig*>(object->name,object));
        }
        file.Close();
        return true;
    }
  }
  namespace item  {
    CEquipEffectTable&  GetCEquipEffectTableInstance()
    {
        static CEquipEffectTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CEquipEffectTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=2556710)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CEquipEffect* object=new CEquipEffect;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->equipcolor,sizeof(object->equipcolor));
            int baseEffectTypeLength;
            file.Read(&baseEffectTypeLength,sizeof(int));
            for(j=0;j<baseEffectTypeLength;j++)
            {
                int baseEffectTypeData;
                file.Read(&baseEffectTypeData,sizeof(int));
                object->baseEffectType.push_back(baseEffectTypeData);
            }
            int baseEffectLength;
            file.Read(&baseEffectLength,sizeof(int));
            for(j=0;j<baseEffectLength;j++)
            {
                int baseEffectData;
                file.Read(&baseEffectData,sizeof(int));
                object->baseEffect.push_back(baseEffectData);
            }
            file.Read(&object->sexNeed,sizeof(object->sexNeed));
            int scorecolorLength;
            file.Read(&scorecolorLength,sizeof(int));
            for(j=0;j<scorecolorLength;j++)
            {
                int scorecolorData;
                file.Read(&scorecolorData,sizeof(int));
                object->scorecolor.push_back(scorecolorData);
            }
            int roleNeedLength;
            file.Read(&roleNeedLength,sizeof(int));
            for(j=0;j<roleNeedLength;j++)
            {
                int roleNeedData;
                file.Read(&roleNeedData,sizeof(int));
                object->roleNeed.push_back(roleNeedData);
            }
            file.Read(&object->bCanSale,sizeof(object->bCanSale));
            file.Read(&object->dbCanSale,sizeof(object->dbCanSale));
            file.Read(&object->sellpricecoef,sizeof(object->sellpricecoef));
            file.Read(&object->endurecoef,sizeof(object->endurecoef));
            file.Read(&object->suiting,sizeof(object->suiting));
            file.Read(&object->weaponid,sizeof(object->weaponid));
            file.Read(&object->weaponeffectid,sizeof(object->weaponeffectid));
            int modelpathleftLength;
            file.Read(&modelpathleftLength,sizeof(int));
            for(j=0;j<modelpathleftLength;j++)
            {
                std::wstring modelpathleftData;
                modelpathleftData = ReadString(file);
                object->modelpathleft.push_back(modelpathleftData);
            }
            int modelpathrightLength;
            file.Read(&modelpathrightLength,sizeof(int));
            for(j=0;j<modelpathrightLength;j++)
            {
                std::wstring modelpathrightData;
                modelpathrightData = ReadString(file);
                object->modelpathright.push_back(modelpathrightData);
            }
            int socketleftLength;
            file.Read(&socketleftLength,sizeof(int));
            for(j=0;j<socketleftLength;j++)
            {
                std::wstring socketleftData;
                socketleftData = ReadString(file);
                object->socketleft.push_back(socketleftData);
            }
            int socketrightLength;
            file.Read(&socketrightLength,sizeof(int));
            for(j=0;j<socketrightLength;j++)
            {
                std::wstring socketrightData;
                socketrightData = ReadString(file);
                object->socketright.push_back(socketrightData);
            }
            object->needCareer=ReadString(file);
            file.Read(&object->eequiptype,sizeof(object->eequiptype));
            mDatas.insert(std::pair<int,CEquipEffect*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace mission  {
    CMainMissionInfoTable&  GetCMainMissionInfoTableInstance()
    {
        static CMainMissionInfoTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CMainMissionInfoTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=11747165)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CMainMissionInfo* object=new CMainMissionInfo;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->AutoDo,sizeof(object->AutoDo));
            object->MissionName=ReadString(file);
            object->MissionTypeString=ReadString(file);
            file.Read(&object->MinLevel,sizeof(object->MinLevel));
            file.Read(&object->MaxLevel,sizeof(object->MaxLevel));
            file.Read(&object->CruiseId,sizeof(object->CruiseId));
            file.Read(&object->TransMinLevel,sizeof(object->TransMinLevel));
            file.Read(&object->TransMaxLevel,sizeof(object->TransMaxLevel));
            int RequestRoleIDListLength;
            file.Read(&RequestRoleIDListLength,sizeof(int));
            for(j=0;j<RequestRoleIDListLength;j++)
            {
                long long RequestRoleIDListData;
                file.Read(&RequestRoleIDListData,sizeof(long long));
                object->RequestRoleIDList.push_back(RequestRoleIDListData);
            }
            int PostMissionListLength;
            file.Read(&PostMissionListLength,sizeof(int));
            for(j=0;j<PostMissionListLength;j++)
            {
                int PostMissionListData;
                file.Read(&PostMissionListData,sizeof(int));
                object->PostMissionList.push_back(PostMissionListData);
            }
            file.Read(&object->TransformID,sizeof(object->TransformID));
            file.Read(&object->FollowID,sizeof(object->FollowID));
            object->NoteInfo=ReadString(file);
            file.Read(&object->ExpReward,sizeof(object->ExpReward));
            file.Read(&object->MoneyReward,sizeof(object->MoneyReward));
            file.Read(&object->PetExpReward,sizeof(object->PetExpReward));
            file.Read(&object->ShengWang,sizeof(object->ShengWang));
            file.Read(&object->SMoney,sizeof(object->SMoney));
            file.Read(&object->RewardMapJumpType,sizeof(object->RewardMapJumpType));
            file.Read(&object->RewardMapID,sizeof(object->RewardMapID));
            file.Read(&object->RewardMapXPos,sizeof(object->RewardMapXPos));
            file.Read(&object->RewardMapYPos,sizeof(object->RewardMapYPos));
            file.Read(&object->ProcessBarTime,sizeof(object->ProcessBarTime));
            object->ProcessBarText=ReadString(file);
            object->ProcessBarColor=ReadString(file);
            file.Read(&object->RewardType,sizeof(object->RewardType));
            file.Read(&object->RewardOption,sizeof(object->RewardOption));
            int RewardItemIDListLength;
            file.Read(&RewardItemIDListLength,sizeof(int));
            for(j=0;j<RewardItemIDListLength;j++)
            {
                int RewardItemIDListData;
                file.Read(&RewardItemIDListData,sizeof(int));
                object->RewardItemIDList.push_back(RewardItemIDListData);
            }
            int RewardItemNumListLength;
            file.Read(&RewardItemNumListLength,sizeof(int));
            for(j=0;j<RewardItemNumListLength;j++)
            {
                int RewardItemNumListData;
                file.Read(&RewardItemNumListData,sizeof(int));
                object->RewardItemNumList.push_back(RewardItemNumListData);
            }
            int RewardItemShapeIDListLength;
            file.Read(&RewardItemShapeIDListLength,sizeof(int));
            for(j=0;j<RewardItemShapeIDListLength;j++)
            {
                int RewardItemShapeIDListData;
                file.Read(&RewardItemShapeIDListData,sizeof(int));
                object->RewardItemShapeIDList.push_back(RewardItemShapeIDListData);
            }
            int RewardItemIsBindListLength;
            file.Read(&RewardItemIsBindListLength,sizeof(int));
            for(j=0;j<RewardItemIsBindListLength;j++)
            {
                int RewardItemIsBindListData;
                file.Read(&RewardItemIsBindListData,sizeof(int));
                object->RewardItemIsBindList.push_back(RewardItemIsBindListData);
            }
            file.Read(&object->MissionType,sizeof(object->MissionType));
            file.Read(&object->ActiveInfoNpcID,sizeof(object->ActiveInfoNpcID));
            file.Read(&object->ActiveInfoMapID,sizeof(object->ActiveInfoMapID));
            file.Read(&object->ActiveInfoLeftPos,sizeof(object->ActiveInfoLeftPos));
            file.Read(&object->ActiveInfoTopPos,sizeof(object->ActiveInfoTopPos));
            file.Read(&object->ActiveInfoRightPos,sizeof(object->ActiveInfoRightPos));
            file.Read(&object->ActiveInfoBottomPos,sizeof(object->ActiveInfoBottomPos));
            file.Read(&object->ActiveInfoTargetID,sizeof(object->ActiveInfoTargetID));
            file.Read(&object->ActiveInfoTargetNum,sizeof(object->ActiveInfoTargetNum));
            file.Read(&object->ActiveInfoMiniStep,sizeof(object->ActiveInfoMiniStep));
            file.Read(&object->ActiveInfoStepProbability,sizeof(object->ActiveInfoStepProbability));
            file.Read(&object->ActiveInfoMaxStep,sizeof(object->ActiveInfoMaxStep));
            file.Read(&object->ActiveInfoTeamState,sizeof(object->ActiveInfoTeamState));
            file.Read(&object->ActiveInfoTimeLimit,sizeof(object->ActiveInfoTimeLimit));
            file.Read(&object->ActiveInfoIsRestartTimer,sizeof(object->ActiveInfoIsRestartTimer));
            file.Read(&object->ActiveInfoGiveBackMoney,sizeof(object->ActiveInfoGiveBackMoney));
            file.Read(&object->ActiveInfoGiveBackPetID,sizeof(object->ActiveInfoGiveBackPetID));
            file.Read(&object->ActiveInfoUseItemID,sizeof(object->ActiveInfoUseItemID));
            file.Read(&object->ActiveInfoOtherType,sizeof(object->ActiveInfoOtherType));
            object->QuestionInfoCorrectAnswer=ReadString(file);
            int QuestionInfoWrongAnswerListLength;
            file.Read(&QuestionInfoWrongAnswerListLength,sizeof(int));
            for(j=0;j<QuestionInfoWrongAnswerListLength;j++)
            {
                std::wstring QuestionInfoWrongAnswerListData;
                QuestionInfoWrongAnswerListData = ReadString(file);
                object->QuestionInfoWrongAnswerList.push_back(QuestionInfoWrongAnswerListData);
            }
            file.Read(&object->QuestionInfoNpcID,sizeof(object->QuestionInfoNpcID));
            object->QuestionInfoConversion=ReadString(file);
            object->TaskInfoDescriptionListA=ReadString(file);
            object->TaskInfoPurposeListA=ReadString(file);
            object->TaskInfoTraceListA=ReadString(file);
            file.Read(&object->AIInfoAIID,sizeof(object->AIInfoAIID));
            file.Read(&object->AIInfoBattleResult,sizeof(object->AIInfoBattleResult));
            file.Read(&object->AIInfoDeathPunish,sizeof(object->AIInfoDeathPunish));
            file.Read(&object->AIInfoTeamSteate,sizeof(object->AIInfoTeamSteate));
            object->AIInfoBattleLevel=ReadString(file);
            file.Read(&object->BattleInfoBattleMapType,sizeof(object->BattleInfoBattleMapType));
            file.Read(&object->BattleInfoBattleZoneID,sizeof(object->BattleInfoBattleZoneID));
            file.Read(&object->BattleInfoDrop,sizeof(object->BattleInfoDrop));
            file.Read(&object->BattleInfoBattleTimes,sizeof(object->BattleInfoBattleTimes));
            int BattleInfoMonsterListLength;
            file.Read(&BattleInfoMonsterListLength,sizeof(int));
            for(j=0;j<BattleInfoMonsterListLength;j++)
            {
                int BattleInfoMonsterListData;
                file.Read(&BattleInfoMonsterListData,sizeof(int));
                object->BattleInfoMonsterList.push_back(BattleInfoMonsterListData);
            }
            file.Read(&object->BattleInfoMonsterNum,sizeof(object->BattleInfoMonsterNum));
            file.Read(&object->BattleInfoDropItemID,sizeof(object->BattleInfoDropItemID));
            file.Read(&object->BattleInfoDropItemNum,sizeof(object->BattleInfoDropItemNum));
            file.Read(&object->ScenarioInfoAnimationID,sizeof(object->ScenarioInfoAnimationID));
            file.Read(&object->ScenarioInfoBranchNpcID,sizeof(object->ScenarioInfoBranchNpcID));
            object->ScenarioInfoBranchNote=ReadString(file);
            int ScenarioInfoNpcConversationListLength;
            file.Read(&ScenarioInfoNpcConversationListLength,sizeof(int));
            for(j=0;j<ScenarioInfoNpcConversationListLength;j++)
            {
                std::wstring ScenarioInfoNpcConversationListData;
                ScenarioInfoNpcConversationListData = ReadString(file);
                object->ScenarioInfoNpcConversationList.push_back(ScenarioInfoNpcConversationListData);
            }
            int ScenarioInfoNpcIDLength;
            file.Read(&ScenarioInfoNpcIDLength,sizeof(int));
            for(j=0;j<ScenarioInfoNpcIDLength;j++)
            {
                int ScenarioInfoNpcIDData;
                file.Read(&ScenarioInfoNpcIDData,sizeof(int));
                object->ScenarioInfoNpcID.push_back(ScenarioInfoNpcIDData);
            }
            int ScenarioInfoFinishConversationListLength;
            file.Read(&ScenarioInfoFinishConversationListLength,sizeof(int));
            for(j=0;j<ScenarioInfoFinishConversationListLength;j++)
            {
                std::wstring ScenarioInfoFinishConversationListData;
                ScenarioInfoFinishConversationListData = ReadString(file);
                object->ScenarioInfoFinishConversationList.push_back(ScenarioInfoFinishConversationListData);
            }
            int ScenarioInfoFinishNpcIDLength;
            file.Read(&ScenarioInfoFinishNpcIDLength,sizeof(int));
            for(j=0;j<ScenarioInfoFinishNpcIDLength;j++)
            {
                int ScenarioInfoFinishNpcIDData;
                file.Read(&ScenarioInfoFinishNpcIDData,sizeof(int));
                object->ScenarioInfoFinishNpcID.push_back(ScenarioInfoFinishNpcIDData);
            }
            int vTaskShowNpcLength;
            file.Read(&vTaskShowNpcLength,sizeof(int));
            for(j=0;j<vTaskShowNpcLength;j++)
            {
                int vTaskShowNpcData;
                file.Read(&vTaskShowNpcData,sizeof(int));
                object->vTaskShowNpc.push_back(vTaskShowNpcData);
            }
            int BiaoQingALength;
            file.Read(&BiaoQingALength,sizeof(int));
            for(j=0;j<BiaoQingALength;j++)
            {
                std::wstring BiaoQingAData;
                BiaoQingAData = ReadString(file);
                object->BiaoQingA.push_back(BiaoQingAData);
            }
            int BiaoQingBLength;
            file.Read(&BiaoQingBLength,sizeof(int));
            for(j=0;j<BiaoQingBLength;j++)
            {
                std::wstring BiaoQingBData;
                BiaoQingBData = ReadString(file);
                object->BiaoQingB.push_back(BiaoQingBData);
            }
            file.Read(&object->ProcessBarTeXiao,sizeof(object->ProcessBarTeXiao));
            file.Read(&object->UnionSeekHelp,sizeof(object->UnionSeekHelp));
            file.Read(&object->WorldSeekHelp,sizeof(object->WorldSeekHelp));
            file.Read(&object->nopuitype,sizeof(object->nopuitype));
            file.Read(&object->nuiid,sizeof(object->nuiid));
            int vNpcChatSoundLength;
            file.Read(&vNpcChatSoundLength,sizeof(int));
            for(j=0;j<vNpcChatSoundLength;j++)
            {
                std::wstring vNpcChatSoundData;
                vNpcChatSoundData = ReadString(file);
                object->vNpcChatSound.push_back(vNpcChatSoundData);
            }
            int vNpcChatSoundFinishLength;
            file.Read(&vNpcChatSoundFinishLength,sizeof(int));
            for(j=0;j<vNpcChatSoundFinishLength;j++)
            {
                std::wstring vNpcChatSoundFinishData;
                vNpcChatSoundFinishData = ReadString(file);
                object->vNpcChatSoundFinish.push_back(vNpcChatSoundFinishData);
            }
            object->BattlePreString=ReadString(file);
            file.Read(&object->BattleVideo,sizeof(object->BattleVideo));
            file.Read(&object->Tipsid,sizeof(object->Tipsid));
            mDatas.insert(std::pair<int,CMainMissionInfo*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace message  {
    CMessageTipTable&  GetCMessageTipTableInstance()
    {
        static CMessageTipTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CMessageTipTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=196623)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CMessageTip* object=new CMessageTip;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->type=ReadString(file);
            object->msg=ReadString(file);
            mDatas.insert(std::pair<int,CMessageTip*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace move  {
    CAutoMovePathTable&  GetCAutoMovePathTableInstance()
    {
        static CAutoMovePathTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CAutoMovePathTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=131080)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CAutoMovePath* object=new CAutoMovePath;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->paths=ReadString(file);
            mDatas.insert(std::pair<int,CAutoMovePath*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace move  {
    CAutoMovePathPointTable&  GetCAutoMovePathPointTableInstance()
    {
        static CAutoMovePathPointTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CAutoMovePathPointTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=1769843)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CAutoMovePathPoint* object=new CAutoMovePathPoint;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->ridename=ReadString(file);
            object->effectname=ReadString(file);
            file.Read(&object->cameraz,sizeof(object->cameraz));
            file.Read(&object->speed,sizeof(object->speed));
            file.Read(&object->sceneid,sizeof(object->sceneid));
            file.Read(&object->pointcount,sizeof(object->pointcount));
            int pointsLength;
            file.Read(&pointsLength,sizeof(int));
            for(j=0;j<pointsLength;j++)
            {
                std::wstring pointsData;
                pointsData = ReadString(file);
                object->points.push_back(pointsData);
            }
            mDatas.insert(std::pair<int,CAutoMovePathPoint*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace npc  {
    CMonsterConfigTable&  GetCMonsterConfigTableInstance()
    {
        static CMonsterConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CMonsterConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=917631)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CMonsterConfig* object=new CMonsterConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->name=ReadString(file);
            object->title=ReadString(file);
            file.Read(&object->modelID,sizeof(object->modelID));
            file.Read(&object->becatch,sizeof(object->becatch));
            file.Read(&object->showhpbar,sizeof(object->showhpbar));
            file.Read(&object->showhpbarbig,sizeof(object->showhpbarbig));
            file.Read(&object->showlevel,sizeof(object->showlevel));
            file.Read(&object->npctype,sizeof(object->npctype));
            file.Read(&object->bodytype,sizeof(object->bodytype));
            file.Read(&object->area1colour,sizeof(object->area1colour));
            file.Read(&object->area2colour,sizeof(object->area2colour));
            file.Read(&object->petid,sizeof(object->petid));
            file.Read(&object->school,sizeof(object->school));
            mDatas.insert(std::pair<int,CMonsterConfig*>(object->id,object));
            mNames.insert(std::pair<const std::wstring,CMonsterConfig*>(object->name,object));
        }
        file.Close();
        return true;
    }
  }
  namespace npc  {
    CSceneNPCConfigTable&  GetCSceneNPCConfigTableInstance()
    {
        static CSceneNPCConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CSceneNPCConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=1048737)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CSceneNPCConfig* object=new CSceneNPCConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->baseid,sizeof(object->baseid));
            file.Read(&object->mapid,sizeof(object->mapid));
            file.Read(&object->posx,sizeof(object->posx));
            file.Read(&object->posy,sizeof(object->posy));
            file.Read(&object->dir,sizeof(object->dir));
            file.Read(&object->talkinterval,sizeof(object->talkinterval));
            object->talkintervalsays=ReadString(file);
            file.Read(&object->talkdistance,sizeof(object->talkdistance));
            object->talkdistancesays=ReadString(file);
            file.Read(&object->talkcharnum,sizeof(object->talkcharnum));
            object->talkcharnumsays=ReadString(file);
            file.Read(&object->pacing,sizeof(object->pacing));
            file.Read(&object->movespeed,sizeof(object->movespeed));
            file.Read(&object->mask,sizeof(object->mask));
            file.Read(&object->transparent,sizeof(object->transparent));
            mDatas.insert(std::pair<int,CSceneNPCConfig*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace npc  {
    CSceneNPCBaseTable&  GetCSceneNPCBaseTableInstance()
    {
        static CSceneNPCBaseTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CSceneNPCBaseTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=852081)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CSceneNPCBase* object=new CSceneNPCBase;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->shapeid,sizeof(object->shapeid));
            object->name=ReadString(file);
            file.Read(&object->type,sizeof(object->type));
            object->npcappear=ReadString(file);
            object->npcdisappear=ReadString(file);
            file.Read(&object->scale,sizeof(object->scale));
            file.Read(&object->area1colour,sizeof(object->area1colour));
            int chatidlistLength;
            file.Read(&chatidlistLength,sizeof(int));
            for(j=0;j<chatidlistLength;j++)
            {
                int chatidlistData;
                file.Read(&chatidlistData,sizeof(int));
                object->chatidlist.push_back(chatidlistData);
            }
            file.Read(&object->mask,sizeof(object->mask));
            file.Read(&object->transparent,sizeof(object->transparent));
            mDatas.insert(std::pair<int,CSceneNPCBase*>(object->id,object));
            mNames.insert(std::pair<const std::wstring,CSceneNPCBase*>(object->name,object));
        }
        file.Close();
        return true;
    }
  }
  namespace skill  {
    CSkillTypeConfigTable&  GetCSkillTypeConfigTableInstance()
    {
        static CSkillTypeConfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CSkillTypeConfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=262161)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CSkillTypeConfig* object=new CSkillTypeConfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            file.Read(&object->skilltype,sizeof(object->skilltype));
            object->skillname=ReadString(file);
            file.Read(&object->showskillname,sizeof(object->showskillname));
            mDatas.insert(std::pair<int,CSkillTypeConfig*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
  namespace SysConfig  {
    CGameconfigTable&  GetCGameconfigTableInstance()
    {
        static CGameconfigTable object;
        if(!object.isLoaded())
        {
               std::wstring strFileNamePath = object.GetBinFileName();
               TableDataManager::instance().InsertTable(strFileNamePath, &object);
               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);
               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();
               object.SetupData(pathFileName);
         }
        return object;
    }
    bool CGameconfigTable::SetupData(const std::wstring& pathFileName)
    {
        LJFM::LJFMF file;
        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
            return false;
        unsigned int type;
        file.Read(&type,sizeof(type));
        unsigned int size;
        file.Read(&size,sizeof(size));
        unsigned short version;
        file.Read(&version,sizeof(version));
        if(version!=101)
        {
            file.Close();
            return false;
        }
        unsigned short itemCount;
        file.Read(&itemCount,sizeof(version));
        unsigned int colCheckNumber;
        file.Read(&colCheckNumber,sizeof(colCheckNumber));
        if(colCheckNumber!=327706)
        {
            file.Close();
            return false;
        }
        for(size_t i=0;i<itemCount;i++)
        {
            CGameconfig* object=new CGameconfig;
            int j=0;
            file.Read(&object->id,sizeof(object->id));
            object->key=ReadString(file);
            object->wndname=ReadString(file);
            file.Read(&object->leixingtype,sizeof(object->leixingtype));
            file.Read(&object->value,sizeof(object->value));
            mDatas.insert(std::pair<int,CGameconfig*>(object->id,object));
        }
        file.Close();
        return true;
    }
  }
}
void TableDataManager::CheckCorrectTableName(std::wstring& strFileNamePath){
      mStrCorrectPath = strFileNamePath;
      cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("g_CheckCorrectTableName", ws2s(strFileNamePath).c_str());
}
void TableDataManager::releaseAllTable()
{ 
   std::map< std::wstring, TableBase* >::iterator it =  mMapTable.begin(); 
   while (it != mMapTable.end()) 
  { 
      TableBase* pTable = it->second;
      ++it;
      if (pTable) 
      { 
          pTable->ReleaseData();
      } 
  }
  mMapTable.clear();
}
void TableDataManager::InsertTable(std::wstring& strFileNamePath, TableBase* pTable)
{ 
      mMapTable.insert(std::make_pair(strFileNamePath, pTable)); 
}
bool TableDataManager::loadAllTable(std::wstring& pathFileName,const std::wstring& path){
    if(sizeof(unsigned int)!=4)
        return false;
    if(sizeof(int)!=4)
        return false;
    if(sizeof(long long)!=8)
        return false;
    if(sizeof(double)!=8)
        return false;
    return true;
}
