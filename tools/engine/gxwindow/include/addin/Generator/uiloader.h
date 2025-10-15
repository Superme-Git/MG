#pragma once

namespace GXWADDIN
{
enum TEXTSTATEFLAG
{
	TSF_DISABLE = 0,
	TSF_NORMAL = 1,
	TSF_MOUSEON = 2,
	TSF_DOWN = 3,
	TSF_SELECT = 3,
	TSF_DOWN_MOUSEON = 4,
	TSF_SELECT_MOUSEON = 4,
	TSF_STATE_MAXNUM = 4,
};

struct TEXTSTATE
{
	GXWINDOW::FONTEFEECTFLAG effect; // ����Ч��, ��bUnderline��bShadow��bOutline����϶���
	GXWINDOW::COLOR	color;	// ������ɫ
	GXWINDOW::COLOR	color2;	// ����ɫ
	GXWINDOW::COLOR	effectColor;
	bool			bUnderline;
	bool			bShadow;
	bool			bOutline;
	bool			bGraudal;	// �Ƿ�ʹ�ý���ɫ

	TEXTSTATE() : effect( GXWINDOW::FET_NONE), color( 0), color2( 0), effectColor( 0), bUnderline( false), bShadow( false), bOutline( false), bGraudal( false)
	{
	}
	TEXTSTATE( const TEXTSTATE& src) : effect( src.effect), color( src.color), color2( src.color2), effectColor( src.effectColor), bUnderline( src.bUnderline), bShadow( src.bShadow), bOutline( src.bOutline), bGraudal( src.bGraudal)
	{
	}

	TEXTSTATE& operator=( const TEXTSTATE& src)
	{
		if ( this == &src)
			return *this;

		effect = src.effect;
		color = src.color;
		color2 = src.color2;
		effectColor = src.effectColor;
		bUnderline = src.bUnderline;
		bShadow = src.bShadow;
		bOutline = src.bOutline;
		bGraudal = src.bGraudal;

		return *this;
	}
};
class CTextStateMap : public std::map<int, TEXTSTATE>
{
public:
	TEXTSTATE* QueryTextState( int flag)
	{
		iterator it = find( flag);
		if ( it == end())
			return NULL;
		else
			return &it->second;
	}
};

struct TEXTFORMAT
{
	int				nFontType;
	CTextStateMap	textStateMap;
};
typedef TEXTFORMAT GUIEditorFontProfile;
class CTextFormatMap : public std::map<std::wstring, TEXTFORMAT>
{
public:
	TEXTFORMAT* QueryTextFormat( const wchar_t* name)
	{
		if ( NULL == name)
			return NULL;

		iterator it = find( name);
		if ( it == end())
			return NULL;
		else
			return &it->second;
	}
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// ���ӹ�ϵ��Ȼ��gwxe.exe�������ɹ�������, ���Դ˴�ֻ������ؼ����Լ���, ���豣��ɿؼ����ṹ
class CComponentAttributes
{
public:
	typedef std::map<std::wstring, std::wstring> StrMap;
	typedef std::map<std::wstring, CComponentAttributes> AttrMap;
	typedef std::vector<CComponentAttributes> AttrVec;
	typedef std::vector<std::wstring> AttrOrder;
private:
	StrMap		m_strAttrs;
	AttrMap		m_childAttrs;
	AttrOrder	m_childAttrOrders;
	AttrVec		m_slots;		// ���ڴ��ͬ��һ�����ڵ��ͬ���ڵ�, ��������еȵ�
public:
	CComponentAttributes();
	virtual ~CComponentAttributes();

	const wchar_t* QueryAttribute( const wchar_t* key, const wchar_t* defaultValue = NULL) const;
	int QueryIntegerAttribute(const wchar_t* key, int defaultValue = 0) const;
	bool QueryBoolAttribute( const wchar_t* key, bool defaultValue = false) const;
	GXWINDOW::COLOR QueryColorAttribute( const wchar_t* key, GXWINDOW::COLOR defaultvalue = 0x00000000) const;
	const CComponentAttributes* QueryChildAttribute( const wchar_t* key) const;
	
	const AttrMap& GetChildMap() const;
	const AttrOrder& GetChildOrder() const;
	
	int GetSlotCount() const;
	const CComponentAttributes& GetSlotAttribute( int n) const;

	void InsertAttribute( const wchar_t* key, const wchar_t* value);
	void InsertChildAttribute( const wchar_t* key, const CComponentAttributes& value);
	CComponentAttributes& NewChildAttribute( const wchar_t* key);

	void Clear();

	CComponentAttributes& operator=( const CComponentAttributes& src);
};

class CGuiLoader
{
public:
	virtual CComponentAttributes LoadGuiFile( const wchar_t* filename, bool bReadImagePFSMode) = 0;
};

class CGuiGenerator
{
	typedef std::map<std::wstring, CGuiGenerator> GeneratorMap;

	CGuiLoader*				m_pguiloader;
	std::wstring			m_filename;
	CComponentAttributes	m_ca;

	static bool				s_bSetTransparentAttr;	// �����Ƿ�Ҫ���ÿؼ���Transparent����, Ĭ�ϴ���
	static bool				s_bReadImgPFSMode;
	static CTextFormatMap	s_tfm;
	static std::wstring		s_path;
	static GeneratorMap		s_genmap;
public:
	virtual ~CGuiGenerator();

	void Clear();
	void Reload();

	virtual void GenerateComponent( GXWINDOW::CComponent& c, const wchar_t* name);
	virtual void GenerateBKComponent( GXWINDOW::CBKComponent& c, const wchar_t* name);
	virtual void GenerateSliderBar( GXWINDOW::CSliderBar& sb, const wchar_t* name);
	virtual void GenerateProgressBar( GXWINDOW::CProgressBar& pb, const wchar_t* name);
	virtual void GenerateScrollBar( GXWINDOW::CScrollBar& sb, const wchar_t* name);
	virtual void GenerateImageButton( GXWINDOW::CImageButton& ib, const wchar_t* name) ;
	virtual void GenerateTextButton( GXWINDOW::CTextButton& tb, const wchar_t* name);
	virtual void GenerateTileImageButton( GXWINDOW::CTileImageButton& tib, const wchar_t* name);
	virtual void GenerateTextImageButton( GXWINDOW::CTextImageButton& tib, const wchar_t* name);
	virtual void GenerateImageCheckButton( GXWINDOW::CImageCheckButton& icb, const wchar_t* name);
	virtual void GenerateTextCheckButton( GXWINDOW::CTextCheckButton& tcb, const wchar_t* name);
	virtual void GenerateImageRadioButton( GXWINDOW::CImageRadioButton& irb, const wchar_t* name);
	virtual void GenerateTextRadioButton( GXWINDOW::CTextRadioButton& trb, const wchar_t* name);
	virtual void GenerateTextImageRadioButton( GXWINDOW::CTextImageRadioButton& tirb, const wchar_t* name);
	virtual void GenerateTextImageCheckButton( GXWINDOW::CTextImageCheckButton& ticb, const wchar_t* name);
	virtual void GenerateCombinationEditArea( GXWINDOW::CCombinationEditArea& cea, const wchar_t* name);
	virtual void GenerateCombinationSingleLineEditArea( GXWINDOW::CCombinationSingleLineEditArea& cslea, const wchar_t* name);
	virtual void GeneratePanel( GXWINDOW::CPanel& p, const wchar_t* name);
	virtual void GenerateBKPanel( GXWINDOW::CBKPanel& bp, const wchar_t* name);
	virtual void GenerateFrame( GXWINDOW::CFrame& f, const wchar_t* name);
	virtual void GenerateImageComponent( GXWINDOW::CImageComponent& ic, const wchar_t* name);
	virtual void GenerateTileImageComponent( GXWINDOW::CTileImageComponent& tic, const wchar_t* name);
	virtual void GenerateListBox( GXWINDOW::CListBox& lb, GXWINDOW::CListDataModule& ldm, const wchar_t* name);
	virtual void GenerateComboBox( GXWINDOW::CComboBox& cb, GXWINDOW::CListDataModule& ldm, const wchar_t* name);
	virtual void GenerateScrollPanel( GXWINDOW::CScrollPanel& sp, const wchar_t* name);
	virtual void GenerateTabControl( GXWINDOW::CTabControl& tc, const wchar_t* name);
	virtual void GenerateTable( GXWINDOW::CTable& t, GXWINDOW::CTableDataModule& tdm, const wchar_t* name);
	virtual void GenerateCombinationTextArea( GXWINDOW::CCombinationTextArea& cta, const wchar_t* name);
	virtual void GenerateTextComponent( GXWINDOW::CTextComponent& tc, const wchar_t* name);
	virtual void GenerateTree( GXWINDOW::CTree& t, const wchar_t* name);
	virtual void GenerateTextArea( GXWINDOW::CTextArea& ta, const wchar_t* name);
	virtual void GenerateEditArea( GXWINDOW::CEditArea& ea, const wchar_t* name);

	const wchar_t* QueryComponentAttribute( const wchar_t* name, const wchar_t* key, const wchar_t* defaultValue = NULL) const;
	int QueryComponentIntegerAttribute( const wchar_t* name, const wchar_t* key, int defaultValue = 0) const;
	bool QueryComponentBoolAttribute( const wchar_t* name, const wchar_t* key, bool defaultValue = false) const;
	GXWINDOW::COLOR QueryComponentColorAttribute( const wchar_t* name, const wchar_t* key, GXWINDOW::COLOR defaultvalue = 0x00000000) const;

	static void SetGuiFilePath( const wchar_t* path);	// filenameָ���ļ�������·��, ��ȡ�ļ�ʱ��ȫ·����path+filename
	static void SetReadImagePFSMode( bool b);
	static void SetComponentTransparentAttr( bool b);	// �����Ƿ�Ҫ���ÿؼ���Transparent����
	static void LoadTextFormat( const wchar_t* name);	// ����GUIEditor���ɵ�������ʽ����
	static const GUIEditorFontProfile* GetGuiEditorFontProfile( const wchar_t* name); // ��nameֵ��ȡһ��GUIEditor2.xml�е����巽��
	static int  GetGuiEditorFontType( const wchar_t* name);	// ����nameֵ��ȡһ��GUIEditor2.xml���巽��������id
	static const TEXTSTATE* GetGuiEditorTextState( const wchar_t* name, TEXTSTATEFLAG state); // ����nameֵ��ָ��state��ȡһ��GUIEditor2.xml���巽���е���Ӧ״̬������Ϣ
	static const TEXTFORMAT* GetGuiEditorTextFormat( const wchar_t* name);	// ����nameֵ��ȡһ��GUIEditor2.xml���巽��
	static CGuiGenerator& GetGenerator( const wchar_t* filename, CGuiLoader* ploader = NULL);

	static void FreeAlllGenerator();
private:
	void Load();
	CGuiGenerator( const wchar_t* filename, CGuiLoader* ploader = NULL, bool bReadImagePFSMode = false);

	// �Ź���ͼƬ��Դ����
	virtual void GenerateTileImage( GXWINDOW::CImagesFill& ti, int nImageIndex, const CComponentAttributes& ca, int componentw, int componenth, GXWINDOW::COLOR clr, bool bVTile, bool bHTile);

	virtual void GenerateTileImage( GXWINDOW::CImagesFill& ti, int nImageIndex, int x, int y, int w, int h, int w0, int h0, int w1, int h1, int componentw, int componenth, GXWINDOW::COLOR clr, bool bVTile, bool bHTile);
	virtual void GenerateButton( GXWINDOW::CAbstractButton& ab, const wchar_t* name);
	virtual void GenerateCheckButton( GXWINDOW::CAbstractCheckButton& acb, const wchar_t* name);
	virtual void GenerateRadioButton( GXWINDOW::CAbstractRadioButton& arb, const wchar_t* name);
	virtual void GenerateTextable( GXWINDOW::CTextable& ta, const CComponentAttributes& ca);
	virtual void GenerateSimpleTextable( GXWINDOW::CSimpleTextable& sta, const wchar_t* name);
	virtual void GenerateListDataModule( GXWINDOW::CListDataModule& ldm, const CComponentAttributes ca);
	virtual void GenerateTabControlButton( GXWINDOW::CAbstractRadioButton* prb, const CComponentAttributes& ca, int btnw, int btnh, GXWINDOW::COLOR clr); // clr�����ɫ
	virtual void GenerateSubScrollBar( GXWINDOW::CScrollBar& scb, const CComponentAttributes& ca);
};

}