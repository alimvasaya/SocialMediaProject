import React, { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import {
  Container,
  TextField,
  Button,
  Grid,
  Card,
  CardContent,
  Typography,
  Box,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";

const Search = () => {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState({ users: [], posts: [] });
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    if (!query.trim()) return; // Prevent empty searches
    setLoading(true);
    try {
      const response = await axios.get(
        `http://localhost:8080/api/search?query=${query}`,
        { withCredentials: true }
      );
      console.log(response.data); // Log the response to see its structure
      setResults(response.data);
    } catch (error) {
      console.error("Error searching:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md">
      <Box display="flex" justifyContent="center" alignItems="center" mb={4}>
        <TextField
          label="Search users or posts"
          variant="outlined"
          fullWidth
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleSearch()}
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleSearch}
          sx={{ marginLeft: 2 }}
          disabled={loading}
          startIcon={<SearchIcon />}
        >
          Search
        </Button>
      </Box>

      {loading && <Typography align="center">Searching...</Typography>}

      <div className="search-results">
        <Typography variant="h4" gutterBottom>
          Search Results
        </Typography>

        {/* Users Section */}
        <Typography variant="h5" gutterBottom>
          Users
        </Typography>
        <Grid container spacing={2}>
          {results.users.length > 0 ? (
            results.users.map((user) => (
              <Grid item xs={12} sm={6} md={4} key={user.userid}>
                <Card variant="outlined">
                  <CardContent>
                    <Typography variant="h6">
                      <Link to={`/user/${user.userid}`}>{user.firstName} {user.lastName}</Link>
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {user.email}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))
          ) : (
            <Grid item xs={12}>
              <Typography>No users found</Typography>
            </Grid>
          )}
        </Grid>

        {/* Posts Section */}
        <Typography variant="h5" gutterBottom>
          Posts
        </Typography>
        <Grid container spacing={2}>
          {results.posts.length > 0 ? (
            results.posts.map((post) => (
              <Grid item xs={12} sm={6} md={4} key={post.id}>
                <Card variant="outlined">
                  <CardContent>
                    <Typography variant="h6">
                      <Link to={`/post/${post.id}`}>{post.title}</Link>
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {post.content.substring(0, 100)}...
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))
          ) : (
            <Grid item xs={12}>
              <Typography>No posts found</Typography>
            </Grid>
          )}
        </Grid>
      </div>
    </Container>
  );
};

export default Search;