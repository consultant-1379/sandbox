var API = {};

API.BASE_URL_LOCAL = 'http://atrcxb2994.athtem.eei.ericsson.se/wordpress/wp-json/wp/v2/';
API.BASE_URL_TEST = 'http://atrcxb2994.athtem.eei.ericsson.se/wordpress/wp-json/wp/v2/';
API.BASE_URL_LIVE = 'http://analytics.ericsson.se:23307/wp-json/wp/v2/';

API.BASE_PAAS_TEST = 'http://eselivm3v260l.lmera.ericsson.se:8889/v1/';
API.BASE_PAAS_LIVE = 'http://atrcxb2994.athtem.eei.ericsson.se:8889/v1/';
API.BASE_PAAS_TM_TEST = 'http://eselivm3v260l.lmera.ericsson.se/templatemanager/v1/';
API.BASE_PAAS_TM_LIVE = 'http://atrcxb2994.athtem.eei.ericsson.se/templatemanager/v1/';

API.BASE_URL = API.BASE_URL_LIVE;
API.BASE_PAAS_URL = API.BASE_PAAS_TEST;
API.BASE_PAAS_TM_URL = API.BASE_PAAS_TM_TEST;

API.DOC = API.BASE_URL + 'pages?slug=:docname';


API.ERROR = API.BASE_URL + 'error';

API.set = function(key, value) {
    API[key] = value;
}

API.get = function(key) {
    return API[key];
}