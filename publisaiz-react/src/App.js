import React from 'react';
import './App.css';
import HeaderComponent from './components/HeaderComponent';
import Articles from './containers/Articles';
import Article from './containers/Article';
import { Route, Router } from 'react-router-dom'
import Login from './components/LoginComponent';
import Cms from './containers/Cms';
import CmsAddArticle from './containers/CmsAddArticle';
import CmsArticles from './containers/CmsArticles';
import AdminManageUsers from './containers/AdminManageUsers';
import { createBrowserHistory } from "history";
const history = createBrowserHistory();

function App() {
  return (
    <div className="App">
      <Router history={history}>
        <HeaderComponent></HeaderComponent>
          <Route exact path="/" component={Articles} />
          <Route path="/login" component={Login} />
          <Route path="/articles" component={Articles} />
          <Route path="/articles/{article}" component={Article} />
          <Route path="/cms" component={Cms} />
          <Route path="/cms/articles" component={CmsArticles} />
          <Route path="/cms/articles/add" component={CmsAddArticle} />
          <Route path="/admin" component={AdminManageUsers} />
      </Router>
    </div>
  );
}

export default App;
