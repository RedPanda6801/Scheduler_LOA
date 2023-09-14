import React, { useState, useEffect } from "react";
import axios from "axios";

const Category4 = () => {
  const [favoriteCrewMembers, setFavoriteCrewMembers] = useState([]);
  const [selectedCrewMember, setSelectedCrewMember] = useState("");
  const [contentProgress, setContentProgress] = useState("");
  const [crewMembers, setCrewMembers] = useState([]);
  const token = localStorage.getItem("token");

  // 크루원 목록 가져오기
  useEffect(() => {
    axios
      .get("/api/crew/members", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setCrewMembers(response.data.members);
      })
      .catch((error) => {
        console.error("Error fetching crew members", error);
      });
  }, [token]);

  const handleFavoriteCrewMember = (crewMember) => {
    // 즐겨찾기 토글
    if (favoriteCrewMembers.includes(crewMember)) {
      setFavoriteCrewMembers(
        favoriteCrewMembers.filter((member) => member !== crewMember)
      );
    } else {
      setFavoriteCrewMembers([...favoriteCrewMembers, crewMember]);
    }
  };

  const handleCrewMemberClick = (crewMember) => {
    // 크루원 컨텐츠 진행 현황 확인
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
      <div>
        <h2>즐겨찾기 크루원</h2>
        {/* 즐겨찾기 리스트 */}
        {crewMembers.map((member) => (
          <div key={member}>
            {member}
            <button onClick={() => handleFavoriteCrewMember(member)}>
              {favoriteCrewMembers.includes(member)
                ? "즐겨찾기 해제"
                : "즐겨찾기"}
            </button>
          </div>
        ))}
      </div>

      <div>
        {/* 크루원 선택 */}
        <select onChange={(e) => setSelectedCrewMember(e.target.value)}>
          <option value="">크루원 선택</option>
          {crewMembers.map((member) => (
            <option key={member} value={member}>
              {member}
            </option>
          ))}
        </select>
        <button onClick={() => handleCrewMemberClick(selectedCrewMember)}>
          컨텐츠 진행 현황 확인
        </button>
        <p>컨텐츠 진행 현황: {contentProgress}</p>
      </div>
    </div>
  );
};

export default Category4;
