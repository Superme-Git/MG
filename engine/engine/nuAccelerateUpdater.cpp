//////////////////////////////////////////////////////////////////////////
// File:		nuAccelerateUpdater.cpp
// Author:	杨春雷
// Date:		2016/04/14
//////////////////////////////////////////////////////////////////////////

#include "nuAccelerateUpdater.h"

namespace Nuclear
{
	// 加速度位移公式：s = v * t + 0.5 * a * t * t

	// 根据目标速度、追踪者当前速度，计算出追至两者距离保持 maxDist 不变，所需加速度和时间
	void calAccelerate(float targetVel, float followVel, float maxDist, float* pAccelerate, float* pTime)
	{
		float time = 0;
		float accel = 0;

		if (targetVel != followVel)
		{
			if (maxDist == 0)
			{
				maxDist = 0.001;
			}

			time = 2 * maxDist / (targetVel - followVel);
			accel = (targetVel - followVel) / time;
		}

		if (pAccelerate)
		{
			*pAccelerate = accel;
		}

		if (pTime)
		{
			*pTime = time;
		}
	}

	AccelerateUpdater::AccelerateUpdater()
		: mbInited(false)
	{}

	void AccelerateUpdater::init(const NuclearLocation& targetPos, const NuclearLocation& followPos, float maxDist)
	{
		mTargetPosition = targetPos;
		mFollowPosition = followPos;
		mMaxDist = maxDist;
		mVelocity = 0;
		mAccelerate = 0;
		mbInited = true;
	}

	static bool sbSmooth = false;
	std::vector<NuclearFPoint> sFollowPosArr;

	void AccelerateUpdater::reset()
	{
		mMaxDist = 0;
		mVelocity = 0;
		mAccelerate = 0;
		mbInited = false;
		sFollowPosArr.clear();
	}

	void AccelerateUpdater::update(float dt, const NuclearLocation& targetPos)
	{
		if (!mbInited)
		{
			return;
		}

		float targetVel = sqrtf(mTargetPosition.Dis2(targetPos)) / dt;  // 目标速度
		float oldDist = sqrtf(mFollowPosition.Dis2(mTargetPosition));  // 旧距离

		// 更新过去 dt 时间内的值的变化
		updateFollowPosition(dt);
		updateFollowSpeed(dt, targetVel);

		mTargetPosition = NuclearFPoint(targetPos.x, targetPos.y);

		if (mFollowPosition == mTargetPosition)
		{
			mAccelerate = 0;
		}
		else
		{
			float dist = sqrtf(mFollowPosition.Dis2(mTargetPosition));

			if (dist > mMaxDist)
			{
				//cocos2d::CCLog("[AccelerateUpdater::update] follower is too far away from target. (dist=%f, mMaxDist=%f) will directly set position.", dist, mMaxDist);

				NuclearFPoint vTargetToFollow = mFollowPosition - mTargetPosition;
				vTargetToFollow.normalize();
				mFollowPosition = mTargetPosition + vTargetToFollow * mMaxDist;
			}
			else
			{
				float oldAccel = mAccelerate;

				if (dist > oldDist)
				{
					//cocos2d::CCLog("[AccelerateUpdater::update] distance larger. calculate accelerate again.");

					calAccelerate(targetVel, mVelocity, mMaxDist - dist, &mAccelerate, NULL);
				}
				else if (dist < oldDist)
				{
					if (targetVel == 0)
					{
						if (mVelocity > 0)
						{
							calAccelerate(0, mVelocity, dist, &mAccelerate, NULL);
							mAccelerate = -mAccelerate;
						}
					}
					else
					{
						mAccelerate = mAccelerate * 0.8;
					}
				}
				else
				{
					if (mVelocity == 0 && mAccelerate == 0)
					{
						//calAccelerate(0, mVelocity, dist, &mAccelerate, &mMaxTime);
						//if (mAccelerate > 0)
						//{
						//	mAccelerate = -mAccelerate;
						//}
						//mCurTime = 0;
					}
				}
			}
		}

		static float fVel = mVelocity;
		static float fAccel = mAccelerate;
		if (fVel != mVelocity || fAccel || mAccelerate)
		{
			fVel = mVelocity;
			fAccel = mAccelerate;
			//cocos2d::CCLog("[AccelerateUpdater::update] mVelocity=%f, mAccelerate=%f", mVelocity, mAccelerate);
			//cocos2d::CCLog("[AccelerateUpdater::update] ================================");
		}

		if (sbSmooth)
		{
			sFollowPosArr.push_back(mFollowPosition);

			if (sFollowPosArr.size() == 101)
			{
				sFollowPosArr.erase(sFollowPosArr.begin());
			}
		}
	}

	const NuclearLocation AccelerateUpdater::getFollowPosition() const
	{
		if (sbSmooth)
		{
			int count = sFollowPosArr.size();
			if (count > 1)
			{
				NuclearFPoint pos;
				for (int i = 0; i < count - 1; ++i)
				{
					const NuclearFPoint& itPos = sFollowPosArr[i];
					pos += itPos;
				}

				pos /= (count - 1);

				pos = pos * 0.2 + mFollowPosition * 0.8;

				return pos;
			}
		}
		
		return mFollowPosition;
	}

	void AccelerateUpdater::updateFollowPosition(float dt)
	{
		if (mFollowPosition != mTargetPosition)
		{
			float moveDist = mVelocity * dt + 0.5 * mAccelerate * dt * dt;

			if (moveDist > 0)
			{
				NuclearFPoint vFollowToTarget = mTargetPosition - mFollowPosition;
				float dist = sqrtf(vFollowToTarget.x * vFollowToTarget.x + vFollowToTarget.y * vFollowToTarget.y);
				vFollowToTarget.x /= dist;
				vFollowToTarget.y /= dist;

				if (moveDist >= dist)
				{
					mFollowPosition = mTargetPosition;
				}
				else
				{
					mFollowPosition = mFollowPosition + vFollowToTarget * moveDist;
				}
			}
		}
	}

	void AccelerateUpdater::updateFollowSpeed(float dt, float targetVel)
	{
		if (mAccelerate != 0)
		{
			float oldVel = mVelocity;

			mVelocity = mVelocity + mAccelerate * dt;

			if (targetVel == 0)
			{
				if (oldVel > 0 && mAccelerate < 0 && mVelocity < 0)
				{
					mVelocity = 0;
					mAccelerate = 0;
				}
				else if (oldVel < 0 && mAccelerate > 0 && mVelocity > 0)
				{
					mVelocity = 0;
					mAccelerate = 0;
				}
			}
			else if (targetVel > 0)
			{
				if (mVelocity > targetVel && mAccelerate > 0)
				{
					mVelocity = targetVel;
					mAccelerate = 0;
				}
				if (mVelocity < 0)
				{
					mVelocity = 0;

					if (mAccelerate < 0)
					{
						mAccelerate = 0;
					}
				}
			}
		}
	}
}