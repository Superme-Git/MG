//////////////////////////////////////////////////////////////////////////
// File:		nuAccelerateUpdater.h
// Author:	Ñî´ºÀ×
// Date:		2016/04/14
//////////////////////////////////////////////////////////////////////////

#ifndef _nuAccelerateUpdater_h_
#define _nuAccelerateUpdater_h_

#include "nuxpmaths.h"

namespace Nuclear
{
	class AccelerateUpdater
	{
	private:
		bool mbInited;

		NuclearFPoint mTargetPosition, mFollowPosition;
		float mMaxDist;
		float mVelocity, mAccelerate;

	public:
		AccelerateUpdater();

		bool isInited() const { return mbInited; }
		void init(const NuclearLocation& targetPos, const NuclearLocation& followPos, float maxDist);
		void reset();

		void update(float dt, const NuclearLocation& targetPos);

		const NuclearLocation getFollowPosition() const;

	private:
		void updateFollowPosition(float dt);
		void updateFollowSpeed(float dt, float targetVel);
	};
}

#endif