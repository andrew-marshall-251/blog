async function registerUser(event) {
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
		const url = "http://localhost:8080/api/auth/register"
		const response = await fetch(url, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				username: formData.get("username"),
				email: formData.get("email"),
				mascot: formData.get("mascot"),
				bio: formData.get("bio"),
				password: password
			})
		});

		if (!response.ok) {
			throw new Error("Registration failed.");
		}

		const result = await response.json();
		console.log("Registration successful:", result);
	} catch (error) {
		console.error(error);
	}
}

const registerForm = document.getElementById("register-form");
registerForm.addEventListener("submit", registerUser);
