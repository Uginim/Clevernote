import React, {Component} from 'react'
import './CategoryList.css'
class CategoryList extends Component {
    render(){
        console.log(this.props);
        return ( 
            <section>

            <header>
                <p className="welcome-msg"><span className="username"> {this.props.username} </span>님 환영합니다. </p>
                <button>새로 고침</button>
            </header>
            <ul>
            </ul>
            </section>           
            
        )
        
    }
}


export default CategoryList;