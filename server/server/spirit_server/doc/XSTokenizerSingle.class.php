<?php
//require_once '/usr/local/xunsearch/sdk/php/lib/XS.php';
class XSTokenizerSingle implements XSTokenizer
{
        private $arg = 1;
        public function __construct($arg = null)
        {
                if ($arg !== null && $arg !== '') {
                        $this->arg = intval($arg);
                        if ($this->arg < 1 || $this->arg > 255) {
                                throw new XSException('Invalid argument for ' . __CLASS__ . ': ' . $arg);
                        }
                }
        }
        public function getTokens($value, XSDocument $doc = null)
        {
                $terms = array();
                for ($i = 0; $i < mb_strlen($value, 'utf-8'); $i += $this->arg) {
                        $terms[] = mb_substr($value, $i, $this->arg, 'utf-8');
                }
                //var_dump($terms);
                return $terms;
        }
}