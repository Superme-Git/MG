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
		if (this->trail.size() > 1) // һ����β��û�����塣
		{
			// ����·��������ǵ�һ���ռ���ƴ��ȫ���㣬�����������ӵ�--���ﲻ���޷��顣
			astar::Path::iterator spliceIt = trail.empty() ? this->trail.begin() : ++this->trail.begin();
			trail.splice(trail.end(), this->trail, spliceIt, this->trail.end());
			this->trail.clear();
			this->__addtrail(this->getLocationDirection()); // ���뵱ǰ�㲻���Ƿ��ƶ��С�
			return true;
		}
		return false;
	}

	void MovableSpriteImp::StopMove()
	{
		// �ѵ�ǰλ�üӵ�β���С�
		this->__addtrail( this->getLocationDirection() );

		// �����ǰ��·��
		this->apath.clear();
		this->path.clear();
#ifdef _DEBUG
		this->apathcopy.clear();
#endif
	}

	void MovableSpriteImp::setLocation(float x, float y)
	{
		// ���β��
		this->trail.clear();

		// �����ǰ��·����
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
				if (eMovingCooldown == 0) // �����Ƚϣ�����дӦ�ûᱻ�Ż�
					this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE); // update immediately
				else
					updateActionCooldown = ::timeGetTime() + eMovingCooldown;
			}
			else
			{
				if (eStandCooldown == 0) // �����Ƚϣ�����дӦ�ûᱻ�Ż�
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
			this->apath.clear(); // ����ģʽ�����֮��

			// �ж�����·���Ƿ���Ժ͵�ǰ·���޷���ϡ�������ܣ���˲�ƹ�ȥ��
			astar::Point back =  this->path.empty() ? this->getLocationDirection() : this->path.back();
			if (false == back.trail_equals(trail.front()))
			{
				astar::Point loc = WorldLogicCoord::logic2world( trail.front());
				this->setLocation(loc.x, loc.y); // setLocation will clear any path
			}

			// ����·�������������ӵ㡣
			this->path.splice(this->path.end(), trail, ++trail.begin(), trail.end());
			this->__setMoving(true); // ��β��ֻ��ת����ʱ���Ƿ���Ҫ�� Action �л�ʡ�Ե���
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
			// ������Ҫ����GetPath()��ȡȫ·��, ����limit��0
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
				// ����ʱ��Ŀ�����ڷ�Χ�ڣ�ǿ�Ȼص�һ�� OnUpdateSpriteAction��
				// �ƶ�����Ŀ��ʱ��������ֹͣ����Ŀ��ʱʩ�ţ��������MoveToʱ��Ŀ�����ڷ�Χ������һ�λص���
				// �����߼����޷��õ���ȷ�ͷ�ʱ��㡣վ��ʱ������Χ�ڵ�Ŀ��Ĵ���ʽ���ƶ�����Ŀ��Ĵ���ʽ����һ�¡�
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
		return range < 60; // �޷��ж�·���Ƿ񹥻��ɴ�ʱ������ʹ�����ħ����
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

	// ��ȡ��ǰpath��һ������, ��ʹ��MoveTo����ÿɵõ�����Ҫ�ߵ�·��(�߼�����)
	void MovableSpriteImp::GetPath(astar::Path & path)
	{ 
		// ����С��ͼ��Ҫ�õ�ȫ·������, �����ڿͻ�����ҪSetSpriteMoveSmoothLimit(0);
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

		// ���� form �� to �� X ��ļн��жϷ���
		// ��ʹ�� sqrt ���㣬�����ᶪʧ���ţ�����ʱ���� dx ���� dy �ķ������ж�һ�Ρ�

		// cos(�Ƕ�) * cos(�Ƕ�)
		float cos2 = direction.x * direction.x / (direction.x * direction.x + direction.y * direction.y);
		//if (Nuclear::isnanf(cos2)) return Nuclear::XPDIR_COUNT;

		if (cos2 > 0.8535534) // abs(�Ƕ�) < 22.5. 0.8535534 = cos(22.5) * cos(22.5)
			return direction.x > 0 ? Nuclear::XPDIR_RIGHT : Nuclear::XPDIR_LEFT;

		if (cos2 > 0.1464466) // abs(�Ƕ�) < 67.5. 0.1464466 = cos(67.5) * cos(67.5)
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

		// ��ͣ�жϷš�
		//    �����ʱ����պô���ֹͣ��colldown���߽�Ҫֹͣ���������״̬��moving���ı�ʹ����¼���OnUpdateAction����
		//    ����ָ��ƶ�ʱ������֡�������䣺����'��'��Ȼ��������'ͣ'�ˡ�
		//    �������� movesuspended ����Ϊ true�Ժ�ֹͣ��״̬�仯���¼���Ȼ�ᷢ����
		//    *** ������жϷ��������Ǹ��㣬�߼���������suspendedΪtrue�Ժ󣬻�ܰ�����
		if ( this->movesuspended )
			return;

		float distanceDelta = this->speed * deltaTime;
		do
		{
			FPOINT locationNext(this->path.begin()->x, this->path.begin()->y); // �߼�����, __currentWaypoint
			this->direction = WorldLogicCoord::logic2world( this->path.begin()->direction);
			FPOINT logicloc = WorldLogicCoord::world2logic(this->location);
			float distanceNext = distance(logicloc, locationNext); // ֻ����2d������z��
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
			// ����ǰĿ�����벻������������һ���ߡ�
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
		// draw apathOrigin. �����Ƚϡ�
		if (astar::PathFinder * finder = this->m_pEB->GetPathFinder())
			if (!this->apathcopy.empty()) // �ɴ�ͼ���ꡣ���ӵ�λ
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
