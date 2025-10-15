using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NpcList
{
    class MapInfo
    {
        public MapInfo(int id, int x, int y,string d)
        {
            _mapid = id;
            _x = x;
            _y = y;
            _dir = d;
        }

        public int _mapid
        {
            set;
            get;
        }

        public int _x
        {
            set;
            get;
        }

        public int _y
        {
            set;
            get;     
        }

        public string _dir
        {
            set;
            get;
        }
    }
}
