================================================================================
MICROSOFT �������: ParticleSystemEditor ��Ŀ����
===============================================================================

Ӧ�ó�������Ϊ����������� ParticleSystemEditor Ӧ�ó��򡣴�Ӧ�ó��򲻽���ʾ Microsoft ������Ļ���ʹ�÷�����������Ϊ����дӦ�ó������㡣

���ļ���Ҫ������� ParticleSystemEditor Ӧ�ó����ÿ���ļ������ݡ�

ParticleSystemEditor.vcproj
����ʹ��Ӧ�ó��������ɵ� VC++ ��Ŀ������Ŀ�ļ��� 
���������ɸ��ļ��� Visual C++ �İ汾��Ϣ���Լ��й�ʹ��Ӧ�ó�����ѡ���ƽ̨�����ú���Ŀ���ܵ���Ϣ��

ParticleSystemEditor.h
����Ӧ�ó������Ҫͷ�ļ���������������Ŀ�ض���ͷ�ļ�(���� Resource.h)�������� CParticleSystemEditorApp Ӧ�ó����ࡣ

ParticleSystemEditor.cpp
���ǰ���Ӧ�ó����� CParticleSystemEditorApp ����ҪӦ�ó���Դ�ļ���

ParticleSystemEditor.rc
���ǳ���ʹ�õ����� Microsoft Windows ��Դ���б������� RES ��Ŀ¼�д洢��ͼ�ꡢλͼ�͹�ꡣ���ļ�����ֱ���� Microsoft Visual C++ �н��б༭����Ŀ��Դλ�� 2052 �С�

res\ParticleSystemEditor.ico
��������Ӧ�ó���ͼ���ͼ���ļ�����ͼ���������Ҫ��Դ�ļ� ParticleSystemEditor.rc �С�

res\ParticleSystemEditor.rc2
���ļ��������� Microsoft Visual C++ �н��б༭����Դ����Ӧ�ý���������Դ�༭���༭��������Դ���ڴ��ļ��С�

/////////////////////////////////////////////////////////////////////////////

��������ܴ���:
��Ŀ������׼ MFC ���档

MainFrm.h, MainFrm.cpp
��Щ�ļ���������� CMainFrame������������
CMDIFrameWnd ���������� MDI ��ܹ��ܡ�

res\Toolbar.bmp
��λͼ�ļ�����Ϊ����������ƽ��ͼ��
��ʼ��������״̬���� CMainFrame ���й��졣ʹ����Դ�༭���༭�˹�����λͼ�������� ParticleSystemEditor.rc �е� IDR_MAINFRAME TOOLBAR ��������ӹ�������ť��
/////////////////////////////////////////////////////////////////////////////

�����ӿ�ܴ���:

ChildFrm.h��ChildFrm.cpp
��Щ�ļ����岢ʵ�� CChildFrame �࣬����֧�� MDI Ӧ�ó����е��Ӵ��ڡ�

/////////////////////////////////////////////////////////////////////////////

Ӧ�ó����򵼴���һ���ĵ����ͺ�һ����ͼ:

ParticleSystemEditorDoc.h��ParticleSystemEditorDoc.cpp - �ĵ�
��Щ�ļ����� CParticleSystemEditorDoc �ࡣ�༭��Щ�ļ�������������ĵ����ݲ���ʵ���ļ�����ͼ���(ͨ�� CParticleSystemEditorDoc::Serialize)��

ParticleSystemEditorView.h��ParticleSystemEditorView.cpp - �ĵ�����ͼ
��Щ�ļ����� CParticleSystemEditorView �ࡣ
CParticleSystemEditorView �������ڲ鿴 CParticleSystemEditorDoc ����

res\ParticleSystemEditorDoc.ico
����ͼ���ļ��������� CParticleSystemEditorDoc ��� MDI �Ӵ��ڵ�ͼ�ꡣ��ͼ���������Ҫ��Դ�ļ� ParticleSystemEditor.rc �С�




/////////////////////////////////////////////////////////////////////////////

������׼�ļ�:

StdAfx.h��StdAfx.cpp
��Щ�ļ�����������Ϊ ParticleSystemEditor.pch ��Ԥ����ͷ (PCH) �ļ�����Ϊ StdAfx.obj ��Ԥ���������ļ���

Resource.h
���Ǳ�׼ͷ�ļ����������µ���Դ ID��
Microsoft Visual C++ ��ȡ�����´��ļ���

ParticleSystemEditor.manifest
	Ӧ�ó����嵥�ļ��� Windows XP ��������Ӧ�ó���
	���ض��汾���г��򼯵������ԡ����س���ʹ�ô�
	��Ϣ�ӳ��򼯻�������ʵ��ĳ��򼯻�
	��Ӧ�ó������˽����Ϣ��Ӧ�ó����嵥����Ϊ�����·ַ�����Ϊ
	��Ӧ�ó����ִ���ļ���װ����ͬ�ļ����е��ⲿ .manifest �ļ�������
	Ҳ��������Դ����ʽ�����ڸÿ�ִ���ļ��С� 
/////////////////////////////////////////////////////////////////////////////

����ע��:

Ӧ�ó�����ʹ�á�TODO:��ָʾӦ��ӻ��Զ����Դ���벿�֡�

���Ӧ�ó����ڹ���� DLL ��ʹ�� MFC������Ҫ���·�����Щ MFC DLL�����Ӧ�ó������õ����������ϵͳ�ĵ�ǰ�������ò�ͬ������Ҫ���·�����Ӧ�ı��ػ���Դ MFC80XXX.DLL���й�����������ĸ�����Ϣ����μ� MSDN �ĵ����й� Redistributing Visual C++ applications (���·��� Visual C++ Ӧ�ó���)���½ڡ�

/////////////////////////////////////////////////////////////////////////////
