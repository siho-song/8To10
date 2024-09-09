document.addEventListener("click", function (event) {
    if(event.target.id === "board-link" || event.target.id === "board-link-img") {
        event.preventDefault();
        showBoardPage();
        loadBoardData();
    }

    else if (event.target.id === "write-post") {
        event.preventDefault();
        showCreatePostPage();
    }

    // TODO 게시글 상세 뷰 기능이 구현되면 post 프래그먼트의 게시글 id값을 확인하여 게시글 페이지의 세부 사항을 넣어주는 함수를 실행 시켜야 함
    else if (event.target.id === "post") {
        event.preventDefault();
        showPostDetailPage();
        // TODO 게시글 세부사항을 채워주는 함수 실행
    }
});

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

function showPostDetailPage() {
    const postDetailPage = document.getElementById('board-container');
    postDetailPage.innerHTML = postDetailView;
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

postDetailView = `
        <div class="post-container">
            <div class="post-main-content">

                <div id="post-content"></div>

                <div class="comment-section">
                    <input type="text" id="comment-input" placeholder="댓글을 입력하세요">
                    <button id="submit-comment">댓글 등록</button>
                </div>

                <div id="comments-container"></div>
            </div>
        </div>
`;
