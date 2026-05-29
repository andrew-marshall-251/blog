async function loginUser(event) {
	event.preventDefault();

	const form = event.currentTarget;
	const formData = new FormData(form);

	console.log(formData.get("usernameOrEmail"), formData.get("password"));

	try {
		const url = "http://localhost:8080/api/auth/login";
		const response = await fetch(url, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				usernameOrEmail: formData.get("usernameOrEmail"),
				password: formData.get("password")
			})
		});

		if (!response.ok) {
			throw new Error("Login failed.");
		}

		const result = await response.json();
		localStorage.setItem("accessToken", result.accessToken);
		console.log("Login successful:", result);
	} catch (error) {
		console.error(error);
	}
}

const loginForm = document.getElementById("login-form");
loginForm.addEventListener("submit", loginUser);
