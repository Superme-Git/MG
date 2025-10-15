using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.IO;
using System.Threading;

namespace NpcList
{
    class Program
    {
        static void Main(string[] args)
        {
            if (!IsRightPath())
            {
                Console.WriteLine("当前运行目录不对，请放在res下运行");
                Environment.Exit(0);
            }


            MapNameMap mapNameMap = new MapNameMap(Directory.GetCurrentDirectory());

            if (!mapNameMap.Init())
                Environment.Exit(0);

            NpcMap npcMap = new NpcMap(Directory.GetCurrentDirectory());

            npcMap.BuildList(mapNameMap.GetMapDirList());

            npcMap.WriteToExcel();
            
        }

        static bool IsRightPath()
        {

            string dir = Directory.GetCurrentDirectory();

            Console.WriteLine("当前运行目录: \"{0}\"", dir);


            dir = dir.Substring(dir.LastIndexOf("\\") + 1);


             return true;
        }


    }
}
