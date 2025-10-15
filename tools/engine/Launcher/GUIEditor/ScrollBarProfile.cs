using System;
using System.Collections.Generic;
using System.Drawing;
using System.Xml;

namespace GUIEditor2
{
	public static class CScrollBarProfile
	{
		public class CScrollBarParam
		{
			public int w, h;
			public string name;
			public string classname;
			public string autoblock;
			public string innerxml;
		}

		private static Dictionary<string, CScrollBarParam> m_hprofile = new Dictionary<string, CScrollBarParam>();
		private static Dictionary<string, CScrollBarParam> m_vprofile = new Dictionary<string, CScrollBarParam>();

		public static void Clear()
		{
			m_hprofile.Clear();
			m_vprofile.Clear();
		}

		public static bool LoadXml(XmlElement sb)
		{
			if(sb.Name != "ScrollBar") return false;
			CScrollBarParam sbp = new CScrollBarParam();
			bool vertical = (sb.GetAttribute("vertical") == "true");
			sbp.name = sb.GetAttribute("n");
			if(sbp.name == null || (!vertical && m_hprofile.ContainsKey(sbp.name))
								|| ( vertical && m_vprofile.ContainsKey(sbp.name))) return false;
			sbp.classname = sb.GetAttribute("class");
			sbp.autoblock = sb.GetAttribute("autoblock");
			if(sbp.autoblock == null) sbp.autoblock = "true";
			string w = sb.GetAttribute("w"); if(w == null || !Int32.TryParse(w, out sbp.w)) return false;
			string h = sb.GetAttribute("h"); if(w == null || !Int32.TryParse(h, out sbp.h)) return false;
			sbp.innerxml = sb.InnerXml;
			if(!vertical) m_hprofile[sbp.name] = sbp;
			else		  m_vprofile[sbp.name] = sbp;
			return true;
		}

		public static string LoadXmlNewName(XmlElement sb, out bool isnew)
		{
			isnew = false;
			if(sb.Name != "ScrollBar") return null;

			string name = sb.GetAttribute("n");
			if(name == null) name = "";
			string newname = name;
			int i = 1;
			while(m_hprofile.ContainsKey(newname) && (m_hprofile[newname].innerxml  != sb.InnerXml ||
													  m_hprofile[newname].classname != sb.GetAttribute("class") ||
													  m_hprofile[newname].autoblock != sb.GetAttribute("autoblock")) ||
				  m_vprofile.ContainsKey(newname) && (m_vprofile[newname].innerxml  != sb.InnerXml ||
													  m_vprofile[newname].classname != sb.GetAttribute("class") ||
													  m_vprofile[newname].autoblock != sb.GetAttribute("autoblock")))
			{
				newname = name + "(修改" + i.ToString() + ')';
				++i;
			}

			if(i > 1 || !m_hprofile.ContainsKey(newname) && !m_vprofile.ContainsKey(newname))
			{
				CScrollBarParam sbp = new CScrollBarParam();
				bool vertical = (sb.GetAttribute("vertical") == "true");
				sbp.name = sb.GetAttribute("n");
				if(sbp.name == null) sbp.name = "";
				sbp.classname = sb.GetAttribute("class");
				sbp.autoblock = sb.GetAttribute("autoblock");
				if(sbp.autoblock == null) sbp.autoblock = "true";
				string w = sb.GetAttribute("w"); if(w == null || !Int32.TryParse(w, out sbp.w)) return null;
				string h = sb.GetAttribute("h"); if(w == null || !Int32.TryParse(h, out sbp.h)) return null;
				sbp.innerxml = sb.InnerXml;
				if(!vertical) m_hprofile[newname] = sbp;
				else		  m_vprofile[newname] = sbp;
				isnew = true;
			}

			return newname;
		}

		public static bool LoadFile(string filename)
		{
			try
			{
				XmlDocument doc = new XmlDocument();			
				doc.Load(filename);
				XmlElement desktop = doc["Desktop"];
				if(desktop == null)
					return false;
				XmlElement content = desktop["Content"];
				if(content == null)
					return false;
				foreach(XmlNode node in content.ChildNodes)
				{
					if(node.NodeType == XmlNodeType.Element)
						LoadXml(node as XmlElement);
				}
				return true;
			}
			catch
			{
				return false;
			}
		}

		public static Dictionary<string, CScrollBarParam>.KeyCollection GetHKeys()
		{
			return m_hprofile.Keys;
		}

		public static Dictionary<string, CScrollBarParam>.KeyCollection GetVKeys()
		{
			return m_vprofile.Keys;
		}

		public static CScrollBarParam FindH(string name)
		{
			return m_hprofile.ContainsKey(name) ? m_hprofile[name] : null;
		}

		public static CScrollBarParam FindV(string name)
		{
			return m_vprofile.ContainsKey(name) ? m_vprofile[name] : null;
		}

		public static bool SaveScrollBar(XmlDocument doc, XmlElement elem, string vname, string hname, Rectangle rect)
		{
			int vwidth = 0;
			int hheight = 0;
			CScrollBarParam sbpv = null;
			CScrollBarParam sbph = null;

			if(vname != null && vname.Length > 0) {sbpv = m_vprofile[vname]; if(sbpv != null) vwidth = sbpv.w; }
			if(hname != null && hname.Length > 0) {sbph = m_hprofile[hname]; if(sbph != null) hheight = sbph.h; }

			if(sbpv != null)
			{
				XmlElement sb = doc.CreateElement("ScrollBar");
				sb.SetAttribute("n", sbpv.name);
				if(sbpv.classname != null) sb.SetAttribute("class", sbpv.classname);
				if(sbpv.autoblock != null) sb.SetAttribute("autoblock", sbpv.autoblock);
				sb.SetAttribute("x", (rect.Width > sbpv.w ? rect.Width - sbpv.w : 0).ToString());
				sb.SetAttribute("y", "0");
				sb.SetAttribute("w", (rect.Width > sbpv.w ? sbpv.w : rect.Width).ToString());
				sb.SetAttribute("h", (rect.Height - hheight).ToString());
				sb.SetAttribute("vertical", "true");
				sb.InnerXml = sbpv.innerxml;
				elem.AppendChild(sb);
			}
			
			if(sbph != null)
			{
				XmlElement sb = doc.CreateElement("ScrollBar");
				sb.SetAttribute("n", sbph.name);
				if(sbph.classname != null) sb.SetAttribute("class", sbph.classname);
				if(sbph.autoblock != null) sb.SetAttribute("autoblock", sbph.autoblock);
				sb.SetAttribute("x", "0");
				sb.SetAttribute("y", (rect.Height > sbph.h ? rect.Height - sbph.h : 0).ToString());
				sb.SetAttribute("w", (rect.Width - vwidth).ToString());
				sb.SetAttribute("h", (rect.Height > sbph.h ? sbph.h : rect.Height).ToString());
				sb.SetAttribute("vertical", "false");
				sb.InnerXml = sbph.innerxml;
				elem.AppendChild(sb);
			}			

			return true;
		}
	}
}
