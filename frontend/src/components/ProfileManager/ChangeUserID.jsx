import React, { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";
import api from "../../api/axiosConfig";

export default function ChangeUserID({ onClose, onSuccess }) {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newUserID, setNewUserID] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChangeUserID = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!currentPassword) {
      setError("Please enter your current password");
      return;
    }

    if (!newUserID) {
      setError("Please enter a new userID");
      return;
    }

    if (newUserID.length < 3) {
      setError("UserID must be at least 3 characters");
      return;
    }

    if (!/^[a-zA-Z0-9_-]+$/.test(newUserID)) {
      setError("UserID can only contain letters, numbers, underscores, and hyphens");
      return;
    }

    setLoading(true);
    try {
      await api.put(`/api/v1/user/change-userid`, {
        currentPassword,
        newValue: newUserID
      });
      setSuccess("UserID changed successfully! Please log in again with your new userID.");
      setCurrentPassword("");
      setNewUserID("");
      setTimeout(() => {
        if (onSuccess) onSuccess();
      }, 2000);
    } catch (err) {
      if (err.response?.status === 401) {
        setError("Current password is incorrect");
      } else if (err.response?.status === 400) {
        setError(err.response?.data || "Invalid new userID or userID already exists");
      } else {
        setError(err.response?.data || "Failed to change userID");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      <Form onSubmit={handleChangeUserID}>
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

        <Form.Group className="mb-3" controlId="newUserID">
          <Form.Label>New UserID</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter new userID"
            value={newUserID}
            onChange={(e) => setNewUserID(e.target.value)}
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
            {loading ? "Changing..." : "Change UserID"}
          </Button>
        </div>
      </Form>
    </div>
  );
}

