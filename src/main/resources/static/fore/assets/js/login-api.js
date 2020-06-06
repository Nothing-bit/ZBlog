/**
 * 检查是否已登录
 *
 */
function checkLogin() {
    var flag=1;
    window.$.ajax({
        url:'/oauth/check',
        type:'post',
        async:false,
        dataType:'json',
        success:function (result) {
            if(result.code==200){
                flag=1;
            }else{
                loginMenu()
                flag=0;
            }
        }
    })
    return flag;
}

/**
 * 登录面板
 */
function loginMenu() {
    layer.open({
        type: 1,
        skin: 'layui-layer-demo', //样式类名
        closeBtn: 1, //不显示关闭按钮
        anim: 1,
        title:'登录',
        shadeClose: true, //开启遮罩关闭
        area: ['500px', '300px'],
        content: $('#loginModal')
    });
}

/**
 * 登录按钮
 */
function  login(platform) {
    window.location.href="/oauth/login/"+platform+"?lastUrl="+window.location.pathname;
}
/**
 * 注销按钮
 */
function logout() {
    layui.jquery.ajax({
        url:'/oauth/logout',
        dataType:'json',
        type:'post',
        success:function (result) {
            if(result.code==200){
                layer.msg("注销成功！",{icon:6})
                setTimeout("window.location.reload()",3000)

            }else{
                layer.msg('注销失败！')
            }
        }
    })
}

/**
 * 回复某一用户
 * @param targetId
 */
function reply(username) {
    layer.open({
        type: 1,
        skin: 'layui-layer-demo', //样式类名
        closeBtn: 1,
        anim: 3,
        title:'回复：'+username,
        shadeClose: true, //开启遮罩关闭
        area: ['600px', '450px'],
        content: $('#replyModal')
    });

}