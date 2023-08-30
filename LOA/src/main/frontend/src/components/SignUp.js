import React, { useState } from "react";
import axios from "axios";

const SignUp = () => {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [charName, setCharName] = useState("");
  const [server, setServer] = useState("");

  const handleSignUp = async (e) => {
    e.preventDefault();
    // Validation
    if(userId == "" || password == "" || charName == "" || server == ""){
      alert("입력이 더 필요합니다.");
      return;
    }
    try {
      const response = await axios.post("/api/auth/sign", {
        userId,
        password,
        charName,
        server,
      });
      alert("회원가입 완료");
      console.log("User registered:", response.data);
    } catch (error) {
      alert("회원가입 실패");
      console.error("Error signing up:", error);
    }
  };

  return (
    <div>
      <h2>Sign Up</h2>
      <form onSubmit={handleSignUp}>
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
        <input
          type="text"
          placeholder="Character Name"
          value={charName}
          onChange={(e) => setCharName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Server"
          value={server}
          onChange={(e) => setServer(e.target.value)}
        />
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};

export default SignUp;
