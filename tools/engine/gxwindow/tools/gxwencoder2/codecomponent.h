#pragma once

class CPropertyMap;
class CComponent;
class CCodeOut;

class CCodeComponent
{
public:
	CCodeComponent();
	virtual ~CCodeComponent();
protected:
	virtual std::string CodeOutImageParam( const CComponent& component, const CComponent& parent, CCodeOut& initialize, const std::string& parentvariant, const std::string& strFuncName, CContext& context, const char* appandparms = NULL);
	virtual void CodeOutBackGround( const std::string& bgcomponentname, const CComponent& component, CCodeOut& CodeOut, CContext& context, const char* bgimagetypename = NULL, 
									const char* bgcomponentnamedec = NULL, const char* clientWidth = NULL, const char* clientHeight = NULL, bool bSubScope = true);
public:
	virtual bool CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context);
	virtual bool CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context);
	virtual void CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeBaseComponent : public CCodeComponent
{
public:
	CCodeBaseComponent();
	virtual ~CCodeBaseComponent();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeBKBaseComponent : public CCodeBaseComponent
{
public:
	CCodeBKBaseComponent();
	virtual ~CCodeBKBaseComponent();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeFrame : public CCodeComponent
{
public:
	CCodeFrame();
	virtual ~CCodeFrame();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeButton : public CCodeComponent
{
public:
	CCodeButton();
	~CCodeButton();

	virtual bool CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context);
	virtual bool CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context);
	virtual void CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse);
};

class CCodeImageButton : public CCodeButton
{
public:
	CCodeImageButton();
	virtual ~CCodeImageButton();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeTextButton : public CCodeImageButton
{
public:
	CCodeTextButton();
	virtual ~CCodeTextButton();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);

	virtual void CodeOutButtonImage( const CComponent& component, CCodeOut& initialize, CCodeOut& release, CContext& context);
};

class CCodeCheckButton : public CCodeButton
{
public:
	CCodeCheckButton();
	~CCodeCheckButton();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual void CodeOutButtonTileImage( const CComponent& component, CCodeOut& initialize, CCodeOut& release, CContext& context);
};

class CCodeTextCheckButton : public CCodeCheckButton
{
public:
	CCodeTextCheckButton();
	~CCodeTextCheckButton();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
};

class CCodeRadioButton : public CCodeCheckButton
{
public:
	CCodeRadioButton();
	~CCodeRadioButton();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeTextRadioButton : public CCodeTextCheckButton
{
public:
	CCodeTextRadioButton();
	~CCodeTextRadioButton();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
};

class CCodePanel : public CCodeComponent
{
public:
	CCodePanel();
	virtual ~CCodePanel();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeBKPanel : public CCodeBKBaseComponent
{
public:
	CCodeBKPanel();
	virtual ~CCodeBKPanel();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeScrollPanel : public CCodePanel
{
public:
	CCodeScrollPanel();
	virtual ~CCodeScrollPanel();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeTextComponent : public CCodeComponent
{
public:
	CCodeTextComponent();
	virtual ~CCodeTextComponent();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
};

class CCodeImageComponent : public CCodeComponent
{
public:
	CCodeImageComponent();
	virtual ~CCodeImageComponent();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeTextArea : public CCodeComponent
{
public:
	CCodeTextArea();
	virtual ~CCodeTextArea();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeOriTextArea : public CCodeComponent
{
public:
	CCodeOriTextArea();
	virtual ~CCodeOriTextArea();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeOriEidtArea : public CCodeComponent
{
public:
	CCodeOriEidtArea();
	virtual ~CCodeOriEidtArea();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeSingleEidtArea : public CCodeComponent
{
public:
	CCodeSingleEidtArea();
	virtual ~CCodeSingleEidtArea();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeMultiLineEidtArea : public CCodeSingleEidtArea
{
public:
	CCodeMultiLineEidtArea();
	virtual ~CCodeMultiLineEidtArea();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeListBox : public CCodeBKBaseComponent
{
public:
	CCodeListBox();
	virtual ~CCodeListBox();
	virtual bool CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context);
	virtual bool CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context);
	virtual void CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);

	virtual void DeclarationListDataModule( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual void SetupListDataModule( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeComboBox : public CCodeBaseComponent
{
public:
	CCodeComboBox();
	virtual ~CCodeComboBox();

	virtual bool CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context);
	virtual bool CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context);
	virtual void CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeTable : public CCodeBKBaseComponent
{
public:
	CCodeTable();
	virtual ~CCodeTable();

	virtual bool CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context);
	virtual bool CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context);
	virtual void CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
	virtual void SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context);

	virtual void DeclarationTableDataModule( const CComponent& component, CCodeOut& declaration, CContext& context);
	virtual void CodeOutCallOnHeaderClickFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse);
	virtual void SetupTableDataModule( const CComponent& component, CCodeOut& CodeOut, CContext& context);
};

class CCodeSliderBar : public CCodeComponent
{
public:
	CCodeSliderBar();
	virtual ~CCodeSliderBar();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeScrollBar : public CCodeComponent
{
public:
	CCodeScrollBar();
	virtual ~CCodeScrollBar();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual void CodeOutScrollBarSetup( const std::string& scbvariant, const CComponent& scbcomponent, const std::string& decinscope, CCodeOut& CodeOut, CContext& context, const char* scbwidth = NULL, const char* scbheight = NULL);
};

class CCodeProgressBar : public CCodeComponent
{
public:
	CCodeProgressBar();
	virtual ~CCodeProgressBar();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeTab : public CCodeBKBaseComponent
{
public:
	CCodeTab();
	virtual ~CCodeTab();
	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual bool CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context);
};

class CCodeAppleButton : public CCodeButton
{
public:
	CCodeAppleButton();
	virtual ~CCodeAppleButton();

	virtual bool CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeAppleMenu : public CCodePanel
{
public:
	CCodeAppleMenu();
	virtual ~CCodeAppleMenu();

	virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
	virtual void AnalyseAppleMenu( CComponent& component);
private:
	void AnalyseAppleMenu( CComponent& menu, const CComponent& submenu, const char* parent = NULL);
};

class CCodeTextImageRadioButton : public CCodeTextRadioButton
{
public:
	CCodeTextImageRadioButton();
	virtual ~CCodeTextImageRadioButton();

	//virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

class CCodeTextImageCheckButton : public CCodeTextCheckButton
{
public:
	CCodeTextImageCheckButton();
	virtual ~CCodeTextImageCheckButton();

	//virtual bool CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const char* const COMTYPE_FRAME = "Frame";
const char* const COMTYPE_BASE = "Base";
const char* const COMTYPE_BKBASE = "BKBase";
const char* const COMTYPE_SCROLLBAR = "ScrollBar";
const char* const COMTYPE_STATIC = "Static";
const char* const COMTYPE_IMAGE = "Image";
const char* const COMTYPE_TEXT = "Text";
const char* const COMTYPE_EDIT = "Edit";
const char* const COMTYPE_ORITEXT = "TextArea";
const char* const COMTYPE_ORIEDIT = "EditArea";
const char* const COMTYPE_MULTIEDIT = "MultiEdit";
const char* const COMTYPE_BUTTONIMAGE = "ButtonImage";
const char* const COMTYPE_BUTTONTEXT = "ButtonText";
const char* const COMTYPE_BUTTONRADIO = "ButtonRadio";
const char* const COMTYPE_TEXTBUTTONRADIO = "TextRadioButton";
const char* const COMTYPE_BUTTONCHECK = "ButtonCheck";
const char* const COMTYPE_TEXTBUTTONCHECK = "TextCheckButton";
const char* const COMTYPE_PROGRESSBAR = "ProgressBar";
const char* const COMTYPE_SLIDERBAR = "SliderBar";
const char* const COMTYPE_COMBOBOX = "ComboBox";
const char* const COMTYPE_LISTBOX = "ListBox";
const char* const COMTYPE_TABLE = "Table";
const char* const COMTYPE_PANEL = "Panel";
const char* const COMTYPE_BKPANEL = "BKPanel";
const char* const COMTYPE_TAB = "Tab";
const char* const COMTYPE_SCROLLPANEL = "ScrollPanel";
const char* const COMTYPE_APPLEBUTTON = "AppleButton";
const char* const COMTYPE_APPLEMENU = "AppleMenu";
const char* const COMTYPE_TEXTIMGRADIOBUTTON = "TextImageRadioButton";
const char* const COMTYPE_TEXTIMGCHECKBUTTON = "TextImageCheckButton";

// 其他
const char* const COMTYPE_BUTTON = "Button";	// 按钮统称
const char* const COMTYPE_BGIMAGE = "Image1";	// 背景图片
const char* const COMTYPE_CONTENT = "Content";	// 容器的子控件集合
const char* const COMTYPE_IMGNORMAL = COMTYPE_BGIMAGE;	// 正常态
const char* const COMTYPE_IMGMOUSEON = "Image2";		// 鼠标悬浮态
const char* const COMTYPE_IMGDOWN = "Image3";			// 按下、选中态
const char* const COMTYPE_IMGSELECTMOUSEON = "Image4";	// 选中时鼠标悬浮态
const char* const COMTYPE_IMGDISABLE = "Image0";		// 无效态
const char* const COMTYPE_MULTILINE = "multiline";		// 多行文本
const char* const COMTYPE_TABLEHEADER = "TableHeader";	// 表头, 主要用于标识是否需要生成表头点击响应函数
const char* const COMTYPE_TABLECOLUMN = "Column";		// 列
const char* const COMTYPE_SCBUPDISABLE = "ImageUp0";		// 滚动条上按钮无效态
const char* const COMTYPE_SCBUPNORMAL = "ImageUp1";		// 滚动条上按钮常态
const char* const COMTYPE_SCBUPON = "ImageUp2";		// 滚动条上按钮鼠标悬浮态
const char* const COMTYPE_SCBUPDOWN = "ImageUp3";			// 滚动条上按钮按下态
const char* const COMTYPE_SCBDOWNDISABLE = "ImageDown0";// 滚动条下按钮无效态
const char* const COMTYPE_SCBDOWNNORMAL = "ImageDown1";	// 滚动条下按钮常态
const char* const COMTYPE_SCBDOWNON = "ImageDown2";	// 滚动条下按钮鼠标悬浮态
const char* const COMTYPE_SCBDOWNDOWN = "ImageDown3";		// 滚动条下按钮按下态
const char* const COMTYPE_SCBRUBNORMAL = "ImageBlock1";	// 滚动块常态
const char* const COMTYPE_SCBRUBON = "ImageBlock2";	// 滚动块鼠标悬浮态
const char* const COMTYPE_SCBRUBDOWN = "ImageBlock3";	// 滚动块按下移动态
const char* const COMTYPE_SCBSLOT = "ImageSlot";		// 滚动槽
const char* const COMTYPE_VSCROLLBAR = "VScrollBar";	// 垂直滚动条
const char* const COMTYPE_HSCROLLBAR = "HScrollBar";	// 水平滚动条
const char* const COMTYPE_LDT_TEXT = "text";			// listbox textdatamodule
const char* const COMTYPE_LDT_CLRTEXT = "colortext";	// listbox colortextdatamodule
const char* const COMTYPE_LDT_IMG = "image";			// listbox imagedatamodule
const char* const COMTYPE_COMBOBOX_IMGBOX = "ImageBox";	// 组合框的背景图
const char* const COMTYPE_COMBOBOX_IMGLIST = "ImageList";// 组合框下拉列表背景图
const char* const COMTYPE_TABLE_HEADER_CELL = "ImageHead"; // 表头格子图片
const char* const COMTYPE_TABLE_CELL = "ImageCell";			// 单元格图片
const char* const COMTYPE_LISTBOX_LINE = "ImageLine";		// 列表框行图片
const char* const COMTYPE_HOOKEDIT = "hookeditkey";			// 当选择处理键盘消息时，用于表示edit控件
const char* const COMTYPE_APPLEBUTTON_BIGDISABLE = "Image5";			// 苹果按钮无效态大图
const char* const COMTYPE_APPLEBUTTON_BIGNORMAL = "Image6";				// 苹果按钮正常态大图
const char* const COMTYPE_APPLEBUTTON_BIGMOUSEON = "Image7";			// 苹果按钮焦点态大图
const char* const COMTYPE_APPLEBUTTON_BIGDOWN = "Image8";				// 苹果按钮按下态大图


static class CCodeGeneratePropertyMap
{
	typedef std::map<std::string,CCodeComponent*> CCodeComponentMap;
	typedef std::map<std::string,std::string> CCodeTypeMap;
	typedef CCodeTypeMap CFrameModeGeneratorMap;

	CCodeComponentMap		m_map;
	CCodeTypeMap			m_codes;
	CFrameModeGeneratorMap	m_genmap;
public:
	CCodeGeneratePropertyMap()
	{
		m_map[ COMTYPE_FRAME] = new CCodeFrame;
		m_codes[ COMTYPE_FRAME] = "GXWINDOW::CFrame";
		m_genmap[ COMTYPE_FRAME] = "GenerateFrame";

		m_map[ COMTYPE_BASE] = new CCodeBaseComponent;
		m_codes[ COMTYPE_BASE] = "GXWINDOW::CComponent";
		m_genmap[ COMTYPE_BASE] = "GenerateComponent";

		m_map[ COMTYPE_BKBASE] = new CCodeBKBaseComponent;
		m_codes[ COMTYPE_BKBASE] = "GXWINDOW::CBKComponent";
		m_genmap[ COMTYPE_BKBASE] = "GenerateBKComponent";

		m_map[ COMTYPE_SCROLLBAR] = new CCodeScrollBar;
		m_codes[ COMTYPE_SCROLLBAR] = "GXWINDOW::CScrollBar";
		m_genmap[ COMTYPE_SCROLLBAR] = "GenerateScrollBar";

		m_map[ COMTYPE_STATIC] = new CCodeTextComponent;
		m_codes[ COMTYPE_STATIC] = "GXWINDOW::CTextComponent";
		m_genmap[ COMTYPE_STATIC] = "GenerateTextComponent";

		m_map[ COMTYPE_IMAGE] = new CCodeImageComponent;
		m_codes[ COMTYPE_IMAGE] = "GXWINDOW::CImageComponent";
		m_genmap[ COMTYPE_IMAGE] = "GenerateImageComponent";

		m_map[ COMTYPE_TEXT] = new CCodeTextArea;
		m_codes[ COMTYPE_TEXT] = "GXWINDOW::CCombinationTextArea";
		m_genmap[ COMTYPE_TEXT] = "GenerateCombinationTextArea";

		m_map[ COMTYPE_ORIEDIT] = new CCodeOriEidtArea;
		m_codes[ COMTYPE_ORIEDIT] = "GXWINDOW::CEditArea";
		m_genmap[ COMTYPE_ORIEDIT] = "GenerateEditArea";

		m_map[ COMTYPE_ORITEXT] = new CCodeOriTextArea;
		m_codes[ COMTYPE_ORITEXT] = "GXWINDOW::CTextArea";
		m_genmap[ COMTYPE_ORITEXT] = "GenerateTextArea";

		m_map[ COMTYPE_EDIT] = new CCodeSingleEidtArea;
		m_codes[ COMTYPE_EDIT] = "GXWINDOW::CCombinationSingleLineEditArea";
		m_genmap[ COMTYPE_EDIT] = "GenerateCombinationSingleLineEditArea";

		m_map[ COMTYPE_MULTIEDIT] = new CCodeMultiLineEidtArea;
		m_codes[ COMTYPE_MULTIEDIT] = "GXWINDOW::CCombinationEditArea";
		m_genmap[ COMTYPE_MULTIEDIT] = "GenerateCombinationEditArea";

		m_map[ COMTYPE_BUTTONIMAGE] = new CCodeImageButton;
		m_codes[ COMTYPE_BUTTONIMAGE] = "GXWINDOW::CImageButton";
		m_genmap[ COMTYPE_BUTTONIMAGE] = "GenerateImageButton";

		m_map[ COMTYPE_BUTTONTEXT] = new CCodeTextButton;
		m_codes[ COMTYPE_BUTTONTEXT] = "GXWINDOW::CTextImageButton";
		m_genmap[ COMTYPE_BUTTONTEXT] = "GenerateTextImageButton";

		m_map[ COMTYPE_BUTTONRADIO] = new CCodeRadioButton;
		m_codes[ COMTYPE_BUTTONRADIO] = "GXWINDOW::CImageRadioButton";
		m_genmap[ COMTYPE_BUTTONRADIO] = "GenerateImageRadioButton";

		m_map[ COMTYPE_TEXTBUTTONRADIO] = new CCodeTextRadioButton;
		m_codes[ COMTYPE_TEXTBUTTONRADIO] = "GXWINDOW::CTextRadioButton";
		m_genmap[ COMTYPE_TEXTBUTTONRADIO] = "GenerateTextRadioButton";

		m_map[ COMTYPE_BUTTONCHECK] = new CCodeCheckButton;
		m_codes[ COMTYPE_BUTTONCHECK] = "GXWINDOW::CImageCheckButton";
		m_genmap[ COMTYPE_BUTTONCHECK] = "GenerateImageCheckButton";

		m_map[ COMTYPE_TEXTBUTTONCHECK] = new CCodeTextCheckButton;
		m_codes[ COMTYPE_TEXTBUTTONCHECK] = "GXWINDOW::CTextCheckButton";
		m_genmap[ COMTYPE_TEXTBUTTONCHECK] = "GenerateTextCheckButton";

		m_map[ COMTYPE_PROGRESSBAR] = new CCodeProgressBar;
		m_codes[ COMTYPE_PROGRESSBAR] = "GXWINDOW::CProgressBar";
		m_genmap[ COMTYPE_PROGRESSBAR] = "GenerateProgressBar";

		m_map[ COMTYPE_SLIDERBAR] = new CCodeSliderBar;
		m_codes[ COMTYPE_SLIDERBAR] = "GXWINDOW::CSliderBar";
		m_genmap[ COMTYPE_SLIDERBAR] = "GenerateSliderBar";

		m_map[ COMTYPE_COMBOBOX] = new CCodeComboBox;
		m_codes[ COMTYPE_COMBOBOX] = "GXWINDOW::CComboBox";
		m_genmap[ COMTYPE_COMBOBOX] = "GenerateComboBox";

		m_map[ COMTYPE_LISTBOX] = new CCodeListBox;
		m_codes[ COMTYPE_LISTBOX] = "GXWINDOW::CListBox";
		m_genmap[ COMTYPE_LISTBOX] = "GenerateListBox";

		m_map[ COMTYPE_TABLE] = new CCodeTable;
		m_codes[ COMTYPE_TABLE] = "GXWINDOW::CTable";
		m_genmap[ COMTYPE_TABLE] = "GenerateTable";

		m_map[ COMTYPE_PANEL] = new CCodePanel;
		m_codes[ COMTYPE_PANEL] = "GXWINDOW::CPanel";
		m_genmap[ COMTYPE_PANEL] = "GeneratePanel";

		m_map[ COMTYPE_BKPANEL] = new CCodeBKPanel;
		m_codes[ COMTYPE_BKPANEL] = "GXWINDOW::CBKPanel";
		m_genmap[ COMTYPE_BKPANEL] = "GenerateBKPanel";

		m_map[ COMTYPE_TAB] = new CCodeTab;
		m_codes[ COMTYPE_TAB] = "GXWINDOW::CTabControl";
		m_genmap[ COMTYPE_TAB] = "GenerateTabControl";

		m_map[ COMTYPE_SCROLLPANEL] = new CCodeScrollPanel;
		m_codes[ COMTYPE_SCROLLPANEL] = "GXWINDOW::CScrollPanel";
		m_genmap[ COMTYPE_SCROLLPANEL] = "GenerateScrollPanel";

		m_map[ COMTYPE_APPLEBUTTON] = new CCodeAppleButton;
		m_codes[ COMTYPE_APPLEBUTTON] = "GXWINDOW::CAppleButton";
		m_genmap[ COMTYPE_APPLEBUTTON] = "GenerateAppleButton";

		m_map[ COMTYPE_APPLEMENU] = new CCodeAppleMenu;
		m_codes[ COMTYPE_APPLEMENU] = "GXWINDOW::CApplePanel";
		m_genmap[ COMTYPE_APPLEMENU] = "GenerateApplePanel";

		m_map[ COMTYPE_TEXTIMGRADIOBUTTON] = new CCodeTextImageRadioButton;
		m_codes[ COMTYPE_TEXTIMGRADIOBUTTON] = "GXWINDOW::CTextImageRadioButton";
		m_genmap[ COMTYPE_TEXTIMGRADIOBUTTON] = "GenerateTextImageRadioButton";

		m_map[ COMTYPE_TEXTIMGCHECKBUTTON] = new CCodeTextImageCheckButton;
		m_codes[ COMTYPE_TEXTIMGCHECKBUTTON] = "GXWINDOW::CTextImageCheckButton";
		m_genmap[ COMTYPE_TEXTIMGCHECKBUTTON] = "GenerateTextImageCheckButton";
	}
	~CCodeGeneratePropertyMap()
	{
		for( CCodeComponentMap::iterator it = m_map.begin(); it != m_map.end(); ++ it)
			delete it->second;
		m_map.clear();
		m_codes.clear();
	}
public:
	CCodeComponent* Query( const char* szType)
	{
		CCodeComponentMap::iterator it = m_map.find( szType);
		if ( it == m_map.end())
			return NULL;
		return it->second;
	}
	const char* GetType( const char* szType)
	{
		CCodeTypeMap::iterator it = m_codes.find( szType);
		if ( it == m_codes.end())
			return NULL;
		std::string& strType = it->second;
		return strType.c_str();
	}
	const char* GetGenFuncName( const char* szType, const char* classname = NULL)
	{
		CCodeTypeMap::iterator it = m_genmap.find( szType);
		if ( it == m_genmap.end())
			return NULL;
		std::string& strType = it->second;
		return strType.c_str();
	}
} g_CodeGeneratePropertyMap;
