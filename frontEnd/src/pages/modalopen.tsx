import React from 'react';

import { useSelector } from 'react-redux';
import { AppState } from '../store/state';
import styles from './modalopen.module.css';
import FirstLogin from './components/modalopen/firstlogin';

function ModalOpen() {
  const isModalOpen = useSelector((state: AppState) => state.isModalOpen);

  return (
    <div className={styles.modal_overlay}>
        {isModalOpen === '첫 로그인' ? <FirstLogin /> 
        : null }
    </div>
  );
}

export default ModalOpen;
