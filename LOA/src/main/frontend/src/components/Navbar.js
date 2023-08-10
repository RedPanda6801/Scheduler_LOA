import React from "react";
import { Link } from "react-router-dom";
import "./Navbar.css"; // Import the CSS file for styling

const Navbar = () => {
  return (
    <nav className="navbar">
      <ul className="nav-list">
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/category1">Category 1</Link>
        </li>
        <li>
          <Link to="/category2">Category 2</Link>
        </li>
        <li>
          <Link to="/category3">Category 3</Link>
        </li>
        <li>
          <Link to="/category4">Category 4</Link>
        </li>
        <li>
          <Link to="/category5">Category 5</Link>
        </li>
        <li>
          <Link to="/signin">Sign In</Link>
        </li>
        <li>
          <Link to="/signup">Sign Up</Link>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
