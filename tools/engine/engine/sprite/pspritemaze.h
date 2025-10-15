#pragma once
#include "..\common\xptypes.h"
#include "..\common\pobject.h"

namespace Nuclear
{
	class PSpriteMaze :
		public PObject
	{
	public:
		const static int VERSION		 = 2;//�汾
		const static int VERSION_NOLTPOS = 1;//�汾
	private:
		CPOINT m_leftTopPos;		//�����ԭ�㵽�ϰ����Ͻǵ���������������Ϊ����
		int	   m_nMazeWidth;		//�ϰ����Ը���Ϊ��λ��
		int	   m_nMazeHeight;		//�ϰ��ߣ��Ը���Ϊ��λ��
		char*  m_pMaze;				//�ϰ���

	public:
		virtual bool Init(int width, int height);	//���Ը���Ϊ��λ��
		virtual bool Init(int width, int height, const CPOINT& ltpos);	//������Ը���Ϊ��λ��ltpos������Ϊ��λ��
		virtual XOStream& marshal(XOStream &) const;
		virtual const XIStream& unmarshal(const XIStream &);

		const CPOINT &GetLeftTopPos() const { return m_leftTopPos; }
		int GetWidth() const { return m_nMazeWidth; }	//�ϰ����Ը���Ϊ��λ��
		int GetHeight() const { return m_nMazeHeight; }	//�ϰ��ߣ��Ը���Ϊ��λ��
		char* GetMaze() const { return m_pMaze; }



	public:
		PSpriteMaze(void);
		virtual ~PSpriteMaze(void);
	};
}