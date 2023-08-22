import React, {useEffect, useState} from "react";
import axios from "axios";

const JWT_TOKEN = localStorage.getItem("token");
const Category4 = () => {
  const [userCharacters, setUserCharacter] = useState([]);

  useEffect(() => {
      axios.get("/api/character/get-chars",
          {
              headers: {
                  Authorization: `Bearer ${JWT_TOKEN}`
              }
          }).then((response) => {
            setUserCharacter(response.data);
          })}, [])

  const handleCharacterSelect = (character) => {
      setUserCharacter(character);
  };

  return (
    <div>
        <h2>크루 관리 게시판</h2>

                <CharacterDiv characters={userCharacters} onSelect={handleCharacterSelect} />
                {setUserCharacter && <ContentStatus character={setUserCharacter} />}

    </div>
  );
};

const CharacterDiv = ({ characters, onSelect }) => {
  return (
    <select onChange={(e) => onSelect(e.target.value)}>
      <option value="">캐릭터를 선택하세요</option>
      {Array.isArray(characters) ? characters.map((character) => (
        <option key={character.id} value={character.charName}>
            {character.charName}
        </option>
      )) : null}
    </select>
  );
};

const ContentStatus = ({ character }) => {
  return (
    <div>
      <h3>{character} 컨텐츠 현황</h3>
      {/* 컨텐츠 추가해야함 */}
    </div>
  );
};

export default Category4;
