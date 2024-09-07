document.addEventListener('DOMContentLoaded', function () {
    const path = window.location.pathname;
    const postId = path.split('&').pop();
    showPostDetail(postId);

    document.getElementById('submit-comment').addEventListener('click', function() {
        const commentInput = document.getElementById('comment-input');
        const commentText = commentInput.value.trim();
        if (commentText) {
            addComment('사용자닉네임', commentText);
            commentInput.value = ''; // 입력 필드 비우기
        }
    });
});

// 게시글 보기 -> 서버랑 통신할 수 있게 바꿔야 하는 함수
function showPostDetail(postId) {
    const postContent = document.getElementById('post-content');

    const defaultProfileImageUrl = "https://via.placeholder.com/100"; // 기본 이미지 URL

    console.log(typeof postId);

    // 해당 게시글 데이터 가져오기 (예제 데이터 사용)
    const post = exampleData.find(post => post.board_id === Number(postId));

    console.log(exampleData);

    if (post) {
        postContent.innerHTML = `
            <div class="post-detail">
                <div class="post-header">
                    <h3 class="post-title">${post.title}</h3>
                    <div class="post-header-button">
                        <button onclick="editPost(${postId})">수정</button>
                        <button onclick="history.back()">글 목록</button>
                        <button class="delete-button" onclick="deletePost(this)">삭제</button>
                    </div>
                </div>
                <div class="post-info">
                    <img src="${defaultProfileImageUrl}" alt="프로필 사진" class="post-profile-image">
                    <div class="post-author-date">
                        <span class="post-author">${post.member_nickname}</span>
                        <span class="post-date">${new Date(post.created_at).toLocaleDateString()}</span>
                    </div>
                </div>
                <p class="post-body">${post.content}</p>
                <div class="post-stats">
                    <div class="post-likes">
                        <button id="like-button" class="like-button">
                            <span class="heart">좋아요</span> 
                            <span id="like-count">${post.total_like}</span>
                        </button>
                    </div>
                    <div class="post-scraps">
                        <button id="scrap-button" onclick="toggleScrap(${postId})">스크랩</button>
                        <span id="scrap-count">${post.total_scrap}</span>
                    </div>
                </div>
            </div>
        `;
        // Like button functionality
        document.getElementById('like-button').addEventListener('click', function() {
            toggleLike(this);
        });
    } else {
        postContent.innerHTML = '<p>게시글을 찾을 수 없습니다.</p>';
    }
}

// 좋아요 버튼 이벤트 반영해주는 함수
function toggleLike(button) {
    const heartIcon = button.querySelector('.heart');
    const likeCount = button.querySelector('#like-count');
    let currentCount = parseInt(likeCount.textContent);

    if (button.classList.contains('active')) {
        button.classList.remove('active');
        heartIcon.textContent = "좋아요";
        likeCount.textContent = currentCount - 1;
    } else {
        button.classList.add('active');
        heartIcon.textContent = "❤️";
        likeCount.textContent = currentCount + 1;
    }
}

// 스크랩 버튼 이벤트 반영해주는 함수
function toggleScrap(postId) {
    const scrapButton = document.getElementById('scrap-button');
    const scrapCount = document.getElementById('scrap-count');
    let currentCount = parseInt(scrapCount.textContent);

    if (scrapButton.classList.contains('active')) {
        scrapButton.classList.remove('active');
        scrapCount.textContent = currentCount - 1;
        alert('스크랩이 취소되었습니다.');
    } else {
        scrapButton.classList.add('active');
        scrapCount.textContent = currentCount + 1;
        alert('스크랩이 추가되었습니다.');
    }
}


// 댓글 달기 기능
function addComment(author, text) {
    const commentsContainer = document.getElementById('comments-container');
    const commentElement = document.createElement('div');
    commentElement.classList.add('comment');
    commentElement.innerHTML = `
        <div class="comment-header">
            <div class="comment-profile">
                <img src="https://via.placeholder.com/40" alt="프로필 사진" class="comment-profile-image">
                <span class="comment-author">${author}</span>님
            </div>  
            
            <span class="comment-text">${text}</span>
            
            <div class="comment-actions">
                <button class="reply-button" onclick="showReplySection(this)">덧글 달기</button>
                <button class="like-button" onclick="toggleLike(this)">
                    <span class="heart">좋아요</span>
                    <span id="like-count">0</span>
                </button>
                <button class="delete-button" onclick="deleteComment(this)">삭제</button>
            </div>
        </div>
        <div class="reply-section">
            <input type="text" class="reply-input" placeholder="덧글을 입력하세요">
            <button class="reply-submit" onclick="submitReply(this)">덧글 등록</button>
        </div>
        <div class="replies-container"></div>
    `;
    commentsContainer.appendChild(commentElement);
}

// 덧글 입력하는 기능 추가해주는 함수
function showReplySection(button) {
    const replySection = button.parentElement.parentElement.nextElementSibling;
    replySection.style.display = 'flex'; // 덧글 입력 영역 표시
}


// 덧글 등록 시 호출되는 함수
function submitReply(button) {
    const replySection = button.parentElement;
    const replyInput = replySection.querySelector('.reply-input');
    const replyText = replyInput.value.trim();
    if (replyText) {
        const repliesContainer = replySection.nextElementSibling;
        const replyElement = document.createElement('div');
        replyElement.classList.add('reply');
        replyElement.innerHTML = `
            <img src="https://via.placeholder.com/40" alt="프로필 사진" class="comment-profile-image">
            <span class="comment-author">사용자닉네임</span>님
            <span class="comment-text">${replyText}</span>
            <div class="reply-actions">
                <button class="like-button" onclick="toggleLike(this)">
                    <span class="heart">좋아요</span>
                    <span id="like-count">0</span>
                </button>
                <button class="delete-button" onclick="deleteReply(this)">삭제</button>
            </div>
        `;
        repliesContainer.appendChild(replyElement);
        replyInput.value = ''; // 입력 필드 비우기
        replySection.style.display = 'none'; // 덧글 입력 영역 숨기기
    }
}

function editPost(postId) {
    console.log(exampleData.find(post=>post.board_id===1));
    const post = exampleData.find(post => post.board_id === postId);
    if (post) {
        window.location.href = `/community/post?mode=update&postId=${postId}&title=${encodeURIComponent(post.title)}&content=${encodeURIComponent(post.content)}`;
    }
}


function deletePost(button) {
    const postDetail = button.closest('.post-detail');
    if (postDetail) {
        postDetail.remove();
        window.location.href = '/community'; // '/community'로 이동
    }
}

function deleteComment(button) {
    const comment = button.closest('.comment');
    if (comment) {
        comment.remove();
    }
}

function deleteReply(button) {
    const reply = button.closest('.reply');
    if (reply) {
        reply.remove();
    }
}