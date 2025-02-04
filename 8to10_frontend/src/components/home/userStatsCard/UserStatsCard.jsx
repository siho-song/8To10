import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useNavigate} from "react-router-dom";


const UserStatsCard = () => {

    const navigate = useNavigate();

    const [nickname, setNickname] = useState('unknown');
    const [role, setRole] = useState('');
    const [achievementRate, setAchievementRate] = useState(0);

    useEffect(() => {
        const loadUserStats = async () => {
            try {
                const url = '/home/user-stats';
                const response = await authenticatedApi.get(
                    url,
                    {
                        apiEndPoint: API_ENDPOINT_NAMES.GET_USER_STATSCARD,
                    }
                );

                const data = response.data;
                localStorage.setItem('nickname', data.nickname);
                setNickname(data.nickname);
                setRole(data.role);
                setAchievementRate(Math.floor(data.achievementRate * 100));
            } catch(error) {
                console.error(error.toString());
                console.error(error);
            }
        };

        loadUserStats();
    },[]);

    return (
        <div className="achievement-box">
            <p><span>{nickname}</span> 님의 <br/> 달성률은 <strong>({achievementRate}%)</strong> 입니다.</p>
            <button className="details-button" onClick={() => navigate('/achievement')}>
                자세히 보기
            </button>
        </div>
    );
}

export default UserStatsCard;