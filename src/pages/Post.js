import React, { useEffect, useState } from "react";
import { Card, CardContent, CardActions, Button, Typography, TextField } from "@mui/material";
import axios from "axios";

const Post = ({ post, userId }) => {
    const [likesCount, setLikesCount] = useState(0);
    const [reactions, setReactions] = useState([]);
    const [hasLiked, setHasLiked] = useState(false);
    const [commentContent, setCommentContent] = useState(""); // For managing new comment text
    const [comments, setComments] = useState([]); // To store list of comments

    useEffect(() => {
        const fetchLikesInfo = async () => {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/reactions/post/${post.id}`,
                    { withCredentials: true }
                );
                setLikesCount(response.data.likeCount || 0);
                setReactions(response.data.reactions || []);
                const userHasLiked = response.data.reactions.some((reaction) => reaction.userId === userId);
                setHasLiked(userHasLiked);
            } catch (error) {
                console.error("Error fetching likes info:", error);
            }
        };

        const fetchComments = async () => {
          try {
              const response = await axios.get(`http://localhost:8080/api/comments/post/${post.id}`);
              setComments(response.data); // Assuming the response directly contains the list of comments
          } catch (error) {
              console.error("Error fetching comments:", error);
          }
      };

        fetchLikesInfo();
        fetchComments();
    }, [post.id, post.userId]);

    const handleLike = async () => {
        const originalState = hasLiked;
        try {
            setHasLiked(!hasLiked); // Optimistic UI update
            setLikesCount(likesCount + (hasLiked ? -1 : 1)); // Adjust count optimistically

            const reactionType = hasLiked ? "UNLIKE" : "LIKE";
            const response = await axios.post(
                "http://localhost:8080/api/reactions",
                { postId: post.id, userId: post.userId, reactionType },
                { withCredentials: true }
            );

            // Revert state if the API fails
            setHasLiked(response.data.isLiked);
            setLikesCount(response.data.likeCount);
        } catch (error) {
            console.error("Error toggling reaction:", error.response?.data || error.message);
            setHasLiked(originalState); // Revert optimistic update
            setLikesCount(likesCount + (hasLiked ? 1 : -1)); // Revert count
        }
    };

    const handleCommentSubmit = async () => {
        if (!commentContent.trim()) {
            alert("Please enter a comment.");
            return;
        }

        try {
            const response = await axios.post(
                "http://localhost:8080/api/comments",
                { postId: post.id, userId: userId, content: commentContent },
                { withCredentials: true }
            );
            setComments((prevComments) => [...prevComments, response.data]); // Add the new comment
            setCommentContent(""); // Clear the comment input field
        } catch (error) {
            console.error("Error posting comment:", error);
        }
    };

    return (
        <Card sx={{ marginBottom: 2 }}>
            <CardContent>
                <Typography variant="h6">{post.title}</Typography>
                <Typography variant="body2" color="text.secondary">
                    By {post.firstName} {post.lastName}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    {post.content}
                </Typography>
                {post.mediaUrl && <img src={post.mediaUrl} alt="Post media" style={{ width: "100%" }} />}
                <Typography variant="body2" color="text.secondary">
                    Likes: {likesCount}
                </Typography>
            </CardContent>
            <CardActions>
                <Button size="small" color={hasLiked ? "secondary" : "primary"} onClick={handleLike}>
                    {hasLiked ? "Unlike" : "Like"} ({likesCount})
                </Button>
                <Button size="small" color="secondary" onClick={handleCommentSubmit}>
                    Comment
                </Button>
            </CardActions>

            {/* Comment section */}
            <CardContent>
                <TextField
                    fullWidth
                    multiline
                    rows={3}
                    variant="outlined"
                    placeholder="Write a comment..."
                    value={commentContent}
                    onChange={(e) => setCommentContent(e.target.value)}
                    sx={{ marginBottom: 2 }}
                />
                <Button variant="contained" color="primary" onClick={handleCommentSubmit}>
                    Submit Comment
                </Button>

                {/* Display comments */}
                <div style={{ marginTop: "20px" }}>
                    {comments.map((comment) => (
                        <div key={comment.id} style={{ marginBottom: "10px" }}>
                            <Typography variant="body2" color="text.primary">
                                <strong>{comment.userName}</strong>: {comment.content}
                            </Typography>
                        </div>
                    ))}
                </div>
            </CardContent>
        </Card>
    );
};

export default Post;