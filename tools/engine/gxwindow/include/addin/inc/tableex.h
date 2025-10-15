#pragma once

namespace GXWADDIN
{


class test
{

};

class CellComponentMap;

// 实现在表格单元格中插入其他控件,
// 所插入的控件需要在外部准备好, 
// CComponentTableDataModule只负责渲染
class CComponentTableDataModule : public GXWINDOW::CTextTableDataModule
{
	CellComponentMap*		m_pComponentMap;
	GXWINDOW::CComponent*	m_pLastMoveComponent;
public:
	CComponentTableDataModule();
	virtual ~CComponentTableDataModule();

	void SetCellComponent( int col, int row, GXWINDOW::CComponent& c, int offsetx = 0, int offsety = 0);
	void RemoveCellComponent( int col, int row);
	const GXWINDOW::CComponent* GetCellComponent( int col, int row) const;

	virtual void RemoveRow( int row);
	virtual void ClearRows();

protected:
	virtual void DrawClientComponent(int col, int row, CCTYPE cct, GXWINDOW::CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight) override;

	virtual void OnComponentShow() override;
	virtual void OnComponentHide() override;

	virtual void OnMouseLeave() override;

	virtual bool OnMouseMove(int x, int y, int key, int col, int row) override;
	virtual bool OnLeftButtonUp(int x, int y, int key, int col, int row) override;
	virtual bool OnLeftButtonDown(int x, int y, int key, int col, int row) override;
};


} // namespace GXWADDIN