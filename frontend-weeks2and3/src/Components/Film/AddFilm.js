import { RedirectUrl } from "../Router.js";
import { getUserSessionData } from "../../utils/session.js";
import callAPI from "../../utils/api.js";
import PrintError from "../PrintError.js";

const API_BASE_URL = "/api/films/";

let addFilmPage = `<h4 id="pageTitle">Add a film</h4>
<form>
<div class="form-group">
  <label for="title">Enter title</label>
  <input
    class="form-control"
    type="text"
    name="title"
    id="title"
    required
  />
</div>
<div class="form-group">
  <label for="duration">Enter duration</label>
  <input
    class="form-control"
    type="number"
    name="duration"
    id="duration"
    required
  />
</div>
<div class="form-group">
  <label for="budget">Enter budget</label>
  <input
    class="form-control"
    type="number"
    name="budget"
    id="budget"
    required
  />
</div>
<div class="form-group">
  <label for="link">Enter link</label>
  <input
    class="form-control"
    type="url"
    name="link"
    id="link"
    required
  />
</div>
<input type="submit" class="btn btn-primary" value="Add Film" />
</form>`;

const AddFilmPage = () => {  

  let page = document.querySelector("#page");
  page.innerHTML = addFilmPage;
  let filmForm = document.querySelector("form");
  filmForm.addEventListener("submit", onAddFilm);
};

const onAddFilm = async (e) => {
  e.preventDefault();
  let film = {
    title: document.getElementById("title").value,
    duration: document.getElementById("duration").value,
    budget: document.getElementById("budget").value,
    link: document.getElementById("link").value,
  };

  const user = getUserSessionData();

  try {
    const filmAdded = await callAPI(
      API_BASE_URL ,
      "POST",
      user.token,
      film
    );
    onFilmAdded(filmAdded);
  } catch (err) {
    console.error("AddFilmPage::onAddFilm", err);
    PrintError(err);
  }  
}

const onFilmAdded = (data) => {
  console.log(data);  
  RedirectUrl("/films");
};

export default AddFilmPage;
