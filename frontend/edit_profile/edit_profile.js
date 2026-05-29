function fillEditForm(user) {
	var usernameField = document.getElementById('username-field');
	var emailField = document.getElementById('email-field');
	var mascotField = document.getElementById('mascot-field');
	var bioField = document.getElementById('bio-field');

	const username = user.username;
	const email = user.email;
	const mascot = user.mascot;
	const bio = user.bio;

	usernameField.value = username;
	emailField.value = email;
	mascotField.value = mascot;
	bioField.value = bio;
}

async function loadMyUser() {
	const token = localStorage.getItem("accessToken");
	const url = "http://localhost:8080/api/users/me";
	var res = await fetch(url, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${token}`,
			"Content-Type": "application/json"
		}
	});

	var user = await res.json();
	fillEditForm(user);
}

async function updateProfile() {
	event.preventDefault();

	const form = event.currentTarget;
	const formData = new FormData(form);

	const password = formData.get("password");
	const confirmPassword = formData.get("confirm-password");
	if (password !== confirmPassword) {
		console.error("Passwords do not match.");
		alert("Passwords do not match.");
		return;
	}

	try {
		const url = "http://localhost:8080/api/users/me"
		const response = await fetch(url, {
			method: "PATCH",
			headers: {
				"Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				username: formData.get("username"),
				email: formData.get("email"),
				mascot: formData.get("mascot"),
				bio: formData.get("bio"),
				password: formData.get("password"),
			})
		});
		console.log(response);
	} catch (error) {
		console.error(error);
	}
	console.log("hello");
}

console.log("hello");
loadMyUser();
const editProfileForm = document.getElementById("edit-profile-form");
editProfileForm.addEventListener("submit", updateProfile);
