#pragma once

enum
{
	TIP_ERROR =						1,			// ����
	ERR_START =						2,			// ������������з�������. �����ļ�ʧ��(%d):\n%s -> %s\n%s\n�볢��������������!
	ERR_START_MISSING_FILE =		3,			// �޷���������\nԭ��ȱ�ٱ�Ҫ�ļ���Ŀ¼ %s! \n�볢�����°�װ����!
	ERR_START_NOT_DELETE_FILE =		4,			// ������������з��������޷�ɾ���¾ɰ汾����\n������Ϣ %d:\n%s\n�볢�����°�װ����!
	ERR_START_NOT_MOVE_FILE =		5,			// ������������з��������޷��滻�¾ɰ汾����\n������Ϣ %d:\n%s\n�볢�����°�װ����!
	ERR_START_FILE_DAMAGE =			6,			// �޷���������\nԭ��Ŀ¼ %s �е��ļ����ƻ�! \n�볢�����°�װ����!
	ERR_START_NOT_CREATE_FILE =		7,			// ������������з��������޷������ļ���\n������Ϣ %d:\n%s\n�����³���������������°�װ����!
	ERR_START_NOT_BACKUP_FILE =		8,			// ������������з��������޷����ݳ���\n%1!s!(%2!d!)\n�����³���������������°�װ����!
	ERR_LOAD_PROGRAME =				9,			// ���س���ʧ��:%s. \n���� %d:\n%s
	QST_OLD_PATCHER_NOT_EXIT =		10,			// �ɵĸ��³���û��ȫ�˳���\nѡ��&quot;����&quot;�����ȴ���\nѡ��&quot;ȡ��&quot;���˳��������Ժ��ֹ��������³�����и��£�
	TIP_TIP =						11,			// ��ʾ
	ERR_OPEN_PROCESS =				12,			// �򿪽���ʧ��
	ERR_START_OPERATE_REGISTER =	13,			// �������������з������⣺\n\n    ע������ʧ�ܡ�\n\n������Ϣ��%1!s!������룺%2!d!\n\n��ȷ�������Թ���Ա�˻������������
	
	TOK_PRODUCT_NAME				= 40,		//����
	TOK_PRODUCT_LAUNCHER			= 41,		//������������
	TOK_PRODUCT_PATCHER				= 42,		//���ɸ��³���
	TOK_PRODUCT_REPAIR				= 43,		//���ɰ汾��֤����
	TOK_PRODUCT_CLIENT				= 44,		//���ɿͻ��˳���
};