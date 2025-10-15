#pragma once
#include "afxcmn.h"
#include "Project.h"
#include "ComboListCtrl/ComboListCtrl.h"
// CEditIgnoreDlg dialog

class CEditIgnoreDlg : public CDialog
{
	DECLARE_DYNAMIC(CEditIgnoreDlg)

public:
	CEditIgnoreDlg( CWnd* pParent = NULL);   // standard constructor
	virtual ~CEditIgnoreDlg();

	void SetChanged();
	void OpenProject( CProject* project);

// Dialog Data
	enum { IDD = IDD_EDITIGNORE };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	void AddFileIgnore( const std::wstring& file, CFileIgnore::CIgnoreItem* pItem);
	int GetImageIndex( const std::wstring& file, bool bPath);
	bool IsExist( const std::wstring& file, const std::wstring& IsPath);

	static void DelRepeatChar( std::wstring& str, wchar_t c)
	{
		wchar_t* buffer = const_cast<wchar_t*>( str.c_str());
		int src = 0, des = 0;
		bool repeat = false;
		while( buffer[src])
		{
			if( repeat)
			{
				if( buffer[src] == c)
					src++;
				else
					repeat = false;
			}
			else
			{
				buffer[des] = buffer[src];
				if( buffer[src] == c)
					repeat = true;
				src++;
				des++;
			}
		}
		str.resize( des);
	}
	static std::wstring GetStandardPath( const std::wstring& path)
	{
		std::wstring temp( path);
		std::replace( temp.begin(), temp.end(), L'\\', L'/');
		temp.erase( 0, temp.find_first_not_of( L'/'));
		DelRepeatChar( temp, L'/');
		temp.erase( temp.find_last_not_of( L'/')+1);
		return temp;
	}

protected:
	CComboListCtrl m_lcIgnore;
	CProject* m_project;

	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedButton2();
	afx_msg LRESULT PopulateComboList(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam);

	virtual void OnOK();
};
