import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import axios from "axios";
import Navbar from "./components/Navbar";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ProfileDetails from "./pages/ProfileDetails";
import Feed from "./pages/Feed";
import Search from "./pages/Search";
import UserProfile from "./components/userProfile";
import PostDetails from "./components/PostDetails";
import FriendRequestList from "./components/FriendRequestList";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Check authentication status on app load
  useEffect(() => {
    const checkAuth = async () => {
      try {
        // Backend endpoint to verify token or check login status
        await axios.get("http://localhost:8080/api/user/auth/check", {
          withCredentials: true, // Send HTTP-only cookies
        });
        setIsLoggedIn(true);
      } catch (error) {
        setIsLoggedIn(false); // Not logged in or token invalid
      }
    };

    checkAuth();
  }, []);

  // Function to handle logout
  const handleLogout = async () => {
    try {
      await axios.post("http://localhost:8080/api/user/logout", {}, { withCredentials: true });
      setIsLoggedIn(false);
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  return (
    <Router>
      {isLoggedIn && <Navbar onLogout={handleLogout} />}
      <Routes>
        <Route path="/login" element={<Login onLogin={() => setIsLoggedIn(true)} />} />
        <Route path="/register" element={<Register onLogin={() => setIsLoggedIn(true)} />} />
        <Route
          path="/profile-details"
          element={isLoggedIn ? <ProfileDetails /> : <Navigate to="/login" />}
        />
        {/* <Route
          path="/user-profile/:userId"
          element={isLoggedIn ? <UserProfile currentUserId={currentUserId} /> : <Navigate to="/login" />}
        /> */}
        <Route path="/user/:id"  element={isLoggedIn ?<UserProfile /> : <Navigate to="/login" />} />

        <Route path="/post/:id" element={isLoggedIn ? <PostDetails /> : <Navigate to="/login" />} />
        <Route
          path="/Search"
          element={isLoggedIn ? <Search /> : <Navigate to="/login" />}
        />
        <Route
          path="/feeds"
          element={isLoggedIn ? <Feed onLogout={handleLogout} /> : <Navigate to="/login" />}
        />
          <Route
            path="/friend-requests"
            element={isLoggedIn ? <FriendRequestList /> : <Navigate to="/login" />}
          />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;