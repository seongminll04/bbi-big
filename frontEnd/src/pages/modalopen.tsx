import React from 'react';

import { useSelector } from 'react-redux';
import { AppState } from '../store/state';
import styles from './modalopen.module.css';
import FirstLogin from './components/modalopen/firstlogin';
import CreateServer from './components/modalopen/createserver';
import SearchServer from './components/modalopen/searchserver';

function ModalOpen() {
  const isModalOpen = useSelector((state: AppState) => state.isModalOpen);

  return (
    <div className={styles.modal_overlay}>
        {isModalOpen === '첫 로그인' ? <FirstLogin /> 
        : isModalOpen === '서버만들기' ? <CreateServer/>
        : isModalOpen === '서버검색' ? <SearchServer />
        : null}
    </div>

  );

}

export default ModalOpen;
