// src/App.js
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./components/Home";
import Category1 from "./components/Category1";
import Category2 from "./components/Category2";
import Category3 from "./components/Category3";
import Category4 from "./components/Category4";
import Category5 from "./components/Category5";
import SignIn from "./components/SignIn";
import SignUp from "./components/SignUp";

const App = () => {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/category1" element={<Category1 />} />
        <Route path="/category2" element={<Category2 />} />
        <Route path="/category3" element={<Category3 />} />
        <Route path="/category4" element={<Category4 />} />
        <Route path="/category5" element={<Category5 />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
      </Routes>
    </Router>
  );
};

export default App;
