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
        post.getElementsByClassName("postLabel")[0].innerText = postEntity['label']
        post.getElementsByClassName("postContent")[0].innerText = postEntity['content']
        post.getElementsByClassName("postCreatedAt")[0].innerText = postEntity['createdAt']
        post.getElementsByClassName("postAuthor")[0].href = '/profile/' + postEntity['author']
        post.getElementsByClassName("postAuthor")[0].innerText = postEntity['author']
        posts_section.insertAdjacentElement('beforeend', post)
    })

}