let page = 0;

function getProfiles() {
    const token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/profile',
        headers: {"X-CSRF-TOKEN": token},
        method: 'post',
        dataType: 'json',
        data: {page: page},
        success: function (data) {
            generateProfiles(data)
        }
    });
    page += 1
}

function generateProfiles(profiles) {
    const posts_section = document.getElementById("profiles_section");
    profiles.forEach(function (profileEntity) {
        const profile = document.getElementById("profile").cloneNode(true)
        profile.hidden = false
        profile.getElementsByClassName("profileUsername")[0].innerText = profileEntity['username']
        profile.getElementsByClassName("profileUsername")[0].href = '/profile/' + profileEntity['username']
        profile.getElementsByClassName("profileEmail")[0].innerText = profileEntity['email']
        posts_section.insertAdjacentElement('beforeend', profile)
    })

}