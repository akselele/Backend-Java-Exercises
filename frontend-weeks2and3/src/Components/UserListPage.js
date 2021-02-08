import { RedirectUrl } from "./Router.js";
import { getUserSessionData } from "../utils/session.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";

const API_BASE_URL = "/api/users/";
let page = document.querySelector("#page");

const UserListPage = async () => {
  // deal with page title
  let page = document.querySelector("#page");
  // clear the page
  page.innerHTML = "";
  let title = document.createElement("h4");
  title.id = "pageTitle";
  title.innerText = "List of users";

  page.appendChild(title);
  const user = getUserSessionData();
  if (!user) RedirectUrl("/error", "Resource not authorized. Please login.");

  try {
    const users = await callAPI(API_BASE_URL, "GET", user.token);
    onUserList(users);
  } catch (err) {
    console.error("UserListPage::onUserList", err);
    PrintError(err);
  }
};

const onUserList = (data) => {
  console.log("onUserList");
  let userListPage = `<h5>List of MyCMS users</h5>
<ul class="list-group list-group-horizontal-lg">`;
  let userList = document.querySelector("ul");
  // Neat way to loop through all data in the array, create a new array of string elements (HTML li tags)
  // with map(), and create one string from the resulting array with join(''). '' means that the separator is a void string.
  userListPage += data
    .map((user) => `<li class="list-group-item">${user.login}</li>`)
    .join("");
  userListPage += "</ul>";
  return (page.innerHTML = userListPage);
};

const onError = (err) => {
  console.error("UserListPage::onError:", err);
  let errorMessage;
  if (err.message) {
    errorMessage = err.message;
  } else errorMessage = err;
  if (errorMessage.includes("jwt expired"))
    errorMessage += "<br> Please logout first, then login.";
  RedirectUrl("/error", errorMessage);
};

export default UserListPage;
