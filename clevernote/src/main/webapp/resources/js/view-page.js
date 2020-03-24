window.addEventListener("load",init,false);
    
function init(e){        
    // var listBtns = document.getElementsByClassName('listBtn');
    var listBtn = document.getElementById('listBtn');
    var deleteBtn = document.getElementById('deleteBtn');
    var replyBtn = document.getElementById('replyBtn');
    var modifyBtn = document.getElementById('modifyBtn');
    var deleteFileBtns = document.getElementsByClassName('delete-file-btn');        
    var saveBtn = document.getElementById("saveBtn");
    var readModeElements = document.getElementsByClassName("");        
    if(deleteBtn){
        deleteBtn.addEventListener("click",function(e){
            e.preventDefault();
            if(confirm("삭제하시겠습니까?")){
                         console.log("삭제"+e.target.getAttribute('data-bnum'));
                         var returnPage = e.target.getAttribute('data-returnPage');
                         var bnum = e.target.getAttribute('data-bnum');
                         location.href = "/board/delete/"+returnPage+"/"+bnum;          }	
        });
    }
    if(replyBtn){
        replyBtn.addEventListener("click",function(e){
            e.preventDefault();	
                    console.log("답글달기"+e.target.getAttribute('data-bnum')); 
                    var returnPage = e.target.getAttribute('data-returnPage');

                    var bnum = e.target.getAttribute('data-bnum');
// 					location.href = getContextPath()+"/board/replyForm/"+returnPage+"/"+bnum;
                    location.href = "/board/reply/page/"+returnPage+"/"+bnum;
        });            
    }
    if(modifyBtn){
        modifyBtn.addEventListener("click",function(e){
            if(confirm("수정하시겠습니까?")){
                console.log("수정"+e.target.getAttribute('data-bnum'));
                var returnPage = e.target.getAttribute('data-returnPage');
                var bnum = e.target.getAttribute('data-bnum');
                location.href = "/board/modify/"+bnum+"/"+returnPage;          
            }	
        });
    }
    if(listBtn){
        listBtn.addEventListener("click",function(e){
            e.preventDefault();
            console.log("목록");
                var returnPage = e.target.getAttribute('data-returnPage');
            location.href="/board/list/"+returnPage;	
        });
    }
   
    //  삭제
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
         // 목록 버튼 클릭시		
        //  console.log("목록 버튼 이벤트리스너 추가");
    // Array.prototype.forEach.call(listBtns, element => {
    //     console.log("목록 버튼 이벤트리스너 추가"+element);
    //         element.addEventListener("click",function(e){            		
    //             e.preventDefault();
    //             console.log("목록");
    //                 var returnPage = e.target.getAttribute('data-returnPage');
    //             location.href="/board/list/"+returnPage;
    //     });    
    //         }); 
            
}
function deleteFileItem(e){
        var list = document.querySelector('#file-list');
        console.log(e.target.responseText,e.target.readyState, e.target.status);    		
        if (e.target.readyState===4 && e.target.status == 200){
            var fid = e.target.responseText.split(':')[1];
            var deleteTarget = document.querySelector('.file-item[data-fid="'+fid+'"]');
            list.removeChild(deleteTarget);
        
       }
}