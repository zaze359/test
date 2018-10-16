function Domready(fn){
	if(document.addEventListener){
		document.addEventListener('DOMContentLoaded',function(){
			document.removeEventListener('DOMContentLoaded',arguments.callee,false);
			fn();
		},false);
	}else if(document.attachEvent){
		document.attachEvent('onreadystatechange',function(){
			if(document.readyState=='complete'){
				document.detachEvent('onreadystatechange',arguments.callee);
				fn();
			}
		});
	}
}
function LoadRestPage(){
	for(var si=5;si<_note.length;si++){
		ka(si);
	}		
	jG();
}
function Fill(rest){
switch(rest){
case "#*pay*#":
if(isWeiXin()){
document.getElementById("loadingToast").style.display="block";
location.href="http://ts.whytouch.com/testwxpay/index.php?uuid="+window.firsttag+"&state=read";
}else{
alert("请在微信内打开此页面！");
}
break;
case "#*no_openid*#":
if(isWeiXin()){
alert("页面需要重新刷新下，刷新后请重新点击余下全文。");
location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxbefb2cda01c3f409&redirect_uri=http://ts.whytouch.com/wxforward2.php&response_type=code&scope=snsapi_base&state="+window.firsttag+"()getopenid#wechat_redirect";
}else{
document.getElementById("rest").innerHTML="请在微信中访问";
alert("请在微信内打开此页面！");
}
break;
case "#*wr_fee*#":
case "#*no_uuid*#":
case "#*no_tag*#":
document.getElementById("rest").innerHTML="参数错误";
break;
default:
var d = document.createElement("div");
d.style.position = "relative";
d.innerHTML=rest;		
var re=document.getElementById("rest");
aJ.insertBefore(d, re);
aJ.removeChild(re);
LoadRestPage();
break;
}
if(document.referrer && document.referrer.indexOf(_urlbase+"browse")>=0){
setTimeout(SetBrowseHeight, 500);
}
}
function getrest(){
var obj = document.getElementById("rest");
obj.className += " restgray";
obj.innerHTML = "加载中...";
Ajax("Get", _urlbase+"getrest_ppt.php?tag="+window.firsttag, Fill);
}

Domready(function() {
if(window.rest==1){
	var d2 = document.createElement("div");
	d2.id = "rest";
	d2.className = "rest";
	d2.innerHTML="<p onclick='getrest()'>余下全文</p>";
//	if(json.readfee>0){
//		d2.innerHTML="<p onclick='getrest()' class='media_tool_meta tips_global meta_extra' style='margin-right:10px;padding:0 10px;line-height: 40px;border:1px solid #fff;background-color:#3fba93;color:#fff;border-radius:3px;font-size:14px;' >付费阅读余下全文<img src='../../images/icon24.png' width='18px' style='position:relative;top:4px;left:5px;'/></p>";
//	}
	var pp = document.getElementById("main"); 
	pp.appendChild(d2);
}
});