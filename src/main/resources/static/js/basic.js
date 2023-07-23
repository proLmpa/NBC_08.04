const host = 'http://' + window.location.host;

$(document).ready(function () {
    let auth = Cookies.get('Authorization')
    if(auth === undefined) auth = ''

    if(auth !== ''){
        document.getElementById('auth-exist').style.display = 'block'
    } else {
        document.getElementById('no-auth-exist').style.display = 'block'
    }

    $.ajax({
        type: 'GET',
        url: `/api/post`,
        contentType: 'application/json',
        success: function(response) {
            console.log(response)

            const resList = response['posts']
            for(let i = 0; i < resList.length; i++) {
                let postDto = resList[i]
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
                    }//for
                }//if
            }//for
        },
        error(error) {
            console.log(error.valueOf())
        }
    })
})

function formPost(postDto) {
    return `<div class="postDto-box postDto-${postDto.id}">
            <div class="postDto-header">
                <div class="postDto-nickname">${postDto.nickname}</div>
                <button class="postDto-follow-btn postDto-follow-${postDto.id}" onclick="followUser(${postDto['userId']})">팔로우</button>
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
                <div class="postDto-postLike postDto-postLike-${postDto.id}" onclick="likePost(${postDto.id})">좋아요 ${postDto['countPostLike']}</div>
                <div class="postDto-reply-btn" onclick="reply(${postDto.id})">댓글 달기 </div>
            </div>
            </div><br>`
}

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

// 게시글 작성 기능(신규)
function writePost() {
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
        },
        error: function (xhr, status, error) {
            alert("게시글 작성에 실패했습니다!")
        }
    })
}

// function displayUpdateBox(postId) {
//     hideUpdateBox()
//
//     let updateHtml = `
//             <table id="post-update" class="post-update-box">
//             <tr>
//                 <th>제목</th>
//                 <td>
//                     <input type="text" id="title-update" placeholder="Update Title" class="post-update-element">
//                 </td>
//             </tr>
//             <tr>
//                 <th>내용</th>
//                 <td>
//                     <input type="text" id="content-update" placeholder="Update Content" class="post-update-element">
//                 </td>
//             </tr>
//             <tr>
//                 <th>파일</th>
//                 <td>
//                     <input type="file" id="file-update" name="file">
//                 </td>
//             </tr>
//             <tr>
//                 <th><button id="post-update-submit" onclick="updatePost(${postId})">수정하기</button></th>
//                 <td><button id="post-update-cancel" onclick="hideUpdateBox()">취소하기</button></td>
//             </tr>
//         </table>
//         `
//
//     $('#post-update-box').append(updateHtml)
//
//     document.getElementById('post-update-box').style.display = 'block'
// }

function hideUpdateBox() {
    $("#post-update-box").empty()
}

// function updatePost(postId) {
//     if(!setToken()) return
//
//     let check = confirm("이대로 게시글을 작성하시겠습니까?")
//     if(!check) return
//
//     var file = $('#file')[0].files[0];
//     var formData = new FormData();
//     formData.append('multipartFile', file);
//
//     var data = {
//         let title = $('#title-update').val(),
//         let content = $('#content-update').val()
//     };
//     formData.append("requestDto", new Blob([JSON.stringify(data)], {type: "application/json"}));
//
//     $.ajax({
//         url: `/api/post/${postId}`,
//         method: "put",
//         data: formData,
//         contentType: false,
//         processData: false,
//         cache: false,
//         enctype: 'multipart/form-data',
//         dataType: "json",
//         success: function(result) {
//             alert("게시글이 작성되었습니다!")
//         },
//         error: function (xhr, status, error) {
//             alert("게시글 작성에 실패했습니다!")
//         }
//     })
//
//     // if(check === true) {
//     //
//     //     $.ajax({
//     //         type: "PUT",
//     //         url: `/api/post/${postId}`,
//     //         contentType: "application/json",
//     //         data: JSON.stringify({title: title, content: content}),
//     //     })
//     //         .done(function() {
//     //             alert('게시글을 수정했습니다!')
//     //             window.location.href = host
//     //         })
//     //         .fail(function() {
//     //             alert('게시글 수정에 실패했습니다.')
//     //             window.location.href = host
//     //         })
//     // }
// }

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
    if(!setToken()) return

    let likePosition = '.postDto-postLike-' + postId

    $.ajax({
        type: "POST",
        url: `/api/post/${postId}/like`,
        contentType: "application/json"
    })
        .done(function(res) {
            $(likePosition).empty()
            $(likePosition).append(res['message'])
            // window.location.href = host
        })
        .fail(function() {
            alert('좋아요 등록 중 오류가 발생했습니다.')
        })
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

                $('.postDto-' + postId + '#reply-content').val('');
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
                $('.replyDto-box-' + replyId + ' #reply-content').text(res.content);
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

function likeReply(replyId, liked) {
    if (!setToken()) return;

    const likePosition = `.replyDto-like-unlike-${replyId}`;
    const likedIcon = '<i class="fas fa-heart like-btn"></i>';
    const unlikedIcon = '<i class="far fa-heart unlike-btn"></i>';

    if (liked) {
        $.ajax({
            type: "DELETE",
            url: `/api/reply/${replyId}/like`,
            contentType: "application/json",
        })
            .done(function (res) {
                $(likePosition).empty().append(unlikedIcon);
                $(`#replylike-${replyId}`).text(` 좋아요 ${res['countReplyLike']}`);
                // Update 'liked' status for the specific comment/reply
                updateReplyLikeStatus(replyId, false);
            })
            .fail(function () {
                alert('좋아요 취소 중 오류가 발생했습니다.');
            });
    } else {
        $.ajax({
            type: "POST",
            url: `/api/reply/${replyId}/like`,
            contentType: "application/json",
        })
            .done(function (res) {
                $(likePosition).empty().append(likedIcon);
                $(`#replylike-${replyId}`).text(` 좋아요 ${res['countReplyLike']}`);
                // Update 'liked' status for the specific comment/reply
                updateReplyLikeStatus(replyId, true);
            })
            .fail(function () {
                alert('좋아요 등록 중 오류가 발생했습니다.');
            });
    }
}


function updateReplyLikeStatus(replyId, liked) {
    const replyDto = findReplyById(replyId);
    if (replyDto) {
        replyDto.liked = liked;
    }
}


function findReplyById(replyId) {
    const resList = response['posts']; // Assuming you have this response variable
    for (const postDto of resList) {
        for (const replyDto of postDto.replies) {
            if (replyDto.id === replyId) {
                return replyDto;
            }
        }
    }
    return null;
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
    return check
}