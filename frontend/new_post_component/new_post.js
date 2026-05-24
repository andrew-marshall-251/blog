async function postNewPost() {
	const url = "http://localhost:8080/api/posts/";
	const button = document.getElementById("new-post-submit");
	button.disabled = true;

	try {
		const newPostTitle = document.
			getElementById("post-title").
			value.trim();
		const newPostContent = document.
			getElementById("editor").
			value.trim();

		if (!newPostTitle || !newPostContent) {
			throw new Error("Title and content are required.");
		}

		const response = await fetch(url, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				authorId: 1,
				threadId: 1,
				postTitle: newPostTitle,
				postContent: newPostContent
			}),
		});

		if (!response.ok) {
			const errorText = await response.text();
			throw new Error("error");
		}

		alert("Post created.");
	} catch (err) {
		console.error(err);
		alert(err.message || "Failed to create post.");
	} finally {
		button.disabled = false;
	}
}

const button = document.getElementById("new-post-submit");
button.addEventListener("click", postNewPost);
