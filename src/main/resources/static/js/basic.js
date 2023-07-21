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
                let tempHtml = formPost(postDto)
                $('#container').append(tempHtml)

                let postPosition = '.postDto-tag-' + postDto['id']
                for(let j = 0; j < postDto['tags'].length; j++){
                    let tempTag = postDto['tags'][j]['tag']
                    $(postPosition).append('#' + tempTag + ' ')
                }
            }
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

function writePost() {
    if(!setToken()) return

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
            .done(function() {
                alert('게시글을 등록했습니다!')
                window.location.href = host
            })
            .fail(function() {
                alert('게시글 등록에 실패했습니다.')
            })
    }
}

function followUser(userId) {

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
    if(!setToken()) return

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
            .done(function() {
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