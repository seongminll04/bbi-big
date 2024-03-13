import React, { useEffect, useState } from 'react';

import styles from './mylist.module.css';
import { CgArrowsExchangeAlt } from "react-icons/cg";
import axios from 'axios';

function MyList() {
    const [nowList, setNowList] = useState('서버')
    const [isList, setList] = useState('')
    const listSwap = () => {
        if (nowList ==='서버') { setNowList('친구') }
        else { setNowList('서버') }};
    
    useEffect(()=>{
        axios({
            method:'get',
            url:process.env.REACT_APP_BACKEND_URL+`${nowList==='서버' ? '/serverlist':'/friendlist'}`,
            withCredentials:true
        }).then(res=>{
            setList(res.data)
        }).catch(err=>{
            console.log(err);
        })
        console.log(nowList)
    },[nowList])

    return (
        <div className={styles.container}>
            <div className={styles.listswap} onClick={listSwap}>
                <CgArrowsExchangeAlt className={`${styles.swapButton} ${nowList==='서버' ? '':styles.swap}`} />
                <p>{nowList} 리스트</p>
                <p style={{width:30}}></p>
            </div>
            <div className={styles.listbox}>
                <p>s</p>
            </div>
        </div>
    );
}

export default MyList;
