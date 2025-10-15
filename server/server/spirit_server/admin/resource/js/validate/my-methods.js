jQuery.extend(jQuery.validator.messages, {
  required: "��ѡ�ֶ�",
  remote: "���������ֶ�",
  email: "��������ȷ��ʽ�ĵ����ʼ�",
  url: "������Ϸ�����ַ",
  date: "������Ϸ�������",
  dateISO: "������Ϸ������� (ISO).",
  number: "������Ϸ�������",
  digits: "ֻ����������",
  creditcard: "������Ϸ������ÿ���",
  equalTo: "���ٴ�������ͬ��ֵ",
  accept: "������ӵ�кϷ���׺�����ַ���",
  maxlength: jQuery.validator.format("������һ�� ��������� {0} ���ַ���"),
  minlength: jQuery.validator.format("������һ�� ���������� {0} ���ַ���"),
  rangelength: jQuery.validator.format("������ һ�����Ƚ��� {0} �� {1} ֮����ַ���"),
  range: jQuery.validator.format("������һ������ {0} �� {1} ֮���ֵ"),
  max: jQuery.validator.format("������һ�����Ϊ{0} ��ֵ"),
  min: jQuery.validator.format("������һ����СΪ{0} ��ֵ")
});
		
// �ֻ�������֤
jQuery.validator.addMethod("mobile", function(value, element) {
    var length = value.length;
    var mobile =  /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "�ֻ������ʽ����");   

// �绰������֤   
jQuery.validator.addMethod("phone", function(value, element) {
    var tel = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
    return this.optional(element) || (tel.test(value));
}, "�绰�����ʽ����");

// ����������֤   
jQuery.validator.addMethod("zipCode", function(value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "���������ʽ����");

// QQ������֤   
jQuery.validator.addMethod("qq", function(value, element) {
    var tel = /^[1-9]\d{4,9}$/;
    return this.optional(element) || (tel.test(value));
}, "qq�����ʽ����");

// IP��ַ��֤
jQuery.validator.addMethod("ip", function(value, element) {
    var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    return this.optional(element) || (ip.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
}, "Ip��ַ��ʽ����");

// ��ĸ�����ֵ���֤
jQuery.validator.addMethod("chrnum", function(value, element) {
    var chrnum = /^([a-zA-Z0-9]+)$/;
    return this.optional(element) || (chrnum.test(value));
}, "ֻ���������ֺ���ĸ(�ַ�A-Z, a-z, 0-9)");

// ���ĵ���֤
jQuery.validator.addMethod("chinese", function(value, element) {
    var chinese = /^[\u4e00-\u9fa5]+$/;
    return this.optional(element) || (chinese.test(value));
}, "ֻ����������");

// ��������֤
$.validator.addMethod("selectNone", function(value, element) {
    return value == "��ѡ��";
}, "����ѡ��һ��");

// �ֽڳ�����֤
jQuery.validator.addMethod("byteRangeLength", function(value, element, param) {
    var length = value.length;
    for (var i = 0; i < value.length; i++) {
        if (value.charCodeAt(i) > 127) {
            length++;
        }
    }
    return this.optional(element) || (length >= param[0] && length <= param[1]);
}, $.validator.format("��ȷ�������ֵ��{0}-{1}���ֽ�֮��(һ����������2���ֽ�)"));