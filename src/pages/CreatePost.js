import React, { useState } from "react";
import axios from "axios";
import { TextField, Button, Grid, Box, Typography, Input, Collapse } from "@mui/material";

const CreatePost = ({ addPost }) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [media, setMedia] = useState(null); // Handle media files
  const [showCreatePostForm, setShowCreatePostForm] = useState(false); // Toggle form visibility

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      formData.append("title", title);
      formData.append("content", content);
      if (media) formData.append("media", media); // Append media if present
      const response = await axios.post(
        "http://localhost:8080/api/posts",
        formData,
        {
          withCredentials: true, // Include cookies
        }
      );

      addPost(response.data); // Update the posts list
      setTitle("");
      setContent("");
      setMedia(null);
    } catch (error) {
      console.error("Error creating post:", error);
    }
  };

  return (
    <Box sx={{ padding: 2 }}>
      <Button
        variant="contained"
        color="primary"
        onClick={() => setShowCreatePostForm(!showCreatePostForm)}
        fullWidth
        sx={{ marginBottom: 2 }}
      >
        {showCreatePostForm ? "Cancel" : "Create Post"}
      </Button>

      {/* Toggleable Post Creation Form */}
      <Collapse in={showCreatePostForm} timeout="auto" unmountOnExit>
        <Typography variant="h6" gutterBottom>
          Create a Post
        </Typography>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Title"
                variant="outlined"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="What's on your mind?"
                variant="outlined"
                multiline
                rows={4}
                value={content}
                onChange={(e) => setContent(e.target.value)}
                required
              />
            </Grid>

            {/* File input for media */}
            <Grid item xs={12}>
              <Input
                type="file"
                accept="image/*,video/*"
                onChange={(e) => setMedia(e.target.files[0])}
                fullWidth
              />
            </Grid>

            <Grid item xs={12}>
              <Button variant="contained" color="primary" type="submit" fullWidth>
                Post
              </Button>
            </Grid>
          </Grid>
        </form>
      </Collapse>
    </Box>
  );
};

export default CreatePost;