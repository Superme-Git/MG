#pragma once
#include <string>
#include <vector>
#include <set>
#include <sstream>
#include "XMLHelper.h"

enum SHOW_LAYER // QQ 秀 层定义
{
	MH_SHOW_BACK_GROUND = 0,// 0.	背景
	MH_SHOW_VEHICLE,		// 1.	交通工具（可移动）
	MH_SHOW_COMPANION,		// 2.	同伴（可移动）
	MH_SHOW_WING,			// 3.	翅膀
	MH_SHOW_HAT_B,			// 4.	帽子B（可移动）
	MH_SHOW_HAIR_B,			// 5.	头发B
	MH_SHOW_MANTLE_B,		// 6.	披风B
	MH_SHOW_ARM_R,			// 7.	手臂(右)
	MH_SHOW_LEG,			// 8.	腿
	MH_SHOW_BODY,			// 9.	胸口

	MH_SHOW_ARM_L,			// 10.	手（左手）
	MH_SHOW_SHIRT,			// 11.	衣服T恤
	MH_SHOW_SHIRT_SLEEVE_R,	// 12.	衣服T恤 袖子(右)
	MH_SHOW_COAT_SLEEVE_R,	// 13.	外衣 袖子(右)
	MH_SHOW_SOCKS,			// 14.	袜子
	MH_SHOW_TROUSERS_INNER,	// 15.	裤子(鞋里面)
	MH_SHOW_SHOES,			// 16.	鞋
	MH_SHOW_TROUSERS_OUTER,	// 17.	裤子(鞋外面)
	MH_SHOW_WAISTBAND,		// 18.	皮带
	MH_SHOW_COAT,			// 19.	外衣
	
	MH_SHOW_SHIRT_SLEEVE_L,	// 20.	衣服T恤 袖子(左)
	MH_SHOW_COAT_SLEEVE_L,	// 21.	外衣 袖子(左)
	MH_SHOW_MANTLE_A,		// 22.	披风A
	MH_SHOW_HEAD_EAR,		// 23.	脸型和耳朵
	MH_SHOW_BEARD,			// 24.	胡子
	MH_SHOW_EARRING,		// 25.	五官和耳环 (耳环)
	MH_SHOW_BROWS,			// 26.	五官和耳环 (眉毛)
	MH_SHOW_MOUTH,			// 27.	五官和耳环 (嘴)
	MH_SHOW_NOSE,			// 28.	五官和耳环 (鼻子)
	MH_SHOW_EYE,			// 29.	五官和耳环 (眼睛)
	MH_SHOW_HAIR_A,			// 30.	头发A
	MH_SHOW_TATTOO,			// 31.	文身（可移动）
	MH_SHOW_HAT_A,			// 32.	帽子A（可移动）
	MH_SHOW_ACCESSORIES_A, 	// 33.	配饰（可移动）
	MH_SHOW_SHAWL,			// 34.	围巾（可移动）
	MH_SHOW_ACCESSORIES_B,	// 35.	周边装饰（可移动）
	MH_SHOW_PET,			// 36.	宠物（可移动）
	MH_SHOW_SUBTITLING,		// 37.	字幕（可移动）
	MH_SHOW_MAGZINE,		// 38.	杂志 
	MH_SHOW_PHOTO_FRAME,  	// 39.	相框
	//----------------------------------
	MH_SHOW_LAYER_MAX,  	// 40   计数
};

enum SHOW_TYPE	// QQ 秀 类型 
{ 
	SHOW_TYPE_UNDEFINE = -1,	// 未定义
	//------------------------------------------------------------------------------
	// 服装
	SHOW_TYPE_COAT = 0,			// 外衣
	SHOW_TYPE_TROUSERS,			// 裤子
	SHOW_TYPE_SUIT,				// 套装
	SHOW_TYPE_SHAWL,			// 围巾
	SHOW_TYPE_HAT,				// 帽子
	SHOW_TYPE_SHOES,			// 鞋
	SHOW_TYPE_MANTLE,			// 披风
	//------------------------------------------------------------------------------
	// 美容
	SHOW_TYPE_HAIR,				// 头发
	SHOW_TYPE_EXPRESSION,		// 表情
	SHOW_TYPE_HEAD_EAR,			// 头像
	//------------------------------------------------------------------------------
	// 场景
	SHOW_TYPE_BACK_GROUND,		// 背景
	SHOW_TYPE_MAGZINE,			// 杂志 
	SHOW_TYPE_PHOTO_FRAME,  	// 相框
	SHOW_TYPE_SUBTITLING,		// 字幕
	SHOW_TYPE_AMBITUS,			// 周边装饰
	SHOW_TYPE_ACCESSORIES_A, 	// 配饰
	SHOW_TYPE_ASSOCIATE,		// 同伴
	SHOW_TYPE_VEHICLE,			// 交通工具
	//------------------------------------------------------------------------------
	// 配饰
	SHOW_TYPE_HEADWEAR,			// 头饰
	SHOW_TYPE_FACEWEAR,			// 脸饰
	SHOW_TYPE_NECKLACE,			// 项链
	SHOW_TYPE_EARRING,			// 耳环
	SHOW_TYPE_GLASSES,			// 眼镜
	SHOW_TYPE_BAG,				// 包包
	SHOW_TYPE_WING,				// 翅膀
	SHOW_TYPE_BROOCH,			// 胸针
	SHOW_TYPE_WAISTWEAR,		// 腰饰
	SHOW_TYPE_OTHERWEAR,		// 其他
	//------------------------------------------------------------------------------
	// 宠物
	SHOW_TYPE_NORMAL_PET,		// 普通宠物
	SHOW_TYPE_SPECAL_PET,		// 特殊宠物
	//------------------------------------------------------------------------------
	// 套装
	SHOW_TYPE_COMPETITIVE,		// 精品
	SHOW_TYPE_POPULARITY,		// 人气
	//------------------------------------------------------------------------------
	// VIP
	SHOW_TYPE_VIP,				// VIP
	//----------------------------------
	SHOW_TYPE_MAX,  	//    计数
};

enum { 
	CHANGED_PART_MIN_ID = 100,		// 可换装模型的最小id，小于这个id为裸模
	LINK_MAX_COUNT = 4,				// linkID 最大数
};

struct ShowPartItem
{
	unsigned long	ulID;			// Part ID
	std::wstring	name;
	long			lType;			// Part Type
	unsigned long	ulLayer;		// Layer ID
	long			bSexMan;		// 1 - women, 0 - man
	unsigned long	bIsSaling;		// 是否上架
	
	unsigned long	ulPrice;
	unsigned long	ulFlag;

	std::wstring	strSvg;			// Svg File Path
	std::wstring	strTga;			// Tga thumbnail File Path
	std::wstring	descript;

	unsigned long	ulLinkID[LINK_MAX_COUNT];	// linkID，最多能link四个
	__int64			conflict;		// 与其它层互斥标志，每一位代表某一层是否互斥

	double			transform[6];	// transform matrix
	double			boundingbox[4];	// bounding box

	int				image;			// image index，只是显示用，不保存到文件

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

	// 缩放图片到m_image的大小
	HBITMAP StretchImage( HBITMAP hSrc, BITMAPINFO* pInfo);
	
	BOOL InitDefaultModel();

public:
	CShowPartLoader();
	~CShowPartLoader();

	int Load();

	// 保存配置
	bool Save( LPCTSTR pszFile);

	TShowPartContainer& GetAllParts() { return m_vShowParts; }

public:

	CImageList& GetImageList() { return m_image; }

	int GetImageIndex( LPCTSTR pszImage);

public:

	int GetIndexFromID( unsigned long id);

	ShowPartItem* GetItemFromID( unsigned long id);

public:
	
	// 得到男女裸模部件
	void GetDefaultParts( TShowPartPtrContainer& tspc, int sex);

	// 得到层为layer的部件
	void GetPartsByLayer( TShowPartPtrContainer& tspc, int sex, int layer);

	// 得到所有套件的名称，先定为svg文件的目录名
	void GetAllSuitName( std::set<std::wstring>& suits, int sex);

	// 得到名称匹配套件的部件
	void GetPartsBySuitName( TShowPartPtrContainer& tspc, int sex, const wchar_t* szSuit);

	// 得到类型为type的部件
	void GetPartsByType( TShowPartPtrContainer& tspc, int sex, long type);

public:

	// 添加一部件
	bool AddPart( const std::wstring& file, int sex, int layer, int type);

	// 删除一部件
	bool DeletePart( unsigned long ulID);

	// 得到当前最大id
	unsigned long GetMaxID() { return m_maxID; }

public:
	static void GetSvgFullPath( std::wstring& fullpath);
	static void GetTgaFullPath( std::wstring& fullpath);
};