function logout() {
    document.getElementById("logout_form").submit()
}

let page = 0;

function getPosts() {
    const token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/posts/getPosts',
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

function generatePosts(posts) {
    const posts_section = document.getElementById("posts")
    posts.forEach(function (postEntity) {
        const post = document.getElementById("post").cloneNode(true)
        post.hidden = false
        post.getElementsByClassName("delete_post_button")[0].value = postEntity['id']
        post.getElementsByClassName("post_label")[0].innerText = postEntity['label']
        post.getElementsByClassName("post_content")[0].innerText = postEntity['content']
        post.getElementsByClassName("post_created_at")[0].innerText = postEntity['createdAt']
        posts_section.insertAdjacentElement('beforeend', post)
    })

}