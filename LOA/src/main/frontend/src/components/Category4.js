import React, { useEffect, useState } from "react";
import axios from "axios";

const JWT_TOKEN = localStorage.getItem("token");

const Category4 = () => {
  const [userCreatedCrew, setUserCreatedCrew] = useState("");
  const [selectedCrewOnes, setSelectedCrewOnes] = useState([]);
  const [selectedCrewMember, setSelectedCrewMember] = useState("");
  const [userCrews, setUserCrews] = useState([]);
  const [selectedCrewInfo, setSelectedCrewInfo] = useState(null);

  useEffect(() => {
    axios
      .get("/api/crew/get-crew", {
        headers: {
          Authorization: `Bearer ${JWT_TOKEN}`,
        },
      })
      .then((response) => {
        setUserCrews(response.data);
      });
  }, []);

  const handleCreateCrew = () => {
    axios
      .post(
        "/api/crew/create",
        { name: "크루크루크크루" },
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          },
        }
      )
      .then((response) => {
        if (response.data === "Create Success") {
          setUserCreatedCrew("Crew created");
          alert("크루가 성공적으로 생성되었습니다.");
        } else {
          setUserCreatedCrew("Crew creation failed");
          alert("크루 생성에 실패하였습니다.");
        }
      })
      .catch((error) => {
        setUserCreatedCrew("Crew creation failed");
        alert("크루 생성에 실패하였습니다.");
      });
  };

  const handleCrewSelect = async (crew) => {
    const response = await axios.get(`/api/crew/get-members/${crew}`, {
      headers: {
        Authorization: `Bearer ${JWT_TOKEN}`,
      },
    });
    setSelectedCrewOnes(response.data);

    setSelectedCrewInfo(response.data);
  };

  return (
    <div>
      <button onClick={handleCreateCrew}>크루 생성</button>
      <h2>크루 관리 게시판</h2>
      <CrewDiv crews={userCrews} onSelect={handleCrewSelect} />
      <select onChange={(e) => setSelectedCrewMember(e.target.value)}>
        <option value="">크루원 선택</option>
        {selectedCrewOnes.map((crewone, index) => (
          <option key={index} value={crewone[index].mainCharacter}>
            {crewone[index].mainCharacter}
          </option>
        ))}
      </select>

      {selectedCrewInfo && <ContentStatus crew={selectedCrewInfo} />}
    </div>
  );
};

const CrewDiv = ({ crews, onSelect }) => {
  return (
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
  );
};

const ContentStatus = ({ crew }) => {
  return (
    <div>
      <h3>{crew[0].crewName} 컨텐츠 현황</h3>
      {crew.map((crewone, index) => (
        <div key={index}>
          <h4>{crewone.mainCharacter} 정보</h4>
        </div>
      ))}
    </div>
  );
};

export default Category4;
