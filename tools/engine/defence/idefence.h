#pragma once

#include <vector>
#include <string>

namespace aio{ class Protocol;}

namespace Defence
{
	class IDefence
	{
	public:
		IDefence(){}
		virtual ~IDefence() {}

		//��roleid�б仯�ͱ���ʾ��, qtype����=3�� seq�� reserved��������С��û��ش�����AnswerNotify
		//pic:	160*160��С�� pic�� path vector�� path��point vector��
		//		��ÿ��path�������������е�point�ͻ���ͼ���ˡ�
		virtual void OnQuestion(int64_t roleid, int qtype, int seq, int reserved, const std::wstring &ask, const std::vector< std::vector<std::pair<unsigned char, unsigned char> > >  &pic ) = 0;

		//����ǵ�½ʱ��ͼƬ�룬acptype = 0ʱ�� pixels��200*80�ĻҶ�ͼ
		virtual void OnCaptcha( int acptype, const std::vector< unsigned char >  &pixels ) = 0;

		//����Ǻ���ͼƬ�룬8����ѡ��2����ͬ�ģ���qtype=4ʱpixels��240*120�ĺڰ�ͼ��0����ڣ�255�ǰס��ش��ǻص�AnswerNotify������answer=idx1<<8+idx2.(idxΪ���ֱ���0-7��idx1<idx2)
		virtual void OnHzQuestion( int64_t roleid, int qtype, int seq, int reserved, unsigned char* pixels ) = 0;

		virtual void OnShowMessageBox(const std::wstring &title, const std::wstring &content) = 0; //�ڿͻ��˵����Ի���
		virtual bool IsRoleInWorld() = 0; //ֻ�н�ɫ����Ϸ��ʱ,������̲߳����С�
		virtual int64_t	 GetRoleID() = 0;	  //��������Э��ֻ����������ϣ���ִ�С�
		virtual bool SendProtocol(const aio::Protocol &protocol) = 0; //��ACReportʱ��Ҫ

		//����ǰ���Ϸexe�Ĵ�������ص�һ���ļ��У�·�����ѡһ����ֻҪ��d��ǰ����������ͺá�
		//�ļ���ʽΪǰ12���ֽ�Ϊ 4�ֽڵ�ImageBase, 4�ֽڵĴ���γ��ȣ�4�ֽڵĴ����VirtualAddress
		//Ȼ������������Ρ�
		virtual const std::wstring GetCodeSegmentFilename() = 0;
		virtual const std::string  GetAdpaterDescription() = 0; //ȡ���Կ���Ϣ��ע����string�� һ�����d3d��GetAdapterIdentifier���Description�ֶ�,ֻ���߳�����ǰ����һ�Ρ�
		virtual void Log(const wchar_t* str,...) = 0; //��defence��¼��־��
		
	private:
		IDefence(const IDefence&);
		IDefence& operator=(const IDefence&);
	};
	
	bool Startup( IDefence *idefence );//����������pp, ����false��ʾʧ�ܣ���Ҫ�ڵ���cleanup
	void Cleanup(); //������ֱ��������߳̽�����Ȼ�����delete idefence��

	void AnswerNotify(int64_t roleid, int qtype, int seq, int reserved, int answer); //answerӦΪ1��2��4��8��Ӧ��1��2��3��4���ض��֡�
	void ACTrace(int id); //id Ϊ[0, 15],�������к����Ŀ�ͷ��������ջ, id��ò�Ҫ�ظ�������ǰȷ��Startup�Ѿ����á�

}


