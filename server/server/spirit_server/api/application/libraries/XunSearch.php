<?php
/**
 * Yii-Xunsearch class file
 *
 * @author Hightman <hightman2[at]yahoo[dot]com[dot]cn>
 * @link http://www.xunsearch.com/
 * @version 1.0
 */

//
// 支持的 GET 参数列表
// q: 查询语句
// m: 开启模糊搜索，其值为 yes/no
// f: 只搜索某个字段，其值为字段名称，要求该字段的索引方式为 self/both
// s: 排序字段名称及方式，其值形式为：xxx_ASC 或 xxx_DESC
// p: 显示第几页，每页数量为 XSSearch::PAGE_SIZE 即 10 条
// ie: 查询语句编码，默认为 UTF-8
// oe: 输出编码，默认为 UTF-8
// xml: 是否将搜索结果以 XML 格式输出，其值为 yes/no
//

class XunSearch
{
	private $_xs, $_search, $_scws;

	public function __construct($project)
	{
		$path = config_item('xunsearch_sdk');
		require_once $path.'/php/lib/XS.php';
		$this->_xs = new XS($project);
		$this->_xs->setDefaultCharset('utf-8');
		
		$this->_search = $this->_xs->search;
		$this->_search->setCharset('UTF-8');
		
	}

	/**
	 * Quickly add a new document (without checking key conflicts)
	 * @param mixed $data XSDocument object or data array to be added
	 */
	public function getSearch($q, $m, $f, $s, $p, $syn)
	{
		// recheck request parameters
		$q = get_magic_quotes_gpc() ? stripslashes($q) : $q;
		$f = empty($f) ? '_all' : $f;
		${'m_check'} = ($m == 'yes' ? ' checked' : '');
		${'syn_check'} = ($syn == 'yes' ? ' checked' : '');
		${'f_' . $f} = ' checked';
		${'s_' . $s} = ' selected';
		
		if (empty($q))
		{
			return null;
		}
		
		$total_begin = microtime(true);
		
		// fuzzy search
		$this->_search->setFuzzy(true);
		 
		// synonym search
		$this->_search->setAutoSynonyms($syn === 'yes');
		 
		// set query
		if (!empty($f) && $f != '_all') {
			$this->_search->setQuery($f . ':(' . $q . ')');
		} else {
			$this->_search->setQuery('subject:'.$q);
		}
		 
		// set sort
		if (($pos = strrpos($s, '_')) !== false) {
			$sf = substr($s, 0, $pos);
			$st = substr($s, $pos + 1);
			$this->_search->setSort($sf, $st === 'ASC');
		}
		 
		// set offset, limit
		$p = max(1, intval($p));
		$n = XSSearch::PAGE_SIZE;
		$this->_search->setLimit($n, ($p - 1) * $n);
		 
		// get the result
		$search_begin = microtime(true);
		$docs = $this->_search->search();
		$search_cost = microtime(true) - $search_begin;
		 
		// get other result
		$count = $this->_search->getLastCount();
		$total = $this->_search->getDbTotal();
		
		//校正词汇, 您是不是想找.., 当结果集较少时
		$corrected = array();
		if ($count < 1 || $count < ceil(0.001 * $total)) {
			$corrected = $this->_search->getCorrectedQuery();
		}
		//相关搜索, get related query
		$related = $this->_search->getRelatedQuery();
		
		//搜索建议
		$expanded = array();
		if ($count < 1) {
			$expanded = $this->_search->getExpandedQuery($q);
		}
		
		// calculate total time cost
		$total_cost = microtime(true) - $total_begin;
		
		
		$list = array();
    	if ($docs)
    	{
    		foreach ($docs as $doc)
    		{
    			$list[] = array(
    					'faq_id' => $doc->faq_id,
    					'title' => $doc->subject,
    					'percent' => $doc->percent(),
    					'rank' => $doc->rank()
    			);
    		}
    	}
	
		$result = array();
		$result['q'] = $q;
		$result['list'] = $list;
		$result['search_cost'] = $search_cost;
		$result['total_cost'] = $total_cost;
		$result['count'] = $count;
		//$result['total'] = $total;
		$result['corrected'] = $corrected;
		$result['related'] = $related;
		$result['expanded'] = $expanded;
		return $result;
	}
	
	/**
	 * 获取热搜的词
	 * 
	 */
	public function getHotQuery()
	{
		return $this->_search->getHotQuery();
	}
	
	/**
	 * Quickly add a new document (without checking key conflicts)
	 * @param mixed $data XSDocument object or data array to be added
	 */
	public function add($data)
	{
		$this->update($data, true);
	}

	/**
	 * @param mixed $data XSDocument object or data array to be updated
	 * @param boolean $add whether to add directly, default to false
	 */
	public function update($data, $add = false)
	{
		if ($data instanceof XSDocument) {
			$this->_xs->index->update($data, $add);
		} else {
			$doc = new XSDocument($data);
			$this->_xs->index->update($doc, $add);
		}
	}
	
	/**
	 * @return XSTokenizerScws get scws tokenizer
	 */
	public function getScws()
	{
		if ($this->_scws === null) {
			$this->_scws = new XSTokenizerScws;
			//$this->_scws->addDict($fpath);
		}
		return $this->_scws;
	}
	
	/**
	 * 查看分词结果
	 * 
	 * @param string $text
	 * @return array
	 */
	public function getScwsResult($text)
	{
		return $this->getScws()->getResult($text);
	}
	
}
