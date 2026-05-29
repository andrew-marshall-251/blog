function addUser(user) {
	var userProfile = document.createElement('div');
	var userImgName = document.createElement('div');
	var img = document.createElement('img');
	var name = document.createElement('h3');
	var bio = document.createElement('p');

	userProfile.classList.add('user-profile', 'shadow');
	userImgName.classList.add('user-img-name');
	img.classList.add('mascot-img');
	name.classList.add('user-name');
	bio.classList.add('user-bio');

	const mascot = user.mascot;
	// console.log(mascot);

	switch (mascot) {
		case "CAPYBARA":
			img.src = "../../BlogResourceFiles/mascots/capybara_mascot.png";
			break;
		case "LEMUR":
			img.src = "../../BlogResourceFiles/mascots/lemur_mascot.png";
			break;
		case "OCTOPUS":
			img.src = "../../BlogResourceFiles/mascots/octopus_mascot.png";
			break;
		case "RED_PANDA":
			img.src = "../../BlogResourceFiles/mascots/red_panda_mascot.png";
			break;
		case "CHAMELEON":
			img.src = "../../BlogResourceFiles/mascots/chameleon_mascot.png";
			break;
		case "OWL":
			img.src = "../../BlogResourceFiles/mascots/owl_mascot.png";
			break;
		case "PENGUIN":
			img.src = "../../BlogResourceFiles/mascots/penguin_mascot.png";
			break;
		default:
			img.src = "../../BlogResourceFiles/mascots/capybara_mascot.png";
			break;
	}

	name.textContent = user.username;
	bio.textContent = user.bio;

	userImgName.appendChild(img);
	userImgName.appendChild(name);
	userProfile.appendChild(userImgName);
	userProfile.appendChild(bio);

	const container = document.getElementById("user-profile-container");
	
	container.appendChild(userProfile);
}

async function loadMyUser() {
	const token = localStorage.getItem("accessToken");
	// console.log(`Bearer ${token}`);

	const url = "http://localhost:8080/api/users/me";

	var res = await fetch(url, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${token}`,
			"Content-Type": "application/json"
		}
	});

	var user = await res.json();
	addUser(user);
}

function addPost(post) {
	var hr = document.createElement('hr');

	var userPost = document.createElement('div');
	var postTitle = document.createElement('h1');
	var postContent = document.createElement('p');
	var postDate = document.createElement('p');

	userPost.classList.add('user-post');
	postTitle.classList.add('post-title');
	postContent.classList.add('post-content');
	postDate.classList.add('post-date');

	postTitle.textContent = post.postTitle;
	postContent.textContent = post.postContent;
	postDate.textContent = post.postDate;

	userPost.appendChild(postTitle);
	userPost.appendChild(postContent);
	userPost.appendChild(postDate);

	const container = document.getElementById("posts-container");
	
	container.appendChild(hr);
	container.appendChild(userPost);
}

async function loadMyPosts() {
	const token = localStorage.getItem("accessToken");
	const url = "http://localhost:8080/api/posts/me";

	var res = await fetch(url, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${token}`,
			"Content-Type": "application/json"
		}
	});

	var userPosts = await res.json();
	console.log(userPosts);
	for (const userPost of userPosts.userPosts) {
		addPost(userPost);
	}
}

loadMyUser();
loadMyPosts();
