using System;
using System.Collections.Generic;
using System.Drawing;
using System.Xml;
using System.Drawing.Text;

namespace GUIEditor2
{
	public static class CFontProfile
	{
		public class CFontParam
		{
			public int id;
			public string name;
			public string fontname;
			public int size;
			public string file;

			public override string ToString()
			{
				return id.ToString() + ':' + name;
			}
		}

		public class CFontFormatParam
		{
			public int fontid;
			public class CFontStateParam
			{
				public Color color = Color.White;
				public Color color2 = Color.White;
				public Color effcolor = Color.Black;
				public bool underline;
				public bool shadow;
				public bool outline;
				public CFontStateParam Clone()
				{
					CFontStateParam fs = new CFontStateParam();
					fs.color = color;
					fs.color2 = color2;
					fs.effcolor = effcolor;
					fs.underline = underline;
					fs.shadow = shadow;
					fs.outline = outline;
					return fs;
				}
			}
			public CFontStateParam[] state = new CFontStateParam[5]
			{
				new CFontStateParam(),
				new CFontStateParam(),
				new CFontStateParam(),
				new CFontStateParam(),
				new CFontStateParam(),
			};
			public CFontFormatParam Clone()
			{
				CFontFormatParam fp = new CFontFormatParam();
				fp.fontid = fontid;
				for(int i = 0; i < 5; ++i)
					fp.state[i] = state[i].Clone();
				return fp;
			}
			public ETextEffect GetEffect(int i)
			{
				ETextEffect effect = ETextEffect.NONE;
				if(state[i].underline) effect |= ETextEffect.UNDERLINE;
				if(state[i].outline) effect |= ETextEffect.OUTLINE;
				else if(state[i].shadow) effect |= ETextEffect.SHADOW;
				return effect;
			}
		}

		private static readonly Dictionary<int, CFontParam> m_font = new Dictionary<int, CFontParam>();
		private static readonly Dictionary<string, CFontFormatParam> m_font_format = new Dictionary<string, CFontFormatParam>();

		private static readonly Dictionary<int, WeakReference> m_font_render = new Dictionary<int, WeakReference>();
		private static readonly PrivateFontCollection m_privatefont = new PrivateFontCollection();
		private static readonly Dictionary<string, string> m_fontname = new Dictionary<string, string>();

		private static IRender m_render_now;

		public static Dictionary<int, CFontParam> FontProfile
		{
			get { return m_font; }
		}

		public static Dictionary<string, CFontFormatParam> FontFormatProfile
		{
			get { return m_font_format; }
		}

		public static bool LoadXml(XmlElement sb)
		{
			if(sb.Name != "fd")
				return false;

			CFontParam item = new CFontParam();

			string fonttype = sb.GetAttribute("fonttype");
			if(fonttype == null)
				return false;
			if(!Int32.TryParse(fonttype, out item.id))
				return false;

			item.name = sb.GetAttribute("fontname");
			if(item.name == null)
				return false;

			string fontsize = sb.GetAttribute("height");
			if(fontsize == null)
				return false;
			if(!Int32.TryParse(fontsize, out item.size))
				return false;

			item.file = sb.GetAttribute("file");
			if(item.file == null)
				return false;
			m_font[item.id] = item;

			if(m_fontname.ContainsKey(item.file))
				item.fontname = m_fontname[item.file];
			else
			{
				item.fontname = sb.GetAttribute("name");
				try
				{
					m_privatefont.AddFontFile(item.file);
					if(item.fontname == null || item.fontname.Length <= 0)
						item.fontname = m_fontname[item.file] = m_privatefont.Families[m_privatefont.Families.Length - 1].Name;
				}
				catch
				{
					if(item.fontname == null)
						return false;
				}
			}

			return true;
		}

		public static bool LoadFile(string filename)
		{
			try
			{
				XmlDocument doc = new XmlDocument();
				doc.Load(filename);
				XmlElement data = doc["data"];
				if(data == null)
					return false;
				XmlElement fontdef = data["FontDef"];
				if(fontdef == null)
					return false;

				foreach(XmlNode node in fontdef.ChildNodes)
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

		public static Dictionary<int, CFontParam>.KeyCollection GetKeys()
		{
			return m_font.Keys;
		}

		public static Dictionary<int, CFontParam>.ValueCollection GetValues()
		{
			return m_font.Values;
		}

		public static CFontParam Find(int id)
		{
			return m_font.ContainsKey(id) ? m_font[id] : null;
		}

		public static IFont GetFont(int id)
		{
			if(m_render_now != Program.GetMainForm().Panel.Render)
			{
				m_render_now = Program.GetMainForm().Panel.Render;
				m_font_render.Clear();
			}

			IFont font = null;
			if(m_font_render.ContainsKey(id))
				font = m_font_render[id].Target as IFont;
			if(font == null && m_font.ContainsKey(id))
			{
				CFontParam fp = m_font[id];
				font = m_render_now.CreateFont();
				if(font.SetFont(fp.fontname, fp.size))
					m_font_render[id] = new WeakReference(font);
			}
			return font;
		}

		public static Dictionary<string, CFontFormatParam>.KeyCollection GetFormatKeys()
		{
			return m_font_format.Keys;
		}

		public static Dictionary<string, CFontFormatParam>.ValueCollection GetFormatValues()
		{
			return m_font_format.Values;
		}

		public static CFontFormatParam FindFormat(string name)
		{
			return name != null && m_font_format.ContainsKey(name) ? m_font_format[name] : null;
		}
	}
}
