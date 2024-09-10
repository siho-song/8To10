document.addEventListener('change', function (event) {
    if (event.target.id === 'sortCond') {
        loadBoardData();
    }
});

document.addEventListener('click', function (event) {
    if (event.target.id === 'search-button') {
        loadBoardData();
    }
});

function createPaginationButton(pageCount) {

    const pagination = document.getElementById('pagination');
    let currentPageCount = parseInt(pagination.dataset.pageCount);
    var isNewPagination = true;

    if (currentPageCount) {
        isNewPagination = false;
        if (currentPageCount === pageCount) {
            return;
        }
    }

    pagination.dataset.pageCount = pageCount;
    pagination.innerHTML = '';

    for (let i = 1; i <= pageCount; i++) {

        const pageLink = document.createElement('a');

        pageLink.className = 'pagination-button';
        pageLink.id = 'pagination-btn-' + i;
        pageLink.href = '#';
        pageLink.textContent = i;
        pageLink.onclick = (e) => {
            e.preventDefault();

            loadBoardData(i - 1);

            const currentPage = document.querySelector('.pagination-button.active');
            if (currentPage) {
                currentPage.classList.remove('active');
            }

            pageLink.classList.add('active');
        };
        pagination.appendChild(pageLink);
    }

    if (isNewPagination) {
        let firstPage = document.getElementById('pagination-btn-1');
        firstPage.classList.add('active');
    }
}

function renderBoard(data) {
    const boardContent = document.getElementById('board-content');
    boardContent.innerHTML = '';

    data.content.forEach(post => {
        const postItem = document.createElement('div');
        console.log("data on renderBoard() : ", post)

        postItem.innerHTML= `
            <div class="post" id="post" data-post-id="${post.id}">
                <h3 class="post-title">${post.title}</h3>
                <div class="post-info">
                    <span class="post-author">${post.nickname}</span>
                </div>
                <div class="post-stats-preview">
                    <span class="post-likes">좋아요: ${post.totalLike}</span>
                    <span class="post-replies">스크랩 수: ${post.totalScrap}</span>
                    <span class="post-date">${new Date(post.createdAt).toLocaleDateString()}</span>
                </div>
            </div>
        `;
        boardContent.appendChild(postItem);
    });
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

            if (data.content.length > 0) {
                renderBoard(data);
                totalPages = data.totalPages;
                totalPosts = data.totalElements;
                createPaginationButton(totalPages);
                console.log(data);
            }

            else {
                alert("해당 게시글을 찾을 수 없습니다. 검색어를 확인해주세요");
            }
        })
        .catch(error => console.error('Error:', error));

}