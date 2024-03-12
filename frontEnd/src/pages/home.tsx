import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppState } from '../store/state';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setLogin } from '../store/actions';


function Home() {
  const isLogin = useSelector((state: AppState) => state.isLogin);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(()=>{
    if (!isLogin) {
      axios({
        method:'get',
        url:'http://localhost:8081/api/getMyData',
        withCredentials: true
      }).then( res => {
        console.log(res)
        dispatch(setLogin(res.data));
        console.log(res.data);
      }).catch( (err) => {
        navigate('/login');
      })
    }
  },[isLogin, dispatch, navigate])

  return (
    <div>
    </div>
  );
}

export default Home;
