let isSubscribed = false
let token

function generateData() {
    token = $("meta[name='_csrf']").attr("content")
    isSubscribedOn()
}


function isSubscribedOn() {
    const profileUsername = document.getElementById("profileUsername").innerText
    $.ajax({
        url: '/subscribing/isSubscribedOn',
        headers: {"X-CSRF-TOKEN": token},
        method: 'post',
        dataType: 'json',
        data: {"profileUsername": profileUsername},
        success: function (data) {
            let response = JSON.parse(data);
            if (response === true) {
                showUnsubscribeButton()
            } else {
                showSubscribeButton()
            }
        }
    });
}

function showSubscribeButton() {
    const button = document.getElementById("subscribeButton");
    button.innerText = "Subscribe"
    isSubscribed = false
}

function showUnsubscribeButton() {
    const button = document.getElementById("subscribeButton");
    button.innerText = "Unsubscribe"
    isSubscribed = true
}

function onSubscribeButtonClick() {
    if (isSubscribed) {
        submitUnsubscribe()
    } else {
        submitSubscribe()
    }
}


function submitSubscribe() {
    const profileUsername = document.getElementById("profileUsername").innerText
    $.ajax({
        url: '/subscribing/subscribe',
        headers: {"X-CSRF-TOKEN": token},
        method: 'post',
        dataType: 'json',
        async: false,
        data: {"profileUsername": profileUsername}
    });
    isSubscribedOn()
}


function submitUnsubscribe() {
    const profileUsername = document.getElementById("profileUsername").innerText
    $.ajax({
        url: '/subscribing/unsubscribe',
        headers: {"X-CSRF-TOKEN": token},
        method: 'post',
        dataType: 'json',
        async: false,
        data: {"profileUsername": profileUsername}
    });
    isSubscribedOn()
}
