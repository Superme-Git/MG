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

		//如roleid有变化就别显示了, qtype现在=3， seq， reserved，保存就行。用户回答后调用AnswerNotify
		//pic:	160*160大小， pic是 path vector， path是point vector，
		//		对每条path，依次连接其中的point就画出图形了。
		virtual void OnQuestion(int64_t roleid, int qtype, int seq, int reserved, const std::wstring &ask, const std::vector< std::vector<std::pair<unsigned char, unsigned char> > >  &pic ) = 0;

		//这个是登陆时的图片码，acptype = 0时， pixels是200*80的灰度图
		virtual void OnCaptcha( int acptype, const std::vector< unsigned char >  &pixels ) = 0;

		//这个是汉字图片码，8个里选择2个相同的，当qtype=4时pixels是240*120的黑白图，0代表黑，255是白。回答还是回调AnswerNotify，其中answer=idx1<<8+idx2.(idx为汉字编码0-7，idx1<idx2)
		virtual void OnHzQuestion( int64_t roleid, int qtype, int seq, int reserved, unsigned char* pixels ) = 0;

		virtual void OnShowMessageBox(const std::wstring &title, const std::wstring &content) = 0; //在客户端弹出对话框
		virtual bool IsRoleInWorld() = 0; //只有角色在游戏中时,反外挂线程才运行。
		virtual int64_t	 GetRoleID() = 0;	  //发过来的协议只有与这个对上，才执行。
		virtual bool SendProtocol(const aio::Protocol &protocol) = 0; //发ACReport时需要

		//这个是把游戏exe的代码段隐藏到一个文件中，路径随便选一个，只要发d布前，更新这个就好。
		//文件格式为前12个字节为 4字节的ImageBase, 4字节的代码段长度，4字节的代码段VirtualAddress
		//然后是整个代码段。
		virtual const std::wstring GetCodeSegmentFilename() = 0;
		virtual const std::string  GetAdpaterDescription() = 0; //取得显卡信息，注意是string， 一般就是d3d的GetAdapterIdentifier里的Description字段,只在线程启动前调用一次。
		virtual void Log(const wchar_t* str,...) = 0; //给defence记录日志用
		
	private:
		IDefence(const IDefence&);
		IDefence& operator=(const IDefence&);
	};
	
	bool Startup( IDefence *idefence );//请首先启动pp, 返回false表示失败，不要在调用cleanup
	void Cleanup(); //会阻塞直到反外挂线程结束，然后可以delete idefence了

	void AnswerNotify(int64_t roleid, int qtype, int seq, int reserved, int answer); //answer应为1，2，4，8对应于1，2，3，4个特定字。
	void ACTrace(int id); //id 为[0, 15],放在敏感函数的开头。用来查栈, id最好不要重复。调用前确保Startup已经调用。

}


