/**
 * 댓글 관련 
 */
const commentIdPrefix = "comment-num-";
const KEY_LAST_COMMENT_TIME = "data-lastcommenttime";
const KEY_POST_NUM = "data-postnum";
const COMMENT_COMPONENT_SELECTOR = 'section#comment';
const KEY_COMMENT_NUM = "data-commentnum"
const KEY_PARENT_NUM = "data-parentnum";
const KEY_USER_NUM = "data-usernum";
const KEY_USER_NAME = "data-username";

window.addEventListener("load",
(e)=>{    
    // 초기화
    initComments();
    // 댓글 등록 버튼
    
    clevernoteUtil.click('section#comment .comment-frm button.submit',(e)=>{
        filterUserLoggedIn(requestWritingComment);
        // requestWritingComment();
    });
    
    // 댓글 취소 버튼
    clevernoteUtil.click('section#comment .comment-frm button.cancel',(e)=>{ 
        const textarea = document.querySelector('section#comment .comment-frm textarea[name="content"]');
        textarea.value="";
    } );
    // 다음 댓글 불러오기
    clevernoteUtil.click('section#comment article.rest-comments button.refresh',(e)=>{
        const time = e.currentTarget.getAttribute(KEY_LAST_COMMENT_TIME);
        requestNextComments(time);
    });

    // list 클릭
    clevernoteUtil.click('section#comment .list .root-list', (e)=>{
        console.log(e.currentTarget, e.target);
        const clickedElement = e.target ;
        // 펼치기 버튼
        if( clickedElement.classList.contains('spread')){
            console.log("펼치기");
            const time = clickedElement.getAttribute(KEY_LAST_COMMENT_TIME);
            const parentNum = clickedElement.getAttribute(KEY_COMMENT_NUM);
            requestNextChildren(time,parentNum);
        }
        // 답글 버튼
        else if ( clickedElement.classList.contains('open-reply')){            
            // 로그인이 필요한 서비스
            const commentNum = clickedElement.getAttribute(KEY_COMMENT_NUM);            
            // filterUserLoggedIn
        }
        // 답글 달기 버튼
        else if( clickedElement.classList.contains('submit-reply')){            
            filterUserLoggedIn(requestWritingReplyComment,clickedElement);
            // requestWritingReplyComment(clickedElement);
        }
        else if( clickedElement.classList.contains('delete')){            
            filterUserLoggedIn(requsetDeleteComment,clickedElement);
            // 삭제 요청
        }
        else if(clickedElement.classList.contains('editor')){            
            // 편집기
            filterUserLoggedIn(openModifyingEditor,clickedElement);
        }
        else if(clickedElement.classList.contains('cancel')){
            // 취소
        }
        else if(clickedElement.classList.contains('modify')){
            // 수정하기
        }
    });
    // 로그인되지 않았을 경우
    if(!isLoggedIn()){
        const commentForm = document.querySelector('section#comment section.comment-frm textarea[name="content"]');
        commentForm.addEventListener('focus',e=>{
            if(confirmLogin()){
                moveToLoginForm();                
            }
            e.currentTarget.blur();// 이거 없으면 반복함...
        });
    
    }
    console.log("usernum:"+getLoggedInUserNum() );
    console.log("isLoggedIn:"+isLoggedIn() );    
});
// 수정 편집기 열기 
function openModifyingEditor(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const commentArticle = document.querySelector('#'+commentIdPrefix+commentNum);
    console.log('editor:',commentArticle);
    const editor = commentArticle.querySelector('textarea.modify-editor');
    const content = commentArticle.querySelector('p.content');
    console.log(editor, content)
    editor.value= content.innerText;
    editor.classList.add("active");
    content.classList.add("disply-none");
}


// 삭제 요청
function requsetDeleteComment(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const formData = new FormData(); 
    formData.append("commentNum",commentNum);
    
    console.log("commentNum:"+commentNum);
    requestJson("DELETE","/comment/"+getPostNum()+"/"+commentNum,(e)=>{
        console.log(e);
    });
    // 변경 이력 갱신 요청

}


// 내 댓글에 내 댓글 표시
function markAllMyComments(){
    if(isLoggedIn()){
        const sessionUserNum = getLoggedInUserNum();
        // 모든 댓글 순회
        // root댓글 순화
        const rootComments = document.querySelectorAll('article.root-comment');
        const childComments = document.querySelectorAll('article.child-comment');                        
        rootComments.forEach((element,num,parent)=>{            
            markUserNum(element,sessionUserNum,element.getAttribute(KEY_USER_NUM), element.getAttribute(KEY_COMMENT_NUM));
        });
        childComments.forEach((element,num,parent)=>{            
            markUserNum(element,sessionUserNum,element.getAttribute(KEY_USER_NUM), element.getAttribute(KEY_COMMENT_NUM));
        });
    }
    function markUserNum(element,  sessionUserNum , commentUserNum, commentNum ){
        console.log("댓글 검사", sessionUserNum, commentUserNum, commentNum)
        if(sessionUserNum === commentUserNum){
            // 내 댓글 표시
            element.classList.add('my-comment');
            // 추가 삭제 버튼 추가
            const header = element.querySelector('p.header');
            // 없을 경우
            if(!header.querySelector(".owner-tools")){
                const toolsDiv = document.createElement('div');
                toolsDiv.classList.add('owner-tools')
                toolsDiv.innerHTML=`<button class="editor" ${KEY_COMMENT_NUM}="${commentNum}">수정</button><button class="cancel" ${KEY_COMMENT_NUM}="${commentNum}">취소</button>
                <button class="modify" ${KEY_COMMENT_NUM}="${commentNum}">완료</button><button class="delete" ${KEY_COMMENT_NUM}="${commentNum}">삭제</button>`;
                header.appendChild(toolsDiv);
            }
        }
    }
}

// 로그인 여부 확인후 로그인화면으로 이동
function filterUserLoggedIn(callBackloggedIn, ...args){
    if(isLoggedIn()){        
        console.log(args);
        callBackloggedIn.apply(null,args);
    }else {
        if(confirmLogin()){
            moveToLoginForm();
        }
    }
}
// 로그인 화면으로 이동
function moveToLoginForm(){        
    location.href="/signin?next="+location.href.substring(location.origin.length,location.href.length);
}

// 로그인할 지 여부 묻기 
function confirmLogin(){
    return confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?");
}

// 로그인 했느지 확인
function isLoggedIn(){
    const userNum = getLoggedInUserNum();
    if(userNum && parseInt(userNum) && !isNaN(parseInt(userNum))){
        return true;
    }else {
        return false;
    }
}
// 로그인 유저 번호
function getLoggedInUserNum(){
    const commentSection = document.querySelector('section#comment');
    const userNum = commentSection.getAttribute(KEY_USER_NUM);
    return userNum;
}


// 댓글 초기화
function initComments(){
    // let postNum = document.querySelector('section#comment').getAttribute(KEY_POST_NUM);
    let postNum = getPostNum();
    requestJson("GET","/comment/"+postNum,(event)=>{        
        
        const responseMsg = event.currentTarget.response;
        let data = JSON.parse(responseMsg);
        console.log('data:',data);
        // 총 댓글 개수 갱신        
        updateTotalCommentCount('section#comment .total-count>.item-count',data.totalCommentCount);
        // 댓글 리스트 갱신
        updateRootCommentsList('section#comment section.list>ul',data.commentList,false);        
        // 남은 댓글 개수 갱신
        updateCount('section#comment article.rest-comments span.count',data.restCommentCount);
        // 마지막 댓글 시간 갱신
        updateLastCommentTime('section#comment article.rest-comments button.refresh',data.commentList);       
        // 투표한것 표시
        markAllMyComments();
    });
}



// 댓글 쓰기 요청
function requestWritingComment(){
    const formElement = document.querySelector('section#comment .comment-frm form');
    const formdata = new FormData(formElement);
    let postNum = getPostNum();
    requestJson("POST","/comment/"+postNum,(event)=>{
    var target  = document.querySelector('section#comment .comment-frm textarea[name="content"]');
        target.value="";// textarea 비우기
        // 댓글 추가적으로 요청하기 
        const lastTIme = getLastCommentTime('section#comment article.rest-comments button.refresh');
        requestNextComments(lastTIme);
    },formdata);  
}


// 답글 쓰기 요청
function requestWritingReplyComment(btnElement){    
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const contentTextarea =  document.querySelector('#'+commentIdPrefix+commentNum+' .reply-editor textarea.editor');
    const formData = new FormData();            
    const postNum = getPostNum();
    const parentNum = btnElement.getAttribute(KEY_PARENT_NUM);
    formData.append("content",contentTextarea.value);
    formData.append("parentNum",parentNum);
    formData.append("targetUsername",btnElement.getAttribute(KEY_USER_NAME));
    formData.append("targetCommentNum",btnElement.getAttribute(KEY_COMMENT_NUM)); 
    
    requestJson("POST","/comment/"+postNum,(event)=>{
        contentTextarea.value ="";
        const spreadBtn = document.querySelector('#'+commentIdPrefix+parentNum+' button.spread');
        // console.log('#'+commentIdPrefix+commentNum+' button.spread');
        const time = spreadBtn.getAttribute(KEY_LAST_COMMENT_TIME);
        // console.log("spreadBtn:",spreadBtn,"time:",time);
    },formData)
}


// postNum 가져오기 
function getPostNum(){
    let postNum = document.querySelector(COMMENT_COMPONENT_SELECTOR ).getAttribute(KEY_POST_NUM);
    return postNum;
}


// 다음 댓글 요청
function requestNextComments(time){
    let postNum = getPostNum();
    requestJson("GET","/comment/"+postNum+"/next?basetime="+time,(event)=>{
        const responseMsg = event.currentTarget.response;
        let data = JSON.parse(responseMsg);
        // 댓글 창 비우기 여부
        let appends = true;
        if(document.querySelector('section#comment .total-count>.item-count').innerText === '0')
            appends = false;

        // 총 댓글 개수 갱신        
        updateTotalCommentCount('section#comment .total-count>.item-count',data.totalCommentCount);
        // 댓글 리스트 갱신
        updateRootCommentsList('section#comment section.list>ul',data.commentList,appends);        
        // 남은 댓글 개수 갱신
        updateCount('section#comment article.rest-comments span.count',data.restCommentCount);
        // 마지막 댓글 시간 갱신
        updateLastCommentTime('section#comment article.rest-comments button.refresh',data.commentList);  
        // 내 댓글 표시
        markAllMyComments();
    });
}

// 다음 자식 댓글 요청
function requestNextChildren(time,parentNum){
    requestJson("GET","/comment/child/?basetime="+time+"&parentNum="+parentNum,(event)=>{
        const responseMsg = event.currentTarget.response;
        const data = JSON.parse(responseMsg);
        const commentSelector = '#'+commentIdPrefix+data.parentKey;
        const selectorPrefix = commentSelector +' ';
        const comment = document.querySelector(commentSelector);
        console.log("comment:"+comment);
        // 리스트 갱신
        updatedChildCommentsList(selectorPrefix+'ul.children-list',data.commentList,true,parentNum);
        // 남은 댓글 수 
        updateCount(selectorPrefix+'span.rest-count',data.restCommentCount);
        // 마지막 댓글 시간 
        updateLastCommentTime(selectorPrefix+'button.spread',data.commentList);
        // 내 댓글 표시
        markAllMyComments();
    });
}

// 총 댓글 개수 갱신        
function updateTotalCommentCount(targetSelector,count){
    const countOfTitle = document.querySelector(targetSelector);
    countOfTitle.innerText = count;
}
// 루트 댓글 리스트 갱신
function updateRootCommentsList(targetSelector,listData,appends){
    const commentList = document.querySelector(targetSelector);
    if(listData && listData.length>0){
        if(!appends)
            commentList.innerHTML="";
        listData.forEach(element => {
            commentList.appendChild(makeNewRootComment(element));    
        });
    } 
}
// 자식 댓글 리스트 갱신
function updatedChildCommentsList(targetSelector,listData,appends,parentNum){
    const commentList = document.querySelector(targetSelector);
    if(listData && listData.length>0){
        if(!appends)
            commentList.innerHTML="";
        listData.forEach(element => {
            commentList.appendChild(makeNewChildComment(element,parentNum));    
        });
    } 
}


// 남은 댓글 수 갱신 
function updateCount(targetSelector,count,listData){
    const restCount = document.querySelector(targetSelector);
    restCount.innerText = count;   
}


// 마지막 댓글 시간  갱신
function updateLastCommentTime(targetSelector,listData){
    if(listData && listData.length>0){
        const refreshBtn = document.querySelector(targetSelector);
        refreshBtn.setAttribute(KEY_LAST_COMMENT_TIME, listData[listData.length-1].createdAt);
    }
}

// 마지막 댓글 시간 가져오기
function getLastCommentTime(targetSelector){
    const refreshBtn = document.querySelector(targetSelector);
    return refreshBtn.getAttribute(KEY_LAST_COMMENT_TIME);
}


// 루트 댓글 만들기
function makeNewRootComment(data){
    const li = document.createElement('li');    
    li.classList.add('comment-item');    
    if(data){
        li.innerHTML = `
            <article id="${commentIdPrefix}${data.commentNum}" class="comment root-comment" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NUM}="${data.userNum}" ${KEY_POST_NUM}="${data.postNum}">
                <p class="header">
                    <span class="username">${data.username}</span>
                    <span class="created-at"><small class="time">${new Date(data.createdAt)} ${data.updatedAt!=data.createdAt ? '(수정됨)':''}</small>에 작성됨</span>
                    <button class="open-reply" ${KEY_COMMENT_NUM}="${data.commentNum}" >답글</button>
                </p>
                <p class="content">${data.content}</p><textarea class="modify-editor"></textarea>
                ${getLikeTag(data)}
                ${getReplyEditorTag(data,data.commentNum)}
            </article>                        
        `;
        if(data.childrenCount) {                          
            const article = li.querySelector('article');
            appendList(article,data);
        }
    }    
    return li;
}
// like 양식
function getLikeTag(data){
    return `<div class="preference">
        <span cliss="like">좋아요<span cliss="count">${data.like}</span></span>
        <span cliss="dislike">싫어요<span cliss="count">${data.dislike}</span></span>
    </div>`;
}
// 답글 양식 
function getReplyEditorTag(data,parentNum){
    // return `<div class="reply-editor"><textarea class="editor"></textarea><button>취소</button><button class="submit-reply" ${KEY_PARENT_NUM}="${parentNum}" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NAME}="${data.username}" ${KEY_POST_NUM}="${data.postNum}" >제출</button></div>`
    return `<div class="reply-editor"><textarea class="editor"></textarea><button>취소</button><button class="submit-reply" ${KEY_PARENT_NUM}="${parentNum}" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NAME}="${data.username}"  >제출</button></div>`
}

// 루트 댓글에 list붙이기
function appendList(rootCommentElement,data){
    const section = document.createElement('section');    
    rootCommentElement.appendChild(section);
    section.outerHTML = `<section class="children-list">
    <ul class="children-list"></ul>
    <p><span class="rest-count">${data.childrenCount}</span>개의 답글이 더 있습니다.<button class="spread" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_LAST_COMMENT_TIME}="${data.createdAt}">가져오기</button></p>    
    </section>`;
}

// 자식 댓글 만들기
function makeNewChildComment(data,parentNum){
    const li = document.createElement('li');    
    li.classList.add('comment-item');    
    if(data){
        li.innerHTML = `
                <article id="${commentIdPrefix}${data.commentNum}" class="comment child-comment" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NUM}="${data.userNum}" ${KEY_POST_NUM}="${data.postNum}">
                <p class="header">
                    <span class="username">${data.username}</span>
                    <span class="created-at"><small class="time">${new Date(data.createdAt)} ${data.updatedAt!=data.createdAt ? '(수정됨)':''}</small>에 작성됨</span>
                    <button class="reply" ${KEY_COMMENT_NUM}="${data.commentNum}" >답글</button>
                </p>
                <a href="#${commentIdPrefix}${data.targetNum}">@${data.targetUsername}&nbsp;</a>
                <p class="content">${data.content}</p>
                <textarea class="modify-editor"></textarea>
                ${getLikeTag(data)}
                ${getReplyEditorTag(data,parentNum)}     
            </article>                        
        `;
    }
    return li;
}