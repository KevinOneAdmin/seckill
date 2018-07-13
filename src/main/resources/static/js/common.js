function g_showLoading(){
    return layer.msg('处理中...',{icon:16,shade:[0.5,'#f5f5f5'],scrollbar:false,offset:'0px',time:10000})
}

var g_password_salt="c014fe74550d4b4fa78170db3c8c177f";

function md5To(inputPass) {
    var end = g_password_salt.length;
    var start = 0;
    var one = "";
    var two = "";
    while (end > 0 && start < g_password_salt.length) {
        end -= 3;
        start += 3;
        if (end > 0 && start < g_password_salt.length) {
            one += g_password_salt.charAt(end);
            two += g_password_salt.charAt(start);
        }
    }

    var rel = one + inputPass + two;
    return md5(rel);
}