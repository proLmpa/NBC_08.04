const host = 'http://' + window.location.host;

$(document).ready(function() {
    if(!setToken()) return

    $.ajax({
        type: 'GET',
        url: '/api/user/info',
        success: function(response) {
            let userId = response['id']
            console.log(userId)

            $.ajax({
                type:'GET',
                url: '/api/profile/' + userId
            }).done(function(response) {
                console.log(response)
                let tempHtml = formProfile(response)
                $('.body').append(tempHtml)
            })
        }
    })
})

function formProfile(profileDto) {
    return `<div class="profileDto-box">
        <div class="profileDto-photo"> </div>
        <div class="profileDto-intro">
            <div class="profileDto-username">가입 ID: ${profileDto.username}</div>
            <div class="profileDto-nickname">별명: ${profileDto.nickname}</div>
            <div class="profileDto-email">Email 주소: ${profileDto.email}</div>
            <div class="profileDto-introduction">소개글: ${profileDto['introduction']}</div>
            <div class="profileDto-role">역할: ${profileDto.role}</div>
            <div class="profile-edit-btns">            
                <button class="profile-edit-btn">프로필 수정하기</button>
                <button class="password-edit-btn">비밀번호 수정하기</button>
            </div>
        </div><br>`
}

function logout() {
    // 토큰 삭제
    Cookies.remove("Authorization", {path: '/'});
    window.location.href = host + '/'
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