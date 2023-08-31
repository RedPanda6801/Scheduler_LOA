// Schedule
import React, {useEffect, useState} from "react";
import axios from "axios";

const Category3 = () => {
  const [scheduleCheckResult, setScheduleCheckResult] = useState("");
  const [characterSchedule, setCharacterSchedule] = useState([]);
  const [resetResult, setResetResult] = useState("");
  const [characterId, setCharacterId] = useState("");

  const JWT_TOKEN = localStorage.getItem("token");

    useEffect(() => {
        axios.get("/api/character/get-chars",
            {
                headers: {
                    Authorization: `Bearer ${JWT_TOKEN}`
                }
            }
        ).then((response) => {
            console.log(response.data);
            setCharacterSchedule(response.data);
        })
    }, []);
  const handleScheduleCheck = (e) => {
      e.preventDefault();
    // api 불러오기가 일단 되는지 확인
    // 데이터값 적용되나?
    let body = {
        "id" : characterId,
        "valtan" : true,
        "biakiss" : true,
        "akkan" : true
    };
    axios
      .post(
        "/api/schedule/check",
        body,
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          },
        }
      )
      .then((response) => {
        setScheduleCheckResult("Check Success");
        window.location.reload();
      })
      .catch((error) => {
        setScheduleCheckResult("Check Failed");
      });
  };

  const handleScheduleReset = () => {
    // reset 항목
    // 초기화 되는지 확인
      if(!window.confirm("정말 초기화 하시겠습니까?")){
          alert("초기화가 취소되었습니다.");
      }
      else
    axios
      .post(
        "/api/schedule/reset",
        {},
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          },
        }
      )
      .then((response) => {
        setResetResult("Reset Success");
        alert("초기화를 성공하셨습니다.");
      })
      .catch((error) => {
        setResetResult("Reset Failed");
      });
  };

  return (
    <div>
      <div>개인 컨텐츠</div>
        {
            Array.isArray(characterSchedule) ? (characterSchedule.map((data) => {
                return (
                    <div key={data.id}>
                        <h4>{data.charName} : {data.level} {data.job}</h4>
                        {
                            Object.keys(data.scheduleDto).map((key) => {
                                if(key == "id") return(<p> 스케줄 ID : {data.scheduleDto[key]}</p>)
                                return(
                                        <p>{key} : {data.scheduleDto[key]? "T" : "F"}</p>
                                )
                            })
                        }
                    </div>
                )
            })) :
                <p>Check to Login</p>
        }
        <form onSubmit={handleScheduleCheck}>
            <input
                type="text"
                placeholder="check Character's Id"
                value={characterId}
                onChange={(e) => setCharacterId(e.target.value)}
            />
            <button type="submit">스케줄 체킹</button>
        </form>

      <p>{scheduleCheckResult}</p>
      <button onClick={handleScheduleReset}>스케줄 초기화</button>
      <p>{resetResult}</p>
    </div>
  );
};

export default Category3;
