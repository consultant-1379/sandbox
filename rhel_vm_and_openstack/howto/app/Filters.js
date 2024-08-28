/**
 * Created By   : Pascal Leloup
 * Class        : Filters
 */
(function () {
    bps
        .filter('truncate', function () {
            return function (text, length, end) {
                try {
                    text = text.replace(/[\n\r]/g, '').replace(/&amp;/g, '&').trim();
                    if (isNaN(length) || length === undefined)
                        length = 10;

                    if (end === undefined)
                        end = "...";

                    if (text.length <= length || text.length - end.length <= length) {
                        return text;
                    }
                    else {
                        return String(text).replace(/[\n\r]/g, '').replace(/&amp;/g, '&').trim().substring(0, length - end.length) + end;
                    }
                } catch (e) {
                    //console.log('yawa oo: ', e.message);
                }
            };
        })
        .filter('decodeURL', function () {
            return function (url) {

                if (url !== undefined) {
                    var u = decodeURIComponent(url);

                    if (u.match(/(\.*).(rancardmobility).(.*?)/g)) {
                        return u.match(/(https)/g) ? u.replace(/\+/g, ' ') : 'https' + u.substring(4).replace(/\+/g, ' ');
                    }
                    else {
                        return u.replace(/\+/g, ' ');
                    }
                }
            }
        })
        .filter('trim', function () {
            return function (data) {
                if (data !== undefined) {
                    return data.toString().replace(/[\n\r]/g, '').replace(/&amp;/g, '&').trim();
                }
            }
        })
        .filter('ucfirst', function () {
            return function (w) {


                try {
                    var s = '',
                        i;
                    var m = w.match(/([0-9-a-z-A-Z-'\(\)\:\;\.\\s&]+)/g);
                    if (!!m && m.length) {
                        for (i = 0; i < m.length; i++) {
                            s += m[i].substring(0, 1) + m[i].substring(1).toLowerCase() + ' ';
                        }
                        return s;
                    }
                } catch (e) {
                    console.log('yawa oo: ', e.message);
                }
            }
        })

        .filter('range', function () {
            return function (start, end, step) {
                var range = [];
                var typeofStart = typeof start;
                var typeofEnd = typeof end;

                if (step === 0) {
                    throw TypeError("Step cannot be zero.");
                }

                if (typeofStart == "undefined" || typeofEnd == "undefined") {
                    throw TypeError("Must pass start and end arguments.");
                } else if (typeofStart != typeofEnd) {
                    throw TypeError("Start and end arguments must be of same type.");
                }

                typeof step == "undefined" && (step = 1);

                if (end < start) {
                    step = -step;
                }

                if (typeofStart == "number") {

                    while (step > 0 ? end >= start : end <= start) {
                        range.push(start);
                        start += step;
                    }

                } else if (typeofStart == "string") {

                    if (start.length != 1 || end.length != 1) {
                        throw TypeError("Only strings with one character are supported.");
                    }

                    start = start.charCodeAt(0);
                    end = end.charCodeAt(0);

                    while (step > 0 ? end >= start : end <= start) {
                        range.push(String.fromCharCode(start));
                        start += step;
                    }

                } else {
                    throw TypeError("Only string and number types are supported");
                }

                return range;
            }
        })

})();


