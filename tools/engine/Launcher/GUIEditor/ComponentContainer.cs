using System;
using System.Collections.Generic;
using System.Drawing;
using System.Xml;

namespace GUIEditor2
{
	public abstract class CComponentContainer : CComponent
	{
		private readonly LinkedList<CComponent> m_list = new LinkedList<CComponent>();

		public LinkedList<CComponent> List
		{
			get { return m_list; }
		}

		public CComponentContainer(CComponentDesktop desktop) : base(desktop)
		{
		}

		public override bool OnLButtonDown(Point pos)
		{
			try
			{
				foreach(CComponent it in m_list)
				{
					if(it.OnLButtonDown(pos))
						return true;
				}
			}
			catch(System.InvalidOperationException) {}

			return base.OnLButtonDown(pos);
		}

		public override bool OnLButtonUp(Point pos)
		{
			foreach(CComponent it in m_list)
			{
				if(it.OnLButtonUp(pos))
					return true;
			}

			return base.OnLButtonUp(pos);
		}

		public override bool OnLButtonDoubleClick(Point pos)
		{
			foreach(CComponent it in m_list)
			{
				if(it.OnLButtonDoubleClick(pos))
					return true;
			}

			return base.OnLButtonDoubleClick(pos);
		}

		public override bool OnRButtonDown(Point pos)
		{
			foreach(CComponent it in m_list)
			{
				if(it.OnRButtonDown(pos))
					return true;
			}

			return base.OnRButtonDown(pos);
		}

		public override bool OnRButtonUp(Point pos)
		{
			foreach(CComponent it in m_list)
			{
				if(it.OnRButtonUp(pos))
					return true;
			}

			return base.OnRButtonUp(pos);
		}

		public override bool OnMouseMove(Point pos, int button)
		{
			foreach(CComponent it in m_list)
			{
				if(it.OnMouseMove(pos, button))
					return true;
			}

			return base.OnMouseMove(pos, button);
		}

		public override void OnDraw()
		{
			base.OnDraw();

			for(LinkedListNode<CComponent> node = m_list.Last; node != null; node = node.Previous)
				node.Value.OnDraw();
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			base.SaveXML(doc, elem);
			if(elem.Name != "Tab" && elem.Name != "AppleMenu" && m_list.Count > 0)
			{
				XmlElement content = doc.CreateElement("Content");
				for(LinkedListNode<CComponent> node = m_list.Last; node != null; node = node.Previous)
					node.Value.SaveXML(doc, content);
				elem.AppendChild(content);
			}
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			XmlElement content = elem["Content"];
			if(elem.Name != "Tab" && elem.Name != "AppleMenu" && content != null)
			{
				foreach(XmlNode node in content.ChildNodes)
				{
					if(node.NodeType == XmlNodeType.Element)
					{
						CComponent one;
						XmlElement sub = node as XmlElement;
						switch(sub.Name)
						{
						case "Base":		one = new CComponentBase(Desktop, Desktop.MainForm, ""); break;
						case "Static":		one = new CComponentStatic(Desktop, Desktop.MainForm, ""); break;
						case "Image":		one = new CComponentImage(Desktop, Desktop.MainForm, ""); break;
						case "ScrollBar":	one = new CComponentScrollBar(Desktop, Desktop.MainForm, ""); break;
						case "Text":		one = new CComponentText(Desktop, Desktop.MainForm, ""); break;
						case "Edit":		one = new CComponentEdit(Desktop, Desktop.MainForm, ""); break;
						case "Frame":		one = new CComponentFrame(Desktop, Desktop.MainForm, ""); break;
						case "ButtonImage":	one = new CComponentButtonImage(Desktop, Desktop.MainForm, ""); break;
						case "ButtonText":	one = new CComponentButtonText(Desktop, Desktop.MainForm, ""); break;
						case "ButtonRadio":	one = new CComponentButtonRadio(Desktop, Desktop.MainForm, ""); break;
						case "ButtonCheck":	one = new CComponentButtonCheck(Desktop, Desktop.MainForm, ""); break;
						case "ProgressBar":	one = new CComponentProgressBar(Desktop, Desktop.MainForm, ""); break;
						case "ComboBox":	one = new CComponentComboBox(Desktop, Desktop.MainForm, ""); break;
						case "ListBox":		one = new CComponentListBox(Desktop, Desktop.MainForm, ""); break;
						case "Table":		one = new CComponentTable(Desktop, Desktop.MainForm, ""); break;
						case "SliderBar":	one = new CComponentSliderBar(Desktop, Desktop.MainForm, ""); break;
						case "Panel":		one = new CComponentPanel(Desktop, Desktop.MainForm, ""); break;
						case "Tab":			one = new CComponentTab(Desktop, Desktop.MainForm, ""); break;
						case "AppleMenu":	one = new CComponentTabApple(Desktop, Desktop.MainForm, ""); break;
						default:			throw new Exception("未知的控件类型: " + sub.Name);
						}
						AddComponent(one);
						one.LoadXML(sub);
					}
				}
			}
		}

		public virtual T AddComponent<T>(T one) where T : CComponent
		{
			if(one == null || this == one)
				return null;

			one.Parent = this;

			return one;
		}

		public virtual void DelComponent(CComponent one)
		{
			if(HasChild(one) || this is CComponentDesktop)
			{
				if(one is CComponentContainer)
					(one as CComponentContainer).ClearComponent();
				Desktop.SelListDel(one);
				m_list.Remove(one);
				if(one.ListViewItem != null)
					one.ListViewItem.Remove();
				if(one == Desktop.Focus)
					Desktop.Focus = null;
				Desktop.DelLayout(one);
				one.Dispose();
			}
			LinkedListNode<CComponent> next = null;
			for(LinkedListNode<CComponent> node = m_list.First; node != null; node = next)
			{
				next = node.Next;
				if(node.Value is CComponentContainer)
					(node.Value as CComponentContainer).DelComponent(one);
			}
		}

		public virtual void DelComponent(string name)
		{
			LinkedListNode<CComponent> next = null;
			for(LinkedListNode<CComponent> node = m_list.First; node != null; node = next)
			{
				next = node.Next;
				if(node.Value.Name == name)
					DelComponent(node.Value);
			}
		}

		public virtual void ClearComponent()
		{
			LinkedListNode<CComponent> next = null;
			for(LinkedListNode<CComponent> node = m_list.First; node != null; node = next)
			{
				next = node.Next;
				if(node.Value != null)
				{
					DelComponent(node.Value);
					node.Value = null;
				}
			}
		}

		public virtual CComponent GetComponent(string name)
		{
			foreach(CComponent it in m_list)
			{
				if(it.Name == name)
					return it;
				if(it is CComponentContainer)
				{
					CComponent one = (it as CComponentContainer).GetComponent(name);
					if(one != null)
						return one;					
				}
			}
			return null;
		}

		public virtual CComponent GetComponent(Point pos)
		{
			if(Contain(pos))
			{
				foreach(CComponent it in m_list)
				{
					if(it.Contain(pos))
					{
						if(it is CComponentContainer)
						{
							CComponent one = (it as CComponentContainer).GetComponent(pos);
							if(one != null)
								return one;
						}
						return it;
					}
				}
				return this;
			}
			return null;
		}

		public virtual CComponentContainer GetContainer(CComponent one)	// 查找可以作为one的最内层容器组件
		{
			if(this != one && Contain(one))
			{
				foreach(CComponent it in m_list)
				{
					if(it is CComponentContainer)
					{
						CComponentContainer con = (it as CComponentContainer).GetContainer(one);
						if(con != null)
							return con;
					}					
				}
				if(!(this is CComponentTab))	// Tab控件的非页面区域(标签区)放置的控件不允许容纳其它控件
					return this;
			}
			return null;
		}

		public void GetAllEditComponentName(ref List<string> list)
		{
			foreach(CComponent it in m_list)
			{
				if(it is CComponentContainer)
					(it as CComponentContainer).GetAllEditComponentName(ref list);
				else if(it is CComponentEdit)
					list.Add(it.Name);
			}
		}
	}
}
