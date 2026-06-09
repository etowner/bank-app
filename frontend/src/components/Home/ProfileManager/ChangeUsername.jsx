import React, { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";
import { changeUsername } from "../../../api/userApi";
import PropTypes from "prop-types";

export default function ChangeUsername({ onClose, onSuccess }) {
  ChangeUsername.propTypes = {
    onClose: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired,
  };
  const [currentPassword, setCurrentPassword] = useState("");
  const [newUsername, setNewUsername] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChangeUsername = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!currentPassword) {
      setError("Please enter your current password");
      return;
    }

    if (!newUsername) {
      setError("Please enter a new username");
      return;
    }

    if (newUsername.length < 3) {
      setError("Username must be at least 3 characters");
      return;
    }

    if (!/^[a-zA-Z0-9_-]+$/.test(newUsername)) {
      setError("Username can only contain letters, numbers, underscores, and hyphens");
      return;
    }

    setLoading(true);
    try {
      await changeUsername(currentPassword, newUsername);
      setSuccess("Username changed successfully! Please log in again with your new username.");
      setCurrentPassword("");
      setNewUsername("");
      setTimeout(() => {
        if (onSuccess) onSuccess();
      }, 2000);
    } catch (err) {
      if (err.response?.status === 401) {
        setError("Current password is incorrect");
      } else if (err.response?.status === 400) {
        setError(err.response?.data || "Invalid new username or username already exists");
      } else {
        setError(err.response?.data || "Failed to change username");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      <Form onSubmit={handleChangeUsername}>
        <Form.Group className="mb-3" controlId="currentPassword">
          <Form.Label>Current Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Enter your current password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            disabled={loading}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="newUsername">
          <Form.Label>New Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter new username"
            value={newUsername}
            onChange={(e) => setNewUsername(e.target.value)}
            disabled={loading}
          />
          <Form.Text className="text-muted">
            3+ characters. Letters, numbers, underscores, and hyphens only.
          </Form.Text>
        </Form.Group>

        <div className="d-flex gap-2">
          <Button 
            variant="secondary" 
            onClick={onClose}
            disabled={loading}
          >
            Cancel
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? "Changing..." : "Change Username"}
          </Button>
        </div>
      </Form>
    </div>
  );
}

