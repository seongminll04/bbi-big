import React from 'react';
import styles from './login.module.css';

import logo from '../assets/images/logo with name.png';
import kakao_logo from '../assets/images/kakao_logo.png';
import naver_logo from '../assets/images/naver_logo.png';
import google_logo from '../assets/images/google_logo.png';

function Login() {
    
    const login = (social:string) => {
        window.location.href = `http://localhost:8081/oauth2/authorization/${social}`
    }

    return (
        <div className={styles.container}>
            <header className={styles.logo_container}>
                <img src={logo} alt="logo" style={{width:'60%', minWidth:500}}  />
            </header>

            <main className={styles.login_container}>
                <button className={`${styles.login} ${styles.kakao}`} 
                        onClick={()=>{login('kakao')}}>
                    <img src={kakao_logo} alt="" className={styles.login_logo} />
                    카카오로 로그인
                </button>
                <button className={`${styles.login} ${styles.naver}`} 
                        onClick={()=>{login('naver')}}>
                    <img src={naver_logo} alt="" className={styles.login_logo}  />
                    네이버로 로그인
                </button>
                <button className={`${styles.login} ${styles.google}`} 
                        onClick={()=>{login('google')}}>
                    <img src={google_logo} alt="" className={styles.login_logo} />
                    구글로 로그인　
                </button>
            </main>
        </div>
    );
}

export default Login;
