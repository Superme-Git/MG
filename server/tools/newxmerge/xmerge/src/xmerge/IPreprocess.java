/**
 * Class: IPreprocess.java
 * Package: xmerge
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-5-6 		yesheng
 *
 * Copyright (c) 2012, Perfect World All Rights Reserved.
*/

package xmerge;

import mkdb.util.Dbx;

/**
 * ClassName:IPreprocess
 * Function: 对于一些需要在合服前预处理的表,实现这个接口,这样在合服之前,相应的表就会
 * 先进行处理.这个接口主要用来处理一些合服Imerge接口无法handle的情况,比如IMerge只会
 * 提供冲突的key和srcvalue,dstvalue,但有的表的merge是需要两个表的所有记录重排的
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-5-6		上午08:41:49
 *
 * @see 	 
 */
public interface IPreprocess {

	/**
	 * 
	 * preProcess:
	 *
	 * @param src_table 需要预处理的表,表名和dst_table相同,在xmerge.xml中定义
	 * @param dst_table 需要预处理的表,表名和src_table相同,在xmerge.xml中定义
	 * @return    
	 * boolean   true表示预处理成功, 如果返回false,合服失败,回滚.
	 * @throws 
	 * @since  　
	 */
	public boolean preProcess(Dbx srcdb,Dbx.Table src_table,Dbx dstdb,Dbx.Table dst_table);
}

