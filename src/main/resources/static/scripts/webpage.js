/**
 * Created by ASUS on 2017/7/6.
 */
jQuery(document).ready(function($) {
    $('.theme-login').click(function(){
        $('.theme-popover-mask').fadeIn(100);
        $('.theme-popover').slideDown(200);
    })
    $('.theme-poptit .close').click(function(){
        $('.theme-popover-mask').fadeOut(100);
        $('.theme-popover').slideUp(200);
    })

})
function REG(){
    loginform.action = "/reg"
}
function LOG() {
    loginform.action = "/log"
}
function changeSuccess() {
     alert("修改成功，请返回首页");

}