using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSTools
{
    class ToolsKunTaCode
    {

        static private void W2F(ToolsStream TS,int TabCount,string Data)
        {
            TS.WriteStrA(ToolsStr.Tabs(TabCount) + Data);
        }
        static public void ExportCodeH(ToolsTable TT, ToolsStream TS, string SrcTableFileName, string DstCodeFileType)
        {
            if (DstCodeFileType == "Client")
            {
                ExportCodeHClient(TT, TS, SrcTableFileName);
            }
            else if (DstCodeFileType == "Server")
            {
                ExportCodeHServer(TT, TS, SrcTableFileName);
            }
        }
        static public void ExportCodeCPP(ToolsTable TT, ToolsStream TS, string SrcTableFileName, string DstCodeFileType)
        {
            if (DstCodeFileType == "Client")
            {
                ExportCodeCPPClient(TT, TS, SrcTableFileName);
            }
            else if (DstCodeFileType == "Server")
            {
                ExportCodeCPPServer(TT, TS, SrcTableFileName);
            }
        }
        static private void ExportCodeHClient(ToolsTable TT, ToolsStream TS, string SrcTableFileName)
        {
            string CurClassName = SrcTableFileName;
            string CurStructName = "S" + SrcTableFileName;

	        W2F(TS, 0, "#ifndef _config_" + SrcTableFileName + "_included_\r\n");
	        W2F(TS, 0, "#define _config_" + SrcTableFileName + "_included_\r\n");
	        W2F(TS, 0, "\r\n");
	        W2F(TS, 0, "#include \"Core.h\"\r\n");
	        W2F(TS, 0, "\r\n");

	        W2F(TS, 0, "namespace FireClient\r\n");
	        W2F(TS, 0, "{\r\n");
		        W2F(TS, 1, "struct " + CurStructName + "\r\n");
		        W2F(TS, 1, "{\r\n");
			        for(int i = 0; i < TT.GetColCount(); i++)
			        {
				        string cnname = TT.GetCell(0, i);
                        string colname = TT.GetCell(1, i);
				        string typname = TT.GetCell(2, i);
				        
				        if(typname == "int")
                        {
					        W2F(TS, 2, "NUCLEUS::i32 " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "long")
                        {
                            W2F(TS, 2, "NUCLEUS::i64 " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "float")
                        {
                            W2F(TS, 2, "float " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "double")
                        {
                            W2F(TS, 2, "double " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "string")
                        {
                            W2F(TS, 2, "std::string " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "bool")
                        {
                            W2F(TS, 2, "bool " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "string[]")
                        {
                            W2F(TS, 2, "std::vector<std::string> " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "float[]")
                        {
                            W2F(TS, 2, "std::vector<float> " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "int[]")
                        {
                            W2F(TS, 2, "std::vector<NUCLEUS::i32> " + colname + ";\t// " + cnname + "\r\n");
                        }
				        else if(typname == "")
                        {
					        continue;
                        }
				        else
                        {
                            continue;
					        //ASSERT(false && "invalid_configure");
                        }
			        }
		        W2F(TS, 1, "};\r\n");
		        W2F(TS, 1, "\r\n");

                string cls_comment_fmt = ToolsStr.Tabs(1) + "/* \r\n";
                       cls_comment_fmt += ToolsStr.Tabs(1) + "@class " + SrcTableFileName + "\r\n";
                       cls_comment_fmt += ToolsStr.Tabs(1) + "@author tool ExcelParse\r\n";
                       cls_comment_fmt += ToolsStr.Tabs(1) + "@file clconf_" + SrcTableFileName + ".h\r\n";
                       cls_comment_fmt += ToolsStr.Tabs(1) + "@brief 从" + SrcTableFileName + "文件中自动生成的配置类\r\n";
                       cls_comment_fmt += ToolsStr.Tabs(1) + "*/ \r\n";
	      
		    W2F(TS, 0, cls_comment_fmt);

		        W2F(TS, 1, "class " + CurClassName + "\r\n");
		        W2F(TS, 1, "{\r\n");
			    W2F(TS, 1, "public:\r\n");
				    W2F(TS, 2, "bool LoadFrom(const char* filename);\r\n");
				    // LoadFrom
				    W2F(TS, 2, "bool LoadFrom(const std::string& filename)\r\n");
				    W2F(TS, 2, "{\r\n");
					    W2F(TS, 3, "return LoadFrom(filename.c_str());\r\n");
				    W2F(TS, 2, "}\r\n");
			    W2F(TS, 1, "public:\r\n");
				    W2F(TS, 2, CurStructName + "* Get(size_t);\r\n");
			    W2F(TS, 1, "public:\r\n");
				    W2F(TS, 2, "inline size_t Count()\r\n");
				    W2F(TS, 2, "{\r\n");
					    W2F(TS, 3, "return m_vtConfigures.size();\r\n");
				    W2F(TS, 2, "}\r\n");
			    W2F(TS, 1, "public:\r\n");
				    W2F(TS, 2, "typedef std::map<int," + CurStructName + " *> CollectionConfiguresT;\r\n");
				    W2F(TS, 2, "CollectionConfiguresT m_vtConfigures;\r\n");
		        W2F(TS, 1, "};\r\n");
	        W2F(TS, 0, "}\r\n");

            W2F(TS, 0, "#endif // _config_" + SrcTableFileName + "_included_\r\n");
        }
        static private void ExportCodeCPPClient(ToolsTable TT, ToolsStream TS, string SrcTableFileName)
        {
            string CurClassName = SrcTableFileName;
            string CurStructName = "S" + SrcTableFileName;

            W2F(TS, 0, "#include \"clconf_" + SrcTableFileName + ".h\"\r\n");
            W2F(TS, 0, "#include \"ConfCsvReader.h\"\r\n");
            W2F(TS, 0, "\r\n");
            W2F(TS, 0, "namespace FireClient\r\n");
            W2F(TS, 0, "{\r\n");
            W2F(TS, 1, "bool " + CurClassName + "::LoadFrom(const char* filename)\r\n");
            W2F(TS, 1, "{\r\n");
                W2F(TS, 2, "CSVReader csv;\r\n");
                W2F(TS, 2, "if(csv.load(filename) != 0)\r\n");
                    W2F(TS, 3, "return false;\r\n");
                    for (int col = 0; col < TT.GetColCount(); col++)
                    {
                        string cnname = TT.GetCell(0, col);
                        string colname = TT.GetCell(1, col);
                        if (colname == "" || cnname == "")
                        {
                            continue;
                        }
                        W2F(TS, 2, "size_t index_" + colname + " = csv.index(\"" + colname + "\"," + "1);\r\n");
                        W2F(TS, 2, "\r\n");
                    }
                    W2F(TS, 2, "\r\n");
                    W2F(TS, 2, "for(size_t row = 3; row < csv.count(); ++row)\r\n");
                    W2F(TS, 2, "{\r\n");
                        W2F(TS, 3, CurStructName + " * conf = new " + CurStructName + ";\r\n");

                        for(int col = 0; col < TT.GetColCount(); col++)
                        {
                            string colname = TT.GetCell(1, col);
                            string typname = TT.GetCell(2, col);
                            if (colname == "" || typname == "")
                            {
                                continue;
                            }
                            if(typname == "int")
                            {
                                W2F(TS, 3, "conf->" + colname + " = csv.get_i32(row,index_" + colname + ");\r\n");
                            }
                            else if(typname == "long")
                            {
                                W2F(TS, 3, "conf->" + colname + " = csv.get_i64(row,index_" + colname + ");\r\n");
                            }
                            else if(typname == "float")
                            {
                                W2F(TS, 3, "conf->" + colname + " = (float)csv.get_dec(row,index_" + colname + ");\r\n");
                            }
                            else if(typname == "double")
                            {
                                W2F(TS, 3, "conf->" + colname + " = csv.get_dec(row,index_" + colname + ");\r\n");
                            }
                            else if(typname == "string")
                            {
                                W2F(TS, 3, "conf->" + colname + " = csv.get_str(row,index_" + colname + ");\r\n");
                            }
                            else if(typname == "bool")
                            {
                                W2F(TS, 3, "conf->" + colname + " = csv.get_bool(row,index_" + colname + ");\r\n");
                            }
                            else if(typname == "string[]")
                            {
                                W2F(TS, 3, "{\r\n");
                                    W2F(TS,4,"const char* __tmp = csv.get_str(row,index_" + colname + ");\r\n");
                                    W2F(TS, 4, "csv.tokenize(__tmp,conf->" + colname + ToolsStr.DH() +
                                        ToolsStr.CombineStr(ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.DH(),
                                        ToolsStr.YH(), ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.XG(),
                                        ToolsStr.YH(), ToolsStr.YH()) + ");\r\n");
                                W2F(TS, 3, "}\r\n");
                            }
                            else if(typname == "float[]")
                            {
                                W2F(TS, 3, "{\r\n");
                                    W2F(TS, 4, "std::vector<std::string> vals;\r\n");
                                    W2F(TS, 4, "const char* __tmp = csv.get_str(row,index_" + colname + ");\r\n");
                                    W2F(TS, 4, "csv.tokenize(__tmp,vals," +
                                        ToolsStr.CombineStr(ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.DH(),
                                        ToolsStr.YH(), ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.XG(),
                                        ToolsStr.YH(), ToolsStr.YH()) + ");\r\n");
                                    W2F(TS,4,"for(size_t i = 0; i < vals.size(); ++i)\r\n");
                                        W2F(TS, 5, "conf->" + colname + ".push_back(NUCLEUS::StringUtil::ParseFloat(vals[i].c_str()));\r\n");
                                W2F(TS, 3, "}\r\n");
                            }
                            else if(typname == "int[]")
                            {
                                W2F(TS, 3, "{\r\n");
                                    W2F(TS, 4, "std::vector<std::string> vals;\r\n");
                                    W2F(TS, 4, "const char* __tmp = csv.get_str(row,index_" + colname + ");\r\n");
                                    W2F(TS, 4, "csv.tokenize(__tmp,vals," +
                                        ToolsStr.CombineStr(ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.DH(),
                                        ToolsStr.YH(), ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.XG(),
                                        ToolsStr.YH(), ToolsStr.YH()) + ");\r\n");
                                    W2F(TS, 4, "for(size_t i = 0; i < vals.size(); ++i)\r\n");
                                        W2F(TS, 5, "conf->" + colname + ".push_back(NUCLEUS::StringUtil::ParseI64(vals[i].c_str()));\r\n");
                                W2F(TS, 3, "}\r\n");
                            }
                        }
                        W2F(TS, 3, "m_vtConfigures.insert(std::make_pair(csv.get_i32(row,0),conf));\r\n");
                    W2F(TS, 2, "}\r\n");
                    W2F(TS, 2, "return true;\r\n");
                W2F(TS, 1, "}\r\n");

                W2F(TS, 1, "\r\n");
                // Get
                W2F(TS, 1, CurStructName + "* " + CurClassName + "::Get(size_t id)\r\n");
                W2F(TS, 1, "{\r\n");
                    W2F(TS, 2, "if(m_vtConfigures.find(id) == m_vtConfigures.end())\r\n");
                    W2F(TS, 2, "{\r\n");
                        W2F(TS, 3, "return NULL;\r\n");
                    W2F(TS, 2, "}\r\n");
                    W2F(TS, 2, "else\r\n");
                    W2F(TS, 2, "{\r\n");
                        W2F(TS, 3, "return m_vtConfigures[id];\r\n");
                    W2F(TS, 2, "}\r\n");
                W2F(TS, 1, "}\r\n");
            W2F(TS, 0, "}\r\n");
        }
        static private void ExportCodeHServer(ToolsTable TT, ToolsStream TS, string SrcTableFileName)
        {
            string CurClassName = SrcTableFileName;
            string CurStructName = "S" + SrcTableFileName;

            W2F(TS, 0, "#ifndef _config_" + SrcTableFileName + "_included_\r\n");
            W2F(TS, 0, "#define _config_" + SrcTableFileName + "_included_\r\n");
            W2F(TS, 0, "\r\n");
            W2F(TS, 0, "#include \"lj_def.h\"\r\n");
            W2F(TS, 0, "\r\n");

            W2F(TS, 0, "namespace ljconf\r\n");
            W2F(TS, 0, "{\r\n");
            W2F(TS, 1, "struct " + CurStructName + "\r\n");
            W2F(TS, 1, "{\r\n");
            for (int i = 0; i < TT.GetColCount(); i++)
            {
                string cnname = TT.GetCell(0, i);
                string colname = TT.GetCell(1, i);
                string typname = TT.GetCell(2, i);

                if (typname == "int")
                {
                    W2F(TS, 2, "sint32 " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "long")
                {
                    W2F(TS, 2, "long " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "float")
                {
                    W2F(TS, 2, "float " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "double")
                {
                    W2F(TS, 2, "double " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "string")
                {
                    W2F(TS, 2, "std::string " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "bool")
                {
                    W2F(TS, 2, "bool " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "string[]")
                {
                    W2F(TS, 2, "std::vector<std::string> " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "float[]")
                {
                    W2F(TS, 2, "std::vector<float> " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "int[]")
                {
                    W2F(TS, 2, "std::vector<sint32> " + colname + ";\t// " + cnname + "\r\n");
                }
                else if (typname == "")
                {
                    continue;
                }
                else
                {
                    continue;
                    //ASSERT(false && "invalid_configure");
                }
            }
            W2F(TS, 1, "};\r\n");
            W2F(TS, 1, "\r\n");

            string cls_comment_fmt = ToolsStr.Tabs(1) + "/* \r\n";
            cls_comment_fmt += ToolsStr.Tabs(1) + "@class " + SrcTableFileName + "\r\n";
            cls_comment_fmt += ToolsStr.Tabs(1) + "@author tool ExcelParse\r\n";
            cls_comment_fmt += ToolsStr.Tabs(1) + "@file ljconf_" + SrcTableFileName + ".h\r\n";
            cls_comment_fmt += ToolsStr.Tabs(1) + "@brief 从" + SrcTableFileName + "文件中自动生成的配置类\r\n";
            cls_comment_fmt += ToolsStr.Tabs(1) + "*/ \r\n";

            W2F(TS, 0, cls_comment_fmt);

            W2F(TS, 1, "class " + CurClassName + "\r\n");
            W2F(TS, 1, "{\r\n");
            W2F(TS, 1, "public:\r\n");
            W2F(TS, 2, "bool LoadFrom(const char* filename);\r\n");
            // LoadFrom
            W2F(TS, 2, "bool LoadFrom(const std::string& filename)\r\n");
            W2F(TS, 2, "{\r\n");
            W2F(TS, 3, "return LoadFrom(filename.c_str());\r\n");
            W2F(TS, 2, "}\r\n");
            W2F(TS, 1, "public:\r\n");
            W2F(TS, 2, CurStructName + "& Get(size_t);\r\n");
            W2F(TS, 1, "public:\r\n");
            W2F(TS, 2, "inline size_t Count()\r\n");
            W2F(TS, 2, "{\r\n");
            W2F(TS, 3, "return m_vtConfigures.size();\r\n");
            W2F(TS, 2, "}\r\n");
            W2F(TS, 1, "public:\r\n");
            W2F(TS, 2, "typedef std::vector<" + CurStructName + "> CollectionConfiguresT;\r\n");
            W2F(TS, 2, "CollectionConfiguresT m_vtConfigures;\r\n");
            W2F(TS, 1, "};\r\n");
            W2F(TS, 0, "}\r\n");

            W2F(TS, 0, "#endif // _config_" + SrcTableFileName + "_included_\r\n");
        }
        static private void ExportCodeCPPServer(ToolsTable TT, ToolsStream TS, string SrcTableFileName)
        {
            string CurClassName = SrcTableFileName;
            string CurStructName = "S" + SrcTableFileName;

            W2F(TS, 0, "#include \"ljconf_" + SrcTableFileName + ".h\"\r\n");
            W2F(TS, 0, "#include \"lj_csv.h\"\r\n");
            W2F(TS, 0, "#include \"lj_utils.h\"\r\n");
            W2F(TS, 0, "#include \"tokenizer.h\"\r\n");
            W2F(TS, 0, "\r\n");
            W2F(TS, 0, "namespace ljconf\r\n");
            W2F(TS, 0, "{\r\n");
                W2F(TS, 1, "bool " + CurClassName + "::LoadFrom(const char* filename)\r\n");
                W2F(TS, 1, "{\r\n");
                    W2F(TS, 2, "ljutils::CSVReader csv;\r\n");
                    W2F(TS, 2, "if(csv.load(filename) != 0)\r\n");
                        W2F(TS, 3, "return false;\r\n");
                    for (int col = 0; col < TT.GetColCount(); col++)
                    {
                        string cnname = TT.GetCell(0, col);
                        string colname = TT.GetCell(1, col);
                        if (colname == "" || cnname == "")
                        {
                            continue;
                        }
                        W2F(TS, 2, "size_t index_" + colname + " = csv.index(\"" + colname + "\"," + "1);\r\n");
                        W2F(TS, 2, "\r\n");
                    }
                    W2F(TS, 2, "\r\n");
                    W2F(TS, 2, "for(size_t row = 3; row < csv.count(); ++row)\r\n");
                    W2F(TS, 2, "{\r\n");
                        W2F(TS, 3, CurStructName + " conf;\r\n");

                    for (int col = 0; col < TT.GetColCount(); col++)
                    {
                        string colname = TT.GetCell(1, col);
                        string typname = TT.GetCell(2, col);
                        if (colname == "" || typname == "")
                        {
                            continue;
                        }
                        if (typname == "int")
                        {
                            W2F(TS, 3, "conf." + colname + " = csv.get_i32(row,index_" + colname + ");\r\n");
                        }
                        else if (typname == "long")
                        {
                            W2F(TS, 3, "conf." + colname + " = csv.get_i64(row,index_" + colname + ");\r\n");
                        }
                        else if (typname == "float")
                        {
                            W2F(TS, 3, "conf." + colname + " = (float)csv.get_dec(row,index_" + colname + ");\r\n");
                        }
                        else if (typname == "double")
                        {
                            W2F(TS, 3, "conf." + colname + " = csv.get_dec(row,index_" + colname + ");\r\n");
                        }
                        else if (typname == "string")
                        {
                            W2F(TS, 3, "conf." + colname + " = csv.get_str(row,index_" + colname + ");\r\n");
                        }
                        else if (typname == "bool")
                        {
                            W2F(TS, 3, "conf." + colname + " = csv.get_bool(row,index_" + colname + ");\r\n");
                        }
                        else if (typname == "string[]")
                        {
                            W2F(TS, 3, "{\r\n");
                                W2F(TS, 4, "const char* __tmp = csv.get_str(row,index_" + colname + ");\r\n");
                                W2F(TS, 4, "tokenize(__tmp,conf." + colname + ToolsStr.DH() +
                                    ToolsStr.CombineStr(ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.DH(),
                                    ToolsStr.YH(), ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.XG(),
                                    ToolsStr.YH(), ToolsStr.YH()) + ");\r\n");
                            W2F(TS, 3, "}\r\n");
                        }
                        else if (typname == "float[]")
                        {
                            W2F(TS, 3, "{\r\n");
                                W2F(TS, 4, "std::vector<std::string> vals;\r\n");
                                W2F(TS, 4, "const char* __tmp = csv.get_str(row,index_" + colname + ");\r\n");
                                W2F(TS, 4, "tokenize(__tmp,vals," +
                                    ToolsStr.CombineStr(ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.DH(),
                                    ToolsStr.YH(), ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.XG(),
                                    ToolsStr.YH(), ToolsStr.YH()) + ");\r\n");
                                W2F(TS, 4, "for(size_t i = 0; i < vals.size(); ++i)\r\n");
                                W2F(TS, 5, "conf." + colname + ".push_back(ljutils::atof(vals[i].c_str()));\r\n");
                            W2F(TS, 3, "}\r\n");
                        }
                        else if (typname == "int[]")
                        {
                            W2F(TS, 3, "{\r\n");
                                W2F(TS, 4, "std::vector<std::string> vals;\r\n");
                                W2F(TS, 4, "const char* __tmp = csv.get_str(row,index_" + colname + ");\r\n");
                                W2F(TS, 4, "tokenize(__tmp,vals," +
                                    ToolsStr.CombineStr(ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.DH(),
                                    ToolsStr.YH(), ToolsStr.YH(), ToolsStr.DH(), ToolsStr.YH(), ToolsStr.XG(),
                                    ToolsStr.YH(), ToolsStr.YH()) + ");\r\n");
                                W2F(TS, 4, "for(size_t i = 0; i < vals.size(); ++i)\r\n");
                                W2F(TS, 5, "conf." + colname + ".push_back(ljutils::atoi(vals[i].c_str()));\r\n");
                            W2F(TS, 3, "}\r\n");
                        }
                    }
                        W2F(TS, 3, "m_vtConfigures.push_back(conf);\r\n");
                    W2F(TS, 2, "}\r\n");
                    W2F(TS, 2, "return true;\r\n");
                W2F(TS, 1, "}\r\n");

                W2F(TS, 1, "\r\n");
                // Get
                W2F(TS, 1, CurStructName + "& " + CurClassName + "::Get(size_t row)\r\n");
                W2F(TS, 1, "{\r\n");
                    W2F(TS, 2, "return m_vtConfigures.at(row);\r\n");
                W2F(TS, 1, "}\r\n");
            W2F(TS, 0, "}\r\n");
        }

    }
}
