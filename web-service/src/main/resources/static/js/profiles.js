let page = 0;

function getProfiles() {
    const token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/posts/getProfilesPageable',
        headers: {"X-CSRF-TOKEN": token},
        method: 'post',
        dataType: 'json',
        data: {page: page},
        success: function (data) {
            generatePosts(data)
        }
    });
    page += 1
}

function generateProfiles(profiles) {
    const posts_section = document.getElementById("profiles_section");
    profiles.forEach(function (postEntity) {
        const profile = document.getElementById("profile").cloneNode(true)
        profile.hidden = false
        profile.getElementsByClassName("profileUsername")[0].innerText = postEntity['username']
        profile.getElementsByClassName("profileUsername")[0].href = '/profile/' + postEntity['username']
        profile.getElementsByClassName("profileEmail")[0].innerText = postEntity['email']
        posts_section.insertAdjacentElement('beforeend', profile)
    })

}