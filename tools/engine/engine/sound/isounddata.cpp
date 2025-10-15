#include "stdafx.h"
#include "isounddata.h"
#include "soundoggdata.h"
#include "soundwavedata.h"

namespace Nuclear
{
	ISoundData* ISoundData::GetSoundData(int type)
	{
		switch (type)
		{
		case XPSOUNDTYPE_WAVE:
			return new SoundWaveData();
			break;
		case XPSOUNDTYPE_OGG:
			return new SoundOggData();
			break;
		default:
			break;
		}
		return NULL;
	}
}
