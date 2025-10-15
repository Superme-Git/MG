#ifndef __CMainMissionInfo__h__
#define __CMainMissionInfo__h__
#include <string>
#include <vector>
#include <map>
#include "ljfmfex.h"
#include "TableBase.h"
std::wstring s2ws(const std::string& s);

namespace GameTable
{
    namespace mission
    {
        class CMainMissionInfo
        {
            public:
            int id;
            int AutoDo;
            std::wstring MissionName;
            std::wstring MissionTypeString;
            int MinLevel;
            int MaxLevel;
            int CruiseId;
            int TransMinLevel;
            int TransMaxLevel;
             std::vector<long long> RequestRoleIDList;
             std::vector<int> PostMissionList;
            int TransformID;
            int FollowID;
            std::wstring NoteInfo;
            long long ExpReward;
            long long MoneyReward;
            long long PetExpReward;
            int ShengWang;
            long long SMoney;
            int RewardMapJumpType;
            int RewardMapID;
            int RewardMapXPos;
            int RewardMapYPos;
            int ProcessBarTime;
            std::wstring ProcessBarText;
            std::wstring ProcessBarColor;
            int RewardType;
            int RewardOption;
             std::vector<int> RewardItemIDList;
             std::vector<int> RewardItemNumList;
             std::vector<int> RewardItemShapeIDList;
             std::vector<int> RewardItemIsBindList;
            int MissionType;
            int ActiveInfoNpcID;
            int ActiveInfoMapID;
            int ActiveInfoLeftPos;
            int ActiveInfoTopPos;
            int ActiveInfoRightPos;
            int ActiveInfoBottomPos;
            int ActiveInfoTargetID;
            int ActiveInfoTargetNum;
            int ActiveInfoMiniStep;
            int ActiveInfoStepProbability;
            int ActiveInfoMaxStep;
            int ActiveInfoTeamState;
            int ActiveInfoTimeLimit;
            int ActiveInfoIsRestartTimer;
            long long ActiveInfoGiveBackMoney;
            int ActiveInfoGiveBackPetID;
            int ActiveInfoUseItemID;
            int ActiveInfoOtherType;
            std::wstring QuestionInfoCorrectAnswer;
             std::vector<std::wstring> QuestionInfoWrongAnswerList;
            int QuestionInfoNpcID;
            std::wstring QuestionInfoConversion;
            std::wstring TaskInfoDescriptionListA;
            std::wstring TaskInfoPurposeListA;
            std::wstring TaskInfoTraceListA;
            int AIInfoAIID;
            int AIInfoBattleResult;
            int AIInfoDeathPunish;
            int AIInfoTeamSteate;
            std::wstring AIInfoBattleLevel;
            int BattleInfoBattleMapType;
            int BattleInfoBattleZoneID;
            int BattleInfoDrop;
            int BattleInfoBattleTimes;
             std::vector<int> BattleInfoMonsterList;
            int BattleInfoMonsterNum;
            int BattleInfoDropItemID;
            int BattleInfoDropItemNum;
            int ScenarioInfoAnimationID;
            int ScenarioInfoBranchNpcID;
            std::wstring ScenarioInfoBranchNote;
             std::vector<std::wstring> ScenarioInfoNpcConversationList;
             std::vector<int> ScenarioInfoNpcID;
             std::vector<std::wstring> ScenarioInfoFinishConversationList;
             std::vector<int> ScenarioInfoFinishNpcID;
             std::vector<int> vTaskShowNpc;
             std::vector<std::wstring> BiaoQingA;
             std::vector<std::wstring> BiaoQingB;
            int ProcessBarTeXiao;
            int UnionSeekHelp;
            int WorldSeekHelp;
            int nopuitype;
            int nuiid;
             std::vector<std::wstring> vNpcChatSound;
             std::vector<std::wstring> vNpcChatSoundFinish;
            std::wstring BattlePreString;
            int BattleVideo;
            int Tipsid;
        };
        class CMainMissionInfoTable  :public  TableBase 
        {
        public:
          CMainMissionInfoTable(){ mErrorData.id=-1; }
          virtual ~CMainMissionInfoTable()
          {
              std::map<int,CMainMissionInfo*>::iterator it=mDatas.begin();
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
              std::map<int,CMainMissionInfo*>::iterator it=mDatas.begin();
              while(it!=mDatas.end())
              {
                  delete it->second;
                  ++it;
              }
              mDatas.clear();
          }
            bool isLoaded(){ return !!mDatas.size(); }
        public:
            const CMainMissionInfo& getRecorder(int id)
            {
                std::map<int,CMainMissionInfo*>::iterator it=mDatas.find(id);
                if(it!=mDatas.end())
                    return *it->second;
                return mErrorData;
            }
            void getAllID(std::vector<int>& recordList)
            {
                std::map<int,CMainMissionInfo*>::iterator it=mDatas.begin();
                while(it!=mDatas.end())
                {
                    recordList.push_back(it->first);
                    ++it;
                }
            }
            virtual unsigned int getSize() const{ return mDatas.size(); }
            const wchar_t* GetBinFileName(){ return L"/table/bintable/mission.cmainmissioninfo.bin"; }
        private:
            std::map<int,CMainMissionInfo*> mDatas;
            CMainMissionInfo mErrorData;
        };
        CMainMissionInfoTable&  GetCMainMissionInfoTableInstance();
    }
}
#endif // __CMainMissionInfo__h__
