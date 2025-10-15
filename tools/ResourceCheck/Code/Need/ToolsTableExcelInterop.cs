using System;
using System.Runtime.InteropServices;
using Microsoft.Office.Interop.Excel;

namespace CSTools
{
    class ToolsTableExcelInterop
    {
        [DllImport("User32.dll")]
        public static extern int GetWindowThreadProcessId(IntPtr hWnd, out int ProcessId);
        static public void KillExcel(Microsoft.Office.Interop.Excel.Application theApp)
        {
            int id = 0;
            IntPtr intptr = new IntPtr(theApp.Hwnd);
            System.Diagnostics.Process p = null;
            try
            {
                GetWindowThreadProcessId(intptr, out id);
                p = System.Diagnostics.Process.GetProcessById(id);
                if (p != null)
                {
                    p.Kill();
                    p.Dispose();
                }
            }
            catch (System.Exception ex)
            {

            }
        }
        static public bool ImportTableInterop(ToolsTable TT, string FullFileName, bool TableOrCode = true)
        {
            TT.Clear();
            // 1. 创建Excel应用程序对象的一个实例，相当于我们从开始菜单打开Excel应用程序。
            Microsoft.Office.Interop.Excel.Application XLSApp = new Microsoft.Office.Interop.Excel.Application();
            if (XLSApp == null)//对此实例进行验证，如果为null则表示运行此代码的机器可能未安装Excel
            {
                return false;
            }
            //string TempTXTFileName = AppPath + "Temp" + ToolsFileStr.GetName(FullFileName) + ".txt";
            Microsoft.Office.Interop.Excel.Workbook WB = null;
            Microsoft.Office.Interop.Excel.Worksheet WS = null;
            try
            {
                WB =
                    (Microsoft.Office.Interop.Excel.Workbook)XLSApp.Workbooks.Open(FullFileName, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing, Type.Missing);
                WS = (Microsoft.Office.Interop.Excel.Worksheet)(WB.Sheets.Item[1]);//第一个sheet页

                int RowCount = WS.UsedRange.Rows.Count;
                RowCount = ((Microsoft.Office.Interop.Excel.Range)WS.Cells[65536, 1]).get_End(XlDirection.xlUp).Row;
                int ColCount = WS.UsedRange.Columns.Count;
                ColCount = ((Microsoft.Office.Interop.Excel.Range)WS.Cells[1, 256]).get_End(XlDirection.xlToLeft).Column;
                TT.SetColCount(ColCount);

                if (RowCount > TT.GetHeadRowCount() && ColCount > 0)
                {
                    int ReadRowCount = RowCount;
                    if (TableOrCode == false)
                    {
                        ReadRowCount = 4;
                    }
                    //DateTime DT1 = DateTime.Now;
                    for (int i = 0; i < ReadRowCount; i++)
                    {
                        ToolsTable.ToolsTableRow Result = TT.AddRow();
                        Result.SetColCount(ColCount);
                        for (int i2 = 0; i2 < ColCount; i2++)
                        {
                            string CurCell = ((Microsoft.Office.Interop.Excel.Range)WS.Cells[i + 1, i2 + 1]).Text;
                            Result.SetCell(i2, CurCell);
                        }
                    }
                    //DateTime DT2 = DateTime.Now;
                    //TimeSpan TS1 = DT2.Subtract(DT1);
                }
            }
            finally
            {
                //System.Runtime.InteropServices.Marshal.ReleaseComObject(WS);
                WS = null;
                WB.Close(true, Type.Missing, Type.Missing);
                //System.Runtime.InteropServices.Marshal.ReleaseComObject(WB);
                WB = null;
                XLSApp.Workbooks.Close();
                XLSApp.Quit();
                KillExcel(XLSApp);
                //System.Runtime.InteropServices.Marshal.ReleaseComObject(XLSApp);
                XLSApp = null;
                //GC.Collect();
                //GC.WaitForPendingFinalizers();
            }
            //LoadFromFileTXT(TempTXTFileName, TableOrCode);
            //DateTime DTB = DateTime.Now;
            //TimeSpan TSA = DTB.Subtract(DTA);
            return true;
        }
    }
}
