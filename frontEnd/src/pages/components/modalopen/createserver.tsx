import React, { useState } from 'react';
import styles from './createserver.module.css';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import {setModal} from '../../../store/actions';

function CreateServer() {
  const dispatch = useDispatch();
  const [serverName, setServerName] = useState('');
  const [serverImg, setServerImg] = useState<FileList | null>(null);
  const [joinMethod, setJoinMethod] = useState('');
  const [searchOpen, setSearchOpen] = useState('');

  const [modalOpen, setModalOpen] = useState(true);

  const create = () => {
    const formdata = new FormData();
    if (serverImg) {
      formdata.append('serverImg', serverImg[0]);
    }

    formdata.append('serverData', JSON.stringify({
      serverName:serverName,
      joinMethod:joinMethod,
      searchOpen:searchOpen
    }))

    axios({
      method:'post',
      url:process.env.REACT_APP_BACKEND_URL + '/server/create',
      data : formdata,
      withCredentials: true,
      headers : {
        "Content-Type": "multipart/form-data",
      }
    }).then(()=> {
      dispatch(setModal(null));
    }).catch(err=> {
      alert('서버를 생성할 수 없습니다.')
    })
  }

  const handleServerName = (value:string) => {
    setServerName(value);
  }

  const handleServerImg = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0 ) {
      setServerImg(event.target.files);
    }
  };

  const modalClose = () => {
    setModalOpen(!modalOpen);
  };

  return (
    <div className={styles.container}>
      <p>서버 만들기</p>
      <input type='file' className={styles.serverImg} onChange={handleServerImg} />
      <input type="text" placeholder='서버이름을 입력해주세요' value={serverName} onChange={(event)=>{handleServerName(event.target.value)}} 
      className={styles.servernameInput} />
      <button className={styles.button} onClick={create}>서버 생성</button>
      <button className={styles.button} onClick={modalClose}>취소</button>
    </div>
  );
}

export default CreateServer;