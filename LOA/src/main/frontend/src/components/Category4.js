import React, { useState } from "react";

const Category4 = () => {
  const characters = ["용준1", "용준2", "용준3"];

  const [selectedCharacter, setSelectedCharacter] = useState(null);

  const handleCharacterSelect = (character) => {
    setSelectedCharacter(character);
  };

  return (
    <div>
      <h2>크루관리 게시판</h2>
      <Dropdown characters={characters} onSelect={handleCharacterSelect} />
      {selectedCharacter && <ContentStatus character={selectedCharacter} />}
    </div>
  );
};

const Dropdown = ({ characters, onSelect }) => {
  return (
    <select onChange={(e) => onSelect(e.target.value)}>
      <option value="">캐릭터를 선택하세요</option>
      {characters.map((character, index) => (
        <option key={index} value={character}>
          {character}
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
