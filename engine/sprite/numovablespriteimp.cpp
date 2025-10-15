#include "../nuiapp.h"
#include "../engine/nuconfigmanager.h"
#include "../renderer/nurenderer.h"
#include "../world/nuworld.h"
#include "nusprite.h"
#include "numovablespriteimp.h"


namespace Nuclear
{
	MovableSpriteImp::MovableSpriteImp(Sprite *pSprite, EngineBase *pEB)
		:m_pSprite(pSprite), m_pEB(pEB), direction(0, 1), iTurnDir(8)
	{
		this->speed     = 160.0f / 1000;
		this->keepTrail = false;
		this->moveMask  = 0;
		this->moving    = false;
		this->setDirection(Nuclear::XPDIR_BOTTOM);

		this->updateActionCooldown = 0;
		this->movesuspended = false;
		this->needCurveMove = false;
		this->straightMove  = false;
		this->accumulatetime = 0;
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
	}

	void MovableSpriteImp::setLocation(float x, float y)
	{
		// ���β��
		this->trail.clear();

		// �����ǰ��·����
		this->apath.clear();
		this->path.clear();

		this->location.x = x;
		this->location.y = y;
	}

	void MovableSpriteImp::__tickActionCooldown(int now)
	{
		if (updateActionCooldown == 0) return;

		if (now - updateActionCooldown >= 0)
		{
			updateActionCooldown = 0;
			this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE);
		}
	}

	void MovableSpriteImp::__setMoving(bool moving)
	{
		if (this->moving == moving) return;

		this->moving = moving;
		if (updateActionCooldown == 0)
		{
			if (moving)
			{
				if (eMovingCooldown == 0) // �����Ƚϣ�����дӦ�ûᱻ�Ż�
					this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE); // update immediately
				else
					updateActionCooldown = GetMilliSeconds() + eMovingCooldown;
			}
			else
			{
				if (eStandCooldown == 0) // �����Ƚϣ�����дӦ�ûᱻ�Ż�
					this->m_pEB->GetApp()->OnUpdateSpriteAction(m_pSprite, XPUSAT_MOVESTATE); // update immediately
				else
					updateActionCooldown = GetMilliSeconds() + eStandCooldown;
			}
		}
		else
		{
			updateActionCooldown = 0;
		}
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
				astar::Point loc = NuclearWorldLogicCoord::logic2world( trail.front());
				this->setLocation(loc.x, loc.y); // setLocation will clear any path
			}

			// ����·�������������ӵ㡣
			this->path.splice(this->path.end(), trail, ++trail.begin(), trail.end());
			this->__setMoving(true); // ��β��ֻ��ת����ʱ���Ƿ���Ҫ�� Action �л�ʡ�Ե���
		}
	}

	void MovableSpriteImp::MoveTo(int targetX, int targetY, int range, const CSIZE * size, bool straight)
	{
		if (this->m_pEB->GetXPWorld() && !this->m_pEB->GetXPWorld()->IsMapLoaded())
			return;

		//straightMove = straight;
		//if (!straightMove)//�Զ�Ѱ·
		//{
			if (astar::PathFinder * finder = this->m_pEB->GetPathFinder())
			{
				if (straight)
				{
					finder->getAMap().setMask(0);
				}
				else
				{
					finder->getAMap().setMask(this->moveMask);
				}

				if (size)
					finder->getAMap().setRect(finder->world2grid(NuclearWorldLogicCoord::world2logic( this->location.x, this->location.y)), finder->world2grid(NuclearWorldLogicCoord::world2logic( (float)size->x, (float)size->y)));
				else
					finder->getAMap().setRectAll();

				NuclearLocation target = NuclearWorldLogicCoord::world2logic( targetX, targetY);
				// ������Ҫ����GetPath()��ȡȫ·��, ����limit��0
				astar::eFindResult rc = finder->find(this->getLocationDirection(), target.x, target.y,
													 m_pEB->GetConfigManager()->GetSpriteMoveSmoothLimit(), this->apath, this->path, range, NULL);
				int szpth = this->path.size();
				astar::Path::iterator i = this->path.begin();
				astar::Path::iterator ed = this->path.end();
				for( ; i != ed; i++)
				{
					int x,y;
					x = i->x;
					y = i->y;
				}
            
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
		//}
		//else
		//{
		//	NuclearLocation target = NuclearWorldLogicCoord::world2logic(targetX, targetY);
		//	//NuclearLocation source = NuclearWorldLogicCoord::world2logic((int)location.x, (int)location.y);
		//	//astar::Point ptSrc(source.x, source.y);
		//	astar::Point ptTarget(target.x, target.y);
		//	ptTarget.direction = astar::Direction(getLocationDirection(), target.x, target.y);
		//	this->path.push_back(ptTarget);
		//	this->__addtrail(this->getLocationDirection());
		//	this->__setMoving(true);
		//}

		startPos = this->location;
	}

	bool MovableSpriteImp::InAttackRange( const Nuclear::NuclearLocation & target, int range, unsigned int mask)
	{
		if (this->m_pEB->GetXPWorld() && !this->m_pEB->GetXPWorld()->IsMapLoaded())
			return false;

		if (astar::PathFinder * finder = this->m_pEB->GetPathFinder())
		{
			NuclearFPoint _loc = NuclearWorldLogicCoord::world2logic( this->location);
			NuclearFPoint _target = NuclearWorldLogicCoord::world2logic( target.ToFPOINT());

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
		if (!this->path.empty())
		{
			path.insert(path.begin(), this->path.begin(), this->path.end());
		}
		if (!path.empty())
		{
			Nuclear::NuclearLocation logical = this->getLogicLocation();
			path.insert(path.begin(), astar::Point(static_cast<float>(logical.x), static_cast<float>(logical.y)));
		}
	}

	NuclearDirection MovableSpriteImp::getDirection() const
	{
		eNuclearWorldMode worldMode = GetEngine()->GetWorld()->GetWorldMode();		
		bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;
		if (bIsInBattle)//�Ƿ���ս��ģʽ֮��
		{
			if ( iTurnDir == 4)
			{
				return direction.x > 0
					? (direction.y > 0 ? Nuclear::XPDIR_BOTTOMRIGHT : Nuclear::XPDIR_TOPRIGHT)
					: (direction.y > 0 ? Nuclear::XPDIR_BOTTOMLEFT : Nuclear::XPDIR_TOPLEFT);
			}
		}
		else
		{
			if (iTurnDir == 4)
			{
				return direction.x > 0
					? (direction.y > 0 ? Nuclear::XPDIR_BOTTOMRIGHT : Nuclear::XPDIR_TOPRIGHT)
					: (direction.y > 0 ? Nuclear::XPDIR_BOTTOMLEFT : Nuclear::XPDIR_TOPLEFT);
			}
			else if (iTurnDir == 2)
			{
				return direction.x > 0 ? Nuclear::XPDIR_BOTTOMRIGHT : Nuclear::XPDIR_TOPLEFT;
			}
			//else if (iTurnDir == 1)
			//{
			//	return Nuclear::XPDIR_RIGHT;
			//}
		}

		return TransVector2XPDirection(direction);
	}

	void MovableSpriteImp::tick(int now, int deltaTime)
	{
		World* world = static_cast<World*>(GetEngine()->GetWorld());
		eNuclearWorldMode worldMode = world->GetWorldMode();
		bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;

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

		astar::Path::iterator ed = this->path.end(); ed--;		
		NuclearFPoint endLocation(ed->x, ed->y); //�߼�����
		NuclearFPoint endPos = NuclearWorldLogicCoord::logic2world(endLocation);//��������
		float fDist = 0.0f;
		NuclearFPoint vDir, midPos;
		bool bCurveMove = bIsInBattle && needCurveMove;//��ս��ģʽ������Ҫ���л����ƶ�
		if (bCurveMove)
		{
			vDir.x = endPos.x - startPos.x;
			vDir.y = endPos.y - startPos.y;
			fDist = 0.5f*sqrtf(vDir.x*vDir.x + vDir.y*vDir.y);
			midPos = NuclearFPoint(0.5f*(endPos.x + startPos.x), 0.5f*(endPos.y + startPos.y));
			vDir.normalize();
		}

		//accumulatetime += deltaTime;
		float deltaDist = this->speed * deltaTime;
		float oneStep = deltaDist;
		do
		{
			NuclearFPoint nextPos(this->path.begin()->x, this->path.begin()->y); // �߼�����, __currentWaypoint
			if (!bIsInBattle)//����ս��ģʽ֮��
			{
				this->direction = NuclearWorldLogicCoord::logic2world(this->path.begin()->direction);
			}

			NuclearFPoint curPos = NuclearWorldLogicCoord::world2logic(this->location);
			float distNext = distance(curPos, nextPos); // ֻ����2d������z��
			deltaDist -= distNext;
			float checkLength = 0.0f;
			if (bIsInBattle)
				checkLength = -oneStep;
			if (deltaDist <= checkLength)
			{
				float R = deltaDist / distNext;
				R = (R >= 0.0f) ? R : -R;
				if (!Nuclear::isnanf(R))
				{
					curPos.x = R * (curPos.x - nextPos.x) + nextPos.x;
					curPos.y = R * (curPos.y - nextPos.y) + nextPos.y;
					NuclearFPoint targetPos = NuclearWorldLogicCoord::logic2world(curPos);
					if (bCurveMove)//��ս��ģʽ������Ҫ���л����ƶ�
					{
						NuclearFPoint v(0.0f, 0.0f);
						float fS = 1.0f - sqrtf(targetPos.Dis2(midPos)) / fDist;
						if (vDir.x > 0.0f)
						{
							v.x = vDir.x*cos(-FIRE_HALFPI) - vDir.y*sin(-FIRE_HALFPI);
							v.y = vDir.x*sin(-FIRE_HALFPI) + vDir.y*cos(-FIRE_HALFPI);
						}
						else
						{
							v.x = vDir.x*cos(FIRE_HALFPI) - vDir.y*sin(FIRE_HALFPI);
							v.y = vDir.x*sin(FIRE_HALFPI) + vDir.y*cos(FIRE_HALFPI);
						}

						targetPos += 0.05f * deltaTime * sqrtf(fS) * world->GetCurveMoveHeight() * v;
					}
					//if (this->location.Dis2(targetPos) >= 1.0f)
					{
						//accumulatetime = 0;
						this->location = targetPos;
					}
					break;
				}
			}

			// ����ǰĿ�����벻������������һ���ߡ�
			this->location = NuclearWorldLogicCoord::logic2world(nextPos);
			this->__nextWaypoint(); // move waypoint from path to trail
		} while (!this->path.empty());
	}

	void MovableSpriteImp::RenderPath()
	{
		const NuclearFPoint view = this->m_pEB->GetViewport().GetPos().ToFPOINT();
		Renderer * renderer = this->m_pEB->GetRenderer();

		// draw Trail
		if (this->m_pEB->GetConfigManager()->IsShowSpriteTrail() && !this->trail.empty())
		{
			float fScale = m_pEB->GetWorldScale();
			std::vector<NuclearFRectt> lines;
			std::vector<NuclearFRectt> rects;

			astar::Path::const_iterator it = this->trail.begin();
			NuclearFPoint from(it->x, it->y);
			from = NuclearWorldLogicCoord::logic2world( from) - view;
			from *= fScale;
			rects.push_back(NuclearFRectt(from, astar::PathFinder::eGridSize / 2));
			for (++it; it != this->trail.end(); ++it)
			{
				NuclearFPoint to(it->x, it->y);
				to = NuclearWorldLogicCoord::logic2world( to) - view;
				to *= fScale;
				lines.push_back(NuclearFRectt(from, to));
				rects.push_back(NuclearFRectt(to, astar::PathFinder::eGridSize / 2));
				from = to;
			}
			NuclearFPoint to = this->location - view;
			to *= fScale;
			lines.push_back(NuclearFRectt(from, to));
			rects.push_back(NuclearFRectt(to, astar::PathFinder::eGridSize / 2));
			renderer->DrawLine(&lines[0], int(lines.size()), 0xffffffff);
			renderer->DrawBox(&rects[0], (int)rects.size(),  0x7fffff00);
		}

		// draw Path
		if (this->m_pEB->GetConfigManager()->IsShowSpritePath() && !this->path.empty())
		{
			float fScale = m_pEB->GetWorldScale();
			std::vector<NuclearFRectt> lines;
			std::vector<NuclearFRectt> rects;

			NuclearFPoint from = this->location - view;
			from *= fScale;
			rects.push_back(NuclearFRectt(from, astar::PathFinder::eGridSize / 2));
			for (astar::Path::const_iterator it = this->path.begin(); it != this->path.end(); ++it)
			{
				NuclearFPoint to(it->x, it->y);
				to = NuclearWorldLogicCoord::logic2world( to) - view;
				to *= fScale;
				lines.push_back(NuclearFRectt(from, to));
				rects.push_back(NuclearFRectt(to, astar::PathFinder::eGridSize / 2));
				from = to;
			}
			renderer->DrawLine(&lines[0], int(lines.size()), 0xffff0000);
			renderer->DrawBox(&rects[0], (int)rects.size(),  0x7fffff00);
		}
	}
};
