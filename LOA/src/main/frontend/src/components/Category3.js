import React, { useEffect, useState } from "react";
import axios from "axios";
import Accordion from "react-bootstrap/Accordion";
import { Form, Col } from "react-bootstrap";
// 모달 bootstrap import
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

const API_KEY =
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMjkxMDMifQ.pzyW3e9arSfzt83LULmVxs0RvBqlm27P1meE6KikBZW7JonoCYxzU7_rFRg8Kmn4Z7qVZ5u7Rn4DYtwZjtruP4dlTmOI229lpsCFs9tBj81rcs1sVD-zzep0EEx9V0XnEgQv_YIKEpEYtR7N06-9M4sFHqj-ScjUllly43RTyXa1vGyKwtHNhfwjXmYPu9oIGIjsdKe-a2aGZStuh6aSYVcFF2-KXcfwlHbTwxulYPn78GQkl6JfXOb6QzSxqum-xoK0XGiJz7GLM4X_GmyBu8PDvfe_eT8hB6P0Xib0VP6j4jKPmbX9GInrlj92IKgWVjLb3WHLHA07a1GiBXGH-A";

const LOA_URL = "https://developer-lostark.game.onstove.com/";

const Category3 = () => {
  const [scheduleCheckResult, setScheduleCheckResult] = useState("");
  const [characterSchedule, setCharacterSchedule] = useState([]);
  const [resetResult, setResetResult] = useState("");
  const [characterId, setCharacterId] = useState("");
  const [userSchedules, setUserSchedules] = useState([]);
  const [serverCharacters, setServerCharacters] = useState([]);
  const [selectedCharacter, setSelectedCharacter] = useState("");
  const [characterScheduleData, setCharacterScheduleData] = useState(null);
  // Modal State
  const [show, setShow] = useState(false);
  const [selected, setSelected] = useState([]);
  const myChar = localStorage.getItem("character");
  const server = localStorage.getItem("server");

  const handleClose = () =>{
    setShow(false);
  }
  const handleShow = () => {
    setShow(true);
  }

  const getMyServerChar = async () => {
    try {
      const apiRes = await axios.get(
        `${LOA_URL}/characters/${myChar}/siblings`, {
          headers: {
            Authorization: `Bearer ${API_KEY}`
          }
        }
      )
      // 데이터가 없으면 사용자의 메인 캐릭터를 수정해야함
      if(apiRes.data === null){
        alert("대표캐릭터가 없거나 잘못된 캐릭터입니다. 개인정보를 수정해주세요");
        setShow(false);
      }else {
        // 받은 데이터 중 서버 데이터 거르기
        let charArr = [];
        let selectedState = [];

        apiRes.data.map((data) =>{
          if(data.ServerName === server){
            // 레벨값 float으로 넣기
            data.ItemAvgLevel = parseFloat(data.ItemAvgLevel.replace(',',''));
            // 셀렉트박스 감지할 state
            selectedState.push(false);
            charArr.push(data);
          }
        })
        // 설정한 서버가 잘못되었거나 서버에 캐릭터가 없는 경우
        if(charArr.length === 0){
          alert("설정하신 서버에 캐릭터가 없습니다. 개인정보를 수정해주세요.");
          setShow(false);
        }else{
          // state에 넣기
          setSelected(selectedState);
          setServerCharacters(charArr);
        }
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    // JWT Token 가져오기
    const JWT_TOKEN = localStorage.getItem("token");

    axios
      .get(`/api/character/get-chars`, {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
      })
      .then((response) => {
        setCharacterSchedule(response.data.data);
      }).catch((error)=> {
        if(error.response.status === 403){
          alert("로그인 필요");
        }
    });

    // 이 API는 쓸모가 없음 위에서 캐릭터별 스케줄 조회 하는중
    /*axios
      .get(`/api/schedule/get/user-schedules`, {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
        params: {
          characterIds: characterSchedule.map((data) => data.id),
        },
      })
      .then((response) => {
        setUserSchedules(response.data.data);
      })
      .catch((error) => {
        console.error("Error fetching user schedules:", error);
      });*/


  }, []);

  const getCharacterSiblings = async (characterName) => {
    try {
      const response = await axios.get(
        `https://developer-lostark.game.onstove.com/characters/${characterName}/siblings`,
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
    const pk = e.target.getAttribute("pk");
    const checkboxs = e.target;
    try {
      // 들어온 값이 있고 0보다 크면 값 입력
      let body = {
        id: pk,
        valtan: checkboxs[0].value > 0  && checkboxs[0].value !== "" ? checkboxs[0].value : null,
        biakiss: checkboxs[1].value > 0 && checkboxs[1].value !== "" ? checkboxs[1].value : 0,
        kuke : checkboxs[2].value > 0 && checkboxs[2].value !== "" ? checkboxs[2].value : 0,
        abrel:checkboxs[3].value > 0 && checkboxs[3].value !== "" ? checkboxs[3].value : 0,
        akkan: checkboxs[4].value > 0 && checkboxs[4].value !== "" ? checkboxs[4].value : 0,
        kkayangel: checkboxs[5].value > 0 && checkboxs[5].value !== "" ? checkboxs[5].value : 0,
        sanghatop: checkboxs[6].value > 0 && checkboxs[6].value !== "" ? checkboxs[6].value : 0,
        kamen: checkboxs[7].value > 0 && checkboxs[7].value !== "" ? checkboxs[7].value : 0,
      };

      await axios.post(`/api/schedule/check`, body, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      setScheduleCheckResult("Check Success");
      alert("수정되었습니다.");
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
        `/api/schedule/reset`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      setResetResult("Reset Success");
      alert("초기화를 성공하셨습니다.");
      window.location.reload();
    } catch (error) {
      console.error("Error resetting schedule:", error);
      setResetResult("Reset Failed");
    }
  };

  const handleScheduleInit = async (e) => {
    e.preventDefault();
    if(window.confirm("해당 캐릭터들을 선택하시겠습니까?")) {

      let requestArr = [];
      serverCharacters.map((data, index) => {
        if(selected[index]){
          let requestForm = {
            charName : "",
            level : 0.0,
            job : ""
          }
          requestForm.charName = data.CharacterName;
          requestForm.level = data.ItemAvgLevel;
          requestForm.job = data.CharacterClassName;
          requestArr.push(requestForm);
        }
      })

      // 6캐릭터가 넘으면 예외처리
      if(requestArr.length > 6){
        alert("6 캐릭터만 관리 가능합니다.");
        return;
      }
      const initRes = await axios.post("/api/character/init",
        requestArr,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
      if(initRes.status === 201){
        alert("초기화 성공");
        window.location.reload();
      }
      else{
        alert("초기화 실패");
        setShow(false);
      }
    }
  }

  const handleInitCheckbox = (e) => {
    selected[e.target.id -1] = e.target.checked;
  }

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
      <h3>개인 컨텐츠 관리</h3>
      <Button variant="primary" onClick={handleShow}>
        캐릭터 추가
      </Button>
      <Modal show={show} onHide={handleClose}>
        <Form onSubmit={handleScheduleInit}>
        <Modal.Header closeButton>
          <Modal.Title>Modal heading</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Button variant="primary" onClick={getMyServerChar}>내 대표 캐릭터로 조회하기</Button>
            {
              Array.isArray(serverCharacters) ? (
                serverCharacters.map((data, index) => {

                  return(
                    <Form.Check // prettier-ignore
                      id={index+1}
                      type={"checkbox"}
                      key={index}
                      label={`${data.CharacterName} // ${data.ItemAvgLevel} // ${data.CharacterClassName}`}
                      onChange={handleInitCheckbox}
                    />
                  )
                })
              ) : "숙제 관리할 캐릭터를 골라주세요"
            }
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            닫기
          </Button>
          <Button variant="primary" type="submit">
            캐릭터 추가
          </Button>
        </Modal.Footer>
        </Form>
      </Modal>
      <Button variant="secondary" onClick={handleScheduleReset}>로요일 초기화</Button>
      <Accordion defaultActiveKey="0">
        {Array.isArray(characterSchedule) ? (
          characterSchedule.map((data) => {
            const pk = data.scheduleDto.id;
            const userSchedule = userSchedules.find(
              (schedule) => schedule.id === data.id
            );
            return (
              <Accordion.Item eventKey={data.id} key={data.id}>
                <Accordion.Header>
                  {data.charName} : {data.level} {data.job}
                </Accordion.Header>
                <Accordion.Body>
                  <Form onSubmit={handleScheduleCheck} pk={pk}>
                    <div className="character-body-div">
                      {Object.keys(data.scheduleDto).map((key) => {
                        if (key === "id")return;
                        const isChecked = userSchedule
                          ? userSchedule[key]
                          : false;
                        return (
                          <Form.Group key={key} as={Col} controlId={key}>
                            {key} <input
                              width="20px"
                              id={key.id}
                              placeholder={data.scheduleDto[key]}
                              type="number"
                            />
                          </Form.Group>
                        );
                      })}
                      <Button type="submit" ></Button>
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

      {/* 안쓰는 코드*/}
      {/*<form onSubmit={handleScheduleCheck}>
        <input
          type="text"
          placeholder="check Character's Id"
          value={characterId}
          onChange={(e) => setCharacterId(e.target.value)}
        />
        <button type="submit">스케쥴 체킹</button>
      </form>
      <p>{resetResult}</p>*/}

      {/* 이 스케줄 조회도 우선은 필요 없어 보인다. => 위에서 조회 및 체크, 초기설정 다 하기 때문
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
      */}
    </div>
  );
};

export default Category3;
