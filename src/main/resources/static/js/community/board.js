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
