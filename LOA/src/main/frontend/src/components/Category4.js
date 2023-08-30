import React, {useEffect, useState} from "react";
import axios from "axios";

const JWT_TOKEN = localStorage.getItem("token");
const Category4 = () => {
    const [selectedCrewOnes, setSelectedCrewOnes] = useState([]);
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
        <h2>크루 관리 게시판</h2>

                <CrewDiv crews={userCrews} onSelect={handleCrewSelect} />
                {
                    selectedCrewOnes.map((crewone, index) => {
                        console.log(index);
                        return(
                            <p key={index}>{crewone[index].mainCharacter}</p>
                            // {<ContentStatus crewone={crewone}/>}
                        )
                    })
                }

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
