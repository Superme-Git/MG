#if !defined NOT_ENABLE_TIYAN  && !defined PRODUCT_TIYAN
// ���������ʱ����������ĺ�
//#define PRODUCT_TIYAN		// �����
#endif

#if !defined PRODUCT_TIYAN && !defined NOT_ENABLE_CHT && !defined PRODUCT_CHT
// ���뷱���ʱ����������ĺ�
// #define PRODUCT_CHT		// ̨�己�����İ�
#endif

#define _STR(v)			#v
#define _STR_(v)		_STR(v)

#ifdef PRODUCT_TIYAN		
#define PRODUCT_NAME		"�λ��������棩"
#define PRODUCT_NAME_SYMBOL	�λ��������棩
#define PRODUCT_SYMBOL		tiyan
#elif defined PRODUCT_CHT
#define PRODUCT_NAME		"�������"
#define PRODUCT_NAME_SYMBOL	�������
#define PRODUCT_SYMBOL		l.cht
#else
#define PRODUCT_NAME		"�λ����"
#define PRODUCT_NAME_SYMBOL	�λ����
#define PRODUCT_SYMBOL		formal
#endif

#ifndef ICON_NAME
#define ICON_NAME			mhjy.ico	
#endif

#ifndef SMALL_ICON_NAME		
#define SMALL_ICON_NAME		mhjy.ico			// ��ʱһ��
#endif

#define PRODUCT_SYMBOL_2	PRODUCT_SYMBOL

#define _M_(a)	a

#define CONCAT_MACRO_2(a,b)	_M_(a) ## _M_(b)
#define CONCAT_MACRO_3(a,b,c)	_M_(a) ## _M_(b) ## _M_(c)
#define CONCAT_MACRO_4(a,b,c,d)	_M_(a) ## _M_(b) ## _M_(c) ## _M_(d)

#define ProductName		PRODUCT_NAME
#define ProductIconName		CONCAT_MACRO_3(PRODUCT_SYMBOL, \\, ICON_NAME)
#define ProductSmallIconName	CONCAT_MACRO_3(PRODUCT_SYMBOL, \\, SMALL_ICON_NAME)

#if defined PRODUCT_CHT
#define LegalCopyrightString	"(C) �ֶ�׿Խ�Ƽ����޹�˾ 2015��������С�"
#define CompanyNameString		"�ֶ�׿Խ�Ƽ����޹�˾"
#define VersionBlockString		"040403b6"
#define TranslationCodeValue	0x404, 950
#else
#define LegalCopyrightString	"(C) �ֶ�׿Խ�Ƽ����޹�˾ 2015����Ȩ���С�"
#define CompanyNameString		"�ֶ�׿Խ�Ƽ����޹�˾"
#define VersionBlockString		"080403a8"
#define TranslationCodeValue	0x804, 936
#endif

