import React, {useEffect, useState} from "react";
import axios from "axios";

const JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJyZWRwYW5kYSIsImlhdCI6MTY5MjI1MzQ2MywiZXhwIjoxNjkyMjU1MjYzLCJpZCI6MSwidXNlcklkIjoicHNnNDE2NCJ9.8KV8cXaqg3sAI_rGS9af6ZIhA5kw2gSbYtOb2PIzjTg";
const Category4 = () => {
  const [userCharacters, setUserCharacter] = useState([]);

  useEffect(() => {
      axios.get("/api/character/user/get-chars",
          {
              headers: {
                  Authorization: `Bearer ${JWT_TOKEN}`
              }
          }).then((response) => {
              console.log(response.data);
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
      {characters.map((character) => (
        <option key={character.id} value={character.charName}>
            {character.charName}
        </option>
      ))}
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
