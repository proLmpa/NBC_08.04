const host = 'http://' + window.location.host;

$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: `/api/post`,
        contentType: 'application/json',
        success: function(response) {
            console.log(response)
            $('#container').empty()

            const resList = response['postsList']
            for(let i = 0; i < resList.length; i++) {
                let postDto = resList[i]
                let tempHtml = addHtml(postDto)
                $('#container').append(tempHtml)
            }
        },
        error(error, status, request) {
            console.log(error.valueOf())
        }
    })
})

function addHtml(postDto) {
    return `<div class="postDto-${postDto.id}">
            <div class="postDto-id">id: ${postDto.id}</div>
            <div class="postDto-title">title: ${postDto.title}</div>
            <div class="postDto-content">content: ${postDto.content}</div>
            <div class="postDto-nickname">nickname: ${postDto.nickname}</div>
            <div class="postDto-likesCount">likesCount: ${postDto['countPostLike']}</div>
            <div class="postDto-createdAt">createdAt: ${postDto['createdAt']}</div>
            <div class="postDto-modifiedAt">modifiedAt: ${postDto['ModifiedAt']}</div>
            </div><br>
        `
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
                alert('Post Registered!')
                window.location.href = host
            })
            .fail(function() {
                alert('Post Register Failed')
            })
    }
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
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if(auth === undefined) {
        return '';
    }

    return auth;
}