const host = 'http://' + window.location.host;

$(document).ready(function () {
    let auth = Cookies.get('Authorization')
    if(auth === undefined) auth = ''

    if (auth !== '') {
        // 로그인된 상태
        document.getElementById('auth-exist').style.display = 'block';
        document.getElementById('no-auth-exist').style.display = 'none';

        // 로그인한 사용자에 대한 AJAX GET 요청
        $.ajax({
            type: 'GET',
            url: `/api/post`,
            contentType: 'application/json',
            success: function (response) {
                console.log(response);

                const resList = response['posts'];
                for (let i = 0; i < resList.length; i++) {
                    const postDto = resList[i];
                    const tempHtml = addHtml(postDto);
                    $('#container').append(tempHtml);

                    // 태그 추가
                    const postId = postDto.id;
                    const postPosition = `.postDto-${postId}-tags`;
                    for (let j = 0; j < postDto.tags.length; j++) {
                        const tempTag = postDto.tags[j]['tag'];
                        $(postPosition).append('#' + tempTag + ' ');
                    }

                    // 댓글 렌더링
                    let replyList = postDto['replies'];
                    let replyDto = $('.replyDto-' + postId);
                    if (replyList !== null) {
                        for (let j = 0; j < replyList.length; j++) {
                            let reply = replyList[j];
                            let tempReplyHtml = addReplyHtml(reply);
                            replyDto.append(tempReplyHtml);
                        }//for
                    }//if
                }
            },
            error: function (error) {
                console.log(error.valueOf());
            }
        });
    } else {
        // 로그아웃된 상태
        document.getElementById('auth-exist').style.display = 'none';
        document.getElementById('no-auth-exist').style.display = 'block';


        $.ajax({
            type: 'GET',
            url: `/api/post`,
            contentType: 'application/json',
            // 로그아웃한 사용자에 대한 성공 콜백 안에 추가
            success: function (response) {
                console.log(response);
                const resList = response['posts'];
                for (let i = 0; i < resList.length; i++) {
                    const postDto = resList[i];
                    const tempHtml = addHtml(postDto);
                    $('#container').append(tempHtml);

                    // 태그 추가
                    const postId = postDto.id;
                    const postPosition = `.postDto-${postId}-tags`;
                    for (let j = 0; j < postDto.tags.length; j++) {
                        const tempTag = postDto.tags[j]['tag'];
                        $(postPosition).append('#' + tempTag + ' ');
                    }

                    // 댓글 렌더링
                    renderReplies(postDto);
                }
            },
            r(error) {
                console.log(error.valueOf())
            }
        })
    }
})


/* 게시글 관련 */

function addHtml(postDto) {

    let likedIcon = '<i class="fas fa-heart like-btn" onclick="likePost(' + postDto.id + ')" data-liked="' + (postDto.liked ? 'true' : 'false') + '"></i>';

    return `<div class="postDto-box postDto-${postDto.id}">
                <div class="postDto-header">
                    <div class="postDto-user">
                        <img class="postDto-profileimage" src="${postDto.imageUrl}">
                        <div class="postDto-nickname">&nbsp; ${postDto['nickname']}</div>
                    </div>
                    <div class="postDto-createdAt">createdAt: ${postDto['createdAt']}</div>
                    <div class="postDto-modifiedAt">modifiedAt: ${postDto['modifiedAt']}</div>
                    <div class="postDto-extra-btn" onclick="displayUpdateBox(${postDto.id})">&nbsp; &nbsp; EDIT</div>
                    <div class="postDto-extra-btn" onclick="deletePost(${postDto.id})">&nbsp; &nbsp; DELETE</div>
                </div>

                <div class="postDto-body">
                    <img class="postDto-photo" src="${postDto['multiMediaUrl']}"><br><br>
                    <div class="postDto-content">
                        &nbsp; ${postDto.content}
                    </div>
                </div>
                <br><br>
                <div class="postDto-divider"></div>
                <div class="postDto-footer"><!--게시글 좋아요-->
                    <div class="post-like-icon" onclick="likePost(${postDto.id})">&nbsp;&nbsp; ${likedIcon}</div>
                    <div class="postDto-postLike postDto-postLike-${postDto.id}">&nbsp; LIKE ${postDto['countPostLike']}</div>
                    <div class="postDto-tags postDto-tag-${postDto.id}">&nbsp;&nbsp;Tags: </div>
                </div>
                <div class="postDto-divider"></div>
                <br>
                
                <br><br>
                <div class="replyDto-box-${postDto.id}"">
                    <input type="text" class="replyDto-input" placeholder="ReplyContent">
                    <br>
                    <button class="replyDto-btn" onclick="writeReply(${postDto.id})">REPLY SUBMIT</button><br>
                </div>
                <div class="replyDto-content replyDto-${postDto.id}" id="replyDto-content" >
                    <!--댓글 자동삽입되는 영역-->
                </div>
            </div><br>`
}

function writePost() { // 게시글 작성
    if(!setToken()) return

    let check = confirm("이대로 게시글을 작성하시겠습니까?")
    if(!check) return

    var file = $('#file')[0].files[0];
    var formData = new FormData();
    formData.append('multipartFile', file);

    var data = {
        title: $('#title').val(),
        content: $('#content').val()
    };
    formData.append("requestDto", new Blob([JSON.stringify(data)], {type: "application/json"}));

    $.ajax({
        url: "/api/post",
        method: "post",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        enctype: 'multipart/form-data',
        dataType: "json",
        success: function(result) {
            alert("게시글이 작성되었습니다!")
            window.location.href = host + '/'
        },
        error: function (xhr, status, error) {
            alert("게시글 작성에 실패했습니다!")
        }
    })
}

function displayUpdateBox(postId) {
    hideUpdateBox()

    let updateHtml = `
            <table id="post-update" class="post-update-box">
            <tr>
                <th>제목</th>
                <td>
                    <input type="text" id="title-update" placeholder="Update Title" class="post-update-element">
                </td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
                    <input type="text" id="content-update" placeholder="Update Content" class="post-update-element">
                </td>
            </tr>
            <tr>
                <th>파일</th>
                <td>
                    <input type="file" id="file-update" name="file">
                </td>
            </tr>
            <tr>
                <th><button id="post-update-submit" onclick="updatePost(${postId})">수정하기</button></th>
                <td><button id="post-update-cancel" onclick="hideUpdateBox()">취소하기</button></td>
            </tr>
        </table>
        `

    $('#post-update-box').append(updateHtml)

    document.getElementById('post-update-box').style.display = 'block'
}

function hideUpdateBox() {
    $("#post-update-box").empty()
}

function updatePost(postId) {
    if(!setToken()) return

    let check = confirm("이대로 게시글을 수정하시겠습니까?")
    if(!check) return

    var file = $('#file-update')[0].files[0];
    var formData = new FormData();
    formData.append('multipartFile', file);

    var data = {
        title: $('#title-update').val(),
        content: $('#content-update').val()
    };
    formData.append("requestDto", new Blob([JSON.stringify(data)], {type: "application/json"}));

    $.ajax({
        url: `/api/post/${postId}`,
        method: "put",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        enctype: 'multipart/form-data',
        dataType: "json",
        success: function(result) {
            alert("게시글이 수정되었습니다!")
            window.location.href = host + '/'
        },
        error: function (xhr, status, error) {
            alert("게시글 수정에 실패했습니다!")
        }
    })
}

function deletePost(postId) {
    if(!setToken()) return

    let check = confirm("정말 게시글을 삭제하시겠습니까?")

    if(check === true) {

        $.ajax({
            type: "DELETE",
            url: `/api/post/${postId}`,
            contentType: "application/json"
        })
            .done(function() {
                alert('게시글을 삭제했습니다!')
                window.location.href = host
            })
            .fail(function() {
                alert('게시글 삭제에 실패했습니다.')
            })
    }
}

function likePost(postId) {
    if (!setToken()) return;

    $.ajax({
        type: "POST",
        url: `/api/post/${postId}/like`,
        contentType: "application/json"
    })
        .done(function (res) {
            const likeIcon = res.liked ? '<i class="fas fa-heart like-btn"></i>' : '<i class="far fa-heart unlike-btn"></i>';


        })
        .fail(function () {
            alert('좋아요 등록 중 오류가 발생했습니다.');
        });
}


/* 댓글 관련 */

function addReplyHtml(replyDto) {
    let likeIcon = '<i class="fas fa-heart like-btn"></i>';

    return `<br><hr>
    <div class="replyDto-box replyDto-box-${replyDto.id}">
        <div class="postDto-nickname" id="replyDto-nickname">&nbsp;&nbsp;${replyDto.nickname}</div>
        <div class="postDto-createdAt" id="replyDto-createdAt">&nbsp;&nbsp;createdAt: ${replyDto.createdAt}</div>
        <div class="postDto-modifiedAt" id="replyDto-modifiedAt">&nbsp;&nbsp;modifiedAt: ${replyDto.modifiedAt}</div>

        <br><br>

        <!--댓글 내용 자동삽입-->
        <br>
        <div class="replyDto-content-${replyDto.id}">&nbsp; &nbsp; ${replyDto.content}</div>
        <div class="postDto-divider"></div>

        <!--댓글 좋아요-->
        <div class="replyDto-${replyDto.id}" id="replyDto-like">
            <div class="relply-replyLike replyDto-like-unlike-${replyDto.id}" onclick="likeReply(${replyDto.id}, ${replyDto.liked})">
                &nbsp; &nbsp; ${likeIcon}
            </div>
            <div class="replyDto-replyLike" id="replylike-${replyDto.id}">
                &nbsp; &nbsp; LIKE ${replyDto.countReplyLike}
            </div>
        </div>

        <!--댓글 수정 및 삭제-->
        <input type="text" class="replyDto-updateInput" id="replyDto-updateInput-${replyDto.id}" placeholder="ReplyUpdateContent">
        <br>
        <button type="button" class="replyDto-extra-btn" onclick="updateReply(${replyDto.id})">REPLY EDIT</button>
        <button type="button" class="replyDto-extra-btn" onclick="deleteReply(${replyDto.id})">REPLY DELETE</button>
    </div>`;
}

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
        let replyContent = $('.replyDto-input').val();

        $.ajax({
            type: 'POST',
            url: `/api/reply/${postId}`,
            contentType: 'application/json',
            data: JSON.stringify({content: replyContent}),
        })
            .done(function (res) {
                alert('댓글을 등록했습니다!');
                let replyHtml = addReplyHtml(res);

                // 댓글이 추가될 위치를 선택하고 새로운 댓글 추가
                let replyPosition = '#replyDto-box-' + postId;
                $(replyPosition).append(replyHtml);

                $('.replyDto-input').val('');

                // 새 댓글을 추가한 후 댓글을 다시 렌더링
                renderReplies(res.post);
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
                $('.postDto-' + postId + ' .replyDto-input').val('');
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
                //$('.replyDto-' + replyId).remove(); -> 이러면 댓글 여러 개 삭제시 공간이 나 버림..
            })
            .fail(function () {
                alert('댓글 삭제에 실패했습니다.');
            });
    }
}

function likeReply(replyId, liked) {
    if (!setToken()) return;

    const likePosition = `.replyDto-like-unlike-${replyId}`;
    const likedIcon = '<i class="fas fa-heart like-btn"></i>';

    // 댓글 좋아요 등록
    $.ajax({
        type: "POST",
        url: `/api/reply/${replyId}/like`,
        contentType: "application/json",
    })
        .done(function (res) {
            $(likePosition).empty().append(likedIcon);
            $(`#replylike-${replyId}`).text(` 좋아요 ${res['countReplyLike']}`);
            // 댓글의 좋아요 여부 업데이트
            updateReplyLikeStatus(replyId, true);
        })
        .fail(function () {
            alert('좋아요 등록 중 오류가 발생했습니다.');
        });
}


function updateReplyLikeStatus(replyId, liked) {
    const replyDto = findReplyById(replyId);
    if (replyDto) {
        replyDto.liked = liked;
    }
}


function findReplyById(replyId) {
    const resList = response['posts'];
    for (const postDto of resList) {
        for (const replyDto of postDto.replies) {
            if (replyDto.id === replyId) {
                return replyDto;
            }
        }
    }
    return null;
}


/* 마이페이지 관련 */
function mypage() {
    if(!setToken()) return

    $.ajax({
        type: 'GET',
        url: `/api/user/info`,
        success: function (response) {
            console.log(response)
            let isAdmin = response['isAdmin']

            if(isAdmin) {
                window.location.href = host + '/api/admin'
            } else {
                window.location.href = host + '/api/profile'
            }
        }
    })
}



/* 회원 관련 */

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

function setToken() {
    let check = false
    let auth = Cookies.get('Authorization')
    if(auth === undefined) auth = ''

    if(auth !== ''){
        check = true
    } else {
        alert('로그인한 유저만 수정 가능합니다!')
        window.location.href = host + '/api/user/login-page'
        return false
    }

    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
        jqXHR.setRequestHeader('Authorization', auth)
    })
    return check1;
}