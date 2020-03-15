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
const KEY_PREPERENCE_MARK = "mark";
const KEY_POST_TIME= "data-posttime";
const CSS_CLASS_ACTIVE = "active";
const CSS_CLASS_NONE = "display-none";
var lastHistoryUpdatedTime;
var updatingHistoryInterval;
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
        if ( clickedElement.classList.contains('open-reply')){// 답글 편집기                         
            filterUserLoggedIn(openReplyEditor,clickedElement);// 로그인이 필요한 서비스            
        }else if( clickedElement.classList.contains('submit-reply')){// 답글 달기 버튼
            filterUserLoggedIn(requestWritingReplyComment,clickedElement);
        }else if( clickedElement.classList.contains('reply-cancel')){ // 답글 취소
            cancelReplyEditor(clickedElement);
        } 
 
        // 댓글 주인 관련 
        if( clickedElement.classList.contains('delete')){            // 삭제 요청
            filterUserLoggedIn(requsetDeleteComment,clickedElement);
        }else if(clickedElement.classList.contains('open-editor') ){ // 편집기
            filterUserLoggedIn(openModifyingEditor,clickedElement);
        }else if(clickedElement.classList.contains('cancel')){ // 취소
            filterUserLoggedIn(cancelModifying,clickedElement);            
        }else if(clickedElement.classList.contains('modify')){ // 수정하기
            filterUserLoggedIn(requestModifying,clickedElement);
        }
 
        // 선호 투표
        if(clickedElement.classList.contains('like')){ // 좋아요                        
            filterUserLoggedIn(requestVote,clickedElement,'l');
        } else if(clickedElement.classList.contains('dislike')){ // 싫어요                        
            filterUserLoggedIn(requestVote,clickedElement,'dislike');
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
// 댓글 article 요소 가져오기 
function getCommentArticle(commentNum){
    const commentArticle = document.querySelector('#'+commentIdPrefix+commentNum);
    return commentArticle;
}

// 답글 버튼 클릭 시
function openReplyEditor(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const commentArticle = getCommentArticle(commentNum);
    console.log('reply-editor:',commentArticle);
    const editor = commentArticle.querySelector('.reply-editor');    
    editor.classList.add(CSS_CLASS_ACTIVE);    
}
// 답글 취소 클릭 시
function cancelReplyEditor(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const commentArticle = getCommentArticle(commentNum);
    console.log('reply-editor-cancle:',commentArticle);
    const editor = commentArticle.querySelector('.reply-editor');
    editor.querySelector('.editor').value= "";
    editor.classList.remove(CSS_CLASS_ACTIVE);    
}


// 선호도 버튼 클릭 시
function requestVote(btnElement,type){
    console.log("선호도",type);
    
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const formdata = new FormData();
    formdata.append('commentNum',commentNum);
    formdata.append('type',type[0].toUpperCase());
    if(!btnElement.classList.contains(KEY_PREPERENCE_MARK)){
        requestJson("POST","/comment/vote",(e)=>{
            console.log("msg",e.currentTarget.response);
            markPreperence(commentNum,type);
            console.log('버튼 클릭');
        },formdata);  
    }else {
        requestJson("DELETE","/comment/vote/"+commentNum,(e)=>{
            console.log("msg",e.currentTarget.response);
            markPreperence(commentNum,type);
            console.log('버튼 클릭');
        });        
    }
}

// 내 모든 선호도 댓글 표시
function markAllMyPreperences(datas){
    console.log("markAllMyPreperences(datas)");
    if(datas){
        datas.forEach((data,num,listarray)=>{
            console.log("data.commentNum,data.type",data.commentNum,data.type);
            markPreperence(data.commentNum,data.type);
        });
    }
}
// 선호도 표시 
function markPreperence(commentNum,type){    
    const commentArticle = getCommentArticle(commentNum);
    const preference = commentArticle.querySelector('.preference')
    if(type[0].toUpperCase() ==='L'){
        preference.querySelector('.like').classList.add(KEY_PREPERENCE_MARK);
        preference.querySelector('.dislike').classList.remove(KEY_PREPERENCE_MARK);
    }else if(type[0].toUpperCase() ==='D') {
        preference.querySelector('.like').classList.remove(KEY_PREPERENCE_MARK);
        preference.querySelector('.dislike').classList.add(KEY_PREPERENCE_MARK);
    }
}


// 수정 편집기 열기 
function openModifyingEditor(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const commentArticle = getCommentArticle(commentNum);
    console.log('editor:',commentArticle);
    const editor = commentArticle.querySelector('textarea.modify-editor');
    const content = commentArticle.querySelector('p.content');
    
    // 버튼 
    const openEditorBtn = commentArticle.querySelector('button.open-editor');
    const cancelBtn = commentArticle.querySelector('button.cancel');
    const modifyBtn = commentArticle.querySelector('button.modify');
    openEditorBtn.classList.add(CSS_CLASS_NONE);
    cancelBtn.classList.add(CSS_CLASS_ACTIVE);
    modifyBtn.classList.add(CSS_CLASS_ACTIVE);

    console.log(editor, content)

    editor.value= content.innerText;
    editor.classList.add(CSS_CLASS_ACTIVE);
    content.classList.add(CSS_CLASS_NONE);
}
// 수정 취소
function cancelModifying(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const commentArticle = getCommentArticle(commentNum);
    const editor = commentArticle.querySelector('textarea.modify-editor');
    const content = commentArticle.querySelector('p.content');    
    editor.value= "";
    editor.classList.remove(CSS_CLASS_ACTIVE);
    content.classList.remove(CSS_CLASS_NONE);

     // 버튼 
     const openEditorBtn = commentArticle.querySelector('button.open-editor');
     const cancelBtn = commentArticle.querySelector('button.cancel');
     const modifyBtn = commentArticle.querySelector('button.modify');
     openEditorBtn.classList.remove(CSS_CLASS_NONE);
     cancelBtn.classList.remove(CSS_CLASS_ACTIVE);
     modifyBtn.classList.remove(CSS_CLASS_ACTIVE);
}

// 수정 완료 동작
function requestModifying(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const commentArticle = getCommentArticle(commentNum);
    // const postNum = getPostNum();
    const editor = commentArticle.querySelector('textarea.modify-editor');
    const content = commentArticle.querySelector('p.content');    
    const sendData = {
        commentNum : commentNum,
        content: editor.value
    }
    let strSendData = JSON.stringify(sendData); // 꼭 string으로 보내야 받더라...
    // 수정 요청
    requestJson("PUT","/comment/one", (event)=>{
        // 현재 댓글 재갱신
        if(event.currentTarget.response){
            console.log(event.currentTarget.response);
            const responseJson = JSON.parse(event.currentTarget.response);
            content.innerText = editor.value;
            // 초기화 동작(수정 취소 시와 동일)
            cancelModifying(btnElement);
        }

    }, strSendData,"application/json");
    
}

// 삭제 요청
function requsetDeleteComment(btnElement){
    const commentNum = btnElement.getAttribute(KEY_COMMENT_NUM);
    const formData = new FormData(); 
    formData.append("commentNum",commentNum);
    
    console.log("commentNum:"+commentNum);
    requestJson("DELETE","/comment/one/"+commentNum,(e)=>{
        
        const target = getCommentArticle(commentNum);
        console.log("삭제 대상:" ,target);
        removeComment(target);
    });
}


// 내 댓글에 표시
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
                toolsDiv.classList.add('btn-group')
                toolsDiv.innerHTML=`<button class="btn btn-outline-primary open-editor " ${KEY_COMMENT_NUM}="${commentNum}">수정</button><button class="btn btn-outline-primary cancel hiding-btn" ${KEY_COMMENT_NUM}="${commentNum}">취소</button>
                <button class="btn btn-outline-primary modify hiding-btn" ${KEY_COMMENT_NUM}="${commentNum}">완료</button><button class="btn btn-outline-primary delete" ${KEY_COMMENT_NUM}="${commentNum}">삭제</button>`;
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
    lastHistoryUpdatedTime = (new Date()).getTime();
    requestJson("GET","/comment/"+postNum,(event)=>{        
        
        const response = event.currentTarget.response;
        let data = JSON.parse(response);
        console.log('data:',data);
        // 총 댓글 개수 갱신        
        updateTotalCommentCount('section#comment .total-count>.item-count',data.totalCommentCount);
        // 댓글 리스트 갱신
        updateRootCommentsList('section#comment section.list>ul',data.commentList,false);        
        // 남은 댓글 개수 갱신
        updateCount('section#comment article.rest-comments span.count',data.restCommentCount);
        // 마지막 댓글 시간 갱신
        updateLastCommentTime('section#comment article.rest-comments button.refresh',data.commentList);       
        if(isLoggedIn()){
            console.log("로그인 했을 때의 초기화 동작");
            // 내 댓글 표시
            markAllMyComments();
            // 투표한 것 표시
            markAllMyPreperences(data.voteList);
        }
        lastHistoryUpdatedTime = Math.max( lastHistoryUpdatedTime, data.lastCommentTime);// 댓글 업데이트 시점으로 초기화
        updatingHistoryInterval = setInterval((e)=>{
            console.log('interval 동작');
            requestChangeHistory(lastHistoryUpdatedTime,updateHistory);
        // },3*60*1000);
        },10*1000);
    });
}



// 댓글 쓰기 요청
function requestWritingComment(){
    const formElement = document.querySelector('section#comment .comment-frm form');
    const formdata = new FormData(formElement);
    let postNum = getPostNum();
    requestJson("POST","/comment/one/"+postNum,(event)=>{
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
    
    requestJson("POST","/comment/one/"+postNum,(event)=>{
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
// 게시글 시간 가져오기 
function getPostTime(){
    let postTime = document.querySelector(COMMENT_COMPONENT_SELECTOR ).getAttribute(KEY_POST_TIME);
    return postTime;
}



// 다음 댓글 요청
function requestNextComments(time){
    let postNum = getPostNum();
    requestJson("GET","/comment/"+postNum+"/next?basetime="+time,(event)=>{
        const response = event.currentTarget.response;
        let data = JSON.parse(response);
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
        if(isLoggedIn()){
            // 내 댓글 표시
            markAllMyComments();
            // 투표한 것 표시
            markAllMyPreperences(data.voteList);
        }
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
        // 총 댓수 갱신
        // 리스트 갱신
        updatedChildCommentsList(selectorPrefix+'ul.children-list',data.commentList,true,parentNum);
        // 남은 댓글 수 
        updateCount(selectorPrefix+'span.rest-count',data.restCommentCount);
        // 마지막 댓글 시간 
        updateLastCommentTime(selectorPrefix+'button.spread',data.commentList);
        if(isLoggedIn()){
            // 내 댓글 표시
            markAllMyComments();
            // 투표한 것 표시
            markAllMyPreperences(data.voteList);
        }
    });
}



// 변경이력 요청
function requestChangeHistory(lastUpdatedTime, func){
    const postNum = getPostNum();    
    console.log(lastHistoryUpdatedTime);
    requestJson("GET",`/comment/history/${postNum}?basetime=`+lastUpdatedTime,(e)=>{
        console.log("requestChangeHistory:",e.currentTarget.response);
        const responseData = JSON.parse(e.currentTarget.response);
        if(func)
            func(responseData);
    });
}
// 갱신하기 
function updateHistory(datas){
    // 마지막 갱신 시간 
    const {historyList,lastHistoryTime} = datas;
    lastHistoryUpdatedTime = Math.max(lastHistoryTime,lastHistoryUpdatedTime);        
    historyList.forEach((value)=>{
        value.type = value.type.trim();// 공백 삭제.. 젠장
        const {postNum, historyNum, commentNum, createdAt, type} = value;
        console.log('postNum,historyNum,commentNum,createdAt,type',postNum,historyNum,commentNum,createdAt,type);
        const commentArticle = getCommentArticle(commentNum);
        if(type==='D'){ // 루트 삭제
            console.log("root 댓글 삭제 ",commentArticle)
            if(removeComment(commentArticle)){
                addCount('section#comment .total-count>.item-count',-1); // 총 개수
                addCount('section#comment article.rest-comments span.count',-1);
            }
        }else if(type==='CD'){ // 자식 삭제
            console.log("자식 삭제됨 ",commentArticle);            
            if(removeComment(commentArticle)){
                addCount('section#comment .total-count>.item-count',-1); // 총 개수
                addCount('section.children-list span.rest-count',-1,commentArticle); // 남은 자식 개수
            }
            // 삭제됨 메시지
        }else if(type==='I') { // 루트 생성됨
            console.log("root 댓글 생성 ",commentArticle)
            // 총댓 수 
            // const totalConnt = document.querySelector('section#comment .total-count>.item-count',data.totalCommentCount);
            addCount('section#comment .total-count>.item-count',1); // 총 개수            
            addCount('section#comment article.rest-comments span.count',1);
        }else if(type==='CI'){ // 자식 생성됨
            console.log("자식 댓글 생성 ",commentArticle)
            addCount('section#comment .total-count>.item-count',1); // 총 개수
            addCount('section.children-list span.rest-count',1,commentArticle); // 남은 자식 개수
        }else if(type==='U') { // 수정됨

        }
    });    
}

// count 수정
function addCount(selector,count,target){
    target = target || document;
    count = count || 1;
    const countElement = target.querySelector(selector);
    console.log("addCount",selector,count,target);
    if(countElement&&countElement.innerText && !isNaN(parseInt(countElement.innerText)) ){
        countElement.innerText = parseInt(countElement.innerText) +count;
    }else {
        countElement.innerText = count
    }
}

// 댓글 삭제
// 삭제 시 true반환
function removeComment(commentArticle){
    console.log("곧 삭제될 코멘트",commentArticle);
    if(commentArticle){        
        
        li = commentArticle.parentElement;
        console.log("before removing li:",li,li.parentElement);
        parent = li.parentElement;
        parent.removeChild(li);
        console.log("after removing li:",li,parent);
        return true;
    }
    return false;
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
            <article id="${commentIdPrefix}${data.commentNum}" class="comment root-comment shadow-sm p-3 mb-5 bg-white rounded" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NUM}="${data.userNum}" ${KEY_POST_NUM}="${data.postNum}">
                <p class="header">
                    <span class="username">${data.username}</span>
                    <span class="created-at"><small class="time text-muted">${new Date(data.createdAt)} ${data.updatedAt!=data.createdAt ? '(수정됨)':''}</small>에 작성됨</span>
                    <button class="btn btn-link open-reply" ${KEY_COMMENT_NUM}="${data.commentNum}" >답글</button>
                </p>
                <p class="content bg-light p-3">${data.content}</p><textarea class="form-control modify-editor"></textarea>
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
    return `<div class="preference mb-4">
        <span class="btn btn-light btn-sm like" ${KEY_COMMENT_NUM}="${data.commentNum}">좋아요<span class="badge badge-dark count">${data.like}</span></span>
        <span class="btn btn-light btn-sm dislike" ${KEY_COMMENT_NUM}="${data.commentNum}">싫어요<span class="badge badge-dark count">${data.dislike}</span></span>
    </div>`;
}
// 답글 양식 
function getReplyEditorTag(data,parentNum){
    // return `<div class="reply-editor"><textarea class="editor"></textarea><button>취소</button><button class="submit-reply" ${KEY_PARENT_NUM}="${parentNum}" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NAME}="${data.username}" ${KEY_POST_NUM}="${data.postNum}" >제출</button></div>`
    return `<div class="reply-editor container-fluid"><div class="row mb-1">
        <textarea class="form-control editor"></textarea>
    </div><div class="row justify-content-end">
        <div class="btn-group">
            <button class="btn btn-dark reply-cancel" ${KEY_COMMENT_NUM}="${data.commentNum}">취소</button><button class="btn btn-dark submit-reply" ${KEY_PARENT_NUM}="${parentNum}" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NAME}="${data.username}"  >제출</button>
        </div>
    </div></div>`
}

// 루트 댓글에 list붙이기
function appendList(rootCommentElement,data){
    const section = document.createElement('section');    
    rootCommentElement.appendChild(section);
    section.outerHTML = `<section class="children-list">
    <ul class="children-list list-unstyled pl-4 pr-3"></ul>
    <p><span class="rest-count">${data.childrenCount}</span>개의 답글이 더 있습니다.<button class="btn btn-link spread" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_LAST_COMMENT_TIME}="${data.createdAt}">가져오기</button></p>    
    </section>`;
}

// 자식 댓글 만들기
function makeNewChildComment(data,parentNum){
    const li = document.createElement('li');    
    li.classList.add('comment-item');    
    if(data){
        li.innerHTML = `
                <article id="${commentIdPrefix}${data.commentNum}" class="comment child-comment pt-3 border-top " ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_USER_NUM}="${data.userNum}" ${KEY_POST_NUM}="${data.postNum}">
                <p class="header">
                    <span class="username">${data.username}</span>
                    <span class="created-at"><small class="time text-muted">${new Date(data.createdAt)} ${data.updatedAt!=data.createdAt ? '(수정됨)':''}</small>에 작성됨</span>
                    <button class="reply btn btn-link" ${KEY_COMMENT_NUM}="${data.commentNum}" >답글</button>
                </p>
                <a href="#${commentIdPrefix}${data.targetNum}">@${data.targetUsername}&nbsp;</a>
                <p class="content bg-light p-3">${data.content}</p>
                <textarea class="form-control modify-editor"></textarea>
                ${getLikeTag(data)}
                ${getReplyEditorTag(data,parentNum)}     
            </article>                        
        `;
    }
    return li;
}

// 날짜 양식
