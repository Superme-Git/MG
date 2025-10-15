#ifndef __Nuclear_pspritemaze_H__
#define __Nuclear_pspritemaze_H__
#include "../common/nuxptypes.h"
#include "../common/nupobject.h"

namespace Nuclear
{
	class PSpriteMaze : public NuclearObject
	{
	public:
		const static int VERSION		 = 2;//�汾
		const static int VERSION_NOLTPOS = 1;//�汾
	private:
		NuclearPoint m_leftTopPos;		//�����ԭ�㵽�ϰ����Ͻǵ���������������Ϊ����
		int	   m_nMazeWidth;		//�ϰ����Ը���Ϊ��λ��
		int	   m_nMazeHeight;		//�ϰ��ߣ��Ը���Ϊ��λ��
		char*  m_pMaze;				//�ϰ���

	public:
		virtual bool Init(int width, int height);	//���Ը���Ϊ��λ��
		virtual bool Init(int width, int height, const NuclearPoint& ltpos);	//������Ը���Ϊ��λ��ltpos������Ϊ��λ��
		virtual NuclearOStream& marshal(NuclearOStream &) const;
		virtual const INuclearStream& unmarshal(const INuclearStream &);

		const NuclearPoint &GetLeftTopPos() const { return m_leftTopPos; }
		int GetWidth() const { return m_nMazeWidth; }	//�ϰ����Ը���Ϊ��λ��
		int GetHeight() const { return m_nMazeHeight; }	//�ϰ��ߣ��Ը���Ϊ��λ��
		char* GetMaze() const { return m_pMaze; }

	public:
		PSpriteMaze(void);
		virtual ~PSpriteMaze(void);
	};
}

#endif