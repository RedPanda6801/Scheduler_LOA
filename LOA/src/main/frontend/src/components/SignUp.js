import React, { useState } from "react";
import axios from "axios";

const SignUp = () => {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [charName, setCharName] = useState("");
  const [server, setServer] = useState("");

  const handleSignUp = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8005/auth/sign", {
        userId,
        password,
        charName,
        server,
      });

      console.log("User registered:", response.data);
    } catch (error) {
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
