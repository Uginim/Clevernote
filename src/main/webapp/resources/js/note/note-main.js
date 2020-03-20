/**
 * 
 */
// 모든 카데고리 객체를 들고옴
function requestCategories(func){	
	var xhr = new XMLHttpRequest();
	xhr.open("GET","note/categories");	
	xhr.send();	
	xhr.addEventListener('load',(e)=>{
		console.log("xhr"+e.target.response);
		console.log(JSON.parse(e.target.response));
		func(JSON.parse(e.target.response));
	});	
}

// jason데이터를 받아옴
function requestJsonDataByAjax(method,url,func,requestBody){
	var xhr = new XMLHttpRequest();
	xhr.open(method,url);
	if(requestBody)
		xhr.send(requestBody);
	else 
		xhr.send();
	xhr.addEventListener('load',(e)=>{
		console.log("xhr"+e.target.response);
		console.log(JSON.parse(e.target.response));
		func(JSON.parse(e.target.response));
	});	
}




// 카데고리 목록을 리셋함
function resetCategoryList(listElement, listItems){
	removeAllChildNodes(listElement) // 리셋
	
	listItems.forEach((category)=>{
		replaceWithDate(category,"createdAt");
		replaceWithDate(category,"updatedAt");
		console.log(category);
		listElement.appendChild(makeCategoryItem(category));
	})
}

// 카데고리 항목을 만듬
function makeCategoryItem(category){
	var categoryEle= document.createElement('li');
	categoryEle.innerHTML = "<h5>"+category.title+"</h5>";
	categoryEle.setAttribute("data-num",category.categoryNum);	
	console.log(categoryEle);	
	return categoryEle;
}

// 모든 자식 노드를 지움
function removeAllChildNodes(parent){
	while(parent.hasChildNodes()){
		parent.removeChild(parent.firstChild)
	}	
}

// 해당 키 값의 아이템을 date로 바꾸어 주기
function replaceWithDate(object,key){
	object[key]= new Date(object[key]);
}


// 특정 카데고리 내 노트들을 가져옴 
function getNoteList(categoryNum,func){
	var xhr = new XMLHttpRequest();
	xhr.open("GET","note/category/"+categoryNum);	
	xhr.send();	
	xhr.addEventListener('load',(e)=>{
		console.log("xhr"+e.target.response);
		console.log(JSON.parse(e.target.response));
		func(e.target.response);
	});
}