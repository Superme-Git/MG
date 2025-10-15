/***********************************************************************
    filename:   CEGUIBasicRenderedStringParser.cpp
    created:    28/05/2009
    author:     Paul Turner
 *************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2009 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#include "CEGUIEditboxStringParser.h"
#include "CEGUILogger.h"




// Start of CEGUI namespace section
namespace CEGUI
{


	//----------------------------------------------------------------------------//
EditboxStringParser::EditboxStringParser()
{

}

//----------------------------------------------------------------------------//
EditboxStringParser::EditboxStringParser(const String& initial_font,
                                            const ColourRect& initial_colours) :
     BasicRenderedStringParser(initial_font,initial_colours)
   
{

}

//----------------------------------------------------------------------------//
EditboxStringParser::~EditboxStringParser()
{
}

//----------------------------------------------------------------------------//
bool EditboxStringParser::parse(const String& input_string,String& out_String)
{
    // first-time initialisation (due to issues with static creation order)
    if (!d_initialised)
        initialiseTagHandlers();

    initialiseDefaultState();

  

    RenderedString rs;
    String curr_section("");
	out_String="";

    size_t curr_pos = 0;
	bool bChange=false;

    while (curr_pos < input_string.length())
    {
        size_t cstart_pos = input_string.find_first_of('[', curr_pos);

        // if no control sequence start char, add remaining text verbatim.
        if (String::npos == cstart_pos)
        {
            curr_section += input_string.substr(curr_pos);
            curr_pos = input_string.length();
        }
        else if (cstart_pos == curr_pos || input_string[cstart_pos - 1] != '\\')
        {
            // append everything up to the control start to curr_section.
            curr_section += input_string.substr(curr_pos, cstart_pos - curr_pos);

            // scan forward for end of control sequence
            size_t cend_pos = input_string.find_first_of(']', cstart_pos);
            // if not found, treat as plain text
            if (String::npos == cend_pos)
            {
                curr_section += input_string.substr(curr_pos);
                curr_pos = input_string.length();
            }
            // extract control string
            else
            {
                //appendRenderedText(rs, curr_section);
               // curr_section.clear();

                String ctrl_string(
                    input_string.substr(cstart_pos + 1,
                                        cend_pos - cstart_pos - 1));
                curr_pos = cend_pos + 1;

               bool bHandle=processControlString(ctrl_string);
			   if(!bHandle) 
               {
                   curr_section += '[';				   
				   curr_section+=ctrl_string;
				   curr_section += ']';
;               }else
			   {
				   bChange=true;

			   }
                continue;
            }
        }
        else
        {
            curr_section += input_string.substr(curr_pos,
                                                cstart_pos - curr_pos - 1);
            curr_section += '[';
            curr_pos = cstart_pos + 1;
            continue;
        }

        //appendRenderedText(rs, curr_section);
        //curr_section.clear();
    }
	out_String=curr_section;
	return bChange;;
}

//----------------------------------------------------------------------------//


//----------------------------------------------------------------------------//
bool EditboxStringParser::processControlString( const String& ctrl_str)
{
    // all our default strings are of the form <var> = <val>
    // so do a quick check for the = char and abort if it's not there.
    if (String::npos == ctrl_str.find('='))
    {
        Logger::getSingleton().logEvent(
            "BasicRenderedStringParser::processControlString: unable to make "
            "sense of control string '" + ctrl_str + "'.  Ignoring!");
        
        return false;
    }

    char var_buf[128];
    char val_buf[128];
    sscanf(ctrl_str.c_str(), " %127[^ =] = '%127[^']", var_buf, val_buf);

    const String var_str(var_buf);
    const String val_str(val_buf);

    // look up handler function
    TagHandlerMap::iterator i = d_tagHandlers.find(var_str);
    // despatch handler, or log error
    if (i == d_tagHandlers.end())
	{
		Logger::getSingleton().logEvent(
			"BasicRenderedStringParser::processControlString: unknown "
			"control variable '" + var_str + "'.  Ignoring!");
		return false;
	}
        
   return true;
}



//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
