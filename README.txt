Document-provider is a library especially useful for testing an application that works with documents of various types :
html - "html"
pdf - "pdf"
odt - "vnd.oasis.opendocument.text"
docx - "vnd.openxmlformats-officedocument.wordprocessingml.document"
doc - "msword"
xlsx - "vnd.openxmlformats-officedocument.spreadsheetml.sheet"
xls - "vnd.ms-excel"
ppt - "vnd.ms-powerpoint"

and languages
(bg, es, cs, da, de, et, el, en, fr, it, lv, lt, hu, mt, nl, pl, pt, ro, sk, sl, fi, sv)

Usage :

mvn install

use it as a maven dependency and call its API via DocumentProvider

DocumentProvider.getDocByTypeAndLang("doc", "en");
