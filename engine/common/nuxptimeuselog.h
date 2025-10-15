//#define XPDBG_PRINT_FRAME_STATE

#if (defined WIN7_32)
#include "platform/platform_types.h"
#endif

struct XL_FRAMESTATE 
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"framestate.csv"; } 
};

struct XL_BIGFRAMESTATE 
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"bigframestate.csv"; } 
};

struct XL_FULLFRAMESTATE 
{ 
	static DWORD s_nProcessId;
	static const wchar_t *fn() { return L"fullframestate.txt"; } 
};


#ifdef XPDBG_PRINT_FRAME_STATE
#define XPLOG_FRAME_STATE xwprint<XL_FRAMESTATE>
#define XPLOG_BIG_FRAME_STATE xwprint<XL_BIGFRAMESTATE>
#define XPLOG_FULL_FRAME_STATE xwprint<XL_FULLFRAMESTATE>
#define XPLOG_FRAME_STATE_CHECK checklogfile<XL_FRAMESTATE>
#define XPLOG_BIG_FRAME_STATE_CHECK checklogfile<XL_BIGFRAMESTATE>
#define XPLOG_FULL_FRAME_STATE_CHECK checklogfile<XL_FULLFRAMESTATE>
#else
#define XPLOG_FRAME_STATE_CHECK dellogfile<XL_FRAMESTATE>
#define XPLOG_BIG_FRAME_STATE_CHECK dellogfile<XL_BIGFRAMESTATE>
#define XPLOG_FULL_FRAME_STATE_CHECK dellogfile<XL_FULLFRAMESTATE>
#endif

namespace Nuclear
{
	class XPTimeUseLog
	{
	public:
		XPTimeUseLog() 
		{
#ifdef  XPDBG_PRINT_FRAME_STATE
			m_total.resize(TUL_COUNT);
			m_min.resize(TUL_COUNT);
			m_max.resize(TUL_COUNT);
			m_count = 0;
			LogsVector::iterator it = m_min.begin(), ie = m_min.end();
			for (;it!=ie;++it)
			{
				*it = 100000.0f;
			}
			XL_FRAMESTATE::s_nProcessId = GetCurrentProcessId();
			XL_BIGFRAMESTATE::s_nProcessId = XL_FRAMESTATE::s_nProcessId;
			m_fileIOCount = 0;
			m_fileIOSize = 0;
#endif
		}
		~XPTimeUseLog() 
		{
#ifdef  XPDBG_PRINT_FRAME_STATE
			if (m_count == 0)
				return;
			FILE *f = NULL;
			if( _wfopen_s(&f, XL_FRAMESTATE::fn(), L"a+")) return;
			fwprintf(f, L"Begin......\n");
			fwprintf(f, L"SoundMan, EffectMan, AppTick, WorldTick, EngineEffectUpdate, AppBeforeRender, WorldBeforeRender, SortMid, 3DBeforeUpdateNode, UpdateSpriteAction, ");
			fwprintf(f, L"3DAfterUpdateNode, MovingBG, RenderCacheRenderTarget, RenderLowLayer, RenderBeforeMin, RenderMid, RenderAfterMid, RenderHeight, RenderMask, REnderWorldEnd, ");
			fwprintf(f, L"RenderUI, RenderBugInfo, RenderEnd, AniManGC, SoundManGC, ProcessMsg, MainLoopRunTask, ExecutorUpdate, FileIOUpdate, MsgBeginTask, ");
			fwprintf(f, L"MsgEndTask, BeginTimer250, AsureReg, AsureBackG, AsureMapObj, AsureDiff, AsureSyncLoad, AsureEleAL, AsureEffAL, AsureDelEle, ");
			fwprintf(f, L"AsureDelEfct, MsgEndT250\n");
			LogsVector::iterator it, ie;
			for (int i=0;i<3;++i)
			{
				switch (i)
				{
				case 0:
					fwprintf(f, L"[Min]\n");
					it = m_min.begin();
					ie = m_min.end();
					break;
				case 1:
					fwprintf(f, L"[Max]\n");
					it = m_max.begin();
					ie = m_max.end();
					break;
				case 2:
					fwprintf(f, L"[Avg]\n");
					it = m_total.begin();
					ie = m_total.end();
					break;
				}
				for (;it!=ie;++it)
				{
					fwprintf(f, L"%f, ", i==2?(*it)/m_count:(*it));
				}
				fwprintf(f, L"\n");
			}
			fwprintf(f, L"End........\n");
			fclose(f);
#endif
		}
	public:
		enum TUL_TYPES {
			TUL_BEGIN = 0,
			TUL_SOUNDMANAGER,
			TUL_EFFECTMANAGER,
			TUL_APPTICK,
			TUL_WORLDTICK,
			TUL_ENGINE_EFFECTUPDATE,
			TUL_APPBEFORERENDER,
			TUL_WORLDBEFORERENDER, //7
			TUL_SORTMID,//8
			TUL_3DBEFOREUPDATENODE,//9
			TUL_UPDATESPRITEACTION,//10
			TUL_3DAFTERUPDATENODE,//11
			TUL_MOVINGBACKGROUND,//12
			TUL_RENDERCACHERENDERTARGET, //13
			TUL_RENDERLOWLAYER,	//14
			TUL_RENDERBEFOREMID,	//15
			TUL_RENDERMID,	//16
			TUL_RENDERAFTERMID, //17
			TUL_RENDERHIGHT,	//18
			TUL_RENDERMASK,	//19
			TUL_RENDERWORLDEND,	//20
			TUL_RENDERUI,//21
			TUL_RENDERDEBUGINFO,//22
			TUL_RENDEREND,//23
			TUL_ANIMANAGERGC, //24
			TUL_SOUNDMANAGERGC,
			TUL_PROCESSMESSAGE,
			TUL_MAINLOOPRUNUSERTASK,
			TUL_EXECUTORUPDATE,
			TUL_FILEIOUPDATE,
			TUL_MSG_BEGIN_TASK,
			TUL_MSG_END_TASK,
			TUL_MSG_BEGIN_TIMER250,
			TUL_ASURE_REGION,
			TUL_ASURE_BACKGROUND,
			TUL_ASURE_MAPOBJS,
			TUL_ASURE_DIFF,
			TUL_ASURE_SYNCLOAD,
			TUL_ASURE_ELE_ASYNCLOAD,
			TUL_ASURE_EFFECT_ASYNCLOAD,
			TUL_ASURE_DEL_ELE,
			TUL_ASURE_DEL_EFCT,
			TUL_MSG_END_TIMER250,
		};

		enum {
			TUL_COUNT = TUL_MSG_END_TIMER250 + 1,
		};
#ifdef XPDBG_PRINT_FRAME_STATE

	private:
		struct Value 
		{
			__int64		logt;
			TUL_TYPES	tult;
		};
		typedef std::vector<Value> ValueVector;
		typedef std::vector<double> LogsVector;
		ValueVector	m_values;
		LogsVector m_total;
		LogsVector m_min;
		LogsVector m_max;
		int m_count;
		int m_fileIOCount;
		int m_fileIOSize;
	private:
		void LogAll( float v, bool isBigFrame)
		{
			ValueVector::const_iterator it = m_values.begin();
			Value last = *it;
			++ it;
			if( TUL_BEGIN != last.tult && TUL_PROCESSMESSAGE != last.tult)
			{
				XPLOG_FRAME_STATE(L"error begin with %d\n\n", last.tult);
				return;
			}
			++m_count;
			static double times[TUL_COUNT];
			for (int i=0;i<TUL_COUNT;++i)
			{
				times[i] = 0.0f;
			}

			ValueVector::iterator ite = m_values.end();
			for(; it != ite; ++it )
			{
				const Value& cur = *it;
				double time = static_cast<double>(cur.logt - last.logt) / v;
				int id = cur.tult-1;
				times[id] += time;
				m_total[id] += time;
				if (times[id] < m_min[id])
					m_min[id] = times[id];
				if (times[id] > m_max[id])
					m_max[id] = times[id];
				if (isBigFrame)
				{
					XPLOG_FULL_FRAME_STATE(L"%d, %f\n", cur.tult, time);
				}
				last = cur;
				
			}
			if (isBigFrame)
			{
				XPLOG_FULL_FRAME_STATE(L"End...\n");
				XPLOG_BIG_FRAME_STATE(L", %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %d, %d\n", 
					times[0], times[1], times[2], times[3], times[4], times[5], times[6], times[7], times[8], times[9], 
					times[10], times[11], times[12], times[13], times[14], times[15], times[16], times[17], times[18], times[19], 
					times[20], times[21], times[22], times[23], times[24], times[25], times[26], times[27], times[28], times[29],
					times[30], times[31], times[32], times[33], times[34], times[35], times[36], times[37], times[38], times[39], 
					times[40], times[41], m_fileIOCount, m_fileIOSize);
			}
		}
#endif
	public:
		inline void AddIOSize(int size)
		{
#ifdef XPDBG_PRINT_FRAME_STATE
			++m_fileIOCount;
			m_fileIOSize += size;
#endif
		}

		inline void Push( TUL_TYPES t)
		{
#ifdef XPDBG_PRINT_FRAME_STATE
			Value	v;
			v.logt = rdtsc();
			v.tult = t;
			m_values.push_back( v);
#endif
		}
		inline bool Log( int64 d, float v)
		{
#ifdef  XPDBG_PRINT_FRAME_STATE
			//if( d /v > 50)
			LogAll(v, d/v > 50);
			m_values.clear();
			m_fileIOCount = 0;
			m_fileIOSize = 0;
			return true;
			//m_values.clear();
#endif
			return false;
		}
	};
}