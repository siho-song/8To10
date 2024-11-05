import "@/styles/home/LeftSideBar..css"

function HomeSidebarLeft() {
    return (
        <div className="sidebar" id="left-sidebar">
            <p>왼쪽 사이드바</p>
            <div className="achievement-box">
                <span>User</span> 님의 <br /> 달성률은 <strong>(95.5%)</strong> 입니다.
                <button className="details-button" onClick={() => window.location.href = '/achievement'}>
                    자세히 보기
                </button>
            </div>

            <div className="todo-container">
                <h3>TO-DO</h3>
                <div id="date-navigation">
                    <button id="prev-date-btn">이전</button>
                    <span id="current-date"></span>
                    <button id="next-date-btn">다음</button>
                </div>
                <div id="todo-list"></div>
                <button id="submit-todo-btn">제출</button>
            </div>
        </div>
    );
}

export default HomeSidebarLeft;
