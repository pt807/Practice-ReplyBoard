// let token = $("meta[name='_csrf']").attr("content");
// let header = $("meta[name='_csrf_header']").attr("content");


const boardId = document.getElementById('postId').value;
const tbody = document.querySelector('tbody');

//페이지 로드시 댓글 불러오기
document.addEventListener('DOMContentLoaded', () => {
    getReply(boardId);
});

document.getElementById('reply-form').addEventListener('submit', async (e) => {
    e.preventDefault(); //새로 실생되는것을 막아줌 submit은 작동됨
    const content = e.target.content.value;
    if (!content) {
        return alert('댓글을 입력해주세요');
    }
    await fetch(`/reply/${boardId}`, {
        method: 'post',
        headers: {
            'content-type': 'application/json',
        },
        body: JSON.stringify({
            content: content
        }),
    })
        .then(() => getReply(boardId))
        .catch((err) => {
            console.log(err);
        });
    e.target.content.value = '';
});

//댓글 새로고침 버튼 이벤트
document.getElementById('replyRefresh').addEventListener('click', () => {
    getReply(boardId);
});

//대 댓글, 수정, 삭제 버튼 이벤트

document.querySelector('tbody').addEventListener('click', (e) => {
    let button = e.target;
    if (button.tagName != 'BUTTON' || !button.dataset.action) {
        return;
    }
    //각 버튼의 action에 맞는 로직 호출
    if (button.dataset.action == 'showCommentForm') {
        showCommentForm(e);
    } else if (button.dataset.action == 'removeCommentForm') {
        removeCommentForm(e);
    } else if (button.dataset.action == 'showUpdateForm') {
        showUpdateForm(e);
    } else if (button.dataset.action == 'removeUpdateForm') {
        removeUpdateForm(e);
    } else if (button.dataset.action == 'delete') {
        deleteReply(e);
    }
})

function showCommentForm(e) {
    let CommentButton = e.target;
    CommentButton.dataset.action = 'removeCommentForm';
    CommentButton.innerText = '답글취소';

    let current_row = CommentButton.closest('tr');
    let row = document.createElement('tr');
    row.setAttribute('class', 'text-center');
    row.style.backgroundColor = 'rgba(0,0,0,0.05)';
    let td = document.createElement('td');
    let span = document.createElement('span');

    // 댓글 들여쓰기
    row.appendChild(td);
    td = document.createElement('td');
    span.innerText = '↳';
    td.appendChild(span);
    row.appendChild(td);

    // 답글 textarea
    let textarea = document.createElement('textarea');
    textarea.setAttribute('id', 'content');
    textarea.setAttribute('placeholder', '이곳에 댓글을 입력해주세요');
    td = document.createElement('td');
    td.setAttribute('colspan', 4);
    td.appendChild(textarea);
    row.appendChild(td);

    //작성 버튼
    td = document.createElement('td');
    let button = document.createElement('button');
    button.setAttribute('class', 'btn btn-info');
    button.innerText = '작성';
    button.addEventListener('click', () => {
        fetch(`/reply/${boardId}`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                content: textarea.value,
                parent: CommentButton.dataset.replyId
            }),
        })
            .then(() => getReply(boardId))
            .catch((err) => {
                console.log(err);
            });
    });
    td.appendChild(button);
    row.appendChild(td);

    current_row.after(row);
}

//대 댓글 폼 삭제 로직
function removeCommentForm(e) {
    let CommentButton = e.target;
    CommentButton.dataset.action = 'showCommentForm';
    CommentButton.innerText = '답글';

    let current_row = CommentButton.closest('tr');
    let comment_row = current_row.nextSibling; // 다음에 있는것 가져오기
    tbody.removeChild(comment_row);
}

//댓글 수정폼 추가 로직
function showUpdateForm(e) {
    const updateButton = e.target;
    updateButton.dataset.action = 'removeUpdateForm';
    updateButton.setAttribute('class', 'btn btn-secondary')
    updateButton.innerText = '수정취소';

    const replyId = updateButton.dataset.replyId;
    const replyContent = document.getElementById(`replyContent_${replyId}`);
    replyContent.style.display = 'none';

    const editDiv = document.getElementById(`edit_${replyId}`);
    const form = document.createElement('form');

    const textarea = document.createElement('textarea');
    textarea.setAttribute('id', 'content');
    textarea.innerText = replyContent.textContent;
    form.appendChild(textarea);

    const button = document.createElement('button');
    button.setAttribute('type', 'submit');
    button.setAttribute('class', 'btn btn-info');
    button.innerText = '수정하기';
    form.appendChild(button);

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const content = e.target.content.value;
        if (!content) {
            return alert('수정할 내용을 입력해주세요!');
        }
        updateReply(replyId, content);
    });
    editDiv.appendChild(form);
}

//댓글 수정폼 삭제 로직
function removeUpdateForm(e) {
    const updateButton = e.target;
    updateButton.dataset.action = 'showUpdateForm';
    updateButton.setAttribute('class', 'btn btn-info');
    updateButton.innerText = '수정';

    const replyId = updateButton.dataset.replyId;
    const replyContent = document.getElementById(`replyContent_${replyId}`);
    replyContent.removeAttribute('style'); //주어진 이름의 특성을 제거

    const editDiv = document.getElementById(`edit_${replyId}`);
    while (editDiv.hasChildNodes()) { //자식 노드가 있는지 boolean 값으로 반환
        editDiv.removeChild(editDiv.firstChild);
    }
}

//댓글 삭제 로직
function deleteReply(e) {
    const replyId = e.target.dataset.replyId;
    const deleteConfirm = confirm('삭제 하시겠습니까?');
    if (!deleteConfirm) {
        return;
    }
    fetch(`/reply/delete/${replyId}`, {
        method: 'delete',
    })
        .then(() => {
            alert('댓글이 삭제 되었습니다.');
            getReply(boardId);
        })
        .catch((err) => {
            console.log(err);
        })
}

//댓글 수정 로직
function updateReply(replyId, content) {
    fetch(`/reply/edit/${replyId}`, {
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            content: content
        }),
    })
        .then(() => {
            alert('댓글이 수정되었습니다.');
            getReply(boardId);
        })
        .catch((err) => {
            console.log(err);
        });
}

//댓글 새로고침 로직
async function getReply(id) {

    try {
        const replies = await fetch(`/board/${id}/replies`, {
            method: 'get',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then((res) => res.json());
        tbody.innerHTML = '';

        //댓글 생성
        replies.map(function (reply) {
            createReplyRow(reply);

            reply.child.map(function (childReply) {
                createReplyRow(childReply);
            });
        });
    } catch (err) {
        console.log(err);
    }
}

//댓글 DOM 동적 생성
function createReplyRow(reply) {
    // const 타입 Element 변수 생성(하나만 존재하므로 재할당 필요X)
    const row = document.createElement('tr');
    row.setAttribute('class', 'text-center');

    const th = document.createElement('th');
    // let 타입 Element 변수 생성(여럿 존재하므로 재할당하여 사용하고자 함)
    let span = document.createElement('span');
    let td = document.createElement('td');
    let a = document.createElement('a');
    let button = document.createElement('button');
    // 댓글/대댓글 분류
    if (!reply.parent) {    // 댓글
        // 댓글 id th Element
        th.setAttribute('scope', 'row');
        th.setAttribute('colspan', 2);
        span.textContent = reply.id;
        th.appendChild(span);
        row.appendChild(th);
    } else {    // 대댓글
        row.appendChild(td);
        td = document.createElement('td');
        span.textContent = "↳";
        td.appendChild(span);
        row.appendChild(td);

        // 댓글 id th Element
        td = document.createElement('td');
        span = document.createElement('span');
        span.textContent = reply.id;
        span.style.fontWeight = 'bold';
        td.appendChild(span);
        row.appendChild(td);
        row.style.backgroundColor = 'rgba(0,0,0,0.05)';
    }

    // 댓글 content td Element
    td = document.createElement('td');
    span = document.createElement('span');
    span.setAttribute('id', `replyContent_${reply.id}`);
    span.textContent = reply.content;
    td.appendChild(span);
    const div = document.createElement('div');
    div.setAttribute('id', `edit_${reply.id}`)
    td.appendChild(div);
    row.appendChild(td);

    // 댓글 createdDate td Element
    td = document.createElement('td');
    span = document.createElement('span');
    span.textContent = reply.createdDate;
    td.appendChild(span);
    row.appendChild(td);

    // 댓글 답글 버튼 td Element
    if (!reply.parent) {
        td = document.createElement('td');
        button.setAttribute('class', 'btn btn-outline-secondary');
        button.setAttribute('data-action', 'showCommentForm');
        button.setAttribute('data-reply-id', reply.id);
        button.innerText = '답글';
        td.appendChild(button);
        row.appendChild(td);
    }

    // 댓글 수정 버튼 td Element
    td = document.createElement('td');
    button = document.createElement('button');
    button.setAttribute('class', 'btn btn-info');
    button.setAttribute('data-action', 'showUpdateForm');
    button.setAttribute('data-reply-id', reply.id);
    button.innerText = '수정';
    td.appendChild(button);
    row.appendChild(td);

    // 댓글 삭제 버튼 td Element
    td = document.createElement('td');
    button = document.createElement('button');
    button.setAttribute('class', 'btn btn-danger');
    button.setAttribute('data-action', 'delete');
    button.setAttribute('data-reply-id', reply.id);
    button.innerText = '삭제';
    td.appendChild(button);
    row.appendChild(td);

    // tbody에 댓글 추가
    tbody.appendChild(row);
}



