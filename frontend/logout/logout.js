async function logoutUser(event) {
	localStorage.removeItem("accessToken");
}

const loginForm = document.getElementById("logout-form");
loginForm.addEventListener("submit", logoutUser);
