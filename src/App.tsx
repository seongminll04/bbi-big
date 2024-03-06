import React from 'react';
import './App.css';

import logo from './assets/images/logo.png'

function App() {
  return (
    <div className="App">
      <header className="App-header">

        <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
          <img src={logo} className="App-logo" alt="logo" />
          <p className='logofont'>bbi-big</p>
        </div>



      </header>
    </div>
  );
}

export default App;
