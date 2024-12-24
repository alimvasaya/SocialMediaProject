import React, { useState, useEffect } from "react";
import axios from "axios";
import { TextField, Button, Typography, Grid, Container, CircularProgress, Box } from "@mui/material";
import { useNavigate } from "react-router-dom";

const ProfileDetails = () => {
  const navigate = useNavigate();
  const [profile, setProfile] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    address: "",
    age: "",
    profileImage: "",
  });
  const [loading, setLoading] = useState(true);
  const [isEditMode, setIsEditMode] = useState(false); // For toggling between edit and view mode

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/user/profile", {
          withCredentials: true, // Ensure cookies are included for authentication
        });
        setProfile(response.data);
      } catch (error) {
        if (error.response && error.response.status === 401) {
          alert("Session expired. Please log in again.");
          navigate("/login");
        } else {
          console.error("Error fetching profile:", error);
          alert("Failed to load profile.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfile((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.put(
        "http://localhost:8080/api/user/profile",
        profile,
        {
          withCredentials: true,
        }
      );
      alert("Profile updated successfully!");
      setIsEditMode(false); // Switch to view mode after updating
    } catch (error) {
      if (error.response && error.response.status === 401) {
        alert("Session expired. Please log in again.");
        navigate("/login");
      } else {
        console.error("Error updating profile:", error);
        alert("Failed to update profile. Please try again.");
      }
    }
  };

  if (loading) return <CircularProgress />;

  const isProfileEmpty = !profile.firstName && !profile.lastName && !profile.phoneNumber; // Determine if the profile is empty

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        Profile Details
      </Typography>

      {isProfileEmpty || isEditMode ? (
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                label="First Name"
                variant="outlined"
                fullWidth
                name="firstName"
                value={profile.firstName}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                label="Last Name"
                variant="outlined"
                fullWidth
                name="lastName"
                value={profile.lastName}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Phone Number"
                variant="outlined"
                fullWidth
                name="phoneNumber"
                value={profile.phoneNumber}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Address"
                variant="outlined"
                fullWidth
                name="address"
                value={profile.address}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Age"
                variant="outlined"
                fullWidth
                name="age"
                value={profile.age}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="Profile Image URL"
                variant="outlined"
                fullWidth
                name="profileImage"
                value={profile.profileImage}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <Button type="submit" variant="contained" color="primary">
                {isProfileEmpty ? "Create Profile" : "Update Profile"}
              </Button>
            </Grid>
          </Grid>
        </form>
      ) : (
        <Box>
          <Typography variant="h6">First Name: {profile.firstName}</Typography>
          <Typography variant="h6">Last Name: {profile.lastName}</Typography>
          <Typography variant="h6">Phone Number: {profile.phoneNumber}</Typography>
          <Typography variant="h6">Address: {profile.address}</Typography>
          <Typography variant="h6">Age: {profile.age}</Typography>
          <Typography variant="h6">Profile Image URL: {profile.profileImage}</Typography>

          <Button
            variant="contained"
            color="primary"
            onClick={() => setIsEditMode(true)} // Switch to edit mode
            sx={{ marginTop: 2 }}
          >
            Edit Profile
          </Button>
        </Box>
      )}
    </Container>
  );
};

export default ProfileDetails;