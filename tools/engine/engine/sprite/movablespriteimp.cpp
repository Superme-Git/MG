#include "stdafx.h"
#include "..\iapp.h"
#include "..\engine\configmanager.h"
#include "..\renderer\renderer.h"
#include "..\world\world.h"
#include "sprite.h"
#include "movablespriteimp.h"



namespace Nuclear
{
	MovableSpriteImp::MovableSpriteImp(Sprite *pSprite, EngineBase *pEB)
		:m_pSprite(pSprite), m_pEB(pEB), direction(0,1), bOnly4Dir(false)
	{
		this->speed     = 160.0f / 1000;
		this->keepTrail = false;
		this->moveMask  = 0;
		this->moving    = false;
		this->setDirection(Nuclear::XPDIR_BOTTOM);

		this->updateActionCooldown = 0;
		this->movesuspended = false;
	}

	bool MovableSpriteImp::TakeMoveTrail(astar::Path & trail /*out*/)
	{
		this->__addtrail(this->getLocationDirection());
		if (this->trail.size() > 1) // 一个点尾迹没有意义。
		{
			// 连接路径。如果是第一次收集，拼接全部点，否则跳过连接点--这里不作无缝检查。
			astar::Path::iterator spliceIt = trail.empty() ? this->trail.begin() : ++this->trail.begin();
			trail.splice(trail.end(), this->trail, spliceIt, this->trail.end());
			this->trail.clear();
			this->__addtrail(this->getLocationDirection()); // 加入当前点不管是否移动中。
			return true;
		}
		return false;
	}

	void MovableSpriteImp::StopMove()
	{
		// 把当前位置加到尾迹中。
		this->__addtrail( this->getLocationDirection() );

		// 清除当前的路径
		this->apath.clear();
		this->path.clear();
#ifdef _DEBUG
		this->apathcopy.clear();
#endif
	}

	void MovableSpriteImp::setLocation(float x, float y)
	{
		// 清除尾迹
		this->trail.clear();

		// 清除当前的路径。
		this->apath.clear();
		this->path.clear();
#ifdef _DEBUG
		this->apathcopy.clear();
#endif

		this->location.x = x;
		this->location.y = y;
	}

	void MovableSpriteImp::__tickActionCooldown(int now)
	{
		if (updateActionCooldown == 0)
			return; // no cooldown

		if (now - updateActionCooldown >= 0)
		{
			updateActionCooldown = 0;
			this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE);
		}
	}

	void MovableSpriteImp::__setMoving(bool moving)
	{
		if (this->moving == moving)
			return; // nochange

		this->moving = moving;
		if (updateActionCooldown == 0)
		{
			if (moving)
			{
				if (eMovingCooldown == 0) // 常量比较，这样写应该会被优化
					this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE); // update immediately
				else
					updateActionCooldown = ::timeGetTime() + eMovingCooldown;
			}
			else
			{
				if (eStandCooldown == 0) // 常量比较，这样写应该会被优化
					this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE); // update immediately
				else
					updateActionCooldown = ::timeGetTime() + eStandCooldown;
			}
		}
		else
		{
			// cancel cooldown
			updateActionCooldown = 0;
		}
		/*/
		// update action if change
		if (this->moving != moving)
		{
		this->moving = moving;
		this->m_pEB->GetApp()->OnUpdateSpriteAction(this);
		}
		// */
	}

	void MovableSpriteImp::MoveTo(astar::Path & trail)
	{
		if (!trail.empty())
		{
			this->apath.clear(); // 播放模式，清除之。

			// 判断新增路径是否可以和当前路径无缝接上。如果不能，则瞬移过去。
			astar::Point back =  this->path.empty() ? this->getLocationDirection() : this->path.back();
			if (false == back.trail_equals(trail.front()))
			{
				astar::Point loc = WorldLogicCoord::logic2world( trail.front());
				this->setLocation(loc.x, loc.y); // setLocation will clear any path
			}

			// 连接路径。不包括连接点。
			this->path.splice(this->path.end(), trail, ++trail.begin(), trail.end());
			this->__setMoving(true); // 当尾迹只是转方向时，是否需要把 Action 切换省略掉？
		}
		// else do nothing
	}

	void MovableSpriteImp::MoveTo(int targetX, int targetY, int range, const CSIZE * size)
	{
		if (this->m_pEB->GetXPWorld() && !this->m_pEB->GetXPWorld()->IsMapLoaded())
			return;

		if (astar::PathFinder * finder = this->m_pEB->GetPathFinder())
		{
			finder->getAMap().setMask(this->moveMask);
			if (size)
				finder->getAMap().setRect(finder->world2grid(WorldLogicCoord::world2logic( this->location.x, this->location.y)), finder->world2grid(WorldLogicCoord::world2logic( (float)size->x, (float)size->y)));
			else
				finder->getAMap().setRectAll();

			Location target = WorldLogicCoord::world2logic( targetX, targetY);
			// 由于需要满足GetPath()获取全路径, 这里limit用0
			astar::eFindResult rc = finder->find(this->getLocationDirection(), target.x, target.y,
				this->m_pEB->GetConfigManager()->GetSpriteMoveSmoothLimit(), this->apath, this->path, range, this->__apathcopy());

			switch (rc)
			{
			case astar::eFindSuccess:
				this->__addtrail(this->getLocationDirection());
				this->__setMoving(true);
				break;

			case astar::eFindInrange0:
			case astar::eFindInrange:
				// 调用时，目标已在范围内，强迫回调一次 OnUpdateSpriteAction。
				// 移动攻击目标时，技能在停止到达目标时施放，如果调用MoveTo时，目标已在范围，补发一次回调。
				// 否则逻辑层无法得到正确释放时间点。站立时攻击范围内的目标的处理方式和移动到达目标的处理方式保持一致。
				this->updateActionCooldown = 0;
				this->moving = true;
				this->__setMoving(false);
				break;
			}
		}
	}

	bool MovableSpriteImp::InAttackRange( const Nuclear::Location & target, int range, unsigned int mask)
	{
		if (this->m_pEB->GetXPWorld() && !this->m_pEB->GetXPWorld()->IsMapLoaded())
			return false;

		if (astar::PathFinder * finder = this->m_pEB->GetPathFinder())
		{
			FPOINT _loc = WorldLogicCoord::world2logic( this->location);
			FPOINT _target = WorldLogicCoord::world2logic( target.ToFPOINT());

			if ( Nuclear::distance(_loc, _target) > (float)range )
				return false;

			finder->getAMap().setMask(mask);
			return astar::eFindFail != finder->linePixel((int)_loc.x, (int)_loc.y, (int)_target.x, (int)_target.y);
		}
		return range < 60; // 无法判断路径是否攻击可达时，总是使用这个魔数。
	}

	void MovableSpriteImp::KeepMoveTrail(bool keep)
	{
		this->keepTrail = keep;
		if (keep)
			this->__addtrail(this->getLocationDirection());
		else
			this->trail.clear();
	}

	void MovableSpriteImp::__nextWaypoint()
	{
		if (this->m_pEB->GetXPWorld() && !this->m_pEB->GetXPWorld()->IsMapLoaded())
			return;

		if (this->keepTrail)
			this->trail.splice(this->trail.end(), this->path, this->path.begin());
		else
			this->path.pop_front();

		if (astar::PathFinder * finder = m_pEB->GetPathFinder())
		{
			finder->getAMap().setMask(this->moveMask);
			finder->smoothness2(this->getLocationDirection(), apath,
				this->m_pEB->GetConfigManager()->GetSpriteMoveSmoothLimit(), path);
		}

		this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVEINFLEXION);
	}

	// 获取当前path的一个拷贝, 在使用MoveTo后调用可得到即将要走的路径(逻辑坐标)
	void MovableSpriteImp::GetPath(astar::Path & path)
	{ 
		// 由于小地图需要得到全路径拷贝, 所以在客户端需要SetSpriteMoveSmoothLimit(0);
		path.clear();
		if ( !this->path.empty())
		{
			path.insert( path.begin(), this->path.begin(), this->path.end());
		}
		if ( !path.empty())
		{
			Nuclear::Location logical = this->getLogicLocation();
			path.insert( path.begin(), astar::Point( static_cast<float>(logical.x), static_cast<float>(logical.y)) ); 
		}
	}

	XPDIRECTION MovableSpriteImp::getDirection() const
	{
		if (IsOnly4Dir())
		{
			return direction.x > 0
				? ( direction.y > 0 ? Nuclear::XPDIR_BOTTOMRIGHT : Nuclear::XPDIR_TOPRIGHT)
				: ( direction.y > 0 ? Nuclear::XPDIR_BOTTOMLEFT : Nuclear::XPDIR_TOPLEFT);
		}

		// 根据 form 到 to 与 X 轴的夹角判断方向。
		// 不使用 sqrt 计算，这样会丢失符号，返回时根据 dx 或者 dy 的符号再判断一次。

		// cos(角度) * cos(角度)
		float cos2 = direction.x * direction.x / (direction.x * direction.x + direction.y * direction.y);
		//if (Nuclear::isnanf(cos2)) return Nuclear::XPDIR_COUNT;

		if (cos2 > 0.8535534) // abs(角度) < 22.5. 0.8535534 = cos(22.5) * cos(22.5)
			return direction.x > 0 ? Nuclear::XPDIR_RIGHT : Nuclear::XPDIR_LEFT;

		if (cos2 > 0.1464466) // abs(角度) < 67.5. 0.1464466 = cos(67.5) * cos(67.5)
			return direction.x > 0
			? ( direction.y > 0 ? Nuclear::XPDIR_BOTTOMRIGHT : Nuclear::XPDIR_TOPRIGHT)
			: ( direction.y > 0 ? Nuclear::XPDIR_BOTTOMLEFT : Nuclear::XPDIR_TOPLEFT);

		return direction.y > 0 ? Nuclear::XPDIR_BOTTOM: Nuclear::XPDIR_TOP;
	}

	void MovableSpriteImp::tick(int now, int deltaTime)
	{
		m_pSprite->UpdateAction();

		// movesuspended <-- here

		this->__tickActionCooldown(now);

		if (this->path.empty())
			return this->__setMoving(false);

		// 暂停判断放。
		//    如果此时精灵刚好处于停止的colldown或者将要停止，允许相关状态（moving）改变和触发事件（OnUpdateAction）。
		//    避免恢复移动时，动画帧发生跳变：先是'跑'，然后马上又'停'了。
		//    坏处就是 movesuspended 被设为 true以后，停止的状态变化和事件仍然会发生。
		//    *** 把这个判断放在上面那个点，逻辑层在设置suspended为true以后，会很安静。
		if ( this->movesuspended )
			return;

		float distanceDelta = this->speed * deltaTime;
		do
		{
			FPOINT locationNext(this->path.begin()->x, this->path.begin()->y); // 逻辑坐标, __currentWaypoint
			this->direction = WorldLogicCoord::logic2world( this->path.begin()->direction);
			FPOINT logicloc = WorldLogicCoord::world2logic(this->location);
			float distanceNext = distance(logicloc, locationNext); // 只处理2d，忽略z。
			distanceDelta -= distanceNext;
			if (distanceDelta <= 0)
			{
				float R = ::abs(distanceDelta / distanceNext);
				if (!Nuclear::isnanf(R))
				{
					//locationNext = __logic2world(locationNext);
					//this->location.x = R * (location.x - locationNext.x) + locationNext.x;
					//this->location.y = R * (location.y - locationNext.y) + locationNext.y;
					logicloc.x = R * (logicloc.x - locationNext.x) + locationNext.x;
					logicloc.y = R * (logicloc.y - locationNext.y) + locationNext.y;
					this->location = WorldLogicCoord::logic2world( logicloc);
					break;
				}
			}
			// 到当前目标点距离不够，继续往下一点走。
			this->location = WorldLogicCoord::logic2world(locationNext);
			this->__nextWaypoint(); // move waypoint from path to trail
		}
		while (false == this->path.empty());


	}

	void MovableSpriteImp::RenderPath()
	{
		const FPOINT view = this->m_pEB->GetViewport().GetPos().ToFPOINT();
		Renderer * renderer = this->m_pEB->GetRenderer();

		// draw Trail
		if (this->m_pEB->GetConfigManager()->IsShowSpriteTrail() && !this->trail.empty())
		{
			float fScale = m_pEB->GetWorldScale();
			std::vector<FRECT> lines;
			std::vector<FRECT> rects;

			astar::Path::const_iterator it = this->trail.begin();
			FPOINT from(it->x, it->y);
			from = WorldLogicCoord::logic2world( from) - view;
			from *= fScale;
			rects.push_back(FRECT(from, astar::PathFinder::eGridSize / 2));
			for (++it; it != this->trail.end(); ++it)
			{
				FPOINT to(it->x, it->y);
				to = WorldLogicCoord::logic2world( to) - view;
				to *= fScale;
				lines.push_back(FRECT(from, to));
				rects.push_back(FRECT(to, astar::PathFinder::eGridSize / 2));
				from = to;
			}
			FPOINT to = this->location - view;
			to *= fScale;
			lines.push_back(FRECT(from, to));
			rects.push_back(FRECT(to, astar::PathFinder::eGridSize / 2));
			renderer->DrawLine(&lines[0], int(lines.size()), 0xffffffff);
			renderer->DrawBox(&rects[0], (int)rects.size(),  0x7fffff00);
		}

		// draw Path
		if (this->m_pEB->GetConfigManager()->IsShowSpritePath() && !this->path.empty())
		{
			float fScale = m_pEB->GetWorldScale();
			std::vector<FRECT> lines;
			std::vector<FRECT> rects;

			FPOINT from = this->location - view;
			from *= fScale;
			rects.push_back(FRECT(from, astar::PathFinder::eGridSize / 2));
			for (astar::Path::const_iterator it = this->path.begin(); it != this->path.end(); ++it)
			{
				FPOINT to(it->x, it->y);
				to = WorldLogicCoord::logic2world( to) - view;
				to *= fScale;
				lines.push_back(FRECT(from, to));
				rects.push_back(FRECT(to, astar::PathFinder::eGridSize / 2));
				from = to;
			}
			renderer->DrawLine(&lines[0], int(lines.size()), 0xffff0000);
			renderer->DrawBox(&rects[0], (int)rects.size(),  0x7fffff00);
		}

#ifdef _DEBUG
		// draw apathOrigin. 用来比较。
		if (astar::PathFinder * finder = this->m_pEB->GetPathFinder())
			if (!this->apathcopy.empty()) // 可达图坐标。格子单位
			{
				float fScale = m_pEB->GetWorldScale();
				std::vector<FRECT> lines;
				astar::APath::const_iterator it = this->apathcopy.begin();
				astar::Point from = WorldLogicCoord::logic2world( finder->grid2world(*it));
				for (++it; it != this->apathcopy.end(); ++it)
				{
					astar::Point to = WorldLogicCoord::logic2world( finder->grid2world(*it));
					lines.push_back((FRECT(from.x, from.y, to.x, to.y) - view) * fScale);
					from = to;
				}
				if (!lines.empty())
					renderer->DrawLine(&lines[0], int(lines.size()), 0x7fffff00);
			}
#endif // _DEBUG
	}
};
