import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppState } from '../store/state';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setLogin, setModal } from '../store/actions';
import styles from './home.module.css';

import MyProfile from './components/home/myprofile';
import MyList from './components/home/mylist';
import ModalOpen from './modalopen';

import make_img from '../assets/images/make_server.png';
import search_img from '../assets/images/search_server.png';

function Home() {
  const isLogin = useSelector((state: AppState) => state.isLogin);
  const isModalOpen = useSelector((state: AppState) => state.isModalOpen != null);
  const dispatch = useDispatch();
  const navigate = useNavigate();


  useEffect(()=>{
    if (!isLogin) {
      axios({
        method:'get',
        url:process.env.REACT_APP_BACKEND_URL+'user/mydata',
        withCredentials: true
      }).then(res => {
        dispatch(setLogin(res.data));
        if (!res.data.nickname) {
          dispatch(setModal('첫 로그인'));
        }
      }).catch(() => {
        navigate('/login');
      })
    }
    else {
      if (!isLogin.nickname) {
        dispatch(setModal('첫 로그인'));
      }
    }
  },[isLogin, dispatch, navigate])

  return (
    <div className={styles.container}>
      {isModalOpen && <ModalOpen />}
      {isLogin?.nickname && 
      <>
      <div className={styles.side_container}>
        <MyList />
        <MyProfile />
      </div>
      <main className={styles.main_container}>
        <div className={styles.sub_box} onClick={()=>{dispatch(setModal("서버만들기"))}}>
          <img src={make_img} alt="" />
          <h1>서버를 만들어볼까요?</h1>
        </div>
        <div className={styles.sub_box} onClick={()=>{dispatch(setModal("서버검색"))}}>
          <img src={search_img} alt="" />
          <h1>서버를 검색하여 가입</h1>
        </div>
      </main>
      </>
      }
    </div>
  );
}

export default Home;
