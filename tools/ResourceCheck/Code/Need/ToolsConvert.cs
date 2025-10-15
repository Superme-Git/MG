using System;

namespace CSTools
{
    class ToolsConvert
    {
        static public void InvBA(Byte[] BArr)
        {
            for (int i = 0; i < BArr.Length / 2; i++)
            {
                Byte CurB = BArr[i];
                BArr[i] = BArr[BArr.Length - 1 - i];
                BArr[BArr.Length - 1 - i] = CurB;
            }
        }
        public static Byte[] I1ToBA(int V)
        {
            Byte[] Result = new Byte[4];
            for (int i = 0; i < 4; i++)
            {
                Result[i] = (Byte)(V >> (i * 8));
            }
            return Result;
        }
        public static int BAToI1(Byte[] V)
        {
            int Mask = 0xFF;
            int Temp = 0;
            int Result = 0;
            for (int i = 0; i < V.Length; i++)
            {
                Result <<= 8;
                Temp = V[V.Length - i - 1] & Mask;
                Result |= Temp;
            }
            return Result;
        }
        public static Byte[] UI1ToBA(uint V)
        {
            Byte[] Result = new Byte[4];
            for (int i = 0; i < 4; i++)
            {
                Result[i] = (Byte)(V >> (i * 8));
            }
            return Result;
        }
        public static uint BAToUI1(Byte[] V)
        {
            uint Mask = 0xFF;
            uint Temp = 0;
            uint Result = 0;
            for (int i = 0; i < V.Length; i++)
            {
                Result <<= 8;
                Temp = V[V.Length - i - 1] & Mask;
                Result |= Temp;
            }
            return Result;
        }

        public static int StrToI1(string V)
        {
            int Result = 0;
            bool B = int.TryParse(V, out Result);
            return Result;
        }
        public static uint StrToUI1(string V)
        {
            uint Result = 0;
            bool B = uint.TryParse(V, out Result);
            return Result;
        }

        //public static byte[] StringToBytes(string HexString)
        //{
        //    byte[] temdata = new byte[HexString.Length / 2];
        //    for (int i = 0; i < temdata.Length; i++)
        //    {
        //        temdata[i] = Convert.ToByte(HexString.Substring(i * 2, 2), 16);
        //    }
        //    return temdata;
        //}

        //public static string ByteToFloat(string instr)
        //{
        //    string result = string.Empty;
        //    if (!string.IsNullOrEmpty(instr))
        //    {
        //        byte[] floatVals1 = StringToBytes(instr);
        //        result = BitConverter.ToSingle(floatVals1, 0).ToString();
        //    }
        //    return result;
        //}

        //public static string ByteToInt(string instr)
        //{
        //    string result = string.Empty;
        //    int n = 0;
        //    if (!string.IsNullOrEmpty(instr))
        //    {
        //        byte[] b = StringToBytes(instr);
        //        int mask = 0xff;
        //        int temp = 0;
        //        for (int i = 0; i < b.Length; i++)
        //        {
        //            n <<= 8;
        //            temp = b[i] & mask;
        //            n |= temp;
        //        }
        //    }
        //    return result = n.ToString();
        //}
    }
}
