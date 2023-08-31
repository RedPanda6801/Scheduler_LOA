import React, {useEffect, useState} from "react";
import axios from "axios";

const JWT_TOKEN = localStorage.getItem("token");
const Category4 = () => {
    const [userCreatedCrew, setUserCreatedCrew] = useState("");
    const [selectedCrewOnes, setSelectedCrewOnes] = useState([]);
    const [selectedCrewMember, setSelectedCrewMember] = useState("");
    const [userCrews, setUserCrews] = useState([]);

  useEffect(() => {
      axios.get("/api/crew/get-crew",
          {
              headers: {
                  Authorization: `Bearer ${JWT_TOKEN}`
              }
          }).then((response) => {
            setUserCrews(response.data);
          })}, [])

    const handleCreateCrew = () => {
        // crew api 따로 적용 할것인지..?
        // 적용 안하면 Post쪽 바꿔야돼
        axios
            .post(
                "/api/crew/create",
                {name :"크루크루크크루"},
                {
                    headers: {
                        Authorization: `Bearer ${JWT_TOKEN}`,
                    },
                }
            )
            .then((response) => {
                setUserCreatedCrew("Crew created");
                alert("crew 생성")
            })
            .catch((error) => {
                setUserCreatedCrew("Crew creation failed");
            });
    };

  const handleCrewSelect = async (crew) => {
      const response = await axios.get(`/api/crew/get-members/${crew}`,
          {
              headers: {
                  Authorization: `Bearer ${JWT_TOKEN}`
              }
          }
      );
      setSelectedCrewOnes(response.data);
  };
  return (
    <div>
        <button onClick={handleCreateCrew}>크루 생성</button>
        <h2>크루 관리 게시판</h2>
                <CrewDiv crews={userCrews} onSelect={handleCrewSelect} />
                <select onChange={(e) => setSelectedCrewMember(e.target.value)}>
                    <option value="">크루원 선택</option>
                {
                    selectedCrewOnes.map((crewone, index) => {
                        return(
                                <option key={index} value={crewone[index].mainCharacter}>
                                    {crewone[index].mainCharacter}
                                </option>
                        )
                    })
                }
                {/*{*/}
                {/*    selectedCrewOnes.map((crewone, index)=>{*/}
                {/*        return(*/}
                {/*                Object.keys(crewone.scheduleDto).map((key) => {*/}
                {/*                    return(*/}
                {/*                        <p>{key} : {crewone.scheduleDto[key]? "T" : "F"}</p>*/}
                {/*                    )*/}
                {/*                })*/}

                {/*        )*/}
                {/*    })*/}
                {/*}*/}
                </select>

    </div>
  );
};

const CrewDiv = ({ crews, onSelect }) => {
  return (
    <select onChange={(e) => onSelect(e.target.value)}>
      <option value="">크루를 선택하세요</option>
      {Array.isArray(crews) ? crews.map((crew, index) => (
        <option key={index} value={crew}>
            {crew}
        </option>
      )) : null}
    </select>
  );
};

const ContentStatus = ({ crew }) => {
  return (
    <div>
      <h3>{crew} 컨텐츠 현황</h3>
    </div>
  );
};

export default Category4;
