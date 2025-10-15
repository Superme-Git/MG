using System.IO;
using NPOI.HSSF.UserModel;
using NPOI.SS.UserModel;

namespace CSTools
{
    class ToolsTableExcelNPOIXLS
    {
        static public bool ImportTableNPOI(ToolsTable TT, string FullFileName, bool TableOrCode = true)
        {
            TT.Clear();
            HSSFWorkbook HSSFWB;
            using (FileStream FS = new FileStream(FullFileName, FileMode.Open, FileAccess.Read))
            {
                HSSFWB = new HSSFWorkbook(FS);
            }
            ISheet CurSheet = HSSFWB.GetSheetAt(0);
            //System.Collections.IEnumerator rows = CurSheet.GetRowEnumerator();
            int RowCount = CurSheet.PhysicalNumberOfRows;// CurSheet.LastRowNum - CurSheet.FirstRowNum + 1;
            int ColCount = CurSheet.GetRow(0).PhysicalNumberOfCells;
            if (RowCount >= TT.GetHeadRowCount() && ColCount > 0)
            {
                TT.SetColCount(ColCount);
                int ReadRowCount = RowCount;
                if (TableOrCode == false)
                {
                    ReadRowCount = 1;
                }
                for (int i = CurSheet.FirstRowNum; i < ReadRowCount + CurSheet.FirstRowNum; i++)
                {
                    IRow CurRow = CurSheet.GetRow(i);
                    if (CurRow == null)
                    {
                        break;
                    }
                    //if (CurRow.LastCellNum - CurRow.FirstCellNum < TT.GetColCount())
                    //{
                    //    CurRow = null;
                    //    break;
                    //}
                    //bool RowDataOK = true;
                    ToolsTable.ToolsTableRow Result = TT.AddRow();
                    Result.SetColCount(TT.GetColCount());
                    //for (int i2 = CurRow.FirstCellNum; i2 < TT.GetColCount() + CurRow.FirstCellNum; i2++)
                    for (int i2 = 0; i2 < TT.GetColCount(); i2++)
                    {
                        string CurCellText = "";
                        ICell CurCell = CurRow.GetCell(i2);
                        if (CurCell != null)
                        {
                            if (CurCell.CellType != CellType.String)
                            {
                                CurCell.SetCellType(CellType.String);
                            }
                            CurCellText = CurCell.ToString();//CurCell.StringCellValue;                            
                        }
                        //if (CurCellText == "")
                        //{
                        //    RowDataOK = false;
                        //    //TT.SetColCount(i2);//
                        //    break;
                        //}
                        Result.SetCell(i2, CurCellText);
                    }
                    //if (RowDataOK == false)
                    //{
                    //    TT.DelRow(Result);
                    //    break;
                    //}
                }
            }
            return true;
        }
    }
}
