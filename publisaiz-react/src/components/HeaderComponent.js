import React, {Component} from 'react';
import { Link } from 'react-router-dom'

class HeaderComponent extends Component{

    render() {
        return (
            <div>
                PUBLISAIZ | >
                <Link to="/">Home</Link> > 
                <Link to="/login">Login</Link> > 
                <Link to="/articles">Articles</Link> > 
                <Link to="/articles/{article}">Article</Link> > 
                <Link to="/cms">CMS</Link> > 
                <Link to="/cms/articles">CMS/Articles</Link> > 
                <Link to="/cms/articles/add">CMS/Article</Link> > 
                <Link to="/admin">Admin</Link>
            </div>
        )
    }
}

export default HeaderComponent;