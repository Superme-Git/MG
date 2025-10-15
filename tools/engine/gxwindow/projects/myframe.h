#pragma once

const wchar_t	g_text[] = L"文本Character combinations consisting<R/>Escape sequences are typically<R/>used to specify actions<R/>such as<R/>carriage<R/>returns and tab movements on terminals and printers. They are also used to provide<C F=1 C=(255,0,0,128) S=(0,0,255, 128) B=(255,0,0) W=60>[link文本]<P>文本</P></C>";
//const wchar_t	g_text[] = L"文本Character combinations consisting";

class CMyButtonClickNotify : public GXWINDOW::CButtonClickNotify, public GXWINDOW::CBarComponentPositionNotify
{
	HWND		m_hWnd;
public:
	CMyButtonClickNotify()
	{
	}
	~CMyButtonClickNotify()
	{
	}
protected:
	void OnButtonClick( GXWINDOW::CAbstractButton& c)
	{

	}
	void OnBarComponentPosition( GXWINDOW::CBarComponent& c, int pos, int max)
	{
	}
public:
	void SetHwnd( HWND hWnd)
	{
		m_hWnd = hWnd;
	}
};

class TestLayoutManager : public GXWINDOW::CLayoutManager
{
public:
	TestLayoutManager() {};
	virtual ~TestLayoutManager() {};
public:
	virtual void Delete()
	{
		delete this;
	}

	virtual void Layout(GXWINDOW::CPanel& panel)
	{
		int x = 0;
		for ( int i = 0; i < panel.GetChildCount(); i++)
		{
			GXWINDOW::CComponent* c = panel.GetChild( i);
			c->SetPos( x, 0);
			x += c->GetWidth() + 2;
		}
	}

	virtual CLayoutManager* Clone() const 
	{
		return new TestLayoutManager();
	}
};

class CMyFrame : public GXWINDOW::CFrame, virtual public GXWINDOW::CFrameClosedNotify, public GXWINDOW::CButtonClickNotify, virtual public GXWINDOW::CTabChangedNotify
{
	GXWINDOW::CFormatTextDecoder	m_ftd;

	GXWINDOW::CCombinationEditArea m_edit;
	GXWINDOW::CTextButton		m_btnClose;
	GXWINDOW::CImageButton		m_btnTeshunengli;
	GXWINDOW::CImageButton		m_btnJineng;
	GXWINDOW::CScrollBar		m_scrollbar;
	GXWINDOW::CSliderBar		m_sliderbar;

	GXWINDOW::CCombinationSingleLineEditArea			m_editline;
	//	GXWINDOW::CScrollPanel			m_editsp;

	GXWINDOW::CBKPanel			m_Panel;
	GXWINDOW::CScrollPanel		m_scrollPanel;

	GXWINDOW::CTextListDataModule		m_tdmListBox;
	GXWINDOW::CScrollPanel				m_spListBox;
	GXWINDOW::CListBox					m_ListBox;

	GXWINDOW::CTable					m_Table; 
	GXWINDOW::CTextTableDataModule		m_ttdm;

	GXWINDOW::CComboBox					m_combobox;
	GXWINDOW::CPanel					m_controls;

	//GXWINDOW::CTextDrawManager		m_tdm;
	GXWINDOW::CTextComponent		m_tc;

	GXWINDOW::CScrollPanel				m_spTree;
	GXWINDOW::CTextTreeDataModal		m_treedm;
	GXWINDOW::CTree						m_tree;

	GXWINDOW::CTabControl				m_tab;

	GXWINDOW::CCombinationTextArea		m_textarea;
	//	GXWINDOW::CTextArea				m_textarea;
	//	GXWINDOW::CScrollPanel			m_spta;

	GXWINDOW::CTextButton			m_tb;
	GXWINDOW::CCombinationEditArea		m_editarea;

	GXWINDOW::CAnimateImageComponent m_animate;

	//	GXWINDOW::CScrollPanel			m_spea;

	int			m_nImageIndex;

	GXWINDOW::CImageComponent			m_imgc;
	GXWINDOW::CTextCheckButton			m_txtcheckbtn;
	GXWINDOW::CImageCheckButton			m_checkbtn;
	GXWINDOW::CImageRadioButton			m_radio1;
	GXWINDOW::CImageRadioButton			m_radio2;
	GXWINDOW::CTextButton				m_txtbtn;
	GXWINDOW::CBKComponent				m_bkbase;
	GXWINDOW::CImageListDataModlue		m_imgldm;
	//GXWINDOW::CColorTextListDataModule	m_clrldm;
public:
	CMyFrame()
	{
		int nImageIndex1 = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( L"RoleInfo.bmp");
		int nImageIndex2 = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( L"2ui04.tga");
		int nImageIndex3 = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( L"chatlog 512.tga");

		FrameClosedNotifyAdd( *this);
		SetPos( 100, 100);
		SetSize( 420, 350);
		SetAutoClose( true);
		SetMoveStyle( GXWINDOW::FMS_ALL);
		//SetBackGroundStyleColor( GXWINDOW::MakeColor( 0, 0, 0, 128));

		{
			SetBackGroundStyle( GXWINDOW::BGS_IMAGE);
			GXWINDOW::CImagesFill& bgf = GetTilesBackGround();
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 105, 101, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_TOPLEFT);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 161, 101, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_TOP);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 350, 101, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_TOPRIGHT);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 105, 137, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_LEFT);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 161, 137, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_CENTER);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 350, 137, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_RIGHT);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 105, 398, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_BOTTOMLEFT);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 161, 398, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_BOTTOM);
			bgf.SetTileImage( GXWINDOW::CImageParams( nImageIndex3, 350, 398, GXWINDOW::MakeColor( 255,255,255,255)), GXWINDOW::IFP_BOTTOMRIGHT);
			bgf.SetTileSize( 189, 261);
			bgf.SetGridSize( 56, 36, GXWINDOW::IFP_TOPLEFT);
			bgf.SetGridSize( 308, 301, GXWINDOW::IFP_CENTER);
			bgf.SetGridSize( 56, 13, GXWINDOW::IFP_BOTTOMRIGHT);
			bgf.SetFillMode( false, false);
		}
		SetMoveStyle( GXWINDOW::FMS_ALL);
		

		GXWINDOW::ImageParams		ipcursor;
		ipcursor.nBitmap = nImageIndex1;
		ipcursor.x = 73;
		ipcursor.y = 470;
		ipcursor.color = GXWINDOW::MakeColor( 255, 255, 255);

		GXWINDOW::CTextAreaProperties::SetDefaultCursor( ipcursor, 2, 14);

		//	SetSize( 256 + 40, 390 + 40);
		//		SetBackGroundStyleColor( GXWINDOW::MakeColor( 255, 0, 0));
		//{
		//	GXWINDOW::COLOR	clrs[1] = { GXWINDOW::MakeColor( 200, 200, 200), };
		//	SetBorderStyleLine( clrs, 1);
		//}

		m_tab.SetPos( 10, 40);
		m_tab.SetSize( 400, 300);
		//m_tab.SetButtonWidth( 20);
		m_tab.TabChangedNotifyAdd( *this);
		InsertComponent( &m_tab);

		m_imgc.SetSize( 383, 27);
		m_imgc.SetPos( 10, 10);
		m_imgc.SetComponentImageNormal( GXWINDOW::CImageParams( 2, 0, 39));
		m_imgc.SetTransparent();
		m_imgc.SetEnable(false);
		//m_imgc.SetZoomScale( 1.5f);
		InsertComponent( &m_imgc);

		//m_checkbtn.SetSize( 152, 34);
		//m_checkbtn.SetPos( 10, 10);
		//m_checkbtn.SetBoxWidth( 34);
		//m_checkbtn.SetButtonBoxStyleImage( GXWINDOW::CImageParams( 2, 356, 254), GXWINDOW::CImageParams( 2, 264, 89));
		////m_checkbtn.SetButtonBoxStyle( GXWINDOW::BBS_TYPAL);
		//InsertComponent( &m_checkbtn);

		//m_txtcheckbtn.SetSize( 50, 20);
		//m_txtcheckbtn.SetPos( 200, 10);
		//m_txtcheckbtn.SetBoxWidth( 20);
		//m_txtcheckbtn.SetComponentText( L"aaa");
		//m_txtcheckbtn.SetButtonBoxStyleImage( GXWINDOW::CImageParams( 2, 356, 254), GXWINDOW::CImageParams( 2, 264, 89));
		//InsertComponent( &m_txtcheckbtn);

		//m_radio1.SetSize( 152, 34);
		//m_radio1.SetPos( 10, 10);
		//m_radio1.SetBoxWidth( 20);
		//m_radio1.SetGroupID( 1);
		//m_radio1.SetButtonBoxStyleImage( GXWINDOW::CImageParams( 2, 356, 254), GXWINDOW::CImageParams( 2, 264, 89));
		//InsertComponent( &m_radio1);

		//m_radio2.SetSize( 152, 34);
		//m_radio2.SetPos( 200, 10);
		//m_radio2.SetBoxWidth( 20);
		//m_radio2.SetGroupID( 1);
		//m_radio2.SetButtonBoxStyleImage( GXWINDOW::CImageParams( 2, 356, 254), GXWINDOW::CImageParams( 2, 264, 89));
		//InsertComponent( &m_radio2);

		//m_txtbtn.SetSize( 60, 20);
		//m_txtbtn.SetPos( 200, 50);
		//m_txtbtn.SetComponentText( L"文字按钮");
		//m_txtbtn.SetComponentTextMouseInColor( GXWINDOW::MakeColor( 255, 0, 0));
		//InsertComponent( &m_txtbtn);

		GXWINDOW::ImageParams		ipframe;

		ipframe.nBitmap = nImageIndex1;
		ipframe.x = 0;
		ipframe.y = 0;
		ipframe.color = GXWINDOW::MakeColor( 255, 255, 255);
		m_Panel.SetBackGroundStyleImage( ipframe);

		//m_btnClose.SetBackGroundStyle( GXWINDOW::BGS_NONE);
		//m_btnClose.SetBorderStyle( GXWINDOW::BS_NONE);
		m_btnClose.SetPos( 235, 4);
		m_btnClose.SetSize( 17, 18);
		m_Panel.InsertComponent( &m_btnClose);
		m_btnClose.ButtonClickNotifyAdd( *this);

		ipframe.x = 0;
		ipframe.y = 391;
		m_btnTeshunengli.SetComponentImageNormal( ipframe);
		ipframe.x = 128;
		m_btnTeshunengli.SetComponentImageMouseIn( ipframe);
		ipframe.x = 192;
		m_btnTeshunengli.SetComponentImageDisable( ipframe);
		ipframe.x = 64;
		m_btnTeshunengli.SetImageButtonDown( ipframe);
		m_btnTeshunengli.SetPos( 18, 356);
		m_btnTeshunengli.SetSize( 64, 22);
		m_Panel.InsertComponent( &m_btnTeshunengli);
		m_btnTeshunengli.ButtonClickNotifyAdd( *this);

		ipframe.x = 0;
		ipframe.y = 415;
		m_btnJineng.SetComponentImageNormal( ipframe);
		ipframe.x = 128;
		m_btnJineng.SetComponentImageMouseIn( ipframe);
		ipframe.x = 192;
		m_btnJineng.SetComponentImageDisable( ipframe);
		ipframe.x = 64;
		m_btnJineng.SetImageButtonDown( ipframe);

		m_btnJineng.SetPos( 80, 356);
		m_btnJineng.SetSize( 64, 22);
		m_Panel.InsertComponent( &m_btnJineng);
		m_btnJineng.ButtonClickNotifyAdd( *this);
		m_btnJineng.SetEnable( false);

		ipframe.nBitmap = nImageIndex2;
		ipframe.x = 246;
		ipframe.y = 254;


		{
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.InsertItem( ipframe);
			m_imgldm.SetImageMouseOnColor( GXWINDOW::MakeColor( 255, 255, 255, 0));
		}

		{
			//m_clrldm.InsertText( L"00000", GXWINDOW::MakeColor( 255, 255, 255), GXWINDOW::MakeColor( 0, 0, 128), GXWINDOW::MakeColor( 0, 0, 255));
			//m_clrldm.InsertText( L"00000", GXWINDOW::MakeColor( 128, 255, 255), GXWINDOW::MakeColor( 0, 0, 128), GXWINDOW::MakeColor( 0, 0, 255));
			//m_clrldm.InsertText( L"00000", GXWINDOW::MakeColor( 128, 128, 255), GXWINDOW::MakeColor( 0, 0, 128), GXWINDOW::MakeColor( 0, 0, 255));
			//m_clrldm.InsertText( L"00000", GXWINDOW::MakeColor( 128, 128, 128), GXWINDOW::MakeColor( 0, 0, 128), GXWINDOW::MakeColor( 0, 0, 255));
			//m_clrldm.InsertText( L"00000", GXWINDOW::MakeColor( 128, 255, 128), GXWINDOW::MakeColor( 0, 0, 128), GXWINDOW::MakeColor( 0, 0, 255));
		}

		//m_scrollbar.SetPos( 10, 50);
		//m_scrollbar.SetSize( 160, 16);
		//m_scrollbar.SetVertical(false);
		m_scrollbar.SetPos( 260, 70);
		m_scrollbar.SetSize( 16, 100);
		m_scrollbar.SetVertical();
		m_scrollbar.SetButtonSize( 16);
		//m_scrollbar.SetSliderScaleStyle( GXWINDOW::SSS_TYPAL);
		//m_scrollbar.SetSliderRubStyle( GXWINDOW::SRS_TYPAL);
		m_scrollbar.SetSliderScaleStyleImage( ipframe);
		m_scrollbar.SetSliderRubStyleImage( GXWINDOW::CImageParams( 2, 265, 254), GXWINDOW::CImageParams( 2, 265, 282), GXWINDOW::CImageParams( 2, 265, 310));
		m_scrollbar.SetTopLeftButtonImage( GXWINDOW::CImageParams( 2, 265, 343), GXWINDOW::CImageParams( 2, 265, 359), GXWINDOW::CImageParams( 2, 265, 375));
		m_scrollbar.SetRightBottomButtonImage( GXWINDOW::CImageParams( 2, 265, 391), GXWINDOW::CImageParams( 2, 265, 407), GXWINDOW::CImageParams( 2, 265, 423));
		m_scrollbar.SetValueMax( 100);
		m_scrollbar.SetRubSize( 28);
		//m_scrollbar.SetSmooth( false);
		m_Panel.InsertComponent( &m_scrollbar);

		m_sliderbar.SetPos( 640, 0);
		m_sliderbar.SetVertical();
		m_sliderbar.SetRubSize( 28);
		m_sliderbar.SetSize( 16, 242);
		//m_sliderbar.SetPos( 80, 40);
		//m_sliderbar.SetSize( 200, 20);
		//m_sliderbar.SetVertical(false);
		//m_sliderbar.SetSmooth( false);
		//m_sliderbar.SetSliderScaleStyle( GXWINDOW::SSS_TYPAL);
		//m_sliderbar.SetSliderRubStyle( GXWINDOW::SRS_TYPAL);
		m_sliderbar.SetSliderScaleStyleImage( ipframe);
		m_sliderbar.SetValueMax( 100);
		m_sliderbar.SetSliderRubStyleImage( GXWINDOW::CImageParams( 2, 265, 254), GXWINDOW::CImageParams( 2, 265, 282), GXWINDOW::CImageParams( 2, 265, 310));
		m_Panel.InsertComponent( &m_sliderbar);

		//m_bkbase.SetPos( 0, 0);
		//m_bkbase.SetSize( 50, 800);
		//m_bkbase.SetBackGroundStyleColor( GXWINDOW::MakeColor( 0,0,255,128));
		//m_Panel.InsertComponent( &m_bkbase);

		ipframe.nBitmap = nImageIndex1;
		m_animate.SetPos( 250, 180);
		m_animate.SetSize( 64, 22);
		ipframe.x = 0;
		ipframe.y = 415;
		m_animate.Insert( ipframe);
		ipframe.x = 128;
		m_animate.Insert( ipframe);
		ipframe.x = 192;
		m_animate.Insert( ipframe);
		ipframe.x = 64;
		m_animate.Insert( ipframe);
		m_Panel.InsertComponent( &m_animate);

		{
			//			m_editline.SetWidth( 120);
			ipframe.x = 75;
			ipframe.y = 470;
			m_editline.ShowCursor( ipframe);

			m_editline.SetPos( 60, 120);
			m_editline.SetSize( 120, 20);

			//{
			//	GXWINDOW::COLOR	clrs[1] = { GXWINDOW::MakeColor( 200, 200, 200), };
			//	m_editline.GetScrollPanel().SetBorderStyleLine( clrs, 1);
			//}
			m_editline.GetEditArea().SetPassword();
//			m_editline.GetEditArea().SetNumber( true);
			m_Panel.InsertComponent( m_editline);
		}

		m_Panel.SetPos( 0, 0);
		m_Panel.SetSize( 656, 390);
		//m_Panel.SetBackGroundStyle( GXWINDOW::BGS_COLOR);
		//m_Panel.SetBackGroundStyleColor( GXWINDOW::MakeColor( 255, 255, 255, 80));

		m_scrollPanel.SetPos( 20, 20);
		m_scrollPanel.SetSize( 180, 240);
		//m_scrollPanel.SetBackGroundStyle( GXWINDOW::BGS_COLOR);
		//m_scrollPanel.SetBackGroundStyleColor( GXWINDOW::MakeColor( 0, 255, 0, 100));
		//m_scrollPanel.SetBorderWidth( 3);
		m_scrollPanel.SetComponent( m_Panel);
		//m_scrollPanel.GetVerticalScrollBar()->SetAdaptMode( false);
		//m_scrollPanel.GetHorizontalScrollBar()->SetAdaptMode( false);

		GXWINDOW::CScrollBar* vbar = m_scrollPanel.GetVerticalScrollBar();
		vbar->SetSize( 16, 240);
		vbar->SetRubSize( 28);
		vbar->SetButtonSize( 16);
		vbar->SetTopLeftButtonImage( GXWINDOW::CImageParams( 2, 265, 343), GXWINDOW::CImageParams( 2, 265, 359), GXWINDOW::CImageParams( 2, 265, 375));
		vbar->SetRightBottomButtonImage( GXWINDOW::CImageParams( 2, 265, 391), GXWINDOW::CImageParams( 2, 265, 407), GXWINDOW::CImageParams( 2, 265, 423));
		vbar->SetSliderScaleStyleImage( GXWINDOW::CImageParams( 2, 246, 254));
		vbar->SetSliderRubStyleImage( GXWINDOW::CImageParams( 2, 265, 254), GXWINDOW::CImageParams( 2, 265, 282), GXWINDOW::CImageParams( 2, 265, 310));

		GXWINDOW::CScrollBar* hbar = m_scrollPanel.GetHorizontalScrollBar();
		hbar->SetSize( 180, 16);
		hbar->SetRubSize( 48);
		hbar->SetButtonSize( 16);
		hbar->SetTopLeftButtonImage( GXWINDOW::CImageParams( 2, 354, 432), GXWINDOW::CImageParams( 2, 354, 448), GXWINDOW::CImageParams( 2, 354, 464));
		hbar->SetRightBottomButtonImage( GXWINDOW::CImageParams( 2, 354, 384), GXWINDOW::CImageParams( 2, 354, 400), GXWINDOW::CImageParams( 2, 354, 416));
		//hbar->SetSliderScaleStyleImage( GXWINDOW::CImageParams( 2, 246, 254));
		hbar->SetSliderRubStyleImage( GXWINDOW::CImageParams( 2, 303, 447), GXWINDOW::CImageParams( 2, 303, 463), GXWINDOW::CImageParams( 2, 303, 479));


		GXWINDOW::ImageParams		ip1, ip2, ip3;
		ip1.nBitmap = nImageIndex1;
		ip1.x = 0;
		ip1.y = 392;
		ip1.color = GXWINDOW::MakeColor( 255, 255, 255);
		ip2.nBitmap = nImageIndex1;
		ip2.x = 64;
		ip2.y = 392;
		ip2.color = GXWINDOW::MakeColor( 255, 255, 255);
		ip3.nBitmap = nImageIndex1;
		ip3.x = 128;
		ip3.y = 392;
		ip3.color = GXWINDOW::MakeColor( 255, 255, 255);

		m_tab.InsetComponent( m_scrollPanel, ip1, ip2, ip3);

		m_spListBox.SetPos( 220, 20);
		//	m_spListBox.SetSize( 180, 240);
		//	m_spListBox.SetComponent( m_ListBox);
		m_spListBox.SetSize( 180, 120);
		m_spListBox.SetComponent( m_Table);

		ip1.y = 416;
		ip2.y = 416;
		ip3.y = 416;

		m_tab.InsetComponent( m_spListBox, ip1, ip2, ip3);

		m_ListBox.SetSize( 360, 320);
		//{
		//	GXWINDOW::COLOR	clrs[1] = { GXWINDOW::MakeColor( 200, 200, 200), };
		//	m_spListBox.SetBorderStyleLine( clrs, 1);
		//}
		m_ListBox.SetListDataModule( m_tdmListBox);
		m_ListBox.SetMultiSelect();

		{
			m_Table.SetTableDataModule( m_ttdm);
			//m_Table.GetTableHeader().SetBackGroundStyleColor( GXWINDOW::MakeColor( 120, 120, 120));
			m_ttdm.InsertColumn( L"t0");
			m_ttdm.InsertColumn( L"t1");
			m_ttdm.InsertColumn( L"t2");
			m_ttdm.InsertColumn( L"t3");
			m_ttdm.InsertColumn( L"t4");
			m_ttdm.InsertColumn( L"t5");
			m_ttdm.SetRowCount( 8);
			m_ttdm.SetCellText( 0, 0, L"c00 0123456789001234567890");
			m_ttdm.SetCellText( 1, 0, L"c10 0123456789001234567890");
			m_ttdm.SetCellText( 2, 0, L"c20 0123456789001234567890");
			m_ttdm.SetCellText( 3, 0, L"c30 0123456789001234567890");
			m_ttdm.SetCellText( 4, 0, L"c40 0123456789001234567890");
			m_ttdm.SetCellText( 5, 0, L"c50 0123456789001234567890");
			m_ttdm.SetCellText( 0, 1, L"c01 0123456789001234567890");
			m_ttdm.SetCellText( 1, 1, L"c11 0123456789001234567890");
			m_ttdm.SetCellText( 2, 1, L"c21 0123456789001234567890");
			m_ttdm.SetCellText( 3, 1, L"c31 0123456789001234567890");
			m_ttdm.SetCellText( 4, 1, L"c41 0123456789001234567890");
			m_ttdm.SetCellText( 5, 1, L"c51 0123456789001234567890");
			m_ttdm.SetCellText( 0, 2, L"c02 0123456789001234567890");
			m_ttdm.SetCellText( 1, 2, L"c12 0123456789001234567890");
			m_ttdm.SetCellText( 2, 2, L"c22 0123456789001234567890");
			m_ttdm.SetCellText( 3, 2, L"c32 0123456789001234567890");
			m_ttdm.SetCellText( 4, 2, L"c42 0123456789001234567890");
			m_ttdm.SetCellText( 5, 2, L"c52 0123456789001234567890");
			m_ttdm.SetCellText( 0, 3, L"c03 0123456789001234567890");
			m_ttdm.SetCellText( 1, 3, L"c13 0123456789001234567890");
			m_ttdm.SetCellText( 2, 3, L"c23 0123456789001234567890");
			m_ttdm.SetCellText( 3, 3, L"c33 0123456789001234567890");
			m_ttdm.SetCellText( 4, 3, L"c43 0123456789001234567890");
			m_ttdm.SetCellText( 5, 3, L"c53 0123456789001234567890");
			m_ttdm.SetCellText( 0, 4, L"c04 0123456789001234567890");
			m_ttdm.SetCellText( 1, 4, L"c14 0123456789001234567890");
			m_ttdm.SetCellText( 2, 4, L"c24 0123456789001234567890");
			m_ttdm.SetCellText( 3, 4, L"c34 0123456789001234567890");
			m_ttdm.SetCellText( 4, 4, L"c44 0123456789001234567890");
			m_ttdm.SetCellText( 5, 4, L"c54 0123456789001234567890");
			m_ttdm.SetCellText( 0, 5, L"c04 0123456789001234567890");
			m_ttdm.SetCellText( 1, 5, L"c14 0123456789001234567890");
			m_ttdm.SetCellText( 2, 5, L"c24 0123456789001234567890");
			m_ttdm.SetCellText( 3, 5, L"c34 0123456789001234567890");
			m_ttdm.SetCellText( 4, 5, L"c44 0123456789001234567890");
			m_ttdm.SetCellText( 5, 5, L"c54 0123456789001234567890");
			m_ttdm.SetCellText( 0, 6, L"c04 0123456789001234567890");
			m_ttdm.SetCellText( 1, 6, L"c14 0123456789001234567890");
			m_ttdm.SetCellText( 2, 6, L"c24 0123456789001234567890");
			m_ttdm.SetCellText( 3, 6, L"c34 0123456789001234567890");
			m_ttdm.SetCellText( 4, 6, L"c44 0123456789001234567890");
			m_ttdm.SetCellText( 5, 6, L"c54 0123456789001234567890");
			m_ttdm.SetCellText( 0, 7, L"c04 0123456789001234567890");
			m_ttdm.SetCellText( 1, 7, L"c14 0123456789001234567890");
			m_ttdm.SetCellText( 2, 7, L"c24 0123456789001234567890");
			m_ttdm.SetCellText( 3, 7, L"c34 0123456789001234567890");
			m_ttdm.SetCellText( 4, 7, L"c44 0123456789001234567890");
			m_ttdm.SetCellText( 5, 7, L"c54 0123456789001234567890");
			m_ttdm.FireDataChanged();

		}

		{
			m_tdmListBox.InsertText( L"00 0123456789001234567890");
			m_tdmListBox.InsertText( L"01 0123456789001234567890");
			m_tdmListBox.InsertText( L"02 0123456789001234567890");
			m_tdmListBox.InsertText( L"03 0123456789001234567890");
			m_tdmListBox.InsertText( L"04 0123456789001234567890");
			m_tdmListBox.InsertText( L"05 0123456789001234567890");
			m_tdmListBox.InsertText( L"06 0123456789001234567890");
			m_tdmListBox.InsertText( L"07 0123456789001234567890");
			m_tdmListBox.InsertText( L"08 0123456789001234567890");
			m_tdmListBox.InsertText( L"09 0123456789001234567890");
			m_tdmListBox.InsertText( L"10 0123456789001234567890");
			m_tdmListBox.InsertText( L"11 0123456789001234567890");
			m_tdmListBox.InsertText( L"12 0123456789001234567890");
			m_tdmListBox.InsertText( L"13 0123456789001234567890");
			m_tdmListBox.InsertText( L"14 0123456789001234567890");
			m_tdmListBox.InsertText( L"15 0123456789001234567890");
			m_tdmListBox.InsertText( L"16 0123456789001234567890");
			m_tdmListBox.InsertText( L"17 0123456789001234567890");
			m_tdmListBox.InsertText( L"18 0123456789001234567890");
			m_tdmListBox.InsertText( L"19 0123456789001234567890");
			m_tdmListBox.InsertText( L"20 0123456789001234567890");
		}

		m_combobox.SetPos( 20, 20);
		//m_combobox.SetBackGroundStyleColor( GXWINDOW::MakeColor( 200, 200, 200));
		m_combobox.SetSize( 180, 35);
		m_combobox.SetListBoxSize( 500, 200);
		m_combobox.SetListDataModule( m_tdmListBox);
		//m_combobox.SetListDataModule( m_imgldm);
		//m_combobox.SetListDataModule( m_clrldm);

		ip1.y = 440;
		ip2.y = 440;
		ip3.y = 440;
		m_controls.SetPos( 0, 0);
		m_controls.SetSize( 400, 300);
		m_controls.InsertComponent( &m_combobox);
		m_tab.InsetComponent( m_controls, ip1, ip2, ip3);

		{
			m_controls.InsertComponent( m_textarea);

			m_textarea.SetPos( 20, 70);
			m_textarea.SetSize( 180, 200);
			m_textarea.AppendText( g_text);

			{
				GXWINDOW::COLOR	clrs[1] = { GXWINDOW::MakeColor( 200, 200, 200), };
				//m_textarea.GetScrollPanel().SetBorderStyleLine( clrs, 1);
				//m_textarea.GetTextArea().SetBorderStyleLine( clrs, 1);

				ipframe.x = 75;
				ipframe.y = 470;
				m_textarea.ShowCursor( ipframe);

				m_textarea.SetSelectAllow( true);
			}
		}

		{
			m_tb.SetComponentText( L"text");
			m_tb.SetComponentFont( 0);
			m_tb.SetComponentTextColor( GXWINDOW::MakeColor( 100, 100, 128));
			m_tb.SetComponentTextMouseInColor( GXWINDOW::MakeColor( 100, 100, 255));
			m_tb.SetPos( 220, 20);
			m_tb.SetSize( 180, 20);
			m_controls.InsertComponent( &m_tb);
			m_tb.ButtonClickNotifyAdd( *this);

			GXWINDOW::COLOR	clrs[1] = { GXWINDOW::MakeColor( 200, 200, 200), };
			//m_editarea.GetScrollPanel().SetBorderStyleLine( clrs, 1);

			m_editarea.SetPos( 220, 70);
			m_editarea.SetSize( 180, 200);
			m_editarea.ShowCursor( ipframe);
			m_editarea.AppendText( L"测试测试测试测试");
			m_controls.InsertComponent( m_editarea);
		}

		m_tc.SetComponentText( L"Tip Info");
		m_tc.SetSize( 150, 20);
		m_btnTeshunengli.SetTipInfo( &m_tc);

		m_spTree.SetPos( 220, 160);
		m_spTree.SetSize( 180, 180);
		m_spTree.SetComponent( m_tree);
		//{
		//	GXWINDOW::COLOR	clrs[1] = { GXWINDOW::MakeColor( 200, 200, 200), };
		//	m_spTree.SetBorderStyleLine( clrs, 1);
		//}
		m_tree.SetTreeDataModal( m_treedm);

		ip1.y = 392;
		ip2.y = 392;
		ip3.y = 392;

		m_tab.InsetComponent( m_spTree, ip1, ip2, ip3);

		{
			m_tree.SetViewWidth( 160);

			GXWINDOW::CTreeNodeObject* p = m_treedm.NewRoot( L"root1");
			m_treedm.NewNode( p, L"node11");
			m_treedm.NewNode( p, L"node12");
			m_treedm.NewNode( p, L"node13");

			{	
				GXWINDOW::CTreeNodeObject* p1 = m_treedm.NewNode( p, L"node14");
				m_treedm.NewNode( p1, L"node141");
				m_treedm.NewNode( p1, L"node142");
				m_treedm.NewNode( p1, L"node143");
				m_treedm.NewNode( p1, L"node144");
				m_treedm.NewNode( p1, L"node145");
				m_treedm.NewNode( p1, L"node146");
				{	
					GXWINDOW::CTreeNodeObject* p2 = m_treedm.NewNode( p1, L"node147");
					m_treedm.NewNode( p2, L"node1471");
					m_treedm.NewNode( p2, L"node1472");
					m_treedm.NewNode( p2, L"node1473");
					m_treedm.NewNode( p2, L"node1474");
					m_treedm.NewNode( p2, L"node1475");
					m_treedm.NewNode( p2, L"node1476");				
				} 
			}			
			{	
				GXWINDOW::CTreeNodeObject* p1 = m_treedm.NewNode( p, L"node15");
				m_treedm.NewNode( p1, L"node151");
				m_treedm.NewNode( p1, L"node152");
				m_treedm.NewNode( p1, L"node153");
				m_treedm.NewNode( p1, L"node154");
				m_treedm.NewNode( p1, L"node155");
				m_treedm.NewNode( p1, L"node156");		
			}

			m_treedm.NewRoot( L"root2");
			p = m_treedm.NewRoot( L"root3");
			{	
				GXWINDOW::CTreeNodeObject* p1 = m_treedm.NewNode( p, L"node34");
				m_treedm.NewNode( p1, L"node341");
				m_treedm.NewNode( p1, L"node342");
				m_treedm.NewNode( p1, L"node343");
				m_treedm.NewNode( p1, L"node344");
				m_treedm.NewNode( p1, L"node345");
				m_treedm.NewNode( p1, L"node346");
				{	
					GXWINDOW::CTreeNodeObject* p2 = m_treedm.NewNode( p1, L"node347");
					m_treedm.NewNode( p2, L"node3471");
					m_treedm.NewNode( p2, L"node3472");
					m_treedm.NewNode( p2, L"node3473");
					m_treedm.NewNode( p2, L"node3474");
					m_treedm.NewNode( p2, L"node3475");
					m_treedm.NewNode( p2, L"node3476");				
				} 
			}			
			{	
				GXWINDOW::CTreeNodeObject* p1 = m_treedm.NewNode( p, L"node35");
				m_treedm.NewNode( p1, L"node351");
				m_treedm.NewNode( p1, L"node352");
				m_treedm.NewNode( p1, L"node353");
				m_treedm.NewNode( p1, L"node354");
				m_treedm.NewNode( p1, L"node355");
				m_treedm.NewNode( p1, L"node356");		
			}

			ipframe.x = 0;
			ipframe.y = 470;
			ipframe.color = GXWINDOW::MakeColor( 255, 255, 255, 255);
			m_tree.SetImageNormal( ipframe);
			ipframe.x = 26;
			m_tree.SetImageExpand( ipframe);
			ipframe.x = 51;
			m_tree.SetImageLeaf( ipframe);

			m_treedm.FireDataChanged();
		}

		SetMoveStyle( GXWINDOW::FMS_ALL);

		//TestLayoutManager* pLayout = new TestLayoutManager();
		//pLayout->Layout( &m_Panel);
		//pLayout->Delete();

		//SetZoomScale( 1.5f);
	}
	~CMyFrame()
	{
		m_tb.ButtonClickNotifyRemove( *this);
		m_btnJineng.ButtonClickNotifyRemove( *this);
		m_btnTeshunengli.ButtonClickNotifyRemove( *this);
		m_btnClose.ButtonClickNotifyRemove( *this);
		m_tab.TabChangedNotifyRemove( *this);
		FrameClosedNotifyRemove( *this);
	}
	virtual void Show()
	{
		CFrame::Show();
	}
	void SetButtonBotify( CMyButtonClickNotify& bcn)
	{

	}
	void OnButtonClick( GXWINDOW::CAbstractButton& c)
	{
		if( c.IsSameComponent( m_btnClose))
		{
			Hide();
		}
		else if( c.IsSameComponent( m_btnJineng))
		{
			m_btnJineng.SetEnable( false);
			m_btnTeshunengli.SetEnable( true);

			GXWINDOW::CFormatTextEncoder		fte;
			m_editline.GetEditArea().Encode( fte);

			//MessageBoxW( NULL, fte.GetResult(), NULL, 0);
			m_animate.StopAt( 2);
		}
		else if( c.IsSameComponent( m_btnTeshunengli))
		{
			m_btnJineng.SetEnable( true);
			m_btnTeshunengli.SetEnable( false);

			GXWINDOW::CFormatTextEncoder		fte;
			m_editline.GetEditArea().Encode( fte);

			//MessageBoxW( NULL, fte.GetResult(), NULL, 0);
			m_animate.StopAt( -1);
		}
		else if( c.IsSameComponent( m_tb))
		{
			m_editarea.AppendText( g_text);
		}
	}
	virtual void OnTabChanged( GXWINDOW::CTabControl& src, int from, int to)
	{
	}

	virtual void OnFrameClosed( GXWINDOW::CFrame& c) 
	{
	}
};


//class CAppleFrame
//{
//	GXWINDOW::CFrame		m_frame;
//
//	GXWINDOW::CAppleButton	m_btn0;
//	GXWINDOW::CAppleButton	m_btn1;
//	GXWINDOW::CAppleButton	m_btn2;
//	GXWINDOW::CAppleButton	m_btn3;
//	GXWINDOW::CAppleButton	m_btn4;
//
//	GXWINDOW::CAppleButton	m_btn20;
//	GXWINDOW::CAppleButton	m_btn21;
//	GXWINDOW::CAppleButton	m_btn22;
//	GXWINDOW::CAppleButton	m_btn23;
//	GXWINDOW::CAppleButton	m_btn24;
//
//	GXWINDOW::CApplePanel	m_panel;
//public:
//	CAppleFrame()
//	{
//		m_frame.SetPos( 100, 100);
//		m_frame.SetSize( 400, 300);
//		m_panel.SetPos( 0, 0);
//		m_panel.SetSize( 400, 300);
//		m_frame.InsertComponent( &m_panel);
//
//		m_panel.InsertAppleButton( m_btn0);
//		m_panel.InsertAppleButton( m_btn1);
//		m_panel.InsertAppleButton( m_btn2);
//		m_panel.InsertAppleButton( m_btn3);
//		m_panel.InsertAppleButton( m_btn4);
//
//		m_panel.InsertAppleButton( m_btn2, m_btn20);
//		m_panel.InsertAppleButton( m_btn2, m_btn21);
//		m_panel.InsertAppleButton( m_btn2, m_btn22);
//		m_panel.InsertAppleButton( m_btn2, m_btn23);
//		m_panel.InsertAppleButton( m_btn2, m_btn24);
//
//		m_panel.SetLargeRadius( 50);
//		m_panel.SetBaseRadius( 25);
//		m_panel.ResetButtonPosition();
//	}
//	~CAppleFrame()
//	{
//	}
//public:
//	void Show()
//	{
//		m_frame.Show();
//	}
//};