// src/components/Home.js
import React, { useEffect, useState } from "react";
import axios from "axios";

const Home = () => {
  const [hello, setHello] = useState("");

  useEffect(() => {
    axios
      .get("/api/hello") // Replace with your actual API endpoint
      .then((response) => setHello(response.data))
      .catch((error) => console.log(error));
  }, []);

  return (
    <div>
      <h2>Welcome to the Home page!</h2>
      <p>Message from the server: {hello}</p>
    </div>
  );
};

export default Home;
