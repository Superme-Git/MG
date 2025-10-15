using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Xml.Serialization;
using System.IO;
using System.Threading;
using System.Reflection;
using System.Windows;
using System.Windows.Data;
using System.ComponentModel;
using System.Collections.ObjectModel;
using Knight.MissionEditor.Xaml;


namespace Knight.MissionEditor
{
    public class ColInfo
    {     
        public string name;
        public int startcol;
        public int endcol;
        public string vartype;
        public bool isArray;
    }

    public class ColInfoBuild
    {
        static public Dictionary<string, ColInfo> BuildInfo(Type tp)
        {
            Dictionary<string,ColInfo> ColInfoMap = new Dictionary<string,ColInfo>();
            MemberInfo[] mbArray = tp.GetMembers();
            int col = 1;
            foreach (MemberInfo mb in mbArray)
            {
                if (mb.MemberType.ToString() == "Field")
                {
                    ColInfo colinfo = new ColInfo();

                    colinfo.name = mb.Name;

                    colinfo.vartype = mb.ToString().Substring(0,mb.ToString().IndexOf(" "));

                    if (colinfo.vartype.EndsWith("[]"))
                        colinfo.isArray = true;
                    else
                        colinfo.isArray = false;

                    if (colinfo.isArray)
                    {
                        colinfo.startcol = col;
                        col += 49;
                        colinfo.endcol = col;
                    }
                    else
                    {
                        colinfo.startcol = col;
                        colinfo.endcol = col;
                    }
                    ++col;
                    ColInfoMap.Add(colinfo.name, colinfo);                   
                }
            }

            return ColInfoMap;
        }
    }
}