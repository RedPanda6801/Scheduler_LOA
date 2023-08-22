import React, { useState, useEffect } from "react";
import axios from "axios";

const MyPage = () => {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [charName, setCharName] = useState("");
  const [server, setServer] = useState("");
  const [userInfo, setUserInfo] = useState(null);
  const [updateResult, setUpdateResult] = useState("");
  const [deleteResult, setDeleteResult] = useState("");

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axios.get(`/api/user/getUser/${userId}`);
        setUserInfo(response.data);
      } catch (error) {
        console.error("Error fetching user info:", error);
      }
    };

    fetchUserInfo();
  }, [userId]);

  const fetchUserInfo = async () => {
    try {
      const response = await axios.get(`/api/user/getUser/${userId}`);
      setUserInfo(response.data);
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`/api/user/update/${userId}`, {
        password: password !== "" ? password : null,
        charName: charName !== "" ? charName : null,
        server: server !== "" ? server : null,
      });

      console.log("Update success:", response.data);
      setUpdateResult("Update Success");
      // Fetch updated user info
      fetchUserInfo();
    } catch (error) {
      console.error("Error updating user:", error);
      setUpdateResult("Update Failed");
    }
  };

  const handleDelete = async () => {
    try {
      const response = await axios.post(
        "/api/user/delete",
        {},
        {
          headers: {
            Authorization: `Bearer YOUR_JWT_TOKEN`,
          },
        }
      );

      console.log("User deleted:", response.data);
      setDeleteResult("Delete Success");
      // Reset user info
      setUserInfo(null);
    } catch (error) {
      console.error("Error deleting user:", error);
      setDeleteResult("Delete Failed");
    }
  };

  return (
    <div>
      <h2>My Page</h2>
      {userInfo ? (
        <div>
          <p>User ID: {userInfo.userId}</p>
          <p>Character Name: {userInfo.charName}</p>
          <p>Server: {userInfo.server}</p>
        </div>
      ) : (
        <p>No user info available.</p>
      )}

      <form onSubmit={handleUpdate}>
        <input
          type="password"
          placeholder="New Password (Leave empty to keep the current password)"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="text"
          placeholder="New Character Name (Leave empty to keep the current name)"
          value={charName}
          onChange={(e) => setCharName(e.target.value)}
        />
        <input
          type="text"
          placeholder="New Server (Leave empty to keep the current server)"
          value={server}
          onChange={(e) => setServer(e.target.value)}
        />
        <button type="submit">Update</button>
        <p>{updateResult}</p>
      </form>

      <button onClick={handleDelete}>Delete Account</button>
      <p>{deleteResult}</p>
    </div>
  );
};

export default MyPage;
