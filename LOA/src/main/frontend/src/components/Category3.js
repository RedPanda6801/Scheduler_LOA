// Schedule
import React, {useEffect, useState} from "react";
import axios from "axios";
import Accordion from "react-bootstrap/Accordion";
import Table from 'react-bootstrap/Table';
import {Button} from "react-bootstrap";
import {Form} from "react-bootstrap";
const Category3 = () => {
  const [scheduleCheckResult, setScheduleCheckResult] = useState("");
  const [characterSchedule, setCharacterSchedule] = useState([]);
  const [resetResult, setResetResult] = useState("");
  const [characterId, setCharacterId] = useState("");

    const API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMjkxMDMifQ.pzyW3e9arSfzt83LULmVxs0RvBqlm27P1meE6KikBZW7JonoCYxzU7_rFRg8Kmn4Z7qVZ5u7Rn4DYtwZjtruP4dlTmOI229lpsCFs9tBj81rcs1sVD-zzep0EEx9V0XnEgQv_YIKEpEYtR7N06-9M4sFHqj-ScjUllly43RTyXa1vGyKwtHNhfwjXmYPu9oIGIjsdKe-a2aGZStuh6aSYVcFF2-KXcfwlHbTwxulYPn78GQkl6JfXOb6QzSxqum-xoK0XGiJz7GLM4X_GmyBu8PDvfe_eT8hB6P0Xib0VP6j4jKPmbX9GInrlj92IKgWVjLb3WHLHA07a1GiBXGH-A";
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
            axios.get(`https://developer-lostark.game.onstove.com/characters/우산구름떡/siblings`,
                {
                    headers: {
                        Authorization: `Bearer ${API_KEY}`
                    }
                }
            ).then((response) => {
                console.log(response.data);
            })
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

  const setSchedule = (e) => {
      e.preventDefault();
      const scheduleId = e.target.id;
      let body = {
          "id" : scheduleId,
          "valtan" : true,
          "biakiss" : true,
          "akkan" : true
      };
      if(window.confirm("정말 스케줄 체크하겠습니까?")){
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
      }
  }

  return (
    <div>
      <div>개인 컨텐츠</div>
        <Accordion defaultActiveKey="0">
        {
            Array.isArray(characterSchedule) ? (characterSchedule.map((data) => {
                    return (
                        <Accordion.Item eventKey={data.id}>
                            <Accordion.Header>{data.charName} : {data.level} {data.job}</Accordion.Header>
                            <Accordion.Body>
                                <Form>
                                <div class="character-body-div">
                                    {
                                        Object.keys(data.scheduleDto).map((key) => {
                                            if(key === "id") return <Button id={data.scheduleDto[key]} onClick={setSchedule}>스케줄 체크</Button>;
                                            return(
                                                <div class="select-div">
                                                    {key}
                                                    <Form.Check aria-label="option 1" defaultChecked={data.scheduleDto[key]} />
                                                </div>
                                                //<Form.Check aria-label="option 1" defaultChecked={data.scheduleDto[key]} />

                                                // <td>{data.scheduleDto[key]? "T" : "F"}</td>/}
                                            )
                                        })
                                    }
                                </div>
                                </Form>
                                {/*<Table striped bordered hover>*/}
                                {/*    <thead>*/}
                                {/*    <tr>*/}
                                {/*        <th></th>*/}
                                {/*        <th>발탄</th>*/}
                                {/*        <th>비아키스</th>*/}
                                {/*        <th>쿠크세이튼</th>*/}
                                {/*        <th>아브렐슈드(12)</th>*/}
                                {/*        <th>아브렐슈드(34)</th>*/}
                                {/*        <th>아브렐슈드(56)</th>*/}
                                {/*        <th>일리아칸</th>*/}
                                {/*        <th>카멘</th>*/}
                                {/*        <th>카양겔</th>*/}
                                {/*        <th>상아탑</th>*/}
                                {/*    </tr>*/}
                                {/*    </thead>*/}
                                {/*    <tbody>*/}
                                {/*    <tr>*/}
                                {/*        {*/}
                                {/*            Object.keys(data.scheduleDto).map((key) => {*/}
                                {/*                if(key === "id") return(*/}
                                {/*                    <Button id={data.scheduleDto[key]} onClick={setSchedule}>스케줄 체크</Button>*/}
                                {/*                );*/}
                                {/*                return(*/}
                                {/*                    <td>*/}
                                {/*                        <Form.Check aria-label="option 1" defaultChecked={data.scheduleDto[key]} />*/}
                                {/*                    </td>*/}
                                {/*                        /*<td>{data.scheduleDto[key]? "T" : "F"}</td>/}
                                {/*                )*/}
                                {/*            })*/}
                                {/*        }*/}
                                {/*    </tr>*/}
                                {/*</tbody>*/}
                                {/*</Table>*/}
                            </Accordion.Body>
                        </Accordion.Item>
                    )
                })) :
                <p>Check to Login</p>
        }
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
