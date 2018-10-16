if(typeof(has_aud) != 'undefined'){
setTimeout(playaud, 2000);
function musicInWeixinHandler() {
	playaud(true);
	document.addEventListener("WeixinJSBridgeReady", function () {
		playaud(true);
	}, false);
    document.removeEventListener('DOMContentLoaded', musicInWeixinHandler);
}
document.addEventListener('DOMContentLoaded', musicInWeixinHandler);
function playaud(){
	var audioEl=document.getElementById("aud").firstChild;
	audioEl.src="aud.mp3";
	
	if (audioEl.paused) {
		audioEl.load(); // iOS 9
		audioEl.play();
    }
}
}