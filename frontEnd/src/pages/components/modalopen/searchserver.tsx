import React, { useState } from 'react';
import styles from './searchserver.module.css';


function SearchServer() {
    const [serverName, setServerName] = useState('');

    const handleSearchServer = () => {
      console.log('서버를 검색합니다:', serverName);
    }
  
    return(
        <div className={styles.container}>
            <button onClick={handleSearchServer}>서버 생성</button>
        </div>
    );
  
  }
  
  export default SearchServer;