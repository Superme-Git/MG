// ���ļ�������engine��gameclient��������Ҫ����İ汾�ţ���Ϊ�ֹ��༭��
// �Ժ���ļ����ܻ��Զ�����. 
// ����б�Ҫ�޸Ĵ��ļ�������ϵ����ѧ�������Ա
//
#include "product.inl"
#ifdef PRODUCT_TIYAN	// �����
#include "version_tiyan.inl"
#elif defined PRODUCT_CHT	// ̨�己�����İ�
#include "version_cht.inl"
#else		// ��ʽ��
#include "version_formal.inl"
#endif

// ��Ҫ��֤����
