import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css'
import {BrowserRouter as Router} from 'react-router-dom'
import {UserProvider} from "./context/context";
import APIUtil from "./util/APIUtil";

const data = JSON.parse(localStorage.getItem("user"))
console.log(data)
ReactDOM.render(
  <React.StrictMode>
      <UserProvider user={{username: data.user }} token={data.userToken}>
          <Router>
              <App/>
          </Router>
      </UserProvider>

  </React.StrictMode>,
  document.getElementById('root')
);


