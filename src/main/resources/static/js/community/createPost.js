document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const mode = urlParams.get('mode');
    const postId = urlParams.get('postId');
    const title = urlParams.get('title');
    const content = urlParams.get('content');

    if (mode === 'update') {
        document.querySelector('.create-post-header h2').textContent = '글 수정';
        document.getElementById('post-title').value = decodeURIComponent(title);
        document.getElementById('post-content').value = decodeURIComponent(content);
        document.querySelector('.post-submit-btn').textContent = '수정';
        document.querySelector('.post-submit-btn').onclick = function() {
            updatePost(postId);
        };
    }
});

function submitPost() {
    const title = document.getElementById('post-title').value.trim();
    const content = document.getElementById('post-content').value.trim();
    let isValid = true;

    if (!title) {
        document.getElementById('title-error').textContent = '글 제목을 입력하세요.';
        document.getElementById('title-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('title-error').style.display = 'none';
    }

    if (!content) {
        document.getElementById('content-error').textContent = '글 내용을 입력하세요.';
        document.getElementById('content-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('content-error').style.display = 'none';
    }

    if (isValid) {
        // Form data to be added to exampleData
        const postData = {
            board_id: exampleData.length + 1,
            title: title,
            content: content,
            member_nickname: '사용자', // Example user
            created_at: new Date().toISOString(),
            updated_at: new Date().toISOString(),
            total_like: 0,
            total_scrap: 0
        };

        exampleData.push(postData);
        console.log(exampleData);
        // For now, simulate successful submission
        console.log('Post submitted:', postData);
        window.location.href = '/community';
    }
}

function updatePost(postId) {
    const title = document.getElementById('post-title').value.trim();
    const content = document.getElementById('post-content').value.trim();
    let isValid = true;

    if (!title) {
        document.getElementById('title-error').textContent = '글 제목을 입력하세요.';
        document.getElementById('title-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('title-error').style.display = 'none';
    }

    if (!content) {
        document.getElementById('content-error').textContent = '글 내용을 입력하세요.';
        document.getElementById('content-error').style.display = 'block';
        isValid = false;
    } else {
        document.getElementById('content-error').style.display = 'none';
    }

    if (isValid) {
        // Find the post in exampleData and update it
        const postIndex = exampleData.findIndex(post => post.board_id == postId);
        if (postIndex !== -1) {
            exampleData[postIndex].title = title;
            exampleData[postIndex].content = content;
            exampleData[postIndex].updated_at = new Date().toISOString();

            // For now, simulate successful update
            window.location.href = '/community';
        }
    }
}

// 게시글 보기 -> 서버랑 통신할 수 있게 바꿔야 하는 함수
function showPostDetail(postId) {
    const postContent = document.getElementById('post-content');

    const defaultProfileImageUrl = "https://via.placeholder.com/100"; // 기본 이미지 URL

    // 해당 게시글 데이터 가져오기 (예제 데이터 사용)
    const post = exampleData.find(post => post.board_id === postId);

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

// 글 수정 페이지로 이동하는 함수
function editPost(postId) {
    const post = exampleData.find(post => post.board_id === postId);
    if (post) {
        const url = `/community/post?mode=update&postId=${postId}&title=${encodeURIComponent(post.title)}&content=${encodeURIComponent(post.content)}`;
        window.location.href = url;
    }
}
