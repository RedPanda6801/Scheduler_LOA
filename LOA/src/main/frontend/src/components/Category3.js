import React, { useEffect, useState } from "react";
import axios from "axios";
import Accordion from "react-bootstrap/Accordion";
// import { Button } from "react-bootstrap"; 버튼은 잠깐 빼놓기
import { Form, Col } from "react-bootstrap";

const Category3 = () => {
  const [scheduleCheckResult, setScheduleCheckResult] = useState("");
  const [characterSchedule, setCharacterSchedule] = useState([]);
  const [resetResult, setResetResult] = useState("");
  const [characterId, setCharacterId] = useState("");

  const [userSchedules, setUserSchedules] = useState([]); // 사용자 스케줄 정보 추가

  const API_KEY =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMjkxMDMifQ.pzyW3e9arSfzt83LULmVxs0RvBqlm27P1meE6KikBZW7JonoCYxzU7_rFRg8Kmn4Z7qVZ5u7Rn4DYtwZjtruP4dlTmOI229lpsCFs9tBj81rcs1sVD-zzep0EEx9V0XnEgQv_YIKEpEYtR7N06-9M4sFHqj-ScjUllly43RTyXa1vGyKwtHNhfwjXmYPu9oIGIjsdKe-a2aGZStuh6aSYVcFF2-KXcfwlHbTwxulYPn78GQkl6JfXOb6QzSxqum-xoK0XGiJz7GLM4X_GmyBu8PDvfe_eT8hB6P0Xib0VP6j4jKPmbX9GInrlj92IKgWVjLb3WHLHA07a1GiBXGH-A";
  const JWT_TOKEN = localStorage.getItem("token");

  useEffect(() => {
    // 사용자의 캐릭터 목록을 가져옵니다.
    axios
      .get("/api/character/get-chars", {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
      })
      .then((response) => {
        console.log(response.data);
        setCharacterSchedule(response.data);
        axios
          .get(
            `https://developer-lostark.game.onstove.com/characters/우산구름떡/siblings`,
            {
              headers: {
                Authorization: `Bearer ${API_KEY}`,
              },
            }
          )
          .then((response) => {
            console.log(response.data);
          });
      });

    // 사용자 스케줄 정보를 가져옵니다.
    axios
      .get("/api/schedule/get/user-schedules", {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
        params: {
          characterIds: characterSchedule.map((data) => data.id), // 모든 캐릭터 ID를 가져오는거
        },
      })
      .then((response) => {
        setUserSchedules(response.data);
      })
      .catch((error) => {
        console.error("Error fetching user schedules:", error);
      });
  }, []);

  const handleScheduleCheck = (e) => {
    e.preventDefault();

    let body = {
      id: characterId,
      valtan: true,
      biakiss: true,
      akkan: true,
    };

    axios
      .post("/api/schedule/check", body, {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
      })
      .then((response) => {
        setScheduleCheckResult("Check Success");
        window.location.reload();
      })
      .catch((error) => {
        setScheduleCheckResult("Check Failed");
      });
  };

  const handleScheduleReset = () => {
    if (!window.confirm("정말 초기화 하시겠습니까?")) {
      alert("초기화가 취소되었습니다.");
    } else {
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
    }
  };

  return (
    <div>
      <div>개인 컨텐츠</div>
      <Accordion defaultActiveKey="0">
        {Array.isArray(characterSchedule) ? (
          characterSchedule.map((data) => {
            const userSchedule = userSchedules.find(
              (schedule) => schedule.id === data.id
            );
            return (
              <Accordion.Item eventKey={data.id} key={data.id}>
                <Accordion.Header>
                  {data.charName} : {data.level} {data.job}
                </Accordion.Header>
                <Accordion.Body>
                  <Form>
                    <div className="character-body-div">
                      {Object.keys(data.scheduleDto).map((key) => {
                        if (key === "id") return null;
                        const isChecked = userSchedule
                          ? userSchedule[key]
                          : false;
                        return (
                          <Form.Group key={key} as={Col} controlId={key}>
                            <Form.Check
                              type="checkbox"
                              label={key}
                              defaultChecked={isChecked}
                            />
                          </Form.Group>
                        );
                      })}
                    </div>
                  </Form>
                </Accordion.Body>
              </Accordion.Item>
            );
          })
        ) : (
          <p>Check to Login</p>
        )}
      </Accordion>
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
