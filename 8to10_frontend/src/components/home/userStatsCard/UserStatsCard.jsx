import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";


const UserStatsCard = () => {
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
            <span>{nickname}</span> 님의 <br/> 달성률은 <strong>({achievementRate}%)</strong> 입니다.
            {/*<button className="details-button" onClick={() => window.location.href = '/achievement'}>*/}
            {/*    자세히 보기*/}
            {/*</button>*/}
        </div>
    );
}

export default UserStatsCard;