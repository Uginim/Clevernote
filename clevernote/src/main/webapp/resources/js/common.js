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

//로그인 화면으로 이동
function moveToLoginForm(){        
    location.href="/signin?next="+location.href.substring(location.origin.length,location.href.length);
}
//로그아웃
function doUserlogOut(){        
	location.href="/signout?next="+location.href.substring(location.origin.length,location.href.length);
}

// ajax 
function requestJson(method,uri, callback, sendData, contentType, arguments){
	// var targetElement = document.querySelector(targetSelector);
	var xhr = new XMLHttpRequest();	
	xhr.addEventListener('load',event=>{
		newArguments = [event];
		callback.apply(null,newArguments.concat(arguments));
	});
	xhr.open(method,uri);
	if(contentType){
		xhr.setRequestHeader("Content-Type", contentType);
	}
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
window.addEventListener('load',previewAttachments);

function previewAttachments (){
	var attachments = document.getElementById('attachments');
	if(attachments){
		
		attachments.addEventListener("change",(e)=>{
			console.log(e);
			console.log(e.currentTarget.files);
			
			let files = e.currentTarget.files;
			fileList = document.getElementById('fileList');
			if(fileList && files ){
				fileList.innerHTML ="";			
				for (let i = 0; i < files.length; i++) {
					const file = files[i];
					const li = document.createElement("li");
					li.classList.add('list-group-item');
					const row = document.createElement("div");
					li.classList.add('py-2');
					row.classList.add('justify-content-between');
					row.classList.add('row');
					row.classList.add('lign-items-center');
					li.appendChild(row);
					fileList.appendChild(li);
					if (!file.type.startsWith('image/')){ continue }
					const img = document.createElement("img");
					img.src = URL.createObjectURL(files[i]);
					img.height = 60;
					img.onload = function() {
						URL.revokeObjectURL(this.src);
					}
					const info = document.createElement("span");
					info.innerHTML = files[i].name + ": " + files[i].size + " bytes";
					info.classList.add('align-middle');
					row.appendChild(info);
					row.appendChild(img);
//			    const img = document.createElement("img");
//			    img.classList.add("obj");
//			    img.file = file;
//			    preview.appendChild(img); // Assuming that "preview" is the div output where the content will be displayed.
//			    
//			    const reader = new FileReader();
//			    reader.onload = (function(aImg) { return function(e) { aImg.src = e.target.result; }; })(img);
//			    reader.readAsDataURL(file);
				}
				fileList.appendChild();
			}
		});
	}
}

 window.addEventListener('load',initSigninBtn);
 function initSigninBtn(){
	 if(document.getElementById("global-signin-btn")){
		 document.getElementById("global-signin-btn").addEventListener('click',moveToLoginForm);
	 }	 
	 if(document.getElementById("global-signout-btn")){
		 document.getElementById("global-signout-btn").addEventListener('click',doUserlogOut);
	 }
 }




var editor;
// window.addEventListener('load',initSunEditor);
function initSunEditor (){
	if(document.getElementById('post-content-editor')) {
		editor = SUNEDITOR.create((document.getElementById('post-content-editor') || 'post-content-editor'),{
			// All of the plugins are loaded in the "window.SUNEDITOR" object in dist/suneditor.min.js file
			// Insert options
			// Language global object (default: en) 
//			 mode: 'inline',
//			 height : 'auto',
			 height : '720px',
//			minHeight : "480px",
//			maxHeight : "720px",
//			lineHeights:{text:'1',value:70},
			lang: SUNEDITOR_LANG['ko']
		});
		document.getElementById('post-content-editor');
	}
	
}