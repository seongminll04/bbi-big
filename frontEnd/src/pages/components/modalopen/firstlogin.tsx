import React, { useState } from 'react';
import styles from './firstlogin.module.css';

import logo from '../../../assets/images/logo with name.png';
import { IoMdInformationCircleOutline, IoMdClose, IoMdCheckmark } from "react-icons/io";

import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { setLogin, setModal, updateNickname } from '../../../store/actions';

function FirstLogin() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [nickname, setNickname] = useState('');
  const [isError, setIsError] = useState<boolean|null>(null);
  const [isError2, setIsError2] = useState<boolean|null>(null);
  
  const handleNickname = (value:string) => {
    setNickname(value);
    if (value==='') {
      setIsError(null);
      setIsError2(null);
    }
    else {
      // 정규식을 사용하여 한글, 영어와 숫자로만 이루어져 있는지 체크
      const pattern = /^[a-zA-Z0-9가-힣ㄱ-ㅎ]+$/;
      setIsError(!pattern.test(value));
      // 닉네임의 길이가 2글자 이상 15글자 이하인지 체크
      setIsError2(value.length < 2 || value.length > 15);
    }
  }
  const logout = () => {
    axios({
      method:'delete',
      url:process.env.REACT_APP_BACKEND_URL + '/user/logout',
      withCredentials:true
    }).then(res=>{
      console.log(res.data)
      dispatch(setLogin(null))
      navigate('/login')
      
    }).catch(err=>{
      console.log(err)
    })
  }

  const nicknameRegist = () => {
    axios({
      method:'patch',
      url:process.env.REACT_APP_BACKEND_URL + '/user/nickname',
      data:{
        nickname:nickname
      },
      withCredentials:true
    }).then(()=>{
      dispatch(updateNickname(nickname));
      dispatch(setModal(null));
    }).catch(err=>{
      alert('오류가 발생했습니다.')
    })
  }
  return (
    <div className={styles.container}>
      <img src={logo} alt="logo" style={{width:'60%', minWidth:500}}  />
      <hr />
      <h1>첫 방문을 환영합니다. <br /> 닉네임을 등록하고 "<span className={styles.logofont}>bbi-big</span>"을 이용해보세요!</h1>
      <hr />
      <input type="text" placeholder='닉네임을 입력해주세요' value={nickname} onChange={(event)=>{handleNickname(event.target.value)}}
      className={styles.nicknameInput} />
      
      <p className={`${styles.attention} ${isError==null ? null: isError ? styles.color1:styles.color2}`}>
        <IoMdInformationCircleOutline /> 
        <span className={styles.rl_margin}>닉네임은 한글, 영문, 숫자로 이루어져야 합니다</span>
        {isError==null ? null: isError ? <IoMdClose /> : <IoMdCheckmark />}</p>
      <p className={`${styles.attention} ${isError2==null ? null: isError2 ? styles.color1:styles.color2}`}>
        <IoMdInformationCircleOutline />
        <span className={styles.rl_margin}>닉네임은 2글자 이상 15글자 이하여야 합니다</span>
        {isError2==null ? null: isError2 ? <IoMdClose /> : <IoMdCheckmark />}</p>
      
      <button className={styles.button} onClick={nicknameRegist}
      disabled={(isError===null) || (isError || isError2) ? true:false}>확인</button>
      <button className={styles.button} onClick={logout}>로그아웃</button>
    </div>
  );
}

export default FirstLogin;