import React from "react";
import ReactDOM from "react-dom";
import CategoryList from "./CategoryList.js";
window.addEventListener('load',(e)=>{
    // ReactDOM.render(<App />, document.getElementById("root"));
    ReactDOM.render(<CategoryList username="uginim" />, document.getElementById("category-list"));
});