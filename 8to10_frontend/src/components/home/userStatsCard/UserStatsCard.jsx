

const UserStatsCard = () => {

    return (
        <div className="achievement-box">
            <span>User</span> 님의 <br/> 달성률은 <strong>(95.5%)</strong> 입니다.
            <button className="details-button" onClick={() => window.location.href = '/achievement'}>
                자세히 보기
            </button>
        </div>
    );
}

export default UserStatsCard;