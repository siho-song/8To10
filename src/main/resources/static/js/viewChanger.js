// TODO 마이페이지, 성취도 페이지가 구현되면 해당 뷰를 담는 container도 비활성화하는 코드 추가 필요
function showBoardPage() {
    const mainContainer = document.getElementById('main-container');
    const boardContainer = document.getElementById('board-container');

    mainContainer.innerHTML = '';
    boardContainer.style.display = 'flex';

    boardContainer.innerHTML = boardView;
}

function showCreatePostPage() {
    const createPostPage = document.getElementById('board-container');
    createPostPage.innerHTML = createPostView;
}

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


createPostView = `
    <div class="create-post-container">
        <div class="create-post-header">
            <h2>글쓰기</h2>
            <button class="view-list-btn" onclick="window.location.href='/community'">목록보기</button>
        </div>
        <hr>
        <div class="post-form">
            <div class="post-form-group">
                <label for="post-title">글 제목</label>
                <input type="text" id="post-title" placeholder="글 제목을 입력하세요">
                <span class="error-message" id="title-error"></span>
            </div>

            <div class="post-form-group">
                <label for="post-content">글 내용</label>
                <textarea id="post-content" rows="10" placeholder="내용을 입력하세요"></textarea>
                <span class="post-error-message" id="content-error"></span>
            </div>

            <div class="post-form-group">
                <label for="post-file">첨부 파일</label>
                <input type="file" id="post-file">
            </div>
            <button class="post-submit-btn" onclick="submitPost()">등록</button>
        </div>
    </div>
`;