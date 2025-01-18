let page = 0;

function getPosts() {
    const token = $("meta[name='_csrf']").attr("content");
    const username = document.getElementById("username").innerText
    $.ajax({
        url: '/posts/getSubscriberPosts',
        headers: {"X-CSRF-TOKEN": token},
        method: 'post',
        dataType: 'json',
        data: {page: page, username: username},
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
        post.getElementsByClassName("post_label")[0].innerText = postEntity['label']
        post.getElementsByClassName("post_content")[0].innerText = postEntity['content']
        post.getElementsByClassName("post_created_at")[0].innerText = postEntity['createdAt']
        post.getElementsByClassName("post_author")[0].href = '/profile/' + postEntity['author']
        post.getElementsByClassName("post_author")[0].innerText = postEntity['author']
        posts_section.insertAdjacentElement('beforeend', post)
    })

}