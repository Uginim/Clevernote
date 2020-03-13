/**
 * 댓글 관련 
 */

window.addEventListener("load",
(e)=>{
    // 
    
    // 초기화
    initComments();
    // 댓글 등록 버튼
    clevernoteUtil.click('section#comment .comment-frm button.submit',(e)=>{requestWritingComment()} );
    // 댓글 취소 버튼
    clevernoteUtil.click('section#comment .comment-frm button.cancel',(e)=>{ 
        const textarea = document.querySelector('section#comment .comment-frm textarea[name="content"]');
        textarea.value="";
    } );
    // 새로고침
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
        if ( clickedElement.classList.contains('reply')){
            console.log("답글");
        }
    })
});
const commentIdPrefix = "comment-num-";
const KEY_LAST_COMMENT_TIME = "data-lastcommenttime";
const KEY_POST_NUM = "data-postnum";
const COMMENT_COMPONENT_SELECTOR = 'section#comment';
const KEY_COMMENT_NUM = "data-commentnum"
let userVoteInfo ={
    loggedIn:false
};
//


// 댓글 초기화
function initComments(){
    // let postNum = document.querySelector('section#comment').getAttribute(KEY_POST_NUM);
    let postNum = getPostNum();
    requestJson("GET","/comment/"+postNum,(event)=>{
        // console.log(event.currentTarget.response);
        const responseMsg = event.currentTarget.response;
        let data = JSON.parse(responseMsg);
        console.log('data:',data);
        // 총 댓글 개수 갱신        
        updateTotalCommentCount('section#comment .total-count>.item-count',data.totalCommentCount);
        // 댓글 리스트 갱신
        updateRootCommentsList('section#comment section.list>ul',data.commentList,false);        
        // 남은 댓글 개수 갱신
        updateRestRootCount('section#comment article.rest-comments span.count',data.restCommentCount);
        // 마지막 댓글 시간 갱신
        updateLastCommentTime('section#comment article.rest-comments button.refresh',data.commentList);       
        // 투표한것 표시
    });
}



// 댓글 쓰기 요청
function requestWritingComment(){
    const formElement = document.querySelector('section#comment .comment-frm form');
    const formdata = new FormData(formElement);
    // formdata.append("",);
    // let postNum = document.querySelector('section#comment').getAttribute(KEY_POST_NUM);
    let postNum = getPostNum();
    requestJson("POST","/comment/"+postNum,(event)=>{
    var target  = document.querySelector('section#comment .comment-frm textarea[name="content"]');
        target.value="";// textarea 비우기
        // 댓글 추가적으로 요청하기 
        const lastTIme = getLastCommentTime('section#comment article.rest-comments button.refresh');
        requestNextComments(lastTIme);
    },formdata);  
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
        updateRestRootCount('section#comment article.rest-comments span.count',data.restCommentCount);
        // 마지막 댓글 시간 갱신
        updateLastCommentTime('section#comment article.rest-comments button.refresh',data.commentList);  
    });
}

// 다음 자식 댓글 요청
function requestNextChildren(time,parentNum){
    requestJson("GET","/comment/child/?basetime="+time+"&parentNum="+parentNum,(event)=>{
        const responseMsg = event.currentTarget.response;
        let data = JSON.parse(responseMsg);
        const comment = document.querySelector('#'+commentIdPrefix+data.parentKey);
        console.log("comment:"+comment);
        comment.querySelector('.')
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


// 남은 댓글 수 갱신 
function updateRestRootCount(targetSelector,count,listData){
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
/*
<li class="comment-item">
            <article class="comment">
        <p><span class="username"></span><span class="created-at"><small class="time"></small>에 작성됨</span></p>
        <p class="content"></p>
        <div>
            <button class="reply">답글</button>
            <button class="modify">수정</button>
            <button class="cancel">취소</button>
            <button class="delete">삭제</button>
        </div>
    </article>                
</li> 
*/
function makeNewRootComment(data){
    const li = document.createElement('li');
    
    li.classList.add('comment-item');    
    if(data){
        li.innerHTML = `
                <article id="${commentIdPrefix}${data.commentNum}" class="comment" ${KEY_COMMENT_NUM}="${data.commentNum}" data-usernum="${data.userNum} data-postnum="${data.postNum}">
                <p>
                    <span class="username">${data.username}</span>
                    <span class="created-at"><small class="time">${new Date(data.createdAt)} ${data.updatedAt!=data.createdAt ? '(수정됨)':''}</small>에 작성됨</span>
                </p>
                <p class="content">${data.content}</p>
                <div>
                    <span cliss="like">${data.like}</span>
                    <span cliss="dislike">${data.dislike}</span>
                </div>
                <div>
                    <button class="reply">답글</button>                
                </div>            
            </article>                        
        `;
        if(data.childrenCount) {            
            const section = document.createElement('section');      
            const article = li.querySelector('article');
            article.appendChild(section);
            section.outerHTML = `<section class="children-list">
                <h6>${data.childrenCount}개의 답글</h6>
                <ul class="children-list">
                <button class="spread" ${KEY_COMMENT_NUM}="${data.commentNum}" ${KEY_LAST_COMMENT_TIME}="${data.createdAt}">펼치기</button>
            </section>`;
        }
    }
    
    return li;
}
