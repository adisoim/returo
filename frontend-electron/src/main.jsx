import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import Management from "./Management.jsx";
import "./index.css";
import { BrowserRouter, Routes, Route, HashRouter } from "react-router-dom";
import Menu from "./Menu.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(
  <HashRouter>
    <Menu />
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/management" element={<Management />} />
    </Routes>
  </HashRouter>
);
