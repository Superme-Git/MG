#pragma once
#include <string>
#include <vector>
#include <set>
#include <sstream>
#include "XMLHelper.h"

enum SHOW_LAYER // QQ �� �㶨��
{
	MH_SHOW_BACK_GROUND = 0,// 0.	����
	MH_SHOW_VEHICLE,		// 1.	��ͨ���ߣ����ƶ���
	MH_SHOW_COMPANION,		// 2.	ͬ�飨���ƶ���
	MH_SHOW_WING,			// 3.	���
	MH_SHOW_HAT_B,			// 4.	ñ��B�����ƶ���
	MH_SHOW_HAIR_B,			// 5.	ͷ��B
	MH_SHOW_MANTLE_B,		// 6.	����B
	MH_SHOW_ARM_R,			// 7.	�ֱ�(��)
	MH_SHOW_LEG,			// 8.	��
	MH_SHOW_BODY,			// 9.	�ؿ�

	MH_SHOW_ARM_L,			// 10.	�֣����֣�
	MH_SHOW_SHIRT,			// 11.	�·�T��
	MH_SHOW_SHIRT_SLEEVE_R,	// 12.	�·�T�� ����(��)
	MH_SHOW_COAT_SLEEVE_R,	// 13.	���� ����(��)
	MH_SHOW_SOCKS,			// 14.	����
	MH_SHOW_TROUSERS_INNER,	// 15.	����(Ь����)
	MH_SHOW_SHOES,			// 16.	Ь
	MH_SHOW_TROUSERS_OUTER,	// 17.	����(Ь����)
	MH_SHOW_WAISTBAND,		// 18.	Ƥ��
	MH_SHOW_COAT,			// 19.	����
	
	MH_SHOW_SHIRT_SLEEVE_L,	// 20.	�·�T�� ����(��)
	MH_SHOW_COAT_SLEEVE_L,	// 21.	���� ����(��)
	MH_SHOW_MANTLE_A,		// 22.	����A
	MH_SHOW_HEAD_EAR,		// 23.	���ͺͶ���
	MH_SHOW_BEARD,			// 24.	����
	MH_SHOW_EARRING,		// 25.	��ٺͶ��� (����)
	MH_SHOW_BROWS,			// 26.	��ٺͶ��� (üë)
	MH_SHOW_MOUTH,			// 27.	��ٺͶ��� (��)
	MH_SHOW_NOSE,			// 28.	��ٺͶ��� (����)
	MH_SHOW_EYE,			// 29.	��ٺͶ��� (�۾�)
	MH_SHOW_HAIR_A,			// 30.	ͷ��A
	MH_SHOW_TATTOO,			// 31.	�������ƶ���
	MH_SHOW_HAT_A,			// 32.	ñ��A�����ƶ���
	MH_SHOW_ACCESSORIES_A, 	// 33.	���Σ����ƶ���
	MH_SHOW_SHAWL,			// 34.	Χ�����ƶ���
	MH_SHOW_ACCESSORIES_B,	// 35.	�ܱ�װ�Σ����ƶ���
	MH_SHOW_PET,			// 36.	������ƶ���
	MH_SHOW_SUBTITLING,		// 37.	��Ļ�����ƶ���
	MH_SHOW_MAGZINE,		// 38.	��־ 
	MH_SHOW_PHOTO_FRAME,  	// 39.	���
	//----------------------------------
	MH_SHOW_LAYER_MAX,  	// 40   ����
};

enum SHOW_TYPE	// QQ �� ���� 
{ 
	SHOW_TYPE_UNDEFINE = -1,	// δ����
	//------------------------------------------------------------------------------
	// ��װ
	SHOW_TYPE_COAT = 0,			// ����
	SHOW_TYPE_TROUSERS,			// ����
	SHOW_TYPE_SUIT,				// ��װ
	SHOW_TYPE_SHAWL,			// Χ��
	SHOW_TYPE_HAT,				// ñ��
	SHOW_TYPE_SHOES,			// Ь
	SHOW_TYPE_MANTLE,			// ����
	//------------------------------------------------------------------------------
	// ����
	SHOW_TYPE_HAIR,				// ͷ��
	SHOW_TYPE_EXPRESSION,		// ����
	SHOW_TYPE_HEAD_EAR,			// ͷ��
	//------------------------------------------------------------------------------
	// ����
	SHOW_TYPE_BACK_GROUND,		// ����
	SHOW_TYPE_MAGZINE,			// ��־ 
	SHOW_TYPE_PHOTO_FRAME,  	// ���
	SHOW_TYPE_SUBTITLING,		// ��Ļ
	SHOW_TYPE_AMBITUS,			// �ܱ�װ��
	SHOW_TYPE_ACCESSORIES_A, 	// ����
	SHOW_TYPE_ASSOCIATE,		// ͬ��
	SHOW_TYPE_VEHICLE,			// ��ͨ����
	//------------------------------------------------------------------------------
	// ����
	SHOW_TYPE_HEADWEAR,			// ͷ��
	SHOW_TYPE_FACEWEAR,			// ����
	SHOW_TYPE_NECKLACE,			// ����
	SHOW_TYPE_EARRING,			// ����
	SHOW_TYPE_GLASSES,			// �۾�
	SHOW_TYPE_BAG,				// ����
	SHOW_TYPE_WING,				// ���
	SHOW_TYPE_BROOCH,			// ����
	SHOW_TYPE_WAISTWEAR,		// ����
	SHOW_TYPE_OTHERWEAR,		// ����
	//------------------------------------------------------------------------------
	// ����
	SHOW_TYPE_NORMAL_PET,		// ��ͨ����
	SHOW_TYPE_SPECAL_PET,		// �������
	//------------------------------------------------------------------------------
	// ��װ
	SHOW_TYPE_COMPETITIVE,		// ��Ʒ
	SHOW_TYPE_POPULARITY,		// ����
	//------------------------------------------------------------------------------
	// VIP
	SHOW_TYPE_VIP,				// VIP
	//----------------------------------
	SHOW_TYPE_MAX,  	//    ����
};

enum { 
	CHANGED_PART_MIN_ID = 100,		// �ɻ�װģ�͵���Сid��С�����idΪ��ģ
	LINK_MAX_COUNT = 4,				// linkID �����
};

struct ShowPartItem
{
	unsigned long	ulID;			// Part ID
	std::wstring	name;
	long			lType;			// Part Type
	unsigned long	ulLayer;		// Layer ID
	long			bSexMan;		// 1 - women, 0 - man
	unsigned long	bIsSaling;		// �Ƿ��ϼ�
	
	unsigned long	ulPrice;
	unsigned long	ulFlag;

	std::wstring	strSvg;			// Svg File Path
	std::wstring	strTga;			// Tga thumbnail File Path
	std::wstring	descript;

	unsigned long	ulLinkID[LINK_MAX_COUNT];	// linkID�������link�ĸ�
	__int64			conflict;		// �������㻥���־��ÿһλ����ĳһ���Ƿ񻥳�

	double			transform[6];	// transform matrix
	double			boundingbox[4];	// bounding box

	int				image;			// image index��ֻ����ʾ�ã������浽�ļ�

	ShowPartItem()
		: image( -1), ulFlag( 0), lType(-1), conflict( 0), bIsSaling( true)
	{
		memset( ulLinkID, 0, sizeof(unsigned long)*LINK_MAX_COUNT);
		memset( transform, 0, sizeof(double)*6);
		transform[0] = 1.0;
		transform[3] = 1.0;
		memset( boundingbox, 0, sizeof(double)*4);
		
	}
};

typedef std::vector<ShowPartItem> TShowPartContainer;
typedef std::vector<ShowPartItem*> TShowPartPtrContainer;

class CShowPartLoader
{
	TShowPartContainer	m_vShowParts;

	CImageList	m_image;

	HDC					m_dcMemSrc;
	HDC					m_dcMemDes;
	unsigned long		m_maxID;

	// ����ͼƬ��m_image�Ĵ�С
	HBITMAP StretchImage( HBITMAP hSrc, BITMAPINFO* pInfo);
	
	BOOL InitDefaultModel();

public:
	CShowPartLoader();
	~CShowPartLoader();

	int Load();

	// ��������
	bool Save( LPCTSTR pszFile);

	TShowPartContainer& GetAllParts() { return m_vShowParts; }

public:

	CImageList& GetImageList() { return m_image; }

	int GetImageIndex( LPCTSTR pszImage);

public:

	int GetIndexFromID( unsigned long id);

	ShowPartItem* GetItemFromID( unsigned long id);

public:
	
	// �õ���Ů��ģ����
	void GetDefaultParts( TShowPartPtrContainer& tspc, int sex);

	// �õ���Ϊlayer�Ĳ���
	void GetPartsByLayer( TShowPartPtrContainer& tspc, int sex, int layer);

	// �õ������׼������ƣ��ȶ�Ϊsvg�ļ���Ŀ¼��
	void GetAllSuitName( std::set<std::wstring>& suits, int sex);

	// �õ�����ƥ���׼��Ĳ���
	void GetPartsBySuitName( TShowPartPtrContainer& tspc, int sex, const wchar_t* szSuit);

	// �õ�����Ϊtype�Ĳ���
	void GetPartsByType( TShowPartPtrContainer& tspc, int sex, long type);

public:

	// ���һ����
	bool AddPart( const std::wstring& file, int sex, int layer, int type);

	// ɾ��һ����
	bool DeletePart( unsigned long ulID);

	// �õ���ǰ���id
	unsigned long GetMaxID() { return m_maxID; }

public:
	static void GetSvgFullPath( std::wstring& fullpath);
	static void GetTgaFullPath( std::wstring& fullpath);
};