import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMjkxMDMifQ.pzyW3e9arSfzt83LULmVxs0RvBqlm27P1meE6KikBZW7JonoCYxzU7_rFRg8Kmn4Z7qVZ5u7Rn4DYtwZjtruP4dlTmOI229lpsCFs9tBj81rcs1sVD-zzep0EEx9V0XnEgQv_YIKEpEYtR7N06-9M4sFHqj-ScjUllly43RTyXa1vGyKwtHNhfwjXmYPu9oIGIjsdKe-a2aGZStuh6aSYVcFF2-KXcfwlHbTwxulYPn78GQkl6JfXOb6QzSxqum-xoK0XGiJz7GLM4X_GmyBu8PDvfe_eT8hB6P0Xib0VP6j4jKPmbX9GInrlj92IKgWVjLb3WHLHA07a1GiBXGH-A";
const Category1 = () => {
  const [category2Content, setCategory2Content] = useState([]);
  const [calendarContant , setCalendarContent ] = useState([]);
  let key = 0;
    useEffect(() => {
        // 공지사항 호출
        axios.get("https://developer-lostark.game.onstove.com/news/notices",
            {
                headers: {
                    Authorization: `Bearer ${API_KEY}`
                }
            }
        ).then((response) => {
            setCategory2Content(response.data);
            // 이벤트섬 호출
            axios.get("https://developer-lostark.game.onstove.com/gamecontents/calendar",
                {
                    headers: {
                        Authorization: `Bearer ${API_KEY}`
                    }
                }
            ).then((response) => {
                setCalendarContent(response.data);
            }).catch((e) =>{
                if(e.response.status === "429"){
                    console.log("Too Many Request!");
                }
                return null;
            })
        }).catch((e) => {
            if(e.response.status === "429"){
                console.log("Too Many Request!");
            }
            return null;
        })
    }, []);

  return (

    <div>
        {
            category2Content.map((data, index) => {
                if(index > 5) return;
                return (
                    <div key={index}>
                        <p>{data.Title}</p>
                        <a href={data.Link}>링크</a>
                    </div>
                )
            })
        }
        {
            calendarContant.map((data, index) => {
                if(index > 5) return;
                return(
                    <div key={key++}>
                        <p>{data.ContentsName}</p>
                    </div>
                )
            })
        }
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
