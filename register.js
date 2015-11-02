var get_params = function(search_string) {
    
    var parse = function(params, pairs) {
        var pair = pairs[0];
        var parts = pair.split('=');
        var key = decodeURIComponent(parts[0]);
        var value = decodeURIComponent(parts.slice(1).join('='));
        
        // Handle multiple parameters of the same name
        if (typeof params[key] == "undefined") {
            params[key] = value;
        } else {
            params[key] = [].concat(params[key], value);
        }
        
        return pairs.length == 1 ? params : parse(params, pairs.slice(1))
    }
    
    // Get rid of leading ?
    return search_string.length == 0 ? {} : parse({}, search_string.substr(1).split('&'));
}

var params = get_params(location.search);

/**
 * @function _guid
 * @description Creates GUID for user based on several different browser variables
 * It will never be RFC4122 compliant but it is robust
 * @returns {Number}
 * @private
 */
var guid = function() {
    
    var nav = window.navigator;
    var screen = window.screen;
    var guid = nav.mimeTypes.length;
    guid += nav.userAgent.replace(/\D+/g, '');
    guid += nav.plugins.length;
    guid += screen.height || '';
    guid += screen.width || '';
    guid += screen.pixelDepth || '';
    
    return guid;
};

function check(form) {
    
    {
        var uuid = guid();
        var psw = form.pswrd.value;
        var encrypted_ = getPsw_(psw);
        var userid = form.user.value;
        var voucher = params['voucher_'];
        
        if (form.user.value != "" && form.pswrd.value == decrypt(encrypted_).toString(CryptoJS.enc.Utf8) )//&& voucher != undefined)
            
        {
            var Password = decrypt(encrypted_);
            
            post("https://localhost/login/register", {
                 
                 pswrd: getPsw(Password),
                 user: userid,
                 voucher_: params['voucher_'],
                 deviceId: uuid
                 
                 });
            
        } else
            
        {
            alert("Customer not found")
            
        }
    }
    
    
    var key = "";
    
    function getPsw(encrypted_) {
        
        var hash = CryptoJS.SHA3(encrypted_, {
                                 outputLength: 512
                                 });
        return hash;
    }
    
    function getPsw_(psw) {
        var hex = "0123456789abcdefghijklmnopqrstuvwxyz";
        
        for (i = 0; i < 256; i++) {
            key += hex.charAt(Math.floor(Math.random() * 16));
        }
        
        var encrypted = CryptoJS.AES.encrypt(psw, key);
        return encrypted;
    }
    
    function decrypt(encrypted_) {
        var decrypted = CryptoJS.AES.decrypt(encrypted_, key);
        
        return decrypted;
    }
    
    function post(path, params, method) {
        method = method || "post"; // Set method to post by default if not specified.
        
        // The rest of this code assumes you are not using a library.
        // It can be made less wordy if you use one.
        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", path);
        
        for (var key in params) {
            if (params.hasOwnProperty(key)) {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);
                
                form.appendChild(hiddenField);
            }
        }
        
        document.body.appendChild(form);
        form.submit();
    }
    
    function getParameterValues() {
        // ...
        var anchor = '';
        for (var i = 0; i < index + 1; i++) {
            var keyName = '#parameter-' + i + '-key';
            var valueName = '#parameter-' + i + '-value';
            var key = $(keyName).val();
            var value = $(valueName).val();
            if (typeof key == 'undefined' || typeof value == 'undefined') {
                continue;
            }
            
            if (key == '') {
                continue;
            }
            
            // ...
            
            if (anchor.length == 0) {
                anchor += '?' + key + '=' + value;
            } else {
                anchor += '&' + key + '=' + value;
            }
        }
        window.location.hash = '#' + anchor;
        return values;
    }
    
    
    
    
}