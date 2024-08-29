boardView = `
    <!-- 왼쪽 사이드바 -->    
    <div class="sidebar" id="left-sidebar">
        <ul class="board-menu">
            <li onclick="showBoard('free')">자유게시판</li>
<!--            <li onclick="showBoard('secret')">비밀게시판</li>-->
        </ul>
    </div>


    <!-- 중앙 컨텐츠 -->
    <div class="board-main-content">
        <div class="board-header">
            <h2 id="board-title">게시판을 선택하세요</h2>
            <div class="board-controls">
                <div class="board-control-left">
                    페이지 당 글 개수:
                    <select id="posts-per-page" onchange="updatePostsPerPage()">
                        <option value="10">10</option>
                        <option value="30">30</option>
                        <option value="50">50</option>
                    </select>
                </div>
                <!-- 검색창 추가 -->
                <div class="search-bar">
                    <input type="text" id="search-input" placeholder="검색어를 입력하세요" oninput="filterPosts()">
                    <button id="search-button" onclick="filterPosts()">검색</button>
                </div>
                <button id="write-post" onclick="writePost()">글쓰기</button>
            </div>
        </div>
        <div id="board-content"></div>
        <div id="pagination" class="pagination"></div>

    </div>
`;