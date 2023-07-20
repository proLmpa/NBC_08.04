const host = 'http://' + window.location.host;

$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: `/api/post`,
        contentType: 'application/json',
        success: function(response) {
            console.log(response)
            $('#container').empty()

            const postList = response['posts']
            console.log('###' + postList)

            for (let i = 0; i < postList.length; i++) {
                let postDto = postList[i]
                let tempHtml = addHtml(postDto)
                $('#container').append(tempHtml)

                let postId = postDto['id']; // postId 정의


                let postPosition = '.postDto-' + postId + '-tags';
                for (let j = 0; j < postDto['tags'].length; j++) {
                    let tempTag = postDto['tags'][j]['tag'];
                    $(postPosition).append('#' + tempTag + ' ');
                }

                // 메인페이지 로드시 DB상 댓글 자동삽입
                let replyList = postDto['replies'];
                let replyDto = $('.replyDto-' + postId);
                if (replyList !== null) {
                    for (let j = 0; j < replyList.length; j++) {
                        let reply = replyList[j];
                        let tempReplyHtml = addReplyHtml(reply);
                        replyDto.append(tempReplyHtml);
                    }
                }
            }
        },
        error(error, status, request) {
            console.log(error.valueOf());
        }
    });
});


//게시글 html 출력
function addHtml(postDto) {
    return `<div class="postDto-box postDto-${postDto.id}">
                <div class="postDto-header">
                    <div class="postDto-nickname">${postDto.nickname}</div>
                    <div class="postDto-createdAt">createdAt: ${postDto['createdAt']}</div>
                    <div class="postDto-modifiedAt">modifiedAt: ${postDto['modifiedAt']}</div>
                    <div class="postDto-update-btn" onclick="displayUpdateBox(${postDto.id})">수정</div>
                    <div class="postDto-delete-btn" onclick="deletePost(${postDto.id})">삭제</div>
                </div> 
                <div class="postDto-divider"></div>
                <div class="postDto-body">
                    <div class="postDto-title">${postDto.title}</div>
                    <div class="postDto-content">${postDto.content}</div><br>
                    <div class="postDto-tags postDto-tag-${postDto.id}">Tags: </div>
                </div>            
                <div class="postDto-divider"></div>
                <div class="postDto-footer">
                    <div class="postDto-postLike postDto-postLike-${postDto.id}" onclick="likePost(${postDto.id})"> 좋아요 ${postDto['countPostLike']}</div>
                </div>
                <br><br>
                <div class="replyDto-box-${postDto.id}"">
                    <input type="text" id="replyDto-input" placeholder="ReplyContent">
                    <button class="replyDto-btn" onclick="writeReply(${postDto.id})">댓글 달기 </button><br>
                </div>
                <div class="replyDto-${postDto.id}"></div>
            </div><br>`
}

function addReplyHtml(replyDto) {
    return `<br><hr>
    <div class="replyDto-box replyDto-${replyDto.id}">
        <div class="replyDto-nickname">${replyDto.nickname}</div>
        <div class="replyDto-createdAt">createdAt: ${replyDto.createdAt}</div>
        <div class="replyDto-modifiedAt">modifiedAt: ${replyDto.modifiedAt}</div>
        <div class="replyDto-content">${replyDto.content}</div>
        <div class="replyDto-replyLike-${replyDto.id}" onclick="likeReply(${replyDto.id})"> 좋아요 ${replyDto['countReplyLike']}</div>
        <br>
        <input type="text" id="replyDto-updateInput-${replyDto.id}" placeholder="ReplyUpdateContent">
        <br>
        <div class="replyDto-btn">
            <button onclick="updateReply(${replyDto.id})">댓글 수정</button>
            <button onclick="deleteReply(${replyDto.id})">댓글 삭제</button>
            <button onclick="likeReply(${replyDto.id})">댓글 좋아요</button>
        </div>
    </div>`;
}


/* 게시글 관련 함수 */

function writePost() {
    const auth = getToken()

    if(auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth)
        })
    } else {
        alert('로그인한 유저만 작성 가능합니다!')
        window.location.href = host + '/api/user/login-page'
        return
    }

    let check = confirm("이대로 게시글을 작성하시겠습니까?")

    if(check === true) {
        let title = $('#title').val()
        let content = $('#content').val()

        $.ajax({
            type: "POST",
            url: `/api/post`,
            contentType: "application/json",
            data: JSON.stringify({title: title, content: content}),
        })
            .done(function(res) {
                alert('게시글을 등록했습니다!')
                window.location.href = host
            })
            .fail(function() {
                alert('게시글 등록에 실패했습니다.')
            })
    }
}


function displayUpdateBox(postId) {
    hideUpdateBox()

    let updateHtml = `
        <div class="post-title">
            <input type="text" id="title-update" placeholder="Update Title" class="post-update-element">
        </div>
        <div class="post-content">
            <input type="text" id="content-update" placeholder="Update Content" class="post-update-element">
        </div>
        <button id="post-update-submit" onclick="updatePost(${postId})">수정하기</button>    
        <button id="post-update-cancel" onclick="hideUpdateBox()">취소하기</button>
    `

    $('#post-update-box').append(updateHtml)

    const updatePostDiv = document.getElementById('post-update-box')
    updatePostDiv.style.display = 'block'
}

function hideUpdateBox() {
    $("#post-update-box").empty()
}

function updatePost(postId) {
    const auth = getToken()

    if(auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth)
        })
    } else {
        alert('로그인한 유저만 수정 가능합니다!')
        window.location.href = host + '/api/user/login-page'
        return
    }

    let check = confirm("이대로 게시글을 수정하시겠습니까?")

    if(check === true) {
        let title = $('#title-update').val()
        let content = $('#content-update').val()

        $.ajax({
            type: "PUT",
            url: `/api/post/${postId}`,
            contentType: "application/json",
            data: JSON.stringify({title: title, content: content}),
        })
            .done(function(res) {
                alert('게시글을 수정했습니다!')
                window.location.href = host
            })
            .fail(function() {
                alert('게시글 수정에 실패했습니다.')
                window.location.href = host
            })
    }
}

function deletePost(postId) {
    const auth = getToken()

    if(auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth)
        })
    } else {
        alert('로그인한 유저만 수정 가능합니다!')
        window.location.href = host + '/api/user/login-page'
        return
    }

    let check = confirm("정말 게시글을 삭제하시겠습니까?")

    if(check === true) {

        $.ajax({
            type: "DELETE",
            url: `/api/post/${postId}`,
            contentType: "application/json"
        })
            .done(function(res) {
                alert('게시글을 삭제했습니다!')
                window.location.href = host
            })
            .fail(function() {
                alert('게시글 삭제에 실패했습니다.')
            })
    }
}


/* 댓글 관련 함수 */

function writeReply(postId) {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        alert('로그인한 유저만 작성 가능합니다!');
        window.location.href = host + '/api/user/login-page';
        return;
    }

    let check = confirm('이대로 댓글을 작성하시겠습니까?');

    if (check === true) {
        let replyContent = $('#replyDto-input').val();

        $.ajax({
            type: 'POST',
            url: `/api/reply/${postId}`,
            contentType: 'application/json',
            data: JSON.stringify({ content: replyContent }),
        })
            .done(function (res) {
                alert('댓글을 등록했습니다!');
                let replyDto = $('.replyDto-' + postId);
                let replyHtml = addReplyHtml(res);

                replyDto.empty();
                location.reload(); //develop 예정

                $('.postDto-' + postId + ' .reply-content').val('');
            })
            .fail(function () {
                alert('댓글 등록에 실패했습니다.');
            });
    }
}


function updateReply(replyId) {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        alert('로그인한 유저만 수정 가능합니다!');
        window.location.href = host + '/api/user/login-page';
        return;
    }

    let check = confirm('댓글을 수정하시겠습니까?');

    if (check === true) {
        let updatedContent = $('#replyDto-updateInput-' + replyId).val();

        $.ajax({
            type: 'PUT',
            url: `/api/reply/${replyId}`,
            contentType: 'application/json',
            data: JSON.stringify({ content: updatedContent }),
        })
            .done(function (res) {
                alert('댓글을 수정했습니다!');
                $('.replyDto-' + replyId + ' .replyDto-content').text(res.content);
            })
            .fail(function () {
                alert('댓글 수정에 실패했습니다.');
            });
    }
}

function deleteReply(replyId) {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        alert('로그인한 유저만 삭제 가능합니다!');
        window.location.href = host + '/api/user/login-page';
        return;
    }

    let check = confirm('댓글을 삭제하시겠습니까?');

    if (check === true) {
        $.ajax({
            type: 'DELETE',
            url: `/api/reply/${replyId}`,
            contentType: 'application/json',
        })
            .done(function (res) {
                alert('댓글을 삭제했습니다!');
                location.reload();
                //loadReplies(); -> 일부만 로드되는 함수 만들어봤지만 작동 無
                //$('.replyDto-' + replyId).remove(); -> 이러면 댓글 여러 개 삭제시 공간이 나버림..
            })
            .fail(function () {
                alert('댓글 삭제에 실패했습니다.');
            });
    }
}

function likeReply(replyId) {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        alert('로그인한 유저만 좋아요를 누를 수 있습니다!');
        window.location.href = host + '/api/user/login-page';
        return;
    }

    let check = confirm('이대로 댓글에 좋아요를 누르시겠습니까?');

    if (check === true) {
        $.ajax({
            type: 'POST',
            url: `/api/reply/${replyId}/like`,
            contentType: 'application/json',
        })
            .done(function (res) {
                alert('댓글에 좋아요를 눌렀습니다!');
                location.reload();
            })
            .fail(function (xhr, status, error) {
                if (xhr.status === 400) {
                    alert('이미 좋아요를 누른 댓글입니다.');
                } else {
                    alert('댓글에 좋아요를 누르는데 실패했습니다.');
                }
            });
    }
}



/* 회원 관련 함수 */

function login() {
    window.location.href = host + '/api/user/login-page'
}

function signup() {
    window.location.href = host + '/api/user/signup'
}

function logout() {
    // 토큰 삭제
    Cookies.remove("Authorization", {path: '/'});
    window.location.href = host + '/'
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if(auth === undefined) {
        return '';
    }

    // 소셜 로그인 사용한 경우 Bearer 추가
    if(auth.indexOf('Bearer') === -1 && auth !== ''){
        auth = 'Bearer ' + auth;
    }

    return auth;
}