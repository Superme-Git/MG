using System;

namespace CSTools
{
    class ToolsKunTaTable
    {

        static public void RotateBuffer(Byte[] BA)
        {
            ToolsConvert.InvBA(BA);
        }

        static public bool ImportTable(ToolsTable TT, ToolsStream TS, bool TableOrCode = true)
        {
            TS.Position = 0;
            TT.Clear();
            int ReadRowCount = -1;
            if (TableOrCode == false)
            {
                ReadRowCount = 4;
            }
            while (TS.Position < TS.Length - 1)
            {
                int RowLen = TS.ReadInt();
                Byte[] BArr = null;
                TS.ReadBA(ref BArr, RowLen);
                RotateBuffer(BArr);
                string CurRow = ToolsStr.AToU(BArr);
                TT.AddRow(CurRow);
                if (TT.GetRowCount() == 1)
                {
                    TT.SetColCount(TT.GetRow(0).GetColCount());
                }
                if (ReadRowCount > 0)
                {
                    if (TT.GetRowCount() >= ReadRowCount)
                    {
                        break;
                    }
                }
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
                long RowLenPos = TS.Position;
                int RowLen = 100000;
                TS.WriteInt(RowLen);
                long RowStartPos = TS.Position;
                string CurRow = "";
                string CurCell = "";
                for (int i2 = 0; i2 < ColCount; i2++)
                {
                    CurCell = TT.GetCell(i, i2);
                    CurRow = CurRow + CurCell + "\t";
                }
                Byte[] BArr = ToolsStr.UToA(CurRow);
                RotateBuffer(BArr);
                TS.WriteBA(BArr);
                long RowOverPos = TS.Position;
                TS.Position = RowLenPos;
                RowLen = (int)(RowOverPos - RowStartPos);
                TS.WriteInt(RowLen);
                TS.Position = RowOverPos;
            }
            return true;
        }

    }
}