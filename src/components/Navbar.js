import React from "react";
import { Link } from "react-router-dom";
import { AppBar, Toolbar, Button, Typography } from "@mui/material";

const Navbar = ({ onLogout }) => {
  return (
    <AppBar position="sticky">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Social Media
        </Typography>

        <Button color="inherit" component={Link} to="/feeds">
          Home
        </Button>
        <Button color="inherit" component={Link} to="/profile-details">
          Profile
        </Button>
        <Button color="inherit" component={Link} to="/Search">
          Search
        </Button>
        <Button color="inherit" component={Link} to="/friend-requests">
        Friend Requests
        </Button>
        <Button color="inherit" onClick={onLogout}>
          Logout
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;