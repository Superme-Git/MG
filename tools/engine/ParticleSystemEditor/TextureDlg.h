#pragma once
#include "afxcmn.h"
#include <vector>
#include <string>
#include <map>

// CTextureDlg �Ի���

const int WIDTH_SHOWIMAGE = 64;
const int HIGH_SHOWIMAGE = 64;

class CTextureDlg : public CDialog
{
	DECLARE_DYNAMIC(CTextureDlg)

public:
	CTextureDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CTextureDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_TEXTUREWND };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	virtual BOOL OnInitDialog();
	BOOL InitImageList(CString strTexturePath); //��Ӧ���ؼ�...

	BOOL InitFilePathList(CString filePath);

	void ImageSizeAdjust(DWORD* pByte, int WIDTH_SHOWIMAGE, int HIGH_SHOWIMAGE,
							DWORD* pFileBuf, int nWidth, int nHeight);

	void FindTextureFileName(CString strRootPath, CString strDir, CStringArray& ArrayStrFile);
	void FillTextureTree(CString strTexturePath, CString strDir, HTREEITEM hParentItem);
	void FillTextureTree(); //����m_mapStrFileArray����ʼ��..
	
	void InitMapStrFileArray();
	HTREEITEM FindItem(HTREEITEM rootItem, CString strItemText); //��������ָ���ڵ�

public:
	void GetTextureFiles(std::vector<std::wstring>& ArrayTextureFileName);
	BOOL SaveBmp(HBITMAP hBitmap, CString FileName);

private:
	CTreeCtrl m_treePSLTexture; //���ṹ��������ͼƬ...
	std::map<CString, std::vector<CString>> m_mapStrFileArray; //��Ŀ¼�����ļ���...
	std::map<CString, CImageList*> m_mapImageList; //��Ŀ¼��¼image��Ϣ...
	CString m_strCurMapKey; //�������ֵ����ʾ����б���ͼƬ..
	
	//HTREEITEM����ֵÿ��ˢ�¾ͱ���...
	HTREEITEM m_hTreeItemMenu;//�Ҽ��˵���...���ƽڵ�·��ʱ������
	HTREEITEM m_hTreeItemCurShow; //�ұߴ���Ŀǰ��ʾ���ļ�����
//�������ϱ���...ÿ��ˢ�¶�Ҫ����m_strCurMapKey���ֵ���������ҵ�׼ȷ��m_hTreeItemCurShow...
	//���� ---2009--4---17

	int m_nRButtonIndex;//���Ƹ����·��..�ұߴ�����

	CListCtrl m_listCtrlTexture;
	CImageList	m_ImageList; //��ߴ�����ʾ������..
	CStringArray m_ArrayStrFile; //ϵͳ�ṩ�����������ļ���·��
	CString m_strTexturePath;
//	CStringArray m_ArrayTextureFileName; //�û�ѡ�������������ļ���·��
	std::vector<std::wstring> m_ArrayTextureFileName;

private:
	afx_msg void OnLvnItemchangedListTexture(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnPaint();
	afx_msg void OnCustomdrawMyList ( NMHDR* pNMHDR, LRESULT* pResult );
public:
	afx_msg void OnBnClickedButtonRefresh();
	afx_msg void OnNMClickTreePsltexturelist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMenuCopypath();
	afx_msg void OnMenuRefreshtexture();
	afx_msg void OnNMRclickTreePsltexturelist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclickListTexture(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMenuCopyfullpath();
	afx_msg void OnNMDblclkTreePsltexturelist(NMHDR *pNMHDR, LRESULT *pResult);
};
