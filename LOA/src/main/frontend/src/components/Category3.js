// Schedule
import React, { useState } from "react";
import axios from "axios";

const Category3 = () => {
  const [scheduleCheckResult, setScheduleCheckResult] = useState("");
  const [resetResult, setResetResult] = useState("");
  const JWT_TOKEN = localStorage.getItem("token");
  const handleScheduleCheck = () => {
    // api 불러오기가 일단 되는지 확인
    // 데이터값 적용되나?
    let body = {
        "id" : 16,
        "valtan" : true,
        "biakiss" : true,
        "akkan" : true
    };
    axios
      .post(
        "/api/schedule/check",
        body,
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          },
        }
      )
      .then((response) => {
        setScheduleCheckResult("Check Success");
      })
      .catch((error) => {
        setScheduleCheckResult("Check Failed");
      });
  };

  const handleScheduleReset = () => {
    // reset 항목
    // 초기화 되는지 확인
    axios
      .post(
        "/api/schedule/reset",
        {},
        {
          headers: {
            Authorization: `Bearer ${JWT_TOKEN}`,
          },
        }
      )
      .then((response) => {
        setResetResult("Reset Success");
      })
      .catch((error) => {
        setResetResult("Reset Failed");
      });
  };

  return (
    <div>
      <div>개인 컨텐츠</div>
      <button onClick={handleScheduleCheck}>스케줄 체킹</button>
      <p>{scheduleCheckResult}</p>
      <button onClick={handleScheduleReset}>스케줄 초기화</button>
      <p>{resetResult}</p>
    </div>
  );
};

export default Category3;
