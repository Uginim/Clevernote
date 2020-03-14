/**
 * 공용 자바 스크립트 
 */
// 컨텍스트 루트 경로 읽어오기
var g_contextPath = true;
function getContextPath() {
	if(g_contextPath){
		let idx = location.href.indexOf(location.host)+location.host.length;
		return location.href.substring(idx,location.href.indexOf('/',idx+1));		
	}else {
		return location.host;
	}
}

// ajax 
function requestJson(method,uri, callback, sendData){
	// var targetElement = document.querySelector(targetSelector);
	var xhr = new XMLHttpRequest();	
	xhr.addEventListener('load',event=>{
		callback(event);		
	});
	xhr.open(method,uri);
	if(sendData)
		xhr.send(sendData);
	else 
		xhr.send();
}

var clevernoteUtil ={
	"click": function(selector,callback){
		const button = document.querySelector(selector);
		button.addEventListener('click',(e)=>{
			callback(e);
		});
	}

}