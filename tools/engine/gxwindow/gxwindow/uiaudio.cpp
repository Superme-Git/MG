#include "gxwindowdef.h"

namespace GXWINDOW {

	namespace INLINECLASS {

		static class CHollowAudio : public CAudioInterface
		{
		public:
			CHollowAudio() 
			{}
			virtual ~CHollowAudio() 
			{}
		public:
			virtual void Play( int soundid)
			{}
		}	g_DefaultUIAudioInterface;
	
		static CAudioInterface*		g_CurrentAudioInterface = &g_DefaultUIAudioInterface;
	} // namespace INLINECLASS {

	CAudioInterface::CAudioInterface()
	{}
	CAudioInterface::~CAudioInterface()
	{}

	void CAudioInterface::SetInstance( CAudioInterface& ai)
	{
		INLINECLASS::g_CurrentAudioInterface = &ai;
		if( NULL == INLINECLASS::g_CurrentAudioInterface)
			INLINECLASS::g_CurrentAudioInterface = &INLINECLASS::g_DefaultUIAudioInterface;
	}

	CAudioInterface& CAudioInterface::GetInstance()
	{
		return *INLINECLASS::g_CurrentAudioInterface;
	}

} // namespace GXWINDOW {
