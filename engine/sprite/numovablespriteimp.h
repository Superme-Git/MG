#ifndef __Nuclear_SPRITEIMP_H__
#define __Nuclear_SPRITEIMP_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../astar/nuastar.hpp"
#include "../astar/nuapath.hpp"
#include "../common/nuxptypes.h"
#include "../common/nuworldlogiccoord.h"

namespace Nuclear
{
	class Sprite;
	class EngineBase;

	// ����õ��ķ���Ϊ�������귽��
	inline astar::Direction toAstarDirection(int direction)
	{
		switch (direction)
		{
		case XPDIR_TOP:         return astar::Direction(0, -1);
		case XPDIR_TOPRIGHT:    return astar::Direction(1, -1);
		case XPDIR_RIGHT:       return astar::Direction(1, 0);
		case XPDIR_BOTTOMRIGHT: return astar::Direction(1, 1);
		case XPDIR_BOTTOM:      return astar::Direction(0, 1);
		case XPDIR_BOTTOMLEFT:  return astar::Direction(-1, 1);
		case XPDIR_LEFT:        return astar::Direction(-1, 0);
		case XPDIR_TOPLEFT:     return astar::Direction(-1, -1);
		default:                return astar::Direction(0, 0); // no direction
		}
	}

	class MovableSpriteImp 
	{
		Sprite *m_pSprite;
		EngineBase *m_pEB;

	public:
		MovableSpriteImp(Sprite *pSprite, EngineBase *pEB);
		~MovableSpriteImp() { }

		///////////////////////////////////////////////////
		typedef astar::Direction Direction; 

		// ����֧��8��4��2��1���ַ���
		void SetTurnDir(int iDir) { iTurnDir = iDir; }
		int  GetTurnDir() const { return iTurnDir; }

		// ��������
		NuclearLocation getLocation() const                { return NuclearLocation(int(this->location.x), int(this->location.y)); }
		void setLocation(const NuclearLocation & location) { this->setLocation((float)location.x, (float)location.y); }
		void setLocation(float x, float y);         // ���þ���λ�ã��������ǰ��·����β����
		NuclearFPoint getVectorDirection() const				{ return NuclearFPoint(direction.x, direction.y); }

		// �߼�����
		NuclearLocation getLogicLocation() const				 { return NuclearWorldLogicCoord::world2logic( int(location.x), int(location.y)); }
		void setLogicLocation(const NuclearLocation & location) { this->setLocation( NuclearWorldLogicCoord::logic2world( location)); }
		void setLogicLocation(float x, float y)			 { this->setLocation( NuclearWorldLogicCoord::logic2world(int(x), int(y))); }
		astar::Point getLocationDirection() const   { return NuclearWorldLogicCoord::world2logic( astar::Point(this->location.x, this->location.y, this->direction)); } // ���Ϊ�߼�����

		// �������귽��
		NuclearDirection getDirection() const;
		void setDirection(NuclearDirection dir8){this->setDirection(toAstarDirection(dir8));}
		void setDirection(const Direction & direction)
		{
			if (false == this->direction.near_equals(direction))
			{
				this->__addtrail(this->getLocationDirection()); // get��add�����߼�����ϵ��, ����ת��
				this->direction = direction;
			}
		}
		void SetDirection(int tx, int ty){ this->setDirection(Direction(NuclearWorldLogicCoord::logic2world( this->getLocationDirection()), (float)tx, (float)ty)); }

		// �߼�����ϵ����
		void setLogicDirection(int tx, int ty){this->setDirection(NuclearWorldLogicCoord::logic2world( Direction(this->getLocationDirection(), (float)tx, (float)ty)));}
		void SetLocationDirection(const astar::Point & point)
		{
			// ����λ�÷����������е�·����β��������ֱ���޸ķ��򣬺� setDirection �Ĵ���������ͬ��
			astar::Point world = NuclearWorldLogicCoord::logic2world( point);
			this->direction = world.direction;
			this->setLocation(world.x, world.y);
		}

		void  SetMoveSpeed(float speed) { this->speed = speed; }
		float GetMoveSpeed() const      { return this->speed; }
		void  StopMove();
		bool  IsMoving() const          { return this->moving; }
		void SetMoveSuspended(bool b)   { this->movesuspended = b; }
		bool IsMoveSuspended() const    { return this->movesuspended; }

		void  MoveTo(astar::Path & trail); // �߼�����
		void  MoveTo(int targetX, int targetY, int range, const CSIZE * size, bool straight = false); // ��������

		void SetCurveMove(bool b)   { this->needCurveMove = b; }
		bool IsCurveMove() const	{ return this->needCurveMove; }

		// ��ȡ��ǰpath��һ������, ��ʹ��MoveTo����ÿɵõ�����Ҫ�ߵ�·��(�߼�����)
		void GetPath(astar::Path & path);

		void KeepMoveTrail(bool keep);
		bool IsKeepMoveTrail() const        { return this->keepTrail; }
		void SetMoveMask(unsigned int mask) { this->moveMask = mask; }
		bool TakeMoveTrail(astar::Path & trail /*out*/); // �߼�����

		bool InAttackRange( const Nuclear::NuclearLocation & target, int range, unsigned int mask); // ��������

		// �ƶ���ડ�����Ϊ��һ�����������ʱ��by world
		void tick(int now, int deltaTime);
		void RenderPath();

	private:

		// world . 2 logic inner
		void __addtrail(const astar::Point & point) // �߼�����
		{
			if (this->keepTrail && (this->trail.empty() || false == this->trail.back().trail_equals(point)))
				this->trail.push_back(point);
		}

		void __nextWaypoint(); // �߼�����

		// �ƶ�ֹͣ������ȴ���ơ���ֹ˲��任������ҳ����ƶ�ʱ���ۿ����յ���β�����������ӳٶ�����ɷ�϶��
		// ��������л�������ִ�еģ�վ������·���������л�����������cooldown�������������л���
		// cooldown�������ϣ�����ʱ����̫С������û�����壬��̫�������Ҳ��������⡣
		// һ����˵����������ͼ��֡Ϊ100ms��ѡ����С��ֵ���������ֵ̫�࣬����ɣ�ԭ���ܶ���վ��Ư�ơ�
		//
		// �������������⣺
		//     �߼�״̬IsMoving��cooldown�Ƿ���ġ���IsMoving�Ѿ���falseʱ���ڲ����ܻ���cooldown��
		//     ����������£���������˽�ɫ����(����������״̬�仯�����繥��)��
		//     ��ô��cooldown�ﵽʱ�䣬��ص� OnUpdateSpriteAction������ٵ��Ļص�ʵ�����Ƕ���ġ�
		//     ���ⲻ�����أ��Ժ��ٿ��ǡ�
		enum
		{
			// 0 ��������������
			eMovingCooldown = 0, // �л����ƶ�����ȴʱ�䡣ms
			eStandCooldown = 80, // �л���վ������ȴʱ�䡣ms
		};

		int updateActionCooldown; // ms
		void __tickActionCooldown(int now);
		void __setMoving(bool moving);

	private:
		// ���ƾ���������������ˮƽ�ʹ�ֱ������ƶ��ٶ�, 
		// a*��ʹ���߼�����, ��������ϵ�Ļ��㷽��Ϊ:
		// ��������.x ==  �߼�����.x * N
		// ��������.y ==  �߼�����.y
		// N = [1, 2]
		// TakeMoveTrail(astar::Path &)��MoveTo(astar::Path &)��
		// �����ռ�β�������������ͽ��շ������㲥β�������ƶ�
		// ���Բ�����trail��Ϊ�߼�����

		NuclearFPoint      location;  // λ�ã��������꣬������
		Direction   direction; // �������������µķ���
		float       speed;     // �ٶȣ��߼��ٶȣ��������߼����ꡣ

		astar::APath apath; // A* ·�����ɴ�ͼ���ꡣ�߼�������ӡ�
		astar::Path  path;  // ƽ��·�����߼����ꡣ
		astar::Path  trail; // �ƶ�β�����߼����ꡣ

		bool    moving;        // �Ƿ��ƶ�
		bool    keepTrail;
		bool    movesuspended; // �Ƿ���ͣ
		int		iTurnDir;	   // ����(8����4����2����1����)
		unsigned int moveMask;
		
		NuclearFPoint	startPos;//��ʼ�ƶ�ʱ�̵�����
		bool	needCurveMove;	//�Ƿ���Ҫ���л����ƶ�
		bool	straightMove;	//�Ƿ�ֱ���ƶ��������Զ�Ѱ·

		int     accumulatetime;//�ۼƵ�ʱ��
	};

}

#endif
