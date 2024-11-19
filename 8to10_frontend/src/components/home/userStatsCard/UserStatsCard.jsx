import {useState} from "react";


const UserStatsCard = () => {
    const [nickname, setNickname] = useState('unknown');
    const [achievementRate, setAchievementRate] = useState(0);

    return (
        <div className="achievement-box">
            <span>{nickname}</span> 님의 <br/> 달성률은 <strong>({achievementRate}%)</strong> 입니다.
            <button className="details-button" onClick={() => window.location.href = '/achievement'}>
                자세히 보기
            </button>
        </div>
    );
}

export default UserStatsCard;