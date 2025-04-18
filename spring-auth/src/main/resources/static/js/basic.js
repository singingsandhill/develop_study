$(document).ready(function () {
    const auth = getToken();
    if(auth === '') {
        $('#login-true').show();
        $('#login-false').hide();
        // window.location.href = host + "/api/user/login-page";
    } else {
        $('#login-true').show();
        $('#login-false').hide();
    }
})

let host = 'http://' + window.location.host;

function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', { path: '/' });
    window.location.href = host + "/api/user/login-page";
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if(auth === undefined) {
        return '';
    }

    return auth;
}