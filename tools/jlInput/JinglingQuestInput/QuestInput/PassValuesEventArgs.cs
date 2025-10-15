using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QuestInput
{
    public class PassValuesEventArgs : EventArgs
    {
        public int Value1;
        public string Value2;
        public PassValuesEventArgs(int arg1, string arg2)
        {
            Value1 = arg1;
            Value2 = arg2;
        }

        public string sValue1;
        public string sValue2;
        public PassValuesEventArgs(string arg1, string arg2)
        {
            sValue1 = arg1;
            sValue2 = arg2;
        }
    }
}
