import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const Category1 = () => {
  const [category2Content, setCategory2Content] = useState("");

  useEffect(() => {
    axios.get("/api/category2Content").then((response) => {
      setCategory2Content(response.data);
    });
  }, []);

  return (
    <div>
      <p>{category2Content}</p>
      <Link to="/">홈으로 가기</Link>
      <NoticeForm />
    </div>
  );
};

const NoticeForm = () => {
  const [notice, setNotice] = useState("");

  const handleNoticeSubmit = (e) => {
    e.preventDefault();
    // Send notice to the backend using an API call
    axios.post("/api/addNotice", { notice }).then((response) => {
      console.log("Notice added:", response.data);
      setNotice("");
    });
  };

  return (
    <form onSubmit={handleNoticeSubmit}>
      <textarea
        value={notice}
        onChange={(e) => setNotice(e.target.value)}
        placeholder="공지사항을 작성해주세요"
        rows="4"
        cols="50"
      />
      <button type="submit">작성</button>
    </form>
  );
};

export default Category1;
