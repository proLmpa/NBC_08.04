const host = 'http://' + window.location.host;

$(document).ready(function () {
    if (!setToken()) return

    $.ajax({
        type: 'GET',
        url: '/api/user/info',
        success: function (response) {
            let userId = response['id']

            $.ajax({
                type: 'GET',
                url: `/api/profile/${userId}`
            }).done(function (response) {
                let tempHtml = formProfile(response)
                $('.body').append(tempHtml)
            })
        }
    })
})

function formProfile(profileDto) {
    return `<div class="profileDto-box">
        <div class="profileDto-header"> 
            <div class="profileDto-photo">
                <img class="profileDto-photo-image" src="${profileDto['imageUrl']}" alt="프로필 사진이 저장될 공간입니다.">
            </div>
            <div class="image-edit-btns">
                <input class="image-search-btn" type="file" name="profile-image" id="image-search-btn">
                <button class="image-register-btn" onclick="registerProfileImage()">프로필 사진 등록</button>
                <button class="image-cancel-btn" onclick="cancelProfileImage()">프로필 사진 삭제</button>
            </div>
        </div>
        <div class="profileDto-intro">
            <div id="profileForm">
                가입 ID: <input type="text" name="username" placeholder="${profileDto.username}" disabled>
                비밀번호: <input type="text" id="password-input" name="password" placeholder="********" disabled>
                <div id="newPasswords-input" style="display:none">
                    새 비밀번호: <input type="text" id="newPassword-input1" name="newPassword1" placeholder="*********">
                    새 비밀번호 확인: <input type="text" id="newPassword-input2" name="newPassword2" placeholder="*********">
                </div>
                별명: <input type="text" id="nickname-input" name="nickname" placeholder="${profileDto.nickname}" disabled>
                소개글: <input type="text" id='introduction-input' name="introduction" placeholder="${profileDto['introduction']}" disabled>
                Email 주소: <input type="text" name="email" placeholder="${profileDto.email}" disabled>
                역할: <input type="text" name="introduction" placeholder="${profileDto.role}" disabled>
                <div id="info-edit-btns" class="info-edit-btns">
                    <button class="profile-edit-btn" onclick="enableEditProfile()">프로필 수정하기</button>
                    <button class="password-edit-btn" onclick="enableEditPassword()">비밀번호 수정하기</button>
                </div>
                <div id="profile-edit-btns" class="profile-edit-btns" style="display:none">
                    <button class="submit-edit-btn" onclick="editProfile()">수정 제출하기</button>
                    <button class="cancel-edit-btn" onclick="disableEditProfile('', '')">수정 취소하기</button>
                </div>
                <div id="password-edit-btns" class="password-edit-btns" style="display:none">
                    <button class="submit-edit-btn" onclick="editPassword()">새 비밀번호 제출하기</button>
                    <button class="cancel-edit-btn" onclick="disableEditPassword()">새 비밀번호 취소하기</button>
                </div>
            </div>
        </div><br>`
}

function editProfile() {
    if (!setToken()) return

    let check = confirm("이대로 프로필을 수정하시겠습니까?")

    if (check === true) {
        let editNickname = $('#nickname-input').val()
        let editIntroduction = $('#introduction-input').val()

        $.ajax({
            type: 'PUT',
            url: `/api/profile`,
            contentType: 'application/json',
            data: JSON.stringify({nickname: editNickname, introduction: editIntroduction})
        }).done(function (response) {
            disableEditProfile(editNickname, editIntroduction)

            alert('프로필이 수정되었습니다!')
        }).fail(function () {
            alert('프로필 수정에 실패했습니다.')
        })
    }
}

function enableEditProfile() {
    const nickname = document.getElementById('nickname-input')
    const introduction = document.getElementById('introduction-input')
    nickname.disabled = false
    introduction.disabled = false

    document.getElementById('info-edit-btns').style.display = 'none'
    document.getElementById('profile-edit-btns').style.display = 'block'
}

function disableEditProfile(name, intro) {
    const nickname = document.getElementById('nickname-input')
    const introduction = document.getElementById('introduction-input')
    nickname.disabled = true
    introduction.disabled = true
    nickname.value = name
    introduction.value = intro

    document.getElementById('info-edit-btns').style.display = 'block'
    document.getElementById('profile-edit-btns').style.display = 'none'
}

function editPassword() {
    if (!setToken()) return

    let check = confirm("이대로 비밀번호를 수정하시겠습니까?")

    if (check === true) {
        let password = $('#password-input').val()
        let newPassword1 = $('#newPassword-input1').val()
        let newPassword2 = $('#newPassword-input2').val()

        $.ajax({
            type: 'PUT',
            url: `/api/profile/password`,
            contentType: 'application/json',
            data: JSON.stringify({password: password, newPassword1: newPassword1, newPassword2: newPassword2})
        }).done(function () {
            disableEditPassword()

            alert('비밀번호가 수정되었습니다!')
        }).fail(function (response) {
            console.log(response)
            alert('Error : ' + response['responseJSON']['message'])
        })
    }
}

function enableEditPassword() {
    const password = document.getElementById('password-input')
    password.disabled = false

    document.getElementById('info-edit-btns').style.display = 'none'
    document.getElementById('password-edit-btns').style.display = 'block'
    document.getElementById('newPasswords-input').style.display = 'block'
}

function disableEditPassword() {
    const password = document.getElementById('password-input')
    const newPassword1 = document.getElementById('newPassword-input1')
    const newPassword2 = document.getElementById('newPassword-input2')
    password.disabled = true
    password.value = ''
    newPassword1.value = ''
    newPassword2.value = ''

    document.getElementById('info-edit-btns').style.display = 'block'
    document.getElementById('password-edit-btns').style.display = 'none'
    document.getElementById('newPasswords-input').style.display = 'none'
}

function registerProfileImage() {
    if (!setToken()) return

    let check = confirm("이대로 프로필 사진을 수정하시겠습니까?")
    if(!check) return

    var file = $('#image-search-btn')[0].files[0];
    var formData = new FormData();
    formData.append('image', file);

    $.ajax({
        url: `/api/profile/image`,
        method: "put",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        enctype: 'multipart/form-data',
        dataType: "json",
        success: function(result) {
            alert("프로필 사진이 수정되었습니다!")
            window.location.href = host + '/api/profile'
        },
        error: function (xhr, status, error) {
            alert("프로필 사진 수정에 실패했습니다!")
        }
    })
}

function cancelProfileImage() {
    if (!setToken()) return

    let check = confirm("프로필 사진을 삭제하시겠습니까?")
    if(!check) return

    $.ajax({
        type: "DELETE",
        url: `/api/profile/image`
    })
        .done(function() {
            alert('프로필 사진을 삭제했습니다!')
            window.location.href = host + '/api/profile'
        })
        .fail(function() {
            alert('프로필 사진 삭제에 실패했습니다.')
        })
}

function logout() {
    // 토큰 삭제
    Cookies.remove("Authorization", {path: '/'});
    window.location.href = host + '/'
}

function setToken() {
    let check = false
    let auth = Cookies.get('Authorization')
    if (auth === undefined) auth = ''

    if (auth !== '') {
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