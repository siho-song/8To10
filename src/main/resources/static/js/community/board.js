document.addEventListener('DOMContentLoaded', () => {
    showBoard();
});

function showBoard() {
    const boardContent = document.getElementById('board-content');
    const boardTitle = document.getElementById('board-title');

    // '게시판을 선택하세요' 메시지 제거
    boardTitle.textContent = '자유게시판';

    // 게시글 렌더링
    boardContent.innerHTML = renderPosts(filteredData.slice(0, postsPerPage));
    updatePagination(filteredData.length, postsPerPage);
}

function renderPosts(data) {
    return data.map(post => `
        <div class="post" id="post">
            <h3 class="post-title">${post.title}</h3>
            <div class="post-info">
                <span class="post-author">${post.created_by}</span>
            </div>
            <p class="post-content-preview">${post.content}</p>
            <div class="post-stats-preview">
                <span class="post-likes">좋아요: ${post.total_like}</span>
                <span class="post-replies">댓글: ${post.total_reply}</span>
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
            document.getElementById('board-content').innerHTML = renderPosts(filteredData.slice(start, end));

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
        document.getElementById('board-content').innerHTML = renderPosts(filteredData.slice(start, end));
    }
}


function loadBoardData(pageNum= 0, currentTotalPage) {
    var totalPages;
    var totalPosts;

    const keyword = document.getElementById("search-input").value;
    const pageSize = document.getElementById("posts-per-page").value;
    const searchCond = document.getElementById("searchCond").value;
    const sortCond = document.getElementById("sortCond").value;

    const params = new URLSearchParams({
        keyword: keyword,
        pageNum: pageNum,
        pageSize: pageSize,
        searchCond: searchCond,
        sortCond: sortCond
    });

    console.log(params.toString());

    fetch(`/community/board?${params.toString()}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            renderBoard(data);
            totalPages = data.totalPages;
            totalPosts = data.totalElements;
            createPaginationButton(totalPages);
            console.log(data);
        })
        .catch(error => console.error('Error:', error));

}
