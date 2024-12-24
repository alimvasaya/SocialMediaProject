import React from "react";
import { Formik, Form, Field } from "formik";
import { useNavigate } from "react-router-dom";
import * as Yup from "yup";
import axios from "axios";
import { Box, TextField, Button, Typography, Container } from "@mui/material";
import Cookies from "js-cookie";
const Register = ({ onLogin }) => {
  const navigate = useNavigate();

  const initialValues = {
    email: "",
    password: "",
  };

  const validationSchema = Yup.object({
    email: Yup.string()
      .email("Invalid email format")
      .required("Email is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required"),
  });

  const handleSubmit = async (values, { setSubmitting, setFieldError }) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/user/register",
        values,
        { withCredentials: true } // This ensures that the JWT token is sent with the request
      );
      Cookies.set("jwt", response.data.token, { expires: 7, path: "/", secure: false, sameSite: "None" });
      // If successful, the token is set in the cookies automatically
      onLogin(response.data.token); // Call onLogin to update the state and handle the login process
      navigate("/profile-details");
      window.location.reload();
      alert("Registration successful!");
    } catch (error) {
      const serverError = error.response?.data || error.message;
      if (serverError.email) {
        setFieldError("email", serverError.email);
      } else if (serverError.password) {
        setFieldError("password", serverError.password);
      } else {
        alert("Registration failed. Please try again.");
      }
    } finally {
      setSubmitting(false);
    }
  };


  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
        backgroundColor: "#f0f0f0",
      }}
    >
      <Container
        maxWidth="xs"
        sx={{
          backgroundColor: "white",
          padding: 4,
          borderRadius: 2,
          boxShadow: 3,
        }}
      >
        <Typography
          variant="h3"
          component="h1"
          sx={{
            textAlign: "center",
            fontWeight: "bold",
            mb: 4,
            color: "#333",
          }}
        >
          NOSEY
        </Typography>
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          {({ errors, touched, isSubmitting }) => (
            <Form>
              <Box
                display="flex"
                flexDirection="column"
                alignItems="center"
                gap={2}
              >
                <Field
                  as={TextField}
                  label="Email"
                  name="email"
                  fullWidth
                  error={touched.email && Boolean(errors.email)}
                  helperText={touched.email && errors.email}
                />
                <Field
                  as={TextField}
                  label="Password"
                  name="password"
                  type="password"
                  fullWidth
                  error={touched.password && Boolean(errors.password)}
                  helperText={touched.password && errors.password}
                />
                <Button
                  type="submit"
                  variant="contained"
                  color="primary"
                  fullWidth
                  disabled={isSubmitting}
                >
                  Register
                </Button>
                <Button
                  variant="outlined"
                  color="secondary"
                  fullWidth
                  onClick={() => navigate("/login")}
                >
                  Login
                </Button>
              </Box>
            </Form>
          )}
        </Formik>
      </Container>
    </Box>
  );
};

export default Register;