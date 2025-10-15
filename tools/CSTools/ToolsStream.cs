using System;
using System.IO;
using System.Text;

namespace CSTools
{
    class ToolsStream : FileStream
    {
        public ToolsStream(string path, FileMode mode, FileAccess access)
            : base(path, mode, access)
        {
        }

        //Byte
        public int ReadBA(ref Byte[] Val, int offset, int Count)
        {
            int Result = 0;
            if (Val == null)
            {
                Val = new Byte[Count];
            }
            Result = Read(Val, offset, Count);
            return Result;
        }
        public int ReadBA(ref Byte[] Val, int Count)
        {
            return ReadBA(ref Val, 0, Count);
        }
        public int ReadBA(ref Byte[] Val)
        {
            return ReadBA(ref Val, (int)Length);
        }
        public void WriteBA(Byte[] Val, int offset, int Count)
        {
            int ReadStart = 0;
            while (Count > 0)
            {
                int CurCount = Count;
                if (Count > 256)
                {
                    CurCount = 256;
                }
                Count -= CurCount;
                Write(Val, ReadStart, CurCount);
                ReadStart += CurCount;
            }
        }
        public void WriteBA(Byte[] Val, int offset)
        {
            WriteBA(Val, offset, Val.Length);
        }
        public void WriteBA(Byte[] Val)
        {
            WriteBA(Val, 0);
        }
        //StringA
        public string ReadStrA(int Count)
        {
            Byte[] StrBA = new Byte[Count];
            ReadBA(ref StrBA, Count);
            string Result = ToolsStr.AToU(StrBA);
            return Result;
        }
        public string ReadStrA()
        {
            return ReadStrA((int)Length);
        }
        public void WriteStrA(string Val)
        {
            Byte[] Result = ToolsStr.UToA(Val);
            WriteBA(Result);
        }
        //String
        public string ReadStr(int Count)
        {
            Byte[] StrBA = new Byte[Count];
            ReadBA(ref StrBA, Count);
            string Result = ToolsStr.BAToStr(StrBA);
            return Result;
        }
        public string ReadStrEx()
        {
            int Count = ReadInt();
            return ReadStr(Count);
        }
        public void WriteStr(string Val)
        {
            Byte[] Result = ToolsStr.StrToBA(Val);
            WriteBA(Result);
        }
        public void WriteStrEx(string Val)
        {
            Byte[] Result = ToolsStr.StrToBA(Val);
            WriteInt(Result.Length);
            WriteBA(Result);
        }
        //Int
        public int ReadInt()
        {
            Byte[] BA = null;
            ReadBA(ref BA, 4);
            int Result = ToolsConvert.BAToI1(BA);
            return Result;
        }
        public void WriteInt(int Val)
        {
            Byte[] Result = ToolsConvert.I1ToBA(Val);
            WriteBA(Result);
        }
        //UInt
        public uint ReadUInt()
        {
            Byte[] BA = null;
            ReadBA(ref BA, 4);
            uint Result = ToolsConvert.BAToUI1(BA);
            return Result;
        }
        public void WriteUInt(uint Val)
        {
            Byte[] Result = ToolsConvert.UI1ToBA(Val);
            WriteBA(Result);
        }

    }
}
