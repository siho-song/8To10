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
        // Form data to be sent to the server or stored
        const postData = {
            title: title,
            content: content
        };

        // Send the data to the server (implementation depends on the server setup)
        // Example using fetch:
        // fetch('/api/posts', {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify(postData)
        // }).then(response => {
        //     if (response.ok) {
        //         window.location.href = '/community';
        //     } else {
        //         alert('글 등록에 실패했습니다.');
        //     }
        // });

        // For now, simulate successful submission
        console.log('Post submitted:', postData);
        window.location.href = '/community';
    }
}