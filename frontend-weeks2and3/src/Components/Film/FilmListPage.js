import { RedirectUrl } from "../Router.js";
import { getUserSessionData } from "../../utils/session.js";
import callAPI from "../../utils/api.js";
import PrintError from "../PrintError.js";
const API_BASE_URL = "/api/films/";

const FilmListPage = async () => {
  // deal with page title
  let page = document.querySelector("#page");
  // clear the page
  page.innerHTML = "";
  let title = document.createElement("h4");
  title.id = "pageTitle";
  title.innerText = "List of films";
  page.appendChild(title);

  const user = getUserSessionData();

  try {
    const films = await callAPI(API_BASE_URL, "GET", user.token);
    onFilmList(films);
  } catch (err) {
    console.error("FilmListPage::onFilmList", err);
    PrintError(err);
  }
};

const onFilmList = (data) => {
  if (!data) return;
  let table = `
  <div id="tableFilms" class="table-responsive mt-3">
  <table class="table">
      <thead>
          <tr>
              <th class="title">Title</th>
              <th class="link">Link</th>
              <th class="duration">Duration (min)</th>
              <th class="budget">Budget (million)</th>
              <th class="save">Save</th>
              <th class="delete">Delete</th>
          </tr>
      </thead>
      <tbody>`;

  data.forEach((element) => {
    table += `<tr data-id="${element.id}">
                <td class="title" contenteditable="true">${element.title}</td>
                <td class="link" contenteditable="true"><a href="${element.link}" target="_blank">${element.link}</a></td>
                <td class="duration" contenteditable="true">${element.duration}</td>
                <td class="budget" contenteditable="true">${element.budget}</td>
                <td class="save"><button class="btn btn-primary saveBtn">Save</button></td>
                <td class="delete"><button class="btn btn-dark deleteBtn">Delete</button></td>
            </tr>
            `;
  });

  table += `</tbody>
  </table>
  </div>`;
  page.innerHTML += table;

  page.innerHTML +=
    '<button id="addBtn" class="btn btn-primary mt-2">Add film</button>';

  const saveBtns = document.querySelectorAll(".saveBtn");
  const deleteBtns = document.querySelectorAll(".deleteBtn");
  deleteBtns.forEach((deleteBtn) => {
    deleteBtn.addEventListener("click", onDelete);
  });

  saveBtns.forEach((saveBtn) => {
    saveBtn.addEventListener("click", onSave);
  });

  const addBtn = document.querySelector("#addBtn");
  addBtn.addEventListener("click", onAddFilm);
};

const onSave = async (e) => {
  // the id is given in the current table row under data-id attribute
  const filmId = e.target.parentElement.parentElement.dataset.id;
  let film = {};
  const tr = e.target.parentElement.parentElement;
  const cells = tr.querySelectorAll("td");
  film.title = cells[0].innerText;
  film.link = cells[1].innerText;
  film.duration = cells[2].innerText;
  film.budget = cells[3].innerText;
  console.log("Film:", film);
  const user = getUserSessionData();

  try {
    const filmUpdated = await callAPI(
      API_BASE_URL + filmId,
      "PUT",
      user.token,
      film
    );
    await FilmListPage();
  } catch (err) {
    console.error("FilmListPage::onSave", err);
    PrintError(err);
  }
};

const onDelete = async (e) => {
  // the id is given in the current table row under data-id attribute
  const filmId = e.target.parentElement.parentElement.dataset.id;
  const user = getUserSessionData();

  try {
    const filmDeleted = await callAPI(
      API_BASE_URL + filmId,
      "DELETE",
      user.token
    );
    FilmListPage();
  } catch (err) {
    console.error("FilmListPage::onDelete", err);
    PrintError(err);
  }
};

const onAddFilm = () => {
  RedirectUrl("/films/add");
};

export default FilmListPage;
