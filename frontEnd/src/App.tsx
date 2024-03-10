import React, { useEffect, useState } from 'react';
import './App.css';
import { Route, Routes, useNavigate } from "react-router-dom";
import Login from './pages/login';
import Home from './pages/home';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { AppState } from './store/state';
import { setLogin } from './store/actions';

function App() {
  const [width, setwidth] = useState(window.innerWidth)
  const [height, setheight] = useState(window.innerHeight)
  const isLogin = useSelector((state: AppState) => state.isLogin);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(()=>{
    window.addEventListener('resize', function() {
      setwidth(window.innerWidth);
      setheight(window.innerHeight);
    });

    if (!isLogin) {
      axios({
        method:'get',
        url:'http://localhost:8081/userdata'
      }).then( res => {
        dispatch(setLogin(res.data.userdata));
        if (res.data.userdata.nickname) { navigate('/nickname'); }
        else { navigate('/');}
      }).catch( err => {
        // navigate('/login');
      })
    }
    else {
      if (!isLogin.nickname) { navigate('/nickname'); }
      else { navigate('/');}
    }
  })

  return (
    <div className='App' style={{width:width, height:height}}>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </div>
  );
}

export default App;
