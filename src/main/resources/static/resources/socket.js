
//在index.html
//下次再发ajax请求把token带到后台
var token = $.cookie('TOKEN');
//全局的ajax的前置拦截
$.ajaxSetup({
    headers:{
        'TOKEN':token
    }
});
   