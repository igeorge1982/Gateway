function check(form) {
    
    {
        var voucher_ = form.voucher.value;
        
        if (form.voucher.value != "")
            
        {
            
            post("https://localhost/login/voucher", {voucher: voucher_});

            
        } else
            
        {
            alert("Error voucher")
            
        }
    }
    
    
    function post(path, params, method) {
        method = method || "post"; // Set method to post by default if not specified.
        
        // The rest of this code assumes you are not using a library.
        // It can be made less wordy if you use one.
        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", path);
        
        for(var key in params) {
            if(params.hasOwnProperty(key)) {
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
    
    function formSubmit()
    {
        document.getElementById("pswrd").submit();
    }
    
}