import React, { useEffect, useState } from 'react';
import './App.css';
import { Route, Routes } from "react-router-dom";
import Login from './pages/login';
import Home from './pages/home';

function App() {
  const [width, setwidth] = useState(window.innerWidth)
  const [height, setheight] = useState(window.innerHeight)

  useEffect(()=>{
    window.addEventListener('resize', function() {
      setwidth(window.innerWidth);
      setheight(window.innerHeight);
    });
  })

  return (
    <div className='App' style={{width:width, height:height}}>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </div>
  );
}

export default App;
