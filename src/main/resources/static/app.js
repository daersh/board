// API Base URL
const API_BASE_URL = '/api';

// Utility function for authenticated API requests
const fetchWithAuth = async (url, options = {}) => {
    // No need to manually add Authorization header for httpOnly cookies
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers,
    };

    const response = await fetch(url, { ...options, headers });

    // Handle token expiration or invalid token (401 Unauthorized)
    if (response.status === 401) {
        // Attempt to logout on the server to clear any invalid cookie
        await fetch(`${API_BASE_URL}/login/logout`, { method: 'POST' });
        alert('세션이 만료되었거나 유효하지 않은 접근입니다. 다시 로그인해주세요.');
        window.location.href = '/login.html';
        return null; // Indicate failure
    }

    return response;
};

// Function to display messages to the user
const showMessage = (element, message, isError = false) => {
    element.textContent = message;
    element.className = isError ? 'message error' : 'message';
    element.style.display = 'block';
};

// Function to check login status using /api/user/info
const checkLoginStatus = async () => {
    try {
        const response = await fetchWithAuth(`${API_BASE_URL}/user/info`);
        if (response && response.ok) {
            return await response.json(); // Returns user info if logged in
        }
        return null; // Not logged in or error
    } catch (error) {
        console.error('Error checking login status:', error);
        return null;
    }
};

// Function to handle logout
const logout = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/login/logout`, { method: 'POST' });
        if (response.ok) {
            alert('로그아웃 되었습니다.');
            window.location.href = '/index.html';
        } else {
            alert('로그아웃 실패.');
        }
    } catch (error) {
        console.error('Error during logout:', error);
        alert('네트워크 오류로 로그아웃에 실패했습니다.');
    }
};


// --- Page Specific Logic ---

// Register Page Logic
const handleRegisterPage = () => {
    const registerForm = document.getElementById('register-form');
    const messageDiv = document.querySelector('#register-form + .message');

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;

            try {
                const response = await fetch(`${API_BASE_URL}/user`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ username, password, name, email }),
                });

                if (response.ok) {
                    showMessage(messageDiv, '회원가입 성공! 로그인 페이지로 이동합니다.');
                    setTimeout(() => {
                        window.location.href = '/login.html';
                    }, 1500);
                } else {
                    const errorData = await response.json();
                    showMessage(messageDiv, `회원가입 실패: ${errorData.message || response.statusText}`, true);
                }
            } catch (error) {
                showMessage(messageDiv, `네트워크 오류: ${error.message}`, true);
            }
        });
    }
};

// Login Page Logic
const handleLoginPage = () => {
    const loginForm = document.getElementById('login-form');
    const messageDiv = document.querySelector('#login-form + .message');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value; // Changed from username to email
            const password = document.getElementById('password').value;

            try {
                const response = await fetch(`${API_BASE_URL}/login`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ email, password }), // Changed from username to email
                });

                if (response.ok) {
                    // Token is set as httpOnly cookie by backend, no need to handle in JS
                    showMessage(messageDiv, '로그인 성공! 메인 페이지로 이동합니다.');
                    setTimeout(() => {
                        window.location.href = '/index.html';
                    }, 1500);
                } else {
                    const errorData = await response.json();
                    showMessage(messageDiv, `로그인 실패: ${errorData.message || response.statusText}`, true);
                }
            } catch (error) {
                showMessage(messageDiv, `네트워크 오류: ${error.message}`, true);
            }
        });
    }
};

// Index Page Logic
const handleIndexPage = async () => {
    const boardList = document.getElementById('board-list');
    const navBar = document.getElementById('nav-bar');
    const paginationDiv = document.getElementById('pagination');
    const loggedInUser = await checkLoginStatus(); // Check login status

    // Update navigation bar based on login status
    if (navBar) {
        navBar.innerHTML = ''; // Clear existing content
        if (loggedInUser) {
            const writeLink = document.createElement('a');
            writeLink.href = '/write.html';
            writeLink.className = 'btn';
            writeLink.textContent = '새 글 작성';
            navBar.appendChild(writeLink);

            const logoutBtn = document.createElement('button');
            logoutBtn.className = 'btn btn-secondary';
            logoutBtn.textContent = '로그아웃';
            logoutBtn.addEventListener('click', logout); // Use the new logout function
            navBar.appendChild(logoutBtn);
        } else {
            const loginLink = document.createElement('a');
            loginLink.href = '/login.html';
            loginLink.className = 'btn';
            loginLink.textContent = '로그인';
            navBar.appendChild(loginLink);

            const registerLink = document.createElement('a');
            registerLink.href = '/register.html';
            registerLink.className = 'btn btn-secondary';
            registerLink.textContent = '회원가입';
            navBar.appendChild(registerLink);
        }
    }

    // Fetch and render board posts
    const fetchPosts = async (page = 0, size = 10) => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/board?page=${page}&size=${size}`);
            if (!response || !response.ok) {
                boardList.innerHTML = '<tr><td colspan="4">게시글을 불러오는데 실패했습니다.</td></tr>';
                return;
            }
            const pageResponse = await response.json(); // Assuming PageResponse DTO
            const posts = pageResponse.content;

            boardList.innerHTML = ''; // Clear existing posts
            if (posts && posts.length > 0) {
                posts.forEach(post => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${post.id}</td>
                        <td><a href="/detail.html?id=${post.id}">${post.title}</a></td>
                        <td>${post.nickname}</td>
                        <td>${new Date(post.createdAt).toLocaleDateString()}</td>
                    `;
                    boardList.appendChild(row);
                });
                renderPagination(pageResponse, fetchPosts);
            } else {
                boardList.innerHTML = '<tr><td colspan="4">게시글이 없습니다.</td></tr>';
                paginationDiv.innerHTML = '';
            }
        } catch (error) {
            console.error('Error fetching posts:', error);
            boardList.innerHTML = '<tr><td colspan="4">게시글을 불러오는 중 오류가 발생했습니다.</td></tr>';
        }
    };

    const renderPagination = (pageResponse, fetchFn) => {
        paginationDiv.innerHTML = '';
        const totalPages = pageResponse.totalPages;
        const currentPage = pageResponse.number; // 0-indexed

        for (let i = 0; i < totalPages; i++) {
            const button = document.createElement('button');
            button.textContent = i + 1;
            button.className = (i === currentPage) ? 'active' : '';
            button.addEventListener('click', () => fetchFn(i));
            paginationDiv.appendChild(button);
        }
    };

    fetchPosts();
};

// Detail Page Logic
const handleDetailPage = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get('id');
    const postDetailDiv = document.getElementById('post-detail');
    const postActionsDiv = document.getElementById('post-actions');
    const commentListDiv = document.getElementById('comment-list');
    const commentForm = document.getElementById('comment-form');
    const commentContentInput = document.getElementById('comment-content');
    const loggedInUser = await checkLoginStatus(); // Check login status

    if (!boardId) {
        postDetailDiv.innerHTML = '<p>잘못된 게시글 ID입니다.</p>';
        return;
    }

    // Fetch post details
    const fetchPost = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/board/${boardId}`);
            if (!response || !response.ok) {
                postDetailDiv.innerHTML = '<p>게시글을 불러오는데 실패했습니다.</p>';
                return null;
            }
            const post = await response.json();
            renderPost(post);
            return post;
        } catch (error) {
            console.error('Error fetching post:', error);
            postDetailDiv.innerHTML = '<p>게시글을 불러오는 중 오류가 발생했습니다.</p>';
            return null;
        }
    };

    // Render post details
    const renderPost = (post) => {
        postDetailDiv.innerHTML = `
            <h2>${post.title}</h2>
            <p class="post-meta">작성자: ${post.nickname} | 작성일: ${new Date(post.createdAt).toLocaleString()}</p>
            <p>${post.content}</p>
        `;

        // Show edit/delete buttons only if logged in and is the author
        if (loggedInUser && loggedInUser.nickname === post.nickname) { // Compare nicknames
            postActionsDiv.innerHTML = `
                <a href="/edit.html?id=${post.id}" class="btn">수정</a>
                <button id="delete-post-btn" class="btn btn-danger">삭제</button>
            `;
            document.getElementById('delete-post-btn').addEventListener('click', handleDeletePost);
        } else {
            postActionsDiv.innerHTML = '';
        }
    };

    // Handle post deletion
    const handleDeletePost = async () => {
        if (!confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            return;
        }
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/board/${boardId}`, {
                method: 'DELETE',
            });
            if (response && response.ok) {
                alert('게시글이 삭제되었습니다.');
                window.location.href = '/index.html';
            } else {
                const errorData = response ? await response.json() : { message: '알 수 없는 오류' };
                alert(`게시글 삭제 실패: ${errorData.message || response.statusText}`);
            }
        } catch (error) {
            console.error('Error deleting post:', error);
            alert(`네트워크 오류: ${error.message}`);
        }
    };

    // Fetch and render comments
    const fetchComments = async () => {
        try {
            // Corrected endpoint and parameters
            const response = await fetchWithAuth(`${API_BASE_URL}/board/comment?boardId=${boardId}&page=0`); // Assuming page 0 for comments
            if (!response || !response.ok) {
                commentListDiv.innerHTML = '<p>댓글을 불러오는데 실패했습니다.</p>';
                return;
            }
            const comments = await response.json();
            commentListDiv.innerHTML = ''; // Clear existing comments
            if (comments && comments.length > 0) {
                comments.forEach(comment => {
                    const commentDiv = document.createElement('div');
                    commentDiv.className = 'comment';
                    commentDiv.innerHTML = `
                        <p class="comment-meta">작성자: ${comment.nickname} | 작성일: ${new Date(comment.createdAt).toLocaleString()}</p>
                        <p class="comment-content">${comment.content}</p>
                    `;
                    commentListDiv.appendChild(commentDiv);
                });
            } else {
                commentListDiv.innerHTML = '<p>댓글이 없습니다.</p>';
            }
        } catch (error) {
            console.error('Error fetching comments:', error);
            commentListDiv.innerHTML = '<p>댓글을 불러오는 중 오류가 발생했습니다.</p>';
        }
    };

    // Handle comment submission
    if (commentForm && loggedInUser) { // Only show comment form if logged in
        commentForm.style.display = 'block';
        commentForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const content = commentContentInput.value;
            if (!content.trim()) {
                alert('댓글 내용을 입력해주세요.');
                return;
            }

            try {
                const response = await fetchWithAuth(`${API_BASE_URL}/board/comment`, { // Corrected endpoint
                    method: 'POST',
                    body: JSON.stringify({
                        content,
                        type: 'COMMENT', // Assuming default type
                        targetId: boardId, // Assuming targetId is the boardId
                        boardId: boardId // Pass boardId in body
                    }),
                });

                if (response && response.ok) {
                    commentContentInput.value = ''; // Clear input
                    fetchComments(); // Refresh comments
                } else {
                    const errorData = response ? await response.json() : { message: '알 수 없는 오류' };
                    alert(`댓글 작성 실패: ${errorData.message || response.statusText}`);
                }
            } catch (error) {
                console.error('Error submitting comment:', error);
                alert(`네트워크 오류: ${error.message}`);
            }
        });
    } else if (commentForm) {
        commentForm.style.display = 'none'; // Hide comment form if not logged in
    }


    // Initial load
    await fetchPost();
    await fetchComments();
};

// Write Page Logic
const handleWritePage = async () => {
    const writeForm = document.getElementById('write-form');
    const messageDiv = document.querySelector('#write-form + .message');
    const loggedInUser = await checkLoginStatus();

    if (!loggedInUser) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login.html';
        return;
    }

    if (writeForm) {
        writeForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const title = document.getElementById('title').value;
            const content = document.getElementById('content').value;

            try {
                const response = await fetchWithAuth(`${API_BASE_URL}/board`, {
                    method: 'POST',
                    body: JSON.stringify({ title, content }),
                });

                if (response && response.ok) {
                    showMessage(messageDiv, '게시글이 성공적으로 작성되었습니다. 메인 페이지로 이동합니다.');
                    setTimeout(() => {
                        window.location.href = '/index.html';
                    }, 1500);
                } else {
                    const errorData = response ? await response.json() : { message: '알 수 없는 오류' };
                    showMessage(messageDiv, `게시글 작성 실패: ${errorData.message || response.statusText}`, true);
                }
            } catch (error) {
                showMessage(messageDiv, `네트워크 오류: ${error.message}`, true);
            }
        });
    }
};

// Edit Page Logic
const handleEditPage = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get('id');
    const editForm = document.getElementById('edit-form');
    const titleInput = document.getElementById('title');
    const contentInput = document.getElementById('content');
    const boardIdInput = document.getElementById('boardId');
    const messageDiv = document.querySelector('#edit-form + .message');
    const loggedInUser = await checkLoginStatus();

    if (!loggedInUser) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login.html';
        return;
    }

    if (!boardId) {
        showMessage(messageDiv, '잘못된 게시글 ID입니다.', true);
        return;
    }

    // Fetch existing post data
    const fetchPostForEdit = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/board/${boardId}`);
            if (!response || !response.ok) {
                showMessage(messageDiv, '게시글 정보를 불러오는데 실패했습니다.', true);
                return;
            }
            const post = await response.json();
            // Check if the logged-in user is the author before allowing edit
            if (loggedInUser.nickname !== post.nickname) {
                alert('게시글 수정 권한이 없습니다.');
                window.location.href = `/detail.html?id=${boardId}`;
                return;
            }
            titleInput.value = post.title;
            contentInput.value = post.content;
            boardIdInput.value = post.id; // Use post.id
        } catch (error) {
            console.error('Error fetching post for edit:', error);
            showMessage(messageDiv, `게시글 정보를 불러오는 중 오류가 발생했습니다: ${error.message}`, true);
        }
    };

    if (editForm) {
        await fetchPostForEdit(); // Load data when page loads

        editForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const title = titleInput.value;
            const content = contentInput.value;
            const id = boardIdInput.value; // Get id from hidden input

            try {
                const response = await fetchWithAuth(`${API_BASE_URL}/board`, { // Corrected endpoint
                    method: 'PUT',
                    body: JSON.stringify({ id, title, content }), // Added id to body
                });

                if (response && response.ok) {
                    showMessage(messageDiv, '게시글이 성공적으로 수정되었습니다. 상세 페이지로 이동합니다.');
                    setTimeout(() => {
                        window.location.href = `/detail.html?id=${boardId}`;
                    }, 1500);
                } else {
                    const errorData = response ? await response.json() : { message: '알 수 없는 오류' };
                    showMessage(messageDiv, `게시글 수정 실패: ${errorData.message || response.statusText}`, true);
                }
            } catch (error) {
                showMessage(messageDiv, `네트워크 오류: ${error.message}`, true);
            }
        });
    }
};


// --- Router ---
const router = () => {
    const path = window.location.pathname;

    if (path === '/register.html') {
        handleRegisterPage();
    } else if (path === '/login.html') {
        handleLoginPage();
    } else if (path === '/index.html' || path === '/') {
        handleIndexPage();
    } else if (path === '/detail.html') {
        handleDetailPage();
    } else if (path === '/write.html') {
        handleWritePage();
    } else if (path === '/edit.html') {
        handleEditPage();
    }
};

// Initialize router on DOMContentLoaded
document.addEventListener('DOMContentLoaded', router);