// Created by: Ariel Flesler | http://flesler.blogspot.com/2008/03/xmlwriter-for-javascript.html
// Licensed under: BSD License
// This script downloaded from www.JavaScriptBank.com

/**
 * XMLWriter - XML generator for Javascript, based on .NET's XMLTextWriter.
 * Copyright (c) 2008 Ariel Flesler - aflesler(at)gmail(dot)com | http://flesler.blogspot.com
 * Licensed under BSD (http://www.opensource.org/licenses/bsd-license.php)
 * Date: 3/12/2008
 * @version 1.0.0
 * @author Ariel Flesler
 * http://flesler.blogspot.com/2008/03/xmlwriter-for-javascript.html
 */
 
function XMLWriter( encoding, version ){
	if( encoding )
		this.encoding = encoding;
	if( version )
		this.version = version;
};
(function(){
	
XMLWriter.prototype = {
	encoding:'ISO-8859-1',// what is the encoding
	version:'1.0', //what xml version to use
	formatting: 'indented', //how to format the output (indented/none)  ?
	indentChar:'\t', //char to use for indent
	indentation: 1, //how many indentChar to add per level
	newLine: '\n', //character to separate nodes when formatting
	
	//start a new document, cleanup if we are reusing
	document:function( standalone ){
		this.close();//cleanup
		this.stack = [ ];
		this.standalone = standalone;
		return this;
	},
	//get back to the root
	endDocument:function(){
		this.active = this.root;
		this.stack = [ ];
		return this;
	},
	//set the text of the doctype
	docType:function( dt ){
		this.doctype = dt;
		return this;
	},
	//start a new node with this name, and an optional namespace
	element:function( name, ns ){
		if( ns )//namespace
			name = ns + ':' + name;
		
		var node = { n:name, a:{ }, c: [ ] };//(n)ame, (a)ttributes, (c)hildren
		
		if( this.active ){
			this.active.c.push(node);
			this.stack.push(this.active);
		}else
			this.root = node;
		this.active = node;
		return this;
	},
	//go up one node, if we are in the root, ignore it
	endElement:function(){
		this.active = this.stack.pop() || this.root;
		return this;
	},
	//add an attribute to the active node
	attr:function( name, value ){
		if( this.active )
			this.active.a[name] = value;
		return this;
	},
	//add a text node to the active node
	text:function( text ){
		if( this.active )
			this.active.c.push(text);
		return this;
	},
	//shortcut, open an element, write the text and close
	elementAll:function( name, text, ns ){
		return this.element( name, ns ).text( text ).endElement();
	},
	//add a text node wrapped with CDATA
	cdata:function( text ){
		return this.text( '<![CDATA[' + text + ']]>' );
	},
	//add a text node wrapped in a comment
	comment:function( text ){
		return this.text('<!-- ' + text + ' -->');
	},
	//generate the xml string, you can skip closing the last nodes
	flush:function(){		
		if( this.stack && this.stack[0] )//ensure it's closed
			this.endDocument();
		
		var 
			chr = '', indent = '', num = this.indentation,
			formatting = this.formatting.toLowerCase() == 'indented';
		
		buffer = [];
		
		if( formatting ){
			while( num-- )
				chr += this.indentChar;
		}
		
		if( this.root )//skip if no element was added
			format( this.root, indent, chr, buffer );
		
		return buffer.join( formatting ? this.newLine : '' );
	},
	//cleanup, don't use again without calling startDocument
	close:function(){
		if( this.root )
			clean( this.root );
		this.active = this.root = this.stack = null;
	},
	getDocument: window.ActiveXObject 
		? function(){ //MSIE
			var doc = new ActiveXObject('Microsoft.XMLDOM');
			doc.async = false;
			doc.loadXML(this.flush());
			return doc;
		}
		: function(){// Mozilla, Firefox, Opera, etc.
			return (new DOMParser()).parseFromString(this.flush(),'text/xml');
	}
};

//utility, you don't need it
function clean( node ){
	var l = node.c.length;
	while( l-- ){
		if( typeof node.c[l] == 'object' )
			clean( node.c[l] );
	}
	node.n = node.a = node.c = null;	
};

//utility, you don't need it
function format( node, indent, chr, buffer ){
	var 
		xml = indent + '<' + node.n,
		nc = node.c.length,
		attr, child, i = 0;
		
	for( attr in node.a )
		xml += ' ' + attr + '="' + node.a[attr] + '"';
	
	xml += nc ? '>' : ' />';

	buffer.push( xml );
		
	if( nc ){
		do{
			child = node.c[i++];
			if( typeof child == 'string' ){
				if( nc == 1 )//single text node
					return buffer.push( buffer.pop() + child + '</'+node.n+'>' );					
				else //regular text node
					buffer.push( indent+chr+child );
			}else if( typeof child == 'object' ) //element node
				format(child, indent+chr, chr, buffer);
		}while( i < nc );
		buffer.push( indent + '</'+node.n+'>' );
	}
};

})();
