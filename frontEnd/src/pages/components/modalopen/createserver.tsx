import React, { useState } from 'react';
import styles from './createserver.module.css';

function CreateServer() {
  const [serverName, setServerName] = useState('');
  const [modalOpen, setModalOpen] = useState(true);
  const modalClose = () => {
    setModalOpen(!modalOpen);
  };

  const handleCreateServer = () => {
    console.log('서버를 생성합니다:', serverName);

  };

  return (
    <div className={styles.container}>
      <p>서버 만들기</p>
      <img className={styles.serverImg} src="" alt="" />
      <button onClick={handleCreateServer}>서버 생성</button>
      <button onClick={modalClose}>취소</button>
    </div>
  );
}

export default CreateServer;