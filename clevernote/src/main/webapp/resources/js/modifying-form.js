window.addEventListener("load",init,false);
    
    function init(e){


        
        var listBtn = document.getElementById('list-btn');
        var cancelBtn = document.getElementById('cancel-btn');
        var submitBtn = document.getElementById('submit-btn');
		var modifyForm = document.getElementById('modify-form');        
		var deleteFileBtns = document.getElementsByClassName('delete-file-btn');
     	// 등록 버튼 클릭시
        submitBtn.addEventListener("click", function(e) {
            e.preventDefault();

            modifyForm.submit();
            
		},false);	
		// 취소 버튼 클릭시        
        cancelBtn.addEventListener("click", function(e) {
				e.preventDefault();
				console.log("취소");
				var returnPage = e.target.getAttribute('data-returnPage');
				var bnum = e.target.getAttribute('data-bnum');
				
				location.href="/board/view/"+returnPage+"/"+bnum ;
		},false);		
        
		// 목록 버튼 클릭시		
		listBtn.addEventListener("click",function(e){
			e.preventDefault();
			console.log("목록");
				var returnPage = e.target.getAttribute('data-returnPage');
			location.href="/board/list/"+returnPage;
		},false);  


		// 첨부파일 삭제
		// 삭제
        console.log("deleteFileBtns",deleteFileBtns);
        Array.prototype.forEach.call(deleteFileBtns, element => {
        		element.addEventListener("click",function(e){
            		var fid = e.target.getAttribute('data-fid');
            		var xhr = new XMLHttpRequest();
            		xhr.addEventListener("load",deleteFileItem);
            		xhr.open("DELETE","/board/file/"+fid,true);
            		xhr.send();            		
            });    
		}); 			
    }
    function deleteFileItem(e){
		//var deleteFileBtns = document.getElementsByClassName('file-item');
		var list = document.querySelector('#file-list');
		console.log(e.target.responseText,e.target.readyState, e.target.status);    		
		if (e.target.readyState===4 && e.target.status == 200){
    		var fid = e.target.responseText;
    		var deleteTarget = document.querySelector('.file-item[data-fid="'+fid+'"]');
    		list.removeChild(deleteTarget);
	        
	   	}
	}