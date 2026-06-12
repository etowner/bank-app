import { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";
import { changeUsername } from "../../../api/userApi";

export default function ChangeUsername({ onClose, onSuccess }: { onClose: () => void; onSuccess?: () => void }) {

  const [currentPassword, setCurrentPassword] = useState("");
  const [newUsername, setNewUsername] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChangeUsername = async (e: React.FormEvent<HTMLFormElement>) => {
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
      console.log("Changing username with:", { currentPassword, newUsername });
      await changeUsername(currentPassword, newUsername);
      setSuccess("Username changed successfully! Please log in again with your new username.");
      setCurrentPassword("");
      setNewUsername("");
      setTimeout(() => {
        if (onSuccess) onSuccess();
      }, 2000);
    } catch (err: any) {
      if (err.response?.status === 401) {
        setError("Current password is incorrect");
        console.error("Incorrect password:", err.response ? err.response : err.request ? err.request : err.message);
      } else if (err.response?.status === 400) {
        setError(err.response?.data || "Invalid new username or username already exists");
        console.error("Invalid username or password:", err.response ? err.response : err.request ? err.request : err.message);
      } else {
        setError(err.response?.data || "Failed to change username");
        console.error("Failed to change username:", err.response ? err.response : err.request ? err.request : err.message);
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

