import React, { useState } from "react";
import axios from "axios";

const Category4 = () => {
  // const [userCreatedCrew, setUserCreatedCrew] = useState("");
  const [favoriteCrewMembers, setFavoriteCrewMembers] = useState([]);
  const [selectedCrewMember, setSelectedCrewMember] = useState("");
  const [contentProgress, setContentProgress] = useState("");

  const token = localStorage.getItem("token");

  // const handleCreateCrew = () => {
  //   // crew api 따로 적용 할것인지..?
  //   // 적용 안하면 Post쪽 바꿔야돼
  //   axios
  //     .post(
  //       "/api/crew/create",
  //       {},
  //       {
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //         },
  //       }
  //     )
  //     .then((response) => {
  //       setUserCreatedCrew("Crew created");
  //     })
  //     .catch((error) => {
  //       setUserCreatedCrew("Crew creation failed");
  //     });
  // };

  const handleFavoriteCrewMember = (crewMember) => {
    // 즐겨찾기
    // Example
    if (favoriteCrewMembers.includes(crewMember)) {
      setFavoriteCrewMembers(
        favoriteCrewMembers.filter((member) => member !== crewMember)
      );
    } else {
      setFavoriteCrewMembers([...favoriteCrewMembers, crewMember]);
    }
  };

  const handleCrewMemberClick = (crewMember) => {
    // 클릭 시 나오는 것들
    axios
      .get(`/api/crew/${crewMember}/content-progress`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setContentProgress(response.data.progress);
      })
      .catch((error) => {
        setContentProgress("Error fetching content progress");
      });
  };

  return (
    <div>
      {/*<button onClick={handleCreateCrew}>크루 생성</button>*/}
      {/*<p>{userCreatedCrew}</p>*/}

      <div>
        <h2>즐겨찾기 크루원</h2>
        {/* 즐겨찾기 리스트 */}
        {favoriteCrewMembers.map((member) => (
          <div key={member}>
            {member}
            <button onClick={() => handleFavoriteCrewMember(member)}>
              즐겨찾기
            </button>
          </div>
        ))}
      </div>

      <div>
        {/* 그냥 리스트업 */}
        <select onChange={(e) => setSelectedCrewMember(e.target.value)}>
          <option value="">크루원 선택</option>
        </select>
        <button onClick={() => handleCrewMemberClick(selectedCrewMember)}>
          콘텐츠 진행 현황 확인
        </button>
        <p>콘텐츠 진행 현황: {contentProgress}</p>
      </div>
    </div>
  );
};

export default Category4;
