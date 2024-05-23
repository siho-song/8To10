// myPosts.js

const myPostsData = [
    {
        "board_id": 1,
        "title": "내가 쓴 글 1",
        "content": "내가 쓴 첫 번째 글 내용입니다.",
        "created_by": "내 닉네임",
        "created_at": "2024-05-01T12:00:00.000Z",
        "updated_at": "2024-05-01T12:00:00.000Z",
        "total_like": 10,
        "total_scrap": 1
    },
    // 다른 게시글 데이터 추가
];

let postsPerPage = 10;

document.addEventListener('DOMContentLoaded', () => {
    showBoard();
});

function showBoard() {
    const mainContent = document.querySelector('.board-main-content');
    const boardContent = document.getElementById('board-content');
    const boardTitle = document.getElementById('board-title');

    // 게시글 렌더링
    boardContent.innerHTML = renderPosts(myPostsData.slice(0, postsPerPage));
    updatePagination(myPostsData.length, postsPerPage);
}

function renderPosts(data) {
    return data.map(post => `
        <div class="post" onclick="viewPost(${post.board_id})">
            <h3 class="post-title">${post.title}</h3>
            <div class="post-info">
                <span class="post-author">${post.created_by}</span>
            </div>
            <p class="post-content-preview">${post.content}</p>
            <div class="post-stats-preview">
                <span class="post-likes">좋아요: ${post.total_like}</span>
                <span class="post-replies">댓글: ${post.total_reply || 0}</span>
                <span class="post-date">${new Date(post.created_at).toLocaleDateString()}</span>
            </div>
        </div>
    `).join('');
}

function updatePagination(totalPosts, postsPerPage) {
    const pagination = document.getElementById('pagination');
    const pageCount = Math.ceil(totalPosts / postsPerPage);

    pagination.innerHTML = '';
    for (let i = 1; i <= pageCount; i++) {
        const pageLink = document.createElement('a');
        pageLink.className = 'pagination-button';
        pageLink.href = '#';
        pageLink.textContent = i;
        pageLink.onclick = (e) => {
            e.preventDefault();
            const start = (i - 1) * postsPerPage;
            const end = start + postsPerPage;
            document.getElementById('board-content').innerHTML = renderPosts(myPostsData.slice(start, end));

            // 현재 페이지 표시
            const currentPage = document.querySelector('.pagination-button.active');
            if (currentPage) {
                currentPage.classList.remove('active');
            }
            pageLink.classList.add('active');
        };
        pagination.appendChild(pageLink);
    }

    // 첫 번째 페이지를 기본으로 활성화
    if (pageCount > 0) {
        pagination.firstChild.classList.add('active');
        const start = 0;
        const end = postsPerPage;
        document.getElementById('board-content').innerHTML = renderPosts(myPostsData.slice(start, end));
    }
}

function updatePostsPerPage() {
    postsPerPage = parseInt(document.getElementById('posts-per-page').value);
    showBoard();
}

function writePost() {
    window.location.href = '/community/post';
}

function viewPost(postId) {
    window.location.href = `/community/post&${postId}`;
}
