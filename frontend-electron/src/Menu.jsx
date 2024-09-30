import React from "react";
import { Link } from "react-router-dom";

export default function Menu() {
  return (
    <nav className="bg-gray-800 p-3">
      <ul className="flex justify-center space-x-4">
        <li>
          <Link
            to="/"
            className="bg-indigo-600 hover:bg-indigo-500 text-white font-bold py-1.5 px-3 rounded transition duration-300 ease-in-out text-sm"
          >
            Returo
          </Link>
        </li>
        <li>
          <Link
            to="/management"
            className="bg-indigo-600 hover:bg-indigo-500 text-white font-bold py-1.5 px-3 rounded transition duration-300 ease-in-out text-sm"
          >
            Management
          </Link>
        </li>
      </ul>
    </nav>
  );
}
