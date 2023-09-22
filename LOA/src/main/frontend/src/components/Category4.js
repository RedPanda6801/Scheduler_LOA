import React, { useEffect, useState } from "react";
import axios from "axios";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {Form, FormText} from "react-bootstrap";

const JWT_TOKEN = localStorage.getItem("token");

const Category4 = () => {
  const [userCreatedCrew, setUserCreatedCrew] = useState("");
  const [selectedCrewOnes, setSelectedCrewOnes] = useState([]);
  const [selectedCrewMember, setSelectedCrewMember] = useState("");
  const [userCrews, setUserCrews] = useState([]);
  const [selectedCrewInfo, setSelectedCrewInfo] = useState(null); // New state to store selected crew info
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [crewName, setCrewName] = useState("");

  useEffect(() => {
    axios
      .get("/api/crew/get-crew", {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
      })
      .then((response) => {
        console.log(response.data);
        setUserCrews(response.data);
      });
  }, []);

  const handleCreateCrew = async (e) => {
    e.preventDefault();

    const body = {
      name : e.target[1].value,
      info : e.target[3].value
    }
    // 중복 검사한 값과 다른 값이 입력될 경우 예외처리
    if(crewName !== body.name){
      const btn = document.getElementById("createBtn");
      alert("크루 이름 중복 검사를 해주세요");
      btn.disabled = true;
    }

    try{
      const response = await axios.post(
        "/api/crew/create",
        body,
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          },
        }
      );
      console.log(response);
      if(response.status === 200){
        alert("생성 성공");
        handleCloseCreateModal();
      }
    }catch(error) {
      console.log(error);
      if (error.response.data.message === "Crew Name Overlapped") {
        alert("크루 이름이 중복되었습니다.");
      } else if (error.response.data.message === "Limited to Create") {
        alert("크루는 2개까지만 만들 수 있습니다.");
      }
      setUserCreatedCrew("Crew creation failed");
    }
  };

  const handleShowCreateModal = () =>{
    setShowCreateModal(true);
  }
  const handleCloseCreateModal = () => {
    setShowCreateModal(false);
  }

  const handleCrewOverlapped = async () => {
    const checkCrewName = document.getElementById("crewNameInput").value;
    try{
      if(checkCrewName === "" || checkCrewName === null){
        alert("크루 이름을 입력해주세요.");
        return;
      }
      // 중복되면 false, 중복되지 않으면 true
      const response = await axios.get(
        `/api/crew/is-crew/${checkCrewName}`,
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          }
        }
      );
      // 중복 시에 예외처리
      if(!response.data.data){
        alert("동일한 크루 이름이 있습니다.");
      }else{
        // 생성 가능하면 생성버튼 disabled 해제
        alert("생성 가능한 크루 이름입니다.");
        const btn = document.getElementById("createBtn");
        setCrewName(checkCrewName);
        btn.disabled = false;
      }
    }catch(error){
      console.log(error);
    }
  }
  const handleCrewSelect = async (crew) => {
    const response = await axios.get(`/api/crew/get-members/${crew}`, {
      headers: {
        Authorization: `Bearer ${JWT_TOKEN}`,
      },
    });
    setSelectedCrewOnes(response.data);

    // Store selected crew info when a crew is selected
    setSelectedCrewInfo(response.data);
  };

  return (
    <div>
      <h2>크루 관리 게시판</h2>
      <Button onClick={handleShowCreateModal}>내 크루 생성하기</Button>
      <Modal show={showCreateModal} onHide={handleCloseCreateModal}>
        <Form id="createForm" onSubmit={handleCreateCrew}>
          <Modal.Header closeButton>
            <Modal.Title>크루 생성 창</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div>
              <Form.Label>크루 이름</Form.Label>
              <Form.Control id="crewNameInput" type="text"/>
              <Button onClick={handleCrewOverlapped}>중복 확인</Button>
            </div>
            <Form.Label>크루 소개</Form.Label>
            <Form.Control as="textarea" rows={2}/>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseCreateModal}>
              닫기
            </Button>
            <Button variant="primary" id="createBtn" disabled={true} type="submmit">
              크루 생성하기
            </Button>
          </Modal.Footer>
      </Form>
      </Modal>
      <CrewDiv crews={userCrews} onSelect={handleCrewSelect} />
      <select onChange={(e) => setSelectedCrewMember(e.target.value)}>
        <option value="">크루원 선택</option>
        {selectedCrewOnes.map((crewone, index) => (
          <option key={index} value={crewone[index].mainCharacter}>
            {crewone[index].mainCharacter}
          </option>
        ))}
      </select>

      {/* Display selected crew information */}
      {selectedCrewInfo && <ContentStatus crew={selectedCrewInfo} />}
    </div>
  );
};

const CrewDiv = ({ crews, onSelect }) => {
  return (
    <div>
      <select onChange={(e) => onSelect(e.target.value)}>
        <option value="">크루를 선택하세요</option>
        {Array.isArray(crews)
          ? crews.map((crew, index) => (
            <option key={index} value={crew}>
              {crew}
            </option>
          ))
          : null}
      </select>
    </div>
  );
};

const ContentStatus = ({ crew }) => {
  return (
    <div>
      <h3>{crew[0].crewName} 컨텐츠 현황</h3>
      {/* Render crew information here */}
      {crew.map((crewone, index) => (
        <div key={index}>
          <h4>{crewone.mainCharacter} 정보</h4>
          {/* 정보 넣을건데 디자인은 나중에 */}
          {/* <p>Valtan: {crewone.valtan ? "T" : "F"}</p> */}
        </div>
      ))}
    </div>
  );
};

export default Category4;
