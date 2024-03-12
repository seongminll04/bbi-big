import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppState } from '../store/state';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setLogin } from '../store/actions';
import styles from './home.module.css';
import { CgArrowsExchangeAlt } from "react-icons/cg";
import { IoIosOptions } from "react-icons/io";

function Home() {
  const isLogin = useSelector((state: AppState) => state.isLogin);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [nowList, setNowList] = useState('서버')

  const listSwap = () => {
    if (nowList ==='서버') {
      setNowList('친구')
    }
    else {
      setNowList('서버')
    }
  }

  useEffect(()=>{
    if (!isLogin) {
      axios({
        method:'get',
        url:'http://localhost:8081/api/getMyData',
        withCredentials: true
      }).then(res => {
        dispatch(setLogin(res.data));
        console.log(res.data);
      }).catch(() => {
        navigate('/login');
      })
    }
  },[isLogin, dispatch, navigate])

  return (
    <div className={styles.container}>
      <div className={styles.side_container}>
        <div className={styles.listswap}>
          <div className={styles.swap}>
            <CgArrowsExchangeAlt width={35} onClick={()=>{listSwap()}}/>
            <p className={styles.drag} style={{margin:0}}>{nowList} 리스트</p>
            <p style={{width:35}}></p>
          </div>
        </div>
        <div className={styles.listbox}>

        </div>
        <div className={styles.myprofile}>
          <img src={isLogin?.profileImg ? isLogin?.profileImg : ''} alt="" className={styles.profileImg} />
          <div className={styles.nickname}>
            <p style={{margin:0}}>1234</p>
            <p style={{margin:0}}>adfs</p>
            <span>asdf</span>
          </div>
          <IoIosOptions className={styles.settingIcon} />
        </div>
      </div>
      <main className={styles.main_container}>


      </main>
    </div>
  );
}

export default Home;
