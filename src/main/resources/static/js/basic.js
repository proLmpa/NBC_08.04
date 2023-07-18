const host = 'http://' + window.location.host;

function login() {
    $.ajax({
        type: 'GET',
        url: `/api/user/login`
    })
}

function signup() {
    $.ajax({
        type: 'GET',
        url: `/api/user/signup`
    })
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

    return auth;
}