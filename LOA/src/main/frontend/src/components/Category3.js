import React, { useEffect, useState } from "react";
import axios from "axios";
import Accordion from "react-bootstrap/Accordion";
import { Form, Col } from "react-bootstrap";

const API_URL = "http://localhost:8005"; // URL 어려워서 선언해 버리기~
const API_KEY =
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMjkxMDMifQ.pzyW3e9arSfzt83LULmVxs0RvBqlm27P1meE6KikBZW7JonoCYxzU7_rFRg8Kmn4Z7qVZ5u7Rn4DYtwZjtruP4dlTmOI229lpsCFs9tBj81rcs1sVD-zzep0EEx9V0XnEgQv_YIKEpEYtR7N06-9M4sFHqj-ScjUllly43RTyXa1vGyKwtHNhfwjXmYPu9oIGIjsdKe-a2aGZStuh6aSYVcFF2-KXcfwlHbTwxulYPn78GQkl6JfXOb6QzSxqum-xoK0XGiJz7GLM4X_GmyBu8PDvfe_eT8hB6P0Xib0VP6j4jKPmbX9GInrlj92IKgWVjLb3WHLHA07a1GiBXGH-A";
const Category3 = () => {
  const [scheduleCheckResult, setScheduleCheckResult] = useState("");
  const [characterSchedule, setCharacterSchedule] = useState([]);
  const [resetResult, setResetResult] = useState("");
  const [characterId, setCharacterId] = useState("");
  const [userSchedules, setUserSchedules] = useState([]);

  const [selectedCharacter, setSelectedCharacter] = useState("");
  const [characterScheduleData, setCharacterScheduleData] = useState(null);

  useEffect(() => {
    // JWT Token 가져오기
    const JWT_TOKEN = localStorage.getItem("token");

    axios
      .get(`${API_URL}/api/character/get-chars`, {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
      })
      .then((response) => {
        setCharacterSchedule(response.data);
      });

    axios
      .get(`${API_URL}/api/schedule/get/user-schedules`, {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
        params: {
          characterIds: characterSchedule.map((data) => data.id),
        },
      })
      .then((response) => {
        setUserSchedules(response.data);
      })
      .catch((error) => {
        console.error("Error fetching user schedules:", error);
      });
  }, []);

  const getCharacterSiblings = async (characterName) => {
    try {
      const response = await axios.get(
        `https://developer-lostark.game.onstove.com/characters/우산구름떡/siblings`,
        {
          headers: {
            Authorization: `Bearer ${API_KEY}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error fetching character siblings:", error);
      return null;
    }
  };

  const handleScheduleCheck = async (e) => {
    e.preventDefault();

    try {
      let body = {
        id: characterId,
        valtan: true,
        biakiss: true,
        akkan: true,
      };

      await axios.post(`${API_URL}/api/schedule/check`, body, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      setScheduleCheckResult("Check Success");
      window.location.reload();
    } catch (error) {
      console.error("Error checking schedule:", error);
      setScheduleCheckResult("Check Failed");
    }
  };

  const handleScheduleReset = async () => {
    if (!window.confirm("정말 초기화 하시겠습니까?")) {
      alert("초기화가 취소되었습니다.");
      return;
    }

    try {
      await axios.post(
        `${API_URL}/api/schedule/reset`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      setResetResult("Reset Success");
      alert("초기화를 성공하셨습니다.");
    } catch (error) {
      console.error("Error resetting schedule:", error);
      setResetResult("Reset Failed");
    }
  };

  const handleGetCharacterSchedule = async () => {
    if (!selectedCharacter) return;

    const siblingsData = await getCharacterSiblings(selectedCharacter);
    if (siblingsData) {
      setCharacterScheduleData(siblingsData);
    } else {
      setCharacterScheduleData(null);
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
        <button type="submit">스케쥴 체킹</button>
      </form>
      <p>{scheduleCheckResult}</p>
      <button onClick={handleScheduleReset}>스케줄 초기화</button>
      <p>{resetResult}</p>

      {/* 스케줄 조회 */}
      <div>
        <h2>캐릭터 스케줄 조회</h2>
        <input
          type="text"
          placeholder="캐릭터 이름"
          value={selectedCharacter}
          onChange={(e) => setSelectedCharacter(e.target.value)}
        />
        <button onClick={handleGetCharacterSchedule}>조회</button>
        {characterScheduleData && (
          <div>
            <h3>{characterScheduleData.CharacterName} 스케줄</h3>
            <p>서버: {characterScheduleData.ServerName}</p>
            <p>레벨: {characterScheduleData.CharacterLevel}</p>
            <p>직업: {characterScheduleData.CharacterClassName}</p>
            <p>평균 아이템 레벨: {characterScheduleData.ItemAvgLevel}</p>
            <p>최대 아이템 레벨: {characterScheduleData.ItemMaxLevel}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Category3;
