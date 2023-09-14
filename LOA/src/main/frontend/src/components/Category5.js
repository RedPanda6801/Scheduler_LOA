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
    // 크루원 목록을 가져오려면 먼저 계정과 연결된 크루를 검색하고
    // 검색된 크루에서 Optional Dropdown bar로 크루 하나를 선택해서
    // URL에 올려서 검색해줘야함
    // 임시 crewName
    const crewName = "2023예비군";
    axios
      .get(`/api/crew/get-members/${crewName}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        // 응답값이 쌩 배열이라
        // [ { 첫번째 크루원의 메인 캐릭터 : 가져온 캐릭터 정보 }, { 두번째 크루원의 메인 캐릭터 : 가져온 캐릭터 정보 }...]
        // 이런 식으로 crewMembers에 저장하고자 함
        // 아래는 데이터 정제 예시
        // 밑에서 사용된 변수도 수정되었으니 확인 바람
        console.log(response.data);
        let result = [];
        response.data.data.map((member) => {
          const mainChar = member[0].mainCharacter;
          result.push(
              {
                mainChar : mainChar,
                characters : member[0]
              });
        })
        console.log(result);
        setCrewMembers(result);
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
          <div key={member.mainChar}>
            {member.mainChar}
            <button onClick={() => handleFavoriteCrewMember(member.mainChar)}>
              {favoriteCrewMembers.includes(member.mainChar)
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
            <option key={member.mainChar} value={member.mainChar}>
              {member.mainChar}
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
