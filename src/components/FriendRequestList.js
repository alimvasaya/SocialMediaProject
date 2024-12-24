import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  Snackbar,
  Alert
} from "@mui/material";
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';

const FriendRequestList = () => {
  const [requests, setRequests] = useState([]);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");

  useEffect(() => {
    const fetchFriendRequests = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/friends/requests", {
          withCredentials: true,
        });
        setRequests(response.data);
      } catch (error) {
        console.error("Error fetching friend requests:", error);
      }
    };
    fetchFriendRequests();
  }, []);

  const handleAccept = async (requestId) => {
    try {
      await axios.post("http://localhost:8080/api/friends/accept", { senderId: requestId }, {
        withCredentials: true,
      });
      setRequests(requests.filter((req) => req.id !== requestId)); // Remove from list
      setSnackbarMessage("Friend request accepted!");
      setSnackbarSeverity("success");
      setOpenSnackbar(true);
    } catch (error) {
      console.error("Error accepting friend request:", error);
      setSnackbarMessage("Failed to accept friend request.");
      setSnackbarSeverity("error");
      setOpenSnackbar(true);
    }
  };

  const handleDecline = async (requestId) => {
    try {
      await axios.post("http://localhost:8080/api/friends/decline", { senderId: requestId }, {
        withCredentials: true,
      });
      setRequests(requests.filter((req) => req.id !== requestId)); // Remove from list
      setSnackbarMessage("Friend request declined.");
      setSnackbarSeverity("info");
      setOpenSnackbar(true);
    } catch (error) {
      console.error("Error declining friend request:", error);
      setSnackbarMessage("Failed to decline friend request.");
      setSnackbarSeverity("error");
      setOpenSnackbar(true);
    }
  };

  return (
    <Container maxWidth="sm">
      <Typography variant="h4" gutterBottom align="center">
        Friend Requests
      </Typography>
      <Grid container spacing={2}>
        {requests.length > 0 ? (
          requests.map((request) => (
            <Grid item xs={12} key={request.id}>
              <Card variant="outlined">
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Typography variant="h6">{request.senderName}</Typography>
                    <Box>
                      <Button
                        variant="contained"
                        color="success"
                        startIcon={<CheckIcon />}
                        onClick={() => handleAccept(request.id)}
                        style={{ marginRight: '8px' }}
                      >
                        Accept
                      </Button>
                      <Button
                        variant="outlined"
                        color="error"
                        startIcon={<CloseIcon />}
                        onClick={() => handleDecline(request.id)}
                      >
                        Decline
                      </Button>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))
        ) : (
          <Grid item xs={12}>
            <Typography variant="body1" color="textSecondary" align="center">
              No friend requests at the moment.
            </Typography>
          </Grid>
        )}
      </Grid>

      {/* Snackbar for feedback */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={4000}
        onClose={() => setOpenSnackbar(false)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert
          onClose={() => setOpenSnackbar(false)}
          severity={snackbarSeverity}
          sx={{ width: '100%' }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default FriendRequestList;