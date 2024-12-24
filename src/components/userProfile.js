import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Button, Card, CardContent, Typography, Avatar, CircularProgress } from "@mui/material";
import { styled } from "@mui/system";

// Styled Components
const ProfileContainer = styled("div")({
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  marginTop: "32px",
});

const ProfileCard = styled(Card)({
  maxWidth: 400,
  width: "100%",
  padding: "16px",
  boxShadow: 4,
  borderRadius: "12px",
});

const ProfileHeader = styled("div")({
  display: "flex",
  alignItems: "center",
  marginBottom: "16px",
});

const ProfileImage = styled(Avatar)({
  width: "100px",
  height: "100px",
  borderRadius: "50%",
  marginRight: "16px",
});

const ActionButtons = styled("div")({
  display: "flex",
  justifyContent: "space-between",
  marginTop: "16px",
});

const UserProfile = () => {
  const { id } = useParams(); // Get the user ID from the URL
  const [user, setUser] = useState(null);
  const [isFollowing, setIsFollowing] = useState(false); // Track follow status
  const [isSelf, setIsSelf] = useState(false); // Check if it's the logged-in user
  const [isFriend, setIsFriend] = useState(false);
  const [hasSentRequest, setHasSentRequest] = useState(false); // Track friend request status

  useEffect(() => {
    const checkFriendship = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/friends/status?friendId=${id}`,
          { withCredentials: true }
        );
        setIsFriend(response.data.isFriend);
        setHasSentRequest(response.data.hasSentRequest);
      } catch (error) {
        console.error("Error checking friendship status:", error);
      }
    };

    if (!isSelf) checkFriendship();
  }, [id, isSelf]);

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/user/${id}`, {
          withCredentials: true,
        });
        setUser(response.data); // Set the user profile data

        const loggedInUserResponse = await axios.get(
          `http://localhost:8080/api/user/loggedInUser`,
          { withCredentials: true }
        );
        const loggedInUserId = loggedInUserResponse.data.id;
        setIsSelf(loggedInUserId === Number(id));

        // Fetch follow status
        if (loggedInUserId !== Number(id)) {
          const followStatusResponse = await axios.get(
            `http://localhost:8080/api/follow/status?followingId=${id}`,
            { withCredentials: true }
          );
          setIsFollowing(followStatusResponse.data.isFollowing);
        }
      } catch (error) {
        if (error.response?.status === 403) {
          console.error("You are not authorized to access this resource.");
        } else {
          console.error("Error fetching user profile:", error);
        }
      }
    };
    fetchUserProfile();
  }, [id]); // Re-run when the user ID in the URL changes

  const handleFollow = async () => {
    if (isSelf) {
      alert("You cannot follow yourself.");
      return;
    }

    try {
      const response = isFollowing
        ? await axios.post(
            `http://localhost:8080/api/follow/unfollow`,
            { followingId: id },
            { withCredentials: true }
          )
        : await axios.post(
            `http://localhost:8080/api/follow/follow`,
            { followingId: id },
            { withCredentials: true }
          );

      alert(response.data.message); // Show follow/unfollow status message
      setIsFollowing(!isFollowing); // Toggle follow status
    } catch (error) {
      console.error("Error following/unfollowing:", error);
    }
  };

  const handleSendFriendRequest = async () => {
    if (isSelf) {
      alert("You cannot add yourself.");
      return;
    }
    try {
      const response = await axios.post(
        `http://localhost:8080/api/friends/request`,
        { senderId: id },
        { withCredentials: true }
      );
      alert(response.data.message); // Notify user
      setHasSentRequest(true); // Update UI
    } catch (error) {
      console.error("Error sending friend request:", error);
    }
  };

  const handleUnfriend = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/friends/unfriend",
        { friendId: id }, // Pass the correct friendId
        { withCredentials: true }
      );
      alert(response.data.message);
      setIsFriend(false); // Update UI
    } catch (error) {
      console.error("Error unfriending user:", error);
    }
  };

  return (
    <ProfileContainer>
      {user ? (
        <ProfileCard>
          <CardContent>
            <ProfileHeader>
              <ProfileImage alt={user.firstName} src={user.profileImage} />
              <div>
                <Typography variant="h5">
                  {user.firstName} {user.lastName}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Age: {user.age}
                </Typography>
              </div>
            </ProfileHeader>

            <ActionButtons>
              {isSelf ? (
                <Typography variant="body2" color="textSecondary">
                  You cannot perform this action on yourself
                </Typography>
              ) : isFriend ? (
                <Button
                  variant="outlined"
                  color="secondary"
                  onClick={handleUnfriend}
                  fullWidth
                >
                  Unfriend
                </Button>
              ) : hasSentRequest ? (
                <Typography variant="body2" color="textSecondary">
                  Friend Request Sent
                </Typography>
              ) : (
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleSendFriendRequest}
                  fullWidth
                >
                  Send Friend Request
                </Button>
              )}
            </ActionButtons>

            <Button
              variant={isFollowing ? "outlined" : "contained"}
              color="primary"
              onClick={handleFollow}
              fullWidth
            >
              {isFollowing ? "Unfollow" : "Follow"}
            </Button>
          </CardContent>
        </ProfileCard>
      ) : (
        <CircularProgress />
      )}
    </ProfileContainer>
  );
};

export default UserProfile;