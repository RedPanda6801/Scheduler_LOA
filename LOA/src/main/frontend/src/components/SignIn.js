import React, { useState } from "react";
import axios from "axios";

const SignIn = () => {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");

  const handleSignIn = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("/api/auth/login", {
        userId,
        password,
      });
      if(response.data === 'Id is Incorrected'){
        alert("아이디 에러");
      }else if(response.data === 'Check Your Password'){
        alert("비밀번호 에러");
      }
      alert("로그인 성공");
      localStorage.setItem("token", response.data);
      console.log("User signed in:", response.data);
    } catch (error) {
      alert("회원가입 실패");
      console.error("Error signing in:", error);
    }
  };

  return (
    <div>
      <h2>Sign In</h2>
      <form onSubmit={handleSignIn}>
        <input
          type="text"
          placeholder="User ID"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Sign In</button>
      </form>
    </div>
  );
};

export default SignIn;
