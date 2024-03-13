import React from 'react';

import { useSelector } from 'react-redux';
import { AppState } from '../../../store/state';

import styles from './myprofile.module.css';

import { IoIosOptions } from "react-icons/io";

function MyProfile() {
  const isLogin = useSelector((state: AppState) => state.isLogin);

  return (
    <div className={styles.container}>
        <img className={styles.profileImg} src={isLogin?.profileImg ? isLogin?.profileImg : 'deafault값 추가해줄것'} alt='유저프로필 이미지' />
        <div className={styles.userdata}>
            <p className={styles.nickname}>{isLogin?.nickname ? isLogin.nickname: "닉네임을 설정해주세요"}</p>
            <p>#{isLogin?.tagNum ? (()=>{
                if (String(isLogin.tagNum).length < 4){ return String(isLogin.tagNum).padStart(4,'0'); } 
                return isLogin.tagNum;})() : '0000'}</p>
        </div>
        <IoIosOptions className={styles.settingIcon} onClick={()=>{}}/>
    </div>
  );
}

export default MyProfile;
