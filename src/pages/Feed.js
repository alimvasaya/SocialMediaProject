import React, { useState, useEffect } from "react";
import axios from "axios";
import Post from "./Post";
import CreatePost from "./CreatePost";
import Search from "./Search";
import { Button, Container, Typography, Grid, Card, CardContent, CircularProgress } from "@mui/material";

const Feed = ({ token }) => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFeed = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/posts/feed", {
          withCredentials: true,
        });
        setPosts(response.data);
      } catch (error) {
        console.error("Error fetching feed:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchFeed();
  }, [token]);

  // Add a new post to the feed
  const addPost = (newPost) => {
    setPosts([newPost, ...posts]);
  };

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        Feed
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <CreatePost token={token} addPost={addPost} />
        </Grid>

        {loading ? (
          <CircularProgress />
        ) : (
          posts.map((post) => (
            <Grid item xs={12} sm={6} md={4} key={post.id}>
              <Card>
                <CardContent>
                  <Post post={post} />
                </CardContent>
              </Card>
            </Grid>
          ))
        )}
      </Grid>
    </Container>
  );
};

export default Feed;