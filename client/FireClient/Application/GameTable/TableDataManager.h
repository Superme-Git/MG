#pragma once
#include <vector>
struct TableDataFileHeader
{
    unsigned int type;//'LDZY'.
    unsigned int size;//file size.
    unsigned short version;//file version.
    unsigned short itemCount;//data item count.
    unsigned int colCheckNumber;//col change check.
};
#include "battle/CStageInfo.h"
#include "battle/CStageInfo2.h"
#include "battle/CSkillInfo.h"
#include "battle/CBattleAIConfig.h"
#include "buff/CBuffConfig.h"
#include "chat/Cbiaoqing.h"
#include "chat/Cquickchat.h"
#include "common/CCommon.h"
#include "effect/CEffectConfig.h"
#include "effect/Ccoloreffect.h"
#include "effect/CUseItemEffect.h"
#include "EffectPath/CEffectPath.h"
#include "EffectPath/CEffectPathNoneDrama.h"
#include "gm/Cbattlelist.h"
#include "instance/CParnterNpcConfig.h"
#include "item/CEquipEffect.h"
#include "mission/CMainMissionInfo.h"
#include "message/CMessageTip.h"
#include "move/CAutoMovePath.h"
#include "move/CAutoMovePathPoint.h"
#include "npc/CMonsterConfig.h"
#include "npc/CSceneNPCConfig.h"
#include "npc/CSceneNPCBase.h"
#include "skill/CSkillTypeConfig.h"
#include "SysConfig/CGameconfig.h"
#include "TableBase.h"
class TableDataManager
{
    public:
        TableDataManager(){};
        ~TableDataManager(){};
        static TableDataManager& instance()
        {
            static TableDataManager dm;
            return dm;
        }
        bool loadAllTable(std::wstring& pathFileName,const std::wstring& path);
        void releaseAllTable();
        void CheckCorrectTableName(std::wstring& strFileNamePath);;
        std::wstring GetCorrectTableName(){ return mStrCorrectPath; }
        void SetCorrectTableName(std::wstring& strFileNamePath){ mStrCorrectPath = strFileNamePath; }
        void InsertTable(std::wstring& strFileNamePath, TableBase* pTable); 
    private:
        std::vector<void*> mDatas;
        std::wstring mStrCorrectPath;
        std::map< std::wstring, TableBase* > mMapTable;
};
