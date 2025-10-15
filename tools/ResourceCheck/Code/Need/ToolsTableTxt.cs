using System;

namespace CSTools
{
    class ToolsTableTxt
    {
        static public bool ImportTable(ToolsTable TT, ToolsStream TS, bool TableOrCode = true)
        {
            TS.Position = 0;
            string SourceStr = TS.ReadStrA();
            TT.Clear();
            string[] SplitResultStr = SourceStr.Split(ToolsStr.StrArr("\r\n"), StringSplitOptions.RemoveEmptyEntries);
            if (SplitResultStr.Length == 0)
            {
                return false;
            }
            ToolsTable.ToolsTableRow NewRow = null;
            int ReadRowCount = SplitResultStr.Length;
            if (TableOrCode == false)
            {
                ReadRowCount = 4;
            }
            for (int i = 0; i < ReadRowCount; i++)
            {
                NewRow = new ToolsTable.ToolsTableRow();
                bool Result = NewRow.SetRowData(SplitResultStr[i]);
                if (!Result)
                {
                    NewRow.Clear();
                    continue;
                }
                if (i == 0)
                {
                    TT.SetColCount(NewRow.GetColCount());
                }
                else
                {
                    NewRow.SetColCount(TT.GetColCount());
                }
                TT.AddRow(NewRow);
            }
            return true;
        }

        static public bool ExportTable(ToolsTable TT, ToolsStream TS)
        {
            TS.Position = 0;
            int RowCount = TT.GetRowCount();
            int ColCount = TT.GetColCount();
            for (int i = 0; i < TT.GetRowCount(); i++)
            {
                string CurRow = TT.GetRow(i).GetRowData();
                if (i < TT.GetRowCount() - 1)
                {
                    CurRow = CurRow + ToolsStr.Enter();
                }
                Byte[] BArr = ToolsStr.UToA(CurRow);
                TS.WriteBA(BArr);
            }
            return true;
        }
    }
}
